package me.levyuri.anticheatbase.utils.custom;

import org.bukkit.entity.Entity;

public class NearbyEntity {

    private final Entity entity;
    private final double horizontalDistance, verticalDistance;

    public NearbyEntity(final Entity entity, final double horizontalDistance, final double verticalDistance) {
        this.entity = entity;
        this.horizontalDistance = horizontalDistance;
        this.verticalDistance = verticalDistance;
    }

    public Entity getEntity() {
        return entity;
    }

    public double getHorizontalDistance() {
        return horizontalDistance;
    }

    public double getVerticalDistance() {
        return verticalDistance;
    }
}
