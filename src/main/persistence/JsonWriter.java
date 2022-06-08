package persistence;

import model.Bracket;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 * Class that saves a bracket
 * Referenced from the JSON example provided in Phase 2 from EDX
 * Link to repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
 */
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter() {
        destination = "";
    }

    public JsonWriter(String name) {
        destination = name;
    }


    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing

    /**
     * MODIFIES: this
     * EFFECTS: opens writer
     *      THROWS FileNotFoundException if file doesn't exist
     */
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    /**
     * EFFECTS: sets the destination
     */
    public void setDestination(String name) {
        destination = name;
    }

    /**
     * MODIFIES: this
     * EFFECTS: closes writer
     */
    public void close() {
        writer.close();
    }

    /**
     * MODIFIES: this
     * EFFECTS: saves to file
     */
    private void saveToFile(String json) {
        writer.print(json);
    }

    /**
     * Writes bracket to json file woohoo
     * @param bracket written to json file
     */
    public void write(Bracket bracket) {
        JSONObject json = bracket.toJson();
        saveToFile(json.toString(TAB));
    }
}
