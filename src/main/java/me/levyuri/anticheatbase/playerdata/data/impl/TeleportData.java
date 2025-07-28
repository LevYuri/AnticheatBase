package me.levyuri.anticheatbase.playerdata.data.impl;

import me.levyuri.anticheatbase.playerdata.data.Data;
import me.levyuri.anticheatbase.processors.Packet;

public class TeleportData implements Data {

    private final int teleportTicks = 2;

    @Override
    public void process(final Packet packet) {

    }

    public int getTeleportTicks() {
        return teleportTicks;
    }
}
