package ensg.eu.project.enveloppes;

/**
 * This class is to generate points
 * 
 * @author Aiman R'KYEK
 *
 */
public class Point {
	
	private double x ;
	private double y ;
	
	public Point(){
		this.x = Double.NaN;
		this.y = Double.NaN;
	}
	
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}
}