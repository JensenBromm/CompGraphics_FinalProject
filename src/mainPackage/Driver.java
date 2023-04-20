package mainPackage;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.universe.SimpleUniverse;

import java.applet.Applet;
import javax.imageio.ImageIO;
import javax.media.j3d.*;
import javax.swing.Timer;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Driver extends Applet {
	
	public SimpleUniverse su;
	public BranchGroup pipes;
	public CollisionDetector cd1;
	public CollisionDetector cd2;
	
	public TransformGroup tg;
	public TransformGroup tg2;
	
	public Sphere playerShape;
	public Shape3D pipe1;
	public Shape3D pipe2;
	
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
		pipes = createPipe();
		pipes.setCapability(BranchGroup.ALLOW_DETACH);
		pipes.compile();
		player.compile();
		
		BranchGroup colDetectors=new BranchGroup();
		colDetectors.addChild(cd1);
		colDetectors.addChild(cd2);
	
		su.addBranchGraph(pipes);
		su.addBranchGraph(back);
		su.addBranchGraph(player);
		//Gets called every time that the timer goes off
		ActionListener recreatePipes=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//Remove the pipes from the univers
				su.getLocale().removeBranchGraph(pipes);
				//Recreate the pipes
				pipes=createPipe();
				pipes.setCapability(BranchGroup.ALLOW_DETACH);
				pipes.compile();
				//Add pipes back to the universe
				su.addBranchGraph(pipes);
				
			}
			
		};
		Timer timer=new Timer(3000,recreatePipes);
		timer.setRepeats(true);
		timer.start();
		
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
		Transform3D tr = new Transform3D();
		tr.setScale(0.1);
		tr.setTranslation(new Vector3d(2f,0,0));
		tg = new TransformGroup(tr);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		//GameThreads gThread1=new GameThreads(tg);
		//gThread1.start();
		move.addChild(tg);
		tg.addChild(pipe1);

		//Flip pipe on X axis
		Transform3D tr2 = new Transform3D();
		tr2.setScale(new Vector3d(0.1,-0.1,0.1));
		tr2.setTranslation(new Vector3d(2f,0,0));
		tg2=new TransformGroup(tr2);
		tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		move.addChild(tg2);
		tg2.addChild(pipe2);
		
		
	
		// move the pipes across the screen
		Alpha alpha = new Alpha(-1, 3000);
		PositionInterpolator pos = new PositionInterpolator(alpha, move);
		pos.setStartPosition(2f);
		pos.setEndPosition(-4f);
		BoundingSphere bounds = new BoundingSphere();
		pos.setSchedulingBounds(bounds);
		move.addChild(pos);
		
		
		//Add Lighting to pipes
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

		playerShape = new Sphere(1.0f);
		playerShape.setAppearance(ap);
		playerShape.setCollidable(true);
		

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
		
		//Collision detector for top pipe
		Shape3D pSphere=playerShape.getShape();
		pSphere.setCollidable(true);
		cd1=new CollisionDetector(pSphere,pipe1);
		cd1.setSchedulingBounds(bounds);
		       
		cd2=new CollisionDetector(pSphere,pipe2);
		cd2.setSchedulingBounds(bounds);

		return player;
	}


	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground", "true");
		new MainFrame( new Driver(), 640, 480);
	}
	
}