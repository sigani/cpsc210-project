package model;

import org.json.JSONObject;

//This will hold 2 Player objects (P1 vs P2)
//The bracket class will hold objects of this class
public class Match implements Writeable {
    //fields
    Player player1;
    Player player2;

    /**
     * MODIFIES: this
     * EFFECTS: creates a match object.
     *          without a given name, the name will be "Bye"
     *          to either symbolize the match is empty
     *          Or there isn't enough players for a full tournament
     */
    public Match() {
        player1 = new Player();
        player2 = new Player();
    }

    /**
     * MODIFIES: this
     * EFFECTS: creates a match object with 2 player objects.
     * @param p1 player 1
     * @param p2 player 2
     */
    public Match(String p1, String p2) {
        player1 = new Player(p1);
        player2 = new Player(p2);
        EventLog.getInstance().logEvent(new Event("Created a match object, containing a player named "
                + p1 + " and player named " + p2));

    }


    public void setPlayer1(Player player) {
        player1 = player;
    }

    public void setPlayer2(Player player) {
        player2 = player;
    }

    public String getPlayer1Name() {
        return player1.getName();
    }

    public String getPlayer2Name() {
        return player2.getName();
    }

    /**
     * EFFECTS: Given a string of the name of a player, will return the corresponding player object,
     *          if they exist
     * @param name name of the player
     * @return the player object, or null if the player doesn't exist
     */
    public Player getPlayerWithName(String name) {
        Player returnPlayer = null;
        if (player1.getName().equals(name)) {
            returnPlayer = player1;
        } else if (player2.getName().equals(name)) {
            returnPlayer = player2;
        }
        //the way I call this method, it literally cannot call null
        //because the way I call this, I first check if the player is in this match
        //then I run this method, which is guaranteed to return a player object because I already checked
        //if that player is in the match or not
        //then i basically convert the string to a Player object
        //and with that, move the player up to the next match (or declare them the winner)
        //but the coverage misses this
        //so idk
        return returnPlayer;
    }

    /**
     * EFFECTS: Returns true if a match contains the person with the given name
     * @param name name of the player the caller is looking for
     * @return true if they are in the match, false otherwise
     */
    public boolean containsPlayer(String name) {
        return (player1.getName().equals(name) || player2.getName().equals(name));
    }

    /**
     * MODIFIES: this
     * EFFECTS: Adds a player to a match.
     * @param player player object to add to a match
     */
    public void addPlayerToMatch(Player player) {
        if (player1.getName().equals("Bye")) {
            player1 = player;
            EventLog.getInstance().logEvent(new Event("Added " + player1.getName() + " to their next match"));
        } else {
            player2 = player;
            EventLog.getInstance().logEvent(new Event("Added " + player2.getName() + " to their next match"));
        }

    }

    /**
     * EFFECTS: Converts this object into a JSON object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("player1", player1.getName());
        json.put("player2", player2.getName());
        return json;
    }

}