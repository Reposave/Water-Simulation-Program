import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.Lock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**WaterUnit class that represents 1 pixel of water on the display.
 * @author Ardo Dlamini
 * @version 1.0
 */
public class WaterUnit{
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
		//
	}

	/**This method tries to Activate a WaterUnit. If it is not active, the depth is set to the WaterAmount and given a base blue colour.
     */
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
	/**Checks the current depth and sets the corresponding colour for that depth.
     */
	public void UpdateColour(){
		if(depth.get()==0){
			col=new Color(0, 0, 0, 0); //transparent.

		}else if(depth.get() >0 && depth.get()<=(175/colormultiplier)){
				int DepthCol = (int)(175-(depth.get() * colormultiplier));
				col = new Color(0, 175-(depth.get() * colormultiplier), 255, 255);
		}else{
				col = new Color(0, 0 , 255, 255); //max colour.
			}
	}
	/**Returns the current Depth of the WaterUnit
	 * @return int
     */
	public int Depth(){
		return depth.get();
	}
	/**Resets the WaterUnit when it has reached the boundary. Will try to get the lock first.
     */
	public void BoundReset()throws Exception{
		if(lk.tryAcquire()){
			depth.set(0);
			lk.release();
			Active.set(false);
		}
	}
	/**This method subtracts a WaterAmount from itself and tries to Activate the other WaterUnit.
     * @param other This is the other WaterUnit Object
	 * @return int
     */
	public void Transfer(WaterUnit other){
		depth.addAndGet(-WaterAmount); //The depth can only get lower so the program won't cause errors which is why we do not grab our own Semaphore.
		other.Activate();
		UpdateColour();
	}
	/**If the depth is 0, deactivate this WaterUnit.
     */
	public void Revalidate(){
		if(depth.get()==0){
			Active.set(false);
		}
	}
	/**Resets the WaterUnit back to its default state.
     */
	public void Reset(){
		depth.set(0);
		Active.set(false);
		col = new Color(0, 0, 0, 0);
		DrawUnit = false;
	}
	/**If the depth is 0, do not draw this Unit.
     */
	public void CheckDrawValidity(){
		if(depth.get()==0){ //No one else will be changing the depth during this stage when we're printing the image.
			DrawUnit = false;
		}
	}
}
