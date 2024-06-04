package com.itsschatten.portablecrafting.virtual.machine;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Base Machine.
 */
@Getter
public abstract class Machine {

    /**
     * Title for this machine.
     */
    private final String name;

    /**
     * The unique id of the machine.
     */
    private final UUID uniqueId;

    /**
     * The date when this machine was last opened.
     */
    // We don't care if this is in the equals or hash.
    @Setter
    private LocalDateTime lastOpened;

    /**
     * Creates a Machine.
     *
     * @param name       The title for this machine.
     * @param uniqueId   The {@link UUID} for this machine.
     * @param lastOpened When this inventory was last opened, may be null.
     */
    public Machine(@NotNull String name, @NotNull UUID uniqueId, @Nullable LocalDateTime lastOpened) {
        this.name = name;
        this.uniqueId = uniqueId;
        this.lastOpened = lastOpened == null ? LocalDateTime.now() : lastOpened;
    }

    /**
     * Called before opening a machine that has already been created previously.
     *
     * @implNote This method must be called by a custom implementation, this method is never called in vanilla unless opening an already created machine.
     */
    public abstract void preOpen();

    /**
     * Tick this machine.
     */
    public abstract void tick();

    /**
     * Open this machine's inventory.
     *
     * @param player The player opening this inventory.
     */
    public abstract void openInventory(Player player);

    /**
     * Forcefully closes the machine's inventory.
     */
    public abstract void forceCloseInventory();

    @Override
    public String toString() {
        return "Machine{" +
                "name='" + name + '\'' +
                ", uniqueId=" + uniqueId +
                ", lastOpened=" + lastOpened +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Machine machine = (Machine) o;
        return Objects.equals(getName(), machine.getName()) && Objects.equals(getUniqueId(), machine.getUniqueId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUniqueId());
    }
}
