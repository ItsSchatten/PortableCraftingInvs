package com.itsschatten.portablecrafting;

import com.shanebeestudios.api.BrewingManager;
import com.shanebeestudios.api.FurnaceManager;
import com.shanebeestudios.api.machine.BrewingStand;
import com.shanebeestudios.api.machine.Furnace;

import java.util.UUID;

public interface MySqlI {
    Furnace getFurnace(UUID owner, FurnaceManager manager, FurnaceTypes type);

    void setFurnace(UUID owner, Furnace furnace, FurnaceTypes type);

    BrewingStand getStand(UUID owner, BrewingManager manager);

    void setStand(UUID owner, BrewingStand stand);

    enum FurnaceTypes {
        FURNACE,
        BLAST_FURNACE,
        SMOKER
    }

}
