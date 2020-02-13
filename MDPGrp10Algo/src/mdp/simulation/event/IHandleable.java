package mdp.simulation.event;

import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

public interface IHandleable {

    void resolveHandler(GUIClickEvent hdlr, MouseEvent e);

    void resolveFrameHandler(GUIWindowEvent hdlr, WindowEvent e);
    
}
