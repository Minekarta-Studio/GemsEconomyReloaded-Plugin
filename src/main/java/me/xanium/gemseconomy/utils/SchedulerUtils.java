package me.xanium.gemseconomy.utils;

import me.xanium.gemseconomy.GemsEconomy;
import org.bukkit.Bukkit;

public class SchedulerUtils {

    public static void runLater(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLater(GemsEconomy.getInstance(), runnable, delay);
    }

    public static void runLaterAsync(long delay, Runnable runnable) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(GemsEconomy.getInstance(), runnable, delay);
    }

    /**
     * Runs a task on another thread immediately.
     *
     * @param runnable - Task to perform.
     */
    public static void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(GemsEconomy.getInstance(), runnable);
    }

    /**
     * Runs a task on the main thread immediately
     *
     * @param runnable - Task to perform
     */
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(GemsEconomy.getInstance(), runnable);
    }
}
