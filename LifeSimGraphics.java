package lifesimgraphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Image;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import static java.lang.Math.floor;


/**
 * CREATE Project: LifeSim
 *
 * cellular automaton 
 * @author [Letizia Moro]
 * 
 * Inspiration: http://mathworld.wolfram.com/CellularAutomaton.html
 */
@SuppressWarnings("serial")
public class LifeSimGraphics extends JPanel
{
        private Color cellColor = new Color(0,0,0);//Cell color:black
        private Color backgroudColor = new Color(255,255,255);//Background color:white
	/**
	 * A constant to regulate the frequency of Timer events.
	 * Note: 100ms is 10 frames per second - you should not need
	 * a faster refresh rate than this
	 */
    
	private final int DELAY = 100; //milliseconds

	   /*
	 * initialize width
	 */
        private int width;

         /**
	 * initialize height
	 */
	private int height; 
        
         /**
	 * initialize g as type Graphics2D.  
         * Necessary for using setStroke()
	 */
        Graphics2D g;
        
        int[][] coord = new int[100][100];
        
        public void setUp(){
            int startX =(int) floor(Math.random() * 90); /*creates the initial cells at random coordinates*/
            int startY =(int) floor(Math.random() * 90);
            coord[startX+1][startY]=1;   coord[startX+3][startY]=1;  // from the initial coordinates, creates other cells
            coord[startX][startY+1]=1;   coord[startX+2][startY+1]=1;
            coord[startX][startY+2]=1;   coord[startX+2][startY+2]=1;
            coord[startX+1][startY+3]=1;
        }

        /**
	 * Draws (and refreshes) the scene at each animation step.
	 * 
	 * @param g0 Graphics context
	 */
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
        @Override
	public void paintComponent(Graphics g0)
	{       
                
            width = getWidth();// panel width
            height = getHeight();//panel height
                
            coord = rules(coord);
            int pop = population(coord);
            
            draw(coord, g0);
          
            System.out.println("population: "+ pop ); //prints population # to console
          
            if(pop == 0||pop>(80*80)){ // if grid gets too crowded, then exits program
                System.exit(0);
            }

            //Makes the animation smoother.
            Toolkit.getDefaultToolkit().sync();
	}
        
      
        public static int population( int[][] coord){
        int pop = 0;
        for(int y=0;y<100;y++){ //increases population count
            for(int x=0;x<100;x++){
                pop += coord[x][y];
            }
        }
        return pop;
    }
    public static int countNeighbors(int[][] coord,int x,int y){
        //Checking surroundings of cells 
        int topLeft = x>0 & y>0 ?  coord[x-1][y-1] : 0 ;
        int top = y>0 ?  coord[x][y-1] : 0 ;
        int topRight = x<99 & y>0 ?  coord[x+1][y-1] : 0 ;
        int Left = x>0 ?  coord[x-1][y] : 0 ;
        int Right = x<99 ?  coord[x+1][y] : 0 ;
        int BottomLeft = x>0 & y<99 ?  coord[x-1][y+1] : 0 ;
        int Bottom = y<99 ?  coord[x][y+1] : 0 ;
        int BottomRight = x<99 & y<99?  coord[x+1][y+1] : 0 ;
        
        return topLeft + top + topRight+ Left + Right + BottomLeft+ BottomRight + Bottom ; 
        
    }
    
    public static int[][] rules(int[][] coord) {
          
        for(int y=0;y<100;y++){
            for(int x=0;x<100;x++){
               int neighbor = countNeighbors(coord, x, y);
                // Rules that govern if cell lives or dies
                if(coord[x][y]== 1 && (neighbor<2||neighbor>3)){
                        coord[x][y]=0;              
                        
                }
                else if(coord[x][y]== 0 && neighbor == 3){
                    coord[x][y]=1;
                }
                // randomly kills off a cell or makes one, just to add some randomness
                boolean LiveRnd = Math.random() <0.02;  
                boolean DieRnd = Math.random() <0.02;
                    // if(coord[x][y]==0 && LiveRnd) {coord[x][y]=1;}
                if(coord[x][y]==1 && DieRnd) {coord[x][y]=0;}

               }
            }
        return coord;
        }
    
    
    
    public void draw(int[][] coord,Graphics g0){
        // Graphics stuff
        g = (Graphics2D) g0;
        g.setColor(backgroudColor); 
        int pixelWSize = (width/100); //allows window to be scalable
        int pixelHSize = (height/100);
        g.fillRect(0, 0, 100*pixelWSize, 100*pixelHSize);
        g.setColor(cellColor); 
        for(int y=0;y<100;y++){
            for(int x=0;x<100;x++){
                if(coord[x][y] == 1){
                    g.fillRect(x*pixelWSize, y*pixelHSize, pixelWSize, pixelHSize);
                }
            }
        }
    }
		

        //used template from first project: Greeting card
	//==============================================================
	// You don't need to modify anything beyond this point.
	//==============================================================
            //used template from first project: Greeting card
         /*
	 * @param args unused
	 */
	public static void main (String[] args)
	{
		JFrame frame = new JFrame ("LifeSim");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new LifeSimGraphics());
		frame.pack();
		frame.setVisible(true);
               
        
	}

	/**
	 * Constructor for the display panel initializes necessary variables.
	 * Only called once, when the program first begins. This method also
	 * sets up a Timer that will call paint() with frequency specified by
	 * the DELAY constant.
	 */
	public LifeSimGraphics()
	{

		// Do not initialize larger than 800x600, won't work for grader
		int initWidth = 500;
		int initHeight = 500;
		setPreferredSize(new Dimension(initWidth, initHeight));
		this.setDoubleBuffered(true);
                
                // Just modified here to initialize the coordinate grid.
                setUp();
                
		//Start the animation
		startAnimation();
	}

	/**
	 * Create an animation thread that runs periodically.
	 */
	private void startAnimation()
	{
		ActionListener timerListener = new TimerListener();
		Timer timer = new Timer(DELAY, timerListener);
		timer.start();
	}

	/**
	 * Repaints the graphics panel every time the timer fires.
	 */
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}
}