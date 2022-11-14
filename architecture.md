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

