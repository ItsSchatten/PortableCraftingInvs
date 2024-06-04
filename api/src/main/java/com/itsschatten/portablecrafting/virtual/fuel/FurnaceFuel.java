package com.itsschatten.portablecrafting.virtual.fuel;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Represents fuel that can be used in a {@link Furnace Furnace}.
 */
@Getter
public class FurnaceFuel extends Fuel {

    public static final List<FurnaceFuel> VANILLA_FUELS = new ArrayList<>();

    // Predefined vanilla furnace fuels, if adding to this list please maintain alphabetical formatting.
    // All of these are taken from https://minecraft.wiki/w/Smelting#Fuel and based on ONLY Java edition items.
    public static final FurnaceFuel ANY_BANNER = get("any_banner", Tag.BANNERS, 300);
    public static final FurnaceFuel ANY_BOAT = get("any_boat", Tag.ITEMS_BOATS, 1200);
    public static final FurnaceFuel ANY_CARPET = get("any_carpet", Tag.WOOL_CARPETS, 67);
    public static final FurnaceFuel ANY_LOG = get("any_log", Tag.LOGS, 300);
    public static final FurnaceFuel ANY_PLANK = get("any_plank", Tag.PLANKS, 300);
    public static final FurnaceFuel ANY_SAPLING = get("any_sapling", Tag.SAPLINGS, 100);
    public static final FurnaceFuel ANY_SIGN = get("any_sign", Tag.ALL_SIGNS, 200);
    public static final FurnaceFuel ANY_WOODEN_BUTTON = get("any_wooden_button", Tag.WOODEN_BUTTONS, 100);
    public static final FurnaceFuel ANY_WOODEN_DOOR = get("any_wooden_door", Tag.WOODEN_DOORS, 200);
    public static final FurnaceFuel ANY_WOODEN_SLAB = get("any_wooden_slab", Tag.WOODEN_SLABS, 150);
    public static final FurnaceFuel ANY_WOOD_FENCE = get("any_wood_fence", Tag.WOODEN_FENCES, 300);
    public static final FurnaceFuel ANY_WOOD_FENCE_GATE = get("any_wood_fence_gate", Tag.FENCE_GATES, 300);
    public static final FurnaceFuel ANY_WOOD_PRESSURE_PLATE = get("any_wood_pressure_plate", Tag.WOODEN_PRESSURE_PLATES, 300);
    public static final FurnaceFuel ANY_WOOD_STAIR = get("any_wood_stair", Tag.WOODEN_STAIRS, 300);
    public static final FurnaceFuel ANY_WOOD_TRAPDOOR = get("any_wood_trapdoor", Tag.WOODEN_TRAPDOORS, 300);
    public static final FurnaceFuel ANY_WOOL = get("any_wool", Tag.WOOL, 100);

    public static final FurnaceFuel BAMBOO = get("bamboo", Material.BAMBOO, 50);
    public static final FurnaceFuel BAMBOO_BLOCKS = get("bamboo_blocks", Tag.BAMBOO_BLOCKS, 300);
    public static final FurnaceFuel BAMBOO_MOSAIC = get("bamboo_mosaic", Material.BAMBOO_MOSAIC, 300);
    public static final FurnaceFuel BAMBOO_MOSAIC_SLAB = get("bamboo_mosaic_slab", Material.BAMBOO_MOSAIC_SLAB, 150);
    public static final FurnaceFuel BAMBOO_MOSAIC_STAIRS = get("bamboo_mosaic_stairs", Material.BAMBOO_MOSAIC_STAIRS, 300);

    public static final FurnaceFuel BARREL = get("barrel", Material.BARREL, 300);
    public static final FurnaceFuel BLAZE_ROD = get("blaze_rod", Material.BLAZE_ROD, 2400);
    public static final FurnaceFuel BLOCK_OF_COAL = get("block_of_coal", Material.COAL_BLOCK, 16000);
    public static final FurnaceFuel BOOKSHELF = get("bookshelf", Material.BOOKSHELF, 300);
    public static final FurnaceFuel BOW = get("bow", Material.BOW, 300);
    public static final FurnaceFuel BOWL = get("bowl", Material.BOWL, 100);

    public static final FurnaceFuel CARTOGRAPHY_TABLE = get("cartography_table", Material.CARTOGRAPHY_TABLE, 300);
    public static final FurnaceFuel CHARCOAL = get("charcoal", Material.CHARCOAL, 1600);
    public static final FurnaceFuel CHEST = get("chest", Material.CHEST, 300);
    public static final FurnaceFuel CHISELED_BOOKSHELF = get("chiseled_bookshelf", Material.CHISELED_BOOKSHELF, 300);
    public static final FurnaceFuel COAL = get("coal", Material.COAL, 1600);
    public static final FurnaceFuel COMPOSTER = get("composter", Material.COMPOSTER, 300);
    public static final FurnaceFuel CRAFTING_TABLE = get("crafting_table", Material.CRAFTING_TABLE, 300);
    public static final FurnaceFuel CROSSBOW = get("cross_bow", Material.CROSSBOW, 300);

