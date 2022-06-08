package model;

import org.json.JSONObject;

//Player object
//The players playing the tournament
public class Player implements Writeable {
    String name;

    /**
     * EFFECTS: creates a player object with the name given
     * @param n name of the player
     */
    public Player(String n) {
        name = n;
        EventLog.getInstance().logEvent(new Event("Created player named " + n));
    }

    /**
     * EFFECTS: creates a "Bye" object, essentially.
     */
    public Player() {
        name = "Bye";
    }

    public String getName() {
        return name;
    }

    /**
     * EFFECTS: Converts this object into a JSON object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        return json;
    }

}
