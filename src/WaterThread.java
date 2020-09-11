import java.util.concurrent.RecursiveAction;
import java.lang.Math;

/**The Fork-Join thread class that will operate on the matrix Terrain data.
	* @author Ardo Dlamini	
	*/
public class GridThread extends RecursiveAction  {
	  int rows; // arguments
	  int columns;

	  int StartRow; //The beginning of this grid section.
	  int StartColumn;

	  gridpoint[][] arr;
	  static int SEQUENTIAL_CUTOFF=1000;
	  static int NUM_OF_THREADS = 1;

	  //int ans = 0; // result 

	  /**The GridThread constructor takes in the array of grid points and a section of the coordinates of the array depending on how the work was split. 
	 * @param a
	 * @param r The number of rows the section covers.
	 * @param c	The number of columns the section covers.
	 * @param SR The starting row coordinate of the section.
	 * @param SC The starting column coordinate of the section.
	*/
	  GridThread(gridpoint[][] a, int r, int c,int SR, int SC) { 
	    rows=r; columns=c; arr=a; StartRow = SR; StartColumn = SC;
	  }
	  public static void CHANGE_CUTOFF(int c){
		SEQUENTIAL_CUTOFF=c;
		}
	  public static void RESET_NUM_OF_THREADS(){
		NUM_OF_THREADS = 1;
		}
      public static int GetNUM_OF_THREADS(){
		return NUM_OF_THREADS;
		}
	  /**The Compute method in each thread will compare the size of the array against the Sequential cut off. If the number of elements in the row-column range is lower than or equal to the sequential cut off, the thread will first verify if the gridpoint it will check has been checked before. If not then it will perform the height comparisons in the CheckNeighbours method.
	  */
	  protected void compute(){// return answer - instead of run
		  if((rows*columns) <= SEQUENTIAL_CUTOFF) {
			  for(int i = StartRow; i<(StartRow+rows); i++){
				for(int j = StartColumn; j<(StartColumn+columns); j++){
					//String s = fileIn.next();
					if(!arr[i][j].checked){ //If the point has not been checked, check it.
						CheckNeighbours(i,j);
					}
					//System.out.println(dataArray[i][j]);
				}
		  	}
		  }
		  else {

			  if(columns<=SEQUENTIAL_CUTOFF){
			  //Split the rows in half.
				  int SplitRow = Math.round(rows/2);

				  GridThread TL = new GridThread(arr,SplitRow,columns,StartRow,StartColumn);
				  GridThread BL = new GridThread(arr,rows-SplitRow,columns,SplitRow+StartRow,StartColumn);

				  NUM_OF_THREADS+=1;

				  TL.fork();
				  BL.compute();

				  TL.join();

			  }else{
				//Split the columns in half.
				  int SplitColumn = Math.round(columns/2);

				  GridThread TL = new GridThread(arr,rows,SplitColumn,StartRow,StartColumn);
			 	  GridThread TR = new GridThread(arr,rows,columns-SplitColumn,StartRow,SplitColumn+StartColumn);
					
				  NUM_OF_THREADS+=1;

				  TL.fork();
				  TR.compute();

				  TL.join();
			 }

			 //Old algorithm

			 /*int SplitRow = Math.round(rows/2);
			  int SplitColumn = Math.round(columns/2);
			  //System.out.print(".");
			  //Top Left, Top Right, Bottom Left, Bottom Right.
			  GridThread TL = new GridThread(arr,SplitRow,SplitColumn,StartRow,StartColumn);
			  GridThread TR = new GridThread(arr,SplitRow,columns-SplitColumn,StartRow,SplitColumn+StartColumn);
			  GridThread BL = new GridThread(arr,rows-SplitRow,SplitColumn,SplitRow+StartRow,StartColumn);
			  GridThread BR = new GridThread(arr,rows-SplitRow,columns-SplitColumn,SplitRow+StartRow,SplitColumn+StartColumn);

			  NUM_OF_THREADS+=3;

			  TL.fork();
			  TR.fork();
			  BL.fork();
			  BR.compute();

			  TL.join();
			  TR.join();
			  BL.join();
			  */
			  //int SplitColumn = Math.round(columns/2);

		  }
	 }
	 /**This method compares the height of the current coordinate, the thread is checking, against the neighbouring 8 coordinates. A successful pass of the height being less than that of the 8 coordinates means it is a basin and every other surrounding point is not. A fail means it is not a basin.
	 * @param r Current row coordinate
	 * @param c Current column coordinate
	*/
	 private void CheckNeighbours(int r, int c){
		int i = 0;
		float ThisHeight = arr[r][c].height+ 0.01f;
		
		if(arr[r-1][c-1].height>=(ThisHeight)){
			arr[r-1][c-1].checked = true;
			i++;}
	    else{
			i--;			
		}

		if(arr[r-1][c].height>=(ThisHeight)){
			arr[r-1][c].checked = true;
			i++;}
		else{
			i--;			
		}

		if(arr[r-1][c+1].height>=(ThisHeight)){
			arr[r-1][c+1].checked = true;
			i++;}
		else{
			i--;			
		}

		if(arr[r][c-1].height>=(ThisHeight)){
			arr[r][c-1].checked = true;
			i++;}
		else{
			i--;			
		}
		
		if(arr[r][c+1].height>=(ThisHeight)){
			arr[r][c+1].checked = true;
			i++;}
		else{
			i--;			
		}

		if(arr[r+1][c-1].height>=(ThisHeight)){
			arr[r+1][c-1].checked = true;
			i++;}
		else{
			i--;			
		}

		if(arr[r+1][c].height>=(ThisHeight)){
			arr[r+1][c].checked = true;
			i++;}
		else{
			i--;			
		}

		if(arr[r+1][c+1].height>=(ThisHeight)){
			arr[r+1][c+1].checked = true;
			i++;}
		else{
			i--;			
		}

		if(i >= 8){
			arr[r][c].basin = true;
			//System.out.print(r+"-"+c+" ");
			arr[r][c].checked = true;
		}else{
			arr[r][c].checked = true;
		}
	 }
	
}
