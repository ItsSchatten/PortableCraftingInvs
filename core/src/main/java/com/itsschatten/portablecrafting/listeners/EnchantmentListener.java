package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.commands.EnchanttableCommand;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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

/*        if (EnchanttableCommand.getOpenEnchantTables().containsKey(event.getEnchanter().getUniqueId())) {
            int maxLevel = EnchanttableCommand.getOpenEnchantTables().get(event.getEnchanter().getUniqueId());

            event.getOffers()[0] = new EnchantmentOffer(obtainRandomEnchant(event.getItem()), ThreadLocalRandom.current().nextInt(2), (int) ((double) maxLevel - ((double) maxLevel * .60)));
            event.getOffers()[1] = new EnchantmentOffer(obtainRandomEnchant(event.getItem()), ThreadLocalRandom.current().nextInt(4), (int) ((double) maxLevel - ((double) maxLevel * .35)));
            event.getOffers()[2] = new EnchantmentOffer(obtainRandomEnchant(event.getItem()), ThreadLocalRandom.current().nextInt(5), maxLevel);

            event.getInventory().getViewers().forEach((viewer) -> {
                if (viewer instanceof Player player) {
                    InventoryView view = player.getOpenInventory();

                    view.setProperty(InventoryView.Property.ENCHANT_BUTTON1, event.getOffers()[0].getCost());
                    view.setProperty(InventoryView.Property.ENCHANT_BUTTON2, event.getOffers()[1].getCost());
                    view.setProperty(InventoryView.Property.ENCHANT_BUTTON3, event.getOffers()[2].getCost());

                    view.setProperty(InventoryView.Property.ENCHANT_LEVEL1, event.getOffers()[0].getEnchantmentLevel());
                    view.setProperty(InventoryView.Property.ENCHANT_LEVEL2, event.getOffers()[1].getEnchantmentLevel());
                    view.setProperty(InventoryView.Property.ENCHANT_LEVEL3, event.getOffers()[2].getEnchantmentLevel());

                    view.setProperty(InventoryView.Property.ENCHANT_ID1, -1);
                    view.setProperty(InventoryView.Property.ENCHANT_ID2, -1);
                    view.setProperty(InventoryView.Property.ENCHANT_ID3, -1);
                    player.updateInventory();
                }
            });
        }*/
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
    public void closeEnchantInventory(@NotNull InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING) {
            EnchanttableCommand.getOpenEnchantTables().remove(event.getPlayer().getUniqueId());
            PortableCraftingInvsPlugin.getFakeContainers().removeFromEnchantList((Player) event.getPlayer());
        }

    }

}
