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
	  private static BufferedImage img;//WaterDraw img.
	  private static BufferedImage out;
	  private int ThreadID = 1;

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

	  public WaterThread TL;
	  public WaterThread TR;
	  public WaterThread BL;
	  public WaterThread BR;
	  
	  public WaterThread(int r, int c,int SR, int SC, int ID) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; ThreadID = ID;
	  }
	  public WaterThread(int r, int c,int SR, int SC, BufferedImage image, WaterFlowPanel Wfp) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; 
		img = image; 
		Height = Terrain.height;
		WaterUnitArr = WaterGrid.GetArray();
		wfp = Wfp;
	  }
	  @Override
	  protected BufferedImage doInBackground() throws Exception{
			if(ThreadID == 1){
				StartWork(); //Only ThreadID 1 can do this task.
			}
			else if(isWorking.get()){ //If we're in the working stage.
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
				return Section;
		  }
		  return new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
	  }

	  public void StartWork() throws Exception{

			out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

			ActionListener taskPerformer = new ActionListener() {

			public void actionPerformed(ActionEvent evt){
						    //...Perform a task...
							try {
                   				 updateFrame = true;
								 UpdateImage();
								 wfp.repaint();
               					 }
               			    catch(Exception ex)
                			{
                     			System.out.println("Image is null");
               				 }
							//wfp.Update(out);
							//wfp.repaint();

			//System.out.println("Reading SMTP Info.");
						}
			};

			Timer timer = new Timer(1000 ,taskPerformer); //25fps 40 milli
			timer.setRepeats(true);
			timer.start();

			 int SplitRow = Math.round(rows/2);
			 int SplitColumn = Math.round(columns/2);

			 TL = new WaterThread(SplitRow,SplitColumn,StartRow,StartColumn,2);
	  		 TR = new WaterThread(SplitRow,columns-SplitColumn,StartRow,SplitColumn+StartColumn,3);
	  		 BL = new WaterThread(rows-SplitRow,SplitColumn,SplitRow+StartRow,StartColumn,4);
	  		 BR = new WaterThread(rows-SplitRow,columns-SplitColumn,SplitRow+StartRow,SplitColumn+StartColumn,5);

			//while(true){
				
			//}
		}

	public void UpdateImage() throws Exception {
		if(updateFrame){
				 synchronized (WaterGrid.class){ //If working on the array, do not let anyone else access it.
					 TL.execute();
					 TR.execute();
					 BL.execute();
					 BR.execute();
					 //BufferedImage imgBR = BR.doInBackground();
					
					 BufferedImage imgTL = TL.get();
				  	 BufferedImage imgTR = TR.get();
					 BufferedImage imgBL = BL.get();
					 BufferedImage imgBR = BR.get();

					 int width = img.getWidth();
					 int height = img.getHeight();

					 //BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

					// paint all 4 images, preserving the alpha channels
					//Graphics g = combined.getGraphics();
					//g.drawImage(imgTL, 0, 0, null);
					//g.drawImage(imgTR, 0, 0, null);
					//g.drawImage(imgBR, 0, 0, null);
					//g.drawImage(imgBL, 0, 0, null);

					//g.dispose();
		
					//out = combined;
					wfp.PaintResult(imgTL,imgTR,imgBL,imgBR);

					updateFrame = false;
					}
				}

				isWorking.set(!isWorking.get());
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

	 private void CheckNeighbours(int r, int c){

	 }
	
}
