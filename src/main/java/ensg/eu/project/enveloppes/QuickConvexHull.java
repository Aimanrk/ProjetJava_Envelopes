package ensg.eu.project.enveloppes;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class compute the convex hull of a set of points using QuickHull algorithm
 * 
 * @author Aiman R'KYEK
 *
 */
public class QuickConvexHull extends ConvexHull{
	
	public List<Point> pointsList;
	
	/**
	 * Constructor 
	 * 
	 * @param pointsList
	 */
	public QuickConvexHull(List<Point> pointsList) {
		this.pointsList = pointsList;
	}
	
	/**
	* This function return a polygon as the convex hull of a list of points 
	*  
	* @param pointsList List of point of type Point
	* @return return a polygon of the convex hull of the pointsList
	* 
	* @throws IllegalArgumentException In case there's less than 3 points in the list of points
	*/
    public Polygon getEnvelope(List<Point> pointsList){
    	
    	//Used to create the geometry 
    	GeometryFactory factory = new GeometryFactory();
        
        // Initialize the list of hull points
    	List<Point> hullPoints = new ArrayList<Point>();
       
		//The list of points must contain at least 3 points 
        if (pointsList.size() < 3) {
        	throw new IllegalArgumentException("Cannot generate a convex hull for less than 3 points");
        }
 
        Point minPoint = null;
        Point maxPoint = null;
        
        //Find the min and max Point
        for(Point point: pointsList) {
        	if(point.getX()==getXmin(pointsList)){
        		minPoint = point;
        	}
        	if(point.getX()==getXmax(pointsList)){
        		maxPoint= point;
        	}
        }
        
        //Min and max point are the first points of the hull
        //Add them in the hullPoints and remove them from the pointsList 
        hullPoints.add(minPoint);
        hullPoints.add(maxPoint);
        pointsList.remove(minPoint);
        pointsList.remove(maxPoint);
        
        //The left and right points of the segment with extremities minPoint and maxPoint
        //Initialize lists
        List<Point> leftPoints= new ArrayList<Point>();
        List<Point> rightPoints = new ArrayList<Point>();
        
        //Route each point of the list to the appropriate list
        for (int i = 0; i < pointsList.size(); i++){
            Point p = pointsList.get(i);
            if (locationFromSegment(minPoint, maxPoint, p) == -1) {leftPoints.add(p);}
            else if (locationFromSegment(minPoint, maxPoint, p) == 1) {rightPoints.add(p);}    	
        }
        
        //Find the hull points for the points in the left and the right of the segment min and max point   
        findHullPoints(minPoint, maxPoint, rightPoints, hullPoints);
        findHullPoints(maxPoint, minPoint, leftPoints, hullPoints);
        
        //Add the first point to close the polygon
        hullPoints.add(hullPoints.get(0));
        
        //Initialize the table coord of Coordinate to create the Polygon
        Coordinate[] coord = new Coordinate[hullPoints.size()];
        
        //Fill the table with the coordinate of each point of the hull;
        for (int i=0; i<hullPoints.size();i++) {
			coord[i] = new Coordinate (hullPoints.get(i).getX(),hullPoints.get(i).getY());;
        }
        
        //Create the polygon from the coordinates
		LinearRing ring = factory.createLinearRing(coord);
		Polygon polygon=factory.createPolygon(ring,new LinearRing[]{});
       
		return polygon;
    }
    
	/**
	* This function finds the points of the convex hull for a list of points from a segment
	*  
	* @param ex1 first extremity
	* @param ex2 second extremity
	* @param pointsList Points list
	* @param hullPoints Hull point
	*   
	*/
    public void findHullPoints(Point ex1, Point ex2, List<Point> pointsList, List<Point> hullPoints){
        
		//Exit if there's no point in the list of point 
        if (pointsList.size() ==0) {
        	return;
        }
        
        //As it the only point in the list it will be surely the the farthest point from the segment
        //so it added to the hull pointsList in the position of the second extremity. Then we exit 
        if (pointsList.size() == 1){
            Point p = pointsList.get(0);
            pointsList.remove(p);
            hullPoints.add(hullPoints.indexOf(ex2), p);
            return;
        }
        
        //Initialize the max distance and the index of the farthest point to minimum value of integer
        double distanceMax = Double.MIN_VALUE;
        int mostFarPointIndex = Integer.MIN_VALUE;
        
        //search the farthestPoint from the segment (ex1,ex2) 
        for (int i = 0; i < pointsList.size(); i++){
        	
        	Point p = pointsList.get(i);
          	
        	double xDiff = ex2.getX() - ex1.getX();
          	double yDiff = ex2.getY() - ex1.getY();
          	double distance = Math.abs(xDiff * (ex1.getY() - p.getY()) - yDiff * (ex1.getX() - p.getX()));
          
	        if (distance > distanceMax){
	        	distanceMax = distance;
	        	mostFarPointIndex = i;
	        }
        }
        
        //Add the farthest point to the list of hull points in the position of the second extremity 
        //and remove it from the list of points 
        Point mostFarPoint = pointsList.get(mostFarPointIndex);
        pointsList.remove(mostFarPointIndex);
        hullPoints.add(hullPoints.indexOf(ex2), mostFarPoint);
 
        //List of point on the left of the first extremity and the farthest point
        List<Point> leftPoint1 = new ArrayList<Point>();
        for (int i = 0; i < pointsList.size(); i++){
            Point p = pointsList.get(i);
            if (locationFromSegment(ex1, mostFarPoint, p) == 1){
            	leftPoint1.add(p);
            }
        }
 
        // List of point on the left of the farthest point and the second extremity
        List<Point> leftPoint2 = new ArrayList<Point>();
        for (int i = 0; i < pointsList.size(); i++){
            Point p = pointsList.get(i);
            if (locationFromSegment(mostFarPoint, ex2, p) == 1)
            {
            	leftPoint2.add(p);
            }
        }
        
        //repeat for every new segment until there's no points
        //So in every time the farthest point is added to the hull points and removed from the list of points
        findHullPoints(ex1, mostFarPoint, leftPoint1, hullPoints);
        findHullPoints(mostFarPoint, ex2, leftPoint2, hullPoints);
    }
    
	/**
	* This function find the location of a point from a segment of the other two points
	*  
	* @param ex1 first extremity
	* @param ex2 second extremity
	* @param p point to found his location from the segment 
	* @return 1 if the point is in the right of the segment and -1 if it in the left
	* 
	*/
    public int locationFromSegment(Point ex1, Point ex2, Point p) {
        double d = (ex2.getX() - ex1.getX()) * (p.getY() - ex1.getY()) - 
        		     (ex2.getY() - ex1.getY()) * (p.getX() - ex1.getX());
        if (d > 0) {return 1;}
        else { return -1;}
    }
    
	/**
	 * This function export the envelope to a shp file
	 * 
	 * @param shapeName name of the shp to export
	 */
	public void exportEnveloppe(String shapeName) throws Exception{
		ExportGeometryToShp export = new ExportGeometryToShp();
		export.exportPolygon(shapeName, getEnvelope(this.pointsList));
	}
}
