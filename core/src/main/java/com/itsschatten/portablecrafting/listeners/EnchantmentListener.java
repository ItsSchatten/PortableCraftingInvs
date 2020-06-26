package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.commands.EnchanttableCommand;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentListener implements Listener {

    @EventHandler
    public void prepareEnchantments(PrepareItemEnchantEvent event) {

        if (event.getItem().getEnchantments().size() > 0 || event.getItem().getType().equals(Material.ENCHANTED_BOOK)) {
            return;
        }

        if (EnchanttableCommand.getOpenEnchantTables().containsKey(event.getEnchanter().getUniqueId())) {
            int maxLevel = EnchanttableCommand.getOpenEnchantTables().get(event.getEnchanter().getUniqueId());

            Utils.log((int) ((double) maxLevel - ((double) maxLevel * .1)) + "");
            Utils.log((int) ((double) maxLevel - ((double) maxLevel * .3)) + "");

            event.getOffers()[0] = new EnchantmentOffer(obtainRandomEnchant(event.getItem()), ThreadLocalRandom.current().nextInt(2), (int) ((double) maxLevel - ((double) maxLevel * .60)));
            event.getOffers()[1] = new EnchantmentOffer(obtainRandomEnchant(event.getItem()), ThreadLocalRandom.current().nextInt(4), (int) ((double) maxLevel - ((double) maxLevel * .35)));
            event.getOffers()[2] = new EnchantmentOffer(obtainRandomEnchant(event.getItem()), ThreadLocalRandom.current().nextInt(5), maxLevel);
        }
    }

    private Enchantment obtainRandomEnchant(ItemStack item) {
        List<Enchantment> possibleEnchants = new ArrayList<>();

        if (item.getType().equals(Material.BOOK)) {
            possibleEnchants.addAll(Arrays.asList(Enchantment.values()));
        } else {
            for (Enchantment enchantment : Enchantment.values()) {
                if (enchantment.canEnchantItem(item)) {
                    possibleEnchants.add(enchantment);
                }

            }
        }

        possibleEnchants.removeIf(Enchantment::isTreasure);

        Enchantment enchantment;

        if (possibleEnchants.size() > 1) {
            enchantment = possibleEnchants.get(0);
        } else {
            Collections.shuffle(possibleEnchants);
            enchantment = possibleEnchants.get(ThreadLocalRandom.current().nextInt(possibleEnchants.size()));
        }

        return enchantment;
    }

    @EventHandler
    public void closeEnchantInventory(InventoryCloseEvent event) {

        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            EnchanttableCommand.getOpenEnchantTables().remove(event.getPlayer().getUniqueId());
        }

    }

}