    public static final FurnaceFuel DAYLIGHT_DETECTOR = get("daylight_detector", Material.DAYLIGHT_DETECTOR, 300);
    public static final FurnaceFuel DEAD_BUSH = get("dead_bush", Material.DEAD_BUSH, 100);
    public static final FurnaceFuel DRIED_KELP_BLOCK = get("dried_kelp_block", Material.DRIED_KELP_BLOCK, 4000);

    public static final FurnaceFuel FISHING_ROD = get("fishing_rod", Material.FISHING_ROD, 300);
    public static final FurnaceFuel FLETCHING_TABLE = get("fletching_table", Material.FLETCHING_TABLE, 300);
    public static final FurnaceFuel JUKEBOX = get("jukebox", Material.JUKEBOX, 300);

    public static final FurnaceFuel LADDER = get("ladder", Material.LADDER, 300);
    public static final FurnaceFuel LAVA_BUCKET = get("lava_bucket", Material.LAVA_BUCKET, 20000);
    public static final FurnaceFuel LECTERN = get("lectern", Material.LECTERN, 300);
    public static final FurnaceFuel LOOM = get("loom", Material.LOOM, 300);

    public static final FurnaceFuel MANGROVE_ROOTS = get("mangrove_roots", Material.MANGROVE_ROOTS, 300);
    public static final FurnaceFuel NOTE_BLOCK = get("note_block", Material.NOTE_BLOCK, 300);
    public static final FurnaceFuel SCAFFOLDING = get("scaffolding", Material.SCAFFOLDING, 50);
    public static final FurnaceFuel SMITHING_TABLE = get("smithing_table", Material.SMITHING_TABLE, 300);
    public static final FurnaceFuel STICK = get("stick", Material.STICK, 100);
    public static final FurnaceFuel TRAPPED_CHEST = get("trapped_chest", Material.TRAPPED_CHEST, 300);

    public static final FurnaceFuel WOODEN_AXE = get("wooden_axe", Material.WOODEN_AXE, 200);
    public static final FurnaceFuel WOODEN_HOE = get("wooden_hoe", Material.WOODEN_HOE, 200);
    public static final FurnaceFuel WOODEN_PICKAXE = get("wooden_pickaxe", Material.WOODEN_PICKAXE, 200);
    public static final FurnaceFuel WOODEN_SHOVEL = get("wooden_shovel", Material.WOODEN_SHOVEL, 200);
    public static final FurnaceFuel WOODEN_SWORD = get("wooden_sword", Material.WOODEN_SWORD, 200);

    // Add all fuel to the VANILLA fuel, this also maintains the alphabetical formatting.
    //<editor-fold defaultstate="collapsed" desc="Static initializations to the Vanilla fuels list.">
    static {
        VANILLA_FUELS.add(ANY_BANNER);
        VANILLA_FUELS.add(ANY_BOAT);
        VANILLA_FUELS.add(ANY_CARPET);
        VANILLA_FUELS.add(ANY_LOG);
        VANILLA_FUELS.add(ANY_PLANK);
        VANILLA_FUELS.add(ANY_SAPLING);
        VANILLA_FUELS.add(ANY_SIGN);
        VANILLA_FUELS.add(ANY_WOODEN_BUTTON);
        VANILLA_FUELS.add(ANY_WOODEN_DOOR);
        VANILLA_FUELS.add(ANY_WOODEN_SLAB);
        VANILLA_FUELS.add(ANY_WOOD_FENCE);
        VANILLA_FUELS.add(ANY_WOOD_FENCE_GATE);
        VANILLA_FUELS.add(ANY_WOOD_PRESSURE_PLATE);
        VANILLA_FUELS.add(ANY_WOOD_STAIR);
        VANILLA_FUELS.add(ANY_WOOD_TRAPDOOR);
        VANILLA_FUELS.add(ANY_WOOL);

        VANILLA_FUELS.add(BAMBOO);
        VANILLA_FUELS.add(BAMBOO_BLOCKS);
        VANILLA_FUELS.add(BAMBOO_MOSAIC);
        VANILLA_FUELS.add(BAMBOO_MOSAIC_SLAB);
        VANILLA_FUELS.add(BAMBOO_MOSAIC_STAIRS);

        VANILLA_FUELS.add(BARREL);
        VANILLA_FUELS.add(BLAZE_ROD);
        VANILLA_FUELS.add(BLOCK_OF_COAL);
        VANILLA_FUELS.add(BOOKSHELF);
        VANILLA_FUELS.add(BOW);
        VANILLA_FUELS.add(BOWL);

        VANILLA_FUELS.add(CARTOGRAPHY_TABLE);
        VANILLA_FUELS.add(CHARCOAL);
        VANILLA_FUELS.add(CHEST);
        VANILLA_FUELS.add(CHISELED_BOOKSHELF);
        VANILLA_FUELS.add(COAL);
        VANILLA_FUELS.add(COMPOSTER);
        VANILLA_FUELS.add(CRAFTING_TABLE);
        VANILLA_FUELS.add(CROSSBOW);

        VANILLA_FUELS.add(DAYLIGHT_DETECTOR);
        VANILLA_FUELS.add(DEAD_BUSH);
        VANILLA_FUELS.add(DRIED_KELP_BLOCK);

        VANILLA_FUELS.add(FISHING_ROD);
        VANILLA_FUELS.add(FLETCHING_TABLE);
        VANILLA_FUELS.add(JUKEBOX);

        VANILLA_FUELS.add(LADDER);
        VANILLA_FUELS.add(LAVA_BUCKET);
        VANILLA_FUELS.add(LECTERN);
        VANILLA_FUELS.add(LOOM);

        VANILLA_FUELS.add(MANGROVE_ROOTS);
        VANILLA_FUELS.add(NOTE_BLOCK);
        VANILLA_FUELS.add(SCAFFOLDING);
        VANILLA_FUELS.add(SMITHING_TABLE);
        VANILLA_FUELS.add(STICK);
        VANILLA_FUELS.add(TRAPPED_CHEST);

        VANILLA_FUELS.add(WOODEN_AXE);
        VANILLA_FUELS.add(WOODEN_HOE);
        VANILLA_FUELS.add(WOODEN_PICKAXE);
        VANILLA_FUELS.add(WOODEN_SHOVEL);
        VANILLA_FUELS.add(WOODEN_SWORD);
    }
    //</editor-fold>

