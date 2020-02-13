package mdp.simulation.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class MainFrame extends JFrame {
    
	private static final String _FRAME_NAME = "MDP Group 10 - Simulator";
    private static final int _FRAME_WIDTH = 680;
    private static final int _FRAME_HEIGHT = 800;
    
    private MainPanel _mainPanel;

    public MainPanel getMainPanel() {
        return _mainPanel;
    }
    
    public MainFrame() {
        // config
        this.setTitle(_FRAME_NAME);
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(new Dimension((int) screenSize.getWidth()/2, (int) screenSize.getHeight()));
        // children
        _mainPanel = new MainPanel();
        this.setContentPane(_mainPanel);
        this.setVisible(true);
    }
    
}
