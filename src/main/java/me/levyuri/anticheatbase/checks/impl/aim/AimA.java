package me.levyuri.anticheatbase.checks.impl.aim;

import me.levyuri.anticheatbase.checks.annotations.Development;
import me.levyuri.anticheatbase.checks.enums.CheckType;
import me.levyuri.anticheatbase.checks.types.Check;
import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.processors.Packet;

@Development
public class AimA extends Check {
    public AimA(Profile profile) {
        super(profile, CheckType.AIM, "A", "Invalid yaw change.");
    }

    @Override
    public void handle(Packet packet) {
        if (packet.isRotation()) {
            final float deltaYaw = profile.getRotationData().getDeltaYaw();
            final boolean invalid = deltaYaw > 0.0f && (deltaYaw % 0.25 == 0.0 || deltaYaw % 0.1 == 0.0);
            if (invalid) {
                if (increaseBuffer() > 6) {
                    fail("deltaYaw=" + deltaYaw);
                }
            } else {
                decreaseBufferBy(0.65);
            }
        }
    }
}
