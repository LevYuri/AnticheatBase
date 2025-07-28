package me.levyuri.anticheatbase.nms;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface NMSInstance {

    float getAttackCooldown(final Player player);

    boolean isChunkLoaded(final World world, final int x, final int z);

    Material getType(final Block block);

    Entity[] getChunkEntities(final World world, final int x, final int z);

    boolean isWaterLogged(final Block block);

    boolean isDead(final Player player);

    boolean isSleeping(final Player player);

    boolean isGliding(final Player player);

    boolean isInsideVehicle(final Player player);

    boolean isRiptiding(final Player player);

    boolean isBlocking(final Player player);

    boolean isSneaking(final Player player);

    ItemStack getItemInMainHand(final Player player);

    ItemStack getItemInOffHand(final Player player);

    float getWalkSpeed(final Player player);

    float getAttributeSpeed(final Player player);

    boolean getAllowFlight(final Player player);

    boolean isFlying(final Player player);

    float getFallDistance(final Player player);
}
