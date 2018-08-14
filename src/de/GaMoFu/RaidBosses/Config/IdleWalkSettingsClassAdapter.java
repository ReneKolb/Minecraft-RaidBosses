package de.GaMoFu.RaidBosses.Config;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class IdleWalkSettingsClassAdapter
        implements JsonSerializer<IdleWalkSettings>, JsonDeserializer<IdleWalkSettings> {

    @Override
    public IdleWalkSettings deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        String thePackage = "de.GaMoFu.RaidBosses.Config.";
        try {
            return context.deserialize(element, Class.forName(thePackage + type));
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unkown element type: " + type, e);
        }

    }

    @Override
    public JsonElement serialize(IdleWalkSettings src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getSimpleName()));
        result.add("properties", context.serialize(src, src.getClass()));
        return result;
    }

}
