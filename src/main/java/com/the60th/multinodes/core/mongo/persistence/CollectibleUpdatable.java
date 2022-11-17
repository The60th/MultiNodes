package com.the60th.multinodes.core.mongo.persistence;

import com.the60th.multinodes.MultiNodes;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.function.Supplier;

public abstract class CollectibleUpdatable extends CompoundUpdatable implements Identifiable {
    private boolean shouldUpdate = false;

    public CollectibleUpdatable(@NonNull String... parentField) {
        super(parentField);
    }

    public CollectibleUpdatable() {
        super();
    }

    @Override
    public void addUpdate(@NonNull String key, @Nullable Object value) {
        if (!shouldUpdate) return;
        super.addUpdate(key, value);
        MultiNodes.get().getDatabase().getUpdateScheduler().queueUpdate(this);
    }

    @Override
    public @Nullable Updatable removeUpdate(@NonNull String key) {
        if (!shouldUpdate) return null;
        MultiNodes.get().getDatabase().getUpdateScheduler().queueUpdate(this);
        return super.removeUpdate(key);
    }

    @Override
    public void lazyUpdate(@NonNull String key, @NonNull Supplier<Object> supplier) {
        if (!shouldUpdate) return;
        super.lazyUpdate(key, supplier);
        MultiNodes.get().getDatabase().getUpdateScheduler().queueUpdate(this);
    }

    public void lazyUpdate(@NonNull String key, @NonNull Object value) {
        if (!shouldUpdate) return;
        this.lazyUpdate(key, () -> value);
    }

    public boolean shouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
}
