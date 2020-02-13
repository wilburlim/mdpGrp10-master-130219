package mdp.robot;

import java.util.LinkedList;
import java.util.List;
import mdp.common.Direction;
import mdp.common.Vector2;

public enum RobotAction {
    MoveForward, MoveBackward, RotateLeft, RotateRight;
    
    public static LinkedList<RobotAction> fromPath(Robot robot, List<Vector2> path) {
        LinkedList<RobotAction> result = new LinkedList<>();
        
        Vector2 curPos = robot.position();
        Robot tempRobot = new Robot(
            new Vector2(robot.position().i(), robot.position().j()),
            robot.orientation()
        );
        for (Vector2 curPathPos : path) {
            Vector2 diff = curPathPos.fnAdd(curPos.fnMultiply(-1));
            for (Direction dir : Direction.values()) {
                // check next pos direction
                if (diff.equals(dir.toVector2())) {
                    // check orientation
                    if (diff.equals(tempRobot.orientation().getLeft().toVector2())) {
                        tempRobot.execute(RotateLeft);
                        result.add(RotateLeft);
                    } else if (diff.equals(tempRobot.orientation().getRight().toVector2())) {
                        tempRobot.execute(RotateRight);
                        result.add(RotateRight);
                    } else if (diff.equals(tempRobot.orientation().getBehind().toVector2())) {
                        tempRobot.execute(RotateLeft);
                        tempRobot.execute(RotateLeft);
                        result.add(RotateLeft);
                        result.add(RotateLeft);
                    }
                    tempRobot.execute(MoveForward);
                    result.add(MoveForward);
                }
            }
            curPos = curPathPos;
        }
        
        return result;        
    }
}