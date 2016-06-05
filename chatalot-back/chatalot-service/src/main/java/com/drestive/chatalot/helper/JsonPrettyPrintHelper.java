package com.drestive.chatalot.helper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Helper class to convert objects to JSON for debugging, etc.
 *
 */
public class JsonPrettyPrintHelper {
    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Return object as JSON
     *
     * @param obj
     * @return obj as JSON
     */
    public static String toJSON(Object obj) {
        return gson.toJson(obj);
    }

}
