package ensg.eu.project.enveloppes;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This class compute a minimum oriented bounding box for a list of points
 * 
 * @author Aiman R'KYEK
 *
 */
public class OrientedMinimumBoundingBox extends Envelope{
	
	public List<Point> pointsList = new ArrayList<Point>();
	
	/**
	 * Constructor
	 * 
	 * @param pointsList
	 */
	public OrientedMinimumBoundingBox(List<Point> pointsList) {
		this.pointsList = pointsList;;
	}
	
	/**
	 * This function return a polygon as the minimum oriented bounding box of a list of point
	 * 
	 * @param pointsList List of point to compute their MOBB 
	 * 
	 * @return return a polygon of MOBB of the pointsList
	 */
	public  Polygon getEnvelope(List<Point> pointsList){
		
        //The list of points must contain at least 3 points 
		if (pointsList.size() < 3) {
        	throw new IllegalArgumentException("Cannot generate a convex hull for less than 3 points");
        }
        
		//Compute the convex hull of the list of points 
		JarvisConvexHull e = new JarvisConvexHull(pointsList);
		Polygon hullConvex = e.getEnvelope(pointsList);
		
		//Gets the factory of the convex hull (Polygon)
		GeometryFactory factory = hullConvex.getFactory();
		
		//Gets the centroid coordinate of the convex hull 
		Coordinate centroid = hullConvex.getCentroid().getCoordinate();
		//Gets the coordinates of the convex hull exterior boundary 
		Coordinate[] extRing = hullConvex.getExteriorRing().getCoordinates();
		
		//Initialize the minimum area enclosing the convex hull and the minimum angle of the rotation for getting this area
		//to the minimum value of double
		double minimumArea = Double.MAX_VALUE;
		double minimumAngle = Double.MAX_VALUE;
		//Initialize the adjusted rectangle,
		//and the current coordinate as the first point of the exterior ring of the convex hull
		Polygon adjustedRec = null;
		Coordinate currentCoord = extRing[0]; 
		Coordinate nextCoord;

		//Repeat for each edge of the convex hull
		for(int i=0; i<extRing.length-1; i++){
			nextCoord = extRing[i+1];
			//compute the edge angle from x and y axes
			double edgeAngle = Math.atan2(nextCoord.y-currentCoord.y, nextCoord.x-currentCoord.x);
			//Rotate the convex hull with the edgeAngle around the centroid 
			Polygon rotateRect = (Polygon) rotatePolygon(hullConvex,centroid,-1.0*edgeAngle,factory).getEnvelope();
			double area = rotateRect.getArea();
			//Stock the the ractangle with minimum area and minimum angle
			if (area < minimumArea) {
				minimumArea = area; 
				adjustedRec = rotateRect;
				minimumAngle = edgeAngle; 
			}
			currentCoord = nextCoord;
		}
		
		return rotatePolygon(adjustedRec,centroid,minimumAngle,factory);
	}
	
	/**
	 *  This function is to compute a new polygon with a rotation around the centroid with an angle
	 *  
	 * @param poly
	 * @param centroid
	 * @param angle
	 * @param factory
	 * 
	 * @return Polygon
	 */
	public Polygon rotatePolygon(Polygon poly, Coordinate centroid, double angle, GeometryFactory factory) {
		LinearRing ring = getRing((LinearRing)poly.getExteriorRing(), centroid, angle, factory);
		LinearRing[] holes = new LinearRing[poly.getNumInteriorRing()];
		for(int j=0; j<poly.getNumInteriorRing(); j++) {
			holes[j] = getRing((LinearRing)poly.getInteriorRingN(j), centroid, angle, factory);
		}
		return factory.createPolygon(ring, holes);
	}
	
	/**
	 * 
	 * @param ring
	 * @param centroid
	 * @param angle
	 * @param factory
	 * @return 
	 */
	public  LinearRing getRing(LinearRing ring, Coordinate centroid, double angle, GeometryFactory factory) {
		int n = ring.getCoordinates().length;
		Coordinate[] coordComp= new Coordinate[n];
		double cosAngle = Math.cos(angle);
		double sinAngle = Math.sin(angle);
		Coordinate c;
		double x, y;
		for(int i=0; i<n; i++) {
			c = ring.getCoordinates()[i];
			x = c.x;
			y = c.y;
			coordComp[i] = new Coordinate(centroid.x+cosAngle*(x-centroid.x) -sinAngle*(y-centroid.y), 
										  centroid.y+sinAngle*(x-centroid.x)+cosAngle*(y-centroid.y));
		}
		return factory.createLinearRing(coordComp);
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
