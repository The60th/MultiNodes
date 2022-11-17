package com.the60th.multinodes.core.mongo.persistence;

import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class CompoundUpdatable implements Updatable {
    private final transient Map<String, Updatable> updateMap = new ConcurrentHashMap<>();
    private final transient String parentField;

    public CompoundUpdatable(@NonNull String... parentField) {
        if (parentField.length == 0) this.parentField = null;
        else {
            var builder = new StringBuilder();
            for (int i = 0; i < parentField.length; i++) {
                builder.append(parentField[i]);
                if (i == parentField.length - 1) {
                    continue;
                }
                builder.append(".");
            }
            this.parentField = builder.toString();
        }
    }

    public CompoundUpdatable() {
        this.parentField = null;
    }

    public void addUpdate(@NonNull String key, @Nullable Object value) {
        if (value instanceof Updatable upd) {
            updateMap.put(key, upd);
        } else {
            updateMap.put(key, new SimpleUpdatable(prefix() + key, value));
        }
    }

    public void lazyUpdate(@NonNull String key, @NonNull Supplier<Object> supplier) {
        updateMap.put(key, new LazyUpdatable<>(prefix() + key, supplier));
    }

    public @Nullable Updatable removeUpdate(@NonNull String key) {
        return updateMap.put(key, new SimpleUpdatable(prefix() + key, null));
    }

    private @NonNull String prefix() {
        String prefix = "";
        if (this.parentField != null) {
            prefix = parentField + ".";
        }
        return prefix;
    }

    @Override
    public @NonNull Bson getMongoUpdate() {
        List<Bson> updates = new ArrayList<>();
        for (var updateable : updateMap.values()) {
            updates.add(updateable.getMongoUpdate());
        }
        return Updates.combine(updates);
    }

    public void clearUpdates() {
        updateMap.clear();
    }
}
