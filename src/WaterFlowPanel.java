import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.swing.JPanel;

//This class draws the water flow onto the display.
public class WaterFlowPanel extends JPanel implements Runnable {
	WaterDraw lan;
	BufferedImage land;
	
	WaterFlowPanel(WaterDraw water) {
		lan=water;
	}
	public void Update(BufferedImage img){
		land = img;
	}
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the water images.
		//if (land.getImage() != null){
			g.drawImage(land, 0, 0, null);
		//}
	}
	
	public void run() {	
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
	    repaint();
	}
}
