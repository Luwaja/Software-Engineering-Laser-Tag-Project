import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

public class LaserTag {
    
    public static void main(String[] args) throws InterruptedException
    {
        // Splash screen ------------------------------------
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        // Creating image
        Image originalImage = Toolkit.getDefaultToolkit().getImage("PhotonLogo.jpg");
        // Scale down image
        Image scaledImage = originalImage.getScaledInstance(662,338,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        frame.add(imageLabel);

        // Creating window splashscreen
        JWindow splashScreen = new JWindow();
        splashScreen.add(imageLabel); // Upload image
        splashScreen.pack();
        splashScreen.setLocationRelativeTo(null);
        // Sizing and centering the screen
        splashScreen.setSize(662,338); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - splashScreen.getWidth())/2;
        int centerY = (screenSize.height - splashScreen.getHeight())/2;
        splashScreen.setLocation(centerX,centerY);
        // Open screen, display for 3 seconds, close screen
        splashScreen.setVisible(true);
        Thread.sleep(3000); //3 secs
        splashScreen.setVisible(false);

        // Application starts here -----------------------------
        // Loading JFrame window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.BLACK);
        
    }
}
