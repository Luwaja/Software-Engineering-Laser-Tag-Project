// SWING imports
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.JTextField;
import javax.swing.Box;
import javax.swing.BoxLayout;
// AWT imports
import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaserTag
{
    
    public static void main(String[] args) throws InterruptedException
    {
        // Splash screen ---------------------------------------
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        
        //Open splash screen for 3 seconds and close
        createSplashScreen(frame).setVisible(true);
        Thread.sleep(3000); //3 secs 
        createSplashScreen(frame).setVisible(false);

        // Application starts here ------------------------------
        // Loading JFrame window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Vertical box to hold horizontal box
        Box vbox1 = Box.createVerticalBox();
        Box vbox2 = Box.createVerticalBox();
        
        String[] redTeamPlayerNames = new String[20];
        String[] greenTeamPlayerNames = new String[20];
        // Add boxes for each teams' 19 players
        for (int i = 0; i < 20; i++)
        {
            redTeamPlayerNames[i] =  addHorizontalBox(vbox1, "Red: Player " + i).getText();
            greenTeamPlayerNames[i] = addHorizontalBox(vbox2, "Green: Player " + i).getText();
        }
        
        // Horizontal box to hold the columns
        Box hbox = Box.createHorizontalBox();
        hbox.add(vbox1);
        hbox.add(Box.createHorizontalStrut(10));
        hbox.add(vbox2);

        // Set the layout manager of the content pane to a BoxLayout
        Container contentPane = frame.getContentPane();
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        contentPane.setLayout(layout);

        // Add the boxes to the content pane
        contentPane.add(hbox);

        // Output
        // Output redTeamPlayerNames to the terminal
        System.out.println("Red Team Player Names:");
        for (int i = 0; i < 20; i++) {
            System.out.println(redTeamPlayerNames[i]);
}

        frame.setVisible(true);
    }

    // Method to create splash screen
    public static JWindow createSplashScreen(JFrame frame) throws InterruptedException
    {
        // Creating image
        Image originalImage = Toolkit.getDefaultToolkit().getImage("PhotonLogo.jpg");
        
        // Scale down image
        Image scaledImage = originalImage.getScaledInstance(660,308,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        frame.add(imageLabel);

        // Creating window splashscreen
        JWindow splashScreen = new JWindow();
        splashScreen.add(imageLabel); // Upload image
        splashScreen.pack();
        splashScreen.setLocationRelativeTo(null);
        
        // Sizing and centering the screen
        splashScreen.setSize(660,308); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - splashScreen.getWidth())/2;
        int centerY = (screenSize.height - splashScreen.getHeight())/2;
        splashScreen.setLocation(centerX,centerY);
    
        return splashScreen;
    }

    // Method to create and add a horizontal box to a vertical box
    private static JTextField addHorizontalBox(Box vbox, String labelText)
    {
        Box hbox = Box.createHorizontalBox();
        hbox.add(new JLabel(labelText));
        hbox.add(Box.createHorizontalStrut(10));
        JTextField textField = new JTextField(10);
        hbox.add(textField);
        vbox.add(hbox);
        return textField; 
    }
}