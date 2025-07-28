package me.levyuri.anticheatbase.utils;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public final class MiscUtils {

    public static final ItemStack EMPTY_ITEM = new ItemStack(Material.AIR);

    public static YamlConfiguration loadConfigurationUTF_8(final File file) {

        final YamlConfiguration config = new YamlConfiguration();

        try {

            final FileInputStream stream = new FileInputStream(file);

            config.load(new InputStreamReader(stream, Charsets.UTF_8));

        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        return config;
    }

    public static void initializeClasses(final String... paths) throws ClassNotFoundException {
        for (final String path : paths) Class.forName(path);
    }

    public static String capitalizeFirstLetter(final String data) {

        final char firstLetter = Character.toTitleCase(data.substring(0, 1).charAt(0));

        final String restLetters = data.substring(1).toLowerCase();

        return firstLetter + restLetters;
    }

    @SuppressWarnings("unchecked")
    public static <E> E randomElement(final Collection<? extends E> collection) {
        if (collection.isEmpty()) return null;

        final int index = new Random().nextInt(collection.size());

        if (collection instanceof List) {

            return ((List<? extends E>) collection).get(index);

        } else {

            final Iterator<? extends E> iter = collection.iterator();

            for (int i = 0; i < index; i++) iter.next();

            return iter.next();
        }
    }

    public static void consoleCommand(final Collection<String> commands) {
        if (commands == null || commands.size() == 0) return;

        final ConsoleCommandSender console = Bukkit.getConsoleSender();

        if (Bukkit.isPrimaryThread()) {

            commands.forEach(command -> Bukkit.dispatchCommand(console, command));

        } else {

            TaskUtils.task(() -> commands.forEach(command -> Bukkit.dispatchCommand(console, command)));
        }
    }
}
