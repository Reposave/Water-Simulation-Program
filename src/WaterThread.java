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
	  private int[] loc = new int[2];

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
				int index = ThreadID*((img.getHeight()*img.getWidth())/4);

				for(int i = StartRow; i<(StartRow+rows); i++){
					for(int j = StartColumn; j<(StartColumn+columns); j++){

						Terrain.getPermute(index,loc);
						index++;
						//loc[0] = i;
						//loc[1] = j;

						if(WaterUnitArr[loc[0]][loc[1]].Active.get()){ //If the point has not been checked, check it.
								
								if((loc[0]==0||loc[1]==0)||(loc[0]==img.getHeight()-1||loc[1]==img.getWidth()-1)){
									synchronized (WaterUnitArr[loc[0]][loc[1]]){
										WaterUnitArr[loc[0]][loc[1]].depth.set(0);
										WaterUnitArr[loc[0]][loc[1]].Revalidate();
										}
								}else{
									synchronized (WaterUnitArr[loc[0]][loc[1]]){
										CheckNeighbours(loc[0],loc[1]);
										WaterUnitArr[loc[0]][loc[1]].Revalidate();
									}
								}
								
						}
				}
			}
			}else{
				//Update Buffered Image.
				 BufferedImage Section = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
				 for(int i = StartRow; i<(StartRow+rows); i++){
					for(int j = StartColumn; j<(StartColumn+columns); j++){
						if(WaterUnitArr[i][j].DrawUnit){ //If the point has not been checked, check it.
							Section.setRGB(i, j, WaterUnitArr[i][j].col.getRGB());
							WaterUnitArr[i][j].CheckDrawValidity();
						}
				}

			}
			Buff[ThreadID] = Section;
		  }
	  }
	 private void CheckNeighbours(int r, int c){
		
		float i = 0f;
		float ThisHeight = Height[r][c] + (WaterUnitArr[r][c].depth.get()* 0.01f);
		i = ThisHeight;
		WaterUnit lowest=null;
			
		if(Height[r-1][c-1] + (0.01f * WaterUnitArr[r-1][c-1].depth.get()) < (i)){
			i=Height[r-1][c-1] + (0.01f * WaterUnitArr[r-1][c-1].depth.get());
			lowest = WaterUnitArr[r-1][c-1];
		}

		if(Height[r-1][c] + (0.01f * WaterUnitArr[r-1][c].depth.get()) < (i)){
			i=Height[r-1][c] + (0.01f * WaterUnitArr[r-1][c].depth.get());
			lowest = WaterUnitArr[r-1][c];
		}

		if(Height[r-1][c+1] + (0.01f * WaterUnitArr[r-1][c+1].depth.get()) < (i)){
			i= Height[r-1][c+1] + (0.01f * WaterUnitArr[r-1][c+1].depth.get());
			lowest = WaterUnitArr[r-1][c+1];
		}


		if(Height[r][c-1] + (0.01f * WaterUnitArr[r][c-1].depth.get()) < (i)){
			i=Height[r][c-1] + (0.01f * WaterUnitArr[r][c-1].depth.get());
			lowest = WaterUnitArr[r][c-1];
		}
		
		if(Height[r][c+1] + (0.01f * WaterUnitArr[r][c+1].depth.get()) < (i)){
			i=Height[r][c+1] + (0.01f * WaterUnitArr[r][c+1].depth.get());
			lowest = WaterUnitArr[r][c+1];
		}

		if(Height[r+1][c-1] + (0.01f * WaterUnitArr[r+1][c-1].depth.get()) < (i)){
			i=Height[r+1][c-1] + (0.01f * WaterUnitArr[r+1][c-1].depth.get());
			lowest = WaterUnitArr[r+1][c-1];
		}

		if(Height[r+1][c] + (0.01f * WaterUnitArr[r+1][c].depth.get()) < (i)){
			i=Height[r+1][c] + (0.01f * WaterUnitArr[r+1][c].depth.get());
			lowest = WaterUnitArr[r+1][c];
		}


		if(Height[r+1][c+1] + (0.01f * WaterUnitArr[r+1][c+1].depth.get()) < (i)){
			i=Height[r+1][c+1] + (0.01f * WaterUnitArr[r+1][c+1].depth.get());
			lowest = WaterUnitArr[r+1][c+1];
		}

		if(i != ThisHeight){
			WaterUnitArr[r][c].Transfer(lowest);
		}
	 }
}

