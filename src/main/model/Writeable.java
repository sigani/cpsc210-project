package model;

import org.json.JSONObject;

/**
 * This is just to force toJson implementation
 */
public interface Writeable {
    /**
     * EFFECTS: returns this object as a JSON object
     * @return this as a JSON object
     */
    JSONObject toJson();
}
