package mdp.solver.exploration;

import java.io.IOException;
import java.util.LinkedList;

import mdp.common.Direction;
import mdp.common.Vector2;
import mdp.map.WPObstacleState;
import mdp.robot.Robot;
import mdp.solver.shortestpath.AStarSolver;
import mdp.solver.shortestpath.AStarSolverResult;

public class explorationUtil {
    
    
    public static Know checkWalkable(Robot robot, Direction d , MapViewer mapViewer) throws InterruptedException, IOException {
        if (robot.checkIfHavingBufferActions()) {
            robot.executeBufferActions(ExplorationSolver.getExePeriod());
        }

        Vector2 edge1, edge2, edge3;
        int s1, s2, s3;
        Direction dir = Direction.Up;

        switch (d) {
        case Up:
            dir = robot.orientation();
            break;
        case Down:
            dir = robot.orientation().getLeft().getLeft();
            break;
        case Right:
            dir = robot.orientation().getRight();
            break;
        case Left:
            dir = robot.orientation().getLeft();
            break;
        default:
            break;
        }

        edge2 = robot.position().fnAdd(dir.toVector2().fnMultiply(2));
        edge1 = edge2.fnAdd(dir.getLeft().toVector2());
        edge3 = edge2.fnAdd(dir.getRight().toVector2());

        s1 = mapViewer.checkExploredState(edge1);
        s2 = mapViewer.checkExploredState(edge2);
        s3 = mapViewer.checkExploredState(edge3);

        if (s1 == 1 && s2 == 1 && s3 == 1) {
            return Know.Yes;
        } else if (s1 == 2 || s2 == 2 || s3 == 2) // got obstacle
        {
            return Know.No;
        } else {
            return Know.Unsure;
        }

    }
    
    public static LinkedList<Vector2> findScannableReachableFromGoal(Vector2 position, Robot robot , MapViewer mapViewer) {

        LinkedList<Vector2> reachable = new LinkedList<Vector2>();
        AStarSolver astarSolver = new AStarSolver();
        AStarSolverResult astarSolverResult;

        if (mapViewer.checkValidBoundary(position) && mapViewer.getObstacleState(position) == WPObstacleState.IsWalkable) {
            astarSolverResult = astarSolver.solve(mapViewer.getSubjectiveMap(), robot, position);

            if (!astarSolverResult.shortestPath.isEmpty()) {
                reachable.add(position);
            }
        }

        LinkedList<Vector2> list;
        int num;

        boolean leftBlocked, rightBlocked, upBlocked, downBlocked;
        leftBlocked = false;
        rightBlocked = false;
        upBlocked = false;
        downBlocked = false;

        int i = 1;
        while (i <= 3) {
            Vector2 up = position.fnAdd(new Vector2(-i, 0));
            Vector2 down = position.fnAdd(new Vector2(i, 0));
            Vector2 left = position.fnAdd(new Vector2(0, -i));
            Vector2 right = position.fnAdd(new Vector2(0, i));

            if (!mapViewer.checkValidBoundary(up) || mapViewer.getObstacleState(up)  == WPObstacleState.IsActualObstacle) {
                upBlocked = true;
            }

            if (!mapViewer.checkValidBoundary(down)
                    || mapViewer.getObstacleState(down)  == WPObstacleState.IsActualObstacle) {
                downBlocked = true;
            }

            if (!mapViewer.checkValidBoundary(left)
                    || mapViewer.getObstacleState(left)  == WPObstacleState.IsActualObstacle) {
                leftBlocked = true;
            }
            if (!mapViewer.checkValidBoundary(right)
                    || mapViewer.getObstacleState(right)  == WPObstacleState.IsActualObstacle) {
                rightBlocked = true;
            }

            list = identifyWalkableAround(i, position, upBlocked, downBlocked, leftBlocked, rightBlocked , mapViewer);

            num = 0;

            for (num = 0; num < list.size(); num++) {
                astarSolverResult = astarSolver.solve(mapViewer.getSubjectiveMap(), robot, list.get(num));
                if (!astarSolverResult.shortestPath.isEmpty())
                    reachable.add(new Vector2(list.get(num).i(), list.get(num).j()));

            }
            list.clear();
            i++;
        }
        ;

        return reachable;

    }
    
