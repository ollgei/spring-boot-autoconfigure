package com.github.ollgei.spring.boot.autoconfigure.gson.spring;

import java.lang.reflect.Type;

import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.gson.JsonParser;
import com.github.ollgei.base.commonj.gson.JsonSerializationContext;
import com.github.ollgei.base.commonj.gson.JsonSerializer;
import springfox.documentation.spring.web.json.Json;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
public class SpringfoxJsonToGsonAdapter implements JsonSerializer<Json> {

    @Override
    public JsonElement serialize(Json json, Type type, JsonSerializationContext context) {
        return JsonParser.parseString(json.value());
    }

}