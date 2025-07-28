package me.levyuri.anticheatbase.playerdata.processors.impl;

import me.levyuri.anticheatbase.managers.profile.Profile;
import me.levyuri.anticheatbase.playerdata.data.impl.MovementData;
import me.levyuri.anticheatbase.playerdata.processors.Processor;

public final class PredictionProcessor implements Processor {

    private final Profile profile;

    private double predictedDeltaY;

    public PredictionProcessor(final Profile profile) {
        this.profile = profile;
    }

    @Override
    public void process() {

        final MovementData movementData = profile.getMovementData();

        this.predictedDeltaY = (movementData.getLastDeltaY() - 0.08) * 0.9800000190734863;
    }

    public double getPredictedDeltaY() {
        return predictedDeltaY;
    }
}
