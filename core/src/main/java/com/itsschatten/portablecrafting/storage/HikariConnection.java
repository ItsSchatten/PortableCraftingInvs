package com.itsschatten.portablecrafting.storage;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.virtual.Storage;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.entity.Player;
import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * This class is modeled after <a href="https://github.com/LuckPerms/LuckPerms/blob/5c1ea5633ed5986c02d3fa1ccf187638d143c091/common/src/main/java/me/lucko/luckperms/common/storage/misc/StorageCredentials.java#L31">LuckPerm's Storage Credentials</a>
 * and <a href="https://github.com/LuckPerms/LuckPerms/blob/5c1ea5633ed5986c02d3fa1ccf187638d143c091/common/src/main/java/me/lucko/luckperms/common/storage/implementation/sql/connection/hikari/HikariConnectionFactory.java#L48">LuckPerm's HikariConnectionFactory</a>.
 * <br/>
 * Responsible for initializing connections with {@link HikariDataSource Hikari data sources}, also stores constants for SQL statements.
 */
public abstract class HikariConnection implements Storage {

    @Language("SQL")
    public static final String COUNT_UNIQUE = "SELECT COUNT(DISTINCT `owner`) AS 'count' FROM `{prefix}brewing_stand_storage` UNION SELECT COUNT(DISTINCT `owner`) FROM `{prefix}furnace_storage`".replace("{prefix}", Settings.DATABASE_PREFIX);

    @Language("SQL")
    public static final String COUNT_FURNACE = "SELECT COUNT(`owner`) AS `count` FROM `{prefix}furnace_storage` WHERE `owner` = ? AND `type` = ?".replace("{prefix}", Settings.DATABASE_PREFIX);

    @Language("SQL")
    public static final String COUNT_BREWING = "SELECT COUNT(`owner`) AS `count` FROM `{prefix}brewing_stand_storage` WHERE `owner` = ?".replace("{prefix}", Settings.DATABASE_PREFIX);

    @Language("SQL")
    public static final String CHECK_FURNACE_EXISTS = "SELECT 1 FROM `{prefix}furnace_storage`".replace("{prefix}", Settings.DATABASE_PREFIX) +
            "WHERE `uuid` = ? AND `owner` = ?";

    @Language("SQL")
    public static final String CHECK_STAND_EXISTS = "SELECT 1 FROM `{prefix}brewing_stand_storage`".replace("{prefix}", Settings.DATABASE_PREFIX) +
            "WHERE `uuid` = ? AND `owner` = ?";

    @Language("SQL")
    public static final String GET_FURNACE = "SELECT * FROM `{prefix}furnace_storage` WHERE `owner` = ? AND `uuid` = ?".replace("{prefix}", Settings.DATABASE_PREFIX);

    @Language("SQL")
    public static final String GET_STAND = "SELECT * FROM `{prefix}brewing_stand_storage` WHERE `owner` = ? AND `uuid` = ?".replace("{prefix}", Settings.DATABASE_PREFIX);

    @Language("SQL")
    public static final String SAVE_FURNACE = "INSERT INTO `{prefix}furnace_storage` ".replace("{prefix}", Settings.DATABASE_PREFIX) +
            "(`uuid`, `owner`, `last_opened`, `type`, `cook_time`,`fuel_time`,`total_fuel`,`experience`,`fuel_item`,`input_item`,`output_item`, `title`) VALUES " +
            "(?,?,?,?,?,?,?,?,?,?,?,?)";

    @Language("SQL")
    public static final String UPDATE_FURNACE = "UPDATE `{prefix}furnace_storage`".replace("{prefix}", Settings.DATABASE_PREFIX) +
            "SET `last_opened` = ?, `cook_time` = ?," +
            "`fuel_time` = ?, `total_fuel`= ?, `experience` = ?," +
            "`fuel_item` = ?, `input_item` = ?,`output_item` = ?, " +
            "`title` = ? WHERE `uuid` = ? AND `owner` = ?";

    @Language("SQL")
    public static final String SAVE_STAND = "INSERT INTO `{prefix}brewing_stand_storage`".replace("{prefix}", Settings.DATABASE_PREFIX) +
            "(`uuid`, `owner`, `last_opened`, `fuel_time`, `brew_time`, `max_brews`, `fuel_item`, `ingredient`, `bottle_1`,`bottle_2`,`bottle_3`,`title`) VALUES" +
            "(?,?,?,?,?,?,?,?,?,?,?,?)";

    @Language("SQL")
    public static final String UPDATE_STAND = "UPDATE `{prefix}brewing_stand_storage`".replace("{prefix}", Settings.DATABASE_PREFIX) +
            "SET `last_opened` = ?, `fuel_time` = ?, `brew_time` = ?," +
            "`max_brews` = ?, `fuel_item` = ?,`ingredient` = ?, " +
            "`bottle_1` = ?, `bottle_2` = ?, `bottle_3` = ?, " +
            "`title` = ? WHERE `uuid` = ? AND `owner` = ?";

