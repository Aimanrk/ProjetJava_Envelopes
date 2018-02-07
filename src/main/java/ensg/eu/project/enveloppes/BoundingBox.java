package ensg.eu.project.enveloppes;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;


/**
* This class compute a bounding box from a list of points
*
* @author Aiman R'KYEK
*/
public class BoundingBox extends Envelope{
	
	public List<Point> pointsList = new ArrayList<Point>();
	
	/**
	 * Constructor 
	 * 
	 * @param pointsList
	 */
	public BoundingBox(List<Point> pointsList) {
		this.pointsList = pointsList;;
	}
	
	
	/**
	* This function return a polygon as the bounding box of a list of point
	*  
	* @param pointsList List of point of type Point
	* 
	* @return return a polygon of the bounding box of pointsList
	* 
	* @throws IllegalArgumentException In case there's less than 3 points in the list of points
	*/
	public Polygon getEnvelope(List<Point> pointsList) {
		
		//The list of points must contain at least 3 points 
        if (pointsList.size() < 3) {
        	throw new IllegalArgumentException("Cannot generate a bounding box for less than 3 points");
        }
        
		//Used to create the geometry 
		GeometryFactory factory = new GeometryFactory();

		double xmin = getXmin(pointsList);
		double ymin = getYmin(pointsList);
		double xmax = getXmax(pointsList);
		double ymax = getYmax(pointsList);
		
		System.out.println(xmin);
		//The 4 coordinates that will constitute the Bounding box 
		Coordinate p1 = new Coordinate (xmin,ymin);
		Coordinate p2 = new Coordinate (xmin,ymax);
		Coordinate p3 = new Coordinate (xmax,ymax);
		Coordinate p4 = new Coordinate (xmax,ymin);

		Coordinate[] coord= {p1,p2,p3,p4,p1}; //add the first point twice to close the polygon!
		
		//Create the polygon from the coordinates
		LinearRing ring = factory.createLinearRing(coord);
		Polygon polygon = factory.createPolygon(ring,new LinearRing[]{});
		
		return polygon;
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
