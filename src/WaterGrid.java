/**WaterGrid  will create and store the WaterUnit array.
 * @author Ardo Dlamini
 * @version 1.0
 */
public class WaterGrid{
	public static WaterUnit[][] WaterUnitArray;
	private static int rows = 0;
	private static int columns = 0; 
	
	/**The constructor takes in an x and y coordinate then creates a WaterUnit array.
	 * @param x number of rows
	 * @param y number of columns
	*/
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
	/**Returns the WaterUnit Array.
	 * @return WaterUnit[][]
     */
	public static WaterUnit[][] GetArray(){
		return WaterUnitArray;
	}
	
	
}
