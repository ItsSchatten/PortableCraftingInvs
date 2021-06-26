package com.itsschatten.portablecrafting.configs;

import com.itsschatten.libs.Utils;
import com.itsschatten.libs.configutils.MySqlUtils;
import com.itsschatten.portablecrafting.MySqlI;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.shanebeestudios.api.BrewingManager;
import com.shanebeestudios.api.FurnaceManager;
import com.shanebeestudios.api.machine.BrewingStand;
import com.shanebeestudios.api.machine.Furnace;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MySql implements MySqlI {

    public static final String furnaceTable = "pci_furnaces", brewingTable = "pci_brewing_stands";

    public final MySqlUtils mysql;

    public final boolean enabled = Settings.USE_MYSQL;

    public MySql(String host, int port, String database, String user, String pass) {
        if (enabled) {
            this.mysql = new MySqlUtils(host, port, database, user, pass);

            Utils.debugLog(Settings.DEBUG, "Checking if tables exist. If they do not we will create them.");
            createIfNotExists();

            if (mysql.getConnection() == null) {
                Utils.log("&cConnection to MySQL was unsuccessful! Reverting to using file system...");

            } else
                Utils.debugLog(Settings.DEBUG, "&aConnection to MySQL was successful!");
        } else
            this.mysql = null;

    }

    private void createIfNotExists() {
        mysql.update("CREATE TABLE IF NOT EXISTS " + furnaceTable +
                " (owner_uuid VARCHAR(64), furnace_uuid VARCHAR(64), furnace_type enum('FURNACE', 'BLAST_FURNACE', 'SMOKER'))");
        mysql.update("CREATE TABLE IF NOT EXISTS " + brewingTable + " (owner_uuid VARCHAR(64), stand_uuid VARCHAR(64))");
    }

    @Override
    public BrewingStand getStand(UUID owner, BrewingManager manager) {
        final CompletableFuture<BrewingStand> getStand = CompletableFuture.supplyAsync(() -> {
            final MySqlUtils.WrappedResultSet set = mysql.query("SELECT * FROM " + brewingTable + " WHERE owner_uuid = '" + owner + "'");
            try {
                if (!set.next() || !set.hasColumn("stand_uuid")) {
                    return null;
                }

                return manager.getByID(UUID.fromString(set.getResultSet().getString("stand_uuid")));
            } catch (SQLException throwable) {
                throwable.printStackTrace();
                return null;
            }
        });
        try {
            return getStand.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setStand(UUID owner, BrewingStand stand) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.update("INSERT INTO " + brewingTable + " (owner_uuid, stand_uuid) VALUES ('" + owner + "', '" + stand.getUniqueID() + "')");
            }
        }.runTaskAsynchronously(PortableCraftingInvsPlugin.getInstance());
    }

    @Override
    public void setFurnace(UUID owner, Furnace furnace, FurnaceTypes type) {
        new BukkitRunnable() {
            @Override
            public void run() {
                mysql.update("INSERT INTO " + furnaceTable + " (owner_uuid, furnace_uuid, furnace_type) VALUES ('" + owner + "', '" + furnace.getUniqueID() + "', '" + type.name() + "')");
            }
        }.runTaskAsynchronously(PortableCraftingInvsPlugin.getInstance());

    }

    @Override
    public Furnace getFurnace(UUID owner, FurnaceManager furnaceManager, FurnaceTypes type) {
        final CompletableFuture<Furnace> getFurnace = CompletableFuture.supplyAsync(() -> {
            final MySqlUtils.WrappedResultSet set = mysql.query("SELECT * FROM " + furnaceTable + " WHERE owner_uuid = '" + owner + "' AND furnace_type = '" + type + "'");
            try {
                if (!set.next() || !set.hasColumn("furnace_uuid")) {
                    return null;
                }

                return furnaceManager.getByID(UUID.fromString(set.getResultSet().getString("furnace_uuid")));
            } catch (SQLException throwable) {
                throwable.printStackTrace();
                return null;
            }
        });
        try {
            return getFurnace.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
            return null;
        }
    }
}
