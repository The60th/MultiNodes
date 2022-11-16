package com.the60th.multinodes.land.data;

import java.util.UUID;

/**
 *   Town {
 *   object_id,
 *   // for mongo indexing
 *   uuid,
 *   name,
 *   date_created
 *   // more meta data here
 *   // note no "game data" is stored here
 * }
 *
 *
 * */

public class Town {
    int object_id;
    UUID uuid;
    String name;
    int data_created; //Unix Time stamp?
}


