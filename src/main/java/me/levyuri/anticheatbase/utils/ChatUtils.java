package me.levyuri.anticheatbase.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import me.levyuri.anticheatbase.Anticheat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

public final class ChatUtils {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '&' + "[0-9A-FK-OR]");

    public static String format(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String stripColorCodes(final String input) {
        return ChatColor.stripColor(STRIP_COLOR_PATTERN.matcher(input).replaceAll(""));
    }

    public static Component deserialize(final String message) {
        final MiniMessage minimessage = MiniMessage.builder()
                .strict(true)
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(TagResolver.standard())
                        .build()
                )
                .build();
        return minimessage.deserialize(message);
    }

    public static void sendMessage(final ChatMessage message, final Collection<? extends UUID> players) {

        final WrapperPlayServerChatMessage chat = new WrapperPlayServerChatMessage(message);

        for (final UUID uuid : players) {

            final Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;

            PacketEvents.getAPI().getProtocolManager().sendPackets(player, chat);
        }
    }

    public static void log(final Level level, final String message) {
        Anticheat.getInstance().getLogger().log(level, message);
    }
}
