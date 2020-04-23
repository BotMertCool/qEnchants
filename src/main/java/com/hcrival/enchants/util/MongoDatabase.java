package com.hcrival.enchants.util;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import lombok.Getter;

@Getter
public class MongoDatabase {

    private com.mongodb.client.MongoDatabase database;

    public MongoDatabase(String databaseName) {
        MongoClient client = new MongoClient(new ServerAddress("127.0.0.1", 27017));

        database = client.getDatabase(databaseName);
    }

}
