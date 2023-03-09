// SWING imports
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.StyledEditorKit.ForegroundAction;
// AWT imports
import java.awt.*;
import java.awt.event.*;
// SQL imports
import java.sql.*;
// Other imports
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Timer;

public class LaserTag implements ActionListener 
{
    //Controller controller;

    // Team player names
    private static ArrayList<Player> redPlayers = new ArrayList<Player>();
    private static ArrayList<Player> greenPlayers = new ArrayList<Player>();

    // Variables
    private JTextField textField;
    final private static Font mainFont = new Font("Dialog", Font.PLAIN, 15); // Main text
    final private static Font welcomeFont = new Font("Dialog", Font.BOLD, 40); // Welcome label
    final private static Font instructFont = new Font("Dialog", Font.PLAIN, 20); // Instruction label

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
        // creates database connection object  
		Connection db = getConnection();
        
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        
        //Open splash screen for 3 seconds and close
        JWindow splashScreen = createSplashScreen(frame);
        Thread.sleep(2000); //2 secs 
        splashScreen.setVisible(false);

        // Create frame and add layout
        createFrame(frame);
        addLayout(frame);

        //Show frame
        frame.setVisible(true);
    }

    // METHODS ==================================================================================================
    // Method that connects to server and returns connection
	public static Connection getConnection() {
        Connection conn = null;
		String url = "jdbc:postgresql://ec2-54-86-224-85.compute-1.amazonaws.com:5432/d7o8d02lik98h5?sslmode=require&user=uyxzxuqnymgnca&password=28ac4c9bcc607991c066ccdcb5bc72e1fac7f43dc34d02d0dd68262bc29db8a1";
		try {
			// Attempts to get connection from heroku
			conn = DriverManager.getConnection(url);
			
			// Checks to see if connection was successful
			if (conn != null) {
				System.out.println("Connected to database");
			} else {
				System.out.println("Failed to connect");
			}
		
		}
		// Checks for SQL Exceptions
		catch (SQLException e){
			e.printStackTrace(System.err);
		}
		// Returns the heroku Connection
		return conn;
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
        frame.setSize(1920, 1080); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void addLayout(JFrame frame)
    {
        /******************************** Text Panel ********************************/
        // Create textLabel for welcome text
        JLabel welcomeLabel = new JLabel("Welcome to Photon Main");
        JLabel instructLabel = new JLabel("Enter in your ID and Codename, then get ready to play!");
        welcomeLabel.setFont(welcomeFont); // Set fonts
        instructLabel.setFont(instructFont);
        welcomeLabel.setForeground(Color.white); // Set text color
        instructLabel.setForeground(Color.white);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center text
        instructLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Add emptyBorder for space
        Border emptyTextBorder = BorderFactory.createEmptyBorder(50, 0, 100, 0);

        // Create textPanel and add elements to it
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new GridLayout(2, 1, 5, 5));
        textPanel.add(welcomeLabel);
        textPanel.add(instructLabel);
        textPanel.setBorder(emptyTextBorder);

        /**************************** Player Entry Panel ****************************/
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
        Box hboxRed = Box.createHorizontalBox();
        Box hboxGreen = Box.createHorizontalBox();
        hboxRed.add(vbox1name);
        hboxRed.add(vbox1id);
        hbox.add(Box.createHorizontalStrut(10));
        hboxGreen.add(vbox2name);
        hboxGreen.add(vbox2id);

        // ACreate playerEntryPanel
        JPanel playerEntryPanel = new JPanel();
        playerEntryPanel.setOpaque(false);
        playerEntryPanel.add(hboxRed);
        playerEntryPanel.add(hbox);
        playerEntryPanel.add(hboxGreen);


        /******************************** Button Panel ********************************/
        // Create start game button
        JButton button = new JButton("[F5] Start Game");
        button.setFont(mainFont);
        button.setPreferredSize(new Dimension(200, 50));

        // Add action listener to button
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                buttonMethod();
            }
        });
        // Make F5 key activate button
        button.addKeyListener(new KeyAdapter() 
        {
            @Override
            public void keyPressed(KeyEvent e) 
            {
                System.out.println("1");
                if (e.getKeyCode() == KeyEvent.VK_F5) 
                {
                    buttonMethod();
                }
            }
        });

        // Add emptyBorder for space
        Border emptyButtonBorder = BorderFactory.createEmptyBorder(0, 0, 50, 0);

        // Create buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(button);
        buttonPanel.setBorder(emptyButtonBorder);

        /******************************** Main Frame ********************************/
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.darkGray);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(textPanel, BorderLayout.NORTH);
        mainPanel.add(playerEntryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);

    }

    // Method to create and add a horizontal box to a vertical box
    private static JTextField addHorizontalBox(Box vbox, String labelText, ArrayList<Player> redPlayers, ArrayList<Player> greenPlayers)
    {
        // Create boxes
        Box hbox = Box.createHorizontalBox(); // Create box
        JLabel label = new JLabel(labelText); // Add label text
        label.setFont(mainFont);
        label.setForeground(Color.white); // Set text color to white
        hbox.add(label); // Add label
        hbox.add(Box.createHorizontalStrut(10)); // Add space for label
        JTextField textField = new JTextField(10); // Add text field
        // Add action listener to each text field
        LaserTag listener = new LaserTag(textField, redPlayers, greenPlayers); // Allows enter to be used to add players 
        textField.addActionListener(listener);                                 // from text fields into respective arrays
        hbox.add(textField); // Add textfield to hbox
        
        // Creates player objects and adds them to their respective arraylists
        if (labelText.contains("Red"))
        {
            // Creates player object with a textField and adds it to the redPlayer arraylist
            Player player = new Player(textField, null, labelText.substring(12), "Red");
            redPlayers.add(player);
        }
        else if (labelText.contains("Green"))
        {
            // Creates player object with a textField and adds it to the greenPlayer arraylist
            Player player = new Player(textField, null, labelText.substring(14), "Green");
            greenPlayers.add(player);
        }

        vbox.add(hbox); // Add hbox to vbox

        return textField;
    }

    // Method for when button is pressed
    public static void buttonMethod()
    {
        System.out.println("BUTTON METHOD");
        printTeams();
    }

    // Method that creates a countdown timer
    public static void countdownTimer()
    {
        Timer timer = new Timer();
        
    }

    // Method to print out array lists of players names
    public static void printTeams()
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

            // String text = textField.getText();
            // if ( textField.getX() == 94 || textField.getX() == 102 )
            //     redPlayers.add(new Player(textField, text, null, "Red"));
            // else
            //     greenPlayers.add(new Player(textField, text, null, "Green"));
            // System.out.println("\n * textField.getLocationOnScreen() for " + text + " = " + textField.getLocationOnScreen());
        }
	}

}