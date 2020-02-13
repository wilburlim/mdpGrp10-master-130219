package mdp.solver.shortestpath;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import mdp.common.Direction;
import mdp.robot.Robot;
import mdp.common.Vector2;
import mdp.map.Map;
import mdp.map.WPObstacleState;
import mdp.map.WPSpecialState;
import mdp.robot.RobotAction;

public class AStarUtil {

    private static final int _SCAN_FACTOR = 50;
    private static final float _SCAN_EXPANSION = 0.3f;
    
    public static float calDistance(List<Vector2> path) {
        float result = 0;
        Vector2 prev = null;
        for (Vector2 cur : path) {
            if (prev != null) {
                Vector2 diff = cur.fnAdd(prev.fnMultiply(-1));
                result += Math.sqrt(Math.pow(diff.i(), 2) + Math.pow(diff.j(), 2));
            }
            prev = cur;
        }
        return result;
    }

    public static int countTurn(List<Vector2> path) {
        LinkedList<RobotAction> actions = RobotAction.fromPath(new Robot(), path);
        return (int) actions.stream()
                .filter(a -> a.equals(RobotAction.RotateLeft) || a.equals(RobotAction.RotateRight))
                .count();
    }

    public static int countTurnSmooth(List<Vector2> smoothPath) {
        return smoothPath.size() - 1;
    }

    public static int getMDistance(Vector2 point1, Vector2 point2) {
        return Math.abs(point1.i() - point2.i())
                + Math.abs(point1.j() - point2.j());
//    	return 1;
    }

    public static int getMoveCost(Direction origin, Direction direction) {
//    	System.out.println(origin + " vs " + direction);
//    	System.out.println(direction == origin);
        if (direction == origin) {
            return 1;
        } else if (direction == origin.getBehind()) {
            return 3;
        } else {
            return 2;
        }
    }

    public static int getSmoothMoveCost(Direction origin, Direction direction) {
        if (direction == origin) {
//            return 3;
            return 1;
        } else if (direction == origin.getBehind()) {
//            return 2;
            return 3;
        } else {
//            return 2;
            return 2;
        }
    }

    public static int getSafetyBenefit(Map map, Vector2 curPos) {
        int result = 0;
        for (Direction dir : Direction.values()) {
            Vector2 adjPos = curPos.fnAdd(dir.toVector2());
            if (map.checkValidPosition(adjPos)) {
                if (!map.getPoint(adjPos).obstacleState()
                        .equals(WPObstacleState.IsWalkable)) {
                    result -= 1;
                }
            }
        }
        return result;
    }

    public static List<Vector2> smoothenPath(Map map, List<Vector2> path, boolean fromEnd) {
        List<Vector2> smoothPath1 = smoothenLv1(map, path);
        List<Vector2> smoothPath2 = smoothenLv2(map, smoothPath1);
        return smoothPath2;
    }

    private static List<Vector2> smoothenLv1(Map map, List<Vector2> path) {
        List<Vector2> result = new ArrayList<>();
        Vector2 lastSavedPos = new Robot().position();
        Vector2 lastSavedTurnPos = null;
        Vector2 prevPos = null;
        result.add(lastSavedPos);

        for (Vector2 curPos : path) {// check existence of obstacle in rectangular area

            int minI = Integer.min(lastSavedPos.i(), curPos.i());
            int maxI = Integer.max(lastSavedPos.i(), curPos.i());
            int minJ = Integer.min(lastSavedPos.j(), curPos.j());
            int maxJ = Integer.max(lastSavedPos.j(), curPos.j());
            boolean actualObsDetected = false;
            boolean virtualObsDetected = false;
            for (int i = minI; i <= maxI; i++) {
                for (int j = minJ; j <= maxJ; j++) {
                    WPObstacleState curObsState = map
                            .getPoint(new Vector2(i, j))
                            .obstacleState();
                    if (!curObsState.equals(WPObstacleState.IsWalkable)) {
                        actualObsDetected = true;
                        break;
                    } else if (curObsState.equals(WPObstacleState.IsVirtualObstacle)) {
                        virtualObsDetected = true;
                    }
                }
                if (actualObsDetected) {
                    break;
                }
            }

            // save if cur doesn't lie on the same line with last saved pos
            if (!(curPos.i() == lastSavedPos.i())
                    && !(curPos.j() == lastSavedPos.j())
                    && lastSavedTurnPos == null) {
                lastSavedTurnPos = curPos;
            }

            // if there is, save the prev pos
            if (actualObsDetected) {
            	if(prevPos != null) {
            		// check if prevPos & lastSavedPos are on the same row/col
            		if ((prevPos.i() == lastSavedPos.i()
            				|| prevPos.j() == lastSavedPos.j())
            				&& virtualObsDetected) {
            			result.add(lastSavedTurnPos);
            		}
            		result.add(prevPos);
            		lastSavedPos = prevPos;
            		lastSavedTurnPos = null;
            	}
            }
            prevPos = curPos;

        }
        // save goal
        result.add(prevPos);

        return result;
    }

    private static List<Vector2> smoothenLv2(Map map, List<Vector2> path) {
        List<Vector2> result = new ArrayList<>();

//        List<Vector2> scanned = new ArrayList<>();
        map.highlight(path, WPSpecialState.IsClosedPoint);

        Vector2 prevPos = null;
        Vector2 prevTurnPos = null;
        for (Vector2 curPos : path) {
            if (prevPos == null) {
                prevPos = curPos;
                prevTurnPos = curPos;
                result.add(curPos);
                continue;
            }

            boolean isCrashing = false;
            for (int offI = -1; offI <= 1; offI += 2) {
                for (int offJ = -1; offJ <= 1; offJ += 2) {
                    Vector2 offPrev = prevPos.fnAdd(new Vector2(offI, offJ));
                    Vector2 offCur = curPos.fnAdd(new Vector2(offI, offJ));
                    Vector2 offDiff = offCur.fnAdd(offPrev.fnMultiply(-1));

                    int steps = Math.max(
                            Math.abs(offDiff.i()),
                            Math.abs(offDiff.j())
                    ) * _SCAN_FACTOR;

                    float incI = offDiff.i() / (float) steps;
                    float incJ = offDiff.j() / (float) steps;

                    float tempI = offPrev.i() + offI * _SCAN_EXPANSION;
                    float tempJ = offPrev.j() + offJ * _SCAN_EXPANSION;

                    for (int v = 0; v < steps; v++) {
                        tempI += incI;
                        tempJ += incJ;
                        Vector2 newPoint = new Vector2(Math.round(tempI), Math.round(tempJ));
                        if (map.checkValidBoundary(newPoint)
                                && map.getPoint(newPoint).obstacleState()
                                        .equals(WPObstacleState.IsActualObstacle)) {
                            isCrashing = true;
                            break;
                        }

//                        if (!scanned.contains(newPoint)) scanned.add(newPoint);
                    }
                    if (isCrashing) {
                        break;
                    }
                }
                if (isCrashing) {
                    break;
                }
            }
            if (isCrashing) {
                result.add(prevTurnPos);
                prevPos = prevTurnPos;
            }
            prevTurnPos = curPos;
        }

        result.add(path.get(path.size() - 1));

//        map.highlight(scanned, WPSpecialState.IsOpenedPoint);
        return result;
    }

}
