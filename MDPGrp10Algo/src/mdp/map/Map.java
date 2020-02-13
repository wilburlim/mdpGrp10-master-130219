package mdp.map;

import java.util.ArrayList;
import java.util.List;
import mdp.common.Console;
import mdp.robot.Robot;
import mdp.common.Vector2;

public class Map {
    // constants
    public static final int DIM_I = 15;
    public static final int DIM_J = 20;
    public static final Vector2 START_POS = new Vector2(1, 1);
    public static final Vector2 GOAL_POS = new Vector2(DIM_I - 2, DIM_J - 2);
    public static final Vector2 WAYPOINT_POS = new Vector2(11, 11);
    
    private final Waypoint[][] _wpMap;
    
    public Map() {
        _wpMap = new Waypoint[DIM_I][DIM_J];  //declare an array of way points with all null way point objects
        for (int i = 0; i < DIM_I; i++) {  
            for (int j = 0; j < DIM_J; j++) {
                // init default values
                Vector2 curPos = new Vector2(i, j); //iterates for the entire map size
                WPSpecialState curSpecState = WPSpecialState.NA;
                WPObstacleState curObsState = WPObstacleState.IsWalkable;
                
                // change special state if applicable
                if (START_POS.equals(curPos)) {
                    curSpecState = WPSpecialState.IsStart;
                } else if (GOAL_POS.equals(curPos)) {
                    curSpecState = WPSpecialState.IsGoal;
                } else if (i == 0 || j == 0 || i == DIM_I - 1 || j == DIM_J - 1) {
                    curObsState = WPObstacleState.IsVirtualObstacle; //border squares of grid container
                }
                
                // create point
                _wpMap[i][j] = new Waypoint(curPos, curSpecState, curObsState);
            }
        }
    }
    
    public Map(int[][] explored, boolean isStillExploring) {
        _wpMap = new Waypoint[DIM_I][DIM_J];
        List<Vector2> obstacles = new ArrayList<>();
        for (int i = 0; i < DIM_I; i++) {
            for (int j = 0; j < DIM_J; j++) {
                // init default values
                Vector2 curPos = new Vector2(i, j);
                WPSpecialState curSpecState = WPSpecialState.NA;
                WPObstacleState curObsState = WPObstacleState.IsWalkable;
                
                // change special state if applicable
                if (START_POS.equals(curPos)) {
                    curSpecState = WPSpecialState.IsStart;
                } else if (GOAL_POS.equals(curPos)) {
                    curSpecState = WPSpecialState.IsGoal;
                } else {
                    switch (explored[i][j]) { //content in array, 1,2 or 0
                        case 0:
                        		if (!isStillExploring) {
                             	obstacles.add(curPos);
                        		}
                            break;
                        case 2:
                            obstacles.add(curPos);
                            curSpecState = WPSpecialState.IsExplored;
                            break;
                        case 1:
                            curSpecState = WPSpecialState.IsExplored;
                            break;
                        
                    }
                }
                
                
                if(i == 0 || i== DIM_I-1 || j == 0 || j==DIM_J-1)
                		curObsState = WPObstacleState.IsVirtualObstacle;
                // create point
                _wpMap[i][j] = new Waypoint(curPos, curSpecState, curObsState);
            }
        }
        addObstacle(obstacles);
    }
    
    private void _setObstacle(Vector2 pos, WPObstacleState obsState) {
        _wpMap[pos.i()][pos.j()].obstacleState(obsState);
    }
    
