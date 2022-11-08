package com.the60th.multinodes.land.ownership;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Ownership {

    private ArrayList<OwnerType> owners;

    public Ownership(){

    }

    public Ownership(ArrayList<OwnerType> owners){
        this.owners = owners;
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();

        return jsonObject;
    }

    public static Ownership fromJson(String json){
        JsonObject obj = new Gson().fromJson(json,JsonObject.class);
        return new Ownership();
    }
}
