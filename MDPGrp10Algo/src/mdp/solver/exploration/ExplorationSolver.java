package mdp.solver.exploration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import mdp.Main;
import mdp.common.Direction;
import mdp.map.Map;
import mdp.map.WPObstacleState;
import mdp.robot.Robot;
import mdp.common.Vector2;
import mdp.robot.RobotAction;
import mdp.solver.shortestpath.AStarSolver;
import mdp.solver.shortestpath.AStarSolverResult;
import mdp.solver.shortestpath.SolveType;
import mdp.simulation.IGUIUpdatable;

public class ExplorationSolver {

    // public enum EndType { Normal, Interrupt }
    private static Map objective_map; // generated from map solver

    private static Simulator simulator;

    private static MapViewer mapViewer;
    private static ActionFormulator actionFormulator;
    private static GoalFormulator goalFormulator;

    private static int _exePeriod;
    private static Robot _robot;

    private static volatile boolean permitTermination = true;

    public static void setPermitTerminationState(boolean var) {
        permitTermination = var;
    }

    public static boolean checkPermitTerminationState() {
        return permitTermination;
    }

    private static boolean _hasFinishedFirstRound;

    public static void solve(Map map, int exePeriod) throws InterruptedException, IOException {
        mapViewer = new MapViewer();
        goalFormulator = new GoalFormulator(mapViewer);
        _exePeriod = exePeriod;
        objective_map = map; //map that was pre-loaded and existing on GUI
        simulator = new Simulator(objective_map);

        Vector2 robotPos = new Vector2(1, 1);
        Direction robotDir = Direction.Right; //robot facing up
        actionFormulator = new ActionFormulator(mapViewer, simulator);
        _robot = new Robot(robotPos, robotDir, mapViewer, actionFormulator);

        // Direction last_orientation;
        // LinkedList<Direction> twoDirectionAway = new LinkedList<>();
        boolean goalZoneReached = false;
        // put some blockers into the map
        System.out.println("For Simulation Purpose");
        System.out.println(objective_map.toString(_robot)); //important

        // data = getDataFromRPI();
        System.out.println(_robot.position());

        // default to rotate in order to get initial sensing data
        _robot.bufferAction(RobotAction.RotateRight);
        _robot.executeBufferActions(ExplorationSolver.getExePeriod());
        ////////////
        // start exploration

        // twoDirectionAway.add(Direction.Down);
        // twoDirectionAway.add(Direction.Down);
        int counter = 0;
        Vector2 last_position;
        /*
         * while (!goalFormulator.checkIfReachFinalGoal(_robot.position())) {
         * //System.out.println("following right wall");
         * 
         * //System.out.println(mapViewer.getSubjectiveMap().toString(_robot));
         * last_position = new Vector2(_robot.position().i(),
         * _robot.position().j()); last_orientation = _robot.orientation();
         * actionFormulator.rightWallFollower(_robot);
         * 
         * System.out.println("Last position: " + last_position.toString());
         * System.out.println("Current position: " +
         * _robot.position().toString());
         * System.out.println("last orientation: " +
         * last_orientation.toString());
         * //System.out.println(mapViewer.exploredAreaToString());
         * //System.out.println(mapViewer.robotVisitedPlaceToString());
         * //System.out.println(mapViewer.getSubjectiveMap().toString(_robot));
         * 
         * if (_robot.position().equals(last_position.fnAdd(last_orientation.
         * getRight().toVector2()))) {
         * 
         * counter++; } else { counter = 0; }
         * 
         * if (counter == 7) { _robot.bufferAction(RobotAction.RotateRight);
         * _robot.bufferAction(RobotAction.RotateRight);
         * _robot.bufferAction(RobotAction.MoveForward);
         * 
         * actionFormulator.view(_robot); counter = 0; }
         * System.out.println("Counter is " + counter);
         * 
         * }
         */

        _hasFinishedFirstRound = false;
        while (!goalZoneReached || !goalFormulator.checkIfReachStartZone(_robot.position())) {

            if (_robot.position().equals(new Vector2(map.DIM_I - 2, map.DIM_J - 2))) {
                goalZoneReached = true;
            }
            actionFormulator.rightWallFollower(_robot);
            
            
            actionFormulator.actionSimplifier(_robot);
        }

        _hasFinishedFirstRound = true;

        // System.out.println("Before thread sleep");
        

        if (!mapViewer.checkIfNavigationComplete() && !Main.getGUI().isSingleRoundRun()) {
            

            // System.out.println("After thread sleep");
            actionFormulator.exploreRemainingArea(_robot);

        }

        // goBackToStart( map,_robot, ()->{});

    }

    public static int getExePeriod() {
        return _exePeriod;
    }

    public static MapViewer getMapViewer() {
        return mapViewer;
    }

    public static Robot getRobot() {
        return _robot;
    }

    public static boolean hasFinishedFirstRound() {
        return _hasFinishedFirstRound;
    }

    // look through map and update
    public static void goBackToStart(Map map, Robot robot, Runnable callback) throws IOException, InterruptedException {
        System.out.println("Going back to start with the following map");
        System.out.println(map.toString(robot));
        AStarSolverResult result = new AStarSolver().solve(map, robot, Map.START_POS, SolveType.Safe);
        LinkedList<RobotAction> actions = RobotAction.fromPath(robot, result.shortestPath);

        robot.cleanBufferedActions();
        for (RobotAction action : actions) {
            // if (robot.checkBufferActionSize() < 4) {
            // robot.bufferAction(action);
            // } else {
            robot.bufferAction(action);
            // actionFormulator.view(_robot);
            robot.executeBufferActions(ExplorationSolver.getExePeriod());
            // allow update map

            // for the purpose of calibration
            // in this way, after every five actions, the robot will calibrate
            // }
        }
        if (robot.checkBufferActionSize() != 0) {
            robot.executeBufferActions(ExplorationSolver.getExePeriod());
            // robot.executeBufferActions(ExplorationSolver.getExePeriod());
        }

        // while (!goalFormulator.checkIfReachStartZone(_robot.position())) {
        // actionFormulator.rightWallFollower(_robot);
        // }
        System.out.println("Starting callback");
        _restoreOrientation(callback);
        if (!Main.isSimulating()) {
            Main.getRpi().sendExplorationEndMarker();
        }
    }

    private static void _restoreOrientation(Runnable callback) throws IOException {
        System.out.println("restoring orientation");
        if (!_robot.orientation().equals(new Robot().orientation())) {
            if (_robot.orientation().equals(Direction.Up)) {
                _robot.bufferAction(RobotAction.RotateLeft);
                _robot.executeBufferActions(_exePeriod);
            }
            if (!Main.isSimulating()) {
                Main.getRpi().sendCalibrationCommand(CalibrationType.Front_LR);
            }
            _robot.bufferAction(RobotAction.RotateRight);
            _robot.executeBufferActions(_exePeriod);
            if (!Main.isSimulating()) {
                Main.getRpi().sendCalibrationCommand(CalibrationType.Front_LR);
            }
            _robot.bufferAction(RobotAction.RotateRight);
            _robot.executeBufferActions(_exePeriod);
        }
        callback.run();
    }
}
