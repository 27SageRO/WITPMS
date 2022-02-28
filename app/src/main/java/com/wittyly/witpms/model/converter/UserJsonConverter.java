package com.wittyly.witpms.model.converter;


import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.wittyly.witpms.model.User;

import java.lang.reflect.Type;

import io.realm.RealmList;

public class UserJsonConverter implements JsonSerializer<RealmList<User>>, JsonDeserializer<RealmList<User>> {

    @Override
    public RealmList<User> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        RealmList<User> tags = new RealmList<>();
        JsonArray ja = json.getAsJsonArray();
        for (JsonElement je : ja) {
            tags.add((User) context.deserialize(je, User.class));
        }
        return tags;
    }

    @Override
    public JsonElement serialize(RealmList<User> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray ja = new JsonArray();
        for (User user : src) {
            ja.add(context.serialize(user));
        }
        return ja;
    }

}
