public class WaterGrid{
	public static WaterUnit[][] WaterUnitArray;
	private static int rows = 0;
	private static int columns = 0; 

	public WaterGrid(int x, int y){
		rows = x;
		columns = y;

		WaterUnitArray = new WaterUnit[x][y];

		for(int i = 0; i<rows; i++){
			for(int j = 0; j<columns; j++){
				WaterUnitArray[i][j] = new WaterUnit();
			}
		}

	}

	public static WaterUnit[][] GetArray(){
		return WaterUnitArray;
	}
	
	
}
