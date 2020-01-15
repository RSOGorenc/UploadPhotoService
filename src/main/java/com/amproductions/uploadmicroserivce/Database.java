package com.amproductions.uploadmicroserivce;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.json.JsonObject;
import javax.json.bind.JsonbBuilder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Database {
    private static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://rso-mongo-service:27017"));
    private static MongoDatabase database = mongoClient.getDatabase("imagePlatform");
    private static MongoCollection collection = database.getCollection("images");

    final static Class<? extends List> docClazz = new ArrayList<String>().getClass();


    @SuppressWarnings("unchecked")
    static boolean AddImage(ImageEntry image){
        try {
            String json = JsonbBuilder.create().toJson(image);
            BasicDBObject dbObject = BasicDBObject.parse(json);
            collection.insertOne(new org.bson.Document(dbObject.toMap()));
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static boolean Exists(String imageId){
        Bson entry = (Bson) collection.find(Filters.eq("imageId", imageId)).first();
        if(entry == null){
            return false;
        }
        return true;
    }

    static boolean CheckOwnership(String imageId, String userId){
        Bson entry = (Bson) collection.find(Filters.and(Filters.eq("imageId", imageId), Filters.eq("userId", userId))).first();
        if(entry == null){
            return false;
        }
        return true;
    }

    static boolean CheckShare(String imageId, String userId){
        Document entry = (Document) collection.find(Filters.eq("imageId", imageId)).first();
        try {
            org.json.simple.JSONObject json = (JSONObject) new JSONParser().parse(entry.toJson());
            org.json.simple.JSONArray array = (JSONArray) json.get("shareUsers");
            Iterator itr = array.iterator();
            while (itr.hasNext()){
                if(userId.equals(itr.next())){
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    static boolean RemoveImage(String imageId, String userId){

        Bson entry = (Bson) collection.find(Filters.and(Filters.eq("imageId", imageId), Filters.eq("userId", userId))).first();
        if(entry == null){
            return false;
        }
        collection.deleteOne(entry);
        return true;
    }
}
