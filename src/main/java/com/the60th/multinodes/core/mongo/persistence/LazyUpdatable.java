package com.the60th.multinodes.core.mongo.persistence;

import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Function;
import java.util.function.Supplier;

public class LazyUpdatable<T> implements Updatable {
    private final transient String key;
    private final transient Supplier<Object> supplier;

    public LazyUpdatable(@NonNull String key, @NonNull Supplier<Object> supplier) {
        this.key = key;
        this.supplier = supplier;
    }

    @Override
    public @NonNull Bson getMongoUpdate() {
        var obj = supplier.get();
        if (obj instanceof Updatable updatable) {
            return updatable.getMongoUpdate();
        } else {
            return Updates.set(key, supplier.get());
        }
    }
}
