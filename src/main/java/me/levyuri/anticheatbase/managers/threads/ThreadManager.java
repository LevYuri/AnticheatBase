package me.levyuri.anticheatbase.managers.threads;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.managers.Initializer;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.utils.MiscUtils;
import me.levyuri.anticheatbase.utils.custom.exception.AnticheatException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ThreadManager implements Listener, Initializer {

    //Get a proper thread limit
    private static final int MAX_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    private final List<ProfileThread> profileThreads = new ArrayList<>();

    private final Anticheat plugin;

    public ThreadManager(final Anticheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, Anticheat.getInstance());
    }

    public ProfileThread getAvailableProfileThread() {

        final ProfileThread profileThread;

        //Check whether or not we should create a new thread based on the thread limit
        if (this.profileThreads.size() < MAX_THREADS) {

            //Create a new profile thread and set it to our variable in order to use it
            profileThread = new ProfileThread();

            //Add our new profile thread to the list in order to use it for future profiles
            this.profileThreads.add(profileThread);

        } else {

            //Get an available thread based on the profiles using it, Otherwise grab a random element to avoid issues.
            profileThread = this.profileThreads
                    .stream()
                    .min(Comparator.comparing(ProfileThread::getProfileCount))
                    .orElse(MiscUtils.randomElement(this.profileThreads));
        }

        //Throw an exception if the profile thread is null, Which should be impossible.
        if (profileThread == null) {
            throw new AnticheatException("Encountered a null profile thread, Please restart the server to avoid any issues.");
        }

        //Return the available thread and increment the profile count
        return profileThread.incrementAndGet();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onQuit(final PlayerQuitEvent e) {

        final Profile profile = this.plugin.getProfileManager().getProfile(e.getPlayer());

        if (profile == null) return;

        final ProfileThread profileThread = profile.getProfileThread();

        if (profileThread.getProfileCount() > 1) {

            profileThread.decrement();

            return;
        }

        this.profileThreads.remove(profileThread.shutdownThread());
    }

    @Override
    public void shutdown() {
        this.profileThreads.forEach(ProfileThread::shutdownThread);

        this.profileThreads.clear();
    }
}