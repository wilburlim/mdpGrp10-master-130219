package mdp.solver.exploration;

import java.util.ArrayList;
import java.util.List;

import mdp.common.Direction;

import mdp.map.Map;
import mdp.robot.Robot;
import mdp.common.Vector2;
import mdp.map.WPObstacleState;
import mdp.map.Waypoint;

public class Simulator {

    private Map objective_map;
    private SensingData s;

    public Simulator(Map map) {
        objective_map = map;
        s = new SensingData();
    }

    public SensingData getSensingData(Robot robot) {
        Vector2 edge, edge_l, edge_r,edge_b,edge_rm;
        
        edge = robot.position().fnAdd(robot.orientation().toVector2());
        s.front_m = detect(edge, robot.orientation());
        //System.out.println("s.front_m " + s.front_m);
        edge_l = edge.fnAdd(robot.orientation().getLeft().toVector2());
        s.front_l = detect(edge_l, robot.orientation());
        //System.out.println("s.front_l" + s.front_l);
        edge_r = edge.fnAdd(robot.orientation().getRight().toVector2());
        s.front_r = detect(edge_r, robot.orientation());
        //System.out.println("s.front_r " + s.front_r);

        s.left = detectLong(edge_l, robot.orientation().getLeft());
        //System.out.println("s.left " + s.left);
        s.right_f = detectMiddle(edge_r, robot.orientation().getRight());
        
        edge_rm = robot.position().fnAdd(robot.orientation().getRight().toVector2());
        s.right_m = detectMiddle(edge_rm, robot.orientation().getRight());
        //System.out.println("s.right " + s.right);
        return s;

    }

    private int detect(Vector2 position, Direction dir) {
        Vector2 tmp = new Vector2(position.i(), position.j());
        Waypoint wp;
        Vector2 unit;
        unit = dir.toVector2();

        tmp.add(unit);

        //seeing range is not relevant to virtual obstacle
        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 1;
        }
        tmp.add(unit);

        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 2;
        }
        tmp.add(unit);

        /*if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 3;
        }*/
        return 0; // no obstacle in front

    }

    
    private int detectMiddle(Vector2 position, Direction dir) {
        Vector2 tmp = new Vector2(position.i(), position.j());
        Waypoint wp;
        Vector2 unit;
        unit = dir.toVector2();

        tmp.add(unit);

        //seeing range is not relevant to virtual obstacle
        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 1;
        }
        tmp.add(unit);

        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 2;
        }
        tmp.add(unit);

        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 3;
        }
        return 0; // no obstacle in front

    }

    
    private int detectLong(Vector2 position, Direction dir) {
        Vector2 tmp = new Vector2(position.i(), position.j());
        Waypoint wp;
        Vector2 unit;
        unit = dir.toVector2();

        tmp.add(unit);

        //seeing range is not relevant to virtual obstacle
        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 1;
        }
        tmp.add(unit);

        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 2;
        }
        tmp.add(unit);

        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 3;
        }
        
        tmp.add(unit);
        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 4;
        }
        
        tmp.add(unit);
        if (!objective_map.checkValidBoundary(tmp) || objective_map.getPoint(tmp).obstacleState() == WPObstacleState.IsActualObstacle) {
            return 5;
        }
        
        return 0; // no obstacle in front

    }
    private static List<Vector2> _genBlockers(int[][] obstacleMap) {
        List<Vector2> blockers = new ArrayList<>();
        for (int i = 0; i < obstacleMap.length; i++) {
            for (int j = 0; j < obstacleMap[0].length; j++) {
                if (obstacleMap[i][j] == 1) {
                    blockers.add(new Vector2(i, j));
                }
            }
        }
        return blockers;
    }

    public void addObstacle(int[][] obstacleMap) {

        objective_map.addObstacle(_genBlockers(obstacleMap));
    }

}
