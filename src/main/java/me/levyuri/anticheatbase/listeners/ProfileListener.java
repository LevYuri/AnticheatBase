package me.levyuri.anticheatbase.listeners;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.enums.Permissions;
import me.levyuri.anticheatbase.files.Config;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ProfileListener implements Listener {

    private final Anticheat plugin;

    public ProfileListener(final Anticheat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onJoin(final PlayerJoinEvent e) {

        final Player player = e.getPlayer();

        this.plugin.getProfileManager().createProfile(player);

        if (Config.Setting.TOGGLE_ALERTS_ON_JOIN.getBoolean() && player.hasPermission(Permissions.COMMAND_ALERTS.getPermission())) {

            this.plugin.getAlertManager().addPlayerToAlerts(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onLeave(final PlayerQuitEvent e) {
        this.plugin.getProfileManager().removeProfile(e.getPlayer());
    }
}
