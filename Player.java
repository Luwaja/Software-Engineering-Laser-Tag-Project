// headers
import javax.swing.JTextField;
// key listeners
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// player class
public class Player 
{
    JTextField textFieldName;
    JTextField textFieldID;
    String Name;
    String ID;
    String teamColor;
    int totalScore;

    public Player(JTextField textFieldName, JTextField textFieldID, String name, String id, String color)
    {
        this.textFieldID = textFieldID;
        this.textFieldName = textFieldName;
        this.Name = name;
        this.ID = id;
        this.teamColor = color;
        this.totalScore = 0;

        // Add a key listener to the textFieldID
        this.textFieldID.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Set the ID attribute to the value entered in the textFieldID
                    ID = textFieldID.getText();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
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
}
