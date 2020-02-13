package mdp.simulation.view;

import java.awt.Color;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
    
    private static final Color _BG_COLOR = new Color(128, 128, 128); //background colour
    
    private GridContainer _gridContainer;

    public GridContainer getGridContainer() {
        return _gridContainer;
    }

    public GridPanel() {
        // config
        this.setBackground(_BG_COLOR);
        
        // children
        _gridContainer = new GridContainer();
        this.add(_gridContainer);
    }  
    
}
