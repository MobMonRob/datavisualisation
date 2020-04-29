/**
 * POVRayScriptFactory.java
 */
package de.orat.povray;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.List;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jzy3d.maths.Coord3d;

/**
 *
 * @author Oliver Rettig
 *
 * TODO
 * - Bug Beschriftung in der Ausrichtung beheben
 *
 */
public class POVRayScriptFactory  {
    
    // Skalierungsfaktor durch den alle Positionen im Raum dividiert werden
    public static double scale = 1d;
              
    //public static long bufferSize = 50000;
    private Writer out;
    
    private HashSet pointDefs = new HashSet();
    
    public POVRayScriptFactory(File outfile, File headerfile) throws FileNotFoundException {
         out = new BufferedWriter(new OutputStreamWriter(
                      new FileOutputStream(outfile,false)));
         if (headerfile != null){
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(headerfile)));
            try {
               String line;
               while ((line = in.readLine()) != null) {
                   try {
                       out.write(line);
                       out.write("\n");
                   } catch (IOException ex) {
                       Logger.getLogger(POVRayScriptFactory.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
            } catch (IOException ex1) {
               Logger.getLogger(POVRayScriptFactory.class.getName()).log(Level.SEVERE, null, ex1);
            } finally {
                  try {
                   in.close();
                  } catch (IOException ex2){}
            }
         }
    }
    
    public double getScale(){
        return scale;
    }
    
    public boolean addComment(String comment){
       boolean result = true;
       try {
        out.write("//");
        out.write(comment);
        out.write("\n");
        out.flush();
       } catch (IOException e){
            System.err.println("POVRayScriptFactory: Fehler beim Schreiben in die Datei: "+e.getMessage());
            result = false;
       }
       return result;
    }
    
    public boolean include(BufferedReader reader){
        boolean result = true;
        if (reader == null){
          System.err.println("POVRayScriptFactory: Fehler beim Einfügen eines Streams: Object=null");
          return false;
        }
        try {
            String line = reader.readLine();
            while (line != null){
                out.write(line);
                out.write("\n");
                line = reader.readLine();
            }
        } catch (IOException e){
            System.err.println("POVRayScriptFactory: Fehler beim Schreiben in die Datei: "+e.getMessage());
            result = false;
        }
        return result;
    }
    public boolean addInclude(String fileName){
       boolean result = true;
       try {
        out.write("#include ");
        out.write(fileName);
        out.write("\n");
        out.flush();
       } catch (IOException e){
            System.err.println("POVRayScriptFactory: Fehler beim Schreiben in die Datei: "+e.getMessage());
            result = false;
       }
       return result;
    }
    
    
    public boolean addLineFeed(){
        boolean result = true;
        try {
            out.write("\n");
        } catch (IOException e){
            System.err.println("POVRayScriptFactory: Fehler beim Schreiben in die Datei: "+e.getMessage());
            result = false;
        }
        return result;
    }
    public boolean addPointDef(String name, Coord3d position) throws IOException {
        boolean result = true;
        if (pointDefs.contains(name)) {
            System.out.println("addPointDef: Der Punkt \""+name+"\" existiert bereits!");
            result = false;
        } else {
            Coord3d scaledPosition = position.clone();
            out.write("#declare ");
            out.write(name);
            out.write(" = <");
            scaledPosition.x = (float) (position.x / scale);
            out.write(String.valueOf(scaledPosition.x));
            out.write(", ");
            scaledPosition.y = (float) (position.y / scale);
            out.write(String.valueOf(scaledPosition.y));
            out.write(", ");
            scaledPosition.z = (float) (position.z / scale);
            out.write(String.valueOf(scaledPosition.z));
            out.write(">;\n");
            out.flush();
        }
        return result;
    }
    public boolean addMarker(String name){
        boolean result = true;
        try {
            out.write("sphere{ ");
            out.write(name);
            out.write(", Rm pigment{color "+"Gray"+"}}\n");

            // Textur etc. versehen
            //FIXME

            out.write("text { ttf \"arial.ttf\", \"");
            out.write(name);
            out.write("\",0.1,0\n            scale 0.5 rotate<20,-45,0> translate ");
            out.write(name);
            out.write("+MKRTRANS pigment{ color Orange } no_shadow}\n\n");
            out.flush();
        } catch (IOException e){
            result = false;
            //TODO
            // Fehlermeldung ausgeben
        }
        return result;
    }
    /**
     * @param origin
     * @param radius
     * @param radiusOfLine
     * @param normal
     * @param color
     * @return
     */
    public boolean addCircle(String origin, String radius, String radiusOfLine, 
                             String normal, String color){
         boolean result = true;
        try {
            out.write("object{ Circle_Line(");
            out.write(origin);
            out.write(", "+radius+", "+radiusOfLine+", "+
                    normal+") pigment{ color "+ color+"}}\n");

            // Textur etc. versehen
            //FIXME

            out.flush();
        } catch (IOException e){
            result = false;
            //TODO
            // Fehlermeldung ausgeben
        }
        return result;
    }
    public boolean addSphere(String origin, String radius, String color){
        boolean result = true;
        try {
            out.write("sphere{ ");
            out.write(origin);
            //out.write(", "+radius+" pigment{ color "+ color+" transmit 0.5}}\n");
            out.write(", "+radius+" pigment{ color "+ color+" transmit 0.0}}\n");

            // Textur etc. versehen
            //FIXME

            out.flush();
        } catch (IOException e){
            result = false;
            //TODO
            // Fehlermeldung ausgeben
        }
        return result;
    }
    
  // das funktioniert zwar, soll aber wieder eliminiert werden zu Gunsten
  // der Methode, die Strings statt Vektor3d-Objekte als Argumente hat
  /*
   * // C-Motion kompatible x=rot, z=blau, y=grün
   *
   *@deprecated
   */
  public boolean addCoordinateSystem(String name, Coord3d origin,
            Coord3d xAxis, Coord3d yAxis, Coord3d zAxis){
      
        // Länge der dargestellten Vektoren
        double l = 100.0;
         
        boolean result = true;
        try {
            // Ursprung des Koordinatensystems 
            String OName = name+"_O";
            addPointDef(OName, origin);
            addPoint(name, OName,"Black");


            // Vektoren ausgeben
            // C-Motion: x=rot, z=blau, y=grün

            // x-Achse
            Coord3d end = xAxis.clone(); // new Point3d(xAxis);
            end.getNormalizedTo((float) l); // scale(l);
            end.add(origin);
            String xName = name+"_X";
            //end.x *=scale;
            //end.y *=scale;
            //end.z *=scale;
            addPointDef(xName, end);
            addVector("x",OName, xName, "Red");

            end = yAxis.clone(); // new Point3d(yAxis);
            end.normalizeTo((float) l);//scale(l);
            end.add(origin);
            //end.x *=scale;
            //end.y *=scale;
            //end.z *=scale;
            String yName = name+"_Y";
            addPointDef(yName, end);
            addVector("y",OName, yName, "Green");

            end = zAxis.clone(); // new Point3d(zAxis);
            end.normalizeTo((float) l); // scale(l);
            end.add(origin);
            //end.x *=scale;
            //end.y *=scale;
            //end.z *=scale;
            String zName = name+"_Z";
            addPointDef(zName, end);
            addVector("z",OName, zName, "Blue");

            // Beschriftung
            out.write("text { ttf \"arial.ttf\", \"");
            out.write(name);
            out.write("\",0.1,0\n            scale 0.5 rotate<20,-45,0> translate ");
            out.write(name);
            out.write("_O");
            out.write("+CSYSTRANS pigment{ color Orange } no_shadow}\n\n");
        } catch (IOException e){
            result = false;
            //TODO
            // Fehlermeldung ausgeben
        }
        return result;
    }
    
    // ungetestet
    public boolean addCoordinateSystem(String name, String origin,
            String xAxis, String yAxis, String zAxis){
      
        boolean result = true;
        // Länge der dargestellten Vektoren
        double l = 1.0;
         
        // Ursprung des Koordinatensystems 
        result &= addPoint(name, origin);
        
        
        // Vektoren ausgeben
        // C-Motion: x=rot, z=blau, y=grün
        
        // x-Achse
        // das ist noch nicht überprüft!!!
        String end = "<"+xAxis+".x * scale, "
                        +xAxis+".y * scale, "
                        +xAxis+".z * scale> + "+origin;
        
        result &= addVector("x",origin, end, "Red");
        
        // y-Axis
        // das ist noch nicht überprüft!!!
        end = "<"+yAxis+".x * scale, "
                 +yAxis+".y * scale, "
                 +yAxis+".z * scale> + "+origin;
        
        result &= addVector("y",origin, end, "Green");
        
        end = "<"+zAxis+".x * scale, "
                 +zAxis+".y * scale, "
                 +zAxis+".z * scale> + "+origin;
        result &= addVector("z",origin, end, "Blue");
        
        // Beschriftung
        try {
            out.write("text { ttf \"arial.ttf\", \"");
            out.write(name);
            out.write("\",0.1,0\n            scale 0.5 rotate<20,-45,0> translate ");
            out.write(name);
            out.write("_O");
            out.write("+CSYSTRANS pigment{ color Orange } no_shadow}\n\n");
        } catch (IOException e){
            result = false;
            //TODO
            // Fehlermeldung ausgeben
        }
        return result;
    }
     
    // um Richtungen z.B. von einem Vektorfeld auf einer Kugel zu visualisieren
    public boolean addCone(String name, String position, String direction, String color){
        boolean result = true;
        try { 
            out.write("object{ SmallVector (");
            out.write(position);
            out.write(", ");
            out.write(direction);
            out.write(", 0.055)\n        pigment{color "+color+"}}\n\n");
            
            // Beschriftung anbringen
            //FIXME
            if (name != null && !name.equals("")){
            
            }
            
        } catch (IOException e){
            result = false;
        }
        return result;
    }
    
    public boolean addVector(String name, String PName, String PEName, 
                                 String color){
        boolean result = true;
        try { 
            out.write("object{ Vector (");
            out.write(PName);
            out.write(", ");
            out.write(PEName);
            out.write(", 0.055)\n        pigment{color "+color+"} no_shadow}\n\n");
            
            // Beschriftung anbringen
            //FIXME
            if (name != null && !name.equals("")){
            
            }
            
        } catch (IOException e){
            result = false;
        }
        return result;
    }

    // vermutlich geht das nicht
    public boolean addStraightLine(String name, String PName, String PEName,
                                 String color){
        boolean result = true;
        try {
            out.write("object{ StraightLine (");
            out.write(PName);
            out.write(", ");
            out.write(PEName);
            out.write(", 0.055)\n        pigment{color "+color+"}}\n\n");

            // Beschriftung anbringen
            //FIXME
            if (name != null && !name.equals("")){

            }

        } catch (IOException e){
            result = false;
        }
        return result;
    }

    public boolean addPoint(String name, String position){
        boolean result = true;
        try {
            out.write("sphere{ ");
            out.write(position);
            out.write(", Rp pigment{color "+"Red"+"}}\n");
            out.flush();
            // eventuell Beschriftung anbringen
            //FIXME
        } catch (IOException e){
            result = false;
        }
        return result;
        
    }
    public boolean addPoint(String name, String position, String color){
        boolean result = true;
        try {
            out.write("sphere{ ");
            out.write(position);
            out.write(", Rp pigment{color "+color+" }}\n");
            out.flush();
            // eventuell Beschriftung anbringen
            //FIXME
        } catch (IOException e){
            result = false;
        }
        return result;
        
    }
    public boolean addPosition(String position){
        boolean result = true;
        try {
            out.write("sphere{ ");
            out.write(position);
            out.write(", 0.02 pigment{color "+"Gray"+"}}\n");
            out.flush();
        } catch (IOException e){
            result = false;
        }
        return result;
        
    }
    
    public boolean addStickfigure(List<String[]> sticks){
       Iterator it = sticks.iterator();
       boolean result = true;
       try {
           while (it.hasNext()){
                String[] points = (String[]) it.next();
                String start = points[0];
                String end = points[1];
                out.write("cylinder{ ");
                out.write(start);
                out.write(", ");
                out.write(end);
                out.write(" Rl pigment{color Green}}\n");
           }
       } catch (IOException e){
            result = false;
            //TODO
            // Fehlermeldung ausgeben
       }
       return result;
    }
    
    public boolean close(){
        boolean result = true;
        try {
            out.close();
        } catch (IOException e){
            result = false;
            // TODO
            // Fehlermeldung ausgeben
        }
        return result;
    }
}
