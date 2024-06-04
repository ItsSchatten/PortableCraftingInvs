package com.itsschatten.portablecrafting.virtual.fuel;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents fuel that can be used in a {@link BrewingStand BrewingStand}.
 */
@Getter
public class BrewingFuel extends Fuel {

    // Default blaze powder.
    public static final BrewingFuel BLAZE_POWDER = get("blaze_powder", Material.BLAZE_POWDER, 20);

    /**
     * The duration to add to the brewing stand.
     */
    private final int duration;

    /**
     * "Creates" a new BrewingFuel.
     *
     * @param name     The name, used for the key.
     * @param item     The Material for the fuel.
     * @param duration How many iterations this fuel adds to the brewing stand.
     */
    public BrewingFuel(String name, Material item, int duration) {
        super(Utils.getKey("brewing_fuel_" + name), new ItemStack(item, 1), null);
        this.duration = duration;
    }

    /**
     * "Creates" a new BrewingFuel.
     *
     * @param name     The name, used for the key.
     * @param item     The Material for the fuel.
     * @param duration How many iterations this fuel adds to the brewing stand.
     */
    public BrewingFuel(String name, ItemStack item, int duration) {
        super(Utils.getKey("brewing_fuel_" + name), item, null);
        this.duration = duration;
    }

    /**
     * Returns a new instance of {@link BrewingFuel}.
     * <br/>
     * <b>This does not take into account if the fuel already exists.</b>
     *
     * @param name     The name to use as the key.
     * @param material The actual fuel {@link Material}.
     * @param duration The amount of operations this fuel adds.
     * @return Returns a new {@link BrewingFuel}.
     */
    public static @NotNull BrewingFuel get(String name, Material material, int duration) {
        return new BrewingFuel(name, material, duration);
    }

    /**
     * Returns a new instance of {@link BrewingFuel}.
     * <br/>
     * <b>This does not take into account if the fuel already exists.</b>
     *
     * @param name     The name to use as the key.
     * @param item     The fuel item stack.
     * @param duration The amount of operations this fuel adds.
     * @return Returns a new {@link BrewingFuel}.
     */
    public static @NotNull BrewingFuel get(String name, ItemStack item, int duration) {
        return new BrewingFuel(name, item, duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BrewingFuel that = (BrewingFuel) o;
        return getDuration() == that.getDuration();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getDuration());
    }

    @Override
    public String toString() {
        return "BrewingFuel{" +
                "duration=" + duration +
                "} " + super.toString();
    }
}
