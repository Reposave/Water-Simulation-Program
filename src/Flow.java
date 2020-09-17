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

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	static WaterFlowPanel wfp;

	static WaterDraw water;
	
	static final ForkJoinPool fjPool = new ForkJoinPool();

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

   		JLayeredPane lp = frame.getLayeredPane();
		lp.setPreferredSize(new Dimension(frameX,frameY));

		fp = new FlowPanel(landdata);
		fp.setPreferredSize(new Dimension(frameX,frameY));
		
		//g.add(fp);

		// to do: add a MouseListener, buttons and ActionListeners on those buttons
	   	
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));

		JButton endB = new JButton("End");
		JButton resetB = new JButton("Reset");
		JButton pauseB = new JButton("Pause");
		JButton playB = new JButton("Play");

		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
			}
		});
		pauseB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				SwingWaterThread.PauseWork();
			}
		});
		playB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				SwingWaterThread.ResumeWork();
			}
		});

		b.add(endB);
		b.add(Box.createRigidArea(new Dimension(10, 0))); //THis may help in hiding the Area of terrain that pops out.
		b.add(resetB);
		b.add(Box.createRigidArea(new Dimension(10, 0)));
		b.add(pauseB);
		b.add(Box.createRigidArea(new Dimension(10, 0)));
		b.add(playB);
		b.add(Box.createRigidArea(new Dimension(10, 0)));

		//g.add(b);
		
		wfp = new WaterFlowPanel(water);
		water.insertWFP(wfp);
		//wfp.setPreferredSize(new Dimension(frameX,frameY));
		wfp.setPreferredSize(fp.getSize());

		//Setting the background colour doesn't matter but make sure it is not opaque as the background will appear above the buffered image of wfp thus blocking it.
		wfp.setBackground(new Color(250, 134, 145, 255));
		wfp.setOpaque(false);

		g.add(wfp);
		g.setOpaque(false);

		g.add(b);
	
		g.setBounds(0, 0, frameX, frameY);
		//wfp.setBounds(0, 0, frameX, frameY);
		fp.setBounds(0, 0, frameX, frameY);
		
		//The greater the integer, the higher the layer.
		//lp.add(wfp, Integer.valueOf(2)); //layer 1
		//lp.add(g, Integer.valueOf(1)); //layer 2
		
		lp.add(fp, Integer.valueOf(1)); //layer 1
		lp.add(g, Integer.valueOf(2)); //layer 2
		
		SwingWaterThread a = new SwingWaterThread(water.dimx, water.dimy, 0, 0, water.img, wfp);
		a.execute();

		wfp.addMouseListener(new MouseAdapter() {
   	    	@Override
   			 public void mouseClicked(MouseEvent e) {
				int x=e.getX();
    			int y=e.getY();
    			System.out.println(x+","+y);//these co-ords are relative to the component
				water.paintPixels(x,y);
				//wfp.repaint();
				
   			 }
			
			/*public void mouseExited(MouseEvent e)
			public void mouseEntered(MouseEvent e)
			*/
		});
    	
		frame.setSize(frameX, frameY+50);	// a little extra space at the bottom for buttons
      	frame.setLocationRelativeTo(null);  // center window on screen
      	//frame.add(g); //add contents to window
		//frame.add(wfp);
		//frame.add(lp);
        //frame.setContentPane(g);
		//frame.setContentPane(lp);

        frame.setVisible(true);

        Thread fpt = new Thread(fp);
        fpt.start();
		
		Thread wfpt = new Thread(wfp);
        wfpt.start();
		
		//SwingUtilities.invokeLater(()->fjPool.invoke(new WaterThread(water.dimx, water.dimy, 0, 0, water.img, wfp)));
		
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

		water.readData(args[0]);
		
		frameX = landdata.getDimX();
		frameY = landdata.getDimY();
		SwingUtilities.invokeLater(()->setupGUI(frameX, frameY, landdata, water));
		
		// to do: initialise and start simulation
	}
}
