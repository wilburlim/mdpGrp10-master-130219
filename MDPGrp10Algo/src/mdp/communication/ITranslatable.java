package mdp.communication;

import java.util.LinkedList;
import java.util.List;
import mdp.common.Vector2;
import mdp.map.Map;
import mdp.robot.RobotAction;
import mdp.solver.exploration.CalibrationType;

public interface ITranslatable {

    String getInputBuffer();
    
    void connect(Runnable callback);

    void listen(Runnable handler);

    void sendInfoToAndroid(Map map, int[][] explored, LinkedList<RobotAction> actions);
    
    void sendSensingRequest();

    void sendMoveCommand(List<RobotAction> actions, String mode);
    
    void sendSmoothMoveCommand(List<Vector2> path);
    
    void sendCalibrationCommand(CalibrationType calType);
    
    void sendExplorationEndMarker();
    
}
