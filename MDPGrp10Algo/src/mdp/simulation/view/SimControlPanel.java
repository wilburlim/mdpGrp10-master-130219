package mdp.simulation.view;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import mdp.Main;

public class SimControlPanel extends JPanel {
    
    private static final Color _BG_COLOR = new Color(128, 128, 128);
    
    private JCheckBox _simCheckBox;
    private JButton _connectBtn;
    private JTextField _wayPoint;

    public SimControlPanel() {
        // config
        this.setBackground(_BG_COLOR);
        
        // children
        _simCheckBox = new JCheckBox("Simulation", Main.isSimulating());
        _connectBtn = new JButton("Connect to RPi");
        _wayPoint = new JTextField("11,11", 5);
        this.add(_simCheckBox);
        this.add(_connectBtn);
        this.add(_wayPoint);
        JLabel wayPointLabel = new JLabel("waypoint");
        wayPointLabel.setForeground(Color.WHITE);
        this.add(wayPointLabel);
    }

    public JCheckBox getSimCheckBox() {
        return _simCheckBox;
    }

    public JButton getConnectBtn() {
        return _connectBtn;
    }
    public JTextField getWayPoint() {
        return _wayPoint;
    }
    
    
}
