package me.levyuri.anticheatbase.utils.custom;

import java.util.HashMap;
import java.util.Map;

public class ExpiringMap<K, V> extends HashMap<K, V> {

    private final long expireMillis;

    private final Map<Long, Pair<K, V>> map = new HashMap<>();

    public ExpiringMap(final long expireMillis) {
        this.expireMillis = expireMillis;
    }

    @Override
    public V get(final Object key) {

        validate();

        for (final Pair<K, V> pair : this.map.values()) {

            if (pair.getKey() == key) return pair.getValue();
        }

        return null;
    }

    public V removeOrDefault(final Object key, final V defaultValue) {

        validate();

        Pair<K, V> pair = null;

        for (final Pair<K, V> kvPair : this.map.values()) {

            if (kvPair.getKey() != key) continue;

            pair = kvPair;
        }

        if (pair != null) {

            this.map.values().remove(pair);

            return pair.getValue();
        }

        return defaultValue;
    }

    @Override
    public V getOrDefault(final Object key, final V defaultValue) {

        validate();

        V v = null;

        for (final Pair<K, V> pair : this.map.values()) {

            if (pair.getKey() != key) continue;

            v = pair.getValue();
        }

        return v != null ? v : defaultValue;
    }

    @Override
    public V put(final K key, final V value) {

        validate();

        this.map.put(System.currentTimeMillis(), new Pair<>(key, value));

        return value;
    }

    @Override
    public V putIfAbsent(final K key, final V value) {

        validate();

        V v = null;

        for (final Pair<K, V> pair : this.map.values()) {

            if (pair.getKey() != key) continue;

            v = pair.getValue();
        }

        if (v == null) v = this.put(key, value);

        return v;
    }

    @Override
    public int size() {

        validate();

        return this.map.size();
    }

    @Override
    public boolean containsKey(final Object key) {

        validate();

        for (final Pair<K, V> pair : this.map.values()) {

            if (pair.getKey() == key) return true;
        }

        return false;
    }

    @Override
    public boolean containsValue(final Object value) {

        validate();

        for (final Pair<K, V> pair : this.map.values()) {

            if (pair.getValue() == value) return true;
        }

        return false;
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    private void validate() {
        this.map.keySet().removeIf(timeStamp -> System.currentTimeMillis() - timeStamp > this.expireMillis);
    }
}
