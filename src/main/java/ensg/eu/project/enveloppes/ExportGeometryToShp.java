package ensg.eu.project.enveloppes;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
* This class export the envelopes to shp 
*
* @author Aiman R'KYEK (A part of the code is taken from documentary of geotools)
* 
*/
public class ExportGeometryToShp {
	
	/**
	 * This function export a polygon into a shapefile 
	 * 
	 * @param nomShape  name of the shp to export
	 * @param p polygon to export
	 * @throws Exception
	 */
	public void exportPolygon(String nomShape, Polygon p) throws Exception {
		
		Coordinate[]  coordinates = p.getCoordinates();

		GeometryFactory geometryFactory = new GeometryFactory();
		
		// Define Lambert 93 as the coordinate system 
		String wkt = "PROJCS[\"RGF93_Lambert_93\", \n" + "  GEOGCS[\"GCS_RGF93\", \n" + "    DATUM[\"D_RGF_1993\", \n"
				+ "      SPHEROID[\"GRS_1980\", 6378137.0, 298.257222101]], \n" + "    PRIMEM[\"Greenwich\", 0.0], \n"
				+ "    UNIT[\"degree\", 0.017453292519943295], \n" + "    AXIS[\"Longitude\", EAST], \n"
				+ "    AXIS[\"Latitude\", NORTH]], \n" + "  PROJECTION[\"Lambert_Conformal_Conic\"], \n"
				+ "  PARAMETER[\"central_meridian\", 3.0], \n" + "  PARAMETER[\"latitude_of_origin\", 46.5], \n"
				+ "  PARAMETER[\"standard_parallel_1\", 49.0], \n" + "  PARAMETER[\"false_easting\", 700000.0], \n"
				+ "  PARAMETER[\"false_northing\", 6600000.0], \n" + "  PARAMETER[\"scale_factor\", 1.0], \n"
				+ "  PARAMETER[\"standard_parallel_2\", 44.0], \n" + "  UNIT[\"m\", 1.0], \n"
				+ "  AXIS[\"x\", EAST], \n" + "  AXIS[\"y\", NORTH]]";

		CoordinateReferenceSystem dataCRS = CRS.parseWKT(wkt);
		CoordinateReferenceSystem worldCRS = CRS.parseWKT(wkt);
		MathTransform transform = CRS.findMathTransform(dataCRS, worldCRS, true);
		
		String saveFilepath = nomShape;
		File theFile = new File(saveFilepath);

		DataStoreFactorySpi factory = new ShapefileDataStoreFactory();
		
		Map<String, Serializable> create = new HashMap<String, Serializable>();
		create.put("url", theFile.toURI().toURL());
		create.put("create spatial index", Boolean.TRUE);
		DataStore dataStore = factory.createNewDataStore(create);
		
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("TYPE");
		builder.add("Polygon", Polygon.class);
		builder.length(15).add("Name", String.class);

		final SimpleFeatureType featType = builder.buildFeatureType();

		SimpleFeatureType TYPE = featType;
		SimpleFeatureType featureType = SimpleFeatureTypeBuilder.retype(TYPE, worldCRS);

		List<SimpleFeature> features = new ArrayList<SimpleFeature>();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
		
		featureBuilder.add(geometryFactory.createPolygon(coordinates));
		features.add(featureBuilder.buildFeature(null));
		
		SimpleFeatureCollection featureCollection = new ListFeatureCollection(featureType, features);
		dataStore.createSchema(featureType);
		String name = dataStore.getTypeNames()[0];
		Transaction t = new DefaultTransaction("project");
		
		try (FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriterAppend(name,t);
		SimpleFeatureIterator iterator = featureCollection.features()) {
			while (iterator.hasNext())
			{
				SimpleFeature feature = iterator.next();
				SimpleFeature copy = writer.next();
				copy.setAttributes(feature.getAttributes());
				Geometry geom = (Geometry) feature.getDefaultGeometry();
				Geometry geomTrans = JTS.transform(geom, transform);
				copy.setDefaultGeometry(geomTrans);
				writer.write();
			}

			t.commit();
		} catch (Exception st) {
			st.printStackTrace();
			t.rollback();
		} finally {
			t.close();
		}
	}
}
