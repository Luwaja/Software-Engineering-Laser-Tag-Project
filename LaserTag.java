// SWING imports
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
// AWT imports
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

public class LaserTag implements ActionListener 
{
    // Team player names
    private static ArrayList<Player> redPlayer = new ArrayList<Player>();
    private static ArrayList<Player> greenPlayer = new ArrayList<Player>();
    private static ArrayList<String> redPlayerIDs = new ArrayList<String>();
    private static ArrayList<String> greenPlayerIDs = new ArrayList<String>();
    // Variables
    private JTextField textField;


    // Laser Tag Constructor
    public LaserTag(JTextField textField) 
    {
        this.textField = textField;
    }

    public static void main(String[] args) throws InterruptedException
    {
        // Splash screen ---------------------------------------
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        
        //Open splash screen for 3 seconds and close
        JWindow splashScreen = createSplashScreen(frame);
        Thread.sleep(3000); //3 secs 
        splashScreen.setVisible(false);

        // Application starts here ------------------------------
        // Create frame
        createFrame(frame);

        // Vertical boxes to hold horizontal box for name and id
        Box vbox1name = Box.createVerticalBox();
        Box vbox1id = Box.createVerticalBox();
        Box vbox2name = Box.createVerticalBox();
        Box vbox2id = Box.createVerticalBox();
    
        // Add boxes for each teams' 19 players
        for (int i = 0; i < 15; i++)
        {
            addHorizontalBox(vbox1name, "Red Player " + (i+1) + ": ");
            addHorizontalBox(vbox1id, "ID: ");
            addHorizontalBox(vbox2name, "Green Player " + (i+1) + ": ");
            addHorizontalBox(vbox2id, "ID: ");
        }
        
        // Horizontal box to hold the columns
        Box hbox = Box.createHorizontalBox();
        hbox.add(vbox1name);
        hbox.add(vbox1id);
        hbox.add(Box.createHorizontalStrut(10));
        hbox.add(vbox2name);
        hbox.add(vbox2id);

        // Set the layout manager of the content pane to a BoxLayout
        Container contentPane = frame.getContentPane();
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        contentPane.setLayout(layout);

        // Create start game button
        JButton button = new JButton("[F5] Start Game");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                buttonMethod();
            }
        });
        button.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                System.out.println("1");
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    buttonMethod();
                }
            }
        });

        // Add the boxes to the content pane
        contentPane.add(hbox);
        contentPane.add(button);

        //Show frame
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
        splashScreen.setVisible(true);
    
        return splashScreen;
    }

    // Method to create frame for application
    public static void createFrame(JFrame frame) throws InterruptedException
    {
        // Loading JFrame window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.PINK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to create and add a horizontal box to a vertical box
    private static JTextField addHorizontalBox(Box vbox, String labelText)
    {
        // Create boxes
        Box hbox = Box.createHorizontalBox();
        hbox.add(new JLabel(labelText));       // Add label
        hbox.add(Box.createHorizontalStrut(10)); // Add space for label
        JTextField textField = new JTextField(10); // Add text field
        // Add action listener to each text field
        LaserTag listener = new LaserTag(textField);
        textField.addActionListener(listener);
        // Add textfield to hbox and hbox to vbox
        hbox.add(textField);
        vbox.add(hbox);
        
        return textField; 
    }

    // Method for when button is pressed
    public static void buttonMethod()
    {
        System.out.println("BUTTON METHOD");
    }

    // Method to print out array lists of players names
    public void printTeams()
    {
        // Print out red team names and IDs
        System.out.println("--------------------------------------\n");
        System.out.println(" * Red Team Player Names: ");
        for (int i = 0; i < redPlayer.size(); i++)
        {
            System.out.println("\t * Player " + i + ": " + redPlayer.get(i) );
        }
        System.out.println(" * Red Team Player IDs: ");
        for (int i = 0; i < redPlayerIDs.size(); i++)
        {
            System.out.println("\t\t -> ID: " + redPlayerIDs.get(i));
        }
        
        //Print out green team names and IDs
        System.out.println("\n--------------------------------------\n");
        System.out.println(" * Green Team Player Names: ");
        for (int i = 0; i < greenPlayer.size(); i++)
        {
            System.out.println("\t -> Player " + i + ": " + greenPlayer.get(i) );
        }
        System.out.println(" * Green Team Player IDs: ");
        for (int i = 0; i < greenPlayerIDs.size(); i++)
        {
            System.out.println("\t\t -> ID: " + greenPlayerIDs.get(i));
        }
        System.out.println("\n");
    }

    public void actionPerformed(ActionEvent e)
	{
        if (e.getSource() == textField) 
        {
            // Handle the event triggered by the Enter key being pressed
            String text = textField.getText();
            if ( textField.getX() == 94 || textField.getX() == 102 )
                redPlayer.add(new Player(text, null));
            else
                greenPlayer.add(new Player(text, null));
            System.out.println("\n * textField.getLocationOnScreen() for " + text + " = " + textField.getLocationOnScreen());
        }
        printTeams();
	}

}