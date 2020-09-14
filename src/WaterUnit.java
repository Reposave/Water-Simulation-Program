import java.awt.Color;

public class WaterUnit{
	private int depth = 0;
	public boolean Active = false;
	public Color col = null;
	private int colormultiplier = 25;
	private int row = 0;
	private int column = 0;
	
	public WaterUnit(){
		
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
		int DepthCol = (175-(depth * colormultiplier));
		if(!(DepthCol <0)){
			col = new Color(0, 175-(depth * colormultiplier), 255, 255);
		}
	}
}
