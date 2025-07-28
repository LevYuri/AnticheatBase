package me.levyuri.anticheatbase.files.commentedfiles;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CommentedConfigurationSection implements ConfigurationSection {

    protected ConfigurationSection config;

    public CommentedConfigurationSection(final ConfigurationSection configuration) {
        this.config = configuration;
    }

    /**
     * Gets a defaulted boolean value. These accept values of either "default", true, or false
     *
     * @param path The value key
     * @return null for "default", otherwise true or false
     */
    public Boolean getDefaultedBoolean(final String path) {
        if (this.isBoolean(path)) {
            return this.getBoolean(path);
        } else if (this.isString(path)) {
            final String stringValue = this.getString(path);
            if (stringValue != null && stringValue.equalsIgnoreCase("default"))
                return null;
        }

        return null;
    }

    /**
     * Gets a defaulted boolean value. These accept values of either "default", true, or false
     *
     * @param path The value key
     * @param def  The value to return if the key is not found
     * @return null for "default", otherwise true or false
     */
    public Boolean getDefaultedBoolean(final String path, final Boolean def) {
        Object value = this.get(path);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            final String stringValue = (String) value;
            if (stringValue.equalsIgnoreCase("default")) return null;
        }

        if (value == null) return def;

        return null;
    }

    @Override
    public @NotNull Set<String> getKeys(final boolean b) {
        return this.config.getKeys(b);
    }

    @Override
    public @NotNull Map<String, Object> getValues(final boolean b) {
        return this.config.getValues(b);
    }

    @Override
    public boolean contains(@NotNull final String s) {
        return this.config.contains(s);
    }

    @Override
    public boolean contains(@NotNull final String s, final boolean b) {
        return this.config.contains(s, b);
    }

    @Override
    public boolean isSet(@NotNull final String s) {
        return this.config.isSet(s);
    }

    @Override
    public String getCurrentPath() {
        return this.config.getCurrentPath();
    }

    @Override
    public @NotNull String getName() {
        return this.config.getName();
    }

    @Override
    public Configuration getRoot() {
        return this.config.getRoot();
    }

    @Override
    public ConfigurationSection getParent() {
        return this.config.getParent();
    }

    @Override
    public Object get(@NotNull final String s) {
        return this.config.get(s);
    }

    @Override
    public Object get(@NotNull final String s, final Object o) {
        return this.config.get(s, o);
    }

    @Override
    public void set(@NotNull final String s, final Object o) {
        this.config.set(s, o);
    }

    @Override
    public @NotNull CommentedConfigurationSection createSection(@NotNull final String s) {
        return new CommentedConfigurationSection(this.config.createSection(s));
    }

    @Override
    public @NotNull CommentedConfigurationSection createSection(@NotNull final String s, @NotNull final Map<?, ?> map) {
        return new CommentedConfigurationSection(this.config.createSection(s, map));
    }

    @Override
    public String getString(@NotNull final String s) {
        return this.config.getString(s);
    }

    @Override
    public String getString(@NotNull final String s, final String s1) {
        return this.config.getString(s, s1);
    }

    @Override
    public boolean isString(@NotNull final String s) {
        return this.config.isString(s);
    }

    @Override
    public int getInt(@NotNull final String s) {
        return this.config.getInt(s);
    }

    @Override
    public int getInt(@NotNull final String s, final int i) {
        return this.config.getInt(s, i);
    }

    @Override
    public boolean isInt(@NotNull final String s) {
        return this.config.isInt(s);
    }

    @Override
    public boolean getBoolean(@NotNull final String s) {
        return this.config.getBoolean(s);
    }

    @Override
    public boolean getBoolean(@NotNull final String s, final boolean b) {
        return this.config.getBoolean(s, b);
    }

    @Override
    public boolean isBoolean(@NotNull final String s) {
        return this.config.isBoolean(s);
    }

    @Override
    public double getDouble(@NotNull final String s) {
        return this.config.getDouble(s);
    }

    @Override
    public double getDouble(@NotNull final String s, final double v) {
        return this.config.getDouble(s, v);
    }

    @Override
    public boolean isDouble(@NotNull final String s) {
        return this.config.isDouble(s);
    }

    @Override
    public long getLong(@NotNull final String s) {
        return this.config.getLong(s);
    }

    @Override
    public long getLong(@NotNull final String s, final long l) {
        return this.config.getLong(s, l);
    }

    @Override
    public boolean isLong(@NotNull final String s) {
        return this.config.isLong(s);
    }

    @Override
    public List<?> getList(@NotNull final String s) {
        return this.config.getList(s);
    }

    @Override
    public List<?> getList(@NotNull final String s, final List<?> list) {
        return this.config.getList(s, list);
    }

    @Override
    public boolean isList(@NotNull final String s) {
        return this.config.isList(s);
    }

    @Override
    public @NotNull List<String> getStringList(@NotNull final String s) {
        return this.config.getStringList(s);
    }

    @Override
    public @NotNull List<Integer> getIntegerList(@NotNull final String s) {
        return this.config.getIntegerList(s);
    }

    @Override
    public @NotNull List<Boolean> getBooleanList(@NotNull final String s) {
        return this.config.getBooleanList(s);
    }

    @Override
    public @NotNull List<Double> getDoubleList(@NotNull final String s) {
        return this.config.getDoubleList(s);
    }

    @Override
    public @NotNull List<Float> getFloatList(@NotNull final String s) {
        return this.config.getFloatList(s);
    }

    @Override
    public @NotNull List<Long> getLongList(@NotNull final String s) {
        return this.config.getLongList(s);
    }

    @Override
    public @NotNull List<Byte> getByteList(@NotNull final String s) {
        return this.config.getByteList(s);
    }

    @Override
    public @NotNull List<Character> getCharacterList(@NotNull final String s) {
        return this.config.getCharacterList(s);
    }

    @Override
    public @NotNull List<Short> getShortList(@NotNull final String s) {
        return this.config.getShortList(s);
    }

    @Override
    public @NotNull List<Map<?, ?>> getMapList(@NotNull final String s) {
        return this.config.getMapList(s);
    }

    @Override
    public <T> T getObject(@NotNull final String s, @NotNull final Class<T> aClass) {
        return this.config.getObject(s, aClass);
    }

    @Override
    public <T> T getObject(@NotNull final String s, @NotNull final Class<T> aClass, final T t) {
        return this.config.getObject(s, aClass, t);
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String s, @NotNull final Class<T> aClass) {
        return this.config.getSerializable(s, aClass);
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(@NotNull final String s, @NotNull final Class<T> aClass, final T t) {
        return this.config.getSerializable(s, aClass, t);
    }

    @Override
    public Vector getVector(@NotNull final String s) {
        return this.config.getVector(s);
    }

    @Override
    public Vector getVector(@NotNull final String s, final Vector vector) {
        return this.config.getVector(s, vector);
    }

    @Override
    public boolean isVector(@NotNull final String s) {
        return this.config.isVector(s);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull final String s) {
        return this.config.getOfflinePlayer(s);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(@NotNull final String s, final OfflinePlayer offlinePlayer) {
        return this.config.getOfflinePlayer(s, offlinePlayer);
    }

    @Override
    public boolean isOfflinePlayer(@NotNull final String s) {
        return this.config.isOfflinePlayer(s);
    }

    @Override
    public ItemStack getItemStack(@NotNull final String s) {
        return this.config.getItemStack(s);
    }

    @Override
    public ItemStack getItemStack(@NotNull final String s, final ItemStack itemStack) {
        return this.config.getItemStack(s, itemStack);
    }

    @Override
    public boolean isItemStack(@NotNull final String s) {
        return this.config.isItemStack(s);
    }

    @Override
    public Color getColor(@NotNull final String s) {
        return this.config.getColor(s);
    }

    @Override
    public Color getColor(@NotNull final String s, final Color color) {
        return this.config.getColor(s, color);
    }

    @Override
    public boolean isColor(@NotNull final String s) {
        return this.config.isColor(s);
    }

    @Override
    public Location getLocation(@NotNull final String path) {
        return this.getSerializable(path, Location.class);
    }

    @Override
    public Location getLocation(@NotNull final String path, final Location def) {
        return this.getSerializable(path, Location.class, def);
    }

    @Override
    public boolean isLocation(@NotNull final String path) {
        return this.getSerializable(path, Location.class) != null;
    }

    @Override
    public CommentedConfigurationSection getConfigurationSection(@NotNull final String s) {
        final ConfigurationSection section = this.config.getConfigurationSection(s);
        if (section == null) return this.createSection(s);

        return new CommentedConfigurationSection(section);
    }

    @Override
    public boolean isConfigurationSection(@NotNull final String s) {
        return this.config.isConfigurationSection(s);
    }

    @Override
    public CommentedConfigurationSection getDefaultSection() {
        return new CommentedConfigurationSection(this.config.getDefaultSection());
    }

    @Override
    public void addDefault(@NotNull final String s, final Object o) {
        this.config.addDefault(s, o);
    }

    @Override
    public @NotNull List<String> getComments(@NotNull final String path) {
        return null;
    }

    @Override
    public @NotNull List<String> getInlineComments(@NotNull final String path) {
        return null;
    }

    @Override
    public void setComments(@NotNull final String path, final List<String> comments) {
    }

    @Override
    public void setInlineComments(@NotNull final String path, final List<String> comments) {
    }

}
