package me.levyuri.anticheatbase.files;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.files.commentedfiles.CommentedFileConfiguration;
import me.levyuri.anticheatbase.managers.Initializer;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Checks implements Initializer {

    private static final String[] HEADER = new String[]{
            "" // TODO: Put ASCII Art header
    };

    private final Anticheat plugin;
    private CommentedFileConfiguration configuration;
    private static boolean exists;

    public Checks(final Anticheat plugin) {
        this.plugin = plugin;
    }

    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    @Override
    public void initialize() {

        final File configFile = new File(this.plugin.getDataFolder(), "checks.yml");

        exists = configFile.exists();

        final boolean setHeaderFooter = !exists;

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
        AIM("Aim", "", "AimAssist Check"),
        AIM_A("aim.a", true, "Should we enable this module?"),
        AIMA_MAX_VL("aim.max_vl", 10, "The maximum violation amount a player needs to reach in order to get punished"),
        AIMA_COMMANDS("aim.commands", Collections.singletonList("kick %player% Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount"),

        BADPACKETS("BadPackets", "", "BadPackets Check"),
        BADPACKETS_A("badpackets.a", true, "Should we enable this module?"),
        BADPACKETSA_MAX_VL("badpackets.max_vl", 10, "The maximum violation amount a player needs to reach in order to get punished"),
        BADPACKETSA_COMMANDS("badpackets.commands", Collections.singletonList("kick %player% Unfair Advantage"), "The commands that will get executed once a player reaches the maximum violation amount");

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
