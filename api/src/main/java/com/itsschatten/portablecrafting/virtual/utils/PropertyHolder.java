package com.itsschatten.portablecrafting.virtual.utils;

import com.itsschatten.portablecrafting.virtual.machine.properties.Properties;

/**
 * "Holder Class" for properties of a Machine.
 *
 * @param <T> The Generic type should extend Properties.
 */
public interface PropertyHolder<T extends Properties> {

    /**
     * @return Returns the properties.
     */
    T getProperties();
}
