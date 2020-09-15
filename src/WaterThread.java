import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.lang.Math;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.awt.event.*;

/**The Fork-Join thread class that will operate on the matrix Terrain data.
	* @author Ardo Dlamini	
	*/
public class WaterThread implements Runnable  {
	  private static BufferedImage img;//WaterDraw img.
	  //private static BufferedImage out;
	  private int ThreadID = 1;
	  BufferedImage[] Buff;
	  //public static AtomicBoolean isPaused = new AtomicBoolean(false);
	  //boolean updateFrame = true;
	  //public static AtomicBoolean isWorking = new AtomicBoolean(false);

	  private static WaterUnit[][] WaterUnitArr;
	  private static float[][] Height;

	  static WaterFlowPanel wfp;
	
      int rows; // arguments
	  int columns;

	  int StartRow; //The beginning of this grid section.
	  int StartColumn;
	  
	  public WaterThread(int r, int c,int SR, int SC, int ID ,BufferedImage[] buff) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; ThreadID = ID; Buff = buff;
		Height = Terrain.height;
		WaterUnitArr = WaterGrid.GetArray();
		img = SwingWaterThread.img; 
	  }
	  public WaterThread(int r, int c,int SR, int SC, BufferedImage image, WaterFlowPanel Wfp) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; 
		img = image; 
		Height = Terrain.height;
		WaterUnitArr = WaterGrid.GetArray();
		wfp = Wfp;
	  }
	  public void run(){
			if(SwingWaterThread.isWorkingMethod()){ //If we're in the working stage.
				//Do Mathematical water movements.
			}else{
				//Update Buffered Image.
				 BufferedImage Section = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
				 for(int i = StartRow; i<(StartRow+rows); i++){
					for(int j = StartColumn; j<(StartColumn+columns); j++){
						if(WaterUnitArr[i][j].Active){ //If the point has not been checked, check it.
							Section.setRGB(i, j, WaterUnitArr[i][j].col.getRGB());
						}
				}

			}
			Buff[ThreadID] = Section;
		  }
	  }
}
