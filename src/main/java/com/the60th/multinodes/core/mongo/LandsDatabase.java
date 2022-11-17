package com.the60th.multinodes.core.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.the60th.multinodes.MultiNodes;
import com.the60th.multinodes.config.NodesConfig;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.concurrent.ExecutionException;

public class LandsDatabase {
    // executors
    private final MultiNodes plugin;
    private final MongoClient client;
    private final MongoDatabase database;
    // collections
    //TODO Nodes, Towns, Nations?
    private final MongoCollection<Object> personaCollection;
    private final MongoCollection<Object> playerDataCollection;
    // thread workers
    private final LandsDatabaseUpdateScheduler updateScheduler;
    private final LandsDatabaseQueryWorker workerPool;
    // codec registry
    private final CodecRegistry codecRegistry;

    public LandsDatabase(MultiNodes plugin){
        this.plugin = plugin;


        //TODO do we want to use codecs?
        codecRegistry = null;

        // connect to DB
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(NodesConfig.get().getMongoSettings().buildConnectionString()))
                .codecRegistry(codecRegistry)
                .serverApi(ServerApi.builder()
                        .version(ServerApiVersion.V1)
                        .build())
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .build();
        this.client = MongoClients.create(settings);
        this.database = client.getDatabase(NodesConfig.get().getMongoSettings().getDatabase());

        // collections
        this.personaCollection = database.getCollection(
                NodesConfig.get().getMongoSettings().getPersonaCollection(),
                Object.class
        ).withCodecRegistry(codecRegistry);
        this.playerDataCollection = database.getCollection(
                NodesConfig.get().getMongoSettings().getPlayerDataCollection(),
                Object.class
        ).withCodecRegistry(codecRegistry);

        // INDEXES
        // we don't need this one anymore
        /*this.personaCollection.createIndex(
                Indexes.compoundIndex(
                        Indexes.hashed(Persona.PLAYER),
                        Indexes.ascending(Persona.SLOT)
                )
        );*/
        this.playerDataCollection.createIndex(
                //TODO String?
                Indexes.hashed("STRING")
        );

        // thread workers
        this.updateScheduler = new LandsDatabaseUpdateScheduler(this);
        this.workerPool = new LandsDatabaseQueryWorker(this, plugin);
    }

    public @NonNull CodecRegistry getCodecRegistry() {
        return codecRegistry;
    }

    public @NonNull MongoCollection<Object> getPersonaCollection() {
        return personaCollection;
    }

    public @NonNull MongoCollection<Object> getPlayerDataCollection() {
        return playerDataCollection;
    }

    public @NonNull LandsDatabaseUpdateScheduler getUpdateScheduler() {
        return updateScheduler;
    }

    public @NonNull LandsDatabaseQueryWorker getWorkerPool() {
        return workerPool;
    }

    /**
     * Close the connection.
     */
    public void close() throws InterruptedException {
        try {
            updateScheduler.close();
            workerPool.close();
        } catch (InterruptedException | ExecutionException e) {
            MultiNodes.getLog().severe("Thread was interrupted while waiting for database workers to shut down!");
            e.printStackTrace();
        }
        client.close();
    }
}
