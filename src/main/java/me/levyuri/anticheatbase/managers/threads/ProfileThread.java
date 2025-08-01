package me.levyuri.anticheatbase.managers.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileThread {

    private final ExecutorService thread = Executors.newSingleThreadExecutor();

    private int profileCount;

    public void execute(final Runnable runnable) {

        //Fixes strange issues when the thread has been shut down and the packet has been delayed
        if (this.thread.isShutdown()) return;

        this.thread.execute(runnable);
    }

    public int getProfileCount() {
        return this.profileCount;
    }

    public ProfileThread incrementAndGet() {

        this.profileCount++;

        return this;
    }

    public void decrement() {
        this.profileCount--;
    }

    public ProfileThread shutdownThread() {

        this.thread.shutdownNow();

        return this;
    }
}