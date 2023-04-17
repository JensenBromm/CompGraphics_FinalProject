package mainPackage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameTexture {
	public String fileLoc;
	public int[] pixels;
	public int size;
	
	//Create the texture objects for each wall texture
	public static GameTexture cement1=new GameTexture("res/CEMENT1.png",64);
	public static GameTexture cement2=new GameTexture("res/CEMENT2.png",64);
	public static GameTexture cement3=new GameTexture("res/CEMENT3.png",64);
	public static GameTexture cement4=new GameTexture("res/CEMENT4.png",64);
	public static GameTexture enemy=new GameTexture("res/enemy.png",64);
	
	//create constructor for textures
	public GameTexture(String location,int size){	
		fileLoc=location;
		this.size=size;
		/*
		 * size of textures
		 * All sizes we used are 64x64 pixels
		 */
		pixels=new int [size*size];
		
		try {
			//Load the texture into image
			BufferedImage image=ImageIO.read(new File(fileLoc));
			
			//get image width and height
			int height=image.getHeight();
			int width=image.getWidth();
			
			//get color of each pixel and store that in the pixels array
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch(Exception e) {
				e.printStackTrace();
		}
	}
}
