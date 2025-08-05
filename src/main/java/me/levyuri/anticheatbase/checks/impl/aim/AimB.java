package me.levyuri.anticheatbase.checks.impl.aim;

import me.levyuri.anticheatbase.checks.enums.CheckType;
import me.levyuri.anticheatbase.checks.types.Check;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.processors.Packet;

public class AimB extends Check {
    public AimB(Profile profile) {
        super(profile, CheckType.AIM, "B", "Repeated yaw values.");
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation()) {
            final float deltaYaw = profile.getRotationData().getDeltaYaw();
            final float lastDeltaYaw = profile.getRotationData().getLastDeltaYaw();
            final boolean invalid = deltaYaw > 1.25F && lastDeltaYaw > 1.25F && deltaYaw == lastDeltaYaw;
            if (invalid) {
                if (increaseBuffer() > 5) {
                    fail("deltaYaw=" + deltaYaw);
                }
            } else {
                decreaseBufferBy(0.75);
            }
        }
    }
}
