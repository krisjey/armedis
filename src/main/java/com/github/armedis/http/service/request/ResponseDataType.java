
package com.github.armedis.http.service.request;

import java.util.HashMap;

public enum ResponseDataType {
    JSON,
    PLAIN_TEXT;

    private static final HashMap<String, ResponseDataType> values = new HashMap<String, ResponseDataType>();

    static {
        for (ResponseDataType type : ResponseDataType.values()) {
            values.put(type.name().toLowerCase(), type);
        }

        values.put("application/json", JSON);
        values.put("text/plain", PLAIN_TEXT);
        values.put("text", PLAIN_TEXT);
    }

    /**
     * Default value is json, This method always return ResponseDataType. 
     * @param typeName
     * @return ResponseDataType if typeName is not valid then return JSON
     */
    public static ResponseDataType of(String typeName) {
        if (typeName == null) {
            return JSON;
        }

        ResponseDataType result = values.get(typeName.toLowerCase());

        if (result == null) {
            result = JSON;
        }

        return result;
    }
}
