// headers
import javax.swing.JTextField;
// key listeners
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.sql.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

// player class
public class Player 
{
    JTextField textFieldName;
    JTextField textFieldID;
    String Name;
    String ID;
    String teamColor;
    int totalScore;
	static Connection pconnect;

    public Player(JTextField textFieldName, JTextField textFieldID, String name, String id, String color, Connection c)
    {
        this.textFieldID = textFieldID;
        this.textFieldName = textFieldName;
        this.Name = name;
        this.ID = id;
        this.teamColor = color;
        this.totalScore = 0;
		this.textFieldID.setEditable(true);
		this.textFieldName.setEditable(false);
		
		// Connection pconnect = c;
		if (pconnect == null)
		{
			pconnect = c;
		}

        // Add a key listener to the textFieldID
		AKL(this.textFieldID);
		AKL(this.textFieldName);

	}

	public void AKL(JTextField field)
	{
		field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                // if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // // Set the ID attribute to the value entered in the textFieldID
                    // ID = textFieldID.getText();
                    // Name = textFieldName.getText();
                // }
            }

            @Override
            public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Set the ID attribute to the value entered in the textFieldID
                    ID = textFieldID.getText();
                    Name = textFieldName.getText();
					if (textFieldID.isEditable() == true)
					{
						boolean tempboo;
						ID = textFieldID.getText();
						Name = textFieldName.getText();
						tempboo = sqInsert();
						if (tempboo == true)
						{
							textFieldName.setEditable(true);
							textFieldID.setEditable(false);
							textFieldID.setFocusable(false);
						}
					}
					else if (textFieldName.isEditable() == true)
					{
						Name = textFieldName.getText();
						textFieldName.setEditable(false);
						textFieldName.setFocusable(false);
					}
                }

			}
		});
	}

    public void update(){
        this.ID = textFieldID.getText();
        this.Name =textFieldName.getText();

    }

    public void setTextFieldName(JTextField textFieldName)
    {
        this.textFieldName = textFieldName;
    }

    public void setTextFieldID(JTextField textFieldID)
    {
        this.textFieldID = textFieldID;
    }

    public void setName(String name)
    {
        this.Name = name;
    }

    public void setID(String id)
    {
        this.ID = id;
    }

    public void setTeamColor(String teamcolor)
    {
        this.teamColor = teamcolor;
    }

    public JTextField getTextFieldName()
    {
        return textFieldName;
    }

    public JTextField getTextFieldID()
    {
        return textFieldID;
    }

    public String getName()
    {
        return Name;
    }

    public String getID()
    {
        return ID;
    }

    public String getTeamColor()
    {
        return teamColor;
    }

    public int getTotalScore()
    {
        return totalScore;
    }

    @Override 
    public String toString()
    {

	    return "info :\n\t\t name = " + Name + "\n\t\t id = " + ID + "\n\t\t team color = " + teamColor + "\n\t\t total score = " + totalScore + "\n\t\t";
    }
	
	public boolean sqInsert()
	{

		try {
			// Converts player string indexes to integers
			int index = Integer.parseInt(ID);
			
			// Creates result statement with the server
			Statement test = pconnect.createStatement();
			
			// Prepares result set
			ResultSet check = test.executeQuery("SELECT count(*) FROM player WHERE player.id = " +  ID);
			System.out.println(check.getInt(1));
			while(check.next()){
				//checks if player ID is taken
				System.out.println(check.getString(1));
				if (check.getInt(1) == 0)
				{
					// Inserts player into database
					PreparedStatement dbInsert = pconnect.prepareStatement("INSERT INTO player VALUES(?, ?, ?, ?)");
					dbInsert.setInt(1, index);
					dbInsert.setString(2, null);
					dbInsert.setString(3, null);
					dbInsert.setString(4, null);

					// Executes insert query
					dbInsert.execute();
					return true;
				}
				else if (check.getInt(1) == 1)
				{
					System.out.println("WE IN");
					Statement testTwo = pconnect.createStatement();
					ResultSet nameCheck = testTwo.executeQuery("SELECT id, first_name, last_name, codename FROM player WHERE player.id = " + ID);
					nameCheck.next();
					String dbname = nameCheck.getString("codename");
					if (dbname == null)
					{
						//if it is null allow for name insert
						System.out.println("ID FREE");
						Statement testThree = pconnect.createStatement();
						PreparedStatement nameInsert = pconnect.prepareStatement("UPDATE player SET codename = " + Name +  " WHERE id = " + ID);
						// nameInsert.setString(1, Name);
						// PreparedStatement nameInsert = pconnect.prepareStatement("INSERT INTO player VALUES(?, ?, ?, ?) WHERE id = " + ID);
						nameInsert.executeUpdate();
						
					}
					else
					{
						//if its not null then dont allow for insert say its taken
						System.out.println("ID IS TAKEN");
						JOptionPane.showMessageDialog(new JFrame(), "ID IS ALREADY TAKEN");
						return false;
					}

				}
			}
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
		return true;
	}
	
	
}
