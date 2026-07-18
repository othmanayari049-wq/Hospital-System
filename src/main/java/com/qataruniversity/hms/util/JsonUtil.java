package com.qataruniversity.hms.util;

import java.math.BigDecimal;
import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.Map;

public final class JsonUtil {
    private JsonUtil() { }

    public static String toJson(Object value) {
        if (value == null) return "null";
        if (value instanceof String || value instanceof Character || value instanceof Enum<?> || value instanceof TemporalAccessor) {
            return quote(value.toString());
        }
        if (value instanceof BigDecimal decimal) return decimal.toPlainString();
        if (value instanceof Number || value instanceof Boolean) return value.toString();
        if (value instanceof Map<?, ?> map) {
            return map.entrySet().stream()
                    .map(entry -> quote(String.valueOf(entry.getKey())) + ":" + toJson(entry.getValue()))
                    .reduce((a, b) -> a + "," + b).map(items -> "{" + items + "}").orElse("{}");
        }
        if (value instanceof Collection<?> collection) {
            return collection.stream().map(JsonUtil::toJson)
                    .reduce((a, b) -> a + "," + b).map(items -> "[" + items + "]").orElse("[]");
        }
        return quote(value.toString());
    }

    private static String quote(String text) {
        return "\"" + text.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r") + "\"";
    }
}
