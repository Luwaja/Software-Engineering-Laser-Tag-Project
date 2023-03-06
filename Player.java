// player class
import javax.swing.JTextField;

public class Player 
{
    private JTextField textField;
    private String teamColor;
    String Name;
    String ID;

    public Player(JTextField textField, String name, String id, String color)
    {
        this.Name = name;
        this.ID = id;
        this.textField = textField;
        this.teamColor = color;
    }

    @Override 
    public String toString()
    {
	    return "info :\n\t\t name = " + Name + "\n\t\t id = " + ID + "\n";
    }
}