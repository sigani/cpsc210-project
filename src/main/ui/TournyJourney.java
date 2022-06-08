package ui;

import model.Bracket;
import model.Match;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The UI for the application
 * This class will create components to add to the JFrame or root pane
 * Implements ActionListener to handle events (buttons)
 */
public class TournyJourney implements ActionListener {
    Bracket bracket; //the actual tournament thingy

    private final TourneyApp rootPane; //reference to jframe
    private JPanel optionArea; //reference to options panel
    private JPanel logisticArea; //reference to the information panel
    private JPanel bracketPanel; //reference to the bracket panel that shows the matches

    //persistence stuff
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String destination;

    /**
     * MODIFIES: this
     * EFFECTS: Initializes the panels of the frame
     * Which is then displayed on the rootPane
     */
    public TournyJourney(TourneyApp app) {
        rootPane = app; //reference to jframe
        init();
    }

    /**
     * MODIFIES: this
     * EFFECTS: initializes the application and starts the GUI
     */
    private void init() {
        bracket = null;
        jsonWriter = new JsonWriter();
        logisticArea = new JPanel();
        bracketPanel = new JPanel();

        createUserInteractions();
        createLogisticInformation();
        paintBracket();
    }

    /**
     * MODIFIES: this
     * EFFECTS: displays the information about the tournament ie number of players and matches.
     */
    private void createLogisticInformation() {
        //update
        rootPane.getContentPane().remove(logisticArea);
        rootPane.revalidate();

        logisticArea = new JPanel();
        JTextArea text = new JTextArea();
        //this is what we are putting in the top right corner
        if (bracket != null) {
            text.append("Name of tournament:\n");
            text.append(" " + bracket.getNameOfBracket() + "\n");
            text.append("Players:\n");
            for (Player p : bracket.getPlayers()) {
                text.append(" " + p.getName() + "\n");
            }
        }
        logisticArea.add(text);
        text.setEditable(false);
        //update
        rootPane.getContentPane().add(logisticArea, BorderLayout.EAST);
        rootPane.revalidate();

    }

    /**
     * MODIFIES: this
     * EFFECTS: creates the area that has all the buttons
     */
    private void createUserInteractions() {
        String tooltipMsg; //so my lines don't exceed 120 characters lol
        //when creating/loading tournament for first time, this is needed lmao
        if (optionArea != null) {
            rootPane.getContentPane().remove(optionArea);
            rootPane.getContentPane().revalidate();
        }

        //the panel that will hold the buttons
        optionArea = new JPanel();
        optionArea.setLayout(new GridLayout(0, 1));
        optionArea.setSize(new Dimension(0, 0));

        //omg it worked
        //created helper functions to reduce lines from 48 to <25 lol
        createButtonForPanel(optionArea, "Create new Tournament", "createTournament",
                "Creates a new Tournament.  This will overwrite the current Tournament in progress");

        createButtonForPanel(optionArea, "Load a Tournament", "loadTournament",
                "Loads a tournament.  This will override the current Tournament in progress");

        if (bracket != null) {
            createButtonForPanel(optionArea, "Save Tournament", "saveTournament", "Saves the tournament");

            createButtonForPanel(optionArea, "Insert player into tournament", "insertPlayer",
                    "Inserts a player into the tournament.  This cannot be done if the tournament has started");

            createButtonForPanel(optionArea, "Remove player from tournament", "removePlayer",
                    "Removes a player from the tournament.  This cannot be done if the tournament has started");

            createButtonForPanel(optionArea, "Start the tournament", "startTournament",
                    "Starts the tournament, creating a single elimination bracket.");

        }

        //i assume it adds stuff to the panel
        rootPane.getContentPane().add(optionArea, BorderLayout.WEST);
        //this needs to be called when changing things in the frame if it is already initialized
        rootPane.revalidate();

    }

    /**
     * EFFECTS, adds a button to the given panel
     *
     * @param panel   The panel the button will be on
     * @param name    The name of the button that will be displayed
     * @param cmd     The action command the button will preform when pressed
     * @param toolTip The tooltip when hovering mouse over
     */
    private void createButtonForPanel(JPanel panel, String name, String cmd, String toolTip) {
        JButton b = new JButton(name);
        b.setActionCommand(cmd);
        b.setToolTipText(toolTip);
        b.addActionListener(this);
        panel.add(b);
    }

