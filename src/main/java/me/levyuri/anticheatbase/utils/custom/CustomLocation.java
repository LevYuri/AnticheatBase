package me.levyuri.anticheatbase.utils.custom;

import me.levyuri.anticheatbase.utils.fastmath.FastMath;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

public class CustomLocation {

    private final World world;
    private final float yaw, pitch;
    private final long timeStamp;
    private double x, y, z;
    private int blockX, blockY, blockZ;

    public CustomLocation(final World world, final double x, final double y, final double z, final float yaw, final float pitch, final long timeStamp) {

        this.world = world;

        this.blockX = FastMath.floorInt(this.x = x);
        this.blockY = FastMath.floorInt(this.y = y);
        this.blockZ = FastMath.floorInt(this.z = z);

        this.yaw = yaw;
        this.pitch = pitch;

        this.timeStamp = timeStamp;
    }

    public CustomLocation(final World world, final double x, final double y, final double z, final float yaw, final float pitch) {

        this.world = world;

        this.blockX = FastMath.floorInt(this.x = x);
        this.blockY = FastMath.floorInt(this.y = y);
        this.blockZ = FastMath.floorInt(this.z = z);

        this.yaw = yaw;
        this.pitch = pitch;

        this.timeStamp = System.currentTimeMillis();
    }

    public CustomLocation(final World world, final double x, final double y, final double z) {

        this.world = world;

        this.blockX = FastMath.floorInt(this.x = x);
        this.blockY = FastMath.floorInt(this.y = y);
        this.blockZ = FastMath.floorInt(this.z = z);

        this.yaw = this.pitch = 0.0F;

        this.timeStamp = System.currentTimeMillis();
    }

    public CustomLocation(final Location location) {

        this.world = location.getWorld();

        this.blockX = FastMath.floorInt(this.x = location.getX());
        this.blockY = FastMath.floorInt(this.y = location.getY());
        this.blockZ = FastMath.floorInt(this.z = location.getZ());

        this.yaw = location.getYaw();
        this.pitch = location.getPitch();

        this.timeStamp = System.currentTimeMillis();
    }

    public Vector toVector() {
        return new Vector(this.x, this.y, this.z);
    }

    public Location toBukkit() {
        return new Location(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public CustomLocation clone() {
        return new CustomLocation(this.world, this.x, this.y, this.z, this.yaw, this.pitch);
    }

    public World getWorld() {
        return world;
    }

    public double getX() {
        return x;
    }

    public void setX(final double x) {
        this.blockX = FastMath.floorInt(this.x = x);
    }

    public double getY() {
        return y;
    }

    public void setY(final double y) {
        this.blockY = FastMath.floorInt(this.y = y);
    }

    public double getZ() {
        return z;
    }

    public void setZ(final double z) {
        this.blockZ = FastMath.floorInt(this.z = z);
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double distance(final CustomLocation other) {
        return FastMath.sqrt(this.distanceSquared(other));
    }

    public double distance(final Location other) {
        return FastMath.sqrt(this.distanceSquared(other));
    }

    public double distanceSquared(final CustomLocation other) {

        final double distX = this.x - other.getX();
        final double distY = this.y - other.getY();
        final double distZ = this.z - other.getZ();

        return (distX * distX) + (distY * distY) + (distZ * distZ);
    }

    public double distanceSquared(final Location other) {

        final double distX = this.x - other.getX();
        final double distY = this.y - other.getY();
        final double distZ = this.z - other.getZ();

        return (distX * distX) + (distY * distY) + (distZ * distZ);
    }

    public double length() {
        return FastMath.sqrt((this.x * this.x) + (this.y * this.y) + (this.z * this.z));
    }

    public Block getBlock() {
        return this.world.getBlockAt(this.blockX, this.blockY, this.blockZ);
    }

    public float getAngle(final Vector other) {

        final double dot = Math.min(Math.max(
                        (this.x * other.getX() + this.y * other.getY() + this.z * other.getZ())
                                / (this.length() * other.length()),
                        -1.0),
                1.0);

        return (float) FastMath.acos(dot);
    }

    public Vector getDirection() {

        final Vector vector = new Vector();

        final double x = FastMath.toRadians(this.pitch);

        vector.setY(-FastMath.sin(x));

        final double xz = FastMath.cos(x);

        final double radiansRotX = FastMath.toRadians(this.yaw);

        vector.setX(-xz * FastMath.sin(radiansRotX));
        vector.setZ(xz * FastMath.cos(radiansRotX));

        return vector;
    }

    public CustomLocation add(final CustomLocation loc) {
        this.blockX = FastMath.floorInt(this.x += loc.getX());
        this.blockY = FastMath.floorInt(this.y += loc.getY());
        this.blockZ = FastMath.floorInt(this.z += loc.getZ());
        return this;
    }

    public CustomLocation add(final Location loc) {
        this.blockX = FastMath.floorInt(this.x += loc.getX());
        this.blockY = FastMath.floorInt(this.y += loc.getY());
        this.blockZ = FastMath.floorInt(this.z += loc.getZ());
        return this;
    }

    public CustomLocation add(final Vector vec) {
        this.blockX = FastMath.floorInt(this.x += vec.getX());
        this.blockY = FastMath.floorInt(this.y += vec.getY());
        this.blockZ = FastMath.floorInt(this.z += vec.getZ());
        return this;
    }

    public CustomLocation add(final double x, final double y, final double z) {
        this.blockX = FastMath.floorInt(this.x += x);
        this.blockY = FastMath.floorInt(this.y += y);
        this.blockZ = FastMath.floorInt(this.z += z);
        return this;
    }

    public CustomLocation subtract(final CustomLocation loc) {
        this.blockX = FastMath.floorInt(this.x -= loc.getX());
        this.blockY = FastMath.floorInt(this.y -= loc.getY());
        this.blockZ = FastMath.floorInt(this.z -= loc.getZ());
        return this;
    }

    public CustomLocation subtract(final Location loc) {
        this.blockX = FastMath.floorInt(this.x -= loc.getX());
        this.blockY = FastMath.floorInt(this.y -= loc.getY());
        this.blockZ = FastMath.floorInt(this.z -= loc.getZ());
        return this;
    }

    public CustomLocation subtract(final Vector vec) {
        this.blockX = FastMath.floorInt(this.x -= vec.getX());
        this.blockY = FastMath.floorInt(this.y -= vec.getY());
        this.blockZ = FastMath.floorInt(this.z -= vec.getZ());
        return this;
    }

    public CustomLocation subtract(final double x, final double y, final double z) {
        this.blockX = FastMath.floorInt(this.x -= x);
        this.blockY = FastMath.floorInt(this.y -= y);
        this.blockZ = FastMath.floorInt(this.z -= z);
        return this;
    }
}