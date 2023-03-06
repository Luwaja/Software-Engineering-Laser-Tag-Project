// SWING imports
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.Box;
// AWT imports
import java.awt.Image;
import java.awt.Insets;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
// SQL imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// Other imports
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;

public class LaserTag implements ActionListener 
{
    //Controller controller;

    // Team player names
    private static ArrayList<Player> redPlayers = new ArrayList<Player>();
    private static ArrayList<Player> greenPlayers = new ArrayList<Player>();

    // Variables
    private JTextField textField;

    // Laser Tag Constructor
    public LaserTag(JTextField textField, ArrayList<Player> redTeam, ArrayList<Player> greenTeam) 
    {
        this.textField = textField;
        this.redPlayers = redTeam;
        this.greenPlayers = greenTeam;
    }

    //MAIN FUNCTION ============================================================================
    public static void main(String[] args) throws InterruptedException
    {  
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        
        //Open splash screen for 3 seconds and close
        JWindow splashScreen = createSplashScreen(frame);
        Thread.sleep(2000); //2 secs 
        splashScreen.setVisible(false);

        // Create frame
        createFrame(frame);

        // Vertical boxes to hold horizontal box for name and id
        Box vbox1name = Box.createVerticalBox();
        Box vbox1id = Box.createVerticalBox();
        Box vbox2name = Box.createVerticalBox();
        Box vbox2id = Box.createVerticalBox();
    
        // Add boxes for each teams' 15 players
        for (int i = 0; i < 15; i++)
        {
            if (i <= 8)
            {
                addHorizontalBox(vbox1name, "(Red Player " + (i+1) + ")   ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox1id, "  Codename: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2name, "(Green Player " + (i+1) + ")   ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2id, "  Codename: ", redPlayers, greenPlayers);
            }
            else
            {
                addHorizontalBox(vbox1name, "(Red Player " + (i+1) + ") ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox1id, "  Codename: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2name, "(Green Player " + (i+1) + ") ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2id, "  Codename: ", redPlayers, greenPlayers);
            }
            
        }
        
        // Horizontal box to hold the columns
        Box hbox = Box.createHorizontalBox();
        hbox.add(vbox1name);
        hbox.add(vbox1id);
        hbox.add(Box.createHorizontalStrut(10));
        hbox.add(vbox2name);
        hbox.add(vbox2id);

        // Set the layout manager of the content pane to a GridBagLayout
        Container contentPane = frame.getContentPane();
        GridBagLayout layout = new GridBagLayout();
        contentPane.setLayout(layout);

        // Creating Constraints
        GridBagConstraints playerEntryConstraints = createConstraints(0, 0, 1, 1, 0.0, 100, 100, 100, 100);
        GridBagConstraints startButtonConstraints = createConstraints(0, 1, 1, 1, 1.0, 10, 10, 10, 10);

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
        contentPane.add(hbox, playerEntryConstraints);
        contentPane.add(button, startButtonConstraints);

        //Show frame
        frame.setVisible(true);
    }


    // METHODS ==================================================================================================
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
        frame.setSize(1920, 1080); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // Method to create and add a horizontal box to a vertical box
    private static JTextField addHorizontalBox(Box vbox, String labelText, ArrayList<Player> redPlayers, ArrayList<Player> greenPlayers)
    {
        // Create boxes
        Box hbox = Box.createHorizontalBox();
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.white);    
        hbox.add(label);                                    // Add label
        hbox.add(Box.createHorizontalStrut(10));     // Add space for label
        JTextField textField = new JTextField(10); // Add text field
        // Add action listener to each text field
        LaserTag listener = new LaserTag(textField, redPlayers, greenPlayers); // Allows enter to be used to add players 
        textField.addActionListener(listener);                                 // from text fields into respective arrays
        hbox.add(textField); // Add textfield to hbox
        
        // Create player object and add it to the respective arrays
        if (labelText.contains("Red"))
        {
            Player player = new Player(textField, null, labelText.substring(12), "Red");
            redPlayers.add(player);
        }
        else if (labelText.contains("Green"))
        {
            Player player = new Player(textField, null, labelText.substring(14), "Green");
            greenPlayers.add(player);
        }

        vbox.add(hbox); // Add hbox to vbox

        return textField;
    }

    // Method to create GridBagConstraints 
    private static GridBagConstraints createConstraints(int gridx, int gridy, int gridwidth, int gridHeight, double weightx, int insetsTop, int insetsLeft, int insetsRight, int insetsBottom)
    {
        // Create new constraint
        GridBagConstraints methodConstraints = new GridBagConstraints();
        // Set constraints with passed in values
        methodConstraints.gridx = gridx;
        methodConstraints.gridy = gridy;
        methodConstraints.gridwidth = gridwidth;
        methodConstraints.gridheight = gridHeight; 
        methodConstraints.weightx = weightx;
        methodConstraints.insets = new Insets(insetsTop, insetsLeft, insetsRight, insetsBottom);
        methodConstraints.fill = GridBagConstraints.NONE;
        // Return method constraint
        return methodConstraints;
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
        for (int i = 0; i < redPlayers.size(); i++)
        {
            System.out.println("\t * Player " + i + ": " + redPlayers.get(i) );
        }
        
        //Print out green team names and IDs
        System.out.println("\n--------------------------------------\n");
        System.out.println(" * Green Team Player Names: ");
        for (int i = 0; i < greenPlayers.size(); i++)
        {
            System.out.println("\t -> Player " + i + ": " + greenPlayers.get(i) );
        }
        
        System.out.println("\n");
    }


    // ACTION PERFORMED =========================================================================================================
    public void actionPerformed(ActionEvent e)
	{
        if (e.getSource() == textField) 
        {
            // Handle the event triggered by the Enter key being pressed
            String text = textField.getText();
            if ( textField.getX() == 94 || textField.getX() == 102 )
                redPlayers.add(new Player(textField, text, null, "Red"));
            else
                greenPlayers.add(new Player(textField, text, null, "Green"));
            System.out.println("\n * textField.getLocationOnScreen() for " + text + " = " + textField.getLocationOnScreen());
        }
        printTeams();
	}

}