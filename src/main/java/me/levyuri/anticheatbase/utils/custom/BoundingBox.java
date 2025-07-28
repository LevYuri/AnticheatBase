package me.levyuri.anticheatbase.utils.custom;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import me.levyuri.anticheatbase.utils.VersionUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class BoundingBox {

    private double minX, minY, minZ, maxX, maxY, maxZ;

    public BoundingBox(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        resize(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static BoundingBox fromBukkit(final Entity entity) {

        // Not supported in legacy versions
        if (VersionUtils.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_13)) return null;

        final org.bukkit.util.BoundingBox bukkitBox = entity.getBoundingBox();

        return new BoundingBox(
                bukkitBox.getMinX(), bukkitBox.getMinY(), bukkitBox.getMinZ(),
                bukkitBox.getMaxX(), bukkitBox.getMaxY(), bukkitBox.getMaxZ()
        );
    }

    public static BoundingBox fromPlayerLocation(final CustomLocation loc) {

        final double x = loc.getX();
        final double y = loc.getY();
        final double z = loc.getZ();

        final double minX = x - 0.3D;
        final double minZ = z - 0.3D;

        final double maxX = x + 0.3D;
        final double maxY = y + 1.8D;
        final double maxZ = z + 0.3D;

        return new BoundingBox(minX, y, minZ, maxX, maxY, maxZ);
    }

    public static BoundingBox fromPlayerVector(final Vector vec) {

        final double x = vec.getX();
        final double y = vec.getY();
        final double z = vec.getZ();

        final double minX = x - 0.3D;
        final double minZ = z - 0.3D;

        final double maxX = x + 0.3D;
        final double maxY = y + 1.8D;
        final double maxZ = z + 0.3D;

        return new BoundingBox(minX, y, minZ, maxX, maxY, maxZ);
    }

    public static BoundingBox of(final Vector min, final Vector max) {
        return new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public static BoundingBox of(final Location min, final Location max) {
        return new BoundingBox(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
    }

    public static BoundingBox of(final Block min, final Block max) {
        final int x1 = min.getX();
        final int y1 = min.getY();
        final int z1 = min.getZ();
        final int x2 = max.getX();
        final int y2 = max.getY();
        final int z2 = max.getZ();

        final int minX = Math.min(x1, x2);
        final int minY = Math.min(y1, y2);
        final int minZ = Math.min(z1, z2);
        final int maxX = Math.max(x1, x2) + 1;
        final int maxY = Math.max(y1, y2) + 1;
        final int maxZ = Math.max(z1, z2) + 1;

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static BoundingBox of(final Block block) {
        return new BoundingBox(block.getX(), block.getY(), block.getZ(), block.getX() + 1, block.getY() + 1, block.getZ() + 1);
    }

    public static BoundingBox of(final Vector center, final double x, final double y, final double z) {
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }

    public static BoundingBox of(final Location center, final double x, final double y, final double z) {
        return new BoundingBox(center.getX() - x, center.getY() - y, center.getZ() - z, center.getX() + x, center.getY() + y, center.getZ() + z);
    }

    public double rayTrace(final Vector start, final Vector direction, final double maxDistance) {

        final double startX = start.getX();
        final double startY = start.getY();
        final double startZ = start.getZ();

        final Vector dir = direction.clone();

        double dirX = dir.getX();
        double dirY = dir.getY();
        double dirZ = dir.getZ();

        dir.setX(dirX == -0.0D ? 0.0D : dirX);
        dir.setY(dirY == -0.0D ? 0.0D : dirY);
        dir.setZ(dirZ == -0.0D ? 0.0D : dirZ);

        dirX = dir.getX();
        dirY = dir.getY();
        dirZ = dir.getZ();

        final double divX = 1.0D / dirX;
        final double divY = 1.0D / dirY;
        final double divZ = 1.0D / dirZ;

        double tMin;
        double tMax;

        if (dirX >= 0.0D) {

            tMin = (this.minX - startX) * divX;
            tMax = (this.maxX - startX) * divX;

        } else {

            tMin = (this.maxX - startX) * divX;
            tMax = (this.minX - startX) * divX;
        }

        final double tyMin;
        final double tyMax;

        if (dirY >= 0.0D) {

            tyMin = (this.minY - startY) * divY;
            tyMax = (this.maxY - startY) * divY;

        } else {

            tyMin = (this.maxY - startY) * divY;
            tyMax = (this.minY - startY) * divY;
        }

        if (tMin <= tyMax && tMax >= tyMin) {

            if (tyMin > tMin) {
                tMin = tyMin;
            }

            if (tyMax < tMax) {
                tMax = tyMax;
            }

            final double tzMin;
            final double tzMax;

            if (dirZ >= 0.0D) {

                tzMin = (this.minZ - startZ) * divZ;
                tzMax = (this.maxZ - startZ) * divZ;

            } else {

                tzMin = (this.maxZ - startZ) * divZ;
                tzMax = (this.minZ - startZ) * divZ;

            }

            if (tMin <= tzMax && tMax >= tzMin) {

                if (tzMin > tMin) tMin = tzMin;

                if (tzMax < tMax) tMax = tzMax;

                return tMax < 0.0D || tMin >= maxDistance ? -1D : tMin;
            }
        }

        return -1D;
    }

    public double distance(final Location location) {
        return Math.sqrt(Math.min(
                Math.pow(location.getX() - this.minX, 2),
                Math.pow(location.getX() - this.maxX, 2)) + Math.min(Math.pow(location.getZ() - this.minZ, 2),
                Math.pow(location.getZ() - this.maxZ, 2)
        ));
    }

    public double distance(final CustomLocation location) {
        return Math.sqrt(Math.min(
                Math.pow(location.getX() - this.minX, 2),
                Math.pow(location.getX() - this.maxX, 2)) + Math.min(Math.pow(location.getZ() - this.minZ, 2),
                Math.pow(location.getZ() - this.maxZ, 2)
        ));
    }

    public double distance(final double x, final double z) {
        final double dx = Math.min(Math.pow(x - minX, 2), Math.pow(x - maxX, 2));
        final double dz = Math.min(Math.pow(z - minZ, 2), Math.pow(z - maxZ, 2));

        return Math.sqrt(dx + dz);
    }

    public double distance(final BoundingBox box) {
        final double dx = Math.min(Math.pow(box.minX - minX, 2), Math.pow(box.maxX - maxX, 2));
        final double dz = Math.min(Math.pow(box.minZ - minZ, 2), Math.pow(box.maxZ - maxZ, 2));

        return Math.sqrt(dx + dz);
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public Vector getMin() {
        return new Vector(getMinX(), getMinY(), getMinZ());
    }

    public Vector getMax() {
        return new Vector(getMaxX(), getMaxY(), getMaxZ());
    }

    public BoundingBox resize(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {

        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);

        return this;
    }

    public double getWidth() {
        return getMaxX() - getMinX();
    }

    public double getDepth() {
        return getMaxZ() - getMinZ();
    }

    public double getHeight() {
        return getMaxY() - getMinY();
    }

    public double getVolume() {
        return getHeight() * getWidth() * getDepth();
    }

    public double getCenterX() {
        return getMinX() + getWidth() * 0.5D;
    }

    public double getCenterY() {
        return getMinY() + getHeight() * 0.5D;
    }

    public double getCenterZ() {
        return getMinZ() + getDepth() * 0.5D;
    }

    public Vector getCenter() {
        return new Vector(getCenterX(), getCenterY(), getCenterZ());
    }

    public BoundingBox copy(final BoundingBox other) {
        return resize(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(), other.getMaxZ());
    }

    public BoundingBox expand(final double negativeX, final double negativeY, final double negativeZ, final double positiveX, final double positiveY, final double positiveZ) {
        if (negativeX == 0.0D && negativeY == 0.0D && negativeZ == 0.0D && positiveX == 0.0D && positiveY == 0.0D && positiveZ == 0.0D) {
            return this;
        } else {
            double newMinX = getMinX() - negativeX;
            double newMinY = getMinY() - negativeY;
            double newMinZ = getMinZ() - negativeZ;
            double newMaxX = getMaxX() + positiveX;
            double newMaxY = getMaxY() + positiveY;
            double newMaxZ = getMaxZ() + positiveZ;
            double centerZ;

            if (newMinX > newMaxX) {
                centerZ = getCenterX();
                if (newMaxX >= centerZ) {
                    newMinX = newMaxX;
                } else if (newMinX <= centerZ) {
                    newMaxX = newMinX;
                } else {
                    newMinX = centerZ;
                    newMaxX = centerZ;
                }
            }

            if (newMinY > newMaxY) {
                centerZ = getCenterY();
                if (newMaxY >= centerZ) {
                    newMinY = newMaxY;
                } else if (newMinY <= centerZ) {
                    newMaxY = newMinY;
                } else {
                    newMinY = centerZ;
                    newMaxY = centerZ;
                }
            }

            if (newMinZ > newMaxZ) {
                centerZ = getCenterZ();
                if (newMaxZ >= centerZ) {
                    newMinZ = newMaxZ;
                } else if (newMinZ <= centerZ) {
                    newMaxZ = newMinZ;
                } else {
                    newMinZ = centerZ;
                    newMaxZ = centerZ;
                }
            }

            return resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
        }
    }

    public BoundingBox expand(final double x, final double y, final double z) {
        return expand(x, y, z, x, y, z);
    }

    public BoundingBox expand(final Vector expansion) {
        return expand(expansion.getX(), expansion.getY(), expansion.getZ());
    }

    public BoundingBox expand(final double expansion) {
        return expand(expansion, expansion, expansion, expansion, expansion, expansion);
    }

    public BoundingBox expand(final double dirX, final double dirY, final double dirZ, final double expansion) {
        if (expansion == 0.0D) {
            return this;
        } else if (dirX == 0.0D && dirY == 0.0D && dirZ == 0.0D) {
            return this;
        } else {
            final double negativeX = dirX < 0.0D ? -dirX * expansion : 0.0D;
            final double negativeY = dirY < 0.0D ? -dirY * expansion : 0.0D;
            final double negativeZ = dirZ < 0.0D ? -dirZ * expansion : 0.0D;
            final double positiveX = dirX > 0.0D ? dirX * expansion : 0.0D;
            final double positiveY = dirY > 0.0D ? dirY * expansion : 0.0D;
            final double positiveZ = dirZ > 0.0D ? dirZ * expansion : 0.0D;

            return expand(negativeX, negativeY, negativeZ, positiveX, positiveY, positiveZ);
        }
    }

    public BoundingBox expand(final Vector direction, final double expansion) {
        return expand(direction.getX(), direction.getY(), direction.getZ(), expansion);
    }

    public BoundingBox expand(final BlockFace blockFace, final double expansion) {
        return blockFace == BlockFace.SELF ? this : expand(blockFace.getDirection(), expansion);
    }

    public BoundingBox expandDirectional(final double dirX, final double dirY, final double dirZ) {
        return expand(dirX, dirY, dirZ, 1.0D);
    }

    public BoundingBox expandDirectional(final Vector direction) {
        return expand(direction.getX(), direction.getY(), direction.getZ(), 1.0D);
    }

    public BoundingBox union(final double posX, final double posY, final double posZ) {
        final double newMinX = Math.min(getMinX(), posX);
        final double newMinY = Math.min(getMinY(), posY);
        final double newMinZ = Math.min(getMinZ(), posZ);
        final double newMaxX = Math.max(getMaxX(), posX);
        final double newMaxY = Math.max(getMaxY(), posY);
        final double newMaxZ = Math.max(getMaxZ(), posZ);

        return newMinX == getMinX() && newMinY == getMinY() && newMinZ == getMinZ() && newMaxX == getMaxX() && newMaxY == getMaxY() && newMaxZ == getMaxZ()
                ? this
                : resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox union(final Vector position) {
        return union(position.getX(), position.getY(), position.getZ());
    }

    public BoundingBox union(final Location position) {
        return union(position.getX(), position.getY(), position.getZ());
    }

    public BoundingBox union(final BoundingBox other) {
        if (contains(other)) {
            return this;
        } else {
            final double newMinX = Math.min(getMinX(), other.getMinX());
            final double newMinY = Math.min(getMinY(), other.getMinY());
            final double newMinZ = Math.min(getMinZ(), other.getMinZ());
            final double newMaxX = Math.max(getMaxX(), other.getMaxX());
            final double newMaxY = Math.max(getMaxY(), other.getMaxY());
            final double newMaxZ = Math.max(getMaxZ(), other.getMaxZ());

            return resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
        }
    }

    public BoundingBox intersection(final BoundingBox other) {

        final double newMinX = Math.max(getMinX(), other.getMinX());
        final double newMinY = Math.max(getMinY(), other.getMinY());
        final double newMinZ = Math.max(getMinZ(), other.getMinZ());
        final double newMaxX = Math.min(getMaxX(), other.getMaxX());
        final double newMaxY = Math.min(getMaxY(), other.getMaxY());
        final double newMaxZ = Math.min(getMaxZ(), other.getMaxZ());

        return resize(newMinX, newMinY, newMinZ, newMaxX, newMaxY, newMaxZ);
    }

    public BoundingBox shift(final double shiftX, final double shiftY, final double shiftZ) {
        return shiftX == 0.0D && shiftY == 0.0D && shiftZ == 0.0D ? this
                : resize(getMinX() + shiftX, getMinY() + shiftY, getMinZ() + shiftZ, getMaxX() + shiftX, getMaxY() + shiftY, getMaxZ() + shiftZ);
    }

    public BoundingBox shift(final Vector shift) {
        return shift(shift.getX(), shift.getY(), shift.getZ());
    }

    public BoundingBox shift(final Location shift) {
        return shift(shift.getX(), shift.getY(), shift.getZ());
    }

    private boolean intersects(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        return getMinX() < maxX && getMaxX() > minX && getMinY() < maxY && getMaxY() > minY && getMinZ() < maxZ && getMaxZ() > minZ;
    }

    public boolean intersects(final BoundingBox other) {
        return intersects(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(), other.getMaxZ());
    }

    public boolean intersects(final Vector min, final Vector max) {

        final double x1 = min.getX();
        final double y1 = min.getY();
        final double z1 = min.getZ();
        final double x2 = max.getX();
        final double y2 = max.getY();
        final double z2 = max.getZ();

        return intersects(x1, y1, z1, x2, y2, z2);
    }

    public boolean contains(final double x, final double y, final double z) {
        return x >= getMinX() && x < getMaxX() && y >= getMinY() && y < getMaxY() && z >= getMinZ() && z < getMaxZ();
    }

    public boolean contains(final Vector position) {
        return contains(position.getX(), position.getY(), position.getZ());
    }

    private boolean contains(final double minX, final double minY, final double minZ, final double maxX, final double maxY, final double maxZ) {
        return getMinX() <= minX && getMaxX() >= maxX && getMinY() <= minY && getMaxY() >= maxY && getMinZ() <= minZ && getMaxZ() >= maxZ;
    }

    public boolean contains(final BoundingBox other) {
        return contains(other.getMinX(), other.getMinY(), other.getMinZ(), other.getMaxX(), other.getMaxY(), other.getMaxZ());
    }

    public boolean contains(final Vector min, final Vector max) {

        final double x1 = min.getX();
        final double y1 = min.getY();
        final double z1 = min.getZ();
        final double x2 = max.getX();
        final double y2 = max.getY();
        final double z2 = max.getZ();

        return contains(x1, y1, z1, x2, y2, z2);
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + Double.hashCode(getMaxX());
        result = 31 * result + Double.hashCode(getMaxY());
        result = 31 * result + Double.hashCode(getMaxZ());
        result = 31 * result + Double.hashCode(getMinX());
        result = 31 * result + Double.hashCode(getMinY());
        result = 31 * result + Double.hashCode(getMinZ());
        return result;
    }

    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof BoundingBox)) {
            return false;
        } else {
            final BoundingBox other = (BoundingBox) obj;
            if (Double.doubleToLongBits(getMaxX()) != Double.doubleToLongBits(other.getMaxX())) {
                return false;
            } else if (Double.doubleToLongBits(getMaxY()) != Double.doubleToLongBits(other.getMaxY())) {
                return false;
            } else if (Double.doubleToLongBits(getMaxZ()) != Double.doubleToLongBits(other.getMaxZ())) {
                return false;
            } else if (Double.doubleToLongBits(getMinX()) != Double.doubleToLongBits(other.getMinX())) {
                return false;
            } else if (Double.doubleToLongBits(getMinY()) != Double.doubleToLongBits(other.getMinY())) {
                return false;
            } else {
                return Double.doubleToLongBits(getMinZ()) == Double.doubleToLongBits(other.getMinZ());
            }
        }
    }

    public String toString() {
        return "BoundingBox [minX=" +
                getMinX() +
                ", minY=" +
                getMinY() +
                ", minZ=" +
                getMinZ() +
                ", maxX=" +
                getMaxX() +
                ", maxY=" +
                getMaxY() +
                ", maxZ=" +
                getMaxZ() +
                "]";
    }

    public BoundingBox clone() {
        try {
            return (BoundingBox) super.clone();
        } catch (CloneNotSupportedException var2) {
            throw new Error(var2);
        }
    }
}
