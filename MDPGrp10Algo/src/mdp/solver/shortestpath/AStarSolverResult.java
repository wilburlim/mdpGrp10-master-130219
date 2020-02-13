package mdp.solver.shortestpath;

import java.util.ArrayList;
import java.util.List;
import mdp.common.Vector2;

public class AStarSolverResult {
    public List<Vector2> shortestPath;
    public List<Vector2> openedPoints;
    public List<Vector2> closedPoints;
    
    AStarSolverResult() {
        shortestPath = new ArrayList<>();
        openedPoints = new ArrayList<>();
        closedPoints = new ArrayList<>();
    }
}
