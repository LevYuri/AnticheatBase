package me.levyuri.anticheatbase.managers.themes;

import me.levyuri.anticheatbase.Anticheat;
import me.levyuri.anticheatbase.files.Config;
import me.levyuri.anticheatbase.managers.Initializer;
import me.levyuri.anticheatbase.managers.themes.impl.DefaultTheme;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThemeManager implements Initializer {

    private final Anticheat plugin;

    private final List<Theme> themes = new ArrayList<>();

    private Theme theme;

    public ThemeManager(final Anticheat plugin) {
        this.plugin = plugin;
    }

    @Override
    public void initialize() {

        final File folder = new File(this.plugin.getDataFolder(), "themes");

        if (!folder.exists()) folder.mkdirs();

        Arrays.asList(
                new DefaultTheme(this.plugin, "default")
        ).forEach(BaseTheme::create);

        final File[] files = folder.listFiles();

        if (files == null) return;

        Arrays.stream(files).filter(File::isFile).forEach(file -> this.themes.add(new Theme(file)));

        this.themes.forEach(Theme::reload);

        setThemeFromName(Config.Setting.THEME.getString());

        final Theme defaultTheme = getThemeByName("default");

        if (this.theme == null) this.theme = defaultTheme;

        if (this.theme != defaultTheme) {

            final AtomicBoolean changed = new AtomicBoolean(false);

            defaultTheme.getConfig().getKeys(false)
                    .stream()
                    .filter(key -> !this.theme.getConfig().getKeys(false).contains(key))
                    .forEach(key -> {

                        this.theme.getConfig().addDefault(key, defaultTheme.getConfig().get(key));

                        if (!changed.get()) changed.set(true);
                    });

            if (changed.get()) {

                try {

                    this.theme.getConfig().save(this.theme.getFile());

                    this.theme.reload();

                } catch (final IOException e) {

                    e.printStackTrace();
                }
            }
        }
    }

    public void reload() {
        shutdown();
        initialize();
    }

    public void setThemeFromName(final String name) {
        this.theme = getThemeByName(name);
    }

    public Theme getThemeByName(final String name) {

        for (final Theme theme : this.themes) {

            if (theme.getThemeName().equalsIgnoreCase(name)) {

                return theme;
            }
        }

        return null;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public Theme getTheme() {
        return theme;
    }

    @Override
    public void shutdown() {
        this.themes.clear();
    }
}
