package me.itsshadow.portablecrafting.commands;

import me.itsshadow.libs.commandutils.PlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.block.Furnace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class FurnaceCommand extends PlayerCommand {


    public FurnaceCommand () {
        super("furnace");
    }

    @Override
    protected void run(Player player, String[] strings) {

        Inventory inv = Bukkit.createInventory(null, InventoryType.FURNACE);

        player.openInventory(inv);

    }
}
