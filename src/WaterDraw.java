import java.io.File;
import java.awt.image.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class WaterDraw {
	int dimx, dimy; // data dimensions
	BufferedImage img; // greyscale image for displaying the terrain top-down

	// overall number of elements in the height grid
	int dim(){
		return dimx*dimy;
	}
	
	// get x-dimensions (number of columns)
	int getDimX(){
		return dimx;
	}
	
	// get y-dimensions (number of rows)
	int getDimY(){
		return dimy;
	}
	
	// get greyscale image
	public BufferedImage getImage() {
		  return img;
	}
	
	// convert height values to greyscale colour and populate an image
	void deriveImage()
	{
		img = new BufferedImage(dimy, dimx, BufferedImage.TYPE_INT_ARGB);

		for(int x=0; x < dimx; x++){
			for(int y=0; y < dimy; y++) {
				 // find normalized height value in range
				 Color col = new Color(123, 0, 0, 50);
				 img.setRGB(x, y, col.getRGB());
			}
		}
	}
	public void paintPixels(int x, int y){
		//print a 10*10 square.
		int XUBound =x+5;
		int YUBound =y+5;
		int XLBound = x-5;
		int YLBound = y-5;

		if(x+5>dimx){
			XUBound = dimx;
		}else if(x-5<0){
			XLBound	= 0;		
			}
		if(y-5<0){
			YLBound = 0;
			}
		else if(y+5>dimy){
			YUBound = dimy;
			}
		
		for(int i=XLBound; i < XUBound; i++){
			for(int j=YLBound; j < YUBound; j++) {
				 WaterGrid.WaterUnitArray[i][j].Activate();
				 Color col = new Color(0, 175-25, 255, 255);
				 img.setRGB(i, j, col.getRGB());
			}
		}
	}	
	
	// read in terrain from file
	void readData(String fileName){ 
		try{ 

			Scanner sc = null;
			File f = new File("../SampleData/"+fileName);
			if(f.isFile()){
				sc=new Scanner(f);
			}else{
				sc=new Scanner(new File("../NewTerrain/"+fileName));					
			}

			// read grid dimensions
			// x and y correpond to columns and rows, respectively.
			// Using image coordinate system where top left is (0, 0).
			dimy = sc.nextInt(); 
			dimx = sc.nextInt();
			sc.close(); 
			
			// generate greyscale heightfield image
			deriveImage();
		} 
		catch (IOException e){ 
			System.out.println("Unable to open input file "+fileName);
			e.printStackTrace();
		}
		catch (java.util.InputMismatchException e){ 
			System.out.println("Malformed input file "+fileName);
			e.printStackTrace();
		}
	}
}