    public static LinkedList<Vector2> identifyWalkableAround(int width, Vector2 center, boolean upBlocked,
            boolean downBlocked, boolean leftBlocked, boolean rightBlocked , MapViewer mapViewer) {
        Vector2 up = center.fnAdd(new Vector2(-width, 0));
        Vector2 down = center.fnAdd(new Vector2(width, 0));
        Vector2 left = center.fnAdd(new Vector2(0, -width));
        Vector2 right = center.fnAdd(new Vector2(0, width));

        LinkedList<Vector2> traversingList = new LinkedList<Vector2>();

        if (!upBlocked) {
            addWalkableToList(traversingList, up, mapViewer);
            addWalkableToList(traversingList, up.fnAdd(new Vector2(0, 1)) , mapViewer);
            addWalkableToList(traversingList, up.fnAdd(new Vector2(0, -1)),mapViewer);
        }

        if (!downBlocked) {
            addWalkableToList(traversingList, down , mapViewer);
            addWalkableToList(traversingList, down.fnAdd(new Vector2(0, 1)) ,mapViewer);
            addWalkableToList(traversingList, down.fnAdd(new Vector2(0, -1)),mapViewer);
        }

        if (!leftBlocked) {
            addWalkableToList(traversingList, left,mapViewer);
            addWalkableToList(traversingList, left.fnAdd(new Vector2(-1, 0)),mapViewer);
            addWalkableToList(traversingList, left.fnAdd(new Vector2(1, 0)),mapViewer);
        }

        if (!rightBlocked) {
            addWalkableToList(traversingList, right,mapViewer);
            addWalkableToList(traversingList, right.fnAdd(new Vector2(1, 0)),mapViewer);
            addWalkableToList(traversingList, right.fnAdd(new Vector2(-1, 0)),mapViewer);
        }

        // continue

        return traversingList; // the special vector marks no vector found

    }

    public static void addWalkableToList(LinkedList<Vector2> traversingList, Vector2 v, MapViewer mapViewer) {
        if (mapViewer.checkValidExploredRange(v)) {
            if (mapViewer.getObstacleState(v) == WPObstacleState.IsWalkable)
                traversingList.add(v);
        }
    }
    
    
    public static boolean checkIfRightFrontDangerous(Robot _robot , MapViewer mapViewer) {
        Vector2 left_m, left_f, left_b, right_up, right_down, right_middle, front_m, front_l, front_r;

        int right, front;
        
        front=0;right=0;

        right_up = _robot.position().fnAdd(_robot.orientation().toVector2())
                .fnAdd(_robot.orientation().getRight().toVector2().fnMultiply(2));
        right_down = _robot.position().fnAdd(_robot.orientation().getBehind().toVector2())
                .fnAdd(_robot.orientation().getRight().toVector2().fnMultiply(2));
        right_middle = _robot.position().fnAdd(_robot.orientation().getRight().toVector2().fnMultiply(2));

        front_m = _robot.position().fnAdd(_robot.orientation().toVector2().fnMultiply(2));
        front_l = front_m.fnAdd(_robot.orientation().getLeft().toVector2());
        front_r = front_m.fnAdd(_robot.orientation().getRight().toVector2());


        if (!mapViewer.checkValidBoundary(right_up)
                || mapViewer.checkExploredState(right_up) ==2) {
            right++;
        }

        if (!mapViewer.checkValidBoundary(right_down)
                || mapViewer.checkExploredState(right_down) ==2) {
            right++;
        }
        if (!mapViewer.checkValidBoundary(right_middle)
                ||  mapViewer.checkExploredState(right_middle) ==2) {
            right++;
        }

        if (!mapViewer.checkValidBoundary(front_m)
                || mapViewer.checkExploredState(front_m) ==2) {
            front++;
        }

        if (!mapViewer.checkValidBoundary(front_l)
                || mapViewer.checkExploredState(front_l) ==2) {
            front++;
        }
        if (!mapViewer.checkValidBoundary(front_r)
                || mapViewer.checkExploredState(front_r) ==2) {
            front++;
        }

        if (front <= 1 && right <= 1 )
            return true;
        else
            return false;
    }

