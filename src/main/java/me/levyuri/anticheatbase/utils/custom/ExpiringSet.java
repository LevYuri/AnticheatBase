package me.levyuri.anticheatbase.utils.custom;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

public class ExpiringSet<E> extends AbstractSet<E> implements Set<E>, Serializable {
    private static final Object PRESENT = new Object();

    private final ConcurrentMap<E, Object> map;
    private final Cache<E, Object> cache;

    public ExpiringSet(final long expireMillis) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireMillis, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<Object, Object>() {
                    public @NotNull Object load(@NotNull final Object o) {
                        return PRESENT;
                    }
                });

        this.map = this.cache.asMap();
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override
    public int size() {
        this.cache.cleanUp();
        return this.map.size();
    }

    @Override
    public boolean add(final E e) {
        return this.map.put(e, ExpiringSet.PRESENT) == null;
    }
}