    @Language("SQL")
    public static final String GET_FURNACE_FROM_OWNER = "SELECT `id`,`uuid` FROM `{prefix}furnace_storage` WHERE `owner` = ? AND `type` = ? ORDER BY `id`".replace("{prefix}", Settings.DATABASE_PREFIX);

    @Language("SQL")
    public static final String GET_STAND_UUID_FROM_OWNER = "SELECT `id`,`uuid` FROM `{prefix}brewing_stand_storage` WHERE `owner` = ? ORDER BY `id`".replace("{prefix}", Settings.DATABASE_PREFIX);

    // Variables.
    private final String address;
    private final String database;
    private final String username;
    private final String password;

    private final int maximumPoolSize;
    private final int minimumIdleConnections;
    private final int maxLifetime;
    private final int keepAliveTimeout;
    private final int connectionTimeout;

    private final Map<String, Object> properties;

    public HikariDataSource dataSource;

    /**
     * Constructs a new {@link HikariConfig} connection pool.
     *
     * @param address                The address of the SQL server.
     * @param database               The database for our tables.
     * @param username               The username to access the SQL server.
     * @param password               The password to access the SQL server.
     * @param maximumPoolSize        The maximum size of the connection pool.
     * @param minimumIdleConnections The minimum idle connections in the pool.
     * @param maxLifetime            The maximum lifetime of a connection.
     * @param keepAliveTimeout       The timeout of keep alive.
     * @param connectionTimeout      The connection timeout.
     * @param properties             Additional properties to apply to the config.
     */
    public HikariConnection(String address, String database, String username, String password,
                            int maximumPoolSize, int minimumIdleConnections, int maxLifetime, int keepAliveTimeout, int connectionTimeout,
                            @NotNull Map<String, Object> properties) {
        this.address = address;
        this.database = database;
        this.username = username;
        this.password = password;

        this.maximumPoolSize = maximumPoolSize;
        this.minimumIdleConnections = minimumIdleConnections;
        this.maxLifetime = maxLifetime;
        this.keepAliveTimeout = keepAliveTimeout;
        this.connectionTimeout = connectionTimeout;

        this.properties = properties;
    }

    /**
     * Initializes the {@link HikariConfig} and {@link HikariDataSource}.
     */
    public final void init() {
        HikariConfig config;
        try {
            config = new HikariConfig();
        } catch (LinkageError er) {
            Utils.logError(er);
            Utils.logError("Failed to create a hikari config!", "https://github.com/ItsSchatten/PortableCraftingInvs/issues/new?template=bug_report.yml&labels=labels=type%3A+bug%2Cpriority%3A+normal%2Cstatus%3A+awaiting+confirmation");
            return;
        }

        config.setPoolName("portable-crafting-invs-hikari");

        configure(config);

        overrideProperties(properties);

        if (!properties.isEmpty()) {
            properties.forEach(config::addDataSourceProperty);
        }

        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdleConnections);
        config.setMaxLifetime(maxLifetime);
        config.setConnectionTimeout(connectionTimeout);
        config.setKeepaliveTime(keepAliveTimeout);

        config.setInitializationFailTimeout(-1);

        this.dataSource = new HikariDataSource(config);