    public static boolean checkIfLeftToCalibrate(Robot _robot, MapViewer mapViewer) {
        // TODO Auto-generated method stub
        Vector2 left_m, left_f, left_b;

        int left;
        left = 0;


        left_m = _robot.position().fnAdd(_robot.orientation().getLeft().toVector2().fnMultiply(2));
        left_f = left_m.fnAdd(_robot.orientation().toVector2());
        left_b = left_m.fnAdd(_robot.orientation().getBehind().toVector2());
        
        
        if (!mapViewer.checkValidBoundary(left_m)
                || mapViewer.checkExploredState(left_m) ==2) {
            left++;
        }

        if (!mapViewer.checkValidBoundary(left_f)
                || mapViewer.checkExploredState(left_f) ==2) {
            left++;
        }
        if (!mapViewer.checkValidBoundary(left_b)
                || mapViewer.checkExploredState(left_b) ==2) {
            left++;
        }
        
        if(left >=2)
            return true;
        else
            return false;
        
        
    }
    
    
    public static boolean checkIfInDangerousZone(Robot _robot, MapViewer mapViewer) throws InterruptedException, IOException {

        Vector2 left_m, left_f, left_b, right_up, right_down, right_middle, front_m, front_l, front_r;

        int left, right, front;
        left = 0;
        right = 0;
        front = 0;

        left_m = _robot.position().fnAdd(_robot.orientation().getLeft().toVector2().fnMultiply(2));
        left_f = left_m.fnAdd(_robot.orientation().toVector2());
        left_b = left_m.fnAdd(_robot.orientation().getBehind().toVector2());

        right_up = _robot.position().fnAdd(_robot.orientation().toVector2())
                .fnAdd(_robot.orientation().getRight().toVector2().fnMultiply(2));
        right_down = _robot.position().fnAdd(_robot.orientation().getBehind().toVector2())
                .fnAdd(_robot.orientation().getRight().toVector2().fnMultiply(2));
        right_middle = _robot.position().fnAdd(_robot.orientation().getRight().toVector2().fnMultiply(2));

        front_m = _robot.position().fnAdd(_robot.orientation().toVector2().fnMultiply(2));
        front_l = front_m.fnAdd(_robot.orientation().getLeft().toVector2());
        front_r = front_m.fnAdd(_robot.orientation().getRight().toVector2());

        if (!mapViewer.checkValidBoundary(left_m)
                || mapViewer.getObstacleState(left_m) == WPObstacleState.IsActualObstacle) {
            left++;
        }

        if (!mapViewer.checkValidBoundary(left_f)
                || mapViewer.getObstacleState(left_f) == WPObstacleState.IsActualObstacle) {
            left++;
        }
        if (!mapViewer.checkValidBoundary(left_b)
                || mapViewer.getObstacleState(left_b) == WPObstacleState.IsActualObstacle) {
            left++;
        }

        if (!mapViewer.checkValidBoundary(right_up)
                || mapViewer.getObstacleState(right_up) == WPObstacleState.IsActualObstacle) {
            right++;
        }

        if (!mapViewer.checkValidBoundary(right_down)
                || mapViewer.getObstacleState(right_down) == WPObstacleState.IsActualObstacle) {
            right++;
        }
        if (!mapViewer.checkValidBoundary(right_middle)
                || mapViewer.getObstacleState(right_middle) == WPObstacleState.IsActualObstacle) {
            right++;
        }

        if (!mapViewer.checkValidBoundary(front_m)
                || mapViewer.getObstacleState(front_m) == WPObstacleState.IsActualObstacle) {
            front++;
        }

        if (!mapViewer.checkValidBoundary(front_l)
                || mapViewer.getObstacleState(front_l)== WPObstacleState.IsActualObstacle) {
            front++;
        }
        if (!mapViewer.checkValidBoundary(front_r)
                || mapViewer.getObstacleState(front_r)== WPObstacleState.IsActualObstacle) {
            front++;
        }

        if (front <= 1 && right <= 1 && left <= 1)
            return true;
        else
            return false;
    }
}
