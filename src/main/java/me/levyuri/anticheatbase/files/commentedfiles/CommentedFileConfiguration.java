package me.levyuri.anticheatbase.files.commentedfiles;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class CommentedFileConfiguration extends CommentedConfigurationSection {

    private int comments;
    private final CommentedFileConfigurationHelper helper;
    private final File file;

    public CommentedFileConfiguration(final Reader configStream, final File configFile, final int comments, final JavaPlugin plugin) {
        super(YamlConfiguration.loadConfiguration(configStream));
        this.comments = comments;
        this.helper = new CommentedFileConfigurationHelper(plugin);
        this.file = configFile;
    }

    public static CommentedFileConfiguration loadConfiguration(final JavaPlugin plugin, final File file) {
        return new CommentedFileConfigurationHelper(plugin).getNewConfig(file);
    }

    public void set(final String path, final Object value, final String... comments) {
        if (!this.contains(path)) {
            final int subpathIndex = path.lastIndexOf('.');
            final String subpath = subpathIndex == -1 ? "" : path.substring(0, subpathIndex) + '.';

            for (final String comment : comments) {
                this.set(subpath + this.helper.getPluginName() + "_COMMENT_" + this.comments, " " + comment);
                this.comments++;
            }
        }

        this.set(path, value);
    }

    public void addComments(final String... comments) {
        for (final String comment : comments) {
            this.set(this.helper.getPluginName() + "_COMMENT_" + this.comments, " " + comment);
            this.comments++;
        }
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.helper.getConfigContent(this.file));
    }

    public void save() {
        this.save(false);
    }

    public void save(final boolean compactLines) {
        final String config = this.getConfigAsString();
        this.helper.saveConfig(config, this.file, compactLines);
    }

    public void save(final File file) {
        this.save(file, false);
    }

    public void save(final File file, final boolean compactLines) {
        final String config = this.getConfigAsString();
        this.helper.saveConfig(config, file, compactLines);
    }

    private String getConfigAsString() {
        if (!(this.config instanceof YamlConfiguration)) throw new UnsupportedOperationException("Cannot get config string of non-YamlConfiguration");

        final YamlConfiguration yamlConfiguration = (YamlConfiguration) this.config;

        // Edit the configuration to how we want it
        try {
            Field field_yamlOptions;
            try {
                field_yamlOptions = YamlConfiguration.class.getDeclaredField("yamlOptions");
            } catch (final NoSuchFieldException e) { // This is used for 1.18.1+
                field_yamlOptions = YamlConfiguration.class.getDeclaredField("yamlDumperOptions");
            }

            field_yamlOptions.setAccessible(true);
            final DumperOptions yamlOptions = (DumperOptions) field_yamlOptions.get(yamlConfiguration);
            yamlOptions.setWidth(Integer.MAX_VALUE);

            if (Stream.of(DumperOptions.class.getDeclaredMethods()).anyMatch(x -> x.getName().equals("setIndicatorIndent"))) yamlOptions.setIndicatorIndent(2);

            if (Stream.of(DumperOptions.class.getDeclaredMethods()).anyMatch(x -> x.getName().equals("setProcessComments"))) yamlOptions.setProcessComments(false);

            if (Stream.of(DumperOptions.class.getDeclaredMethods()).anyMatch(x -> x.getName().equals("setSplitLines"))) yamlOptions.setSplitLines(false);
        } catch (final ReflectiveOperationException e) {
            e.printStackTrace();
        }

        return yamlConfiguration.saveToString();
    }

}
