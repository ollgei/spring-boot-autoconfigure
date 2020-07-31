package com.github.ollgei.spring.boot.autoconfigure.gson.cache;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import com.github.ollgei.base.commonj.gson.Gson;
import com.github.ollgei.base.commonj.gson.GsonBuilder;
import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.gson.JsonObject;
import com.github.ollgei.base.commonj.gson.reflect.TypeToken;

/**
 * gson.
 * @author ollgei
 * @since 1.0.0
 */
public class GsonCache {

    private final Gson gson;

    public GsonCache(GsonBuilder builder) {
        this(builder.create());
    }

    public GsonCache(Gson gson) {
        this.gson = gson;
    }

    public String toJson(Object src) {
        return gson.toJson(src);
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public <T> T fromJson(String json, TypeToken<T> typeOfT) {
        return gson.fromJson(json, typeOfT.getType());
    }

    public JsonObject toJsonObject(Object data) {
        JsonElement element = gson.toJsonTree(data);
        if (element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        return null;
    }

    public Map<String, Object> beanToMap(Object bean) {
        final JsonElement element = toJsonObject(bean);
        if (Objects.isNull(element)) {
            return Collections.emptyMap();
        }
        return gson.fromJson(element, new TypeToken<Map<String, Object>>(){}.getType());
    }

}
