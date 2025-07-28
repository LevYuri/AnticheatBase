package me.levyuri.anticheatbase.utils;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.playerdata.data.impl.ActionData;
import me.levyuri.anticheatbase.utils.custom.CustomLocation;
import org.bukkit.inventory.ItemStack;

import java.util.function.Predicate;

public final class PlayerUtils {

    private static final Predicate<ItemStack> blockable = item -> {
        switch (item.getType().toString()) {
            case "WOOD_SWORD":
            case "WOODEN_SWORD":
            case "STONE_SWORD":
            case "IRON_SWORD":
            case "GOLD_SWORD":
            case "GOLDEN_SWORD":
            case "DIAMOND_SWORD":
            case "NETHERITE_SWORD":
            case "SHIELD":
                return true;
            default:
                return false;
        }
    };

    private PlayerUtils() {
    }

    public static int getMaxVelocityTicks(final double velocityXZ, final double velocityY) {

        int ticks = 0;

        float horizontal = (float) Math.abs(velocityXZ);

        do {

            horizontal -= .02F; //SpeedInAir Value

            horizontal *= MoveUtils.FRICTION; //Horizontal Friction

            if (ticks++ > 30) break;

        } while (horizontal > 0F);

        float vertical = (float) Math.abs(velocityY);

        do {

            vertical -= .08F; //Falling acceleration

            vertical *= (float) MoveUtils.MOTION_Y_FRICTION; //Vertical Friction

            if (ticks++ > 60) break;

        } while (vertical > 0F);

        return ticks;
    }

    public static double getEyeHeight(final Profile profile) {

        final ActionData actionData = profile.getActionData();

        // When sleeping
        if (actionData.getLastSleepingTicks() == 0) return .2F;

        float height = 1.62F;

        if (actionData.isSneaking()) {

            // Eye height diff in 1.14+ clients
            height -= profile.getVersion().isNewerThanOrEquals(ClientVersion.V_1_14) ? .35000002384F : .08F;
        }

        return height;
    }

    public static CustomLocation getEyeLocation(final Profile profile) {

        final CustomLocation location = profile.getMovementData().getLocation();

        return location.clone().add(0D, getEyeHeight(profile), 0D);
    }

    public static boolean isHoldingSwordOrShield(final Profile profile) {

        final ActionData actionData = profile.getActionData();

        return blockable.test(actionData.getItemInMainHand()) || blockable.test(actionData.getItemInOffHand());
    }
}