package me.levyuri.anticheatbase.api.events;

import me.levyuri.anticheatbase.checks.enums.CheckType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AnticheatViolationEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final CheckType check;
    private final String description;
    private final String type;
    private final String information;
    private final int vl;
    private final int maxVl;
    private final boolean experimental;
    private boolean cancel = false;

    // WARNING: Async!
    public AnticheatViolationEvent(final Player player, final CheckType check, final String description, final String type, final String information, final int vl, final int maxVl, final boolean experimental) {
        super(true);
        this.player = player;
        this.check = check;
        this.description = description;
        this.type = type;
        this.information = information;
        this.vl = vl;
        this.maxVl = maxVl;
        this.experimental = experimental;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(final boolean cancel) {
        this.cancel = cancel;
    }

    public CheckType getCheck() {
        return check;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public int getVl() {
        return vl;
    }

    public int getMaxVl() {
        return maxVl;
    }

    public String getInformation() {
        return information;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}
