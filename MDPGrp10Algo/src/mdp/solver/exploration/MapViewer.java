package mdp.solver.exploration;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import mdp.map.Map;
import mdp.map.WPSpecialState;
import mdp.robot.Robot;
import mdp.robot.RobotAction;
import mdp.solver.shortestpath.AStarSolver;
import mdp.solver.shortestpath.AStarSolverResult;
import mdp.common.Vector2;
import mdp.common.Direction;
import mdp.Main;
import mdp.map.WPObstacleState;


public class MapViewer  {

    private Map map;

    // 1 empty, 2 obstacle, 0 havent explored
    private int[][] explored;
    private int[][] robotPosition;
    private int[][] confidentDetectionArea;
    private int[][] scanningRepeatedArea;
    public LinkedList<RobotMovementHistory> robotMovementHistory;

    MapViewer() {
        map = new Map();
        explored = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        robotMovementHistory = new LinkedList<RobotMovementHistory>();
        robotPosition = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        confidentDetectionArea = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
        
        scanningRepeatedArea = new int[][] { { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 } };
                
                
                
       
    }

    public boolean checkScanningRepeatedArea(Vector2 v){
        if (map.checkValidPosition(v)) {
            if (scanningRepeatedArea[v.i()][v.j()] == 1)
                return true;
        }
        return false;
        
    }
    
    public void markScanningRepeatedArea(Vector2 v){
        if (map.checkValidPosition(v)) {
            scanningRepeatedArea[v.i()][v.j()] =1;
                
        }
    }
    
    
    
    public boolean markRobotVisited(Vector2 v) {
        if (map.checkValidPosition(v)) {
            robotPosition[v.i()][v.j()] = 1;
            return true;
        }
        return false;

    }

    public boolean checkConfidence(Vector2 v) {
        if (map.checkValidBoundary(v)) {
            if (confidentDetectionArea[v.i()][v.j()] == 1)
                return true;
            else
                return false;
        }
        return true;

    }

