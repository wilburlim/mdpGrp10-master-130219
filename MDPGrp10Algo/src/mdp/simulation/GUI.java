package mdp.simulation;

import mdp.simulation.view.MainFrame;
import mdp.simulation.event.EventHandler;
import mdp.map.Map;
import mdp.robot.Robot;
import mdp.simulation.event.GUIClickEvent;

public class GUI implements IGUIControllable {
    
    private Map _map;
    private Robot _robot;
    
    private MainFrame _mainFrame;
    private EventHandler _eventHandler;

    public GUI() {
        _mainFrame = new MainFrame();
        _eventHandler = new EventHandler(this);
    }

    @Override
    public Map getMap() {
        return _map;
    }

    @Override
    public Robot getRobot() {
        return _robot;
    }

    @Override
    public MainFrame getMainFrame() {
        return _mainFrame;
    }
    
    @Override
    public void reset() {
        this.update(new Map(), new Robot());
    }
    
    @Override
    public void update(Map map, Robot robot) {
        _map = map;
        _robot = robot;
        _mainFrame
            .getMainPanel()
            .getGridPanel()
            .getGridContainer().fillGrid(_map, _robot);
        _mainFrame.revalidate();
    }
    @Override
    public void update(Map map) {
        update(map, _robot);
    }
    @Override
    public void update(Robot robot) {
        update(_map, robot);
    }
    
//    @Override
//    public void trigger(ManualTrigger trigger) {
//        RunControlPanel runCtrlPanel = _mainFrame.getMainPanel().getRunCtrlPanel();
//        InterruptControlPanel intrtCtrlPanel = _mainFrame.getMainPanel().getIntrCtrlPanel();
//        switch (trigger) {
//            case Exploration:
//                runCtrlPanel.getExplorationBtn().doClick();
//                break;
//            case ShortestPath:
//                runCtrlPanel.getShortestPathBtn().doClick();
//                break;
//            case Combined:
//                runCtrlPanel.getCombinedBtn().doClick();
//                break;
////            case Stop:
////                intrtCtrlPanel.getStopBtn().doClick();
////                break;
//        }
//    }

    @Override
    public void trigger(GUIClickEvent hdlr) {
        _eventHandler.resolveHandler(hdlr, null);
    }

    @Override
    public boolean isSingleRoundRun() {
        return _mainFrame
                .getMainPanel()
                .getIntrCtrlPanel()
                .getTermRoundCheckbox().isSelected();
    }
    
}