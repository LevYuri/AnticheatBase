package me.levyuri.anticheatbase.listeners;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.api.events.AnticheatViolationEvent;
import me.levyuri.anticheatbase.enums.MsgType;
import me.levyuri.anticheatbase.files.Config;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.tasks.TickTask;
import me.levyuri.anticheatbase.utils.ChatUtils;
import me.levyuri.anticheatbase.utils.JsonBuilder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class ViolationListener implements Listener {

    private final Anticheat plugin;

    public ViolationListener(final Anticheat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onViolation(final AnticheatViolationEvent e) {

        this.plugin.getAlertManager().getAlertExecutor().execute(() -> {

            final Player p = e.getPlayer();

            if (p == null || !p.isOnline()) return;

            final Profile profile = this.plugin.getProfileManager().getProfile(p);

            if (profile == null) return;

            final String tps = String.valueOf(TickTask.getTPS());

            final String checkType = e.getType();

            final String check = (checkType.isEmpty() + " (" + checkType + ")")
                    + (e.isExperimental() ? " (Experimental)" : "");

            final String description = e.getDescription();

            final String information = e.getInformation();

            final String playerName = p.getName();

            final int vl = e.getVl();

            // TODO: Logs?
            /*
            this.plugin.getLogManager().addLogToQueue(new PlayerLog(
                    Config.Setting.SERVER_NAME.getString(),
                    playerName,
                    p.getUniqueId().toString(),
                    check,
                    information
            ));
             */

            //We're sending the alerts by using the server chat packet, Making this much more efficient.
            alerts:
            {

                final String hoverMessage = MsgType.ALERT_HOVER.getMessage()
                        .replace("%description%", description)
                        .replace("%information%", information)
                        .replace("%tps%", tps);

                final String alertMessage = MsgType.ALERT_MESSAGE.getMessage()
                        .replace("%player%", playerName)
                        .replace("%check%", check)
                        .replace("%vl%", String.valueOf(vl));

                final JsonBuilder jsonBuilder = new JsonBuilder(alertMessage)
                        .setHoverEvent(JsonBuilder.HoverEventType.SHOW_TEXT, hoverMessage)
                        .setClickEvent(JsonBuilder.ClickEventType.RUN_COMMAND, "/tp " + playerName)
                        .buildText();

                jsonBuilder.sendMessage(this.plugin.getAlertManager().getPlayersWithAlerts());

                if (!Config.Setting.CHECK_SETTINGS_ALERT_CONSOLE.getBoolean()) break alerts;

                //We're using bukkit's logger in order for the prefix to not show twice
                Bukkit.getLogger().info(ChatUtils.stripColorCodes(alertMessage));
            }
        });
    }
}