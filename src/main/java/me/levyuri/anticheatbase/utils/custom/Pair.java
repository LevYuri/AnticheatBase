package me.levyuri.anticheatbase.utils.custom;

public class Pair<T, Y> {

    private T key;
    private Y value;

    public Pair(final T key, final Y value) {
        this.key = key;
        this.value = value;
    }

    public Pair(final Pair<T, Y> pair) {
        this.key = pair.key;
        this.value = pair.value;
    }

    public T getKey() {
        return key;
    }

    public void setKey(final T key) {
        this.key = key;
    }

    public Y getValue() {
        return value;
    }

    public void setValue(final Y value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
