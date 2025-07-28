package me.levyuri.anticheatbase.tasks;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.checks.types.Check;
import org.bukkit.scheduler.BukkitRunnable;

public class ViolationTask extends BukkitRunnable {

    private final Anticheat plugin;

    public ViolationTask(final Anticheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        this.plugin.getProfileManager().getProfileMap().values().forEach(profile -> {
            for (final Check check : profile.getCheckHolder().getChecks()) check.resetVl();
        });
    }

}
