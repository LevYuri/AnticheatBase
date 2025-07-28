package me.levyuri.anticheatbase.files;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.files.commentedfiles.CommentedFileConfiguration;
import me.levyuri.anticheatbase.managers.Initializer;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config implements Initializer {

    private static final String[] HEADER = new String[]{
            "" // TODO: Put ASCII Art header
    };

    private final Anticheat plugin;
    private CommentedFileConfiguration configuration;
    private static boolean exists;

    public Config(final Anticheat plugin) {
        this.plugin = plugin;
    }

    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    @Override
    public void initialize() {

        final File configFile = new File(this.plugin.getDataFolder(), "config.yml");

        exists = configFile.exists();

        final boolean setHeaderFooter = !configFile.exists();

        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.plugin, configFile);

        if (setHeaderFooter) this.configuration.addComments(HEADER);

        for (final Setting setting : Setting.values()) {

            setting.reset();

            changed |= setting.setIfNotExists(this.configuration);
        }

        if (changed) this.configuration.save();
    }

    @Override
    public void shutdown() {
        for (final Setting setting : Setting.values()) setting.reset();
    }

    public enum Setting {
        SERVER_NAME("server_name", "Server", "The server name that will be shown in Player Logs"),

        THEME("theme", "default", "The theme that the anticheat is going to use"),

        TOGGLE_ALERTS_ON_JOIN("toggle_alerts_on_join", true, "Should we enable alerts for admins when they join?"),

        DISABLE_BYPASS_PERMISSION("disable_bypass_permission", true, "Should we disable the bypass permission?", "Disable this for some perfomance gain"),

        GHOST_MODE("ghost_mode", true, "Should we enable the Ghost Mode?", "If enabled the Anticheat will use Setbacks for movement related checks"),

        CHECK_SETTINGS("check_settings", "", "Check Settings"),
        CHECK_SETTINGS_ALERT_CONSOLE("check_settings.alert_console", false, "Should we also send alerts in console?"),
        CHECK_SETTINGS_VIOLATION_RESET_INTERVAL("check_settings.violation_reset_interval", 5, "How often should we clear the player violations? (In minutes)"),

        LOGS("logs", "", "Log Settings"),
        LOGS_ENABLED("logs.enabled", true, "Should we enable logging?"),
        LOGS_TYPE("logs.type", "YAML", "What type of Database should we use for logging?"),
        LOGS_CLEAR_DAYS("logs.clear_days", 5, "Logs older than this value of Days will be cleared");

        private final String key;
        private final Object defaultValue;
        private boolean excluded;
        private final String[] comments;
        private Object value = null;

        Setting(final String key, final Object defaultValue, final String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        Setting(final String key, final Object defaultValue, final boolean excluded, final String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
            this.excluded = excluded;
        }

        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        public String getKey() {
            return this.key;
        }

        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        public String getString() {
            this.loadValue();
            return String.valueOf(this.value);
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        private boolean setIfNotExists(final CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (exists && this.excluded) return false;

            if (fileConfiguration.get(this.key) == null) {
                final List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }

                return true;
            }

            return false;
        }

        public void reset() {
            this.value = null;
        }

        public boolean isSection() {
            return this.defaultValue == null;
        }

        private void loadValue() {
            if (this.value != null) return;
            this.value = Anticheat.getInstance().getConfiguration().get(this.key);
        }
    }
}
