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

/**The SwingWorker thread class that will control the 4 threads that operate and the display frame.
	* @author Ardo Dlamini	
	*/
public class SwingWaterThread extends SwingWorker<BufferedImage, Object>  {
	  public static BufferedImage img;//WaterDraw img.
	  static BufferedImage[] Buff = new BufferedImage[4];

	  public static AtomicBoolean isPaused = new AtomicBoolean(true);
	  boolean updateFrame = true; //do not change.
	  public static AtomicBoolean isWorking = new AtomicBoolean(false);
	  public static AtomicBoolean isReset = new AtomicBoolean(false);
	  private static boolean StopThread = false;

	  private static WaterUnit[][] WaterUnitArr;
	  private static float[][] Height;

	  static WaterFlowPanel wfp;

	  static int TimeStep = 0; 
	
      int rows;
	  int columns;

	  int StartRow; //The beginning of this grid section.
	  int StartColumn;

	  public Thread TL;
	  public Thread TR;
	  public Thread BL;
	  public Thread BR;
	  
	  /**The SwingWaterThread constructor takes in the coordinates of the array. 
	 * @param r The number of rows the section covers.
	 * @param c	The number of columns the section covers.
	 * @param SR The starting row coordinate of the section.
	 * @param SC The starting column coordinate of the section.
	 * @param image The image from the WaterDraw class.
	 * @param wfp The WaterFlowPanel
	*/
	  public SwingWaterThread(int r, int c,int SR, int SC, BufferedImage image, WaterFlowPanel Wfp) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; 
		img = image; 
		Height = Terrain.height;
		WaterUnitArr = WaterGrid.GetArray();
		wfp = Wfp;
	  }
	
	  @Override
	  protected BufferedImage doInBackground() throws Exception{
				StartWork(); //Only ThreadID 1 can do this task.
				return null;
	  }
	  public static boolean isWorkingMethod(){ //WaterThreads use this to determine whether it is time to display the WaterUnits or calculate the movement.
			return isWorking.get();
	  }
	  public static void PauseWork(){
			isPaused.set(true);
	  }
	  public static void ResumeWork(){
			isPaused.set(false);
	  }
	  public static void Reset(){
			isReset.set(true);
			TimeStep = 0;
			Flow.timer.setText(""+TimeStep);
	  }
	  public static void Stop(){
			StopThread=true;
	  }
	  public void UpdateTimeStep(){
			Flow.timer.setText(""+TimeStep);
	  }

	  /**The StartWork method will manage how the threads operate. From Pausing, resetting and resuming the work the threads have to do.
	 * @exception Exception when acquiring/releasing a Sempahore lock fails
	*/
	  public void StartWork() throws Exception{

			ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt){
						    //This ActionListener waits for the SwingTimer to trigger it.
                   				 updateFrame = true;
								 wfp.repaint();
						}
			};

			Timer timer = new Timer(40 ,taskPerformer); //25fps 40 milliseconds
			timer.setRepeats(true);
			timer.start();

			int SplitRow = Math.round(rows/2);
			int SplitColumn = Math.round(columns/2);

			WaterThread TLW = new WaterThread(SplitRow,SplitColumn,StartRow,StartColumn,0,Buff);
			WaterThread TRW = new WaterThread(SplitRow,columns-SplitColumn,StartRow,SplitColumn+StartColumn,1,Buff);
			WaterThread BLW = new WaterThread(rows-SplitRow,SplitColumn,SplitRow+StartRow,StartColumn,2,Buff);
			WaterThread BRW = new WaterThread(rows-SplitRow,columns-SplitColumn,SplitRow+StartRow,SplitColumn+StartColumn,3,Buff);
			
			BR = new Thread(BRW);

			while(!StopThread){

				if(updateFrame){	
				isWorking.set(false);
				 synchronized (WaterGrid.class){ //If working on the array, do not let anyone else access it.
					 TL = new Thread(TLW);
	  		 		 TR = new Thread(TRW);
	  		 		 BL = new Thread(BLW);

					 TL.start();
					 TR.start();
					 BL.start();
					 BR.run();
					 
					 TL.join();
				  	 TR.join();
					 BL.join();

					wfp.PaintResult(Buff[0],Buff[1],Buff[2],Buff[3]);

					updateFrame = false;
					}
				isWorking.set(true);
				}
				
				if(isReset.get()){
					isWorking.set(false);
					 synchronized (WaterGrid.class){
					//The update frame stage sometimes may not finish resetting so new threads need to be created to finish the job.
					TL = new Thread(TLW);
		  		 	TR = new Thread(TRW);
		  		 	BL = new Thread(BLW);
		  		 	

					TL.start();
					TR.start();
					BL.start();
				    BR.run();
					 
					TL.join();
				  	TR.join();
					BL.join();
					
					isReset.set(false);
					isWorking.set(true);
					}
				}

				if(isPaused.get()){ //If we are paused, never move the water.
					isWorking.set(false);		
				}else{
					//Does not need synchronization because in order to work on a waterunit, you need their lock first.			
					 isWorking.set(true);
					 TL = new Thread(TLW);
	  		 		 TR = new Thread(TRW);
	  		 		 BL = new Thread(BLW);
	  		 		 

					 TL.start();
					 TR.start();
					 BL.start();
					 BR.run();
					
					 TL.join();
				  	 TR.join();
					 BL.join();

					 TimeStep++;
					 UpdateTimeStep();
				}
				
			}
			timer.stop();
			cancel(true);
		}
	
}
