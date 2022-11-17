package com.the60th.multinodes.core.mongo.persistence;

import org.bson.conversions.Bson;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Updatable {
    @NonNull Bson getMongoUpdate();
}
