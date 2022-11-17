package com.the60th.multinodes.core.mongo.persistence;

import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SimpleUpdatable implements Updatable {
    private final transient Bson update;

    public SimpleUpdatable(@NonNull String key, @Nullable Object value) {
        if (value instanceof Updatable u) {
            update = u.getMongoUpdate();
        } else {
            update = Updates.set(key, value);
        }
    }

    @Override
    public @NonNull Bson getMongoUpdate() {
        return this.update;
    }
}
