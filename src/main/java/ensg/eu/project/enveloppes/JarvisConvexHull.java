package ensg.eu.project.enveloppes;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class compute the convex hull of a set of points using Jarvis's algorithm (Gift wrapping)
 * 
 * @author Aiman R'KYEK 
 *
 */
public class JarvisConvexHull extends ConvexHull{
	
	public List<Point> pointsList;
	
	/**
	 * Constructor 
	 * 
	 * @param pointsList
	 */
	public JarvisConvexHull(List<Point> pointsList) {
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
    	
		//The list of points must contain at least 3 points 
		int pointsListSize = pointsList.size();
        if (pointsListSize < 3) {
        	throw new IllegalArgumentException("Cannot generate a convex hull for less than 3 points");
        }
        
        // Initialize the list of hull points
        List<Point> hullPoints = new ArrayList<Point>();
        
        //Find the most left point
        Point mostLeft = null;
        for(Point point: pointsList) {
        	if(point.getX()==getXmin(pointsList)) {
        		mostLeft = point;
        	}
        }
        
        int mostLeftIndex = pointsList.indexOf(mostLeft);
        
        //Initialize the current point as the most left point
        int currentPointIndex = mostLeftIndex, nextPointIndex;
        
        //Loops until we came back to the first point
        //Add the first point(most left point) and then add each point who respect the orientation condition
        do{
        	hullPoints.add(pointsList.get(currentPointIndex));
        	
        	//modulo to give a result between 0 and the last index of the list
        	//otherwise the index is out of range (IndexOutOfBoundsException)
        	nextPointIndex = (currentPointIndex + 1)% pointsListSize;
            
        	//Search for a point from the list who formed a counterclockwise orientation with the the current and next point
            for (int i = 0; i < pointsListSize; i++){
               if (triplePointDirection(pointsList.get(currentPointIndex), pointsList.get(i), pointsList.get(nextPointIndex))== 2){
            	   nextPointIndex = i;
               }
            }
      
            currentPointIndex = nextPointIndex;
      
        } while (currentPointIndex != mostLeftIndex); 
        
        //Add the first point to close the polygon
        hullPoints.add(pointsList.get(mostLeftIndex));    
        
        //Initialize the table coord of Coordinate to create the Polygon
        Coordinate[] coord = new Coordinate[hullPoints.size()];
        
        //Fill the table with the coordinate of each point of the hull;
        for (int i=0; i<hullPoints.size();i++) {
			coord[i] = new Coordinate (hullPoints.get(i).getX(),hullPoints.get(i).getY());
        }
        
        //Create the polygon from the coordinates
		LinearRing ring =factory.createLinearRing(coord);
		Polygon polygon=factory.createPolygon(ring,new LinearRing[]{});
        
		return polygon;
    }
    
	/**
	* This function compute the type of the orientation between 3 points
	*  
	* @param p1 first point
	* @param p2 second point
	* @param p3 third point 
	* @return 0 if the orientation is collinear, 1 if it clockwise and 2 if it counterclockwise
	* 
	*/
    public int triplePointDirection(Point p1, Point p2, Point p3){

    	double val = (p2.getY() - p1.getY()) * (p3.getX() - p2.getX()) -
                	 (p2.getX() - p1.getX()) * (p3.getY() - p2.getY());

        if(val==0) {
        	//collinear orientation 
        	return 0;
        }
        else if(val>0) {
        	//clockwise orientation (right turn)
        	return 1;
        }
        else { //val<0
        	//counterclockwise orientation (left turn)
        	return 2; 
        }
        
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
