package mdp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import mdp.common.Vector2;
import mdp.communication.ITranslatable;
import mdp.communication.Translator;
import mdp.map.Map;
import mdp.map.WPSpecialState;
import mdp.robot.Robot;
import mdp.solver.shortestpath.AStarSolver;
import mdp.solver.shortestpath.AStarUtil;
import mdp.communication.Compiler;
import mdp.robot.RobotAction;
import mdp.solver.shortestpath.SolveType;

public class Test {
    
    public static void run() throws IOException {
        
        AStarSolver solver = new AStarSolver();
        Robot robot = new Robot();
        Map map = new Map();
        
        
        List<Vector2> obs = new LinkedList<>();
        
        obs.add(new Vector2(5, 9));
        obs.add(new Vector2(6, 10));

        obs.add(new Vector2(0, 6));
        obs.add(new Vector2(1, 6));
        obs.add(new Vector2(2, 6));
        obs.add(new Vector2(2, 7));
        
        obs.add(new Vector2(0, 12));
        obs.add(new Vector2(1, 12));
        obs.add(new Vector2(2, 12));
        obs.add(new Vector2(2, 13));
        
        obs.add(new Vector2(3, 17));
        obs.add(new Vector2(3, 18));
        obs.add(new Vector2(3, 19));
        
        obs.add(new Vector2(11, 0));
        obs.add(new Vector2(11, 1));
        obs.add(new Vector2(11, 2));
        obs.add(new Vector2(8, 3));
        obs.add(new Vector2(9, 3));
        obs.add(new Vector2(10, 3));
        
        obs.add(new Vector2(6, 9));
        obs.add(new Vector2(6, 10));
        obs.add(new Vector2(6, 11));
        obs.add(new Vector2(6, 12));
        obs.add(new Vector2(7, 12));
        
        obs.add(new Vector2(9, 15));
        obs.add(new Vector2(8, 15));
        obs.add(new Vector2(8, 16));
        
        obs.add(new Vector2(14, 15));
        
        obs.add(new Vector2(14, 11));
        obs.add(new Vector2(13, 11));
        obs.add(new Vector2(12, 11));
        obs.add(new Vector2(12, 10));
        map.addObstacle(obs);
        System.out.println(map.toString(robot));
        
        
        List<Vector2> path = solver.solve(map, robot, waypoint,SolveType.Smooth).shortestPath;
        System.out.println("Path");
        path.forEach((pos) -> {
            System.out.println(pos);
        });
        map.highlight(path, WPSpecialState.IsPathPoint);
        System.out.println(map.toString(robot));
        
        
        List<Vector2> smoothPath = AStarUtil.smoothenPath(map, path, false);
        System.out.println("Smooth Path");
        smoothPath.forEach((pos) -> {
            System.out.println(pos);
        });
        map.highlight(smoothPath, WPSpecialState.IsOpenedPoint);
        System.out.println(map.toString(robot));
        
        Main.getRpi().sendSmoothMoveCommand(smoothPath);

    }
    
}
