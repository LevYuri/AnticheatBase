package me.levyuri.anticheatbase.utils.custom.aim;

import me.levyuri.anticheatbase.utils.fastmath.FastMath;

import java.util.HashSet;
import java.util.Set;

/**
 * A simple heuristics tool that can prove useful when coding AimAssist related checks.
 * Example usage:
 * <p>
 * private final RotationHeuristics heuristics = new RotationHeuristics(100, 1.25F, 7.5F);
 *
 * @Override
 * public void handle(Packet packet) {
 * if (!packet.isRotation() || profile.isExempt().aim(15, 0)) return;
 * <p>
 * RotationData data = profile.getRotationData();
 * <p>
 * this.heuristics.process(data.getDeltaYaw());
 * <p>
 * if (!this.heuristics.isFinished()) return;
 * <p>
 * final RotationHeuristics.HeuristicsResult result = this.heuristics.getResult();
 * <p>
 * final float average = result.getAverage();
 * final float min = result.getMin();
 * final float max = result.getMax();
 * final int lowCount = result.getLowCount();
 * final int highCount = result.getHighCount();
 * final int duplicates = result.getDuplicates();
 * final int roundedCount = result.getRoundedCount();
 * }
 */
public class RotationHeuristics {

    private final int maxSize;
    private final float lowThreshold, highThreshold;
    private final Set<Float> distinctRotations = new HashSet<>();
    private int size;
    private float average, min, max;
    private int highCount, lowCount, roundedCount;

    public RotationHeuristics(final int maxSize, final float lowThreshold, final float highThreshold) {
        this.size = this.maxSize = maxSize;
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        reset();
    }

    public void process(final float rotation) {

        if (isFinished()) return;

        this.average = (this.size - 1) == 0 ? (this.average + rotation) / this.maxSize : this.average + rotation;

        // Anti duplicate
        this.distinctRotations.add(rotation);

        this.highCount = rotation > this.highThreshold ? this.highCount + 1 : this.highCount;

        this.lowCount = rotation < this.lowThreshold ? this.lowCount + 1 : this.lowCount;

        this.min = Math.min(rotation, this.min);

        this.max = Math.max(rotation, this.max);

        this.roundedCount = rotation > 1F && rotation % 1.5 != 0F && (FastMath.round(rotation) == 0F || rotation % 1 == 0F) ? this.roundedCount + 1 : this.roundedCount;

        this.size--;
    }

    public void reset() {
        this.size = this.maxSize;
        this.average = this.highCount = this.lowCount = this.roundedCount = 0;
        this.min = Float.MAX_VALUE;
        this.max = Float.MIN_VALUE;
        this.distinctRotations.clear();
    }

    private boolean isFinished() {
        return this.size == 0;
    }

    public HeuristicsResult getResult() {

        if (isFinished()) {

            return new HeuristicsResult(
                    this.average, this.min, this.max,
                    (this.maxSize - this.distinctRotations.size()),
                    this.highCount, this.lowCount, this.roundedCount
            );
        }

        return null;
    }

    public static class HeuristicsResult {

        private final float average, min, max;
        private final int duplicates, highCount, lowCount, roundedCount;

        public HeuristicsResult(final float average, final float min, final float max, final int duplicates, final int highCount, final int lowCount, final int roundedCount) {
            this.average = average;
            this.min = min;
            this.max = max;
            this.duplicates = duplicates;
            this.highCount = highCount;
            this.lowCount = lowCount;
            this.roundedCount = roundedCount;
        }

        public float getAverage() {
            return average;
        }

        public float getMin() {
            return min;
        }

        public float getMax() {
            return max;
        }

        public int getDuplicates() {
            return duplicates;
        }

        public int getHighCount() {
            return highCount;
        }

        public int getLowCount() {
            return lowCount;
        }

        public int getRoundedCount() {
            return roundedCount;
        }
    }
}
