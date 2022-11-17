package com.the60th.multinodes.core.mongo.persistence;

import org.bson.types.ObjectId;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface Identifiable {
    @NonNull ObjectId getId();
}
