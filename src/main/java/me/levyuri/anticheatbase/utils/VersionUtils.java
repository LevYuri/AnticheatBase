package me.levyuri.anticheatbase.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class VersionUtils {

    public static String getClientVersionAsString(final Player player) {
        return PacketEvents.getAPI().getPlayerManager().getClientVersion(player).name().replaceAll("_", ".").substring(2);
    }

    public static ClientVersion getClientVersion(final Player player) {
        return PacketEvents.getAPI().getPlayerManager().getClientVersion(player);
    }

    public static ServerVersion getServerVersion() {
        final String serverPackageName = Bukkit.getServer().getClass().getPackage().getName();

        ServerVersion version;

        try {

            version = ServerVersion.valueOf(serverPackageName.substring(serverPackageName.lastIndexOf(".") + 1).trim());

        } catch (final IllegalArgumentException e) {

            version = ServerVersion.V_1_7_10;
        }

        return version;
    }
    // TODO: Fix getServerVersion

}