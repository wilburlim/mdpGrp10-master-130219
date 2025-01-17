package mdp;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

import mdp.communication.Translator;
import mdp.robot.Robot;
import mdp.communication.ITranslatable;
import mdp.simulation.GUI;
import mdp.simulation.event.GUIClickEvent;
import mdp.simulation.IGUIUpdatable;
import mdp.solver.exploration.ActionFormulator;

public class Main {

    private static IGUIUpdatable _gui;
    private static ITranslatable _rpi;

    private static boolean _isSimulating = false;

    public static void main(String[] args) throws IOException {
        // run simulation
        System.out.println("Initiating GUI...");
        startGUI();

        // testing (if needed)
//        connectToRpi();
    }

    public static boolean isSimulating() {
        return _isSimulating;
    }

    public static void isSimulating(boolean isSimulating) {
        _isSimulating = isSimulating;
    }

    public static IGUIUpdatable getGUI() {
        return _gui;
    }

    public static ITranslatable getRpi() {
        return _rpi;
    }

    public static void startGUI() {
        SwingUtilities.invokeLater(() -> {
            _gui = new GUI();
        });
    }

    public static void connectToRpi() throws IOException {
        _rpi = new Translator();
        _rpi.connect(() -> {
            try {
                _listenToRPi();
//                /*Test.run();*/
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private static void _listenToRPi() throws IOException {
        _rpi.listen(() -> {
            String inStr = _rpi.getInputBuffer();
            System.out.println("inStr = " + inStr);
            switch (inStr) {
                // Android start commands
                case "e":
                    System.out.println("Triggering Exploration");
                    _gui.trigger(GUIClickEvent.OnExploration);
                    break;
                case "s":
                    System.out.println("Triggering ShortestPath");
                    _gui.trigger(GUIClickEvent.OnShortestPath);
                    break;
                //new case for startpos to waypoint    
                case "x":
                    System.out.println("Triggering to WayPoint");
                    _gui.trigger(GUIClickEvent.OnToWayPoint);
                    break;
                case "y":
                    System.out.println("Triggering from WayPoint to Goal");
                    _gui.trigger(GUIClickEvent.OnWayPointToGoalPath);
                    break;
                case "c":
                    System.out.println("Triggering Combined");
                    _gui.trigger(GUIClickEvent.OnCombined);
                    break;
                case "D":
                    //Robot.actionCompletedCallBack();
                    ActionFormulator.calibrationCompletedCallBack();
                    break;
                case "a": //a for accuracy , calibration

                    break;
                default:

                    if (inStr.length() == 6) {
                        System.out.println("Analyzing sensing information");
                        ActionFormulator.sensingDataCallback(inStr);
                        Robot.actionCompletedCallBack();
                    } else {
                        System.out.println("Unrecognized input");
                    }
                    break;
            }
        });
    }

}
