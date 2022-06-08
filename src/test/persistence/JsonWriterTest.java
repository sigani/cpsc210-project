package persistence;

import model.Bracket;
import model.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// * Referenced from the JSON example provided in Phase 2 from EDX
// link to repo: https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public class JsonWriterTest {
    //note in the demo repo says to write then read to see if it wrote lol

    @Test
    public void testWriterInvalidFile() {
        try {
            Bracket b = new Bracket("test");
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyBracket() {
        try {
            Bracket b = new Bracket("emptyWrite");
            String destination = "./data/" + b.getNameOfBracket() + ".json";
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(b);
            writer.close();

            JsonReader jsonReader = new JsonReader(destination);
            b = jsonReader.read();
            assertEquals(b.getNameOfBracket(), "emptyWrite");
            assertEquals(0, b.getNumberOfMatches());
            assertEquals(0, b.getNumberOfPlayers());

        } catch (Exception e) {
            fail("No exception should be thrown!!");
        }
    }

    @Test
    public void testWriterBracketNotStartedYet() {
        try {
            Bracket b = new Bracket("tourneyNoStart");
            b.insertPlayer(new Player("a"));
            b.insertPlayer(new Player("b"));
            b.insertPlayer(new Player("c"));
            b.insertPlayer(new Player("d"));

            String destination = "./data/" + b.getNameOfBracket() + ".json";
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(b);
            writer.close();

            JsonReader jsonReader = new JsonReader(destination);
            b = jsonReader.read();
            assertEquals(b.getNameOfBracket(), "tourneyNoStart");
            assertEquals(0, b.getNumberOfMatches());
            assertEquals(4, b.getNumberOfPlayers());

        } catch (Exception e) {
            fail("No exception should be thrown!!");
        }
    }

    @Test
    public void testWriterBracketNotStartedYetAnotherWay() {
        try {
            Bracket b = new Bracket("tourneyNoStart2");
            b.insertPlayer(new Player("a"));
            b.insertPlayer(new Player("b"));
            b.insertPlayer(new Player("c"));
            b.insertPlayer(new Player("d"));

            String destination = "./data/" + b.getNameOfBracket() + ".json";
            JsonWriter writer = new JsonWriter();
            writer.setDestination(destination);
            writer.open();
            writer.write(b);
            writer.close();

            JsonReader jsonReader = new JsonReader(destination);
            b = jsonReader.read();
            assertEquals(b.getNameOfBracket(), "tourneyNoStart2");
            assertEquals(0, b.getNumberOfMatches());
            assertEquals(4, b.getNumberOfPlayers());

        } catch (Exception e) {
            fail("No exception should be thrown!!");
        }
    }

    @Test
    public void testWriterBracketHasStarted() {
        try {
            //like above, but we started the tournament
            Bracket b = new Bracket("tourneyNoStart");
            b.insertPlayer(new Player("a"));
            b.insertPlayer(new Player("b"));
            b.insertPlayer(new Player("c"));
            b.insertPlayer(new Player("d"));
            b.startSingleEliminationTournament();

            String destination = "./data/" + b.getNameOfBracket() + ".json";
            JsonWriter writer = new JsonWriter(destination);
            writer.open();
            writer.write(b);
            writer.close();

            JsonReader jsonReader = new JsonReader(destination);
            b = jsonReader.read();
            assertEquals(b.getNameOfBracket(), "tourneyNoStart");
            assertEquals(3, b.getNumberOfMatches());
            assertEquals(0, b.getNumberOfPlayers());

        } catch (Exception e) {
            fail("No exception should be thrown!!");
        }
    }
}
