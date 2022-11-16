package com.the60th.multinodes.land.data;

import java.util.Set;
import java.util.UUID;

/**
 * TownPopulation {
 *   town_uuid,
 *   owner_player,
 *   member_set
 * }
 *
 * */
public class TownPopulation {
    int object_id;
    UUID town_uuid;
    UUID owner_player;
    Set<Object> member_map;
}
