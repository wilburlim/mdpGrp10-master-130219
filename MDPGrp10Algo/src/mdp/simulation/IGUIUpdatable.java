package mdp.simulation;

import mdp.map.Map;
import mdp.robot.Robot;
import mdp.simulation.event.GUIClickEvent;

public interface IGUIUpdatable {
    
//    public enum ManualTrigger { Exploration, ShortestPath, Combined, Stop }

//    void trigger(ManualTrigger trigger);
    void trigger(GUIClickEvent hdlr);

    void update(Map map, Robot robot);

    void update(Map map);

    void update(Robot robot);
    
    boolean isSingleRoundRun();
    
}
