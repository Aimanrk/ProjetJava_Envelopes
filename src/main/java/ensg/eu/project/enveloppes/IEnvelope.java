package ensg.eu.project.enveloppes;

import java.util.List;
import com.vividsolutions.jts.geom.Polygon;

public interface IEnvelope {
	
	public Polygon getEnvelope(List<Point> pointsList);
	public void exportEnveloppe(String shapeName) throws Exception;
}
