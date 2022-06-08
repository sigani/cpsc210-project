package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BracketTest {
    Bracket testBracket;

    @BeforeEach
    public void runBefore() {
        testBracket = new Bracket();
    }

    @Test
    public void testAddingRemovingSizeOfPlayers() {
        testBracket.printPlayers();
        testBracket.insertPlayer(new Player("a"));
        assertEquals(1, testBracket.getNumberOfPlayers());
        testBracket.printPlayers();

        testBracket.insertPlayer(new Player("b"));
        testBracket.insertPlayer(new Player("c"));
        assertEquals(3, testBracket.getNumberOfPlayers());

        //should still be 3, cannot insert duplicate users
        testBracket.insertPlayer(new Player("a"));
        assertEquals(3, testBracket.getNumberOfPlayers());
        testBracket.printPlayers();


        assertTrue(testBracket.removePlayer("a"));
        assertEquals(2, testBracket.getNumberOfPlayers());

        //69 not added, should be false
        assertFalse(testBracket.removePlayer("69"));

    }

    @Test
    public void testCalculateHeightOfTree() {
        //ceil(log_2(1)) should be 0
        testBracket.insertPlayer(new Player("a"));
        assertEquals(0, testBracket.calculateHeightOfBracket(testBracket.getNumberOfPlayers()));

        //ceil(log_2(2)) should be 1
        testBracket.insertPlayer(new Player("b"));
        assertEquals(1, testBracket.calculateHeightOfBracket(testBracket.getNumberOfPlayers()));

        //ceil(log_2(3)) ceiling should be 2
        testBracket.insertPlayer(new Player("c"));
        assertEquals(2, testBracket.calculateHeightOfBracket(testBracket.getNumberOfPlayers()));

        //ceil(log_2(4)) ceiling should still be 2
        testBracket.insertPlayer(new Player("d"));
        assertEquals(2, testBracket.calculateHeightOfBracket(testBracket.getNumberOfPlayers()));
    }

    @Test
    public void testSingleElimBracketCreation8Players() {
        //insert 8 players into bracket
        for(int i = 0; i < 8; i++) {
            testBracket.insertPlayer(new Player(Integer.toString(i)));
        }

        //starts the tournament
        testBracket.startSingleEliminationTournament();
        //size of array should be 7
        assertEquals(7, testBracket.getNumberOfMatches());
        //size of players should be 0 (because in the tournament)
        assertEquals(0, testBracket.getNumberOfPlayers());
    }

    @Test
    public void testSingleElimBracketCreationLess2Players() {
        //cannot start with 0 players in bracket
        assertFalse(testBracket.startSingleEliminationTournament());
        testBracket.insertPlayer(new Player("a"));
        //cannot start with 1 player in the bracket
        assertFalse(testBracket.startSingleEliminationTournament());
    }

    @Test
    public void testSingleEliminationWinnerEvenPlayers() {
        //even means 2^h = n
        //where h = ceil(log_2(n))

        //or, if n is a number that can be represented by 2 to the power of something
        //insert 8 players into bracket
        for(int i = 0; i < 8; i++) {
            testBracket.insertPlayer(new Player(Integer.toString(i)));
        }

        //tournament no started, is false
        assertFalse(testBracket.hasTournamentStarted());
        //starts the tournament
        testBracket.startSingleEliminationTournament();
        //tournament has started, is true
        assertTrue(testBracket.hasTournamentStarted());


        //1 won his match...
        //if 1 won, means his match was the
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.printBracket();

        //this will fail, 69 not in bracket lol
        assertFalse(testBracket.reportWinnerNoMatchNumber("69"));

        assertTrue(testBracket.reportWinnerNoMatchNumber("2"));
        testBracket.printBracket();

        assertTrue(testBracket.reportWinnerNoMatchNumber("4"));
        testBracket.printBracket();

        assertTrue(testBracket.reportWinnerNoMatchNumber("7"));
        testBracket.printBracket();

        //this should not work because tournament hasn't finished
        assertFalse(testBracket.announceWinner());
        assertEquals("n/a", testBracket.getWinnerName());

        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.printBracket();

        assertTrue(testBracket.reportWinnerNoMatchNumber("7"));
        testBracket.printBracket();

        //1 wins the whole tournament,
        //announce the winner
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.announceWinner();

        assertEquals("1", testBracket.getWinnerName());
    }

    @Test
    public void testSingleEliminationWinnerOddPlayers() {
        //even means 2^h = n
        //where h = ceil(log_2(n))
        //so odd is if its not this, 7 in this case

        //or, if n is a number that can be represented by 2 to the power of something
        for(int i = 0; i < 7; i++) {
            testBracket.insertPlayer(new Player(Integer.toString(i)));
        }

        //starts the tournament
        testBracket.startSingleEliminationTournament();
        testBracket.printBracket();

        //1 won his match...
        //if 1 won, means his match was the
        System.out.println("1 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.printBracket();

        System.out.println("2 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("2"));
        testBracket.printBracket();

        System.out.println("4 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("4"));
        testBracket.printBracket();

        System.out.println("6 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("6"));
        testBracket.printBracket();

        //this should not work because tournament hasn't finished
        assertFalse(testBracket.announceWinner());

        System.out.println("1 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.printBracket();

        System.out.println("4 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("4"));
        testBracket.printBracket();

        //1 wins the whole tournament,
        //announce the winner
        System.out.println("1 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        assertTrue(testBracket.announceWinner());
    }

    @Test
    public void testSingleEliminationWinnerOdd2Players() {
        //even means 2^h = n
        //where h = ceil(log_2(n))
        //so odd is if its not this, 6 in this case

        //or, if n is a number that can be represented by 2 to the power of something
        for(int i = 0; i < 6; i++) {
            testBracket.insertPlayer(new Player(Integer.toString(i)));
        }

        //starts the tournament
        testBracket.startSingleEliminationTournament();
        testBracket.printBracket();

        //1 won his match...
        //if 1 won, means his match was the
        System.out.println("1 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.printBracket();

        System.out.println("2 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("2"));
        testBracket.printBracket();

        System.out.println("4 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("4"));
        testBracket.printBracket();

        //this should not work because tournament hasn't finished
        assertFalse(testBracket.announceWinner());

        System.out.println("1 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("1"));
        testBracket.printBracket();

        System.out.println("4 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("4"));
        testBracket.printBracket();

        //4 wins the whole tournament,
        //announce the winner
        System.out.println("4 wins!");
        assertTrue(testBracket.reportWinnerNoMatchNumber("4"));
        assertTrue(testBracket.announceWinner());
    }

}