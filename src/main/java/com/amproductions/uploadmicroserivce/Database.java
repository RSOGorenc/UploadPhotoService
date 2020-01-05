package com.amproductions.uploadmicroserivce;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

import javax.json.bind.JsonbBuilder;

class Database {
    private static MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://192.168.99.100:27017"));
    private static MongoDatabase database = mongoClient.getDatabase("imagePlatform");
    private static MongoCollection collection = database.getCollection("images");

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

    static boolean RemoveImage(String imageId, String userId){

        Bson entry = (Bson) collection.find(Filters.and(Filters.eq("imageId", imageId), Filters.eq("userId", userId))).first();
        if(entry == null){
            return false;
        }
        collection.deleteOne(entry);
        return true;
    }

}
