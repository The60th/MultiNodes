package com.the60th.multinodes.core.mongo;


import com.google.common.collect.Sets;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.WriteModel;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.config.NodesConfig;
import com.the60th.multinodes.core.mongo.persistence.CollectibleUpdatable;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.logging.Level;


public class LandsDatabaseUpdateScheduler {
    private final LandsDatabase db;

    private final ScheduledExecutorService updateScheduler;
    private final ScheduledFuture<?> task;

    private final Set<CollectibleUpdatable> personaUpdateQueue = Sets.newConcurrentHashSet();
    private final Set<CollectibleUpdatable> playerDataUpdateQueue = Sets.newConcurrentHashSet();

    public LandsDatabaseUpdateScheduler(LandsDatabase database) {
        this.db = database;

        updateScheduler = Executors.newSingleThreadScheduledExecutor();

        task = updateScheduler.scheduleAtFixedRate(
                this::tick,
                NodesConfig.get().getInternalSettings().getUpdatePeriodMillis(),
                NodesConfig.get().getInternalSettings().getUpdatePeriodMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    private void tick() {
        flushQueue(personaUpdateQueue, db.getPersonaCollection());
        flushQueue(playerDataUpdateQueue, db.getPlayerDataCollection());
    }

    private static <T extends CollectibleUpdatable> void flushQueue(@NonNull Set<CollectibleUpdatable> queue, @NonNull MongoCollection<Object> collection) {
        final List<WriteModel<T>> bulkWrite = new ArrayList<>();
        var it = queue.iterator();
        var options = new UpdateOptions().upsert(true);
        while (it.hasNext()) {
            var update = it.next();
            bulkWrite.add(
                    new UpdateOneModel<>(
                            Filters.eq(update.getId()),
                            update.getMongoUpdate(),
                            options
                    )
            );
            update.clearUpdates();
        }
        if (!bulkWrite.isEmpty())
            collection.bulkWrite(bulkWrite);
        queue.clear();
    }

    public void queueUpdate(@NonNull CollectibleUpdatable updatable) {
        MultiNodes.debug(() -> {
            MultiNodes.getLog().log(Level.INFO, "QUEUED UPDATE!!!!!!!!!!!!!!!!!!!!!!!!!!");
            MultiNodes.getLog().log(Level.INFO, updatable.toString());
        });
        //TODO Types
        if (updatable instanceof Object) {
            personaUpdateQueue.add(updatable);
        } else if (updatable instanceof Object) {
            playerDataUpdateQueue.add(updatable);
        }
    }

    public void close() throws InterruptedException, ExecutionException {
        task.cancel(false);
        updateScheduler.submit(this::tick).get();
        boolean success = updateScheduler.awaitTermination(
                NodesConfig.get().getInternalSettings().getShutdownMillis(),
                TimeUnit.MILLISECONDS
        );
        if (!success) {
            MultiNodes.getLog().severe("Scheduler timed out before properly shutting down!");
        }
    }
}
