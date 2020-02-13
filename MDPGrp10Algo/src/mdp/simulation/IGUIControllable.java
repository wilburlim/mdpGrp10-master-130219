package mdp.simulation;

import mdp.map.Map;
import mdp.robot.Robot;
import mdp.simulation.view.MainFrame;

public interface IGUIControllable extends IGUIUpdatable {

    MainFrame getMainFrame();

    Map getMap();

    Robot getRobot();

    void reset();
}
