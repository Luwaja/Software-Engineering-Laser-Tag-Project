import java.awt.event.MouseListener;
import java.util.ResourceBundle.Control;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class Controller implements ActionListener, MouseListener, KeyListener
{
	boolean keyF5;

	Controller()
	{
		
	}

	public void actionPerformed(ActionEvent e)
	{

	}

	//Mouse pressed input
	public void mousePressed(MouseEvent e)
	{

	}

	public void mouseReleased(MouseEvent e) {	}
	public void mouseEntered(MouseEvent e) {	}
	public void mouseExited(MouseEvent e) {		}
	public void mouseClicked(MouseEvent e) {	}

	//Key pressed inputs
	public void keyPressed(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_F5: 
				keyF5 = true;
			    break;
		}
	}

	//Key released inputs
	public void keyReleased(KeyEvent e)
	{
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_F5: 
				keyF5 = false; 
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
		}
	}

	public void keyTyped(KeyEvent e)
	{

	}

	// Key inputs 
	void update()
	{
		// Commands for key presses
	}
}
