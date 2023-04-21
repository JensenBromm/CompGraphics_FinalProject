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

	public Sphere playerShape;
	public Shape3D pSphere;
	public Shape3D pipe1;
	public Shape3D pipe2;
	public Text3D score;
	public Shape3D scoreShape;
	
	public BoundingSphere bounds = new BoundingSphere();
	
	
	public void init() {
		GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();

		Canvas3D cv = new Canvas3D(gc);
		setLayout(new BorderLayout());
		add(cv, BorderLayout.CENTER);
		su = new SimpleUniverse(cv);
		su.getViewingPlatform().setNominalViewingTransform();
		//Add the image background
		BranchGroup back = null;
		try {
			back = createBackground();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		//Create the player object
		BranchGroup player = createPlayer();
		//Set the capabilities so that the player can collide with pipes
		player.setCapability(BranchGroup.ALLOW_COLLIDABLE_WRITE);
		player.setCapability(BranchGroup.ALLOW_COLLIDABLE_READ);
		player.compile();
		
		//Create the pipe branch group
		pipes = createPipe();
		//Set capabilities so that the pipe group can collide with the player
		pipes.setCapability(BranchGroup.ALLOW_COLLIDABLE_WRITE);
		pipes.setCapability(BranchGroup.ALLOW_COLLIDABLE_READ);
		//Set capabilities so that the pipe group can be detached from the scene
		pipes.setCapability(BranchGroup.ALLOW_DETACH);
		pipes.setPickable(true);
		pipes.compile();
				
		//Create the colliders
		BranchGroup colDetectors=new BranchGroup();
		//Allow the colliders to be removed from scene
		colDetectors.setCapability(BranchGroup.ALLOW_DETACH);
		//Set the collider group to allow collisions between player and pipes
		colDetectors.setCapability(BranchGroup.ALLOW_COLLIDABLE_WRITE);
		colDetectors.setCapability(BranchGroup.ALLOW_COLLIDABLE_READ);
		//Create the collider objects and add them to the group
		createColliders();
		colDetectors.addChild(cd1);
		colDetectors.addChild(cd2);
		
		//Create the score text in top right of scene
		BranchGroup scoreGraph=new BranchGroup();
		scoreGraph.setCapability(Group.ALLOW_CHILDREN_WRITE);
		scoreGraph.setCapability(BranchGroup.ALLOW_DETACH);
		createScore();
		//Scale down and move the text
		Transform3D tr=new Transform3D();
		tr.setScale(0.4);
		tr.setTranslation(new Vector3d(0.55f,0.4f,0f));
		TransformGroup tGroup=new TransformGroup(tr);	
		//Add a light to the text
		PointLight ptlight = new PointLight(new Color3f(Color.WHITE), new Point3f(0.6f, 0.4f,1f), new Point3f(2f, 2f, 0f));
		ptlight.setInfluencingBounds(bounds);
		//Add everything to the branch group
		scoreGraph.addChild(tGroup);
		scoreGraph.addChild(ptlight);
		tGroup.addChild(scoreShape);
		
	
		//Add original branchGroups to the simple universe
		su.addBranchGraph(scoreGraph);
		su.addBranchGraph(colDetectors);
		su.addBranchGraph(pipes);
		su.addBranchGraph(back);
		su.addBranchGraph(player);
		
		//Gets called every time that the timer goes off
		ActionListener recreatePipes=new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * Our solution to randomizing the pipes y value every time is to remove the current set of pipes from the universe
				 * After the removal, we recreate the two pipes calling the createPipe method.
				 * This method randomizes the y value of the pipe so each time we call this the y value changes
				 * We also have to reset the colliders for the player and pipes since the pipes are being removed
				 * We then re-add these new pipes to the universe
				 */
				//Remove the pipes from the univers
				su.getLocale().removeBranchGraph(pipes);
				su.getLocale().removeBranchGraph(colDetectors);
				//Recreate the pipes
				pipes=createPipe();
				//Reset capabilities of the pipes graph
				pipes.setCapability(BranchGroup.ALLOW_DETACH);
				pipes.setCapability(BranchGroup.ALLOW_COLLIDABLE_WRITE);
				pipes.setCapability(BranchGroup.ALLOW_COLLIDABLE_READ);
				pipes.setPickable(true);
				pipes.compile();
				
				//Remove the past colliders
				colDetectors.removeAllChildren();
				
				//Recreate Colliders for the new pipes
				createColliders();
				//Add the new colliders
				colDetectors.addChild(cd1);
				colDetectors.addChild(cd2);
				
				/*
				 * Since the timer will only get called when the player has passed a set of pipes
				 * We can update the score whenever the past set of pipes was deleted
				 */
				updateScore();
				
				//Add pipes back to the universe
				su.addBranchGraph(pipes);
				su.addBranchGraph(colDetectors);
			}
			
		};
		//Create the timer that runs every 3 seconds
		Timer timer=new Timer(3000,recreatePipes);
		timer.setRepeats(true);
		timer.start();

	}

	public BranchGroup createPipe() {
		//Create the branch group to store everything in
		BranchGroup pipes=new BranchGroup();

		//Create the transform group to move the pipes across the screen
		TransformGroup move = new TransformGroup();
		move.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE); //This allows the PositionInterpolator to move anything in this group
		pipes.addChild(move);
		
		//the gap is the amount of space between the pipes that allows the player to pass through
		int gap=5;
		//Randomize the y value of the pipes to go across the screen
		double y=(Math.random()*10-5); //Random range between -5 and +5

		//Create the pipes
		pipe1=new Pipe(y);
		pipe2=new Pipe(-y+gap); //-y flips it and the +gap makes the gap between the pipes 5 units
		
		//Set up pipe objects
		pipe1.setBoundsAutoCompute(true);
		pipe1.setCollidable(true); //This line is important to make sure collisions can be detected
		pipe1.setPickable(true);
		pipe2.setBoundsAutoCompute(true);
		pipe2.setCollidable(true); //This line is important to make sure collisions can be detected
		pipe2.setPickable(true);
		
		//Scale down the pipes and create them off screen
		Transform3D tr = new Transform3D();
		tr.setScale(0.1);
		tr.setTranslation(new Vector3d(2f,0,0));
		TransformGroup tg = new TransformGroup(tr);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		move.addChild(tg);
		tg.addChild(pipe1);

		//Create transform for second pipe
		Transform3D tr2 = new Transform3D();
		//Scale down and Flip the pipe so that the neck of it faces down
		tr2.setScale(new Vector3d(0.1,-0.1,0.1));
		tr2.setTranslation(new Vector3d(2f,0,0)); //Create pipe off screen
		TransformGroup tg2=new TransformGroup(tr2);
		tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		move.addChild(tg2);
		tg2.addChild(pipe2);
		
		
	
		//The PositionInterpolator allows the pipes to move across the screen
		Alpha alpha = new Alpha(-1, 3000);
		PositionInterpolator pos = new PositionInterpolator(alpha, move);
		pos.setStartPosition(2f);
		pos.setEndPosition(-4f);
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
		
		//Pipes and Lights are all set up
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
		playerShape.setBoundsAutoCompute(true);

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
		
		pSphere=playerShape.getShape();
		pSphere.setBoundsAutoCompute(true);
		pSphere.setCollisionBounds(new BoundingSphere(new Point3d(0, 0, 0), 0.5));
		pSphere.setPickable(true);
		pSphere.setCollidable(true);
		

		return player;
	}
	
	public void createColliders() {
		//Two colliders are needed so that the player sphere can collide with either the top pipe or bottom pipe
		cd1=new CollisionDetector(pSphere,pipe1);
		cd1.setSchedulingBounds(bounds);    
		cd2=new CollisionDetector(pSphere,pipe2);
		cd2.setSchedulingBounds(bounds);
	}
	public void createScore() {
		//Setup the text object in the top right of the screen
		score=new Text3D(new Font3D(new Font("Helvetica",Font.PLAIN,1), new FontExtrusion()), "0");
		score.setCapability(Text3D.ALLOW_STRING_WRITE);
		Appearance appearance=new Appearance();
		Material mat=new Material();
		mat.setDiffuseColor(new Color3f(0.0f,1.0f,0.0f));
		appearance.setMaterial(mat);
		PolygonAttributes polyAttr = new PolygonAttributes();
		polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
		appearance.setPolygonAttributes(polyAttr);
		scoreShape=new Shape3D(score,appearance);
		
	}
	
	public void updateScore() {
		//Take the current score and add 1 then update the text3d object
		String current=score.getString();
		int num=Integer.parseInt(current);
		num++;
		String newString=Integer.toString(num);
		score.setString(newString);
	}


	public static void main(String[] args) {
		System.setProperty("sun.awt.noerasebackground", "true");
		new MainFrame( new Driver(), 640, 480);
	}
	
}