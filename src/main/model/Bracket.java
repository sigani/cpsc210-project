package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;

//This is the class that will hold all the Match objects
//The UI will mainly interact with this class
//Responsible for creating and managing the tournament bracket
public class Bracket implements Writeable {
    String nameOfBracket;
    //This is initialized once the players are added into the tournament
    int heightOfTree;
    //needs to be linkedlist so i can pop lol
    LinkedList<Player> players;
    //this holds the actual bracket
    Match[] bracket;
    //this is the winner of the bracket
    Player winner;
    boolean tournamentStarted;

    /**
     * EFFECTS: creates a tournament without a name
     */
    public Bracket() {
        nameOfBracket = null;
        players = new LinkedList<>();
        heightOfTree = 0;
        winner = null;
        tournamentStarted = false;
    }

    /**
     * Creates a tournament with a given name
     *
     * @param name name of the tournament
     */
    public Bracket(String name) {
        nameOfBracket = name;
        players = new LinkedList<>();
        heightOfTree = 0;
        winner = null;
        tournamentStarted = false;
        EventLog.getInstance().logEvent(new Event("Tournament named " + name + " has been created"));
    }

    //a single elim bracket is a binary tree with height of ceiling(log(2)n), where n is number of matches
    //So, basically, (2^h) - 1 is space needed for a tournament
    //(h = height of tree)
    //h is found through a function below

