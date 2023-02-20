public class Player 
{
    String Name;
    String ID;

    public Player(String name, String id)
    {
        this.Name = name;
        this.ID = id;
    }

    @Override 
    public String toString()
    {
	    return " * Player, name = " + Name + ", id = " + ID;
    }
}
