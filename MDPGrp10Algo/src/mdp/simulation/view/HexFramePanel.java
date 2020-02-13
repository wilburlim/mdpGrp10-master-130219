package mdp.simulation.view;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class HexFramePanel extends JPanel {
    
    private JTextArea _hexTextArea;

    public JTextArea getHexTextArea() {
        return _hexTextArea;
    }
    
    public HexFramePanel() {
        // config
        
        // children
        _hexTextArea = new JTextArea("Hex descriptor populated there", 10, 50);
        this.add(_hexTextArea);
    }
    
}
