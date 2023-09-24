package com.itsschatten.portablecrafting.listeners;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.FakeContainers;
import com.itsschatten.portablecrafting.Permissions;
import com.itsschatten.portablecrafting.PortableCraftingInvsPlugin;
import com.itsschatten.portablecrafting.configs.Messages;
import com.itsschatten.portablecrafting.configs.Settings;
import com.itsschatten.portablecrafting.configs.SignsConfig;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class SignListener implements Listener {

    @EventHandler
    public void onSign(final SignChangeEvent event) {
        if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE.getPermission()))
            return;

        if (event.getLine(0) == null) {
            return;
        }

        switch (Objects.requireNonNull(event.getLine(0), "Somehow bypassed the null check, null line check. Uh oh.").toLowerCase()) {
            case "[anvil]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_ANVIL.getPermission()))
                    return;
                if (!Settings.USE_ANVIL_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.ANVIL_SIGN));

                makeSign(event, SignTypes.ANVIL);

                Utils.tell(event.getPlayer(), Messages.ANVIL_SIGN_CREATED);
            }

            case "[blastfurnace]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.USE_SIGN_BLASTFURANCE.getPermission()))
                    return;
                if (!Settings.USE_BLAST_FURNACE_SIGN)
                    return;

                event.setLine(0, Utils.colorize(Messages.BLAST_FURNACE_SIGN));

                makeSign(event, SignTypes.BLAST_FURNACE);

                Utils.tell(event.getPlayer(), Messages.BLAST_FURNACE_SIGN_CREATED);
            }

            case "[brewing]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.USE_SIGN_BREWING.getPermission()))
                    return;
                if (!Settings.USE_BREWING_STAND_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.BREWING_SIGN));

                makeSign(event, SignTypes.BREWING_STAND);

                Utils.tell(event.getPlayer(), Messages.BREWING_SIGN_CREATED);
            }

            case "[cartography]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_CARTOGRAPHY.getPermission()))
                    return;
                if (!Settings.USE_CARTOGRAPHY_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.CARTOGRAPHY_SIGN));

                makeSign(event, SignTypes.CARTOGRAPHY);

                Utils.tell(event.getPlayer(), Messages.CARTOGRAPHY_SIGN_CREATED);
            }

            case "[crafting]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_CRAFTING.getPermission()))
                    return;
                if (!Settings.USE_CRAFTING_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.CRAFTING_SIGN));

                makeSign(event, SignTypes.CRAFTING_TABLE);

                Utils.tell(event.getPlayer(), Messages.CRAFTING_SIGN_CREATED);
            }

            case "[enchanttable]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_ENCHANT_TABLE.getPermission()))
                    return;
                if (!Settings.USE_ENCHANT_TABLE_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.ENCHANT_TABLE_SIGN));

                makeSign(event, SignTypes.ENCHANTMENT_TABLE);

                Utils.tell(event.getPlayer(), Messages.ENCHANT_TABLE_SIGN_CREATED);
            }

            case "[enderchest]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_ENDERCHEST.getPermission()))
                    return;
                if (!Settings.USE_ENDERCHEST_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.ENDERCHEST_SIGN));

                makeSign(event, SignTypes.ENDER_CHEST);

                Utils.tell(event.getPlayer(), Messages.ENDERCHEST_SIGN_CREATED);
            }

            case "[furnace]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.USE_SIGN_FURANCE.getPermission()))
                    return;
                if (!Settings.USE_FURNACE_SIGN)
                    return;

                event.setLine(0, Utils.colorize(Messages.FURNACE_SIGN));

                makeSign(event, SignTypes.FURNACE);

                Utils.tell(event.getPlayer(), Messages.FURNACE_SIGN_CREATED);
            }

            case "[grindstone]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_GRINDSTONE.getPermission()))
                    return;
                if (!Settings.USE_GRINDSTONE_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.GRINDSTONE_SIGN));

                makeSign(event, SignTypes.GRINDSTONE);

                Utils.tell(event.getPlayer(), Messages.GRINDSTONE_SIGN_CREATED);
            }

            case "[loom]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_LOOM.getPermission()))
                    return;
                if (!Settings.USE_LOOM_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.LOOM_SIGN));

                makeSign(event, SignTypes.LOOM);

                Utils.tell(event.getPlayer(), Messages.LOOM_SIGN_CREATED);
            }

            case "[stonecutter]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_STONE_CUTTER.getPermission()))
                    return;
                if (!Settings.USE_STONE_CUTTER_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.STONE_CUTTER_SIGN));

                makeSign(event, SignTypes.STONE_CUTTER);

                Utils.tell(event.getPlayer(), Messages.STONE_CUTTER_SIGN_CREATED);
            }

            case "[smithing]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.SIGN_CREATE_SMITHING.getPermission()))
                    return;
                if (!Settings.USE_SMITHING_SIGN)
                    return;
                event.setLine(0, Utils.colorize(Messages.SMITHING_TABLE_SIGN));

                makeSign(event, SignTypes.SMITHING);

                Utils.tell(event.getPlayer(), Messages.SMITHING_TABLE_SIGN_CREATED);
            }

            case "[smoker]" -> {
                if (Settings.USE_PERMISSIONS && !event.getPlayer().hasPermission(Permissions.USE_SIGN_SMOKER.getPermission()))
                    return;
                if (!Settings.USE_SMOKER_SIGN)
                    return;

                event.setLine(0, Utils.colorize(Messages.SMOKER_SIGN));

                makeSign(event, SignTypes.SMOKER);

                Utils.tell(event.getPlayer(), Messages.SMOKER_SIGN_CREATED);
            }

            default -> {
            }
        }
    }

    @EventHandler
    public void onSignInteract(final PlayerInteractEvent event) {

        if (isSign(event)) {
            if (!Settings.USE_SIGNS)
                return;
            final SignsConfig signsConfig = SignsConfig.getInstance();

            if (Settings.REQUIRE_SIGHT_CLICK_BREAK_SIGN && event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
                String path;

                for (String key : signsConfig.getConfigurationSection("signs").getKeys(false)) {
                    if (signsConfig.get("signs." + key + ".where.world") == event.getPlayer().getLocation().getWorld().getName()
                            && signsConfig.getInt("signs." + key + ".where.x") == event.getClickedBlock().getX()
                            && signsConfig.getInt("signs." + key + ".where.y") == event.getClickedBlock().getY()
                            && signsConfig.getInt("signs." + key + ".where.z") == event.getClickedBlock().getZ()) {
                        path = "signs." + key;
                        signsConfig.set(path, null);
                        signsConfig.set("sign-amount", SignsConfig.getInstance().getInt("sign-amount") - 1);
                        signsConfig.saveConfig();
                    }
                }
                return;
            }

            if (Settings.REQUIRE_SIGHT_CLICK_BREAK_SIGN && event.getAction() == Action.LEFT_CLICK_BLOCK && event.getPlayer().hasPermission(Permissions.SIGN_CREATE.getPermission())) {
                Utils.tell(event.getPlayer(), Messages.MUST_SHIFT_CLICK_TO_BREAK_SIGN);
                event.setCancelled(true);
            }

            for (String key : signsConfig.getConfigurationSection("signs").getKeys(false)) {
                if (signsConfig.getString("signs." + key + ".where.world").equalsIgnoreCase(event.getPlayer().getLocation().getWorld().getName())
                        && signsConfig.getInt("signs." + key + ".where.x") == event.getClickedBlock().getX()
                        && signsConfig.getInt("signs." + key + ".where.y") == event.getClickedBlock().getY()
                        && signsConfig.getInt("signs." + key + ".where.z") == event.getClickedBlock().getZ()) {

                    if (SignTypes.valueOf(signsConfig.getString("signs." + key + ".type")).equals(SignTypes.ENCHANTMENT_TABLE)) {
                        try {
                            if (signsConfig.getInt("signs." + key + ".max-level") != -1) {
                                int level = signsConfig.getInt("signs." + key + ".max-level");
                                getSign(event, SignTypes.valueOf(signsConfig.getString("signs." + key + ".type")), level);
                            }
                        } catch (NullPointerException ex) { // Catch this error because spigot is weird and doesn't want to use a proper check because it doesn't exist...
                            getSign(event, SignTypes.valueOf(signsConfig.getString("signs." + key + ".type")));
                        }
                    }

                    getSign(event, SignTypes.valueOf(signsConfig.getString("signs." + key + ".type")));
                }
            }
        }

    }

    private boolean isSign(final @NotNull PlayerInteractEvent event) {
        final SignsConfig signsConfig = SignsConfig.getInstance();
        if (event.getClickedBlock() == null) {
            return false;
        }

        if (event.getClickedBlock().getType().name().contains("SIGN")) {
            if (signsConfig.getConfigurationSection("signs") == null || signsConfig.getConfigurationSection("signs").getKeys(false).isEmpty()) {
                return false;
            }

            for (String key : signsConfig.getConfigurationSection("signs").getKeys(false)) {
                if (signsConfig.getString("signs." + key + ".where.world").equalsIgnoreCase(event.getPlayer().getLocation().getWorld().getName())
                        && signsConfig.getInt("signs." + key + ".where.x") == event.getClickedBlock().getX()
                        && signsConfig.getInt("signs." + key + ".where.y") == event.getClickedBlock().getY()
                        && signsConfig.getInt("signs." + key + ".where.z") == event.getClickedBlock().getZ()) {

                    return true;
                }
            }
        }

        return false;
    }

    private void makeSign(final @NotNull SignChangeEvent event, final @NotNull SignTypes signType) {
        final SignsConfig signs = SignsConfig.getInstance();
        signs.set("sign-amount", signs.getInt("sign-amount") + 1);

        signs.set("signs." + signs.getInt("sign-amount") + ".where.world", event.getBlock().getWorld().getName());
        signs.set("signs." + signs.getInt("sign-amount") + ".where.x", event.getBlock().getX());
        signs.set("signs." + signs.getInt("sign-amount") + ".where.y", event.getBlock().getY());
        signs.set("signs." + signs.getInt("sign-amount") + ".where.z", event.getBlock().getZ());
        signs.set("signs." + signs.getInt("sign-amount") + ".type", signType.name());
        signs.set("signs." + signs.getInt("sign-amount") + ".created-by", event.getPlayer().getName());

        if (signType.equals(SignTypes.ENCHANTMENT_TABLE)) {
            if (event.getLine(1) == null || event.getLine(1).isEmpty()) {
                signs.set("signs." + signs.getInt("sign-amount") + ".max-level", -1);
                return;
            }
            try {
                int maxLevel = Math.min(Integer.parseInt(Objects.requireNonNull(event.getLine(1))), 30);
                signs.set("signs." + signs.getInt("sign-amount") + ".max-level", maxLevel);
            } catch (NumberFormatException ignored) {
            }
        }

        signs.saveConfig();
    }

    private void getSign(@NotNull PlayerInteractEvent event, @NotNull SignTypes signTypes, int... maxLevel) {
        final Player player = event.getPlayer();
        final FakeContainers fakeContainers = PortableCraftingInvsPlugin.getFakeContainers();

        switch (signTypes) {
            case ANVIL: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_ANVIL.getPermission()))
                    return;
                if (!Settings.USE_ANVIL_SIGN)
                    return;
                fakeContainers.openAnvil(player);
                break;
            }

            case BLAST_FURNACE: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_BLASTFURANCE.getPermission()))
                    return;
                if (!Settings.USE_BLAST_FURNACE_SIGN)
                    return;
                fakeContainers.openBlastFurnace(player);
                break;
            }

            case BREWING_STAND: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_BREWING.getPermission()))
                    return;
                if (!Settings.USE_BREWING_STAND_SIGN)
                    return;
                fakeContainers.openBrewingStand(player);
                break;
            }

            case CARTOGRAPHY: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_CARTOGRAPHY.getPermission()))
                    return;
                if (!Settings.USE_CARTOGRAPHY_SIGN)
                    return;
                fakeContainers.openCartography(player);
                break;
            }

            case CRAFTING_TABLE: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_CRAFTING.getPermission()))
                    return;
                if (!Settings.USE_CRAFTING_SIGN)
                    return;
                player.openWorkbench(player.getLocation(), true);
                break;
            }

            case ENCHANTMENT_TABLE: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_ENCHANT_TABLE.getPermission()))
                    return;
                if (!Settings.USE_ENCHANT_TABLE_SIGN)
                    return;

                if (maxLevel.length > 1) {
                    fakeContainers.openEnchant(player, maxLevel[0]);
                } else
                    fakeContainers.openEnchant(player);

                break;
            }

            case ENDER_CHEST: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_ENDERCHEST.getPermission()))
                    return;
                if (!Settings.USE_ENDERCHEST_SIGN)
                    return;
                player.openInventory(player.getEnderChest());
                break;
            }

            case FURNACE: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_FURANCE.getPermission()))
                    return;
                if (!Settings.USE_FURNACE_SIGN)
                    return;
                fakeContainers.openFurnace(player);
                break;
            }

            case GRINDSTONE: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_GRINDSTONE.getPermission()))
                    return;
                if (!Settings.USE_GRINDSTONE_SIGN)
                    return;
                fakeContainers.openGrindStone(player);
                break;
            }

            case LOOM: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_LOOM.getPermission()))
                    return;
                if (!Settings.USE_LOOM_SIGN)
                    return;
                fakeContainers.openLoom(player);
                break;
            }

            case STONE_CUTTER: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_STONE_CUTTER.getPermission()))
                    return;
                if (!Settings.USE_STONE_CUTTER_SIGN)
                    return;
                fakeContainers.openStoneCutter(player);
                break;
            }

            case SMITHING: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_SMITHING.getPermission()))
                    return;
                if (!Settings.USE_SMITHING_SIGN)
                    return;
                if (!Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].contains("1_16_R1")) {
                    Utils.debugLog( "Version is not 1.16+, not attempting to open the smithing table.");
                    return;
                }
                fakeContainers.openSmithing(player);
            }

            case SMOKER: {
                if (Settings.USE_PERMISSIONS && !player.hasPermission(Permissions.USE_SIGN_SMOKER.getPermission()))
                    return;
                if (!Settings.USE_SMOKER_SIGN)
                    return;
                fakeContainers.openSmoker(player);
                break;
            }

            default:
                break;
        }

    }

    enum SignTypes {
        ANVIL,
        BLAST_FURNACE,
        BREWING_STAND,
        CARTOGRAPHY,
        CRAFTING_TABLE,
        ENCHANTMENT_TABLE,
        ENDER_CHEST,
        FURNACE,
        GRINDSTONE,
        LOOM,
        STONE_CUTTER,
        SMITHING,
        SMOKER
    }

}
