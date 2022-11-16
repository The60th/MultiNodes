package com.the60th.multinodes.land.data;

import java.util.Set;
import java.util.UUID;

/**
 * TownClaims {
 *   town_uuid,
 *   set_of_node_claims
 *   // etc
 * }
 *
 * */

public class TownClaims {
    int object_id;
    UUID town_uuid;
    Set<Object> node_claims;
}
