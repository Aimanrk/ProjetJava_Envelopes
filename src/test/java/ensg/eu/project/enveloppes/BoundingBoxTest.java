package ensg.eu.project.enveloppes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class BoundingBoxTest {
	
	@Test
	public void getEnvelopeTest() {
        
		//***********************************
        List<Point> pointList = new ArrayList<Point>();
        pointList.add(new Point(0, 3));
        pointList.add(new Point(2, 3));
        pointList.add(new Point(1, 1));
        pointList.add(new Point(2, 1));
        pointList.add(new Point(3, 0));
        pointList.add(new Point(0, 0));
        pointList.add(new Point(3, 3));
        pointList.add(new Point(5, 3));
        pointList.add(new Point(-2, 1));

		//***********************************
        BoundingBox bb = new BoundingBox(pointList);
		Polygon p = bb.getEnvelope(pointList);
		Coordinate[] coordHull = p.getCoordinates();
		
		//***********************************
        Coordinate c1 = new Coordinate(-2,0);
        Coordinate c2 = new Coordinate(-2,3);
        Coordinate c3 = new Coordinate(5,3);
        Coordinate c4 = new Coordinate(5,0);
        Coordinate c5 = new Coordinate(-2,0);

        Coordinate[] testCoord ={c1,c2,c3,c4,c5};

        assertArrayEquals(testCoord,coordHull);
		
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void lessThreePoint() {
		
        //**************************************
		List<Point> pointList = new ArrayList<Point>();
        pointList.add(new Point(0, 3));
        pointList.add(new Point(2, 3));
        
		//***********************************
        BoundingBox bb = new BoundingBox(pointList);
		Polygon p = bb.getEnvelope(pointList);
		Coordinate[] coordHull = p.getCoordinates();
        
		assertEquals(coordHull,0);
	}
}
