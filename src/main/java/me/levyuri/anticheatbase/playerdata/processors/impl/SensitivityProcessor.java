package me.levyuri.anticheatbase.playerdata.processors.impl;

import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.playerdata.data.impl.RotationData;
import me.levyuri.anticheatbase.playerdata.processors.Processor;
import me.levyuri.anticheatbase.utils.MathUtils;

public class SensitivityProcessor implements Processor {

    private final Profile profile;

    private double mouseX, mouseY, constantYaw, constantPitch, yawGcd, pitchGcd;

    public SensitivityProcessor(final Profile profile) {
        this.profile = profile;
    }

    @Override
    public void process() {

        final RotationData data = profile.getRotationData();

        final float deltaYaw = data.getDeltaYaw();
        final float deltaPitch = data.getDeltaPitch();

        final float lastDeltaYaw = data.getLastDeltaYaw();
        final float lastDeltaPitch = data.getLastDeltaPitch();

        this.yawGcd = MathUtils.getAbsoluteGcd(deltaYaw, lastDeltaYaw);
        this.pitchGcd = MathUtils.getAbsoluteGcd(deltaPitch, lastDeltaPitch);

        this.constantYaw = this.yawGcd / MathUtils.EXPANDER;
        this.constantPitch = this.pitchGcd / MathUtils.EXPANDER;

        this.mouseX = (int) (deltaYaw / this.constantYaw);
        this.mouseY = (int) (deltaPitch / this.constantPitch);

        handleSensitivity();
    }

    private void handleSensitivity() {
        //TODO: Handle sensitivity
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public double getConstantYaw() {
        return constantYaw;
    }

    public double getConstantPitch() {
        return constantPitch;
    }

    public double getYawGcd() {
        return yawGcd;
    }

    public double getPitchGcd() {
        return pitchGcd;
    }
}
