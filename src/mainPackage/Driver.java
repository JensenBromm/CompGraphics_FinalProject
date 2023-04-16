package mainPackage;

import java.applet.Applet;

public class Driver extends Applet {
	public Camera camera;
	public Screen screen;

	public Driver() {
		camera = new Camera(4.5, 4.5, 1, 0, 0., -.66);
		addKeyListener(camera);

		//Initialize textures
		
		//construct screen
		screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);
	}

	public void run() {
		//Need to add screen.update(camera, pixels)
		//Need to add camera.update(map) within the while (delta >= 1) loop
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello World");

	}

}
