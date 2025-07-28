package me.levyuri.anticheatbase.utils.custom;

public class Teleport {

    private final double x, y, z;
    private final long timeStamp;

    public Teleport(final double x, final double y, final double z, final long timeStamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.timeStamp = timeStamp;
    }

    public boolean matches(final double x, final double y, final double z) {
        // Check if it's less than .03125D due to precision loss in rare cases
        return Math.abs(this.x - x) < .03125D && Math.abs(this.y - y) < .03125D && Math.abs(this.z - z) < .03125D;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
