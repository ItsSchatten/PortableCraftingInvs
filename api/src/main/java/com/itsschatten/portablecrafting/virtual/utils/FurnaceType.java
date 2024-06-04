package com.itsschatten.portablecrafting.virtual.utils;

import com.itsschatten.portablecrafting.virtual.machine.properties.BlastingProperties;
import com.itsschatten.portablecrafting.virtual.machine.properties.FurnaceProperties;
import com.itsschatten.portablecrafting.virtual.machine.properties.SmokerProperties;
import lombok.Getter;
import org.bukkit.event.inventory.InventoryType;

/**
 * Default configured FurnaceType.
 */
@Getter
public enum FurnaceType {
    /**
     * A normal ol' furnace.
     */
    FURNACE(InventoryType.FURNACE, "Furnace", FurnaceProperties.FURNACE),
    /**
     * A smoker, used for meat and such.
     */
    SMOKER(InventoryType.SMOKER, "Smoker", SmokerProperties.SMOKER),
    /**
     * A blasting furnace, used for smelting ores.
     */
    BLASTING(InventoryType.BLAST_FURNACE, "Blast Furnace", BlastingProperties.BLASTING);

    /**
     * The inventory type to open.
     */
    final InventoryType type;
    /**
     * The name of the furnace, used as a default title.
     */
    final String name;
    /**
     * Properties of the furnace.
     */
    final FurnaceProperties properties;

    FurnaceType(InventoryType type, String name, FurnaceProperties properties) {
        this.type = type;
        this.name = name;
        this.properties = properties;
    }
}
