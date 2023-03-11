
// headers
import javax.swing.JTextField;

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
