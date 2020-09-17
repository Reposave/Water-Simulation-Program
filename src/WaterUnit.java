import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WaterUnit{
	//public int depth = 0;
	public AtomicInteger depth = new AtomicInteger(0);
	public AtomicBoolean Active = new AtomicBoolean(false);
	public boolean DrawUnit = false;
	public Color col = null;

	private int WaterAmount = 1; //The amount of water to increment by.

	private int colormultiplier = 25;
	private int row = 0;
	private int column = 0;
	
	public WaterUnit(){
		
	}
	public synchronized void Activate(){
		if(!Active.get()){
			Active.set(true);
			DrawUnit = true;
			depth.set(WaterAmount);
			//red green blue alpha
			col = new Color(0, 175-(depth.get() * colormultiplier), 255, 255);
			//the greater the depth, the darker the colour.
		}else{
			depth.addAndGet(WaterAmount);
			UpdateColour();
		}
	}
	public void UpdateColour(){
		if(depth.get()==0){
			col=new Color(0, 0, 0, 0); //transparent.

		}else if(depth.get() >0 && depth.get()<=(175/colormultiplier)){
				int DepthCol = (int)(175-(depth.get() * colormultiplier));
				//System.out.println(depth);
				col = new Color(0, 175-(depth.get() * colormultiplier), 255, 255);
		}else{
				col = new Color(0, 0 , 255, 255); //max colour.
			}
	}

	public synchronized void Transfer(WaterUnit other){
		depth.addAndGet(-WaterAmount);
		other.Activate();
		UpdateColour();
	}
	public synchronized void Revalidate(){
		if(depth.get()==0){
			Active.set(false);
		}
	}
	public void CheckDrawValidity(){
		if(depth.get()==0){ //No one else will be changing the depth during the stage when we're printing the image.
			DrawUnit = false;
		}
	}
}
