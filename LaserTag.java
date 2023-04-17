// Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.net.*;

public class LaserTag implements ActionListener 
{
    // Variables
    private JTextField textField;
    private JTextField textFieldID;
    private JTextField textFieldName;
    private DatagramSocket socket;
    private int redScore;
    private int greenScore;
    private JLabel gameTitleLabel;

    // Card Variables
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JPanel redTeamPanel;
    private JPanel greenTeamPanel;
    private JPanel redTeamBoxPanel;
    private JPanel greenTeamBoxPanel;
    private JPanel gamePanel;
    private JPanel actionDisplayPanel;
    
    
    // Team player names
    private static ArrayList<Player> redPlayers = new ArrayList<Player>();
    private static ArrayList<Player> greenPlayers = new ArrayList<Player>();

    // Screen size
    private Toolkit tk = Toolkit.getDefaultToolkit();
    private Dimension screenSize = tk.getScreenSize();
    private int screenWidth = (int) screenSize.getWidth();
    private int screenHeight = (int) screenSize.getHeight();

    // Timer variables
    private static final int timerDelay = 1000;
    private static final int timerPeriod = 1000;
    private JLabel timerLabel;
    private int playerEntrySeconds = 3;
    private String playerEntryPhrase = "Starting the game in:";
    private int actionDisplaySeconds = 360;
    private String actionDisplayPhrase = "Time left in the game:";
    
    // Fonts
    final private static Font welcomeFont = new Font("Dialog", Font.BOLD, 40); // Welcome label
    final private static Font instructFont = new Font("Dialog", Font.PLAIN, 20); // Instruction label
    final private static Font mainFont = new Font("Dialog", Font.PLAIN, 15); // Main text
    final private static Font timerFont = new Font("Dialog", Font.PLAIN, 25); // Timer label

    // Laser Tag Constructor
    public LaserTag(JTextField textFieldID, JTextField textFieldName, ArrayList<Player> redTeam, ArrayList<Player> greenTeam) 
    {
        this.textFieldID = textFieldID;
        this.textFieldName = textFieldName;
        this.redPlayers = redTeam;
        this.greenPlayers = greenTeam;
        this.redScore = 0;
        this.greenScore = 0;

        try{
            this.socket = new DatagramSocket(7501);
        } catch (SocketException e){
            System.out.println(e.getMessage());
        }
    }

    //MAIN FUNCTION ============================================================================
    public static void main(String[] args) throws InterruptedException
    {  
        // Create instance of LaserTag
        LaserTag laserTag = new LaserTag(null, null, redPlayers, greenPlayers);

        // creates database connection object  
		Connection db = getConnection();
        
        // Create instance of JFrame
        JFrame frame = new JFrame("LASER TAG");
        
        //Open splash screen for 3 seconds and close
        JWindow splashScreen = createSplashScreen(frame);
        Thread.sleep(3000); //3 secs 
        splashScreen.setVisible(false);

        // Create frame and add layout
        laserTag.createFrame(frame);
        JPanel playerEntryPanel = laserTag.createPlayerEntry();
        JPanel actionDisplayPanel = laserTag.createActionDisplay();
        laserTag.setLayout(frame, playerEntryPanel, actionDisplayPanel);
        // If button is pressed in playerEntryPanel
        laserTag.pressedKey(frame);

        //Show frame
        frame.setVisible(true);

        //Create UDP Socket to take input from traffic generator
        laserTag.getTraffic();
        
    }

