package me.levyuri.anticheatbase.managers;

import me.levyuri.anticheatbase.Anticheat;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AlertManager implements Listener, Initializer {

    private final ExecutorService alertExecutor = Executors.newSingleThreadExecutor();

    private final List<UUID> playersWithAlerts = new ArrayList<>();

    @Override
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, Anticheat.getInstance());
    }

    public ExecutorService getAlertExecutor() {
        return alertExecutor;
    }

    public List<UUID> getPlayersWithAlerts() {
        return playersWithAlerts;
    }

    public void addPlayerToAlerts(final UUID uuid) {
        this.playersWithAlerts.add(uuid);
    }

    public void removePlayerFromAlerts(final UUID uuid) {
        this.playersWithAlerts.remove(uuid);
    }

    public boolean hasAlerts(final UUID uuid) {
        return this.playersWithAlerts.contains(uuid);
    }

    //Make sure we dont get a memory leak
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent e) {
        removePlayerFromAlerts(e.getPlayer().getUniqueId());
    }

    @Override
    public void shutdown() {
        this.playersWithAlerts.clear();
    }
}