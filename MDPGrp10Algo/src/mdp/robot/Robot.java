package mdp.robot;

import java.io.IOException;

import java.util.Arrays;
import java.util.LinkedList;

import mdp.common.Direction;
import mdp.common.Vector2;
import mdp.communication.Translator;
import mdp.solver.exploration.ActionFormulator;
import mdp.solver.exploration.ExplorationSolver;
import mdp.solver.exploration.MapViewer;
import mdp.Main;
import mdp.map.Map;

public class Robot {

    private Vector2 _position;
    private Direction _orientation;

    private static volatile int calibrationCounter = 0;
    private static volatile boolean actionCompleted = false;

    private static volatile boolean robotVisitedBefore = false;
    
    private static LinkedList<RobotAction> bufferedActions = new LinkedList<>();
    private MapViewer mapViewer;
    private ActionFormulator actionFormulator;
    private long executionStartTime = System.currentTimeMillis();
    private long executionEndTime ;
    public Robot() {
        this(new Vector2(1, 1), Direction.Right); //right is up in this case lmao
    }

    public Robot(Vector2 position, Direction direction) {
        _position = position;
        _orientation = direction;
    }

    public Robot(Vector2 position, Direction direction, MapViewer mv , ActionFormulator ac) {
        _position = position;
        _orientation = direction;
        mapViewer = mv;
        actionFormulator = ac;
    }

    public boolean checkIfRobotVisitedBefore(){
        
        return robotVisitedBefore;
    }
    public Vector2 position() {
        return _position;
    }

    public Direction orientation() {
        return _orientation;
    }

    public void position(Vector2 position) {
        _position = position;
    }

    public void orientation(Direction direction) {
        _orientation = direction;
    }

    public void execute(RobotAction action) {
        Vector2 dirVector = _orientation.toVector2();
        switch (action) {
            case MoveForward:
                // RPI call

                _position.add(dirVector);
                break;
            case MoveBackward:
                // RPI call
                dirVector.multiply(-1);

                _position.add(dirVector);
                break;
            case RotateLeft:
                // RPI call
                _orientation = _orientation.getLeft();
                break;
            case RotateRight:
                // RPI call
                _orientation = _orientation.getRight();
                break;
                
            
        }
    }

    public boolean bufferAction(RobotAction action) {
        return bufferedActions.add(action);
    }
    
    public int checkBufferActionSize() {
        return bufferedActions.size();
    }
    
    public void cleanBufferedActions(){
    		bufferedActions.clear();
    		
    }
    
    public static void actionCompletedCallBack() {
        actionCompleted = true;

    }
    
    public void executeBufferActions(int sleepPeriod) throws IOException {
        try {
            
            ExplorationSolver.setPermitTerminationState(false);    
            if (!Main.isSimulating()) {
            		executionEndTime = System.currentTimeMillis();
            		System.out.println("Computational time for next movement"+ (executionStartTime- executionEndTime) + "ms");
                
            	Main.getRpi().sendMoveCommand(bufferedActions, Translator.MODE_0);
                
                while (!actionCompleted) {
                }
                executionStartTime = System.currentTimeMillis();
                Map map = mapViewer.getSubjectiveMap();
                int[][] explored = mapViewer.getExplored();

                // send info to android
                Main.getRpi().sendInfoToAndroid(map, explored, bufferedActions);

                //System.out.println("Actions completed");
                actionCompleted = false;
                //increment calibrationCounter
                calibrationCounter += bufferedActions.size();
            }
            int first = 0;
            int index =0;
            for (RobotAction action : bufferedActions) {
                
                execute(action);
                if(mapViewer.checkRobotVisited(_position)){
                    robotVisitedBefore= true;
                    
                }
                else 
                    robotVisitedBefore = false;
                mapViewer.markRobotVisited(_position);
                
                //System.out.println("Execute action: "+action.toString());
                /*f(first == 0 && mapViewer.detectCircle(_position, _orientation)!=-1){
                		first = 1;
                		index = mapViewer.detectCircle(_position, _orientation);
                }
                mapViewer.markRobotHistory(_position, _orientation);*/
                Main.getGUI().update(this);
                
            if (Main.isSimulating()) {
                    Thread.sleep(sleepPeriod);
                } 
            } 
            
            
            /*
            if(first!= 0){
            		
            		
            	    actionFormulator.reverseToThePoint(mapViewer.getRobotMovementHistory().get(index), this);
            	    int i = mapViewer.robotMovementHistory.size()-index -1 ;
            	    while( i >0 )
            		{
            			mapViewer.robotMovementHistory.removeLast();
            			i--;
            		}
            	    
            }
            	*/	
            
            
            bufferedActions.clear();
            ExplorationSolver.setPermitTerminationState(true);   
        } catch (InterruptedException e) {
            System.out.println("Robot execution interrupted");
        }
    }

    public boolean checkIfHavingBufferActions() {
        return !bufferedActions.isEmpty();
    }

    public boolean checkIfCalibrationCounterReached() {
        return (calibrationCounter >= 6);
    }

    public boolean clearCalibrationCounter() {
        calibrationCounter = 0;
        return true;
    }
    
    public  LinkedList<RobotAction> getBufferedActions(){
        return bufferedActions;
    }

}