    // METHODS =================================================================================================
    public void getTraffic() 
    {
        byte[] buffer = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while (true) 
        {   
            try{
                //take in message from traffic generator in format "playerID:otherPlayerID"
                socket.receive(packet);
                String gameEvent = new String(packet.getData(), 0, packet.getLength());

                System.out.println(gameEvent);
                //call function to process message
                handleTraffic(gameEvent);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }   
    
  // Method that processes message from traffic generator
  public void handleTraffic(String event){

    //extract player IDs from the packet sent from traffic generator
    String[] parts = event.split(":");

    String shootingPlayerID = (parts[0]);
    String shotPlayerID = (parts[1]);
    String shootingPlayerName;
    String shotPlayerName;
    String fshot = shootingPlayerID + " blasted " + shotPlayerID;
    System.out.println(fshot);

    //search players with shootingPlayerID, adding tend points to player and team when found
    for(int i = 0; i < greenPlayers.size(); i++)
        {
            if (greenPlayers.get(i).getID() == null){
                System.out.println("not a player");
            }
            else if(greenPlayers.get(i).getID().equals(shootingPlayerID)){
                greenPlayers.get(i).score();
                this.greenScore += 10;
                printGameAction(fshot);
            }
        }
    for(int i = 0; i < redPlayers.size(); i++)
    {
        if (redPlayers.get(i).getID() == null){
            System.out.println("not a player");
        }
        else if(redPlayers.get(i).getID().equals(shootingPlayerID)){
            redPlayers.get(i).score();
            this.redScore += 10;
            printGameAction(fshot);
        }
    }
    System.out.println("Red Team Score: " + redScore);
    System.out.println("Green Team Score: " + greenScore);

  }

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
    
	// Method to insert players into database
	private static void sqInsert(Connection connect, Player player)
	{
		try {
			// Converts player string indexes to integers
			int index = Integer.parseInt(player.ID);
			
			// Creates result statement with the server
			Statement test = connect.createStatement();
			
			// Prepares result set
			ResultSet check = test.executeQuery("SELECT count(*) FROM player WHERE player.id = " +  player.ID);
			
			while(check.next()){
				//checks if player ID is taken
				System.out.println(check.getInt(1));
				if (check.getInt(1) == 0)
				{
					// Inserts player into database
					PreparedStatement dbInsert = connect.prepareStatement("INSERT INTO player VALUES(?, ?, ?, ?)");
					dbInsert.setInt(1, index);
					dbInsert.setString(2, null);
					dbInsert.setString(3, null);
					dbInsert.setString(4, player.Name);

					// Executes insert query
					dbInsert.execute();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
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
    public void createFrame(JFrame frame) throws InterruptedException
    {
        // Loading JFrame window
        frame.setSize(screenSize); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(Color.darkGray);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    // PLAYER ENTRY SCREEN ================================================================
    public JPanel createPlayerEntry()
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

        // Create textPanel and add elements to it
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new GridLayout(3, 1, 5, 5));
        textPanel.add(welcomeLabel);
        textPanel.add(instructLabel);
        textPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 50, 0)); // Add emptyBorder for space);

        /************************* Boxes Panel ****************************/
        // Vertical boxes to hold horizontal box for name and id
        Box vboxRed = Box.createVerticalBox();
        Box vboxGreen = Box.createVerticalBox();
    
        // Add boxes for each teams' 15 players
        for (int i = 0; i < 15; i++)
        {
            if (i <= 8)
            {
                addHorizontalBox(vboxRed, "Red Player " + (i+1) + "     ID: ", "  Codename: ", redPlayers, greenPlayers);
                addHorizontalBox(vboxGreen, "Green Player " + (i+1) + "     ID: ", "  Codename: ", redPlayers, greenPlayers);
            }
            else
            {
                addHorizontalBox(vboxRed, "Red Player " + (i+1) + "   ID: ", "  Codename: ", redPlayers, greenPlayers);
                addHorizontalBox(vboxGreen, "Green Player " + (i+1) + "   ID: ", "  Codename: ", redPlayers, greenPlayers);
            }
            
        }
        
        // Horizontal boxes to hold the columns
        // Set as red, strut, and green boxes for panel segmentation
        Box hboxRed = Box.createHorizontalBox();
        Box hboxStrut = Box.createHorizontalBox();
        Box hboxGreen = Box.createHorizontalBox();
        hboxRed.add(vboxRed);
        hboxStrut.add(Box.createHorizontalStrut(10));
        hboxGreen.add(vboxGreen);
        
        // Add color to team panels
        JPanel redTeamPanel = new JPanel();
        redTeamPanel.add(hboxRed);
        redTeamPanel.setBackground(new Color(133, 3, 3));
        JPanel greenTeamPanel = new JPanel();
        greenTeamPanel.add(hboxGreen);
        greenTeamPanel.setBackground(new Color(14, 115, 2));

        // Create playerEntryPanel
        JPanel boxesPanel = new JPanel();
        //boxesPanel.setPreferredSize(new Dimension((screenWidth/2), (screenHeight/2)));
        boxesPanel.setOpaque(false);
        boxesPanel.add(redTeamPanel);
        boxesPanel.add(hboxStrut);
        boxesPanel.add(greenTeamPanel);

        /******************************** Button Panel ********************************/
        // Create timerLabel and timerPanel
        timerLabel = new JLabel("");
        timerLabel.setFont(timerFont); // Set fonts
        timerLabel.setForeground(Color.white); // Set text color
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel timerPanel = new JPanel();
        timerPanel.add(timerLabel);
        timerPanel.setOpaque(false);
        
        // Create start game button
        JButton button = new JButton("[F5] Start Game");
        button.setFont(mainFont);
        button.setPreferredSize(new Dimension(200, 50));
        JPanel startButtonPanel = new JPanel();
        startButtonPanel.add(button);
        startButtonPanel.setOpaque(false);
        
        // Add action listener to button
        button.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e) 
            {
                buttonMethod();
            }
        });
        
        // Create buttonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(timerPanel);
        buttonPanel.add(startButtonPanel);
        buttonPanel.setLayout(new GridLayout(2, 1, 0, 50));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0)); // Add emptyBorder for space

        /******************************** Player Entry Panel ********************************/
        JPanel playerEntryPanel = new JPanel();
        playerEntryPanel.setLayout(new BorderLayout());
        playerEntryPanel.setBackground(Color.darkGray);
        playerEntryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        playerEntryPanel.add(textPanel, BorderLayout.NORTH);
        playerEntryPanel.add(boxesPanel, BorderLayout.CENTER);
        playerEntryPanel.add(buttonPanel, BorderLayout.SOUTH);

        return playerEntryPanel;
    }

    // ACTION DISPLAY SCREEN ================================================================================
    // Method to create actionDisplayPanel
    public JPanel createActionDisplay()
    {
        /******************************** Teams Panel ********************************/
        redTeamBoxPanel = new JPanel();
        greenTeamBoxPanel = new JPanel();
        redTeamBoxPanel.setOpaque(false);
        greenTeamBoxPanel.setOpaque(false);

        // Red team: Create panels, boxes, and labels
        redTeamPanel = new JPanel();
        Box vboxRedTeam = Box.createVerticalBox();
        Box hboxRedTeam = Box.createHorizontalBox();
        JLabel redTitleLabel = new JLabel("RED TEAM: " + redScore);
        // Set label attributes
        redTitleLabel.setFont(welcomeFont);
        redTitleLabel.setForeground(Color.red);
        redTitleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Not working for some reason
        // Boxes
        hboxRedTeam.add(redTitleLabel);
        vboxRedTeam.add(hboxRedTeam);
        // set panel attributes
        redTeamPanel.setPreferredSize(new Dimension((int) (screenWidth/2.125), (screenHeight/2)));
        redTeamPanel.setBorder(BorderFactory.createLineBorder(Color.red));
        redTeamPanel.setOpaque(false);
        //redTeamPanel.add(hboxRedTeam);
        redTeamPanel.add(vboxRedTeam);
        redTeamPanel.setLayout(new GridLayout(2, 1, 5, 0));
        
            
        // Green team: Create panels, boxes, and labels
        greenTeamPanel = new JPanel();
        Box vboxGreenTeam = Box.createVerticalBox();
        Box hboxGreenTeam = Box.createHorizontalBox();
        JLabel greenTitleLabel = new JLabel("GREEN TEAM: " + greenScore);
        // Set label attributes
        greenTitleLabel.setFont(welcomeFont);
        greenTitleLabel.setForeground(Color.green);
        greenTitleLabel.setHorizontalAlignment(SwingConstants.LEFT); // Not working for some reason

        // Boxes
        hboxGreenTeam.add(greenTitleLabel);
        vboxGreenTeam.add(hboxGreenTeam);
        // Set panel attributes
        greenTeamPanel.setPreferredSize(new Dimension((int) (screenWidth/2.125), (screenHeight/2)));
        greenTeamPanel.setBorder(BorderFactory.createLineBorder(Color.green));
        greenTeamPanel.setOpaque(false);
        //greenTeamPanel.add(hboxGreenTeam);
        greenTeamPanel.add(vboxGreenTeam);
        greenTeamPanel.setLayout(new GridLayout(2, 1, 5, 0));
        
        // Create teamsPanel and add elements
        JPanel teamsPanel = new JPanel();
        teamsPanel.setOpaque(false);
        teamsPanel.add(redTeamPanel);
        teamsPanel.add(greenTeamPanel);
        
        /******************************** Game Panel ********************************/
        gameTitleLabel = new JLabel("GAME ACTION");
        gameTitleLabel.setFont(welcomeFont);
        gameTitleLabel.setForeground(Color.white);
        gameTitleLabel.setHorizontalAlignment(SwingConstants.LEFT);

        // Create gamePanel and add elements
        gamePanel = new JPanel();
        gamePanel.setPreferredSize(new Dimension((screenWidth/2), (int) (screenHeight/3.25)));
        gamePanel.setBorder(BorderFactory.createLineBorder(Color.white));
        gamePanel.setOpaque(false);
        //gamePanel.setLayout(new GridLayout(6, 1, 5, 0));
        gamePanel.add(gameTitleLabel);
        
        /******************************** Action Display Panel ********************************/
        JLabel actionLabel = new JLabel("Action Display");
        actionLabel.setFont(instructFont);
        actionLabel.setForeground(Color.white);
        actionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Create actionDisplayPanel and add elements
        actionDisplayPanel = new JPanel();
        actionDisplayPanel.setLayout(new BorderLayout());
        actionDisplayPanel.setBackground(Color.darkGray);
        actionDisplayPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        actionDisplayPanel.add(actionLabel, BorderLayout.NORTH);
        actionDisplayPanel.add(teamsPanel, BorderLayout.CENTER);
        actionDisplayPanel.add(gamePanel, BorderLayout.SOUTH);

        //update team scores in labels
        teamScoreUpdate(greenTitleLabel, redTitleLabel);
        
        return actionDisplayPanel;
    }

    // Set panels from layout to card panel and add to frame
    public void setLayout(JFrame frame, JPanel playerEntryPanel, JPanel actionDisplayPanel)
    {
        /******************************** Card Panel ********************************/
        // Create cardLayout and cardPanel
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        
        // Add panels to cardPanel
        cardPanel.add(playerEntryPanel, "Panel 1");
        cardPanel.add(actionDisplayPanel, "Panel 2");

        // Add cardPanel to frame
        frame.add(cardPanel);
    }

    // Method to change screens
    public void changeCard()
    {
        cardLayout.show(cardPanel, "Panel 2");
        addStuff();
    }

    // Method to add stuff to action display
    public void addStuff()
    {
        Box vbox1 = Box.createVerticalBox();
        Box vbox2 = Box.createVerticalBox();
        
        // Loop to add player info as labels to team red team panel
        for(int i = 0; i < redPlayers.size(); i++)
        {
            // Create boxes, strings, and labels
            Box hbox = Box.createHorizontalBox();
            String id = redPlayers.get(i).getID();
            String cn = redPlayers.get(i).getName();
            JLabel idLabel = new JLabel(id);
            idLabel.setForeground(Color.white);
            idLabel.setFont(instructFont);
            JLabel cnLabel = new JLabel(cn);
            cnLabel.setForeground(Color.white);
            cnLabel.setFont(instructFont);

            // Add labels to boxes and boxes to redTeam panel
            hbox.add(idLabel);
            hbox.add(Box.createHorizontalStrut(20));
            hbox.add(cnLabel);
            vbox1.add(hbox);
        }

        // Loop to add player info as labels to team green team panel
        for(int i = 0; i < greenPlayers.size(); i++)
        {
            // Create boxes, strings, and labels
            Box hbox = Box.createHorizontalBox();
            String id = greenPlayers.get(i).getID();
            String cn = greenPlayers.get(i).getName();
            JLabel idLabel = new JLabel(id);
            idLabel.setForeground(Color.white);
            idLabel.setFont(instructFont);
            JLabel cnLabel = new JLabel(cn);
            cnLabel.setForeground(Color.white);
            cnLabel.setFont(instructFont);

            // Add labels to boxes and boxes to greenTeam panel
            hbox.add(idLabel);
            hbox.add(Box.createHorizontalStrut(20));
            hbox.add(cnLabel);
            vbox2.add(hbox);
        }

        redTeamBoxPanel.add(vbox1);
        greenTeamBoxPanel.add(vbox2);
        redTeamPanel.add(redTeamBoxPanel);
        greenTeamPanel.add(greenTeamBoxPanel);
    }

    // Method to create and add a horizontal box to a vertical box
    private static JTextField[] addHorizontalBox(Box vbox, String labelTextID, String labelTextName, ArrayList<Player> redPlayers, ArrayList<Player> greenPlayers)
    {
        // Create main HBox to hold internal ID and Name HBoxes
        Box hboxMain = Box.createHorizontalBox(); // Create box

        //create ID HBox
        Box hboxID = Box.createHorizontalBox();
        JLabel labelID = new JLabel(labelTextID); // Add label text
        labelID.setFont(mainFont);
        labelID.setForeground(Color.white); // Set text color to white
        hboxID.add(labelID); // Add label
        hboxID.add(Box.createHorizontalStrut(10)); // Add space for label
        JTextField textFieldID = new JTextField(10); // Add text field
        hboxID.add(textFieldID); // Add textfield to hbox

        //Add ID HBox to main HBox
        hboxMain.add(hboxID);

        //Create Name HBox
        Box hboxName = Box.createHorizontalBox();
        JLabel labelName = new JLabel(labelTextName);
        labelName.setFont(mainFont);
        labelName.setForeground(Color.white); // Set text color to white
        hboxName.add(labelName); // Add label
        hboxName.add(Box.createHorizontalStrut(10)); // Add space for label
        JTextField textFieldName = new JTextField(10); // Add text field
        hboxName.add(textFieldName);

        //Add Name HBox to Main HBox
        hboxMain.add(hboxName);

        // Add action listener to each text field
        LaserTag listener = new LaserTag(textFieldID, textFieldName, redPlayers, greenPlayers); // Allows enter to be used to add players 
        textFieldID.addActionListener(listener);                                // from text fields into respective arrays
        textFieldName.addActionListener(listener);

        Player player = new Player(textFieldName, textFieldID, null, null, null);
        // Creates player objects and adds them to their respective arraylists
        if (labelTextID.contains("Red"))
        {
            // Creates player object with a textField and adds it to the redPlayer arraylist
            player.setTeamColor("Red");
            redPlayers.add(player);
        }
        else if (labelTextID.contains("Green"))
        {
            // Creates player object with a textField and adds it to the greenPlayer arraylist
            player.setTeamColor("Green");
            greenPlayers.add(player);
        }

        vbox.add(hboxMain); // Add hbox to vbox

        return new JTextField[] {textFieldID, textFieldName};
    }
 
    // Method for updating the players
    private void updateMethod()
    {
		Connection conn = getConnection();
        System.out.println("test");
        //loop through players and call update methods
        for (int i = 0; i < redPlayers.size(); i++)
		{
            redPlayers.get(i).update();
			// Updates red players in database
			if (redPlayers.get(i).getID().isEmpty() !=  true)
				sqInsert(conn, redPlayers.get(i));
		}
        for (int i = 0; i < greenPlayers.size(); i++)
		{
            greenPlayers.get(i).update();
			// Updates green players in database
			if (greenPlayers.get(i).getID().isEmpty() !=  true)
				sqInsert(conn, redPlayers.get(i));
		}
    }
    // Method for when F5 key is pressed
    public void pressedKey(JFrame frame)
    {
        // Make F5 key activate button
        frame.addKeyListener(new KeyAdapter() 
        {
            public void keyPressed(KeyEvent e) 
            {
                if (e.getKeyCode() == KeyEvent.VK_F5) 
                {
                    buttonMethod();
                }
            }
        });
    }

    // Method for when button is pressed
    public void buttonMethod()
    {
        //System.out.println("pressed");
        //updateMethod();
        countdownTimer(playerEntrySeconds, playerEntryPhrase);
        printTeams();
    }

    public void teamScoreUpdate(JLabel redTeamLabel, JLabel greenTeamLabel)
    {
            Thread thread = new Thread(() -> {
            while (true) {
                // Update the score of the red team label
                redTeamLabel.setText("Red Team: " + redScore);

                // Update the text of the green label
                greenTeamLabel.setText("Green Team: " + greenScore);

                try {
                    //updates every second
                    Thread.sleep(1000); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start(); // Start the thread to update the labels
    }


    private JLabel[] labels = new JLabel[5];
    private Box vbox = Box.createVerticalBox();

    public void printGameAction(String actionout) {
        JLabel newLabel = new JLabel(actionout);
        newLabel.setForeground(Color.white);
        newLabel.setFont(instructFont);

        // Shift the remaining labels up in the array
        for (int i = 0; i < labels.length - 1; i++) {
            labels[i] = labels[i+1];
        }

        // Add the new label to the end of the array
        labels[labels.length - 1] = newLabel;

        vbox.removeAll();
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != null) {
                vbox.add(labels[i]);
                vbox.add(Box.createVerticalStrut(10));
            }
        }

        // Add the vbox to the game panel
        gamePanel.removeAll();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(gameTitleLabel, BorderLayout.NORTH);
        gamePanel.add(vbox, BorderLayout.CENTER);

        gamePanel.revalidate();
        gamePanel.repaint();
    }


    // Method that creates a countdown timer based on a passed in # of seconds
    public void countdownTimer(int seconds, String phrase)
    {
        // Create timer to run at a fixed rate specified by delay and period
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(new TimerTask() 
        {
            int secondsRemaining = seconds + 1;
            public void run() 
            {
                // If 
                if (secondsRemaining > 0) 
                {
                    secondsRemaining--;
                    timerLabel.setText(phrase + " " + String.valueOf(secondsRemaining) + " seconds!");
                } else 
                {
                    timer.cancel();
                    changeCard();
                    actionDisplayTimer();
                }
            }
        }, timerDelay, timerPeriod);
    }

    public void actionDisplayTimer()
    {
        countdownTimer(actionDisplaySeconds, actionDisplayPhrase);
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
            if (e.getSource() == textFieldID) 
            {
                // Handle the event triggered by the Enter key being pressed
                String text = textFieldID.getText();
                System.out.println(" * enter key has been pressed...  ");

            }
        }
	}

}
