package ensg.eu.project.enveloppes;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class JarvisConvexHullTest {
	
	@Test
	public void triplePointDirectionTest() {
		
		//*****************************
		List<Point> p = new ArrayList<>();
        Point p1 = new Point(0, 0);
        Point p2 = new Point(2, 2);
        Point p3 = new Point(0, 3);
        Point p4 = new Point(-2,1);
        
        JarvisConvexHull j = new JarvisConvexHull(p);
        
        //collinear orientation  test
        assertEquals(0, j.triplePointDirection(p1,p1,p1));
        //clockwise orientation test
        assertEquals(1, j.triplePointDirection(p1,p4,p3));
        //counter clockwise orientation test
        assertEquals(2, j.triplePointDirection(p1,p2,p3));
	}
	
	@Test
	public void getEnvelopeTest(){
		
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
        Coordinate c1 = new Coordinate(-2.0,1.0);
        Coordinate c2 = new Coordinate(0.0,0.0);
        Coordinate c3 = new Coordinate(3.0,0.0);
        Coordinate c4 = new Coordinate(5.0,3.0);
        Coordinate c5 = new Coordinate(0.0,3.0);
        Coordinate c6 = new Coordinate(-2.0,1.0);

        Coordinate[] testCoord ={c1,c2,c3,c4,c5,c6};
        
        JarvisConvexHull j = new JarvisConvexHull(pointList);
        
        assertArrayEquals(testCoord,j.getEnvelope(pointList).getCoordinates());
	}
}
