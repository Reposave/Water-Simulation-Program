import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import java.awt.BorderLayout;
import java.awt.Color;

import java.util.concurrent.ForkJoinPool;

/**This class creates the GUI
 * @author Ardo Dlamini
 * @version 1.0
*/
public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	static WaterFlowPanel wfp;
	static SwingWaterThread a;

	static WaterDraw water;
	
	static final ForkJoinPool fjPool = new ForkJoinPool();

	public static JLabel timer;

	// start timer
	private static void tick(){
		startTime = System.currentTimeMillis();
	}
	
	// stop timer, return time elapsed in seconds
	private static float tock(){
		return (System.currentTimeMillis() - startTime) / 1000.0f; 
	}
	
	public static void setupGUI(int frameX,int frameY,Terrain landdata,WaterDraw water) {
		
		Dimension fsize = new Dimension(800, 800);
    	JFrame frame = new JFrame("Waterflow"); 
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.getContentPane().setLayout(new BorderLayout());
    	
      	JPanel g = new JPanel();
        g.setLayout(new BoxLayout(g, BoxLayout.PAGE_AXIS)); 

   		JLayeredPane lp = frame.getLayeredPane(); //Necessary to overlay the Water Flow Panel above the terrain.
		lp.setPreferredSize(new Dimension(frameX,frameY));

		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		
	   	
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));

		JButton endB = new JButton("End");
		JButton resetB = new JButton("Reset");
		JButton pauseB = new JButton("Pause");
		JButton playB = new JButton("Play");

		timer = new JLabel("0");
		timer.setPreferredSize(new Dimension(100, 0));
		
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				a.Stop();
				frame.dispose();
			}
		});
		pauseB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SwingWaterThread.PauseWork();
			}
		});
		playB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SwingWaterThread.ResumeWork();
			}
		});
		resetB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				SwingWaterThread.PauseWork(); //Stop Working on the array first.
				SwingWaterThread.Reset();
			}
		});

		b.add(endB);
		b.add(Box.createRigidArea(new Dimension(10, 0))); //This creates spaces between the buttons and the timer.
		b.add(resetB);
		b.add(Box.createRigidArea(new Dimension(10, 0)));
		b.add(pauseB);
		b.add(Box.createRigidArea(new Dimension(10, 0)));
		b.add(playB);
		b.add(Box.createRigidArea(new Dimension(10, 0)));
		b.add(timer);
		b.add(Box.createRigidArea(new Dimension(10, 0)));

		
		wfp = new WaterFlowPanel(water);
		water.insertWFP(wfp);

		wfp.setPreferredSize(fp.getSize());

		wfp.setBackground(new Color(250, 134, 145, 255));
		wfp.setOpaque(false); //This seems to have no effect on bufferedImages.

		g.add(wfp);
		g.setOpaque(false);

		g.add(b);
	
		g.setBounds(0, 0, frameX, frameY);

		fp.setBounds(0, 0, frameX, frameY);
		
		
		lp.add(fp, Integer.valueOf(1)); //layer 1
		lp.add(g, Integer.valueOf(2)); //layer 2
		
		a = new SwingWaterThread(water.dimx, water.dimy, 0, 0, water.img, wfp);
		a.execute();
		
        //This will capture mouse input on the WaterFLowPanel.
		wfp.addMouseListener(new MouseAdapter() {
   	    	@Override
   			 public void mouseClicked(MouseEvent e) {
				int x=e.getX();
    			int y=e.getY();
    			//System.out.println(x+","+y);//these co-ords are relative to the component
				water.paintPixels(x,y);
				
   			 }
		});
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
        frame.setVisible(true);

        Thread fpt = new Thread(fp);
        fpt.start();
		
		Thread wfpt = new Thread(wfp);
        wfpt.start();
		
	}
	
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();

		water = new WaterDraw();
		
		// check that number of command line arguments is correct
		if(args.length != 1)
		{
			System.out.println("Incorrect number of command line arguments. Should have form: java -jar flow.java intputfilename");
			System.exit(0);
		}
				
		// landscape information from file supplied as argument
		// 
		landdata.readData(args[0]);

		water.readData(landdata.getDimX(), landdata.getDimY());
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata, water));
		
		// to do: initialise and start simulation
	}
}