    private void _processObstacleList(List<Vector2> obsList, 
                                        WPObstacleState actualState,
                                        WPObstacleState virtualCheckState,
                                        WPObstacleState virtualState) {
        obsList.forEach((curObsPos) -> {
            // add blocking tag for the obstacle
            _setObstacle(curObsPos, actualState);
            
            // add blocking tag for points adjacent to the obstacle
            for (int deltaI = -1; deltaI <= 1; deltaI++) {
                for (int deltaJ = -1; deltaJ <= 1; deltaJ++) {
                    int adjI = curObsPos.i() + deltaI;
                    int adjJ = curObsPos.j() + deltaJ;
                    if (adjI > -1 && adjI < DIM_I && adjJ > -1 && adjJ < DIM_J &&
                        _wpMap[adjI][adjJ].obstacleState() == virtualCheckState) {
                        Vector2 adjacentPos = new Vector2(adjI, adjJ);
                        _setObstacle(adjacentPos, virtualState);
                    }
                }
            }
        });
    }
    public void addObstacle(List<Vector2> obstaclePositions) {
        _processObstacleList(
            obstaclePositions,
            WPObstacleState.IsActualObstacle,
            WPObstacleState.IsWalkable,
            WPObstacleState.IsVirtualObstacle
        );
    }
    public void addObstacle(Vector2 pos) {
        List<Vector2> temp = new ArrayList<>();
        temp.add(pos);
        addObstacle(temp);
    }
    public void clearObstacle(List<Vector2> obstaclePositions) {
        _processObstacleList(
            obstaclePositions,
            WPObstacleState.IsWalkable,
            WPObstacleState.IsVirtualObstacle,
            WPObstacleState.IsWalkable
        );
    }
    public void clearObstacle(Vector2 pos) {
        List<Vector2> temp = new ArrayList<>();
        temp.add(pos);
        clearObstacle(temp);
    }
    public List<Waypoint> toList() {  //from the array of way points _wpMap, add all array elements to an arraylist and return it
        List<Waypoint> result = new ArrayList<>();
        for (int i = 0; i < DIM_I; i++) {
            for (int j = 0; j < DIM_J; j++) {
                result.add(_wpMap[i][j]);
            }
        }
        return result;
    }
    
    public void highlight(List<Vector2> hightlightPositions, WPSpecialState wPSpecialState) {
        hightlightPositions.forEach((pos) -> {
            Waypoint curPoint = _wpMap[pos.i()][pos.j()];
            if (curPoint.specialState() != WPSpecialState.IsStart &&
                curPoint.specialState() != WPSpecialState.IsGoal) {
                curPoint.specialState(wPSpecialState);
            }
        });
    }
    public void clearAllHighlight() {
        for (Waypoint[] row : _wpMap) {
            for (Waypoint wp : row) {
                if (!wp.specialState().equals(WPSpecialState.IsStart) && 
                    !wp.specialState().equals(WPSpecialState.IsGoal)) {
                    wp.specialState(WPSpecialState.NA);
                }
            }
        }
    }
    
    public String toString(Robot robot) {
        String result = "";
        for (int i = -1; i <= DIM_I; i++) {
            for (int j = -1; j <= DIM_J; j++) {
                if (i == -1 || j == -1 || i == DIM_I || j == DIM_J) {
                    result += "# ";
                } else {
                    Waypoint curPoint = _wpMap[i][j];
                    if (curPoint.position().equals(robot.position())) {
                        String symbol = "  ";
                        switch (robot.orientation()) {
                            case Up:
                                symbol = "^ ";
                                break;
                            case Down:
                                symbol = "_ ";
                                break;
                            case Left:
                                symbol = "< ";
                                break;
                            case Right:
                                symbol = "> ";
                                break;
                        }
                        result += Console.ANSI_RED + symbol + Console.ANSI_RESET;
                        continue;
                    }
                    switch (curPoint.specialState()) {
                        case IsStart:
                            result += Console.ANSI_CYAN + "S " + Console.ANSI_RESET;
                            continue;
                        case IsGoal:
                            result += Console.ANSI_PURPLE + "G " + Console.ANSI_RESET;
                            continue;
                        case IsPathPoint:
                            result += Console.ANSI_GREEN + "x " + Console.ANSI_RESET;
                            continue;
                        case IsOpenedPoint:
                            result += "x ";
                            continue;
                        case IsClosedPoint:
                            result += Console.ANSI_YELLOW + "x " + Console.ANSI_RESET;
                            continue;
                        case NA:
                            break;
                    }
                    switch (curPoint.obstacleState()) {
                        case IsActualObstacle:
                            result += Console.ANSI_WHITE_BACKGROUND + " " + Console.ANSI_RESET + " ";
                            break;
                        case IsVirtualObstacle:
                            result += "  ";
                            break;
                        case IsWalkable:
                            result += Console.ANSI_GRAY + "+ " + Console.ANSI_RESET;
                            break;
                    }
                }
            }
            result += "\n";
        }
        return result;
    }
    
    public Waypoint getPoint(Vector2 position) {
    		//System.out.println("i "+ position.i() + "j "+ position.j());
        return _wpMap[position.i()][position.j()];
    }
    
    public boolean checkValidPosition(Vector2 pos) {
        return pos.i() > 0 && pos.i() < DIM_I &&
               pos.j() > 0 && pos.j() < DIM_J;
    }
    
    public boolean checkValidBoundary(Vector2 pos){
    		return pos.i() >=0 && pos.i() < DIM_I &&
                pos.j() >=0 && pos.j() < DIM_J;
    }
    
    
}
