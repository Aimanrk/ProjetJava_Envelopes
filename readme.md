# Importation de l'API

Pour utiliser l'api il faut d'abord importer la librairie .jar dans les dépendances 

```
	<dependency>
	  <groupId>ensg.eu.project</groupId>
	  <artifactId>enveloppes</artifactId>
	  <version>0.0.1-SNAPSHOT</version>
  	  <scope>system</scope>
  	  <systemPath>/target/enveloppes-0.0.1-SNAPSHOT.jar</systemPath>
	</dependency>
```

# BoundingBox

```java
	//pointsList est la liste des points à envelopper
	BoundingBox bb = new BoundingBox(pointsList);

	//Enveloppe
	Polygon p = bb.getEnvelope(pointsList);

	//Exportation en shp 
	//shapeName: Nom du fichier à exporter
	bb.exportEnveloppe(shapeName);	
	
```

# Enveloppe convexe en utilisant l'algorithme Jarvis

```java
	//pointsList est la liste des points à envelopper
	JarvisConvexHull j = new JarvisConvexHull(pointsList);

	//Enveloppe
	Polygon p = j.getEnvelope(pointsList);

	//Exportation en shp 
	//shapeName: Nom du fichier à exporter
	j.exportEnveloppe(shapeName);	
	
```
# Enveloppe convexe en utilisant l'algorithme QuickHull

```java
	//pointsList est la liste des points à envelopper
	QuickConvexHull q = new QuickConvexHull(pointsList);

	//Enveloppe
	Polygon p = q.getEnvelope(pointsList);

	//Exportation en shp 
	//shapeName: Nom du fichier à exporter
	q.exportEnveloppe(shapeName);	
	
```
# Oriented Minimum Bounding Box

```java
	//pointsList est la liste des points à envelopper
	OrientedMinimumBoundingBox ombb = new OrientedMinimumBoundingBox(pointsList);


	//Enveloppe
	Polygon p = ombb.getEnvelope(pointsList);

	//Exportation en shp 
	//shapeName: Nom du fichier à exporter
	ombb.exportEnveloppe(shapeName);	
	
```

