package mainPackage;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Texture {
	public String fileLoc;
	public int[] pixels;
	public int size;
	
	//Create the texture objects for each wall texture
	public static Texture cement1=new Texture("res/CEMENT1.png",64);
	public static Texture cement2=new Texture("res/CEMENT2.png",64);
	public static Texture cement3=new Texture("res/CEMENT3.png",64);
	public static Texture cement4=new Texture("res/CEMENT4.png",64);
	public static Texture finish=new Texture("res/finish.png",64);
	
	//create constructor for textures
	public Texture(String location,int size){	
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
