package mainPackage;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.applet.Applet;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Driver extends Applet {
	public void init() {
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();

		Canvas3D cv = new Canvas3D(gc);
		setLayout(new BorderLayout());
		add(cv, BorderLayout.CENTER);

//		BranchGroup pipes = createPipe();
		BranchGroup back = null;
		try {
			back = createBackground();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		BranchGroup player = createPlayer();
//		pipes.compile();
//		player.compile();

		SimpleUniverse su = new SimpleUniverse(cv);
		su.getViewingPlatform().setNominalViewingTransform();
//		su.addBranchGraph(pipes);
		su.addBranchGraph(back);
//		su.addBranchGraph(player);
	}

	private BranchGroup createPipe() {
		return null;
	}

	private BranchGroup createBackground() throws IOException {
		BranchGroup backgroundGroup = new BranchGroup();

		BufferedImage backImage = ImageIO.read(new File("res/back.png"));
		ImageComponent2D imageComp = new ImageComponent2D(ImageComponent.FORMAT_RGB, backImage);

		Background b = new Background(imageComp);
		b.setApplicationBounds(new BoundingSphere());
		backgroundGroup.addChild(b);

		return backgroundGroup;

	}

	private BranchGroup createPlayer() {
		return null;
	}


	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground", "true");
		new MainFrame( new Driver(), 640, 480);
	}
}