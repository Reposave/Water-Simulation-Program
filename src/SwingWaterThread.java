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
public class SwingWaterThread extends SwingWorker<BufferedImage, Object>  {
	  public static BufferedImage img;//WaterDraw img.
	  private static BufferedImage out;
	  static BufferedImage[] Buff = new BufferedImage[4];

	  public static AtomicBoolean isPaused = new AtomicBoolean(false);
	  boolean updateFrame = true;
	  public static AtomicBoolean isWorking = new AtomicBoolean(false);

	  private static WaterUnit[][] WaterUnitArr;
	  private static float[][] Height;

	  static WaterFlowPanel wfp;
	
      int rows; // arguments
	  int columns;

	  int StartRow; //The beginning of this grid section.
	  int StartColumn;

	  public Thread TL;
	  public Thread TR;
	  public Thread BL;
	  public Thread BR;
	  
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
	  public static boolean isWorkingMethod(){
			return isWorking.get();
	  }

	  public void StartWork() throws Exception{

			out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

			ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt){
						    //...Perform a task..
                   				 updateFrame = true;
								 wfp.repaint();
							//wfp.Update(out);
							//wfp.repaint();

			//System.out.println("Reading SMTP Info.");
						}
			};

			Timer timer = new Timer(40 ,taskPerformer); //25fps 40 milli
			timer.setRepeats(true);
			timer.start();

			int SplitRow = Math.round(rows/2);
			int SplitColumn = Math.round(columns/2);

			WaterThread TLW = new WaterThread(SplitRow,SplitColumn,StartRow,StartColumn,0,Buff);
			WaterThread TRW = new WaterThread(SplitRow,columns-SplitColumn,StartRow,SplitColumn+StartColumn,1,Buff);
			WaterThread BLW = new WaterThread(rows-SplitRow,SplitColumn,SplitRow+StartRow,StartColumn,2,Buff);
			WaterThread BRW = new WaterThread(rows-SplitRow,columns-SplitColumn,SplitRow+StartRow,SplitColumn+StartColumn,3,Buff);

			//TL = new Thread(TLW);
	  		//TR = new Thread(TRW);
	  		//BL = new Thread(BLW);
	  		//BR = new Thread(BRW);

			while(true){
				TL = new Thread(TLW);
	  		 	TR = new Thread(TRW);
	  		 	BL = new Thread(BLW);
	  		 	BR = new Thread(BRW);

				UpdateImage();
			}
		}

	public void UpdateImage() throws Exception {

		if(updateFrame){
				
				isWorking.set(false);
				 synchronized (WaterGrid.class){ //If working on the array, do not let anyone else access it.
					 TL.start();
					 TR.start();
					 BL.start();
					 BR.run();
					 
					 //BufferedImage imgTL = TL.join();
				  	 //BufferedImage imgTR = TR.join();
					 //BufferedImage imgBL = BL.join();

					 TL.join();
				  	 TR.join();
					 BL.join();

					wfp.PaintResult(Buff[0],Buff[1],Buff[2],Buff[3]);

					updateFrame = false;
					}
				isWorking.set(true);
				}
				//Allows the MouseClick to add water and reduce latency.

				if(isPaused.get()){ //If we are paused, never move the water.
					isWorking.set(false);		
				}else{
					/*synchronized (WaterGrid.class){ //If working on the array, do not let anyone else access it.
						 TL.execute();
						 TR.execute();
						 BL.execute();
						 BR.doInBackground();

						 TL.get();
					  	 TR.get();
						 BL.get();
					}*/
				}
	}
	
}
