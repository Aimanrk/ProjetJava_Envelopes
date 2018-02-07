package ensg.eu.project.enveloppes;

import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class is the mother class of all the envelopes 
 * 
 * Contain some functions used in the to implement envelopes
 * 
 * @author Aiman R'KYEK
 *
 */
public abstract class Envelope implements IEnvelope{
	
	/**
	* This function return the minimum x coordinate in the list
	*  
	* @param pointsList List of point of type Point
	* @return return Xmin
	*/
	public double getXmin(List<Point> pointsList) {
		List<Double> xList = new ArrayList<Double>();
		for(Point p : pointsList) {
			double x = p.getX();
			xList.add(x);
		}
		double xmin = xList.get(0);
		for(double x : xList) {
			if(x<=xmin) {
				xmin = x;
			}
		}
		return xmin;
	}
	
	/**
	* This function return the maximum x coordinate in the list
	*  
	* @param pointsList List of point of type Point
	* @return return Xmax
	*/
	public double getXmax(List<Point> pointsList) {
		List<Double> xList = new ArrayList<Double>();
		for(Point p : pointsList) {
			double x = p.getX();
			xList.add(x);
		}
		double xmax = xList.get(0);
		for(double x : xList) {
			if(x>=xmax) {
				xmax = x;
			}
		}
		return xmax;
	}
	
	/**
	* This function return the minimum y coordinate in the list
	*  
	* @param pointsList List of point of type Point
	* @return return Ymin
	*/
	public double getYmin(List<Point> pointsList) {
		List<Double> yList = new ArrayList<Double>();
		for(Point p : pointsList) {
			double y = p.getY();
			yList.add(y);
		}
		double ymin = yList.get(0);
		for(double y : yList) {
			if(y<=ymin) {
				ymin = y;
			}
		}
		return ymin;
	}
	
	/**
	* This function return the minimum y coordinate in the list
	*  
	* @param pointsList List of point of type Point
	* @return return Ymax
	*/
	public double getYmax(List<Point> pointsList) {
		List<Double> yList = new ArrayList<Double>();
		for(Point p : pointsList) {
			double y = p.getY();
			yList.add(y);
		}
		double ymax = yList.get(0);
		for(double y : yList) {
			if(y>=ymax) {
				ymax = y;
			}
		}
		return ymax;
	}

}
