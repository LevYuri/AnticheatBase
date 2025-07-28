package me.levyuri.anticheatbase.playerdata.data.impl;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.playerdata.data.Data;
import me.levyuri.anticheatbase.processors.Packet;
import me.levyuri.anticheatbase.utils.MiscUtils;
import me.levyuri.anticheatbase.utils.custom.PlacedBlock;
import me.levyuri.anticheatbase.utils.custom.desync.Desync;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ActionData implements Data {

    private final GameMode gameMode;

    private boolean sneaking;

    private final Desync desync;

    private PlacedBlock placedBlock;

    private final ItemStack itemInMainHand = MiscUtils.EMPTY_ITEM;
    private final ItemStack itemInOffHand = MiscUtils.EMPTY_ITEM;

    private int lastAllowFlightTicks, lastSleepingTicks, lastRidingTicks;

    public ActionData(final Profile profile) {

        this.desync = new Desync(profile);

        //Initialize

        Player player = profile.getPlayer();

        this.gameMode = player.getGameMode();

        boolean allowFlight = Anticheat.getInstance().getNmsManager().getNmsInstance().getAllowFlight(player);

        this.lastAllowFlightTicks = allowFlight ? 0 : 100;
    }

    @Override
    public void process(final Packet packet) {
    }

    public int getLastRidingTicks() {
        return lastRidingTicks;
    }

    public PlacedBlock getPlacedBlock() {
        return placedBlock;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public ItemStack getItemInMainHand() {
        return itemInMainHand;
    }

    public ItemStack getItemInOffHand() {
        return itemInOffHand;
    }

    public Desync getDesync() {
        return desync;
    }

    public int getLastSleepingTicks() {
        return lastSleepingTicks;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
