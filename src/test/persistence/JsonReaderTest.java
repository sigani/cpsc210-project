package persistence;

import model.Bracket;
import org.junit.jupiter.api.Test;


import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// * Referenced from the JSON example provided in Phase 2 from EDX
// link to repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonReaderTest {
    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/aiuwdyajhksbdgawjhdg.json");
        try {
            Bracket b = reader.read();
            fail("IOException was supposed to be thrown!!!!!!");
        } catch (IOException e) {
            //woohoo
        }
    }

    @Test
    public void testReaderEmptyBracket() {
        JsonReader reader = new JsonReader("./data/empty.json");
        try {
            Bracket b = reader.read();
            assertEquals("empty", b.getNameOfBracket());
            assertEquals(0, b.getNumberOfMatches());
            assertEquals(0, b.getNumberOfPlayers());
        } catch (IOException e) {
            fail("Couldn't read file ):");
        }
    }

    @Test
    public void testReaderTournamentStartedCase() {
        JsonReader reader = new JsonReader("./data/please.json");
        try {
            Bracket b = reader.read();
            assertEquals("please", b.getNameOfBracket());
            assertEquals(3, b.getNumberOfMatches());
            assertEquals(0, b.getNumberOfPlayers());
        } catch (IOException e) {
            fail("Couldn't read file ):");
        }
    }

    @Test
    public void testReaderTournamentHasNotStartedCase() {
        JsonReader reader = new JsonReader("./data/tree.json");
        try {
            Bracket b = reader.read();
            assertEquals("tree", b.getNameOfBracket());
            assertEquals(0, b.getNumberOfMatches());
            assertEquals(4, b.getNumberOfPlayers());
        } catch (IOException e) {
            fail("Couldn't read file ):");
        }
    }

}
