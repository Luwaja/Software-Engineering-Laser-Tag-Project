// SWING imports
import javax.swing.ImageIcon;
import javax.swing.JFrame;
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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.temporal.TemporalAccessor;
import java.awt.Component;
import java.util.ArrayList;

public class LaserTag implements ActionListener 
{
    // arraylist of green players and red players
    private static ArrayList<Player> redPlayers = new ArrayList<Player>();
    private static ArrayList<Player> greenPlayers = new ArrayList<Player>();
    // variables
    private JTextField textField;

    // laser Tag Constructor
    public LaserTag(JTextField textField, ArrayList<Player> redTeam, ArrayList<Player> greenTeam) 
    {
        this.textField = textField;
        this.redPlayers = redTeam;
        this.greenPlayers = greenTeam;
    }

    public static void main(String[] args) throws InterruptedException
    {
        // splash screen ---------------------------------------
        // create instance of JFrame
        JFrame frame = new JFrame(" * LASER TAG * ");
        
        // open splash screen for 3 seconds and close
        JWindow splashScreen = createSplashScreen(frame);
        Thread.sleep(3000);     // 3 secs 
        splashScreen.setVisible(false);

        // application starts here ------------------------------
        // create frame
        createFrame(frame);

        // vertical boxes to hold horizontal box for name and id
        Box vbox1name = Box.createVerticalBox();
        Box vbox1id = Box.createVerticalBox();
        Box vbox2name = Box.createVerticalBox();
        Box vbox2id = Box.createVerticalBox();
    
        // add boxes for each teams' 15 players
        for (int i = 0; i < 15; i++)
        {
            // organizes horizontal bozes so that they line up 
            if (i <= 8)
            {
                addHorizontalBox(vbox1name, " - Red Player " + (i + 1) + ":  ", redPlayers, greenPlayers);
                addHorizontalBox(vbox1id, " - ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2name, "- Green Player " + (i + 1) + ":  ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2id, " - ID: ", redPlayers, greenPlayers);
            }
            else 
            {
                addHorizontalBox(vbox1name, " - Red Player " + (i + 1) + ":", redPlayers, greenPlayers);
                addHorizontalBox(vbox1id, " - ID: ", redPlayers, greenPlayers);
                addHorizontalBox(vbox2name, "- Green Player " + (i + 1) + ":", redPlayers, greenPlayers);
                addHorizontalBox(vbox2id, " - ID: ", redPlayers, greenPlayers);
            }
        }
        
        // horizontal box to hold the columns
        Box hbox = Box.createHorizontalBox();
        hbox.add(vbox1name);
        hbox.add(vbox1id);
        hbox.add(Box.createHorizontalStrut(10));
        hbox.add(vbox2name);
        hbox.add(vbox2id);

        // set the layout manager of the content pane to a BoxLayout
        Container contentPane = frame.getContentPane();
        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
        contentPane.setLayout(layout);

        // create start game button
        JButton button = new JButton("[F5] Start Game");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                startButtonMethod();
            }
        });
        button.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                System.out.println("1");
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    startButtonMethod();
                }
            }
        });

        // add the boxes to the content pane
        contentPane.add(hbox);
        contentPane.add(button);

        // show frame
        frame.setVisible(true);

        // display play action screen
    }

    // method to create splash screen
    public static JWindow createSplashScreen(JFrame frame) throws InterruptedException
    {
        // creating image
        Image originalImage = Toolkit.getDefaultToolkit().getImage("PhotonLogo.jpg");
        
        // scale down image
        Image scaledImage = originalImage.getScaledInstance(660,308,Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        frame.add(imageLabel);

        // creating window splashscreen
        JWindow splashScreen = new JWindow();
        splashScreen.add(imageLabel);   // upload image
        splashScreen.pack();
        splashScreen.setLocationRelativeTo(null);
        
        // sizing and centering the screen
        splashScreen.setSize(660,308); 
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (screenSize.width - splashScreen.getWidth())/2;
        int centerY = (screenSize.height - splashScreen.getHeight())/2;
        splashScreen.setLocation(centerX, centerY);
        splashScreen.setVisible(true);
    
        return splashScreen;
    }

    // method to create frame for application
    public static void createFrame(JFrame frame) throws InterruptedException
    {
        // loading JFrame window
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.PINK);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // method to create and add a horizontal box to a vertical box
    public static JTextField addHorizontalBox(Box vbox, String labelText, ArrayList<Player> redPlayers, ArrayList<Player> greenPlayers)
    {
        // create boxes
        Box hbox = Box.createHorizontalBox();
        hbox.add(new JLabel(labelText));                    // add label
        hbox.add(Box.createHorizontalStrut(10));      // add space for label
        JTextField textField = new JTextField(10);  // add text field
        // add action listener to each text field
        LaserTag listener = new LaserTag(textField, redPlayers, greenPlayers);
        textField.addActionListener(listener);
        // add textfield to hbox and hbox to vbox
        hbox.add(textField); // add text field to box
        // create player object and add it to the appropriate arraylist
        if (labelText.contains("Red"))
        {
            Player player = new Player(textField, null, labelText.substring(12), "red");
            redPlayers.add(player);
        }
        else if (labelText.contains("Green"))
        {
            Player player = new Player(textField, null, labelText.substring(14), "green");
            greenPlayers.add(player);
        }

        // add horizontal box to vertical box
        vbox.add(hbox);

        return textField;
    }

    // method for when button is pressed
    public static void startButtonMethod()
    {
        System.out.println(" * BUTTON METHOD * ");
    }

    // method to print out array lists of players names
    public void printTeams()
    {
        System.out.println("--------------------------------------\n");
        System.out.println(" * Red Team Player Names: \n");
        for (int i = 0; i < redPlayers.size(); i++)
        {
            System.out.println("\t * Player " + (i + 1) + " " + redPlayers.get(i) );
        }
        System.out.println("--------------------------------------\n");
        System.out.println(" * Green Team Player Names: \n");
        for (int i = 0; i < greenPlayers.size(); i++)
        {
            System.out.println("\t -> Player " + (i + 1) + " " + greenPlayers.get(i) );
        }
        System.out.println("\n");
    }

    // action performed method
    public void actionPerformed(ActionEvent e)
	{
        if (e.getSource() == textField) 
        {
            // handle the event triggered by the Enter key being pressed
            String text = textField.getText();
            if (textField.getX() == 114 )
                redPlayers.add(new Player(textField, text, null, "red"));
            else if ( textField.getX() == 123 )
                greenPlayers.add(new Player(textField, text, null, "green"));
            // System.out.println("\n * textField.getX() for " + text + " = " + textField.getX());
        }
        printTeams();
	}

}




