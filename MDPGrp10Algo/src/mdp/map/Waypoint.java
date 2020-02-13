package mdp.map;

import mdp.common.Vector2;

public class Waypoint {
    
    private final Vector2 _position;
    private WPSpecialState _specialState;
    private WPObstacleState _obstacleState;
    
    public Waypoint(Vector2 position, WPSpecialState specialState, WPObstacleState obstacleState) {
        _position = position;
        _specialState = specialState;
        _obstacleState = obstacleState;
    
    }
    public Waypoint(Vector2 position) {
        this(position, WPSpecialState.NA, WPObstacleState.IsWalkable); 
    }
    public Waypoint() {
        this(new Vector2(-1, -1)); 
    }
    
    public Vector2 position() { return _position; }
    public WPSpecialState specialState() { return _specialState; }
    public WPObstacleState obstacleState() { return _obstacleState; }
    public void obstacleState(WPObstacleState obstacleState) { _obstacleState = obstacleState; }
    public void specialState(WPSpecialState specialState) { _specialState = specialState; }
    
}
