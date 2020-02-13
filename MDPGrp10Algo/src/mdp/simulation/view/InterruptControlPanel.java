package mdp.simulation.view;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class InterruptControlPanel extends JPanel {
    
    private static final Color _BG_COLOR = new Color(128, 128, 128);
    
//    private JButton _stopBtn;
//    private JButton _resetBtn;
//    private JButton _restartBtn;
    private JTextField _termCoverageText;
    private JTextField _termTimeText;
    private JCheckBox _termRoundCheckbox;
    
    public InterruptControlPanel() {
        // config
        this.setBackground(_BG_COLOR);
        
        // children
//        _stopBtn = new JButton("Stop");
//        _resetBtn = new JButton("Reset");
//        _restartBtn = new JButton("Restart");
        _termCoverageText = new JTextField("100", 3);
        _termTimeText = new JTextField("0", 3);
        _termRoundCheckbox = new JCheckBox("Terminate after 1st round");
        JLabel termLabel = new JLabel("Terminate after: ");
        JLabel coverageLabel = new JLabel("% or");
        JLabel timeLabel = new JLabel("seconds");
        termLabel.setForeground(Color.WHITE);
        coverageLabel.setForeground(Color.WHITE);
        timeLabel.setForeground(Color.WHITE);
//        this.add(_stopBtn);
//        this.add(_resetBtn);
        this.add(termLabel);
        this.add(_termCoverageText);
        this.add(coverageLabel);
        this.add(_termTimeText);
        this.add(timeLabel);
//        this.add(_restartBtn);
        this.add(_termRoundCheckbox);
    }

//    public JButton getStopBtn() {
//        return _stopBtn;
//    }
//
//    public JButton getResetBtn() {
//        return _resetBtn;
//    }

//    public JButton getRestartBtn() {
//        return _restartBtn;
//    }

    public JTextField getTermCoverageText() {
        return _termCoverageText;
    }

    public JTextField getTermTimeText() {
        return _termTimeText;
    }

    public JCheckBox getTermRoundCheckbox() {
        return _termRoundCheckbox;
    }
    
}
