package com.the60th.multinodes.core.mongo;

import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.config.NodesConfig;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

public class LandsDatabaseQueryWorker {
    private final MultiNodes plugin;
    private final LandsDatabase db;

    private final ExecutorService workerPool = Executors.newCachedThreadPool();

    private final ConcurrentMap<OfflinePlayer, CompletableFuture<Object>> playerDataQueryMap = new ConcurrentHashMap<>();
    private final Object playerDataQueryLock = new Object();

    private final ConcurrentMap<ObjectId, CompletableFuture<Object>> singleQueryMap = new ConcurrentHashMap<>();
    private final ConcurrentMap<ObjectId, CompletableFuture<Map<ObjectId, Object>>> multiQueryMap = new ConcurrentHashMap<>();

    public LandsDatabaseQueryWorker(@NonNull LandsDatabase db,@NonNull MultiNodes plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    private @Nullable CompletableFuture<@Nullable Object> getQuery(@NonNull ObjectId id) {
        /*
        var singleFuture = singleQueryMap.get(id);
        if (singleFuture != null) return singleFuture;
        var multiFuture = multiQueryMap.get(id);
        if (multiFuture != null) return multiFuture.thenApply(map -> map.get(id));
        return null;*/
        return null;
    }

    /*private void updateCache(@NonNull Persona persona, @NonNull LoadReason reason) {
        plugin.getApiCache().add(persona);
        if (reason == LoadReason.LOGIN)
            plugin.getOnlinePersonaCache().add(persona);
    }*/

    private synchronized void cleanupPersonaQueryMap(@NonNull ObjectId id) {
        singleQueryMap.remove(id);
        multiQueryMap.remove(id);
    }

    private void cleanupPlayerDataQueryMap(@NonNull OfflinePlayer player) {
        synchronized (playerDataQueryLock) {
            playerDataQueryMap.remove(player);
        }
    }

    // TODO surge protection
    public synchronized @NonNull CompletableFuture<@Nullable Object> getPersona(@NonNull ObjectId id, @NonNull Object reason) {
        /*
        var cachedFut = getQuery(id);
        if (cachedFut != null) return cachedFut;
        var future = CompletableFuture.supplyAsync(() -> {
            Bukkit.getPluginManager().callEvent(new PrePersonaLoadEvent(List.of(id), reason)); // api event
            var persona = getPersonaFromDatabase(id);
            if (persona != null) {
                Bukkit.getPluginManager().callEvent(new PostPersonaLoadEvent(List.of(persona), reason));
                plugin.getPersonaFactory().register(persona, reason == LoadReason.LOGIN);
            }
            cleanupPersonaQueryMap(id);
            return persona;
        }, workerPool);
        singleQueryMap.put(id, future);
        return future;

         */
        return null;
    }

    public synchronized @NonNull CompletableFuture<@NonNull Map<ObjectId, Object>> getPersonas(@NonNull Collection<ObjectId> ids, @NonNull Object reason) {
        /*
        var alreadyQueried = new ArrayList<CompletableFuture<Persona>>();
        final var toBeQueried = new ArrayList<ObjectId>();
        for (var id : ids) {
            var cachedFuture = getQuery(id);
            if (cachedFuture != null) alreadyQueried.add(cachedFuture);
            else toBeQueried.add(id);
        }
        CompletableFuture<Map<ObjectId, Persona>> result;
        CompletableFuture<Persona>[] alreadyQueriedCopy = new CompletableFuture[alreadyQueried.size()];
        alreadyQueried.toArray(alreadyQueriedCopy);
        var query = CompletableFuture.supplyAsync(() -> {
            Bukkit.getPluginManager().callEvent(new PrePersonaLoadEvent(ids, reason)); // api event
            var personas = getPersonasFromDatabase(ids);
            Bukkit.getPluginManager().callEvent(new PostPersonaLoadEvent(new ArrayList<>(personas), reason)); // api event
            return personas;
        }, workerPool);
        result = CompletableFuture.allOf(
                query,
                CompletableFuture.allOf(alreadyQueriedCopy)
        ).thenApply(nothing -> {
            Map<ObjectId, Persona> map = new ConcurrentHashMap<>();
            try {
                var personaColl = query.get();
                for (var persona : personaColl) {
                    map.put(persona.getId(), persona);
                    plugin.getPersonaFactory().register(persona, reason == LoadReason.LOGIN);
                }

                // cleanup query map
                for (var id : toBeQueried)
                    cleanupPersonaQueryMap(id);

                for (var fut : alreadyQueried) {
                    var persona = fut.get();
                    if (persona != null)
                        map.put(persona.getId(), persona);
                }
            } catch (InterruptedException | ExecutionException e) {
                Personas.logger().severe("Encountered an unexpected exception while loading persona from database");
                throw new RuntimeException(e);
            }
            return map;
        });
        for (var id : toBeQueried) {
            multiQueryMap.put(id, result);
        }
        return result;

         */
        return null;
    }

    public @NonNull CompletableFuture<@Nullable Object> getPlayerData(@NonNull OfflinePlayer player) {
        /*
        synchronized (playerDataQueryLock) {
            CompletableFuture<PlayerData> query = playerDataQueryMap.get(player);
            if (query != null) return query;
            var future = CompletableFuture.supplyAsync(() -> {
                var playerData = getPlayerDataFromDatabase(player);
                if (playerData != null)
                    plugin.getPlayerDataCache().add(playerData);
                cleanupPlayerDataQueryMap(player);
                return playerData;
            }, workerPool);
            playerDataQueryMap.put(player, future);
            return future;
        }*/
        return null;
    }

    // database methods
    private @Nullable Object getPlayerDataFromDatabase(@NonNull OfflinePlayer player) {
        /*
        var filter = Filters.eq(PlayerData.PLAYER, player.getUniqueId());
        var res = db.getPlayerDataCollection().find(filter).first();
        Personas.debug(() -> {
            Personas.logger().log(Level.INFO, "-----------PLAYERDATA------------");
            Personas.logger().log(Level.INFO, res != null ? res.toString() : null);
            Personas.logger().log(Level.INFO, "RAW DOCUMENT TEST");
            var doc = db.getPlayerDataCollection().find(filter, Document.class).first();
            Personas.logger().log(Level.INFO, doc != null ? doc.toString() : null);
        });
        if (res != null)
            res.setShouldUpdate(true);
        return res;
        */
        return null;
    }

    private @Nullable Object getPersonaFromDatabase(@NonNull ObjectId id) {
        /*
        var filter = Filters.eq(id);
        var persona = db.getPersonaCollection().find(filter).first();
        if (persona != null) {
            persona.setShouldUpdate(true);
            persona.setPropogateOnlineUpdates(true);
        }
        return persona;*/
        return null;
    }

    private @NonNull Collection<@NonNull Object> getPersonasFromDatabase(@NonNull Collection<ObjectId> ids) {
        /*
        if (ids.isEmpty()) return new ArrayList<>();
        var filter = Filters.in(Persona.ID, ids);
        Collection<Persona> res = db.getPersonaCollection().find(filter).into(new ArrayList<>());
        for (var persona : res)
            if (persona != null)
                persona.setShouldUpdate(true);
        return res;*/
        return null;
    }

    public void close() throws InterruptedException {
        boolean success = workerPool.awaitTermination(
                NodesConfig.get().getInternalSettings().getShutdownMillis(),
                TimeUnit.MILLISECONDS
        );
        if (!success) {
            MultiNodes.getLog().severe("Query worker pool timed out before properly shutting down!");
        }
    }
}
