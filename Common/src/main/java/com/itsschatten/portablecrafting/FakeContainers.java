package com.itsschatten.portablecrafting;

import org.bukkit.entity.Player;

public interface FakeContainers {

    default void register() {
    }


    void setUsingMysql(boolean bool);

    void setDebug(boolean bool);

    boolean openAnvil(Player player);

    boolean openBlastFurnace(Player player);

    boolean openBrewingStand(Player player);

    boolean openCartography(Player player);

    boolean openEnchant(Player player);

    boolean openEnchant(Player player, int maxLvl);

    boolean openFurnace(Player player);

    boolean openGrindStone(Player player);

    boolean openLoom(Player player);

    boolean openStoneCutter(Player player);

    boolean openSmithing(Player player);

    boolean openSmoker(Player player);

    void removeFromEnchantList(Player player);
}
