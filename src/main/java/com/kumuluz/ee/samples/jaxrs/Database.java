package com.kumuluz.ee.samples.jaxrs;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

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

}
