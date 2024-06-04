package com.itsschatten.portablecrafting.storage.implementations.sql;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.storage.HikariConnection;
import com.itsschatten.portablecrafting.storage.StorageCredentials;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import com.itsschatten.portablecrafting.virtual.utils.FurnaceType;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class MySQLStorage extends HikariConnection {

    public MySQLStorage(@NotNull StorageCredentials credentials) {
        this(credentials.address(), credentials.database(), credentials.username(), credentials.password(),
                credentials.maxPool(), credentials.minIdle(), credentials.maxLife(), credentials.keepAlive(),
                credentials.connectionTimeout(), credentials.properties());
    }

    public MySQLStorage(String address, String database, String username, String password,
                        int maximumPoolSize, int minimumIdleConnections, int maxLifetime, int keepAliveTimeout, int connectionTimeout,
                        @NotNull Map<String, Object> properties) {
        super(address, database, username, password, maximumPoolSize, minimumIdleConnections, maxLifetime, keepAliveTimeout, connectionTimeout, properties);
    }

    @Override
    protected String defaultPort() {
        return "3306";
    }

    @Override
    protected String driverClass() {
        return "com.mysql.cj.jdbc.Driver";
    }

    @Override
    protected String jdbcId() {
        return "mysql";
    }

    @Override
    protected void overrideProperties(@NotNull Map<String, Object> properties) {
        // https://github.com/brettwooldridge/HikariCP/wiki/MySQL-Configuration
        properties.putIfAbsent("cachePrepStmts", "true");
        properties.putIfAbsent("prepStmtCacheSize", "250");
        properties.putIfAbsent("prepStmtCacheSqlLimit", "2048");
        properties.putIfAbsent("useServerPrepStmts", "true");
        properties.putIfAbsent("useLocalSessionState", "true");
        properties.putIfAbsent("rewriteBatchedStatements", "true");
        properties.putIfAbsent("cacheResultSetMetadata", "true");
        properties.putIfAbsent("cacheServerConfiguration", "true");
        properties.putIfAbsent("elideSetAutoCommits", "true");
        properties.putIfAbsent("maintainTimeStats", "false");
        properties.putIfAbsent("alwaysSendSetIsolation", "false");
        properties.putIfAbsent("cacheCallableStmts", "true");

        // https://stackoverflow.com/a/54256150
        // It's not super important which timezone we pick, because we don't use time-based
        // data types in any of our schemas/queries.
        properties.putIfAbsent("serverTimezone", "UTC");

        super.overrideProperties(properties);
    }

    @Override
    public String implementationName() {
        return "MySQL";
    }

    @Override
    public void saveFurnace(final UUID uuid, final Furnace furnace) {
        if (furnace == null) return;

        // Must be run async, it will be a blocking request.
        Bukkit.getScheduler().runTaskAsynchronously(Utils.getInstance(), () -> {
            try (final Connection connection = getConnection();
                 final PreparedStatement statement = connection.prepareStatement(CHECK_FURNACE_EXISTS)) {
                // Set player and furnace UUIDs.
                statement.setString(1, furnace.getUniqueId().toString());
                statement.setString(2, uuid.toString());

                // Serialize the BrewingStand for SQL.
                final Map<String, Object> data = new HashMap<>(furnace.serializeForSQL());

                try (final ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        try (final PreparedStatement insertStatement = connection.prepareStatement(SAVE_FURNACE)) {
                            // Set player and furnace UUIDs.
                            insertStatement.setString(1, furnace.getUniqueId().toString());
                            insertStatement.setString(2, uuid.toString());

                            // Set the new open time to now.
                            insertStatement.setTimestamp(3, Timestamp.from(Instant.now()));

                            insertStatement.setString(4, furnace.getType().name());

                            // Timings.
                            insertStatement.setInt(5, (Integer) data.get("cook_time"));
                            insertStatement.setInt(6, (Integer) data.get("fuel_time"));
                            insertStatement.setInt(7, (Integer) data.get("total_fuel"));
                            insertStatement.setFloat(8, (Float) data.get("experience"));

                            // ItemStacks.
                            insertStatement.setString(9, (String) data.get("fuel_item"));
                            insertStatement.setString(10, (String) data.get("input_item"));
                            insertStatement.setString(11, (String) data.get("output_item"));
                            insertStatement.setString(12, (String) data.get("title"));

                            // Execute the statement.
                            insertStatement.executeUpdate();
                        }
                    } else {
                        try (final PreparedStatement updateStatement = connection.prepareStatement(UPDATE_FURNACE)) {
                            // Set player and furnace UUIDs.
                            updateStatement.setString(10, furnace.getUniqueId().toString());
                            updateStatement.setString(11, uuid.toString());

                            // Set the new open time to now.
                            updateStatement.setTimestamp(1, Timestamp.from(Instant.now()));

                            // Timings.
                            updateStatement.setInt(2, (Integer) data.get("cook_time"));
                            updateStatement.setInt(3, (Integer) data.get("fuel_time"));
                            updateStatement.setInt(4, (Integer) data.get("total_fuel"));
                            updateStatement.setFloat(5, (Float) data.get("experience"));

                            // ItemStacks.
                            updateStatement.setString(6, (String) data.get("fuel_item"));
                            updateStatement.setString(7, (String) data.get("input_item"));
                            updateStatement.setString(8, (String) data.get("output_item"));
                            updateStatement.setString(9, (String) data.get("title"));

                            // Execute the statement.
                            updateStatement.executeUpdate();
                        }
                    }
                }
            } catch (SQLException exception) {
                Utils.logError(exception);
            }
        });
    }

    @Override
    public Furnace loadFurnace(final UUID player, final UUID uuid) {
        // Get all data from the database as a CompletableFuture.
        final CompletableFuture<Map<String, Object>> future = getDataFromDatabase(GET_FURNACE, player, uuid);

        try {
            // Get our data.
            final Map<String, Object> data = future.get();
            // Deserialize the data to a Furnace.
            return Furnace.deserializeFromSQL(data);
        } catch (InterruptedException | ExecutionException e) {
            Utils.logError(e);
            // Return null if we error.
            return null;
        }
    }

    @Override
    public void saveBrewingStand(final @NotNull UUID uuid, final BrewingStand brewingStand) {
        if (brewingStand == null) return;

        // Must be run async, it will be a blocking request.
        Bukkit.getScheduler().runTaskAsynchronously(Utils.getInstance(), () -> {
            // Try-with-resources to automatically close the obtained connection and the prepared statement.
            try (final Connection connection = getConnection();
                 // Prepare the statement with the connection.
                 final PreparedStatement statement = connection.prepareStatement(CHECK_STAND_EXISTS)) {
                // Set player and brewing stand UUIDs.
                statement.setString(1, brewingStand.getUniqueId().toString());
                statement.setString(2, uuid.toString());

                // Serialize the BrewingStand for SQL.
                final Map<String, Object> data = new HashMap<>(brewingStand.serializeForSQL());

                try (final ResultSet resultSet = statement.executeQuery()) {
                    if (!resultSet.next()) {
                        try (final PreparedStatement insertStatement = connection.prepareStatement(SAVE_STAND)) {
                            // Set player and furnace UUIDs.
                            insertStatement.setString(1, brewingStand.getUniqueId().toString());
                            insertStatement.setString(2, uuid.toString());

                            // Set the new open time to now.
                            insertStatement.setTimestamp(3, Timestamp.from(Instant.now()));

                            // Timings.
                            insertStatement.setInt(4, (Integer) data.get("fuel_time"));
                            insertStatement.setInt(5, (Integer) data.get("brew_time"));
                            insertStatement.setInt(6, (Integer) data.get("max_brews"));

                            // ItemStacks.
                            insertStatement.setString(7, (String) data.get("fuel_item"));
                            insertStatement.setString(8, (String) data.get("ingredient"));
                            insertStatement.setString(9, (String) data.get("bottle_1"));
                            insertStatement.setString(10, (String) data.get("bottle_2"));
                            insertStatement.setString(11, (String) data.get("bottle_3"));
                            insertStatement.setString(12, (String) data.get("title"));

                            // Execute the statement.
                            insertStatement.executeUpdate();
                        }
                    } else {
                        try (final PreparedStatement updateStatement = connection.prepareStatement(UPDATE_STAND)) {
                            // Set player and furnace UUIDs.
                            updateStatement.setString(11, brewingStand.getUniqueId().toString());
                            updateStatement.setString(12, uuid.toString());

                            // Set the new open time to now.
                            updateStatement.setTimestamp(1, Timestamp.from(Instant.now()));

                            // Timings.
                            updateStatement.setInt(2, (Integer) data.get("fuel_time"));
                            updateStatement.setInt(3, (Integer) data.get("brew_time"));
                            updateStatement.setInt(4, (Integer) data.get("max_brews"));

                            // ItemStacks.
                            updateStatement.setString(5, (String) data.get("fuel_item"));
                            updateStatement.setString(6, (String) data.get("ingredient"));
                            updateStatement.setString(7, (String) data.get("bottle_1"));
                            updateStatement.setString(8, (String) data.get("bottle_2"));
                            updateStatement.setString(9, (String) data.get("bottle_3"));
                            updateStatement.setString(10, (String) data.get("title"));

                            // Execute the statement.
                            updateStatement.executeUpdate();
                        }
                    }
                }
            } catch (SQLException exception) {
                Utils.logError(exception);
                Utils.logError("Failed to save a brewing stand (" + brewingStand.getUniqueId() + ") to your MySQL database!");
            }
        });
    }

    @Override
    public BrewingStand loadBrewingStand(final UUID player, final UUID uuid) {
        // Get all data from the database as a CompletableFuture.
        final CompletableFuture<Map<String, Object>> future = getDataFromDatabase(GET_STAND, player, uuid);

        try {
            // Get our data.
            final Map<String, Object> data = future.get();
            // Deserialize the data to a BrewingStand.
            return BrewingStand.deserializeFromSQL(data);
        } catch (InterruptedException | ExecutionException e) {
            Utils.logError(e);
            // Return null if we error.
            return null;
        }
    }

    @Override
    public @Nullable UUID getFurnaceUUIDFromID(UUID owner, Integer id, FurnaceType type) {
        // Obtain the UUID of a furnace based on the owner and the type.
        return obtainUUID(owner, id, GET_FURNACE_FROM_OWNER, type == null ? FurnaceType.FURNACE : type);
    }

    @Override
    public @Nullable UUID getBrewingStandUUIDFromID(UUID owner, Integer id) {
        // Obtain the UUID of a brewing stand based on the owner.
        return obtainUUID(owner, id, GET_STAND_UUID_FROM_OWNER, null);
    }

    /**
     * Obtains a UUID from the database based on owner and furnace type (if required).
     *
     * @param player           The owner to search for.
     * @param id               The id of the machine to get.
     * @param getUuidFromOwner The statement string.
     * @param type             The furnace type.
     * @return Returns a UUID or {@code null}.
     */
    @Nullable
    private UUID obtainUUID(UUID player, Integer id, String getUuidFromOwner, final FurnaceType type) {
        final CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> {
            // The list of UUIDS.
            final List<UUID> uuids = new ArrayList<>();

            // Make our connection and statement.
            try (final Connection connection = getConnection();
                 final PreparedStatement statement = connection.prepareStatement(getUuidFromOwner)) {
                // Set the player UUID.
                statement.setString(1, player.toString());

                // If we are searching for a furnace, make sure it isn't null.
                if (type != null) {
                    statement.setString(2, type.name().toLowerCase());
                }


                // Get our result set.
                try (final ResultSet set = statement.executeQuery()) {
                    // Loop the set and add to uuids.
                    while (set.next()) {
                        uuids.add(UUID.fromString(set.getString("uuid")));
                    }
                }

                // Get the UUID at a specific location.
                return uuids.isEmpty() ? null : uuids.size() <= id ? null : uuids.get(id);
            } catch (SQLException exception) {
                Utils.logError(exception);
                return null;
            }
        });

        // Get the UUID or null if errored.
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Utils.logError(e);
            return null;
        }
    }

    /**
     * Returns a Map of data based on the UUID of a machine and the owner.
     *
     * @param getString The statement String.
     * @param player    The owner of the machine.
     * @param machine   The machine itself.
     * @return Returns a CompletableFuture containing a {@literal Map<String, Object>}
     */
    private @NotNull CompletableFuture<Map<String, Object>> getDataFromDatabase(String getString, UUID player, UUID machine) {
        return CompletableFuture.supplyAsync(() -> {
            try (final Connection connection = getConnection();
                 final PreparedStatement statement = connection.prepareStatement(getString)) {

                // Set the variables.
                statement.setString(1, player.toString());
                statement.setString(2, machine.toString());

                // Return the entire ResultSet as a map.
                try (final ResultSet set = statement.executeQuery()) {
                    if (!set.next()) {
                        return Map.of();
                    }

                    final Map<String, Object> data = new HashMap<>();
                    final ResultSetMetaData resultSetMetaData = set.getMetaData();

                    final String[] cols = new String[resultSetMetaData.getColumnCount()];
                    for (int i = 1; i < resultSetMetaData.getColumnCount() + 1; i++) {
                        cols[i - 1] = resultSetMetaData.getColumnLabel(i);
                    }

                    for (String col : cols) {
                        data.put(col, set.getObject(col));
                    }

                    return data;

                }
            } catch (SQLException ex) {
                Utils.logError(ex);
                Utils.logError("Failed to load a brewing stand from your MySQL database!");
                return Map.of();
            }
        });
    }
}
