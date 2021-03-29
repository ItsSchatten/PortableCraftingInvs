package com.itsschatten.portablecrafting;

import com.shanebeestudios.vf.api.FurnaceManager;
import com.shanebeestudios.vf.api.machine.Furnace;

import java.util.UUID;

public interface MySqlI {
    Furnace getFurnace(UUID owner, FurnaceManager manager, FurnaceTypes type);

    void setFurnace(UUID owner, Furnace furnace, FurnaceTypes type);

    enum FurnaceTypes {
        FURNACE,
        BLAST_FURNACE,
        SMOKER
    }

}
