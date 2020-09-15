import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.swing.JPanel;

//This class draws the water flow onto the display.
public class WaterFlowPanel extends JPanel implements Runnable {
	WaterDraw lan;
	BufferedImage TL = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	BufferedImage TR = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	BufferedImage BL = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	BufferedImage BR = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	
	WaterFlowPanel(WaterDraw water) {
		lan=water;
		setDoubleBuffered(true);
	}
	//public void Update(BufferedImage img){
	//	land = img;
	//}
		
	// responsible for painting the terrain and water
	// as images
	@Override
    protected void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		  
		super.paintComponent(g);
		
		// draw the water images.
		//if (land.getImage() != null){
			//g.drawImage(land, 0, 0, null);
			g.drawImage(TL, 0, 0, null);
			g.drawImage(TR, 0, 0, null);
			g.drawImage(BR, 0, 0, null);
			g.drawImage(BL, 0, 0, null);
		//}
	}
	public void PaintResult(BufferedImage TLs, BufferedImage TRs, BufferedImage BLs, BufferedImage BRs){
					TL = TLs;
					TR = TRs;
					BL = BLs;
					BR = BRs;
					//super.paintComponent(g);				
	}
	
	public void run() {	
		// display loop here
		// to do: this should be controlled by the GUI
		// to allow stopping and starting
	    repaint();
	}
}
