package de.orat.povray;

//import de.hd.gaitlab.MotionAnalysis.Functions.PartingRightSide;
//import de.orat.povray.POVRayScriptFactory;
import de.dhbw.conjunctvisu.Utils;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jzy3d.maths.Coord3d;

/**
 * @author Oliver Rettig
 *
 * bereits in das package Povraytools kopiert. kann hier gelöscht werden
  */
public class VectorFieldVisualisation {
    
    public VectorFieldVisualisation() {
    }
    
    /*protected void addPointDef(StringBuffer buffer, String name, Vector3d value){
        
        buffer.append("#declare ");
        buffer.append(name);
        buffer.append(" = <");
        buffer.append(value.x);
        buffer.append(", ");
        buffer.append(value.y);
        buffer.append(", ");
        buffer.append(value.z);
        buffer.append(">;\n");
    }*/
    public static String calculatePOVRayVisualisation(POVRayScriptFactory scriptFactory,
                                  List<Coord3d> supportingPoints, List<Coord3d> referenceVectors)
                         throws IOException {
        StringBuilder buffer = new StringBuilder();
        
        double r = 8d;
        double l = 1.5d;
        for (int i=0;i<supportingPoints.size();i++){
           
            Coord3d P = (Coord3d) supportingPoints.get(i);
            P.normalizeTo( (float) r);
            P.z = -P.z; // wegen linkshändigem Koordinatensystem in Povray
            Coord3d V = (Coord3d) referenceVectors.get(i);
            V.z = -V.z; // wegen linkshändigem Koordinatensystem in Povray
            //String color = "Red";
            //if (Math.abs(P.dot(V)) > iCalcModelElement.EPSILON*10d){
            //    color = "Blue";
            //}
            String PName = "P"+i;
            String VName = "V"+i;
            scriptFactory.addPointDef(PName, P);
            scriptFactory.addPointDef(VName, V);
            
            //buffer.append("sphere{ P");
            //buffer.append(i);
            //buffer.append(", Rp pigment{color ").append(color).append("}}\n");
            
            Coord3d PE = V.clone(); //new Vector3d(V);
            String PEName = "PE"+i;
            PE.normalizeTo((float) l);//scale(r);
            PE = PE.add(P);
            scriptFactory.addPointDef(PEName,PE);
            
            scriptFactory.addVector(null, PName, PEName, "Blue");
            
            // addVector
            //buffer.append("object{ Vector (");
            //buffer.append(PName);
            //buffer.append(", ");
            //buffer.append(PEName);
            //buffer.append(", 0.055)\n        pigment{color YellowGreen}}\n\n");
        }
        return buffer.toString();
    }
    /*protected Vector calculateReferenceVectors(Vector supportingPoints){
        Vector result = new Vector();
        for (int i=0;i<supportingPoints.size();i++){
            //result.add(PartingLeftSide.calculateReferenceVectorPartingLeftSide((Vector3d) supportingPoints.get(i)));
            result.add(PartingRightSide.calculateReferenceVectorPartingRightSide((Vector3d) supportingPoints.get(i)));
        }
        return result;
    }*/
    /*protected Vector calculateSupportingPoints(){
        
        int n = 16; // Zahl der Punkte auf einem Breitengrad
        int m = 8;  // Zahl der Breitengrade
        double radius = 3.0d; // Radius der Kugel
        
        Vector result = new Vector();
        
        // Breitengrad durchiterieren
        // phi von 0 bis pi
        for (int i=0;i<=m;i++){
            // Längengrade durchiterieren
            // theta von 0 bis 2pi
            for (int j=0;j<=n;j++){
                double theta = Math.PI*2d*j/n;
                double phi =  Math.PI*i/m;
                Vector3d p = new Vector3d(cartesianCoordinates(radius,theta,phi));
                // WORKAROUND
                p.add(new Vector3d(0.01,0.01,0.01));
                result.add(p);
            }
        }
        return result;
    }*/
    
    // theta von 0 bis pi Längengrad
    // phi von 0 bis 2pi Breitengrad
    /*public static Vector3d cartesianCoordinates(double radius, double theta, 
                                                               double phi){
        Vector3d result =  new Vector3d(Math.sin(theta)*Math.cos(phi),
                            Math.sin(theta)*Math.sin(phi),
                            Math.cos(theta));
        result.scale(radius);
        return result;
    }*/
    
    public static void main(String[] args) throws IOException {
        
        boolean excludePoles = true;
        File headerFile = null;
        File outputFile = new File("vectorfieldConjunct.inc");
        POVRayScriptFactory scriptFactory =
                new POVRayScriptFactory(outputFile, headerFile);
        
        VectorFieldVisualisation obj = new VectorFieldVisualisation();
        List<Coord3d> supportingPoints = Utils.createPositionSet(excludePoles);
        List<Coord3d> referenceVectors = Utils.createMasudaN(excludePoles);
        String result = VectorFieldVisualisation.calculatePOVRayVisualisation(
                scriptFactory,supportingPoints,referenceVectors);
        //System.out.println(result);
        
        
        outputFile = new File("vectorfieldCardan.inc");
        scriptFactory =
                new POVRayScriptFactory(outputFile, headerFile);
        
        obj = new VectorFieldVisualisation();
        supportingPoints = Utils.createPositionSet(excludePoles);
        referenceVectors = Utils.createCardanN(excludePoles);
        result = VectorFieldVisualisation.calculatePOVRayVisualisation(
                scriptFactory,supportingPoints,referenceVectors);
        
        
        
        outputFile = new File("vectorfieldtwostep.inc");
        scriptFactory =
                new POVRayScriptFactory(outputFile, headerFile);
        
        obj = new VectorFieldVisualisation();
        supportingPoints = Utils.createPositionSet(excludePoles);
        referenceVectors = Utils.createTwoStepN(excludePoles);
        result = VectorFieldVisualisation.calculatePOVRayVisualisation(
                scriptFactory,supportingPoints,referenceVectors);
    }
}
