import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

/**Activates WaterUnits by mouse click
 * @author Ardo Dlamini
 * @version 1.0
*/
public class WaterDraw {
	public int dimx, dimy;
	public BufferedImage img;
	public WaterFlowPanel wfp;

	// overall number of elements in the height grid
	int dim(){
		return dimx*dimy;
	}
	
	// get x-dimensions (number of columns)
	int getDimX(){
		return dimx;
	}
	void insertWFP(WaterFlowPanel Wfp){
		wfp = Wfp;
	}
	
	// get y-dimensions (number of rows)
	int getDimY(){
		return dimy;
	}
	
	// get greyscale image
	public BufferedImage getImage() {
		  return img;
	}
	
	/**Creates the bufferedImage for the Water layer.
	*/
	void deriveImage()
	{
		img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);
		
	}
	/**Takes in an x and y position of the mouse click and draws a 10x10 square. Must first obtain the lock to the WaterGrid class before any WaterUnits are Activated.
	 * @param x coordinate of the mouse click.
	 * @param y coordinate of the mouse click.
	*/
	public void paintPixels(int x, int y){
		int XUBound =x+5;
		int YUBound =y+5;
		int XLBound = x-5;
		int YLBound = y-5;

		if(x+5>dimx){
			XUBound = dimx;
		}else if(x-5<0){
			XLBound	= 0;		
			}
		if(y-5<0){
			YLBound = 0;
			}
		else if(y+5>dimy){
			YUBound = dimy;
			}
		synchronized (WaterGrid.class){ //If adding Water Units to the array, do not allow anyone else to access it.

			for(int i=XLBound; i < XUBound; i++){
				for(int j=YLBound; j < YUBound; j++) {
					 WaterGrid.WaterUnitArray[i][j].Activate();
				}
			}

		}
	}	
	/**Takes in an x and y dimension of the Terrain array.
	 * @param x X-Dimension
	 * @param y Y-Dimension
	*/
	void readData(int x, int y){ 

			dimy = y; 
			dimx = x;
			
			deriveImage();
		} 
}