    // Variables.
    /**
     * How long this fuel burns in the furnace.
     */
    private final int burnTime;

    /**
     * "Creates" a FurnaceFuel.
     *
     * @param name     The name, used for the key.
     * @param material The {@link Material} that this fuel is, converted to an {@link ItemStack}.
     * @param burnTime How long this fuel burns.
     */
    public FurnaceFuel(String name, Material material, int burnTime) {
        super(Utils.getKey("furnace_fuel_" + name), new ItemStack(material, 1), null);
        this.burnTime = burnTime;
    }

    /**
     * "Creates" a FurnaceFuel.
     *
     * @param name     The name, used for the key.
     * @param fuelItem The {@link ItemStack} that this fuel is.
     * @param burnTime How long this fuel burns.
     */
    public FurnaceFuel(String name, ItemStack fuelItem, int burnTime) {
        super(Utils.getKey("furnace_fuel_" + name), fuelItem, null);
        this.burnTime = burnTime;
    }

    /**
     * "Creates" a FurnaceFuel.
     *
     * @param name     The name, used for the key.
     * @param tag      The {@link Tag<Material>} for this fuel.
     * @param burnTime How long this fuel burns.
     */
    public FurnaceFuel(String name, Tag<Material> tag, int burnTime) {
        super(Utils.getKey("furnace_fuel_" + name), null, tag);
        this.burnTime = burnTime;
    }

    /**
     * Returns a new instance of {@link FurnaceFuel}.
     * <br/>
     * <b>This does not take into account if the fuel already exists.</b>
     *
     * @param name         The name to use as the key.
     * @param fuelMaterial The fuel material, used to create an {@link ItemStack}.
     * @param burnTime     The amount of burn time this fuel adds.
     * @return Returns a new {@link FurnaceFuel}.
     */
    public static @NotNull FurnaceFuel get(String name, Material fuelMaterial, int burnTime) {
        return new FurnaceFuel(name, fuelMaterial, burnTime);
    }

    /**
     * Returns a new instance of {@link FurnaceFuel}.
     * <br/>
     * <b>This does not take into account if the fuel already exists.</b>
     *
     * @param name          The name to use as the key.
     * @param fuelItemStack The fuel item stack.
     * @param burnTime      The amount of burn time this fuel adds.
     * @return Returns a new {@link FurnaceFuel}.
     */
    public static @NotNull FurnaceFuel get(String name, ItemStack fuelItemStack, int burnTime) {
        return new FurnaceFuel(name, fuelItemStack, burnTime);
    }

    /**
     * Returns a new instance of {@link FurnaceFuel}.
     * <br/>
     * <b>This does not take into account if the fuel already exists.</b>
     *
     * @param name     The name to use as the key.
     * @param fuelTag  The {@link Tag} that belongs to this fuel.
     * @param burnTime The amount of burn time this fuel adds.
     * @return Returns a new {@link FurnaceFuel}.
     */
    public static @NotNull FurnaceFuel get(String name, Tag<Material> fuelTag, int burnTime) {
        return new FurnaceFuel(name, fuelTag, burnTime);
    }

    /**
     * Returns the {@link Fuel#fuelItem} as it's underlying {@link Material}.
     *
     * @return The {@link Material} if there is a fuel item, otherwise <code>null</code>.
     */
    public final @Nullable Material getFuel() {
        return this.getFuelItem() == null ? null : this.fuelItem.getType();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        FurnaceFuel that = (FurnaceFuel) o;
        return getBurnTime() == that.getBurnTime();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBurnTime());
    }

    @Override
    public String toString() {
        return "FurnaceFuel{" +
                "burnTime=" + burnTime +
                "} " + super.toString();
    }
}