    /**
     * EFFECTS: returns number of players if the tournament has nnot started (tournamentStarted == false)
     * else, will return 0 (because the players will be put into the bracket array)
     */
    public int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * EFFECTS: returns number of matches if the tournament has started (tournamentStarted == true)
     * else, will return 0 (because no matches as tournament hasn't started)
     */
    public int getNumberOfMatches() {
        if (bracket != null) {
            return bracket.length;
        }
        return 0;
    }

    /**
     * EFFECTS: returns true if tournament has started, false otherwise
     */
    public boolean hasTournamentStarted() {
        return tournamentStarted;
    }

    /**
     * EFFECTS: returns the bracket
     */
    public Match[] getBracket() {
        return bracket;
    }

    /**
     * EFFECTS: returns name of tournament
     */
    public String getNameOfBracket() {
        return nameOfBracket;
    }

    /**
     * EFFECTS: returns a linked list of players
     */
    public LinkedList<Player> getPlayers() {
        return players;
    }

    /**
     * EFFECTS: returns the name of the winner if they exist
     * otherwise, returns the string "n/a"
     */
    public String getWinnerName() {
        if (winner != null) {
            return winner.getName();
        }
        return "n/a";
    }

    /**
     * EFFECTS: returns the height of the bracket
     *
     * @param numPlayers used to calculate the height
     * @return The ceiling of Log_2(numPlayers)
     */
    public int calculateHeightOfBracket(int numPlayers) {
        //need to first change of base
        //then ceiling it, or else casting it right away will be a floor!!!! D:
        //idk why ceiling keeps it a double it literally turns it into an integer lol
        return (int) Math.ceil((Math.log10(numPlayers) / Math.log10(2)));
    }

    /**
     * //EFFECTS: inserts a player into the linkedlist of players
     * //  (but not into the bracket!!!)
     *
     * @param player will be inserted into the tournament
     * @return true if player is successfully inserted
     * false otherwise (perhaps already in the tournament)
     */
    public boolean insertPlayer(Player player) {
        boolean success = true;
        //check if the user is already in the array of players/waiting list
        for (Player value : players) {
            if (value.getName().equals(player.getName())) {
                success = false;
                break;
            }
        }
        //if this is true, then the user isn't in the tournament
        if (success) {
            players.add(player);
            EventLog.getInstance().logEvent(new model.Event("Added " + player.getName()
                    + " to the tournament."));
        }
        //for the creation
        heightOfTree = calculateHeightOfBracket(players.size());
        return success;
    }

    /**
     * EFFECTS: Removes a player from the 'waiting' queue/players linked list, if they exist
     *
     * @param name to be removed from the tournament, if they exist
     * @return true if player successfully removed
     * false otherwise (perhaps they don't exist)
     */
    public boolean removePlayer(String name) {
        boolean success = false;
        //check if the user is in the players list/'waiting' queue
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getName().equals(name)) {
                players.remove(players.get(i));
                //removing player, could change height of tree
                heightOfTree = calculateHeightOfBracket(players.size());
                success = true;
                EventLog.getInstance().logEvent(new model.Event("Removed " + players.get(i).getName()
                        + " from the tournament."));
            }
        }
        return success;
    }

    /**
     * MODIFIES: this
     * EFFECTS: initializes the bracket,
     * popping every player from the linkedlist into the array
     *
     * @return true if successfully starts the tournament
     * false otherwise (less than 2 players in the tournament)
     */
    public boolean startSingleEliminationTournament() {
        //if 0 or 1 players, no competitions lol
        //this technically (should?) works with 2, but its just going to be an array with 1 object in it
        if (players.size() > 1) {
            //this is for the length of the array
            int numberOfMatches = ((int) Math.pow(2, heightOfTree) - 1);
            //the bracket which holds the tournament lol
            bracket = new Match[numberOfMatches];

            //i initially had (int) Math.pow(2, heightOfTree - 1) as a variable
            //but it was 26 lines so i had to remove it lol sorry
            //the 'variable' was basically for the first set of matches to exist
            for (int i = numberOfMatches - 1; i >= (int) Math.pow(2, heightOfTree - 1) - 1; i--) {
                addMatchWithPlayersInIt(i);
            }

            //initialize rest of the bracket
            //again, the variable was supposed to represent the first set of matches
            for (int i = ((int) Math.pow(2, heightOfTree - 1) - 1) - 1; i >= 0; i--) {
                bracket[i] = new Match();
            }
            //tournament has started
            tournamentStarted = true;
        }
        EventLog.getInstance().logEvent(new Event("Tournament named " + nameOfBracket + " has "
                + "created its bracket."));
        return tournamentStarted;

    }

    /**
     * Adds a match to the tournament
     * @param i the index the match will be entered in
     */
    private void addMatchWithPlayersInIt(int i) {
        Match match = new Match();

        //these are necessary if the number of players isn't some sort of 2^n number
        if (players.size() > 0) {
            match.setPlayer1(players.pop());
        } else {
            match.setPlayer1(new Player()); //a bye
        }

        if (players.size() > 0) {
            match.setPlayer2(players.pop());
        } else {
            match.setPlayer2(new Player()); //a bye
        }

        bracket[i] = match;
        EventLog.getInstance().logEvent(new Event("Added match to the bracket, containing "
                + "players " + match.getPlayer1Name() + " and " + match.getPlayer2Name()));
    }

    /**
     * MODIFIES: this
     * EFFECTS: Advances the player with the name given into the next match
     * if the name given belongs to no players in the array, won't do anything
     *
     * @param name will be moved to the next "match" in the tournament
     *             if they are already at the last match, the winner variable will be set
     *             to this name
     * @return true if successfully advances/announces a winner
     * false otherwise (if they don't exist)
     */
    //the parent match is found with
    //p = ((c-1)/2), where c is child and p is parent
    public boolean reportWinnerNoMatchNumber(String name) {
        boolean success = false;
        //height of tree (log_2(n)) rounded up, n = number of players
        //there are n/2 set of matches in the first round
        //which is then /2 afterwards until 1
        //which is just 2^n - 1 matches then
        //e.g 8 players = 2^3 players
        //4 matches + 2 matches + 1 match = (2^3)-1
        int numberOfMatches = ((int) Math.pow(2, heightOfTree) - 1);

        //there shouldn't be a problem with iterating from the start of array
        //to find the dude
        //because once dude is found, it will be the 'latest' match
        //so, we just advance them to next match
        //and then break the loop so it doesn't ruin anything else
        for (int i = 0; i < numberOfMatches; i++) {
            //if the match contains the person reporting their win,
            if (bracket[i].containsPlayer(name)) {
                //this is just saying if we are at the top of the bracket i.e the final match
                if (i == 0) {
                    //then set the winner of the bracket to the winner of that match
                    winner = bracket[i].getPlayerWithName(name);
                } else {
                    //otherwise, we are putting the winner of the match into it's parent
                    Player winner = bracket[i].getPlayerWithName(name);
                    int parent = ((i - 1) / 2);
                    bracket[parent].addPlayerToMatch(winner);
                }
                success = true;
                //if I implement addPlayerToMatch differently, I prob wont need break
                //but once I find the dude, I don't need to iterate through the array anyway
                break;
            }
        }
        return success;
    }

    /**
     * EFFECTS: Announces the winner of the tournament, if they exist
     */
    public boolean announceWinner() {
        boolean result = false;
        if (winner != null) {
            result = true;
            EventLog.getInstance().logEvent(new Event(getWinnerName() + " has won the tournament!"));
        }
        return result;
    }


    /**
     * REQUIRES: The tournament to have started
     * EFFECTS: Prints each match sequentially from 0
     */
    public void printBracket() {
        System.out.println("-------------");
        for (int i = 0; i < bracket.length; i++) {
            String player1Name = bracket[i].getPlayer1Name();
            String player2Name = bracket[i].getPlayer2Name();
            System.out.println("[Match " + i + ": " + player1Name + " vs " + player2Name + "]");
        }
        System.out.println("-------------");
    }

    /**
     * REQUIRES: The tournament to have not started
     * EFFECTS: Prints each player 'waiting' to participate in the tournament
     */
    public void printPlayers() {
        System.out.println("-------------");
        for (Player player : players) {
            System.out.println(player.getName());
        }
        if (players.size() == 0) {
            System.out.println("No players yet");
        }
        System.out.println("-------------");
    }

    /**
     * EFFECTS: Forces a creation of an empty bracket
     * This is strictly for reading/writing
     */
    public void manualCreateEmptyBracket(int height) {
        heightOfTree = height;
        bracket = new Match[(int) Math.pow(2, heightOfTree) - 1];
    }

    /**
     * EFFECTS: Forces an insertion of matches into the bracket
     * This is strictly for reading/writing
     */
    public void manualInsertIntoBracket(Match m) {
        //I SHOULDVE MADE IT AN ARRAYLIST OMG
        for (int i = 0; i < bracket.length; i++) {
            if (bracket[i] == null) {
                bracket[i] = m;
                break; //break or else whole bracket will be the same match lol
            }
        }
    }

    /**
     * EFFECTS: Forcefully sets the tournamentStarted to bool
     * This is strictly for reading/writing
     */
    public void manualSetTournamentStarted(Boolean bool) {
        tournamentStarted = bool;
    }

    /**
     * EFFECTS: Converts this object into a JSON object
     */
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", nameOfBracket);

        //either one of these will be empty, depending on if they started the tournament or not
        json.put("players", playersToJsonArray());
        json.put("bracket", bracketToJsonArray());

        json.put("heightTree", heightOfTree);
        json.put("winner", winner);
        json.put("tournamentStarted", tournamentStarted);
        return json;
    }

    /**
     * EFFECTS: Helper function for toJson(), converts the player arraylist into a jsonarray
     */
    private JSONArray playersToJsonArray() {
        JSONArray ja = new JSONArray();
        if (players.size() >= 1) {
            for (Player p : players) {
                ja.put(p.toJson());
            }
        }
        return ja;
    }

    /**
     * EFFECTS: Helper function for toJson(), converts the bracket array into a jsonarray
     */
    private JSONArray bracketToJsonArray() {
        JSONArray ja = new JSONArray();
        if (bracket != null) {
            for (Match m : bracket) {
                ja.put(m.toJson());
            }
        }
        return ja;
    }
}