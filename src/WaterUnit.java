import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.Lock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class WaterUnit{
	//public int depth = 0;
	public AtomicInteger depth = new AtomicInteger(0);
	public AtomicBoolean Active = new AtomicBoolean(false);
	public boolean DrawUnit = false;
	public Color col = null;
	public Semaphore lk = new Semaphore(10);

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
			lk.release();

			//red green blue alpha
			col = new Color(0, 175-(depth.get() * colormultiplier), 255, 255);
			//the greater the depth, the darker the colour.
		}else{
			depth.addAndGet(WaterAmount);
			lk.release();
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
	public int Depth(){
		//lk.tryAcquire();
		return depth.get();
		//lk.release();
	}
	public void BoundReset()throws Exception{
		if(lk.tryAcquire()){
			depth.set(0);
			lk.release();
			Active.set(false);
		}
	}
	public void Transfer(WaterUnit other){
		depth.addAndGet(-WaterAmount); //The depth can only get lower so the program won't cause errors.
		other.Activate();
		UpdateColour();
	}
	public void Revalidate(){
		if(depth.get()==0){
			Active.set(false);
		}
	}
	public void Reset(){
		depth.set(0);
		Active.set(false);
		col = new Color(0, 0, 0, 0);
		DrawUnit = false;
	}
	public void CheckDrawValidity(){
		if(depth.get()==0){ //No one else will be changing the depth during the stage when we're printing the image.
			DrawUnit = false;
		}
	}
}
