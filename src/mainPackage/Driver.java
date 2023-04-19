package mainPackage;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.applet.Applet;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import java.awt.*;

public class Driver extends Applet {
	public void init() {
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();

		Canvas3D cv = new Canvas3D(gc);
		setLayout(new BorderLayout());
		add(cv, BorderLayout.CENTER);

		BranchGroup pipes = createPipe();
		BranchGroup back = createBackground();
		BranchGroup player = createPlayer();
		pipes.compile();
		player.compile();

		SimpleUniverse su = new SimpleUniverse(cv);
		su.getViewingPlatform().setNominalViewingTransform();
		su.addBranchGraph(pipes);
		su.addBranchGraph(back);
		su.addBranchGraph(player);
	}

	private BranchGroup createPipe() {
		return null;
	}

	private BranchGroup createBackground() {
		return null;
	}

	private BranchGroup createPlayer() {
		return null;
	}


	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground", "true");
		new MainFrame( new Driver(), 640, 480);
	}
}