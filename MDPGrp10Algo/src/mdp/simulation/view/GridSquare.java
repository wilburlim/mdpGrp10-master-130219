package mdp.simulation.view;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mdp.common.Vector2;

public class GridSquare extends JPanel {
    
    private Vector2 _position;

    public GridSquare(Vector2 position) {
        _position = position;
        if(position.i()==12&&position.j()==19) {
        	 JLabel label = new JLabel(" ");
             label.setForeground(Color.white);
             label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
             this.add(label); 
        }
        else if(position.i()==13&&position.j()==19){
        	JLabel label = new JLabel(" ");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
            this.add(label); 
        }
        else if(position.i()==14&&position.j()==19){
        	JLabel label = new JLabel(" ");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
            this.add(label); 
        }
        else if(position.i()==12&&position.j()==18){
        	JLabel label = new JLabel("G");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            this.add(label); 
        }
        else if(position.i()==13&&position.j()==18){
        	JLabel label = new JLabel("O  A");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            this.add(label); 
        }
        else if(position.i()==14&&position.j()==18){
        	JLabel label = new JLabel("L");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 8));
            this.add(label); 
        }
        else if(position.i()==12&&position.j()==17){
        	JLabel label = new JLabel(" ");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
            this.add(label); 
        }
        else if(position.i()==13&&position.j()==17){
        	JLabel label = new JLabel(" ");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
            this.add(label); 
        }
        else if(position.i()==14&&position.j()==17){
        	JLabel label = new JLabel(" ");
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
            this.add(label); 
        }
        else {
        	JLabel label = new JLabel(_position.i() + ", " + _position.j());
            label.setForeground(Color.white);
            label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 7));
            this.add(label); 
        }
        
    }

    public Vector2 position() {
        return _position;
    }
    
    public void toggleBackground() {
        if (this.getBackground().equals(ColorConfig.OBSTACLE)) {
            this.setBackground(ColorConfig.NORMAL);
        } else {
            this.setBackground(ColorConfig.OBSTACLE);
        }
    }
    
}