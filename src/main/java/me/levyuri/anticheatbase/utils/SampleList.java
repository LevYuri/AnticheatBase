package me.levyuri.anticheatbase.utils;

import java.util.LinkedList;

public final class SampleList<T> extends LinkedList<T> {

    private final int sampleSize;
    private final boolean update;

    public SampleList(final int sampleSize) {
        this.sampleSize = sampleSize;
        this.update = false;
    }

    public SampleList(final int sampleSize, final boolean update) {
        this.sampleSize = sampleSize;
        this.update = update;
    }

    @Override
    public boolean add(final T t) {
        if (isCollected()) {
            if (this.update) {
                super.removeFirst();
            } else super.clear();
        }

        return super.add(t);
    }

    public int getMaxSize() {
        return sampleSize;
    }

    public boolean isCollected() {
        return super.size() >= this.sampleSize;
    }
}
