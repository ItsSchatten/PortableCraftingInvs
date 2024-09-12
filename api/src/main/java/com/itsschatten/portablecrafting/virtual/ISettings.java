package com.itsschatten.portablecrafting.virtual;

public interface ISettings {

    /**
     * @return The maximum furnace amount.
     */
    int maximumFurnaces();

    /**
     * @return The maximum brewing stand amount.
     */
    int maximumBrewingStands();

    /**
     * If we should use furnaces.
     *
     * @return The value.
     */
    boolean useFurnaces();

    /**
     * If we should use brewing stands.
     *
     * @return The value.
     */
    boolean useBrewingStands();

}
