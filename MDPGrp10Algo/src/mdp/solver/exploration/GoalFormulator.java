package mdp.solver.exploration;

import mdp.map.Map;
import mdp.map.Waypoint;
import mdp.common.Vector2;

public class GoalFormulator {

    private MapViewer mapViewer;

    public GoalFormulator(MapViewer mv) {
        mapViewer = mv;
    }

    public void updateFrontiers(Map map) {

    }

    public Waypoint findFirstFrontier() {
        Waypoint waypoint = new Waypoint();

        return waypoint;
    }

    public boolean checkIfReachFinalGoal(Vector2 v) {
        return (v.i() == 13) && (v.j() == 18);
    }

    public boolean checkIfReachStartZone(Vector2 v) {
        return (v.i() == 1) && (v.j() == 1);
    }
}
