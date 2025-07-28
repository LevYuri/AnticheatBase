package me.levyuri.anticheatbase.enums;

import me.levyuri.anticheatbase.utils.ChatUtils;

import java.util.Arrays;
import java.util.List;

public enum MsgType {
    PREFIX("&8「&cAnticheat&8」&7»&r"),
    NO_PERMISSION(PREFIX.getMessage() + "No perms"),
    CONSOLE_COMMANDS(PREFIX.getMessage() + "Not for console"),
    ALERT_MESSAGE(PREFIX.getMessage() + "&f%player% &7failed &b%check% &7x%vl%"),
    ALERT_HOVER(stringFromList(Arrays.asList(
            "&7Description:&r",
            "%description%",
            "",
            "&7Information:&r",
            "%information%",
            "",
            "&7TPS: &r%tps%",
            "",
            "&fClick to teleport"
    )));

    private final String message;

    MsgType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    private static String stringFromList(final List<String> list) {

        final StringBuilder sb = new StringBuilder();

        final int size = list.size();

        for (int i = 0; i < size; i++) {

            sb.append(list.get(i));

            if (size - 1 != i) sb.append("\n");
        }

        return ChatUtils.format(sb.toString());
    }
}
