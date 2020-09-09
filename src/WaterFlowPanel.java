import java.awt.Graphics;
import javax.swing.JPanel;

//This class draws the water flow onto the display.
public class WaterFlowPanel extends JPanel implements Runnable {
	WaterDraw land;
	
	WaterFlowPanel(WaterDraw water) {
		land=water;
	}
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the water images.
		if (land.getImage() != null){
			g.drawImage(land.getImage(), 0, 0, null);
		}
	}
	
	public void run() {	
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
	    repaint();
	}
}
