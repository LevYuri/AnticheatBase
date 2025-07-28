package me.levyuri.anticheatbase.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import static io.github.retrooper.packetevents.util.SpigotReflectionUtil.getChannel;

public class JsonBuilder {

    // Main message text
    private static String text;
    // Basic JSON message, without any events
    private static String json = "{\"text\":\"" + text + "\"}";
    // Value of the hover events
    private String hover;
    // Value of the click events
    private String click;

    private HoverEventType hoverAction;

    private ClickEventType clickAction;

    public JsonBuilder(final String text) {
        JsonBuilder.text = ChatUtils.format(text);
    }

    public JsonBuilder setHoverEvent(final JsonBuilder.HoverEventType type, final String value) {
        hover = ChatUtils.format(value);
        hoverAction = type;
        return this;
    }

    public JsonBuilder setClickEvent(final JsonBuilder.ClickEventType type, final String value) {
        click = value;
        clickAction = type;
        return this;
    }

    public JsonBuilder buildText() {

        if (!getClick().isPresent() && !getHover().isPresent()) json = "{\"text\":\"" + text + "\"}";

        if (!getClick().isPresent() && getHover().isPresent()) {

            if (hoverAction == HoverEventType.SHOW_ACHIEVEMENT) {

                json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"achievement." + hover + "\"}}";

            } else if (hoverAction == HoverEventType.SHOW_STATISTIC) {

                json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"stat." + hover + "\"}}";

            } else {

                json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"" + hover + "\"}}";
            }
        }

        if (getClick().isPresent() && getHover().isPresent()) {

            json = "{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickAction.getActionName() + "\",\"value\":\"" + click + "\"},\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"" + hover + "\"}}";
        }

        if (getClick().isPresent() && !getHover().isPresent()) {

            json = "{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickAction.getActionName() + "\",\"value\":\"" + click + "\"}}";
        }

        return this;
    }

    public void sendMessage(final Player player) {

        final User user = (User) player;

        user.sendMessage(AdventureSerializer.parseComponent(json));
    }

    public void sendMessage(final Collection<? extends UUID> players) {

        final ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();

        final Object chatPacket;

        if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {

            chatPacket = new WrapperPlayServerSystemChatMessage(false, AdventureSerializer.parseComponent(json));

        } else {

            final ChatMessage message;

            if (version.isNewerThanOrEquals(ServerVersion.V_1_16)) {

                message = new ChatMessage_v1_16(AdventureSerializer.parseComponent(json), ChatTypes.CHAT, new UUID(0L, 0L));

            } else {

                message = new ChatMessageLegacy(AdventureSerializer.parseComponent(json), ChatTypes.CHAT);

            }

            chatPacket = new WrapperPlayServerChatMessage(message);
        }

        for (final UUID uuid : players) {

            final Player player = Bukkit.getPlayer(uuid);

            if (player == null) continue;

            PacketEvents.getAPI().getProtocolManager().sendPacket(getChannel(player), (PacketWrapper)chatPacket);
        }
    }

    public String getUnformattedText() {
        return text;
    }

    public String getJson() {
        return json;
    }

    private Optional<String> getHover() {
        return Optional.of(hover);
    }

    private Optional<String> getClick() {
        return Optional.of(click);
    }

    public enum ClickEventType {

        OPEN_URL("open_url"),

        RUN_COMMAND("run_command"),

        SUGGEST_TEXT("suggest_command");

        // JSON name of the events
        private final String actionName;

        ClickEventType(final String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return actionName;
        }
    }

    public enum HoverEventType {

        SHOW_TEXT("show_text"),

        SHOW_ITEM("show_item"),

        SHOW_ACHIEVEMENT("show_achievement"),

        /**
         * Show a statistic on hover (the same action name is due to them being the same, however the prefix is different)
         * Since achievements take achievement.AchievementName, while statistics take stat.StatisticName
         */
        SHOW_STATISTIC("show_achievement");

        // JSON name of the event
        private final String actionName;

        HoverEventType(final String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return actionName;
        }
    }
}
