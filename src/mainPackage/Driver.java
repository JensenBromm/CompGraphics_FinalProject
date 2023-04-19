package mainPackage;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.applet.Applet;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Driver extends Applet {
	
	public SimpleUniverse su;
	public PositionInterpolator transform;
	public BranchGroup pipes = createPipe();
	public Shape3D pipe1;
	public Shape3D pipe2;
	public Transform3D tr;
	public Transform3D tr2;
	public TransformGroup tg;
	public TransformGroup tg2;
	
	public void init() {
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();

		Canvas3D cv = new Canvas3D(gc);
		setLayout(new BorderLayout());
		add(cv, BorderLayout.CENTER);
		su = new SimpleUniverse(cv);
		su.getViewingPlatform().setNominalViewingTransform();
		
		

		
		BranchGroup back = null;
		try {
			back = createBackground();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		BranchGroup player = createPlayer();
		pipes.compile();
		player.compile();
		

	
		su.addBranchGraph(pipes);
		su.addBranchGraph(back);
		su.addBranchGraph(player);
	}

	public BranchGroup createPipe() {
		BranchGroup pipes=new BranchGroup();

		TransformGroup move = new TransformGroup();
		move.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		pipes.addChild(move);
		// object
		int gap=3;
		int y=(int) Math.floor(Math.random() * 10 - 5);

		pipe1=new Pipe(y);
		pipe2=new Pipe(-y+gap);
		tr = new Transform3D();
		tr.setScale(0.1);
		tg = new TransformGroup(tr);
		move.addChild(tg);
		tg.addChild(pipe1);

		//Flip pipe on X axis
		tr2 = new Transform3D();
		tr2.setScale(new Vector3d(0.1,-0.1,0.1));
		tg2=new TransformGroup(tr2);
		move.addChild(tg2);
		tg2.addChild(pipe2);
	
		// move the pipes
		Alpha alpha = new Alpha(-1, 2800);
		//Create an executioner to constantly change y position of pipes
		Runnable changeY = new Runnable() {
		    public void run() {
		    	System.out.print("help");
		    	double rand=Math.floor(Math.random() * 10 - 5);
			    Vector3d translation=new Vector3d();
			    tr.get(translation);
			    tr.setTranslation(new Vector3d(translation.x,rand,translation.z));
			   // tg.setTransform(tr);
			    
			    Vector3d translation2=new Vector3d();
			    tr2.get(translation2);
			    tr2.setTranslation(new Vector3d(translation2.x,-rand+gap,translation2.z));
		    	//tg2.setTransform(tr2);
		    }
		};

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(changeY, 0, 3, TimeUnit.SECONDS);
		
		transform = new PositionInterpolator(alpha, move);
		transform.setStartPosition(1f);
		transform.setEndPosition(-3f);
		BoundingSphere bounds = new BoundingSphere();
		transform.setSchedulingBounds(bounds);
		
		move.addChild(transform);
		AmbientLight light = new AmbientLight(true, new Color3f(Color.gray));
		light.setInfluencingBounds(bounds);
		pipes.addChild(light);
		PointLight ptlight = new PointLight(new Color3f(Color.WHITE), new Point3f(0f, 3f,0f), new Point3f(1f, 0f, 0f));
		ptlight.setInfluencingBounds(bounds);
		pipes.addChild(ptlight);
		PointLight ptlight2 = new PointLight(new Color3f(Color.WHITE),new Point3f(0.0f,0.0f,0.0f), new Point3f(1f,0f,0f));
		ptlight2.setInfluencingBounds(bounds);
		pipes.addChild(ptlight2);
		PointLight ptlight3 = new PointLight(new Color3f(Color.WHITE),new Point3f(0,5f,-1f), new Point3f(1f,0.5f,0f));
		ptlight3.setInfluencingBounds(bounds);
		pipes.addChild(ptlight3);
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

		Sphere playerShape = new Sphere(1.0f);
		playerShape.setAppearance(ap);

		Transform3D tr = new Transform3D();
		tr.setScale(0.08);
		tr.setTranslation(new Vector3d(-0.5,0, 0));
		TransformGroup tg1 = new TransformGroup(tr);
		tg1.addChild(playerShape);

		player.addChild(tg1);
		//light
		PointLight light = new PointLight(new Color3f(Color.black), new Point3f(1f,1f,1f), new Point3f(1f,0.1f,0f));

		BoundingSphere bounds = new BoundingSphere();
		light.setInfluencingBounds(bounds);
		player.addChild(light);

		return player;
	}


	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground", "true");
		new MainFrame( new Driver(), 640, 480);
	}
}