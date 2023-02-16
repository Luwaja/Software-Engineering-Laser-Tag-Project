import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;

public class LaserTag {
    
    public static void main(String[] args)
    {
        // Creating frame
        JFrame frame = new JFrame("LASER TAG");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.BLACK);
        
        // Creating image
        ImageIcon imageIcon = new ImageIcon("");
        JLabel imageLabel = new JLabel(imageIcon);
        frame.add(imageLabel);
    }

}
