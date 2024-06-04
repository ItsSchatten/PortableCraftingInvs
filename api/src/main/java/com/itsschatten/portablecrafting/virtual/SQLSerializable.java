package com.itsschatten.portablecrafting.virtual;

import java.util.Map;

public interface SQLSerializable {
    /**
     * A map that can be used to serialize data into an SQL row.
     *
     * @return Returns a Map that should be used to serialize the data into an SQL row.
     */
    Map<String, Object> serializeForSQL();
}
