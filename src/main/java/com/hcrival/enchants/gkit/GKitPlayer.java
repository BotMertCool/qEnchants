package com.hcrival.enchants.gkit;

import com.hcrival.enchants.Enchants;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Data;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Data
public class GKitPlayer {

    private static MongoCollection<Document> collection = Enchants.getInstance().getMongoDatabase().getDatabase().getCollection("gkitz");
    @Getter private static Map<UUID, GKitPlayer> gKitPlayers = new HashMap<>();
    @Getter private static ExecutorService executorService = Executors.newFixedThreadPool(4);

    private UUID uuid;

    private Map<String, Long> kitCooldowns = new HashMap<>();

    public GKitPlayer(UUID uuid) {
        this.uuid = uuid;

        load();
    }

    public void load() {
        Document document = collection.find(Filters.eq("uuid", uuid.toString())).first();

        if (document == null) {
            save();
            document = collection.find(Filters.eq("uuid", uuid.toString())).first();
        }

        for (Map.Entry<String, Object> entry : document.entrySet()) {
            if (entry.getKey().equalsIgnoreCase("_id") || entry.getKey().equalsIgnoreCase("uuid")) continue;

            String key = entry.getKey();
            long value = document.getLong(key);

            if (System.currentTimeMillis() < value) {
                kitCooldowns.put(key, value);
            }
        }
    }

    public void save() {
        Document document = new Document();

        document.put("uuid", uuid.toString());

        for (Map.Entry<String, Long> entry : kitCooldowns.entrySet()) {
            if (System.currentTimeMillis() < entry.getValue()) {
                document.put(entry.getKey(), entry.getValue());
            }
        }

        collection.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

    public boolean canUseKit(String kit) {
        return kitCooldowns.get(kit) == null || System.currentTimeMillis() >= kitCooldowns.get(kit);
    }

    public static GKitPlayer getPlayer(Player player) {
        if (!gKitPlayers.containsKey(player.getUniqueId())) {
            gKitPlayers.put(player.getUniqueId(), new GKitPlayer(player.getUniqueId()));
        }

        return gKitPlayers.get(player.getUniqueId());
    }

}
