package ensg.eu.project.enveloppes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;

public class QuickConvexHullTest {

	@Test
	public void locationFromSegmentTest() {
		List<Point> pointList = new ArrayList<Point>();	

		Point ex1 = new Point(0,4);
		Point ex2 = new Point(4,0);
		Point p1 = new Point(1,1);
		Point p2 = new Point(4,3);
		
        pointList.add(ex1);
        pointList.add(ex2);
        pointList.add(p1);
        pointList.add(p2);
		
		QuickConvexHull q = new QuickConvexHull(pointList);
		
		assertEquals(-1,q.locationFromSegment(ex1,ex2,p1));
		assertEquals(1,q.locationFromSegment(ex1,ex2,p2));
	}
	
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
        QuickConvexHull q = new QuickConvexHull(pointList);
		Polygon p = q.getEnvelope(pointList);
		Coordinate[] coordHull = p.getCoordinates();
		
		//***********************************
		//(3.0,0.0),(0.0,0.0),(-2.0,1.0),(0.0,3.0),(5.0,3.0),(3.0,0.0)
		Coordinate c1 = new Coordinate(3.0,0.0);
		Coordinate c2 = new Coordinate(0.0,0.0);
		Coordinate c3 = new Coordinate(-2.0,1.0);
		Coordinate c4 = new Coordinate(0.0,3.0);
        Coordinate c5 = new Coordinate(5.0,3.0);       
        Coordinate c6 = new Coordinate(3.0,0.0);

        Coordinate[] testCoord ={c1,c2,c3,c4,c5,c6};
        
        assertArrayEquals(testCoord,coordHull);
	}
	
	
	
}