    public boolean markConfidentRange(Robot robot) {
        Vector2 edge, edge_l, edge_r, edge_rm, edge_rf, edge_lf, left;

        Vector2 robotPosition = robot.position();
        markConfidentDetection(robotPosition);
        markConfidentDetectionDirect(robotPosition.i(), robotPosition.j() + 1);
        markConfidentDetectionDirect(robotPosition.i(), robotPosition.j() - 1);
        markConfidentDetectionDirect(robotPosition.i() - 1, robotPosition.j());
        markConfidentDetectionDirect(robotPosition.i() + 1, robotPosition.j());
        markConfidentDetectionDirect(robotPosition.i() + 1, robotPosition.j() + 1);
        markConfidentDetectionDirect(robotPosition.i() - 1, robotPosition.j() - 1);
        markConfidentDetectionDirect(robotPosition.i() + 1, robotPosition.j() - 1);
        markConfidentDetectionDirect(robotPosition.i() - 1, robotPosition.j() + 1);

        edge = robot.position().fnAdd(robot.orientation().toVector2().fnMultiply(2));
        edge_l = edge.fnAdd(robot.orientation().getLeft().toVector2());
        edge_r = edge.fnAdd(robot.orientation().getRight().toVector2());
        left = robot.position().fnAdd(robot.orientation().toVector2())
                .fnAdd(robot.orientation().getLeft().toVector2().fnMultiply(2));

        edge_rm = robot.position().fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2));
        edge_rf = robot.position().fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2))
                .fnAdd(robot.orientation().toVector2());
        edge_lf = robot.position().fnAdd(robot.orientation().getLeft().toVector2().fnMultiply(2))
                .fnAdd(robot.orientation().toVector2());

        markConfidentDetection(edge);
        markConfidentDetection(edge_l);
        markConfidentDetection(edge_r);
        markConfidentDetection(edge_rm);
        markConfidentDetection(edge_rf);
        markConfidentDetection(edge_lf);
        markConfidentDetection(left);

        return true;

    }

    public boolean markConfidentDetection(Vector2 v) {
        if (checkValidExploredRange(v))
            confidentDetectionArea[v.i()][v.j()] = 1;
        return true;
    }

    public boolean markConfidentDetectionDirect(int i, int j) {
        if (checkValidExploredRange(new Vector2(i, j)))
            confidentDetectionArea[i][j] = 1;
        return true;
    }

    public boolean checkRobotVisited(Vector2 v) {
        if (map.checkValidPosition(v)) {
            if (robotPosition[v.i()][v.j()] == 1)
                return true;
        }
        return false;

    }

    public int[][] getExplored() {
        return explored;
    }

    public Map getSubjectiveMap() {
        return map;
    }

    public void markExploredEmpty(Vector2 v) {

        if (map.checkValidBoundary(v) && (!checkConfidence(v))) {
            explored[v.i()][v.j()] = 1;
        }
    }

    public void markUnreachable(Vector2 v) {
        if (map.checkValidBoundary(v) && (!checkConfidence(v))) {
            explored[v.i()][v.j()] = -1;
        }
    }

    private void markExploredObstacle(Vector2 v) {

        if (map.checkValidBoundary(v) && (!checkConfidence(v))) // prevent it
                                                                // being a wall
                                                                // , out of
                                                                // bound array
        {
            if ((!(v.i() >= map.DIM_I - 3 && v.j() >= map.DIM_J - 3)) && (!(v.i() < 3 && v.j() < 3)))

                explored[v.i()][v.j()] = 2;
            else
                explored[v.i()][v.j()] = 1;   //goal and start position set as empty
        }
    }

    private void markExploredDirectEmpty(int i, int j) {

        explored[i][j] = 1;
    }

    public int checkExploredState(Vector2 v) {
        if (!map.checkValidBoundary(v)) {
            return 2;
        }
        return explored[v.i()][v.j()];

    }

    public void markRobotHistory(Vector2 p, Direction d) {

        robotMovementHistory.add(new RobotMovementHistory(p, d));
    }

    public int detectCircle(Vector2 p, Direction d) {
        RobotMovementHistory tmp = new RobotMovementHistory(p, d);
        for (int i = 0; i < robotMovementHistory.size(); i++) {
            if (RobotMovementHistory.compare(tmp, robotMovementHistory.get(i))) {
                return i - 1;
            }
        }

        return -1;

    }

    public LinkedList<RobotMovementHistory> getRobotMovementHistory() {
        return robotMovementHistory;
    }

    public boolean checkIfNavigationComplete() {
        boolean complete = true;
        for (int i = 0; i < Map.DIM_I; i++) {
            for (int j = 0; j < Map.DIM_J; j++) {
                if (explored[i][j] == 0) {
                    complete = false;
                    break;
                }
            }
        }

        return complete;
    }

    private void insertExploredIntoMap() {
        LinkedList<Vector2> listOfObserved = new LinkedList<>();
        for (int i = 0; i < Map.DIM_I; i++) {
            for (int j = 0; j < Map.DIM_J; j++) {
                if (explored[i][j] > 0) {
                    listOfObserved.add(new Vector2(i, j));
                }
            }
        }
        map.highlight(listOfObserved, WPSpecialState.IsExplored);
    }

    public String exploredAreaToString() {
        String result = "";
        for (int i = -1; i <= Map.DIM_I; i++) {
            for (int j = -1; j <= Map.DIM_J; j++) {
                if (i == -1 || j == -1 || i == Map.DIM_I || j == Map.DIM_J) {
                    result += "# ";
                } else {
                    switch (explored[i][j]) {
                    case 0:
                        result += "0 ";
                        break;
                    case 2:
                        result += "x ";
                        break;
                    case -1:
                        result += "? ";
                        break;    
                    default:
                        result += "  ";
                        break;
                    }
                }
            }
            result += "\n";
        }
        return result;

    }

    public String robotVisitedPlaceToString() {
        String result = "";
        for (int i = -1; i <= Map.DIM_I; i++) {
            for (int j = -1; j <= Map.DIM_J; j++) {
                if (i == -1 || j == -1 || i == Map.DIM_I || j == Map.DIM_J) {
                    result += "# ";
                } else {
                    switch (robotPosition[i][j]) {
                    case 1:
                        result += ": ";
                        break;

                    default:
                        result += "  ";
                        break;
                    }
                }
            }
            result += "\n";
        }
        return result;

    }

    public String confidenceDetectionAreaToString() {
        String result = "";
        for (int i = -1; i <= Map.DIM_I; i++) {
            for (int j = -1; j <= Map.DIM_J; j++) {
                if (i == -1 || j == -1 || i == Map.DIM_I || j == Map.DIM_J) {
                    result += "# ";
                } else {
                    switch (confidentDetectionArea[i][j]) {
                    case 1:
                        result += "+ ";
                        break;

                    default:
                        result += "  ";
                        break;
                    }
                }
            }
            result += "\n";
        }
        return result;

    }

    public Know checkAllAroundEmpty(Robot robot) throws InterruptedException, IOException {
        if (robot.checkIfHavingBufferActions()) {
            robot.executeBufferActions(ExplorationSolver.getExePeriod());
        }

        Know l, r, b, f;

        l = explorationUtil.checkWalkable(robot, Direction.Left, this);
        r = explorationUtil.checkWalkable(robot, Direction.Right, this);
        b = explorationUtil.checkWalkable(robot, Direction.Down,this);
        f = explorationUtil.checkWalkable(robot, Direction.Up, this);

        if (l == Know.Yes && r == Know.Yes && b == Know.Yes && f == Know.Yes) {
            return Know.Yes;
        }
        if (l == Know.Unsure || r == Know.Unsure || b == Know.Unsure || f == Know.Unsure) {
            return Know.Unsure;
        } else {
            return Know.No;
        }

    }

    public boolean checkIfRight5SquaresEmpty(Robot robot) throws InterruptedException, IOException {

        Vector2 edge_up, edge_mid, edge_down;

        return checkBackRightConnectingPoint(robot) != WPObstacleState.IsActualObstacle
                && checkFrontRightConnectingPoint(robot) != WPObstacleState.IsActualObstacle
                && explorationUtil.checkWalkable(robot, Direction.Right,this) == Know.Yes;
        // do sth

    }

    public void markRobotHistory() {

    }

    // 1 walkable, 0 not walkable, 2 need further exploration


    // take RPI data from Solver , update what I saw
    public Map updateMap(Robot robot, SensingData s) {
        List<Vector2> obstaclePositions = new ArrayList<>();
        Vector2 obstaclePosition;
        Vector2 edge, edge_l, edge_r, edge_b, edge_rm;
        int i = 1;
        int leftRange= 5;
        Vector2 robotPosition = robot.position();
        markExploredEmpty(robotPosition);
        markExploredDirectEmpty(robotPosition.i(), robotPosition.j() + 1);
        markExploredDirectEmpty(robotPosition.i(), robotPosition.j() - 1);
        markExploredDirectEmpty(robotPosition.i() - 1, robotPosition.j());
        markExploredDirectEmpty(robotPosition.i() + 1, robotPosition.j());
        markExploredDirectEmpty(robotPosition.i() + 1, robotPosition.j() + 1);
        markExploredDirectEmpty(robotPosition.i() - 1, robotPosition.j() - 1);
        markExploredDirectEmpty(robotPosition.i() + 1, robotPosition.j() - 1);
        markExploredDirectEmpty(robotPosition.i() - 1, robotPosition.j() + 1);

        edge = robot.position().fnAdd(robot.orientation().toVector2());
        edge_l = edge.fnAdd(robot.orientation().getLeft().toVector2());
        edge_r = edge.fnAdd(robot.orientation().getRight().toVector2());
        edge_b = robot.position().fnAdd(robot.orientation().getRight().toVector2())
                .fnAdd(robot.orientation().getBehind().toVector2());
        edge_rm = robot.position().fnAdd(robot.orientation().getRight().toVector2());
        
        if (s.front_m != 0) {
            obstaclePosition = edge.fnAdd(robot.orientation().toVector2().fnMultiply(s.front_m));
            if (map.checkValidBoundary(obstaclePosition)) {

                markExploredObstacle(obstaclePosition);
            }
            for (i = 1; i < s.front_m; i++) {
                markExploredEmpty(edge.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
            }

        } else {
            for (i = 1; i <= 2; i++) {
                markExploredEmpty(edge.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
            }
        }

        if (s.front_l != 0) {
            obstaclePosition = edge_l.fnAdd(robot.orientation().toVector2().fnMultiply(s.front_l));
            if (map.checkValidBoundary(obstaclePosition)) {

                markExploredObstacle(obstaclePosition);
            }
            for (i = 1; i < s.front_l; i++) {
                markExploredEmpty(edge_l.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
            }
            markExploredObstacle(edge_l.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
        } else {
            for (i = 1; i <= 2; i++) {
                markExploredEmpty(edge_l.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
            }
        }

        if (s.front_r != 0) {
            obstaclePosition = edge_r.fnAdd(robot.orientation().toVector2().fnMultiply(s.front_r));
            if (map.checkValidBoundary(obstaclePosition)) {

                markExploredObstacle(obstaclePosition);
            }
            for (i = 1; i < s.front_r; i++) {
                markExploredEmpty(edge_r.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
            }

        } else {
            for (i = 1; i <= 2; i++) {
                markExploredEmpty(edge_r.fnAdd(robot.orientation().toVector2().fnMultiply(i)));
            }
        }

        if (s.left != 0) {
            obstaclePosition = edge_l.fnAdd(robot.orientation().getLeft().toVector2().fnMultiply(s.left));
            if (map.checkValidBoundary(obstaclePosition)) {

                markExploredObstacle(obstaclePosition);
            }
            for (i = 1; i < s.left; i++) {
                markExploredEmpty(edge_l.fnAdd(robot.orientation().getLeft().toVector2().fnMultiply(i)));
            }

        } else {
            for (i = 1; i <= leftRange; i++) {
                markExploredEmpty(edge_l.fnAdd(robot.orientation().getLeft().toVector2().fnMultiply(i)));
            }
        }

        if (s.right_f != 0) {
            obstaclePosition = edge_r.fnAdd(robot.orientation().getRight().toVector2().fnMultiply(s.right_f));
            if (map.checkValidBoundary(obstaclePosition)) {

                markExploredObstacle(obstaclePosition);

            }
            for (i = 1; i < s.right_f; i++) {
                markExploredEmpty(edge_r.fnAdd(robot.orientation().getRight().toVector2().fnMultiply(i)));
            }

        } else {
            for (i = 1; i <= 3; i++) {
                markExploredEmpty(edge_r.fnAdd(robot.orientation().getRight().toVector2().fnMultiply(i)));
            }
        }

        if (s.right_m != 0) {

            obstaclePosition = edge_rm.fnAdd(robot.orientation().getRight().toVector2().fnMultiply(s.right_m));
            if (map.checkValidBoundary(obstaclePosition)) {

                markExploredObstacle(obstaclePosition);

            }
            for (i = 1; i < s.right_m; i++) {
                markExploredEmpty(edge_rm.fnAdd(robot.orientation().getRight().toVector2().fnMultiply(i)));
            }

        } else {
            for (i = 1; i <= 3; i++) {
                markExploredEmpty(edge_rm.fnAdd(robot.orientation().getRight().toVector2().fnMultiply(i)));
            }
        }

        /// mark confident explored area
        markConfidentRange(robot);
        // update map with proper obstacles
        map = new Map(explored, true);

        //insertExploredIntoMap();
        Main.getGUI().update(map);
        return map;
    }

    public LinkedList<Vector2> findUnexploredInAscendingDistanceOrder(Robot robot) {
        int i = 1;
        LinkedList<Vector2> total = new LinkedList<Vector2>();

        do {
            total.addAll(IdentifyUnexploredAround(i, robot.position()));
            i++;

        } while (i != 20);
        return total;
    }

    private List<Vector2> IdentifyUnexploredAround(int width, Vector2 center) {
        Vector2 traverse = center.fnAdd(new Vector2(width, -width));
        List<Vector2> list = new ArrayList<>();

        int i;
        for (i = 0; i < width * 2; i++) {
            if (checkValidExploredRange(traverse)) {

                if (explored[traverse.i()][traverse.j()] == 0) {
                    list.add(new Vector2(traverse.i(), traverse.j()));

                }
            }
            traverse.add(new Vector2(0, 1));
        }

        for (i = 0; i < width * 2; i++) {
            if (checkValidExploredRange(traverse)) {
                if (explored[traverse.i()][traverse.j()] == 0) {
                    list.add(new Vector2(traverse.i(), traverse.j()));

                }
            }
            traverse.add(new Vector2(-1, 0));
        }

        for (i = 0; i < width * 2; i++) {
            if (checkValidExploredRange(traverse)) {
                if (explored[traverse.i()][traverse.j()] == 0) {
                    list.add(new Vector2(traverse.i(), traverse.j()));

                }
            }
            traverse.add(new Vector2(0, -1));
        }

        for (i = 0; i < width * 2; i++) {
            if (checkValidExploredRange(traverse)) {
                if (explored[traverse.i()][traverse.j()] == 0) {
                    list.add(new Vector2(traverse.i(), traverse.j()));

                }
            }
            traverse.add(new Vector2(1, 0));
        }

        return list;
        // the special vector marks no vector found

    }

    public boolean markGhostBlock(Vector2 center) {
        Vector2 up, down, right, left;
        boolean upBlocked = false;
        boolean downBlocked = false;
        boolean rightBlocked = false;
        boolean leftBlocked = false;
        up = center.fnAdd(new Vector2(-1, 0));
        down = center.fnAdd(new Vector2(1, 0));
        right = center.fnAdd(new Vector2(0, 1));
        left = center.fnAdd(new Vector2(0, -1));
        int i;

        for (i = 0; i < 3; i++) {
            if (!map.checkValidBoundary(up) || map.getPoint(up).obstacleState() == WPObstacleState.IsActualObstacle) {
                upBlocked = true;
                break;
            }
            up.add(new Vector2(-1, 0));
        }

        for (i = 0; i < 3; i++) {
            if (!map.checkValidBoundary(down)
                    || map.getPoint(down).obstacleState() == WPObstacleState.IsActualObstacle) {
                downBlocked = true;
                break;
            }
            down.add(new Vector2(1, 0));
        }

        for (i = 0; i < 3; i++) {
            if (!map.checkValidBoundary(right)
                    || map.getPoint(right).obstacleState() == WPObstacleState.IsActualObstacle) {
                rightBlocked = true;
                break;
            }
            right.add(new Vector2(0, 1));
        }

        for (i = 0; i < 3; i++) {
            if (!map.checkValidBoundary(left)
                    || map.getPoint(left).obstacleState() == WPObstacleState.IsActualObstacle) {
                leftBlocked = true;
                break;
            }
            left.add(new Vector2(0, -1));
        }

        /*
         * System.out.println(center.toString() + "up "+ upBlocked + "down "+
         * downBlocked + "left "+ leftBlocked + "right "+ rightBlocked);
         */
        if ((upBlocked == true) && (downBlocked == true) && (leftBlocked == true) && (rightBlocked == true)) {
            // System.out.println("Here");
            markUnreachable(center);
            return true;
        } else
            return false;

    }

    public boolean checkValidExploredRange(Vector2 v) {

        return v.i() >= 0 && v.i() < (map.DIM_I) && v.j() >= 0 && v.j() < (map.DIM_J);
    }

    public boolean validate(Robot robot, RobotAction action) throws InterruptedException, IOException {
        Vector2 position;
        switch (action) {
        case MoveForward:
            if (explorationUtil.checkWalkable(robot, Direction.Up, this) == Know.No) {
                return false;
            }
            break;
        default:
            return true;
        }

        return true;
    }

    public boolean checkValidBoundary(Vector2 v){
        
        return map.checkValidBoundary(v);
    }
    
    public WPObstacleState getObstacleState(Vector2 v){
        
        return map.getPoint(v).obstacleState();
    }
    
    
 

 

    public ArrayList<Vector2> getUnExplored() {
        ArrayList<Vector2> unexplored = new ArrayList<Vector2>();
        for (int row = 0; row < Map.DIM_I; ++row) {
            for (int col = 0; col < Map.DIM_J; ++col) {
                if (explored[row][col] == 0) {
                    if (unexplored.size() > 0 && unexplored.get(unexplored.size() - 1).j() == row) {
                        unexplored.remove(unexplored.size() - 1);
                    }
                    Vector2 pair = new Vector2(row, col);
                    System.out.println(pair);
                    unexplored.add(pair);

                }
            }
        }
        int count = 0;
        while (unexplored.size() - 2 >= count) {
            if (unexplored.get(count + 1).i() == unexplored.get(count).i()) {
                if (unexplored.get(count + 1).j() - unexplored.get(count).j() <= 2) {
                    unexplored.remove(count);
                    --count;
                }
            }
            ++count;
        }

        return unexplored;
    }

    public boolean checkRightFrontBack(Robot robot) {
        Vector2 right_up, right_down, right_middle;

        /*
         * front_m =
         * robot.position().fnAdd(robot.orientation().toVector2().fnMultiply(2))
         * ; front_l = front_m.fnAdd(robot.orientation().getLeft().toVector2());
         * front_r = front_m.fnAdd(robot.orientation().getRight().toVector2());
         */
        right_up = robot.position().fnAdd(robot.orientation().toVector2())
                .fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2));
        right_down = robot.position().fnAdd(robot.orientation().getBehind().toVector2())
                .fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2));
        right_middle = robot.position().fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2));

        /*
         * if(map.getPoint(right_up).obstacleState() ==
         * WPObstacleState.IsActualObstacle &&
         * map.getPoint(right_down).obstacleState() ==
         * WPObstacleState.IsActualObstacle){ return CalibrationType.Right; }
         */

        /*
         * if(!map.checkValidBoundary(front_r) ||
         * map.getPoint(front_r).obstacleState() ==
         * WPObstacleState.IsActualObstacle) if(!map.checkValidBoundary(front_l)
         * ||map.getPoint(front_l).obstacleState() ==
         * WPObstacleState.IsActualObstacle){ return CalibrationType.Front_LR; }
         * 
         * if(!map.checkValidBoundary(front_m) ||
         * map.getPoint(front_m).obstacleState() ==
         * WPObstacleState.IsActualObstacle) if(!map.checkValidBoundary(front_l)
         * ||map.getPoint(front_l).obstacleState() ==
         * WPObstacleState.IsActualObstacle){ return CalibrationType.Front_ML; }
         * 
         * if(!map.checkValidBoundary(front_r) ||
         * map.getPoint(front_r).obstacleState() ==
         * WPObstacleState.IsActualObstacle) if(!map.checkValidBoundary(front_m)
         * ||map.getPoint(front_m).obstacleState() ==
         * WPObstacleState.IsActualObstacle){ return CalibrationType.Front_MR; }
         */
        if (!map.checkValidBoundary(right_up)
                || map.getPoint(right_up).obstacleState() == WPObstacleState.IsActualObstacle)
            if (!map.checkValidBoundary(right_down)
                    || map.getPoint(right_down).obstacleState() == WPObstacleState.IsActualObstacle) {
                if (map.checkValidBoundary(right_middle)
                        && map.getPoint(right_middle).obstacleState() != WPObstacleState.IsActualObstacle)
                    return true;
            }

        return false;

    }

    public WPObstacleState checkFrontRightConnectingPoint(Robot robot) {

        if (!map.checkValidBoundary(robot.position().fnAdd(robot.orientation().toVector2().fnMultiply(2))
                .fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2))))
            return WPObstacleState.IsActualObstacle;

        return map.getPoint(robot.position().fnAdd(robot.orientation().toVector2().fnMultiply(2))
                .fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2))).obstacleState();
    }

    public WPObstacleState checkBackRightConnectingPoint(Robot robot) {
        if (!map.checkValidBoundary(robot.position().fnAdd(robot.orientation().toVector2().fnMultiply(-2))
                .fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2))))
            return WPObstacleState.IsActualObstacle;

        return map.getPoint(robot.position().fnAdd(robot.orientation().toVector2().fnMultiply(-2))
                .fnAdd(robot.orientation().getRight().toVector2().fnMultiply(2))).obstacleState();
    }

    public boolean checkLeftObstacles(Robot _robot) {
        // TODO Auto-generated method stub
        Vector2 left_f, left_m, left_b;
        int m = 0, f = 0, b = 0;
        left_m = _robot.position().fnAdd(_robot.orientation().getLeft().toVector2().fnMultiply(2));
        left_f = left_m.fnAdd(_robot.orientation().toVector2());
        left_b = left_m.fnAdd(_robot.orientation().getBehind().toVector2());

        if (!map.checkValidBoundary(left_m)
                || checkExploredState(left_m)==2) {
            m = 1;
        }

        if (!map.checkValidBoundary(left_f)
                || checkExploredState(left_f)==2) {
            f = 1;
        }
        if (!map.checkValidBoundary(left_b)
                || checkExploredState(left_b)==2) {
            b = 1;
        }

        return (m + f + b) == 3;
    }

    
    


    public int getExploredState(Vector2 v) {
        // TODO Auto-generated method stub
        if (map.checkValidBoundary(v))
            return explored[v.i()][v.j()];
        else
            return -2;
    }

 
}
