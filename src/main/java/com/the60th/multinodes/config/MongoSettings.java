package com.the60th.multinodes.config;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class MongoSettings {
    // ---CONFIG PROPERTIES---
    private String username = "admin";
    private String password = "password";
    private String host = "localhost";
    private int port = 27017;
    private String database = "database";
    private String playerDataCollection = "persona_playerdata";
    @Comment("The name of the collection that stores persona data. ie: name, race, age, etc.")
    private String personaCollection = "persona_personas";
    @Comment("Alternatively you can simply define a connection string. If this is empty, personas will try to construct its own connection string.")
    private String connectionString = "mongodb+srv://admin:admin@serverless-test-serber.wk1x6.mongodb.net/test";

    public String getPlayerDataCollection() {
        return playerDataCollection;
    }

    public int getPort() {
        return port;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public String getPersonaCollection() {
        return personaCollection;
    }

    public @NonNull String buildConnectionString() {
        if (connectionString == null || connectionString.equals("")) {
            return "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/?maxPoolSize=20&w=majority";
        } else {
            return connectionString;
        }
    }
}