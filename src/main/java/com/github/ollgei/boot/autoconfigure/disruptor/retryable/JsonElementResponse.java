package com.github.ollgei.boot.autoconfigure.disruptor.retryable;

import java.math.BigDecimal;

import com.github.ollgei.base.commonj.gson.JsonArray;
import com.github.ollgei.base.commonj.gson.JsonElement;
import com.github.ollgei.base.commonj.gson.JsonNull;
import com.github.ollgei.base.commonj.gson.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * desc.
 * @author ollgei
 * @since 1.0
 */
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JsonElementResponse extends RetryableDownstreamResponse {

    private JsonElement element;

    public JsonElementResponse(JsonElement element) {
        this.element = element;
    }

    public JsonElement deepCopy() {
        return element.deepCopy();
    }

    public JsonElement getElement() {
        return element;
    }

    public void put(String property, String value) {
        if (element.isJsonObject()) {
            ((JsonObject) element).addProperty(property, value);
        }
    }

    public void put(String property, Number value) {
        if (element.isJsonObject()) {
            ((JsonObject) element).addProperty(property, value);
        }
    }

    public void put(String property, Boolean value) {
        if (element.isJsonObject()) {
            ((JsonObject) element).addProperty(property, value);
        }
    }

    public void put(String property, Character value) {
        if (element.isJsonObject()) {
            ((JsonObject) element).addProperty(property, value);
        }
    }

    public void add(String string) {
        if (element.isJsonArray()) {
            ((JsonArray) element).add(string);
        }
    }

    public void add(Number number) {
        if (element.isJsonArray()) {
            ((JsonArray) element).add(number);
        }
    }

    public void add(Boolean bool) {
        if (element.isJsonArray()) {
            ((JsonArray) element).add(bool);
        }
    }

    public void add(Character character) {
        if (element.isJsonArray()) {
            ((JsonArray) element).add(character);
        }
    }

    public JsonElement remove(String property) {
        if (element.isJsonObject()) {
            ((JsonObject) element).remove(property);
        }
        return JsonNull.INSTANCE;
    }

    public JsonElement remove(int index) {
        if (element.isJsonArray()) {
            return ((JsonArray) element).remove(index);
        }
        return JsonNull.INSTANCE;
    }

    public JsonElement get(String memberName) {
        if (element.isJsonObject()) {
            return ((JsonObject) element).get(memberName);
        }
        return JsonNull.INSTANCE;
    }

    public String getAsString(String memberName) {
        if (element.isJsonObject()) {
            final JsonElement element = get(memberName);
            if (element.isJsonPrimitive()) {
                return element.getAsString();
            }
        }
        return "";
    }

    public BigDecimal getAsBigDecimal(String memberName) {
        if (element.isJsonObject()) {
            final JsonElement element = get(memberName);
            if (element.isJsonPrimitive()) {
                return element.getAsBigDecimal();
            }
        }
        return BigDecimal.ZERO;
    }

    public int getAsInt(String memberName) {
        if (element.isJsonObject()) {
            final JsonElement element = get(memberName);
            if (element.isJsonPrimitive()) {
                return element.getAsInt();
            }
        }
        return 0;
    }

    public boolean getAsBoolean(String memberName) {
        if (element.isJsonObject()) {
            final JsonElement element = get(memberName);
            if (element.isJsonPrimitive()) {
                return element.getAsBoolean();
            }
        }
        return false;
    }

    public JsonElement get(int index) {
        if (element.isJsonArray()) {
            return ((JsonArray) element).get(index);
        }
        return JsonNull.INSTANCE;
    }

    public String getAsString(int index) {
        if (element.isJsonArray()) {
            final JsonElement element = get(index);
            if (element.isJsonPrimitive()) {
                return element.getAsString();
            }
        }
        return "";
    }

    public BigDecimal getAsBigDecimal(int index) {
        if (element.isJsonObject()) {
            final JsonElement element = get(index);
            if (element.isJsonPrimitive()) {
                return element.getAsBigDecimal();
            }
        }
        return BigDecimal.ZERO;
    }

    public int getAsInt(int index) {
        if (element.isJsonObject()) {
            final JsonElement element = get(index);
            if (element.isJsonPrimitive()) {
                return element.getAsInt();
            }
        }
        return 0;
    }

    public boolean getAsBoolean(int index) {
        if (element.isJsonObject()) {
            final JsonElement element = get(index);
            if (element.isJsonPrimitive()) {
                return element.getAsBoolean();
            }
        }
        return false;
    }
}
