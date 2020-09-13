import java.awt.Color;

public class WaterUnit{
	private int depth = 0;
	private boolean Active = false;
	private Color col = null;
	private int colormultiplier = 25;
	private int row = 0;
	private int column = 0;
	
	public WaterUnit(int row, int column){
		
	}
	public synchronized void Activate(){
		if(!Active){
			Active =  true;
			depth = 1;
			//red green blue alpha
			col = new Color(0, 175-(depth * colormultiplier), 255, 255);
			//the greater the depth, the darker the colour.
		}else{
			depth = depth + 1;
			UpdateColour();
		}
	}
	public void UpdateColour(){
		col = new Color(0, 175-(depth * colormultiplier), 255, 255);
	}
}
