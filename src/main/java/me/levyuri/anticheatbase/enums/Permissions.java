package me.levyuri.anticheatbase.enums;

public enum Permissions {
    ADMIN("anticheat.admin"),
    BYPASS("anticheat.bypass"),
    COMMAND_ALERTS("anticheat.commands.alerts");

    private final String permission;

    Permissions(final String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