        postInit();
        deregisterDriver(driverClass());
    }

    @Override
    public int getPlayerBrewingStandCount(Player player) {
        return getCount(player, COUNT_BREWING, null);
    }

    @Override
    public int getPlayerFurnaceCount(Player player, final FurnaceType type) {
        return getCount(player, COUNT_FURNACE, type);
    }

    // Utility method to get the count from the database.
    private int getCount(Player player, String statementString, final FurnaceType type) {
        final CompletableFuture<Integer> countFurnaces = CompletableFuture.supplyAsync(() -> {
            try (final Connection connection = getConnection();
                 final PreparedStatement statement = connection.prepareStatement(statementString)) {
                statement.setString(1, player.getUniqueId().toString());
                if (type != null) {
                    statement.setString(2, type.name());
                }

                try (final ResultSet set = statement.executeQuery()) {
                    if (set.next()) {
                        return set.getInt(1);
                    } else {
                        return 0;
                    }

                }
            } catch (SQLException e) {
                Utils.logError(e);
                return 0;
            }
        });

        try {
            return countFurnaces.get();
        } catch (InterruptedException | ExecutionException e) {
            Utils.logError(e);
            return 0;
        }
    }

    @Override
    public int getUniquePlayers() {
        final CompletableFuture<Integer> countPlayers = CompletableFuture.supplyAsync(() -> {
            try (final Connection connection = getConnection();
                 final PreparedStatement statement = connection.prepareStatement(COUNT_UNIQUE);
                 final ResultSet set = statement.executeQuery()) {
                if (set.next()) {
                    return set.getInt(1);
                } else {
                    return 0;
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            return countPlayers.get();
        } catch (InterruptedException | ExecutionException e) {
            Utils.logError(e);
            return 0;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void shutdown() {
        if (this.dataSource != null) {
            this.dataSource.close();
        }
    }

    /**
     * Obtains a {@link Connection} from the {@link com.zaxxer.hikari.pool.HikariPool connection pool}.
     *
     * @return Returns the obtained {@link Connection}.
     * @throws SQLException Thrown if the {@link #dataSource} is {@code null} or {@link Connection} is {@code null}.
     */
    public final @NotNull Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("Failed to get a connection from Hikari. (dataSource is null?)");
        }

        final Connection connection = dataSource.getConnection();
        if (connection == null) {
            throw new SQLException("Unable to get a connection from Hikari. (dataSource.getConnection() returned null?)");
        }

        return connection;
    }

    /**
     * Executed after the config and datasource are initialized.
     */
    protected void postInit() {

        try (Connection connection = getConnection()) {
            // Creates the furnace storage table.
            try (final PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS {prefix}furnace_storage (".replace("{prefix}", Settings.DATABASE_PREFIX) +
                            "`id` int NOT NULL AUTO_INCREMENT," +
                            "`uuid` VARCHAR(36) NOT NULL," +
                            "`owner` VARCHAR(36) NOT NULL," +
                            "`last_opened` DATETIME NOT NULL DEFAULT current_timestamp()," +
                            "`type` ENUM('BLASTING','FURNACE','SMOKER') NOT NULL DEFAULT 'FURNACE'," +
                            "`cook_time` INT," +
                            "`fuel_time` INT ," +
                            "`total_fuel` INT ," +
                            "`experience` FLOAT(2)," +
                            "`fuel_item` TEXT," +
                            "`input_item` TEXT," +
                            "`output_item` TEXT," +
                            "`title` TEXT," +
                            "PRIMARY KEY (`id`)," +
                            "UNIQUE INDEX `uuid_owner` (`uuid`, `owner`)) DEFAULT CHARSET = utf8mb4")) {
                statement.execute();
            }

            // Creates the brewing stand storage table.
            try (final PreparedStatement statement = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS {prefix}brewing_stand_storage (".replace("{prefix}", Settings.DATABASE_PREFIX) +
                            "`id` int NOT NULL AUTO_INCREMENT," +
                            "`uuid` VARCHAR(36) NOT NULL," +
                            "`owner` VARCHAR(36) NOT NULL," +
                            "`last_opened` DATETIME NOT NULL DEFAULT current_timestamp()," +
                            "`brew_time` INT," +
                            "`fuel_time` INT ," +
                            "`max_brews` INT," +
                            "`fuel_item` TEXT," +
                            "`ingredient` TEXT," +
                            "`bottle_1` TEXT," +
                            "`bottle_2` TEXT," +
                            "`bottle_3` TEXT," +
                            "`title` TEXT," +
                            "PRIMARY KEY (`id`)," +
                            "UNIQUE INDEX `uuid_owner` (`uuid`, `owner`)) DEFAULT CHARSET = utf8mb4")) {
                statement.execute();
            }
        } catch (SQLException e) {
            Utils.logError(e);
            Utils.logError("Failed to create the tables!");
        }
    }

    /**
     * Configures the {@link HikariConfig}.
     *
     * @param config The config to configure.
     */
    protected void configure(@NotNull HikariConfig config) {
        final String[] addressSplit = getAddress().split(":");
        final String address = addressSplit[0];
        final String port = addressSplit.length > 1 ? addressSplit[1] : defaultPort();

        config.setDriverClassName(driverClass());
        config.setJdbcUrl(String.format("jdbc:%s://%s:%s/%s", jdbcId(), address, port, getDatabase()));
        config.setUsername(getUsername());
        config.setPassword(getPassword());
    }

    /**
     * Overrides and properties if required.
     *
     * @param properties The properties to override.
     */
    protected void overrideProperties(@NotNull Map<String, Object> properties) {
        // https://github.com/brettwooldridge/HikariCP/wiki/Rapid-Recovery
        properties.putIfAbsent("socketTimeout", String.valueOf(TimeUnit.SECONDS.toMillis(30)));
    }

    /**
     * @return The default port of this connection.
     */
    protected abstract String defaultPort();

    /**
     * @return The driver class for this connection.
     */
    protected abstract String driverClass();

    /**
     * @return The ID to use in JDBC connection URL.
     */
    protected abstract String jdbcId();

    /**
     * @return Gets the address, errors if null.
     */
    public final String getAddress() {
        return Objects.requireNonNull(this.address, "'address' cannot be null");
    }

    /**
     * @return Gets the database, errors if null.
     */
    public final String getDatabase() {
        return Objects.requireNonNull(this.database, "'database' cannot be null");
    }

    /**
     * @return Gets the username, errors if null.
     */
    public final String getUsername() {
        return Objects.requireNonNull(this.username, "'username' cannot be null");
    }

    /**
     * @return Gets the password, errors if null.
     */
    public final String getPassword() {
        return Objects.requireNonNull(this.password, "'password' cannot be null");
    }

    // Removes the registered driver from the DriverManager.
    private static void deregisterDriver(final String driverClassName) {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            if (driver.getClass().getName().equals(driverClassName)) {
                try {
                    DriverManager.deregisterDriver(driver);
                } catch (SQLException ignored) {
                    // ignore
                }
            }
        }
    }
}
