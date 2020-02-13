package mdp.simulation.view;

import mdp.simulation.event.EventHandler;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import mdp.map.Map;
import mdp.robot.Robot;
import mdp.common.Vector2;
import mdp.map.WPObstacleState;
import mdp.map.WPSpecialState;

public class GridContainer extends JPanel {
    
    private static final int _DIM_ROW = 15;
    private static final int _DIM_COL = 20;
    private static final int _SQUARE_SIZE = 20; //size of each maze square
    private static final int _GAP = 1; //gapsize between white square boxes
    
    private GridSquare[][] _grid;
    private MouseAdapter _gridMouseAdapter;

    public GridSquare[][] getGrid() {
        return _grid;
    }

    public GridContainer() {
        // config
        this.setLayout(new FlowLayout(FlowLayout.LEFT, _GAP, _GAP));
        this.setPreferredSize(new Dimension(
        		_DIM_ROW * _SQUARE_SIZE + (_DIM_ROW + 1) * _GAP,
        		_DIM_COL * _SQUARE_SIZE + (_DIM_COL + 1) * _GAP
        ));
        this.setBackground(ColorConfig.BG);
        
        // children
        this.fillGrid(new Map(), new Robot());
    }
    
    private List<Vector2> _gen3x3(Vector2 curPos) {
        List<Vector2> result = new ArrayList<>();
        result.add(curPos);
        result.add(curPos.fnAdd(new Vector2(0, 1)));
        result.add(curPos.fnAdd(new Vector2(0, -1)));
        result.add(curPos.fnAdd(new Vector2(1, 0)));
        result.add(curPos.fnAdd(new Vector2(-1, 0)));
        result.add(curPos.fnAdd(new Vector2(1, 1)));
        result.add(curPos.fnAdd(new Vector2(1, -1)));
        result.add(curPos.fnAdd(new Vector2(-1, 1)));
        result.add(curPos.fnAdd(new Vector2(-1, -1)));
        return result;
    }
    
    private int _getColorOrder(Color color) {
        if (color.equals(ColorConfig.ROBOT_HEAD)) {
            return 0;
        } else if (color.equals(ColorConfig.ROBOT_BODY)) {
            return -1;
        } else if (color.equals(ColorConfig.PATH) || 
                    color.equals(ColorConfig.OPENED) || 
                    color.equals(ColorConfig.CLOSED) ||
                    color.equals(ColorConfig.UNEXPLORED)||color.equals(ColorConfig.GOAL)) {
            return -2;
        } else {
            return -3;
        }
    }
    
    private Color _resolveColor(String curKey, Color targetColor, HashMap<String, Color> specialColors) {
        Color existingColor = specialColors.getOrDefault(curKey, ColorConfig.NORMAL);
        return _getColorOrder(existingColor) > _getColorOrder(targetColor) ? 
                existingColor : targetColor;
    }
    
    private boolean _colorOverideCheck(Vector2 curPos, HashMap<String, Color> specialColors) {
        if (specialColors.containsKey(curPos.toString())) {
            if (specialColors.get(curPos.toString()).equals(ColorConfig.PATH)) return false;
            for (Vector2 adjPos : _gen3x3(curPos)) {
                if (specialColors.containsKey(adjPos.toString())) {
                    if (specialColors.get(adjPos.toString()).equals(ColorConfig.ROBOT_BODY)) return false;
                    if (specialColors.get(adjPos.toString()).equals(ColorConfig.ROBOT_HEAD)) return false;
                }
            }
        }
        return true;
    }
    
    private void _applyMouseAdapter() {
        for (GridSquare[] row : _grid) {
            for (GridSquare square : row) {
                for (MouseListener mouseListener : square.getMouseListeners()) {
                    square.removeMouseListener(mouseListener);
                }
                square.addMouseListener(_gridMouseAdapter); //add mouse listener to each grid square
            }
        }
    }
    
