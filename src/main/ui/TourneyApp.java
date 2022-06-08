package ui;

import model.EventLog;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * I just read the documentation
 * https://docs.oracle.com/javase/tutorial/uiswing/components/index.html
 * THIS WAS CREATED ON MY HOME PC THAT USES A 2K MONITOR
 * I cannot test my application on a laptop because my laptop/tablet is really weird
 * (it uses a bigger resolution on a small screen, so everything is scaled weirdly)
 * I cannot guarantee that the look will be the same on a laptop ):
 */
public class TourneyApp extends JFrame {
    private static final int HEIGHT = 800;
    private static final int WIDTH = 1200;
    private TournyJourney tj;

    //according to the doc
    //i think this is the root pane
    //or top-level pane

    /**
     * EFFECTS: Initializes the GUI of the application
     */
    TourneyApp() {
        super("Tourney Journey");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocation(WIDTH / 2, HEIGHT / 2);

        //referenced https://kodejava.org/how-do-i-handle-a-window-closing-event/
        //to find out how to handle window closing events
        //essentially I didn't know the method to use
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                for (model.Event event : EventLog.getInstance()) {
                    System.out.println(event.getDate() + "\n" + event.getDescription() + "\n");
                }
                System.exit(0);
            }
        });

        tj = new TournyJourney(this);
        setVisible(true);

    }
}
