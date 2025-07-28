package me.levyuri.anticheatbase.utils;

import me.levyuri.anticheatbase.Anticheat;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public final class TaskUtils {

    private TaskUtils() {
    }

    public static BukkitTask taskTimer(final Runnable runnable, final long delay, final long interval) {
        return Bukkit.getScheduler().runTaskTimer(Anticheat.getInstance(), runnable, delay, interval);
    }

    public static BukkitTask taskTimerAsync(final Runnable runnable, final long delay, final long interval) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Anticheat.getInstance(), runnable, delay, interval);
    }

    public static BukkitTask task(final Runnable runnable) {
        return Bukkit.getScheduler().runTask(Anticheat.getInstance(), runnable);
    }

    public static BukkitTask taskAsync(final Runnable runnable) {
        return Bukkit.getScheduler().runTaskAsynchronously(Anticheat.getInstance(), runnable);
    }

    public static BukkitTask taskLater(final Runnable runnable, final long delay) {
        return Bukkit.getScheduler().runTaskLater(Anticheat.getInstance(), runnable, delay);
    }

    public static BukkitTask taskLaterAsync(final Runnable runnable, final long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Anticheat.getInstance(), runnable, delay);
    }
}