    private HashMap<String, Color> _genSpecialColor(Map map, Robot robot) {
        HashMap<String, Color> result = new HashMap<>();
        map.toList().forEach((curPoint) -> { //from map object, call function toList to get arraylist of Way point objects
            String curKey;                   //then for each way point, 
            // 1x1
            // actual obstacle
            curKey = curPoint.position().toString(); //convert the way point vector object to string (i, j)
            
            boolean isUnexplored = false;
            if (!EventHandler.isShortestPath()) { //if event handler shortest path state is false, initially true
                if (!curPoint.specialState().equals(WPSpecialState.IsExplored) &&
                    !curPoint.specialState().equals(WPSpecialState.IsStart) &&
                    !curPoint.specialState().equals(WPSpecialState.IsGoal)) {
                    result.put(curKey, _resolveColor(curKey, ColorConfig.UNEXPLORED, result)); //if square is unexplored, called grid container function _resolveColor and add
                    isUnexplored = true;
                }
            }
            if (curPoint.obstacleState().equals(WPObstacleState.IsActualObstacle)) {
                result.put(curKey, _resolveColor(curKey, ColorConfig.OBSTACLE, result));
            }
            // 3x3
            switch (curPoint.specialState()) {
                case IsStart:
                    for (Vector2 curPos : _gen3x3(curPoint.position())) {
                        curKey = curPos.toString();
                        result.put(curKey, _resolveColor(curKey, ColorConfig.START, result));
                    }
                    break;
                case IsGoal:
                    for (Vector2 curPos : _gen3x3(curPoint.position())) {
                        curKey = curPos.toString();
                        result.put(curKey, _resolveColor(curKey, ColorConfig.GOAL, result));
                    }
                    break;
                case IsPathPoint:
                    if (!isUnexplored) {
                        curKey = curPoint.position().toString();
                        result.put(curKey, _resolveColor(curKey, ColorConfig.PATH, result));
                    }
                    break;
                case IsClosedPoint:
                    if (!isUnexplored) {
                        curKey = curPoint.position().toString();
                        result.put(curKey, _resolveColor(curKey, ColorConfig.CLOSED, result));
                    }
                    break;
                case IsOpenedPoint:
                    if (!isUnexplored) {
                        curKey = curPoint.position().toString();
                        result.put(curKey, _resolveColor(curKey, ColorConfig.OPENED, result));
                    }
                    break;
            }
            // robot
            if (curPoint.position().equals(robot.position())) {
                for (Vector2 curPos : _gen3x3(curPoint.position())) {
                    Color robotSqColor = ColorConfig.ROBOT_BODY;
                    if (robot.position()
                            .fnAdd(robot.orientation().toVector2())
                            .equals(curPos)) {
                        robotSqColor = ColorConfig.ROBOT_HEAD;
                    }
                    curKey = curPos.toString();
                    result.put(curKey, _resolveColor(curKey, robotSqColor, result));
                }
            }
            
        });
        
        String curKey;
        for (Vector2 curPos : _gen3x3(new Vector2(13,18))) {
            curKey = curPos.toString();
            result.put(curKey, _resolveColor(curKey, ColorConfig.GOAL, result));
        }
        
        return result;
    }
    
    public void fillGrid(Map map, Robot robot) {
        boolean isFirstTime = _grid == null;
//        this.removeAll();
        if (isFirstTime) _grid = new GridSquare[_DIM_COL][_DIM_ROW]; //array of square grid objects

        HashMap<String, Color> specialColors = _genSpecialColor(map, robot); //generate hash for special colors for robot, obstacles and start and goal

        for (int j = 19; j >= 0; j--) {
            for (int i = 0; i < _DIM_ROW; i++) {
                if (isFirstTime) _grid[_DIM_COL-1-j][i] = new GridSquare(new Vector2(i, j)); //for each array element in array _grid, instantiate new grid square object

                Vector2 curLocation = new Vector2(i, j); // add vector object to grid square
                Color targetColor = ColorConfig.NORMAL; //set square color to white
                if (specialColors.containsKey(curLocation.toString())) { // if grid square element is a special square in the hash map specialColors
                    targetColor = specialColors.get(curLocation.toString()); //change square color to special color
                }
                _grid[_DIM_COL-1-j][i].setBackground(targetColor); 
                
                if (isFirstTime) {
                	_grid[_DIM_COL-1-j][i].setPreferredSize(new Dimension(
                        _SQUARE_SIZE,
                        _SQUARE_SIZE
                    ));
                	System.out.println(i+","+ j);
                }
            }
        }
        
        
        if (isFirstTime) {
            for (GridSquare[] row : _grid) {
                for (GridSquare square : row) {
                    this.add(square); //add all the grid square objects to grid container
                }
            }
        }
    }
    
    public void setGridAdapter(MouseAdapter adapter) {
        _gridMouseAdapter = adapter; 
        _applyMouseAdapter();
    }
    
}
