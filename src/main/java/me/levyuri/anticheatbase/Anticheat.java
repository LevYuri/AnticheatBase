package me.levyuri.anticheatbase;

import com.github.retrooper.packetevents.PacketEvents;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import me.levyuri.anticheatbase.files.Checks;
import me.levyuri.anticheatbase.files.Config;
import me.levyuri.anticheatbase.files.commentedfiles.CommentedFileConfiguration;
import me.levyuri.anticheatbase.listeners.ProfileListener;
import me.levyuri.anticheatbase.listeners.ViolationListener;
import me.levyuri.anticheatbase.managers.AlertManager;
import me.levyuri.anticheatbase.managers.profile.ProfileManager;
import me.levyuri.anticheatbase.managers.themes.ThemeManager;
import me.levyuri.anticheatbase.managers.threads.ThreadManager;
import me.levyuri.anticheatbase.nms.NMSManager;
import me.levyuri.anticheatbase.processors.listeners.BukkitListener;
import me.levyuri.anticheatbase.processors.listeners.NetworkListener;
import me.levyuri.anticheatbase.tasks.TickTask;
import me.levyuri.anticheatbase.tasks.ViolationTask;
import me.levyuri.anticheatbase.utils.ChatUtils;
import me.levyuri.anticheatbase.utils.MiscUtils;
import me.levyuri.anticheatbase.utils.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public class Anticheat extends JavaPlugin {

    private static Anticheat INSTANCE;

    private ProfileManager profileManager;
    private ThreadManager threadManager;
    private AlertManager alertManager;
    private NMSManager nmsManager;
    private ThemeManager themeManager;

    private Config configuration;
    private Checks checks;

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        //Are all listeners read only?
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false)
                .checkForUpdates(false);
        PacketEvents.getAPI().load();
    }

    public void onEnable() {

        INSTANCE = this;

        this.profileManager = new ProfileManager();
        this.threadManager = new ThreadManager(this);
        this.alertManager = new AlertManager();
        this.nmsManager = new NMSManager();
        this.themeManager = new ThemeManager(this);
        this.configuration = new Config(this);
        this.checks = new Checks(this);

        this.profileManager.initialize();
        this.threadManager.initialize();
        this.alertManager.initialize();
        //this.themeManager.initialize(); // TODO: Fix
        this.configuration.initialize();
        this.checks.initialize();

        // Bukkit Listeners
        Arrays.asList(
                new ProfileListener(this),
                new ViolationListener(this),
                new BukkitListener()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        PacketEvents.getAPI().getEventManager().registerListener(new NetworkListener(this));
        PacketEvents.getAPI().init();

        // We're most likely going to be using transactions - ping pongs, So we need to do this for ViaVersion
        System.setProperty("com.viaversion.handlePingsAsInvAcknowledgements", "true");

        // Initialize static variables to make sure our threads won't get affected when they run for the first time.
        try {

            MiscUtils.initializeClasses(
                    "me.levyuri.anticheatbase.utils.fastmath.FastMath",
                    "me.levyuri.anticheatbase.utils.fastmath.FastMathLiteralArrays",
                    "me.levyuri.anticheatbase.utils.fastmath.NumbersUtils",
                    "me.levyuri.anticheatbase.utils.minecraft.MathHelper",
                    "me.levyuri.anticheatbase.utils.CollisionUtils",
                    "me.levyuri.anticheatbase.utils.MoveUtils"
            );

        } catch (final ClassNotFoundException e) {

            // Impossible unless we made a mistake
            ChatUtils.log(Level.SEVERE, "An error was thrown during initialization, The anticheat may not work properly.");

            e.printStackTrace();
        }

        new TickTask(this).runTaskTimerAsynchronously(this, 50L, 0L);

        new ViolationTask(this).runTaskTimerAsynchronously(this,
                Config.Setting.CHECK_SETTINGS_VIOLATION_RESET_INTERVAL.getLong() * 1200L,
                Config.Setting.CHECK_SETTINGS_VIOLATION_RESET_INTERVAL.getLong() * 1200L);
    }

    public void onDisable() {

        this.profileManager.shutdown();
        this.threadManager.shutdown();
        this.alertManager.shutdown();
        this.themeManager.shutdown();
        this.configuration.shutdown();
        this.checks.shutdown();

        Bukkit.getScheduler().cancelTasks(this);

        PacketEvents.getAPI().terminate();

        // Clear reflection cache
        ReflectionUtils.clear();

        INSTANCE = null;
    }

    public static Anticheat getInstance() {
        return INSTANCE;
    }

    public ProfileManager getProfileManager() {
        return this.profileManager;
    }

    public ThreadManager getThreadManager() {
        return this.threadManager;
    }

    public AlertManager getAlertManager() {
        return this.alertManager;
    }

    public NMSManager getNmsManager() {
        return this.nmsManager;
    }

    public ThemeManager getThemeManager() {
        return this.themeManager;
    }

    public CommentedFileConfiguration getConfiguration() {
        return this.configuration.getConfig();
    }

    public CommentedFileConfiguration getChecks() {
        return this.checks.getConfig();
    }
}
