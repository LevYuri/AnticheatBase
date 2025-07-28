package me.levyuri.anticheatbase.nms;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.levyuri.anticheatbase.utils.VersionUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class InstanceDefault implements NMSInstance {

    @Override
    public float getAttackCooldown(final Player player) {
        return VersionUtils.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_16) ? player.getAttackCooldown() : 1F;
    }

    @Override
    public boolean isChunkLoaded(final World world, final int x, final int z) {
        return world.isChunkLoaded(x >> 4, z >> 4);
    }

    @Override
    public Material getType(final Block block) {
        return block.getType();
    }

    @Override
    public Entity[] getChunkEntities(final World world, final int x, final int z) {
        return world.isChunkLoaded(x >> 4, z >> 4) ? world.getChunkAt(x >> 4, z >> 4).getEntities() : new Entity[0];
    }

    @Override
    public boolean isWaterLogged(final Block block) {
        return VersionUtils.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13_1)
                && (block.getBlockData() instanceof org.bukkit.block.data.Waterlogged
                && ((org.bukkit.block.data.Waterlogged) block).isWaterlogged());
    }

    @Override
    public boolean isDead(final Player player) {
        return player.isDead();
    }

    @Override
    public boolean isSleeping(final Player player) {
        return player.isSleeping();
    }

    @Override
    public boolean isGliding(final Player player) {
        return VersionUtils.getServerVersion().isNewerThan(ServerVersion.V_1_8_3) && player.isGliding();
    }

    @Override
    public boolean isInsideVehicle(final Player player) {
        return player.isInsideVehicle();
    }

    @Override
    public boolean isRiptiding(final Player player) {
        return VersionUtils.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_13_1) && player.isRiptiding();
    }

    @Override
    public boolean isBlocking(final Player player) {
        return player.isBlocking();
    }

    @Override
    public boolean isSneaking(final Player player) {
        return player.isSneaking();
    }

    @Override
    public ItemStack getItemInMainHand(final Player player) {
        return player.getItemInUse();
    }

    @Override
    public ItemStack getItemInOffHand(final Player player) {
        return VersionUtils.getServerVersion().isNewerThan(ServerVersion.V_1_8_3) ? player.getInventory().getItemInOffHand() : null;
    }

    @Override
    public float getWalkSpeed(final Player player) {
        return player.getWalkSpeed();
    }

    @Override
    public float getAttributeSpeed(final Player player) {
        return VersionUtils.getServerVersion().isNewerThan(ServerVersion.V_1_8_3) ? (float) Objects.requireNonNull(player.getAttribute(Attribute.MOVEMENT_SPEED)).getValue() : 0F;
    }

    @Override
    public boolean getAllowFlight(final Player player) {
        return player.getAllowFlight();
    }

    @Override
    public boolean isFlying(final Player player) {
        return player.isFlying();
    }

    @Override
    public float getFallDistance(final Player player) {
        return player.getFallDistance();
    }
}