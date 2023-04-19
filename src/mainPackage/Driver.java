package mainPackage;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.applet.Applet;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.xml.crypto.dsig.Transform;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Driver extends Applet {
	public void init() {
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();

		Canvas3D cv = new Canvas3D(gc);
		setLayout(new BorderLayout());
		add(cv, BorderLayout.CENTER);

		BranchGroup pipes = createPipe();
		BranchGroup back = null;
		try {
			back = createBackground();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
//		BranchGroup player = createPlayer();
		pipes.compile();
//		player.compile();

		SimpleUniverse su = new SimpleUniverse(cv);
		su.getViewingPlatform().setNominalViewingTransform();
		su.addBranchGraph(pipes);
		su.addBranchGraph(back);
//		su.addBranchGraph(player);
	}

	private BranchGroup createPipe() {
		BranchGroup pipes=new BranchGroup();

		TransformGroup spin = new TransformGroup();
		spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		pipes.addChild(spin);
		// object
		Appearance ap = new Appearance();
		ap.setMaterial(new Material());
		PolygonAttributes pa = new PolygonAttributes();
		pa.setBackFaceNormalFlip(true);
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		ap.setPolygonAttributes(pa);
		//Turn BackFace Normal
		Material m=new Material();
		m.setAmbientColor(0.5f, 0.5f, 0.5f);
		ap.setMaterial(m);
		
		int y1=new Random().nextInt(5);
		int y2=new Random().nextInt(2);
		Shape3D pipe1=new Pipe(y1);
		Shape3D pipe2=new Pipe(y2);

		pipe1.setAppearance(ap);
		pipe2.setAppearance(ap);
		Transform3D tr = new Transform3D();
		tr.setScale(0.1);
		TransformGroup tg = new TransformGroup(tr);
		spin.addChild(tg);
		tg.addChild(pipe1);
		tg.addChild(pipe2);

		// rotator
		Alpha alpha = new Alpha(-1, 24000);
		RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
		BoundingSphere bounds = new BoundingSphere();
		rotator.setSchedulingBounds(bounds);
		spin.addChild(rotator);
		AmbientLight light = new AmbientLight(true, new Color3f(Color.black));
		light.setInfluencingBounds(bounds);
		pipes.addChild(light);
		PointLight ptlight = new PointLight(new Color3f(Color.gray), new Point3f(1f, 1f,1f), new Point3f(1f, 0f, 0f));
		ptlight.setInfluencingBounds(bounds);
		pipes.addChild(ptlight);
		PointLight ptlight2 = new PointLight(new Color3f(Color.gray), new Point3f(-2f, 2f, 2f), new Point3f(1f, 0f, 0f));
		ptlight2.setInfluencingBounds(bounds);
		pipes.addChild(ptlight2);
		return pipes;
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
		BranchGroup player = new BranchGroup();

		Appearance ap = new Appearance();
		ap.setMaterial(new Material());

		Sphere sphere = new Sphere(1.0f);
		Shape3D sphereShape = sphere.getShape();

		Transform3D tr = new Transform3D();
		tr.setScale(0.2);

		TransformGroup tg = new TransformGroup(tr);
		tg.addChild(sphereShape);

		PointLight light = new PointLight(new Color3f(Color.white), new Point3f(1f,1f,1f), new Point3f(1f,0.1f,0f));
		BoundingSphere bounds = new BoundingSphere();

		light.setInfluencingBounds(bounds);
		player.addChild(light);
		player.addChild(tg);

		return player;
	}


	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground", "true");
		new MainFrame( new Driver(), 640, 480);
	}
}