# MultiNodes Architecture

For tech stack we'll be working with Redis for a shared cache, MongoDB for persistence, 
and MultiLib message passing as a messaging service. 

MongoDB is where we will store the entirety of 
our data across restarts. Redis will act as a shared cache across all the MP subservers. Population 
strategies for Redis can be discussed later, but generally we want to either start with an empty or
small subset of the data initially cached and continuously populate as activity ramps up. 

For communication between subservers, we use MultiLib's built in message passing service. Redis has
built in support for a publisher-subscriber relationship for clients, I figure since we're using
Multipaper we may as well use Multipaper's built-in functionality for it.

## Shared Cache, Database, Local Cache

We will have 3 layers of data storage. I'll start with the shared cache. This will be a Redis instance
accessible by all subservers. The shared cache will contain all data stored by any of the subservers. In
other words if the data is stored on at least a single subserver, then it will also be on the shared
cache. Depending on how often we expect restarts of the entire system, purging the shared cache may
not be necessary. 

The database stores all nodes-related data in its entirety. We will only query the database when a
subserver encounters data that is not currently loaded in the shared cache and/or at server startup when
or if we decide to initially populate the shared cache with data.

Each MP subserver will have its own local cache embedded with the MultiNodes plugin. Since we'll
most likely be running Redis on the same machine as our subservers, it seems unnecessary to include
a local cache on top of our shared cache. However, Redis is single threaded, and I would argue that making
Redis queries on almost every event/command will incur a significant performance cost. We only need to
populate the local cache with only data that is accessible by the players being ticked by the subserver
that it is running on. We only include the data we need in local cache, and query the Redis cache (or
database if it's not on Redis)

## Nodes

Conceptually a node is simple a grouping of chunks, with one chunk within this set designated
as the home chunk. We can define them like so:

```json
{
  "id": "ObjectID here",
  "chunks": [
    12312313213,
    32134565467,
    32134235667
  ],
  "home_chunk": 12312313213
}
```

Where `chunks` is an array of chunk keys (you obtain these using `Chunk#getChunkKey()` in Bukkit) and
`home_chunk` is some chunk key from the array of `chunks`. We include an `ObjectID` for MongoDB indexing.

### Claims vs Permissions

Since we're only trying to build functionality for nodes here, we don't actually need to store the
ownership of every single chunk on the server in database. We can simply store the ownership of nodes
on database, and include chunk overrides set by users for any specific chunk. This system can also
be useful for setting permission overrides for chunks claimed during war.

Before we continue I want to note here that there is a difference between the *ownership* and 
the *permissions* of land. The concept of *ownership* really only applies to nodes. Ownership of a town
implies onwership of said node's chunks. Only a single town may own a node. *Permissions* of chunks and
nodes define what can be done and by who in claimed land. Only owners can modify permissions.

## Design

The actual design of the plugin follows a non-standard approach to typical OO style. We split
functionality (and data/persistence/cache validation) in pieces, and implement these pieces of
functionality encapsulated from one another. How we split this functionality is completely arbitrary,
but the purpose is to encapsulate sets of features that make sense to be conceptually grouped. Code-wise
we can put the implementations of each feature set in its own package with its related classes. Each
"feature" will handle its own persistence and cache propogation on other subservers.

At this point it may make sense to define a set of shared interfaces for providing a general framework
for each of these features to work within. I would argue, however, that doing so does not actually add
much value. Very rarely, if ever, will we ever be working with these features simple as some arbitrary
`Feature` type and not some specific `OwnershipFeature` or `PermissionsFeature` type. To put it another way,
when we're working with some feature, we will be specifically working with that feature, not some 
arbitrary feature.

What we will need to do, however, is provide a set of Redis, MP propogation, and MongoDB APIs with which
to build out a sort of *Meta-API* that can be used to build features. After all, the idea of permissions,
land claiming, town/nation relations, and town rosters is not difficult in concept or in practice to
implement. Each of these requires just a few listeners and some data classes to implement. What is tricky
and is also a shared concern across each of these "features" is persistence and cache propogation.

### "Meta-API"

Earlier I mentioned the concept of building a "Meta-API" that we can use to build out features. What I
mean by this is essentially a set of infrastructure APIs that we, as the developers of Nodes, will use
to handle our persistence and caching concerns. These can range from simple database drivers to
boilerplate for Ser/De java objects into byte arrays and back. I still need to mull over this stuff 
but I'll have concrete implementation soon.

### Land Ownership

I'll give an example schema and class definition for land claiming. Once again I want to note 
that **ownership** and **permissions** of land are 2 different things! Permissions are a more
complicated in terms of data schema, but the same design principles should apply here:

```java
// main class
public class OwnershipFeature {
    private final OwnershipStore store;
    private final OwnershipListeners listeners;
    private final OwnershipDB db;
    private final OwnershipCache cache;
    
    public OwnershipFeature() {
        // impl
    }
    
    // these 2 methods could be in an interface
    public void startup() {
        
    }
    
    public void shutdown() {
        
    }
}

// for the next couple classes assume that they all have
// the store, listeners, persistence, and cache instances
// that are members of the main class

public class OwnershipStore {
    // node -> owning town
    // this is the "local cache" so to speak
    private final Map<Node, Town> ownershipMap = new HashMap<>(); // consider switching to BST
    // keeps track of all the nodes we have loaded
    private final Set<Node> loadedNodes = new HashSet<>(); // consider BST
    
    public void setOwnership(Node node, Town town) {
        ownershipMap.put(node.getId(), town);
        cache.updateSharedCacheOwnershipChange(node, town);
        db.updateSharedCacheOwnershipChange(node, town);
    }
}

public class OwnershipListeners {
    public void onChunkLoad(ChunkLoadEvent e) {
        var chunk = e.getChunk();
        if (!store.contains(chunk)) {
            // not real code
            // query redis
            var res = cache.query(chunk);
            if (res == null) { // if not loaded, query db
                res = db.query(chunk);
            }
            // update our store with resulting data
        }
    }
    
    public void onChunkUnload(ChunkUnloadEvent e) {
        // purge it from cache
    }
    
    // plus whatever listeners that modify game behavior
}

// util class
public final class OwnershipSerde {
    private OwnershipSerde() {
    }
    
    public static byte[] serializeOwnershipChange(Node node, Town town) {
        // impl
    }
    
    public static Tuple<Node, Town> deserializeOwnershipChange(Node node, Town town) {
        // impl
    }
}

// this class could use an interface, or we define the
// MultiPaper message receiver in a different class
public class OwnershipCache {
    
    // to accept messages
    public onMessageReceive(String channel, byte[] payload) {
        if (channel.equals("ownership_channel")) {
            // process payload, update store as necessary
        }
    }
    
    public updateSharedCacheOwnershipChange(Node node, Town town) {
        // update params to some intermediate form 
        // our centralized Redis handler can swallow
        var update = "";
        RedisCache.queueUpdate(update);
        CachePropogator.queueUpdate(
            "ownership_channel", 
            OwnershipSerde.serializeOwnershipChange(node, town) // byte array
        );
    }
    
    // other methods
}

public class OwnershipDB {
    public updateSharedCacheOwnershipChange(Node node, Town town) {
        // same deal as cache, put it into some form our database
        // can swallow, then queue the update to that
        var update = MongoQueryBuilder.whatever();
        MongoDB.queueUpdate(update);
    }
    // other methods
}
```

Essentially, our feature code consists purely the data and listeners needed to make it run
with some limited glue code that lets our feature interface with pre-existing 
infrastructure API that handles persistence and caching stuff. Like I mentioned earlier, I still
need some time to mull over the design of this internal infrastructure API, but the basic concept
is that we will have some Redis and Mongo boilerplate classes to wrap around raw queries/updates.
Moreover, we can also implement some kind of ticker system to do batch updates and queries as well
as batch together multiple MP propogation messages.
