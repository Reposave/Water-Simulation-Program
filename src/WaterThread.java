import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.Math;

/**The Fork-Join thread class that will operate on the matrix Terrain data.
	* @author Ardo Dlamini	
	*/
public class WaterThread extends RecursiveTask<BufferedImage>  {
	  private static BufferedImage img;//WaterDraw img.
	  private int ThreadID = 1;

	  public static AtomicBoolean isPaused = new AtomicBoolean(false);
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
	  public WaterThread(int r, int c,int SR, int SC, BufferedImage image) { 
	    rows=r; columns=c; StartRow = SR; StartColumn = SC; 
		img = image; 
		Height = Terrain.height;
		WaterUnitArr = WaterGrid.GetArray();
	  }

	  protected  compute(){
			if(ThreadID == 1){
				StartWork(); //Only ThreadID 1 can do this task.
			}
			else if(isWorking.get()){ //If we're in the working stage.
				//Do Mathematical water movements.
			}else{
				//Update Buffered Image.
			}
		  }

	  public void StartWork(){

			 int SplitRow = Math.round(rows/2);
			 int SplitColumn = Math.round(columns/2);

			 WaterThread TL = new WaterThread(SplitRow,SplitColumn,StartRow,StartColumn,2);
	  		 WaterThread TR = new WaterThread(SplitRow,columns-SplitColumn,StartRow,SplitColumn+StartColumn,3);
	  		 WaterThread BL = new WaterThread(rows-SplitRow,SplitColumn,SplitRow+StartRow,StartColumn,4);
	  		 WaterThread BR = new WaterThread(rows-SplitRow,columns-SplitColumn,SplitRow+StartRow,SplitColumn+StartColumn,5);

			while(true){

				 synchronized (WaterGrid.class){ //If working on the array, do not let anyone else access it.
					 TL.fork();
					 TR.fork();
					 BL.fork();
					 BufferedImage imgBR = BR.compute();

					 BufferedImage imgTL = TL.join();
				  	 BufferedImage imgTR = TR.join();
					 BufferedImage imgBL = BL.join();

					 int width = img.getWidth();
					 int height = img.getHeight();

					 BufferedImage combined = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

					// paint all 4 images, preserving the alpha channels
					Graphics g = combined.getGraphics();
					g.drawImage(imgTL, 0, 0, null);
					g.drawImage(imgTR, 0, 0, null);
					g.drawImage(imgBR, 0, 0, null);
					g.drawImage(imgBL, 0, 0, null);

					g.dispose();
		
					img = combined;
					wfp.repaint();
				}

				isWorking.set(!isWorking.get());
				//Allows the MouseClick to add water and reduce latency.

				if(isPaused.get()){ //If we are paused, never move the water.
					isWorking.set(false);		
				}else{
					synchronized (WaterGrid.class){ //If working on the array, do not let anyone else access it.
						 TL.fork();
						 TR.fork();
						 BL.fork();
						 BR.compute();

						 TL.join();
					  	 TR.join();
						 BL.join();
					}
				}
			}
		}

	 private void CheckNeighbours(int r, int c){

	 }
	
}
