package persistence;

import model.Bracket;
import model.Match;
import model.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Class that loads a bracket
 * Referenced from the JSON example provided in Phase 2 from EDX
 * Link to repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonReader {
    private String source;

    public JsonReader(String source) {
        this.source = source;
    }

    /**
     * EFFECTS: Reads a json file given a path destination and returns a bracket object
     *      THROWS IOException if destination is invalid
     */
    public Bracket read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseBracket(jsonObject);
    }

    /**
     * EFFECTS: helper function for read(), parses the bracket object from JSON file and returns it
     */
    private Bracket parseBracket(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Bracket b = new Bracket(name);
        boolean bool = jsonObject.getBoolean("tournamentStarted");
        b.manualSetTournamentStarted(bool);
        addDudes(b, jsonObject); //add the arrays
        return b;
    }

    /**
     * EFFECTS: helper function for the helper functions
     *      Parses either array into the bracket object
     */
    private void addDudes(Bracket b, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("players");
        //EITHER ONE OF THESE FOR LOOPS WILL RUN
        //DEPENDING ON IF THE MATCH HAS STARTED YET

        //THIS IS TO ADD PLAYERS INTO THE PLAYER ARRAYLIST
        for (Object json : jsonArray) {
            JSONObject nextPlayer = (JSONObject) json;
            addDudeToPlayerArray(b, nextPlayer);
        }

        //THIS IS TO ADD MATCHES TO THE MATCH ARRAY
        jsonArray = jsonObject.getJSONArray("bracket");
        if (!jsonArray.isEmpty()) {
            int height = jsonObject.getInt("heightTree");
            b.manualCreateEmptyBracket(height);
            for (Object json : jsonArray) {
                JSONObject nextMatch = (JSONObject) json;
                addBracketToBracketArray(b, nextMatch);
            }
        }

    }

    /**
     * EFFECTS: Helper function for the helper function for the helper function
     *      Adds a match to the bracket array in the bracket object
     */
    private void addBracketToBracketArray(Bracket b, JSONObject jsonObject) {
        String player1 = jsonObject.getString("player1");
        String player2 = jsonObject.getString("player2");
        Match match = new Match(player1, player2);
        b.manualInsertIntoBracket(match);
    }

    /**
     * EFFECTS: Helper function for the helper function for the helper function
     *      Adds a player to the player arraylist in the bracket object
     */
    private void addDudeToPlayerArray(Bracket b, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Player player = new Player(name);
        b.insertPlayer(player);
    }

    /**
     * EFFECTS: Helper function for read()
     *      reads the file given
     */
    private String readFile(String source) throws IOException {
        StringBuilder content = new StringBuilder(); //ig this is just string with funky methods

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> content.append(s));
        }

        return content.toString();
    }


}
