package mainPackage;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3d;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;

public class Pipe extends Shape3D{
	public Pipe(int y) {
		GeometryInfo gi=new GeometryInfo(GeometryInfo.POLYGON_ARRAY);
		Point3d[] vertices=
				{
						//Create Outer Rim
						new Point3d(2,y,0),//Point 0
						new Point3d(1.847,y,0.765),//Point 1
						new Point3d(1.414,y,1.414), //Point 2
						new Point3d(0.765,y,1.847), //Point 3
						new Point3d(0,y,2), //Point 4
						new Point3d(-0.765,y,1.847), //Point 5
						new Point3d(-1.414,y, 1.414), //Point 6
						new Point3d (-1.847,y, 0.765), //Point 7
						new Point3d(-2,y, 0), //Point 8
						new Point3d(-1.847,y, -0.765), //Point 9
						new Point3d(-1.414,y, -1.414), //Point 10
						new Point3d (-0.765,y, -1.847), //Point 11
						new Point3d(0,y, -2), //Point 12
						new Point3d(0.765,y, -1.847), //Point 13
						new Point3d(1.414,y, -1.414), //Point 14
						new Point3d(1.847,y, -0.765), //Point 15

						//Create Top of outer rim
						new Point3d(2,y+1,0),//Point 16
						new Point3d(1.847,y+1,0.765),//Point 17
						new Point3d(1.414,y+1,1.414), //Point 18
						new Point3d(0.765,y+1,1.847), //Point 19
						new Point3d(0,y+1,2), //Point 20
						new Point3d(-0.765,y+1,1.847), //Point 21
						new Point3d(-1.414,y+1, 1.414), //Point 22
						new Point3d (-1.847,y+1, 0.765), //Point 23
						new Point3d(-2,y+1, 0), //Point 24
						new Point3d(-1.847,y+1, -0.765), //Point 25
						new Point3d(-1.414,y+1, -1.414), //Point 26
						new Point3d (-0.765,y+1, -1.847), //Point 27
						new Point3d(0,y+1, -2), //Point 28
						new Point3d(0.765,y+1, -1.847), //Point 29
						new Point3d(1.414,y+1, -1.414), //Point 30
						new Point3d(1.847,y+1, -0.765), //Point 31


						//Create inner Circle Front Face
						new Point3d(-1,y+0.5,0), //Point 32
						new Point3d(-0.9239,y+0.5,0.3827), //Point 33
						new Point3d(-0.7071,y+0.5,0.7071), //Point 34
						new Point3d(-0.3827,y+0.5,0.9239), //Point 35
						new Point3d(0,y+0.5,1), //Point 36
						new Point3d(0.3827,y+0.5,0.9239),//Point 37
						new Point3d(0.7071,y+0.5,0.7071), //Point 38
						new Point3d(0.9239,y+0.5,0.3827),//Point 39
						new Point3d(1,y+0.5,0), //Point 40
						new Point3d(0.9239,y+0.5,-0.3827),//Point 41
						new Point3d(0.7071,y+0.5,-0.7071), //Point 42
						new Point3d(0.3827,y+0.5,-0.9239), //Point 43
						new Point3d(0,y+0.5,-1), //Point 44
						new Point3d(-0.3827,y+0.5,-0.9239), //Point 45
						new Point3d(-0.7071,y+0.5,-0.7071),//Point 46
						new Point3d(-0.9239,y+0.5,-0.3827),//Point 47
						//Create inner Circle Back Face
						new Point3d(-1,y+20,0), //Point 48
						new Point3d(-0.9239,y+20,0.3827), //Point 49
						new Point3d(-0.7071,y+20,0.7071), //Point 50
						new Point3d(-0.3827,y+20,0.9239), //Point 51
						new Point3d(0,y+10,1), //Point 52
						new Point3d(0.3827,y+20,0.9239),//Point 53
						new Point3d(0.7071,y+20,0.7071), //Point 54
						new Point3d(0.9239,y+20,0.3827),//Point 55
						new Point3d(1,y+20,0), //Point 56
						new Point3d(0.9239,y+20,-0.3827),//Point 57
						new Point3d(0.7071,y+20,-0.7071), //Point 58
						new Point3d(0.3827,y+20,-0.9239), //Point 59
						new Point3d(0,y+20,-1), //Point 60
						new Point3d(-0.3827,y+20,-0.9239), //Point 61
						new Point3d(-0.7071,y+20,-0.7071),//Point 62
						new Point3d(-0.9239,y+20,-0.3827),//Point 63
				};

		int [] indices=
				{
						//Make the lip of the pipe
						0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,
						16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,

						//Connect the walls of the lip
						0,16,17,1,0,
						1,17,18,2,1,
						2,18,19,3,2,
						3,19,20,4,3,
						4,20,21,5,4,
						5,21,22,6,5,
						6,22,23,7,6,
						7,23,24,8,7,
						8,24,25,9,8,
						9,25,26,10,9,
						10,26,27,11,10,
						11,27,28,12,11,
						12,28,29,13,12,
						13,29,30,14,13,
						14,30,31,15,14,
						15,31,16,0,15,

						//Make the neck of the pipe
						32,33,34,35 ,36,37,38,39 ,40,41,42,43 ,44,45,46,47,
						48,49,50,51 ,52,53,54,55 ,56,57,58,59 ,60,61,62,63,

						//Connect the walls of the neck
						32,48,49,33,32,
						33,49,50,34,33,
						34,50,51,35,34,
						35,51,52,36,35,
						36,52,53,37,36,
						37,53,54,38,37,
						38,54,55,39,38,
						39,55,56,40,39,
						40,56,57,41,40,
						41,57,58,42,41,
						42,58,59,43,42,
						43,59,60,44,43,
						44,60,61,45,44,
						45,61,62,46,45,
						46,62,63,47,46,
						47,63,48,32,47,
				};

		gi.setCoordinates(vertices);
		gi.setCoordinateIndices(indices);

		int[] stripCounts = {16,16,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,16,16,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5,5};
		gi.setStripCounts(stripCounts);
		NormalGenerator ng = new NormalGenerator();
		ng.generateNormals(gi);

		gi.indexify();

		this.setGeometry(gi.getIndexedGeometryArray());

		Appearance a=new Appearance();
		PolygonAttributes pa = new PolygonAttributes();
		pa.setBackFaceNormalFlip(true);
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		a.setPolygonAttributes(pa);

		Material m=new Material();
		m.setDiffuseColor(0f, 1f, 0f);
		m.setAmbientColor(0f,1f,0f);
		a.setMaterial(m);
		this.setAppearance(a);
	}
}