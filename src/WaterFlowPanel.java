import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.swing.JPanel;

/**This class draws the water flow onto the display.
 * @author Ardo Dlamini
 * @version 1.0
*/
public class WaterFlowPanel extends JPanel implements Runnable {
	WaterDraw lan;
	BufferedImage TL = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	BufferedImage TR = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	BufferedImage BL = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	BufferedImage BR = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
	
	/** Constructor that takes in a WaterDraw object.
	*/
	WaterFlowPanel(WaterDraw water) {
		lan=water;
	}
		
	/** responsible for painting the terrain and water as images
	*/
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
	/** Receives the newly rendered images from the Threads.
	 * @param TLs BufferedImage
	 * @param TRs BufferedImage
	 * @param BLs BufferedImage
	 * @param BRs BufferedImage
	*/
	public void PaintResult(BufferedImage TLs, BufferedImage TRs, BufferedImage BLs, BufferedImage BRs){
					TL = TLs;
					TR = TRs;
					BL = BLs;
					BR = BRs;				
	}
	
	public void run() {	
	    repaint();
	}
}
