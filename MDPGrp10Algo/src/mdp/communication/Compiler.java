package mdp.communication;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import java.util.List;
import mdp.common.Vector2;
import mdp.map.Descriptor;
import mdp.map.Map;
import mdp.robot.Robot;
import mdp.robot.RobotAction;
import mdp.solver.exploration.CalibrationType;

public class Compiler {

    private static final String _MOVE_FORWARD = "f";
    private static final String _MOVE_BACKWARD = "b";
    private static final String _ROTATE_LEFT = "l";
    private static final String _ROTATE_RIGHT = "r";

    private static final String _CAL_FRONT_LR = "x";
    private static final String _CAL_FRONT_ML = "x";
    private static final String _CAL_FRONT_MR = "x";
    private static final String _CAL_RIGHT = "c";
    private static final String _CAL_LEFT = "z";
    private static final String _CAL_EMERGENCY = "e";
    
    
    private static final String _CAL_LEFT_SPECIAL = "q";
    private static final String _SENSING_REQUEST = "s";
    private static final String _TRAILER = "|";

    private static final String _DESC_SEPARATOR = "g";

    private static final String _DECIMAL_FORMAT = "###.###";

    private static String _roundToString(double target) {
        return new DecimalFormat(_DECIMAL_FORMAT).format(target);
    }
    
    static String compileArbitrary(String action) {
        return action + _TRAILER;
    }

    static String compileActions(List<RobotAction> actions, String mode) {
        String result = "";
        result += !mode.isEmpty() ? mode + _TRAILER : "";
        int count = 1;
        String lastAction = "";
        for (RobotAction action : actions) {
            String nextActionStr;
            switch (action) {
                case MoveForward:
                    nextActionStr = _MOVE_FORWARD;
                    break;
                case MoveBackward:
                    nextActionStr = _MOVE_BACKWARD;
                    break;
                case RotateLeft:
                    nextActionStr = _ROTATE_LEFT;
                    break;
                case RotateRight:
                    nextActionStr = _ROTATE_RIGHT;
                    break;
                default:
                    nextActionStr = " ";
                    break;
            }
            if (result.length() != 0) {
                if (lastAction.equals(nextActionStr)) {
                    boolean isRotating = lastAction.equals(_ROTATE_LEFT) || lastAction.equals(_ROTATE_RIGHT);
                    if (isRotating) {
                        result += _TRAILER + lastAction;
                    } else {
                        count++;
                    }
                } else {
                    boolean isRotating = lastAction.equals(_ROTATE_LEFT) || lastAction.equals(_ROTATE_RIGHT);
                    result += (isRotating ? "" : count) + _TRAILER + nextActionStr;
                    count = 1;
                }
            } else {
                result += nextActionStr;
            }
            lastAction = nextActionStr;
        }
        boolean isRotating;
        if (result.length() != 1) {
            isRotating = lastAction.equals(_ROTATE_LEFT) || lastAction.equals(_ROTATE_RIGHT);
        } else {
            isRotating = result.equals(_ROTATE_LEFT) || result.equals(_ROTATE_RIGHT);
        }

        result += (isRotating ? "" : count) + _TRAILER;
        System.out.println("Sending out: " + result);
        
        return result;
    }

    static String compileSmoothActions(List<Vector2> smoothPath, String mode) {
        String result = "";
        result += !mode.isEmpty() ? mode + _TRAILER : "";
        double orientation;
        switch (new Robot().orientation()) {
            case Up:
                orientation = 90;
                break;
            case Down:
                orientation = 270;
                break;
            case Left:
                orientation = 180;
                break;
            case Right:
            default:
                orientation = 0;
                break;
        }
        for (int i = 0; i < smoothPath.size() - 1; i++) {
            // calculate vector difference
            Vector2 posDiff = smoothPath.get(i + 1).fnAdd(smoothPath.get(i).fnMultiply(-1));
            
            // calculate rotation
            double alpha = Math.toDegrees(
                    Math.atan(((double) posDiff.i()) / ((double) posDiff.j()))
            );
            double angle = alpha + (posDiff.j() < 0 ? 90 : 0);
            double rotation = Math.abs(angle - orientation);
//            System.out.println("diff = " + ());
//            System.out.println("posDiff = " + posDiff);
//            System.out.println("angle = " + angle);
//            System.out.println("orientation = " + orientation);
            String rotateDirection = angle - orientation > 0 ? "r" : "l";
            String roundedAngle = _roundToString(rotation);
            String rotationStr;
            if (!"0".equals(roundedAngle)) {
                rotationStr = rotateDirection + _roundToString(rotation * 0.99f) + _TRAILER;
            } else {
                rotationStr = "";
            }
            
            // calculate distance to travel
            double distance = Math.sqrt(Math.pow(posDiff.i(), 2) + Math.pow(posDiff.j(), 2));
            String distanceStr = "f" + _roundToString(distance * 0.99f) + _TRAILER;
//            String distanceStr = "f" + _roundToString(distance) + _TRAILER;
            
            // wrapping up
            result += rotationStr + distanceStr;
            orientation = angle;
        }
        return result;
    }

    static String compileMap(Map map, int[][] explored) {
        String strResult = Descriptor.stringify(map, explored);
        String[] hexDesc = Descriptor.toHex(strResult);
        return hexDesc[0] + _DESC_SEPARATOR + hexDesc[1];
    }

    static String compileCalibration(CalibrationType calType) {
        switch (calType) {
            case Right:
                return _CAL_RIGHT + _TRAILER;
            case Front_LR:
                return _CAL_FRONT_LR + _TRAILER;
            case Front_ML:
                return _CAL_FRONT_ML + _TRAILER;
            case Front_MR:
                return _CAL_FRONT_MR + _TRAILER;
            case LeftSpecial:
                return _CAL_LEFT_SPECIAL + _TRAILER;
            case Left:
                return _CAL_LEFT + _TRAILER;
            case Emergency:
                return _CAL_EMERGENCY + _TRAILER;
            default:
                return "";
        }
    }

    static String compileSensingRequest() {
        return _SENSING_REQUEST + _TRAILER;
    }
}
