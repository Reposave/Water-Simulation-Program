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
import java.util.concurrent.TimeUnit;

/**The thread class that will operate on the matrix Terrain and the WaterUnit array data.
	* @author Ardo Dlamini
	* @version 1.0 
	*/
public class WaterThread implements Runnable  {
	  private static BufferedImage img;//WaterDraw img.
	  private int ThreadID = 1;
	  BufferedImage[] Buff;

	  private static WaterUnit[][] WaterUnitArr;
	  private static float[][] Height;
	  private int[] loc = new int[2];

	  static WaterFlowPanel wfp;
	
      int rows;
	  int columns;

	  int StartRow; //The beginning of this grid section.
	  int StartColumn;

	  /**The WaterThread constructor takes in a section of the coordinates of the array depending on how the work was split. It is also assigned an ID which will be used in the BufferedImage array to determine index positions to place the renders. 
	 * @param r The number of rows the section covers.
	 * @param c	The number of columns the section covers.
	 * @param SR The starting row coordinate of the section.
	 * @param SC The starting column coordinate of the section.
	 * @param ID The threads number used to place renders in an output BufferedImage array.
	 * @param buff The BufferedImage array.
	*/
	  public WaterThread(int r, int c,int SR, int SC, int ID ,BufferedImage[] buff) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; ThreadID = ID; Buff = buff;
		Height = Terrain.height;
		WaterUnitArr = WaterGrid.GetArray();
		img = SwingWaterThread.img; 
	  }
	  
	  /**The run method in each thread will begin by first checking what working stage we are in. If true, try and find Active WaterUnits. Active WaterUnits will be reset at boundaries but will be moved if not on the boundary. If working stage is false, we either draw the Water Units and place the Images in a BufferedImage array or reset them.
	  */
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

						if(WaterUnitArr[loc[0]][loc[1]].Active.get()){
								
								if((loc[0]==0||loc[1]==0)||(loc[0]==img.getHeight()-1||loc[1]==img.getWidth()-1)){
									synchronized (WaterUnitArr[loc[0]][loc[1]]){
											try{
												WaterUnitArr[loc[0]][loc[1]].BoundReset();
											}catch(Exception e){
												System.out.println("Interrupted Semaphore acquiring.");
											}
										}
								}else{
									synchronized (WaterUnitArr[loc[0]][loc[1]]){
										try{
											CheckNeighbours(loc[0],loc[1]);
										}catch(Exception e){
											System.out.println("Interrupted Semaphore acquiring.");
											}
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
							if(!SwingWaterThread.isReset.get()){
								Section.setRGB(i, j, WaterUnitArr[i][j].col.getRGB());
								WaterUnitArr[i][j].CheckDrawValidity();
							}else{
								WaterUnitArr[i][j].Reset();
								Section.setRGB(i, j, WaterUnitArr[i][j].col.getRGB());
							}
						}
				}

			}
			Buff[ThreadID] = Section;
		  }
	  }
	 /**This method compares the combined height and depth of the current coordinate, the thread is checking, against the neighbouring 8 coordinates. When the lowest coordinate is found, water is transferred to it. WaterUnit objects have Semaphore locks that must be obtained first before checking the Water Surface.
	 * @param r Current row coordinate
	 * @param c Current column coordinate
	 * @exception Exception when acquiring/releasing of the lock fails
	*/
	 private void CheckNeighbours(int r, int c)throws Exception{
		
		float i = 0f;
		float other =0f;
		float ThisHeight = Height[r][c] + (WaterUnitArr[r][c].depth.get()* 0.01f);
		i = ThisHeight;
		WaterUnit lowest=null;
		
		if(WaterUnitArr[r-1][c-1].lk.tryAcquire()){ //First attempt to grab the Semaphore.
			other=Height[r-1][c-1] + (0.01f * WaterUnitArr[r-1][c-1].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r-1][c-1];
			}else{
				WaterUnitArr[r-1][c-1].lk.release(); //Release the Semaphore if condition not met.
			}
		}

		if(WaterUnitArr[r-1][c].lk.tryAcquire()){
			other=Height[r-1][c] + (0.01f * WaterUnitArr[r-1][c].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r-1][c];
			}else{
				WaterUnitArr[r-1][c].lk.release();
			}
		}

		if(WaterUnitArr[r-1][c+1].lk.tryAcquire()){
			other=Height[r-1][c+1] + (0.01f * WaterUnitArr[r-1][c+1].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r-1][c+1];
			}else{
				WaterUnitArr[r-1][c+1].lk.release();
			}
		}

		if(WaterUnitArr[r][c-1].lk.tryAcquire()){
			other=Height[r][c-1] + (0.01f * WaterUnitArr[r][c-1].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r][c-1];
			}else{
				WaterUnitArr[r][c-1].lk.release();
			}
		}

		if(WaterUnitArr[r][c+1].lk.tryAcquire()){
			other=Height[r][c+1] + (0.01f * WaterUnitArr[r][c+1].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r][c+1];
			}else{
				WaterUnitArr[r][c+1].lk.release();
			}
		}

		if(WaterUnitArr[r+1][c-1].lk.tryAcquire()){
			other=Height[r+1][c-1] + (0.01f * WaterUnitArr[r+1][c-1].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r+1][c-1];
			}else{
				WaterUnitArr[r+1][c-1].lk.release();
			}
		}
	
		if(WaterUnitArr[r+1][c].lk.tryAcquire()){
			other=Height[r+1][c] + (0.01f * WaterUnitArr[r+1][c].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r+1][c];
			}else{
				WaterUnitArr[r+1][c].lk.release();
			}
		}

		if(WaterUnitArr[r+1][c+1].lk.tryAcquire()){
			other=Height[r+1][c+1] + (0.01f * WaterUnitArr[r+1][c+1].Depth());
			if(other < i){
				i=other;
				lowest = WaterUnitArr[r+1][c+1];
			}else{
				WaterUnitArr[r+1][c+1].lk.release();
			}
		}

		if(i != ThisHeight){
			WaterUnitArr[r][c].Transfer(lowest);
			//The one who runs this method will always have it's own lock and the other's Semaphore.
		}
	 }
}

