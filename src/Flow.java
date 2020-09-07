import javax.swing.*;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.BorderLayout;
import java.awt.Color;

public class Flow {
	static long startTime = 0;
	static int frameX;
	static int frameY;
	static FlowPanel fp;
	static WaterFlowPanel wfp;

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

		wfp = new WaterFlowPanel(water);
		wfp.setPreferredSize(new Dimension(frameX,frameY));
		wfp.setBackground(new Color(250, 134, 145, 123));
		//wfp.setOpaque(false);
		
		g.add(fp);

		g.setBounds(0, 0, frameX, frameY);
		wfp.setBounds(frameY/2, 0, frameX, frameY);

		lp.add(wfp, Integer.valueOf(2)); //layer 1
		lp.add(g, Integer.valueOf(1)); //layer 2
    	
	    
		// to do: add a MouseListener, buttons and ActionListeners on those buttons
	   	
		JPanel b = new JPanel();
	    b.setLayout(new BoxLayout(b, BoxLayout.LINE_AXIS));
		JButton endB = new JButton("End");;
		// add the listener to the jbutton to handle the "pressed" event
		endB.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// to do ask threads to stop
				frame.dispose();
			}
		});
		
		b.add(endB);
		g.add(b);
    	
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
	
	}
	
		
	public static void main(String[] args) {
		Terrain landdata = new Terrain();

		WaterDraw water = new WaterDraw();
		
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
