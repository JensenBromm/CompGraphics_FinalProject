package mainPackage;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Driver extends JFrame implements Runnable{

	/**
	 * Basic maze game
	 */
	private static final long serialVersionUID = 1L;
	public int height=16;
	public int width=20;
	public Thread t;
	public boolean running;
	public BufferedImage image;
	public int[] pixels;
	
	public Camera camera;
	public Screen screen;
	
	public ArrayList<Texture> tex;

	
	/*
	 * Create the map array for the player to walk through
	 * 0 is empty space
	 * 1-4 is wall textures
	 * 5 is the finish
	 */
	public static int[][] map=
		{
				{2,2,2,1,2,2,1,1,1,1,2,2,1,1,1,2,2,2,2,2},
				{1,0,5,3,3,0,0,0,0,0,0,0,1,0,1,0,0,0,0,1},
				{1,0,3,0,0,0,1,1,1,0,1,0,1,0,1,0,1,1,0,1},
				{1,0,0,0,1,0,0,0,0,0,1,0,1,0,1,0,1,0,0,1},
				{1,0,4,3,0,3,0,0,0,0,1,0,0,0,0,0,1,0,3,1},
				{1,3,3,4,0,3,3,3,3,3,3,3,3,3,3,3,3,0,4,1},
				{1,0,0,0,0,2,0,0,0,0,3,0,0,0,0,1,0,0,1,1},
				{1,0,1,2,1,0,3,0,2,0,3,2,2,2,0,0,0,2,0,1},
				{1,0,0,1,1,0,0,0,2,0,3,0,0,0,2,0,1,2,0,1},
				{1,1,0,0,0,4,0,4,3,0,3,1,1,0,1,0,0,0,0,1},
				{1,0,1,1,0,3,0,3,0,0,3,2,2,0,1,1,1,1,0,1},
				{1,0,0,0,0,4,0,3,0,4,0,0,0,0,0,2,0,0,0,1},
				{1,0,4,3,3,4,0,3,0,4,2,2,0,3,0,2,0,2,0,1},
				{1,0,0,3,0,0,0,3,0,4,1,0,0,4,0,2,0,2,0,1},
				{1,0,0,0,0,4,4,0,0,0,0,0,0,4,0,0,0,2,0,1},
				{1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
				
		};
	
	//Constructor
	public Driver(){

		//Create a camera object and add a key listner to it
		//key listener is to get movement keys
		camera = new Camera(4.5, 4.5, 1, 0, 0., -.66);
		addKeyListener(camera);
		
		//Add all the textures to a list for the texture map
		tex=new ArrayList<Texture>();
		tex.add(Texture.cement1);
		tex.add(Texture.cement2);
		tex.add(Texture.cement3);
		tex.add(Texture.cement4);
		tex.add(Texture.finish);
		
		//Create the screen to display
		screen = new Screen(map, width, height, tex, 640, 480);
		
		t=new Thread(this);
		//Create a new image and load the pixels onto that image
		image=new BufferedImage(640,480,BufferedImage.TYPE_INT_RGB);
		pixels=((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		//Setup game window
		setSize(640,480);
		setResizable(false);
		setTitle("Computer Graphics Final Project- Jensen Bromm and Jerome Larson");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}
	
	private synchronized void start() { 
		//start the thread
		running=true;
		t.start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;//60 times per second
		double delta = 0;
		requestFocus();
		while(running) {
			long now = System.nanoTime();
			delta = delta + ((now-lastTime) / ns);
			lastTime = now;
			while (delta >= 1)//Make sure update is only happening 60 times a second
			{
				//handles all of the logic restricted time
				//handles all of the logic restricted time
				screen.update(camera, pixels);
				camera.update(map);
				delta--;
			}
			render();//displays to the screen unrestricted time
		}
	}
	
	public void render(){
		BufferStrategy b=getBufferStrategy();
		
		if(b==null) {
			createBufferStrategy(3);
			return;
		}
		
		Graphics g=b.getDrawGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
		b.show();
	}
	
	public static void main(String[] args) {
		Driver game=new Driver();
	}
	

}

