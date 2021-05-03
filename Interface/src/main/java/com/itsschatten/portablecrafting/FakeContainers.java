package com.itsschatten.portablecrafting;

import org.bukkit.entity.Player;

public interface FakeContainers {
    void setUsingMysql(boolean bool);

    void setDebug(boolean bool);

    void openLoom(Player player);

    void openAnvil(Player player);

    void openCartography(Player player);

    void openGrindStone(Player player);

    void openStoneCutter(Player player);

    void openSmithing(Player player);

    void openEnchant(Player player);

    void openEnchant(Player player, int maxLvl);

    void openFurnace(Player player);

    void openBlastFurnace(Player player);

    void openSmoker(Player player);

    void openBrewingStand(Player player);
}
