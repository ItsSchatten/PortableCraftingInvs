package com.itsschatten.portablecrafting;

import org.bukkit.entity.Player;

public interface FakeContainers {
    void setDebug(boolean bool);

    void openLoom(Player player);

    void openAnvil(Player player);

    void openCartography(Player player);

    void openGrindStone(Player player);

    void openStoneCutter(Player player);

    void openEnchant(Player player);

    void openEnchant(Player player, int maxLvl);
}
