package com.minekarta.gemseconomyreloaded.utils;

import com.minekarta.gemseconomyreloaded.GemsEconomyReloaded;
import org.bukkit.Bukkit;

public class SchedulerUtils {

    public static void runLater(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(GemsEconomyReloaded.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(GemsEconomyReloaded.getInstance(), runnable, delay);
    }

    /**
     * Runs a task on another thread immediately.
     *
     * @param runnable - Task to perform.
     */
    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(GemsEconomyReloaded.getInstance(), runnable);
    }

    /**
     * Runs a task on the main thread immediately
     *
     * @param runnable - Task to perform
     */
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(GemsEconomyReloaded.getInstance(), runnable);
    }
}
