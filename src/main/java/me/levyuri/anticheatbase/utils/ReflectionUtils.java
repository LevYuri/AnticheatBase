package me.levyuri.anticheatbase.utils;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.levyuri.anticheatbase.utils.custom.BoundingBox;
import me.levyuri.anticheatbase.utils.custom.exception.AnticheatException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class ReflectionUtils {

    private static final Map<String, Class<?>> NMS_CLASSES = new HashMap<>();
    private static final Map<String, Class<?>> OBC_CLASSES = new HashMap<>();
    private static final Map<Class<?>, Map<String, Method>> METHODS = new HashMap<>();
    private static final Map<Class<?>, Map<String, Field>> FIELDS = new HashMap<>();
    private static String VERSION;

    public ReflectionUtils() {
    }

    public static void clear() {
        VERSION = null;
        NMS_CLASSES.clear();
        OBC_CLASSES.clear();
        METHODS.clear();
        FIELDS.clear();
    }

    public static String getVersion() {
        if (VERSION == null) {

            final String name = Bukkit.getServer().getClass().getPackage().getName();

            VERSION = name.substring(name.lastIndexOf('.') + 1) + ".";
        }

        return VERSION;
    }

    public static Class<?> getNMSClass(final String nmsClassName) {
        if (NMS_CLASSES.containsKey(nmsClassName)) {

            return NMS_CLASSES.get(nmsClassName);
        }

        final String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;

        final Class<?> clazz;

        try {

            clazz = Class.forName(clazzName);

        } catch (final Throwable t) {

            t.printStackTrace();

            return NMS_CLASSES.put(nmsClassName, null);
        }

        NMS_CLASSES.put(nmsClassName, clazz);
        return clazz;
    }

    public static Class<?> getOBCClass(final String obcClassName) {
        if (OBC_CLASSES.containsKey(obcClassName)) {

            return OBC_CLASSES.get(obcClassName);
        }

        final String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;

        final Class<?> clazz;

        try {

            clazz = Class.forName(clazzName);

        } catch (final Throwable t) {

            t.printStackTrace();

            OBC_CLASSES.put(obcClassName, null);

            return null;
        }

        OBC_CLASSES.put(obcClassName, clazz);

        return clazz;
    }

    public static Object getConnection(final Player player) {

        final Method getHandleMethod = getMethod(player.getClass(), "getHandle");

        if (getHandleMethod != null) {
            try {
                final Object nmsPlayer = getHandleMethod.invoke(player);

                final Field playerConField = getField(nmsPlayer.getClass(), "playerConnection");

                return playerConField.get(nmsPlayer);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void sendPacket(final Player player, final Object packet) {

        final Object playerConnection = getConnection(player);

        final Method sendPacketMethod = getMethod(playerConnection.getClass(), "sendPacket",
                getNMSClass("Packet"));

        if (sendPacketMethod != null) {
            try {
                sendPacketMethod.invoke(playerConnection, packet);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public static Object craftEntity(final Entity entity) {
        try {
            return getMethod(getOBCClass("entity.CraftEntity"), "getHandle").invoke(entity);
        } catch (final IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static BoundingBox getBoundingBox(final Entity entity) {

        if (VersionUtils.getServerVersion().isOlderThan(ServerVersion.V_1_13_1)) {
            try {
                final Object nmsBoundingBox = getMethod(getNMSClass("Entity"), "getBoundingBox")
                        .invoke(craftEntity(entity));

                return toBoundingBox(nmsBoundingBox);
            } catch (final IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } else {
            return BoundingBox.fromBukkit(entity);
        }

        return null;
    }

    public static BoundingBox toBoundingBox(final Object aaBB) {

        final Vector min = getBoxMin(aaBB);
        final Vector max = getBoxMax(aaBB);

        return BoundingBox.of(min, max);
    }

    private static Vector getBoxMin(final Object box) {

        double x = 0D;
        double y = 0D;
        double z = 0D;

        final Class<?> boxClass = box.getClass();

        try {
            if (VersionUtils.getServerVersion().isOlderThan(ServerVersion.V_1_13_1)) {
                x = (double) getField(boxClass, "a").get(box);
                y = (double) getField(boxClass, "b").get(box);
                z = (double) getField(boxClass, "c").get(box);
            } else {
                x = (double) getField(boxClass, "minX").get(box);
                y = (double) getField(boxClass, "minY").get(box);
                z = (double) getField(boxClass, "minZ").get(box);
            }
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }

        return new Vector(x, y, z);
    }

    private static Vector getBoxMax(final Object box) {

        double x = 0D;
        double y = 0D;
        double z = 0D;

        final Class<?> boxClass = box.getClass();

        try {

            if (VersionUtils.getServerVersion().isOlderThan(ServerVersion.V_1_13_1)) {
                x = (double) getField(boxClass, "d").get(box);
                y = (double) getField(boxClass, "e").get(box);
                z = (double) getField(boxClass, "f").get(box);
            } else {
                x = (double) getField(boxClass, "maxX").get(box);
                y = (double) getField(boxClass, "maxY").get(box);
                z = (double) getField(boxClass, "maxZ").get(box);
            }
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        }

        return new Vector(x, y, z);
    }

    public static Constructor<?> getConstructor(final Class<?> clazz, final Class<?>... params) {
        try {
            return clazz.getConstructor(params);
        } catch (final NoSuchMethodException e) {
            return null;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... params) {
        if (!METHODS.containsKey(clazz)) {
            METHODS.put(clazz, new HashMap<>());
        }

        final Map<String, Method> methods = METHODS.get(clazz);

        if (methods.containsKey(methodName)) {
            return methods.get(methodName);
        }

        try {
            final Method method = clazz.getMethod(methodName, params);

            methods.put(methodName, method);

            METHODS.put(clazz, methods);

            return method;
        } catch (final Exception e) {

            e.printStackTrace();

            methods.put(methodName, null);

            METHODS.put(clazz, methods);

            throw new AnticheatException("Couldn't find method at class " + clazz.getSimpleName());
        }
    }

    public static Field getField(final Class<?> clazz, final String fieldName) {
        if (!FIELDS.containsKey(clazz)) {
            FIELDS.put(clazz, new HashMap<>());
        }

        final Map<String, Field> fields = FIELDS.get(clazz);

        if (fields.containsKey(fieldName)) {
            return fields.get(fieldName);
        }

        try {
            final Field field = clazz.getField(fieldName);
            fields.put(fieldName, field);
            FIELDS.put(clazz, fields);
            return field;
        } catch (final Exception e) {
            e.printStackTrace();
            fields.put(fieldName, null);
            FIELDS.put(clazz, fields);
        }

        throw new AnticheatException("Couldn't find field at class " + clazz.getSimpleName());
    }

    public static Method findMethod(final Class<?> clazz, final int requires, final int excludes, final Class<?> result,
                                    final Class<?>... params) {
        final int paramsCount = params.length;

        for (final Method m : clazz.getMethods()) {

            if (m.getParameterCount() == paramsCount) {

                if (m.getReturnType() == result) {

                    int modifier = m.getModifiers();

                    if ((modifier & requires) == requires && (modifier & excludes) == 0) {

                        if (Arrays.equals(m.getParameterTypes(), params)) {
                            return m;
                        }
                    }
                }
            }
        }
        throw new AnticheatException("Couldn't find method at class " + clazz.getSimpleName());
    }
}
