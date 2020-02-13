package mdp.solver.exploration;

import mdp.common.Direction;
import mdp.common.Vector2;

public class RobotMovementHistory {
	public Vector2 _position;
	public Direction _direcition;
	
	public RobotMovementHistory(Vector2 p , Direction d){
		_position = p;
		_direcition = d;
	}
	
	public static boolean compare(RobotMovementHistory a, RobotMovementHistory b){
		return a._position==b._position && a._direcition == b._direcition;
		
	}
	
}
