package com.itsschatten.portablecrafting.virtual.utils;

import com.itsschatten.libs.Utils;
import com.itsschatten.portablecrafting.virtual.VirtualManager;
import com.itsschatten.portablecrafting.virtual.machine.BrewingStand;
import com.itsschatten.portablecrafting.virtual.machine.Furnace;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Task that will tick all open brewing stands and furnaces.
 */
public class TickTask extends BukkitRunnable {

    /**
     * Identification number for this task, used to cancel this task.
     */
    private final int id;

    /**
     * If this task is currently running.
     */
    private boolean running;

    /**
     * Constructs this task, setting it to run after 15 ticks and then run every tick thereafter.
     */
    public TickTask() {
        this.running = true;
        this.id = this.runTaskTimerAsynchronously(Utils.getInstance(), 15, 1).getTaskId();

        Utils.debugLog("Set running the ticking task with ID: " + this.id);
    }

    /**
     * Cancels this task.
     *
     * @throws IllegalStateException If the task was not scheduled yet.
     */
    @Override
    public synchronized void cancel() throws IllegalStateException {
        if (running) {
            this.running = false;
        }

        Bukkit.getScheduler().cancelTask(id);
    }

    /**
     * Runs the task, called every tick.
     */
    @Override
    public void run() {
        // Check if we have any open furnaces OR open brewing stands, if not, don't continue.
        if (VirtualManager.getInstance().getOpenFurnaces().isEmpty() && VirtualManager.getInstance().getOpenBrewingStands().isEmpty()) {
            return;
        }

        // Are we even running?
        if (!running) {
            // We shouldn't be running, cancel!
            cancel();
            return;
        }

        try {
            // Loop all open brewing stands and furnaces and tick them.
            for (final Furnace furnace : VirtualManager.getInstance().getOpenFurnaces().values()) furnace.tick();
            for (final BrewingStand stand : VirtualManager.getInstance().getOpenBrewingStands().values()) stand.tick();
        } catch (Exception ex) {
            Utils.logError(ex);
            Utils.logError("Failed to tick virtual tile entities!");
        }
    }
}
