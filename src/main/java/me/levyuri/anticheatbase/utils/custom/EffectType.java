package me.levyuri.anticheatbase.utils.custom;

public enum EffectType {
    UNKNOWN,
    SPEED,
    SLOWNESS,
    HASTE,
    MINING_FATIGUE,
    STRENGTH,
    INSTANT_HEALTH,
    INSTANT_DAMAGE,
    JUMP_BOOST,
    NAUSEA,
    REGENERATION,
    RESISTANCE,
    FIRE_RESISTANCE,
    WATER_BREATHING,
    INVISIBILITY,
    BLINDNESS,
    NIGHT_VISION,
    HUNGER,
    WEAKNESS,
    POISON,
    WITHER,
    HEALTH_BOOST,
    ABSORPTION,
    SATURATION,
    GLOWING,
    LEVITATION,
    LUCK,
    BAD_LUCK,
    SLOW_FALLING,
    CONDUIT_POWER,
    DOLPHINS_GRACE,
    BAD_OMEN,
    HERO_OF_THE_VILLAGE;

    public static EffectType fromID(final int id) {
        return id >= EffectType.values().length ? UNKNOWN : EffectType.values()[id];
    }
}
