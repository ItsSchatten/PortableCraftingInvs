package com.itsschatten.portablecrafting.storage;

import com.itsschatten.portablecrafting.configs.Settings;

import java.util.Map;

/**
 * Storage credentials, util class to get them all quickly.
 *
 * @param address           The address for the connection.
 * @param database          The database to use for the connection.
 * @param username          The username to connect.
 * @param password          The password to connect.
 * @param maxPool           Maximum pool size.
 * @param minIdle           Minimum idle connections.
 * @param maxLife           Maximum lifetime.
 * @param keepAlive         Keepalive timeout.
 * @param connectionTimeout Connection timeout.
 * @param properties        Additional properties.
 */
public record StorageCredentials(String address, String database, String username, String password, int maxPool,
                                 int minIdle, int maxLife, int keepAlive, int connectionTimeout,
                                 Map<String, Object> properties) {

    public StorageCredentials() {
        this(Settings.DATABASE_ADDRESS, Settings.DATABASE_DATABASE, Settings.DATABASE_USER, Settings.DATABASE_PASSWORD,
                Settings.POOL_MAX_SIZE, Settings.POOL_MIN_CONNECTIONS, Settings.POOL_MAX_LIFE, Settings.POOL_KEEP_ALIVE, Settings.POOL_CONNECTION_TIMEOUT,
                Settings.PROPERTIES);
    }

}