    /**
     * EFFECTS: Depending on the button pressed, will call another helper function
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //this is basically the processCommand method I made for the console ui
        //but no console woohoo
        switch (e.getActionCommand()) {
            case "createTournament":
                createTournament();
                break;
            case "loadTournament":
                loadTournament();
                break;
            case "saveTournament":
                saveTournament();
                break;
            case "insertPlayer":
                addPlayer();
                break;
            case "removePlayer":
                removePlayer();
                break;
            case "startTournament":
                startTournament();
                break;
            default:
                //default case will never be called lol
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: Creates a new tournament
     */
    private void createTournament() {
        String name = JOptionPane.showInputDialog(rootPane, "What is the name of the tournament?");
        if (name != null && name.length() > 0) {
            bracket = new Bracket(name);
            destination = "./data/" + bracket.getNameOfBracket() + ".json";
            createUserInteractions();
            createLogisticInformation();
            paintBracket(); //in case there is already an in progress tournament, and we make a new one
        } else {
            String errorMsg = "Name cannot be empty!";
            JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }

        //else, do nothing, if they cancel the window or smth

    }

    /**
     * MODIFIES: this
     * EFFECTS: loads a tournament, if there is one saved
     */
    //i referenced https://stackoverflow.com/questions/3154488/how-do-i-iterate-through-the-files-in-a-directory-in-java
    //for iterating through a directory
    //i just changed it so i can find a file given a string instead of a file itself
    //because im asking user for a string
    private void loadTournament() {
        try {
            String name = JOptionPane.showInputDialog(rootPane, "What is the name of the tournament?");
            destination = "./data/" + name + ".json";
            jsonReader = new JsonReader(destination);
            bracket = jsonReader.read();
            createUserInteractions();
            createLogisticInformation();
            paintBracket();
        } catch (IOException e) {
            String errorMsg = "Can't find tournament, or you forgot to put a name!";
            JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * EFFECTS: saves a tournament
     */
    private void saveTournament() {
        try {
            jsonWriter.setDestination(destination);
            jsonWriter.open();
            jsonWriter.write(bracket);
            JOptionPane.showMessageDialog(rootPane, "Saved tournament named " + bracket.getNameOfBracket() + "!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(rootPane, "Something went wrong");
        } finally {
            jsonWriter.close(); //should always close right!!!!
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: Adds a player into the bracket if both the tournament hasn't started yet
     * AND the player isn't already in the bracket
     * OTHERWISE, does nothing/outputs an error message
     */
    public void addPlayer() {
        //first check if the tournament has started
        if (!bracket.hasTournamentStarted()) {
            //get name of player wanting to add
            String name = JOptionPane.showInputDialog(rootPane, "What is the name of the user?");
            if (name != null && !name.equals("") && bracket.insertPlayer(new Player(name))) {
                //update
                createLogisticInformation();
            } else if (name == null || name.equals("")) {
                //didn't work
                String errorMsg = "Name cannot be empty!";
                JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                //didn't work as well
                //insert player checks to see if a player is already in the tournament or not
                String errorMsg = "Error: player already exists!";
                JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else /*if it has started, don't insert another player*/ {
            String errorMsg = "Error: Tournament already started!";
            JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * MODIFIES: this
     * EFFECTS: Removes a player from the bracket if both the tournament hasn't started yet
     * AND the player exists in the bracket
     * OTHERWISE, does nothing/outputs an error message
     */
    private void removePlayer() {
        //first check if the tournament has started
        if (!bracket.hasTournamentStarted()) {
            //get input from user
            String name = JOptionPane.showInputDialog(rootPane, "What is the name of the user?");
            if (bracket.removePlayer(name)) {
                //update
                createLogisticInformation();
                JOptionPane.showMessageDialog(rootPane, "Player removed successfully");
            } else {
                //didn't work
                String errorMsg = "Error: player does not exist!";
                JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else /*don't remove players from a tournament already started!!!*/ {
            String errorMsg = "Error: Tournament already started!";
            JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: "Starts" the tournament IF there are enough players (at least 2)
     * note: this technically works with 2, but it would just be a single match...
     */
    private void startTournament() {
        //check if the tournament has started already
        if (!bracket.hasTournamentStarted()) {
            //basically tells user if the bracket creation was successful or not
            if (bracket.startSingleEliminationTournament()) {
                paintBracket(); //paint the bracket
            } else /*only way this happens if there are <2 players*/ {
                String errorMsg = "Error: Not enough players!";
                JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else /*can't start a tournament that already started lol*/ {
            String errorMsg = "Error: Tournament already started!";
            JOptionPane.showMessageDialog(rootPane, errorMsg, "Error", JOptionPane.ERROR_MESSAGE);
        }
        //update
        rootPane.getContentPane().revalidate();

    }

    /**
     * MODIFIES: this
     * EFFECTS: updates and displays a visual representation of the tournament bracket
     */
    private void paintBracket() {
        //update stuff
        rootPane.getContentPane().remove(bracketPanel);
        rootPane.revalidate();

        //this will hold all the matches and display it as a tournament single elim tree
        bracketPanel = new JPanel();
        if (bracket != null && bracket.hasTournamentStarted()) {
            //bruh this holds all the row of matches
            bracketPanel.setLayout(new BoxLayout(bracketPanel, BoxLayout.Y_AXIS));
            //create the tree essentially
            paintTheRounds();
        } else {
            //call this if the tournament hasn't started yet
            //which just adds some picture
            addSplashArtInstead();
        }
        //add panel to frame and update
        rootPane.getContentPane().add(bracketPanel, BorderLayout.CENTER);
        rootPane.getContentPane().revalidate();
    }

    /**
     * MODIFIES: this
     * EFFECTS: paint each round of the tournament bracket
     */
    private void paintTheRounds() {

        //create a panel to hold all match panels
        JPanel rowOfMatches = new JPanel();

        //represents a match panel
        JPanel matchPanel;

        //index of the imaginary rowNumber 'array'
        int i = 1;
        //the rowNumber represents the number of matches that belong in each row
        int rowNumber = 1;
        ArrayList<String> playersInBracketAlready = new ArrayList<>();
        //for each match in the bracket, attach it to panel

        for (Match m : bracket.getBracket()) {
            boolean enableButton = true;

            //This represents the match
            //i.e, the border and the text area
            matchPanel = new JPanel();
            matchPanel.setBorder(BorderFactory.createLineBorder(Color.black));

            //this is adding the names of players to the matchPanel
            JTextArea text = new JTextArea(2, 15);
            text.setEditable(false);
            text.append(m.getPlayer1Name() + "\n" + m.getPlayer2Name());
            matchPanel.add(text);
            //this is for ensuring the button for reporting the winner is disabled or not
            //so basically is disabled if the player is already somewhere ahead of the tournament than the start
            paintAMatchForRound(rowOfMatches, matchPanel, playersInBracketAlready, m, enableButton);

            //if the row is 'full'
            if (i == rowNumber) {
                //add the row of matches to the rootPane
                bracketPanel.add(rowOfMatches);

                //make a new row of matches, and hope it'll make a new line lol
                rowOfMatches = new JPanel();
                rowNumber += rowNumber;
                i = 1;
            } else {
                //next match
                i++;
            }
        }

    }

    /**
     * MODIFIES: this
     * EFFECTS: adds a match to a specific round
     *
     * @param row     the row panel
     * @param match   the match panel
     * @param bracket the array of players that are already in the tournament bracket
     * @param m       the match to add
     * @param enable  determine whether the button attached to each match is enabled or not
     */
    private void paintAMatchForRound(JPanel row, JPanel match, ArrayList<String> bracket, Match m, boolean enable) {
        if (!bracket.contains(m.getPlayer1Name())) {
            bracket.add(m.getPlayer1Name());
        } else {
            if (!m.getPlayer1Name().equals("Bye")) {
                enable = false;
            }
        }
        if (!bracket.contains(m.getPlayer2Name())) {
            bracket.add(m.getPlayer2Name());
        } else {
            if (!m.getPlayer2Name().equals("Bye")) {
                enable = false;
            }
        }
        //special cases!!!
        //disabled for cases where there are no players in the match
        if (m.getPlayer1Name().equals("Bye") && m.getPlayer2Name().equals("Bye")) {
            enable = false;
        }

        //this is for reporting a winner
        String[] players = {m.getPlayer1Name(), m.getPlayer2Name(), "Cancel"};

        //attach a button to the match panel
        attachAButtonToMatch(match, enable, players);

        //this then add the matchPanel to the rowOfMatches
        row.add(match);
    }

    /**
     * MODIFIES: this
     * EFFECTS: attaches a button to the match panel
     *
     * @param match   the match panel the button will be on
     * @param enable  if this is true, the button will be enabled.  False otherwise
     * @param players when the button is pressed, the button will display the two players from this array
     */
    private void attachAButtonToMatch(JPanel match, boolean enable, String[] players) {
        //add a button to matchPanel
        JButton button = new JButton("W");
        button.setEnabled(enable);

        //this button will declare a winner
        button.addActionListener(e -> {
            int winnerNumber = JOptionPane.showOptionDialog(rootPane, "Who won?", "Winner",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, players, players[2]);
            String winner = players[winnerNumber];
            reportWinner(winner);
        });
        match.add(button);
    }

    /**
     * Tries to add a random splashart to the panel.
     */
    private void addSplashArtInstead() {
        //bracket is not initialize, put some image lol
        //image from https://thyswyldelyfe.blogspot.com/2019/06/jpg-jokes-for-computer-science-students.html
        //i got help from https://stackoverflow.com/questions/299495/how-to-add-an-image-to-a-jpanel
        //because the doc didn't have anything about images
        try {
            addSomeSplashArt();
        } catch (Exception e) {
            addDoggyInstead();
        }
    }

    /**
     * EFFECTS: Adds a random splashart to the panel
     *
     * @throws IOException if the file doesn't exist or the path is incorrect
     */
    private void addSomeSplashArt() throws IOException {
        String pathname;
        //50/50 why not lol
        //either a doggy or a joke!!!
        if ((int) (Math.random() * 2) % 2 == 0) {
            pathname = "./data/jokeimagelol.jpg";
        } else {
            pathname = "./data/tobs.jpg";
        }
        BufferedImage splashart = ImageIO.read(new File(pathname));
        JLabel displayPicture = new JLabel(new ImageIcon(splashart));
        bracketPanel.add(displayPicture);
    }

    /**
     * EFFECTS: Adds a doggy picture to the panel
     * This is only called if the first picture doesn't exist
     * Since the project came with the photo, it should not fail
     * However, it will still do something if the photo disappears
     */
    private void addDoggyInstead() {
        JTextArea errorText = new JTextArea();
        errorText.append("IF YOU SEE THIS YOU FORGOT TO PUSH THE IMAGES!!!!!!!");
        bracketPanel.add(errorText);
        try {
            //just put the doggy this file started with
            String pathname = "./data/tobs.jpg";
            BufferedImage splashart = ImageIO.read(new File(pathname));
            JLabel displayPicture = new JLabel(new ImageIcon(splashart));
            bracketPanel.add(displayPicture);
        } catch (Exception e1) {
            JTextArea errorText2 = new JTextArea();
            errorText.append("IF YOU SEE THIS AS WELL SOMEHOW THE STARTER DOGGY DISAPPEARED!!!!");
            bracketPanel.add(errorText2);
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: "Advances" a player into their next match, if both the tournament already started
     * AND the player exists
     * IF the player is in the last match (i.e in the 0 position of the arraylist of matches),
     * then, the winner of the tournament will be set to the player inputted instead of advancing the player
     * (because there is no match to advance them into), and will be announced in a window.
     * OTHERWISE, does nothing/outputs an error message
     * this can no longer output an error message the way it is called.
     */
    private void reportWinner(String name) {
        //if we pass "Cancel", then the user cancelled the winner prompt
        //ok it can't actually be cancel because some persons name could be Cancel lol
        //also they cannot report Bye as a winner
        if (name.equals("")) {
            return;
        } else if (name.equals("Bye")) {
            JOptionPane.showMessageDialog(rootPane, "You cannot advance a Bye as a winner!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        //tournament must have started in order to report a winner of a match
        if (bracket.hasTournamentStarted()) {
            //get input
            //String name = input.next();
            //see if they exist
            if (bracket.reportWinnerNoMatchNumber(name)) {
                //repaint bracket
                paintBracket();
                //if this is called on the final match, then we announce the winner
                if (bracket.announceWinner()) {
                    JOptionPane.showMessageDialog(rootPane, bracket.getWinnerName() + " has won the tournament!",
                            "Winner!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }

}
