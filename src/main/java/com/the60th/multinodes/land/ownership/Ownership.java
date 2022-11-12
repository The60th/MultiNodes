package com.the60th.multinodes.land.ownership;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.the60th.multinodes.land.ownership.owner.Owner;
import com.the60th.multinodes.land.ownership.owner.PlayerOwner;

import java.util.ArrayList;

public class Ownership {

    private ArrayList<Owner> owners = new ArrayList<>();

    public Ownership(){

    }

    public Ownership(ArrayList<Owner> owners){
        this.owners = owners;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("owner_size",owners.size());
        int x = 0;
        for (Owner o: owners) {
            JsonObject ownerShip = o.toJson();
            jsonObject.add("owner_"+x,ownerShip);
            x++;
        }

        return jsonObject;
    }

    public void addOwner(Owner owner){
        if(owner instanceof PlayerOwner){
            System.out.println("Adding Player Owner");
            owners.add(owner);
        }
        //TODO Else if for other types
    }

    public static Ownership fromJson(String json){
        assert json != null;
        Ownership ownership = new Ownership();
        try {
            JsonObject obj = new Gson().fromJson(json,JsonObject.class);
            int size = Integer.parseInt(String.valueOf(obj.get("owner_size")).replaceAll("\"",""));
            for (int i = 0; i < size; i++){
                String s = String.valueOf(obj.get("owner_"+i)).replaceAll("\"","");
                PlayerOwner playerOwner = PlayerOwner.fromJson(s);
                ownership.addOwner(playerOwner);
            }
            System.out.println("OwnerShip: " +  obj.toString());
        }catch (Exception e){
            //System.out.println("Error: Failed to parse Ownership");
            return new Ownership();
        }

        return ownership;
    }

}
