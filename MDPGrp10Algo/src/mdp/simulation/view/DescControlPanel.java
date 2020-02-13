package mdp.simulation.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DescControlPanel extends JPanel {
    
    private static final Color _BG_COLOR = new Color(128, 128, 128);
    
    private JTextField _filePathBtn;
    private JButton _openDescBtn;
    private JButton _saveDescBtn;
    private JButton _getHexBtn;
    private JButton _checkWebBtn;
    
    public DescControlPanel() {
        // config
        this.setBackground(_BG_COLOR);
        
        // children
        _filePathBtn = new JTextField("", 10);
        _openDescBtn = new JButton("Open");
        _saveDescBtn = new JButton("Save");
        _getHexBtn = new JButton("Get Hex");
        _filePathBtn.setHorizontalAlignment(JTextField.RIGHT);
        //_checkWebBtn = new JButton("Check @ mdpcx3004");
        JLabel pathLabel = new JLabel(".txt");
        pathLabel.setForeground(Color.WHITE);
        this.add(_filePathBtn);
        this.add(pathLabel);
        this.add(_openDescBtn);
        this.add(_saveDescBtn);
        this.add(_getHexBtn);
        //this.add(_checkWebBtn);
    }

    public JTextField getFilePathTextField() {
        return _filePathBtn;
    }

    public JButton getOpenDescBtn() {
        return _openDescBtn;
    }

    public JButton getSaveDescBtn() {
        return _saveDescBtn;
    }

    public JButton getGetHexBtn() {
        return _getHexBtn;
    }

/*    public JButton getCheckWebBtn() {
        return _checkWebBtn;
    } */
    
}
