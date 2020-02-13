package mdp.simulation.view;

import java.awt.Dimension;
import javax.swing.JFrame;

public class HexFrame extends JFrame {
        
    private static final String _FRAME_NAME = "Hex Map Descriptor";
    private static final int _FRAME_WIDTH = 600;
    private static final int _FRAME_HEIGHT = 210;
    
    private HexFramePanel _hexFramePanel;
    
    public HexFramePanel getHexFramePanel() {
        return _hexFramePanel;
    }
    
    public HexFrame() {
        // config
        this.setTitle(_FRAME_NAME);
        this.setSize(new Dimension(_FRAME_WIDTH, _FRAME_HEIGHT));
        
        // children
        _hexFramePanel = new HexFramePanel();
        
        this.setContentPane(_hexFramePanel);
        this.setVisible(true);
    }
}
