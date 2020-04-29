package de.dhbw.conjunctvisu;

import java.util.ArrayList;
import java.util.List;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.RotationMatrix2;
import org.jzy3d.maths.Vector3d;

/**
 *
 * @author Oliver Rettig
 */
public class Utils {
    
    // Zahl der Längen-/Breitenkreise in der Darstellung des Vektorfelds
    // immer gerade Anzahl!
    private static final double longitudes = 12; // Längenkreise (von oben nach unten) fester Azimutwinkel in der Ebene
    private static final double latitudes = 12; // Breitenkreise, Polarwinkel, einer mehr damit Positionen an beiden Polen
    
    // Koordinatensystem:
    // x nach vorne
    // z nach lateral/rechts
    // y nach oben
    // bei gamma=0 und beta=0 zeigt humerus nach unten und Unterarm nach vorne in x-Richtung
    // beta läuft von 0 nach -180Grad
    
    /**
     * Create a list of positions, defined by spherical coordinates.
     * 
     * For each longitude a list of positions are defined laying on a sequence
     * of latitudes.
     * 
     * @param excludePoles if true no positions are created for the poles (beta=-180 and beta=0)
     * @return List of arrays with angles {gamma=longitude, beta=latitude, in radians}
     */
    public static List<double[]> createPositionSetAngles(boolean excludePoles){
        List<double[]> result = new ArrayList<>();
        // gamma=0: Frontalebene, Drehung nach vorne
        // [-180,180], damit gezeigt werden kann, dass bei 180 und -180 verschiedenes
        // rauskommt, d.h. 13 Winkelpositionen, also in 30Grad-Schritten
        for (int i=-(int) longitudes/2;i<= (int) longitudes/2;i++){
            //TODO als function from longitudes formulieren
            double gamma = ((double) i)*30d/180d * Math.PI;
            //System.out.println("i="+i+" gamma="+gamma);
            // Breitenkreise: beta = [-180;0]
            // [-90,0] obere Hemisphere, [-180,-90] untere Hemisphere
            for (int j=0;j<=latitudes;j++){
                if (!excludePoles || (j != 0 && j != latitudes)){
                    double beta = - ((double) j) * 15d/180d * Math.PI;
                    result.add(new double[]{gamma, beta});
                }
            }
        }
        return result;
    }
    
    /**
     * Calculate a List of spherical coordinates (gamma, beta) on the lower hemisphere.
     * 
     * @return {gamma, beta} in degree
     */
    public static List<double[]> createPositionSetAnglesLowerHemisphere(){
        List<double[]> result = new ArrayList<>();
        // Längenkreise: gamma = [0,360]
        // gamma=0: Frontalebene, Drehung nach vorne
        //for (int i=0;i<6/*longitudes*/;i++){
        // [-180,180], damit gezeigt werden kann, dass bei 180 und -180 verschiedenes
        // rauskommt.
        for (int i=-6;i<7/*longitudes*/;i++){
           double gamma = ((double) i)*30d/180d * Math.PI;
            // Breitenkreise: beta = [-180;0]
            // [-180,-90] untere Hemisphere
            for (int j=6;j<=latitudes;j++){
                double beta = - ((double) j) * 15d/180d * Math.PI;
                result.add(new double[]{gamma, beta});
            }
        }
        return result;
    }
    
    /**
     * Create a list of spherical coordinates for the upper hemisphere.
     * 
     * @return list of {gamma, beta} with gamma [-pi,pi] and beta [-p/2,0]
     */
    public static List<double[]> createPositionSetAnglesUpperHemisphere(){
        List<double[]> result = new ArrayList<>();
        // Längenkreise: 
        // gamma=0: Frontalebene, Drehung nach vorne
        // gamma = [-180,180], damit gezeigt werden kann, dass bei 180 und -180 verschiedenes
        // rauskommt.
        for (int i=-6;i<7/*longitudes*/;i++){
           double gamma = ((double) i)*30d/180d * Math.PI;
            // Breitenkreise: beta = [-180;0]
            // [-90,0] obere Hemisphere
            for (int j=0;j<7;j++){
                double beta = - ((double) j) * 15d/180d * Math.PI;
                result.add(new double[]{gamma, beta});
            }
        }
        return result;
    }
    
    /**
     * Create a set of position on a sphere based on a set of sperical angles.
     * 
     * Achtung: die y-Position entspricht dem Ellenbogengelenkszentrum und damit
     * der negativen y-Achse.
     * 
     * @param excludePoles
     * @return a list of cartesian coordinates
     */
    public static List<Coord3d> createPositionSet(boolean excludePoles){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAngles(excludePoles).forEach((angles) -> {
            // 1. Ableitung aus Kugelkoordinten:
            // x --> z
            // y --> x
            // z --> y
            // phi --> gamma
            // gamma --> -beta
            
            // 2. Ableitung nach Masuda oder Cardan yxy siehe Wikipedia
            // Zweite Spalte der Matrix R in Masuda ist die y-Achse, h und h2 sind im paper 
            // fälschlicherweise vertauscht
            // am Schluss die Vorzeichen von x und z tauschen, warum? d.h. ist der gleiche
            // Schritt wie gamma --> -beta
            double x = -Math.sin(angles[0])*Math.sin(angles[1]); 
            double y = Math.cos(angles[1]);
            double z = -Math.sin(angles[1]) * Math.cos(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    
    /**
     * Create random frames corresponding to a predefined set of points on the sphere.
     * 
     * testweise, frames corresponding to the cardan reference vector field.
     * 
     * überprüft für  gamma=0 und 90 degree, und beta=-90
     * 
     * @param excludePoles
     * @return 
     */
    public static List<RotationMatrix2> createRandomFrames(boolean excludePoles){
       
        List<Coord3d> rList = createPositionSet(excludePoles);
        
        // zuerst einmal testweise mit cardan vectorfeld
        List<Coord3d> vList = createCardanN(excludePoles);
        
        List<RotationMatrix2> result = new ArrayList<>();
        for (int i=0;i<rList.size();i++){
            double[][] m = new double[3][3];
            Coord3d v = vList.get(i);
            m[0][0] = v.x;
            m[1][0] = v.y;
            m[2][0] = v.z;
            
            // Achtung: hier ist die y-Achse des gedrehten Koordinatensystems
            // gemeint und nicht die Richtung des Oberarms mit dem Ellenbogen als Spitze
            // daher muss das Vorzeichen gewechselt werden
            Coord3d r = rList.get(i).negative();
            m[0][1] = r.x;
            m[1][1] = r.y;
            m[2][1] = r.z;
            
            Coord3d z = cross(v,r);
            m[0][2] = z.x;
            m[1][2] = z.y;
            m[2][2] = z.z;
            result.add(new RotationMatrix2(m));
        }
        return result;
    }
    
    public static List<Coord3d> createPositionSetLowerHemisphere(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesLowerHemisphere().forEach((angles) -> {
            // 1. Ableitung aus Kugelkoordinten:
            // x --> z
            // y --> x
            // z --> y
            // phi --> gamma
            // gamma --> -beta
            
            // 2. Ableitung nach Masuda oder Cardan yxy siehe Wikipedia
            // Zweite Spalte der Matrix R in Masuda ist die y-Achse, h und h2 sind im paper 
            // fälschlicherweise vertauscht
            // am Schluss die Vorzeichen von x und z tauschen, warum? d.h. ist der gleiche
            // Schritt wie gamma --> -beta
            double x = -Math.sin(angles[0])*Math.sin(angles[1]); 
            double y = Math.cos(angles[1]);
            double z = -Math.sin(angles[1]) * Math.cos(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    public static List<Coord3d> createPositionSetUpperHemisphere(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesUpperHemisphere().forEach((angles) -> {
            // 1. Ableitung aus Kugelkoordinten:
            // x --> z
            // y --> x
            // z --> y
            // phi --> gamma
            // gamma --> -beta
            
            // 2. Ableitung nach Masuda oder Cardan yxy siehe Wikipedia
            // Zweite Spalte der Matrix R in Masuda ist die y-Achse, h und h2 sind im paper 
            // fälschlicherweise vertauscht
            // am Schluss die Vorzeichen von x und z tauschen, warum? d.h. ist der gleiche
            // Schritt wie gamma --> -beta
            double x = -Math.sin(angles[0])*Math.sin(angles[1]); 
            double y = Math.cos(angles[1]);
            double z = -Math.sin(angles[1]) * Math.cos(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    public static List<Coord3d> createTwoStepN(boolean excludePoles){
        List<Coord3d> result = new ArrayList<>();
        List<double[]> positionSetAngles = createPositionSetAngles(excludePoles);
        for (int i=0;i<positionSetAngles.size();i++){
            double beta = positionSetAngles.get(i)[1];
            double gamma = positionSetAngles.get(i)[0];
            result.add(createTwoStepN(gamma, beta));
        }
        return result;
    }
    
    public static Coord3d createTwoStepN(double gamma, double beta){
        return new Coord3d((1+Math.cos(beta))*Math.pow(Math.cos(gamma),2)-Math.cos(beta), 
                                     -Math.sin(beta)*Math.sin(gamma), 
                                    -(1+Math.cos(beta))*Math.cos(gamma)*Math.sin(gamma));
    }
    /*public static List<RotationMatrix2> createTwoStepNErsterVersuch(){
        List<RotationMatrix2> result = new ArrayList<>();
        List<Coord3d> positions = createPositionSet();
        List<double[]> positionSetAngles = createPositionSetAngles();
        for (int i=0;i<positions.size();i++){
            //Coord3d y0 = new Coord3d(0,-1,0);
            //coord3d r = positions.get(i);
            //Coord3d u = y0.cross(r);
            double azimutAngle = positionSetAngles.get(i)[0];
            Coord3d u = new Coord3d(Math.cos(azimutAngle), 0d, Math.sin(azimutAngle));
            double rotAngle = positionSetAngles.get(i)[1]+Math.PI; // das ist irgendwie -beta oder ähnlich
            RotationMatrix2 rot = new RotationMatrix2(u, rotAngle);
            RotationMatrix2 base = new RotationMatrix2();
            result.add(new RotationMatrix2(rot.transform(base.m)));
        }
        return result;
    }*/
    public static List<Coord3d> createCardanN(boolean excludePoles){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAngles(excludePoles).forEach((angles) -> {
            // Masuda R, erste Spalte (h und h2, vertauscht, h2=0) oder Cardan yxy, siehe Wikipedia
            double x = Math.cos(angles[0]);
            double y = 0d;
            double z = -Math.sin(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    
    public static List<Coord3d> createCardanNlH(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesLowerHemisphere().forEach((angles) -> {
            // Masuda R, erste Spalte (h und h2, vertauscht, h2=0) oder Cardan yxy, siehe Wikipedia
            double x = Math.cos(angles[0]);
            double y = 0d;
            double z = -Math.sin(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    public static List<Coord3d> createCardanNuH(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesUpperHemisphere().forEach((angles) -> {
            // Masuda R, erste Spalte (h und h2, vertauscht, h2=0) oder Cardan yxy, siehe Wikipedia
            double x = Math.cos(angles[0]);
            double y = 0d;
            double z = -Math.sin(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    
    /**
     * Create reference vector field following Masdua et al 2008. Keep in mind: The
     * field is unphysiological for the upper hemisphere and is therefor not proposed.
     * 
     * @param excludePoles
     * @return list of reference vectors from type Coord3d
     */
    public static List<Coord3d> createMasudaN(boolean excludePoles){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAngles(excludePoles).forEach((angles) -> {
            // erste Spalte aus R: Cardan yxy oder Masuda R aber h und h2 vertauscht 
            // und h2 = -h*cos(beta)
            // beta --> - beta
            double gamma2 = -angles[0]*Math.cos(angles[1]);
            double x = (Math.cos(angles[0]) * Math.cos(gamma2) - Math.sin(angles[0])*Math.cos(angles[1])*Math.sin(gamma2));
            double y = -(Math.sin(angles[1]) * Math.sin(gamma2));
            double z = (-Math.sin(angles[0]) * Math.cos(gamma2) - Math.cos(angles[0])*Math.cos(angles[1])*Math.sin(gamma2));
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    
    /**
     * Create Conjunct rotatation reference vector field for the lower hemisphere
     * based on the formulae from Masuda et al 2008.
     * 
     * @return 
     */
    public static List<Coord3d> createMasudaNlH(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesLowerHemisphere().forEach((angles) -> {
            // erste Spalte aus R: Cardan yxy oder Masuda R aber h und h2 vertauscht 
            // und h2 = -h*cos(beta)
            // beta --> - beta
            double gamma2 = -angles[0]*Math.cos(angles[1]);
            double x = Math.cos(angles[0]) * Math.cos(gamma2) - Math.sin(angles[0])*Math.cos(angles[1])*Math.sin(gamma2);
            double y = -Math.sin(angles[1] * Math.sin(gamma2));
            double z = -Math.sin(angles[0]) * Math.cos(gamma2) - Math.cos(angles[0])*Math.cos(angles[1])*Math.sin(gamma2);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    
    /**
     * Create Conjunct rotatation reference vector field for the upper hemisphere
     * based on the formulae from Masuda et al 2008 by rotation and mirroring of 
     * the lower hemispheres field.
     * 
     * @return list of reference vectors for positions of the upper hemisphere
     */
    public static List<Coord3d> createMasudaNuH(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesUpperHemisphere().forEach((angles) -> {
            // untere Hemisphere:
            // erste Spalte aus R: Cardan yxy oder Masuda R aber h und h2 vertauscht 
            // und h2 = -h*cos(beta)
            // beta --> - beta
            // obere Hemisphere:
            // 1. Drehung um 90 Grad um die y-Achse
            // z-->x
            // x-->-z
            // y-->y
            // 2. Spiegelung an Ebene mit y als NormalenVektor bei beta=-90:
            // y-->-y
            // 3. Winkelabbildung
            // gamma-->gamma-90
            // beta--> -180-beta
            //double gamma2 = -(angles[0]-Math.PI/2d)*Math.cos(-Math.PI-angles[1]);
            //double x = -Math.sin(angles[0]-Math.PI/2d) * Math.cos(gamma2) - 
            //                  Math.cos(angles[0]-Math.PI/2d)*Math.cos(-Math.PI-angles[1])*Math.sin(gamma2);
            //double y = Math.sin(-Math.PI-angles[1]) * Math.sin(gamma2);
            //double z = -(Math.cos(angles[0]-Math.PI/2d) * Math.cos(gamma2) - 
            //                  Math.sin(angles[0]-Math.PI/2d)*Math.cos(-Math.PI-angles[1])*Math.sin(gamma2));
            
            // Anwendung der Additionstheoreme:
            // cos(alpha-pi/2)=sin(alpha)
            // sin(alpha-pi/2)=-cos(alpha)
            // sin(-pi-alpha)=sin(alpha)
            // cos(-pi-alpha)=-cos(alpha)
            double gamma2 = (angles[0]-Math.PI/2d)*Math.cos(angles[1]);
            double x = Math.cos(angles[0]) * Math.cos(gamma2) + 
                              Math.sin(angles[0])*Math.cos(angles[1])*Math.sin(gamma2);
            double y = Math.sin(angles[1]) * Math.sin(gamma2);
            double z = -Math.sin(angles[0]) * Math.cos(gamma2) + 
                             Math.cos(angles[0])*Math.cos(angles[1])*Math.sin(gamma2);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
   
    /**
     * Calculate conjunct rotation reference vector for a point given bei longitude
     * and latitude angles.
     * 
     * @param gamma [rad]
     * @param beta [rad]
     * @return conjunct rotation reference vector
     */
    public static Coord3d createConjunctN(double gamma, double beta){
        Coord3d result;
        // lower hemisphere
        if (beta <= -Math.PI/2d){
            // erste Spalte aus R: Cardan yxy oder Masuda R aber h und h2 vertauscht 
            // und h2 = -h*cos(beta)
            // beta --> - beta
            double gamma2 = -gamma*Math.cos(beta);
            double x = Math.cos(gamma) * Math.cos(gamma2) - Math.sin(gamma)*Math.cos(beta)*Math.sin(gamma2);
            double y = -(Math.sin(beta) * Math.sin(gamma2));
            double z = -Math.sin(gamma) * Math.cos(gamma2) - Math.cos(gamma)*Math.cos(beta)*Math.sin(gamma2);
            result = new Coord3d(x,y,z);
            
        // upper hemisphere
        } else {
            double gamma2 = (gamma-Math.PI/2d)*Math.cos(beta);
            double x = Math.cos(gamma) * Math.cos(gamma2) + 
                              Math.sin(gamma)*Math.cos(beta)*Math.sin(gamma2);
            double y = Math.sin(beta) * Math.sin(gamma2);
            double z = -Math.sin(gamma) * Math.cos(gamma2) + 
                             Math.cos(gamma)*Math.cos(beta)*Math.sin(gamma2);
            result = new Coord3d(x,y,z);
        }
        
        return result;
    }
    
    public static List<Coord3d> createConjunctN(boolean excludePoles){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAngles(excludePoles).forEach((angles) -> {
            Coord3d ref = createConjunctN(angles[0], angles[1]);
            result.add(ref);
        });
        return result;
    }
    
    
    public static List<Coord3d> createUpperConjunctNOld(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesUpperHemisphere().forEach((angles) -> {
            result.add(createUpperConjunctNOld(angles[0], angles[1]));
        });
        return result;
    }
    /**
     * Determine conjunct rotation reference vector field for the upper hemisphere
     * following [Wolf2009].
     * 
     * FIXME 
     * - (rot) stimmt alles irgendwie nicht überhein mit der neuen Methode, 
     *   aber besser als die untere Hemisphere
     *   wenigstens scheint das Feld tangential zu sein
     * - am Äquator sollte das singulär werden, wegen dem tangens
     * 
     * @param gamma [rad]
     * @param beta [rad]
     * @return list of reference vectors (normalized and tangential to the unit sphere)
     */
    public static Coord3d createUpperConjunctNOld(double gamma, double beta){
        
        // x' = x
        // y' = -z
        // z' = y
        // phi [-pi/2, 3pi/2]= pi/2-gamma [-pi,pi] (Höhe) Azimutwinkel
        // theta [0;pi] = pi + beta [-pi;0] (Höhe) Polarwinkel
        double theta = Math.PI+beta;
        double b = Math.sin(theta);
        double phi = Math.PI/2-gamma;
        double a2 = 1d/(1+Math.pow(Math.tan(theta),2)*
                        Math.pow(Math.sin((b-1)*phi), 2));
        double x = Math.sqrt(a2)*Math.sin(b*phi);

        // y und z vertauscht u. Vorzeichen von z getauscht, damit Systeme 
        // zu ISB und Masuda passen
        double z = -Math.sqrt(a2)*Math.cos(b*phi);
        double y = Math.sqrt(1-a2); // ohne Vorzeichen scheint es so richtig zu sein

        return new Coord3d(x,y,z);
    }
    
    public static List<Coord3d> createLowerConjunctNOld(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAnglesLowerHemisphere().forEach((angles) -> { 
            result.add(createLowerConjunctNOld(angles[0], angles[1]));
        });
        return result;
    }
    /**
     * Determine conjunct rotation reference vector field for the lower hemisphere
     * following [Wolf2009].
     * 
     * FIXME 
     * - scheint nicht zu stimmen, schlechter als für die obere Hemisphere,
     *   das Feld scheint nicht mal tangential zu sein
     * - bei theta = pi/2 also Äquator sollte das singulär werden wegen dem tangens
     * 
     * @param gamma [rad]
     * @param beta [rad]
     * @return list of reference vectors (normalized and tangential to the unit sphere)
     */
    public static Coord3d createLowerConjunctNOld(double gamma, double beta){
        
        // x' = x
        // y' = -z
        // z' = y
        // theta [0;pi] = pi + beta [-pi;0] (Höhe) Polarwinkel
        // phi [-pi/2, 3pi/2] = pi/2-gamma [-pi,pi] Azimutwinkel

        // phi90 = phi - pi/2 = (pi/2 - gamma) - p/2 = -gamma 

        double theta = Math.PI+beta;
        double b = Math.sin(theta);
        double phi90 = -gamma;
        double a2 = 1d/(1+Math.pow(Math.tan(theta),2)*Math.pow(Math.sin((b-1)*phi90), 2));

        double x = Math.sqrt(a2)*Math.cos(b*phi90); // "-" machts noch schlechter

        // y und z vertauscht u. Vorzeichen von z getauscht damit Systeme 
        // zu ISB und Masuda passen
        double z = Math.sqrt(a2)*Math.sin(b*phi90); // "-" machts noch schlechter
        double y = Math.sqrt(1-a2); // das "-" hats auch nicht gebracht
        // "-" bei z und y oder x und y hats auch schlechter gemacht

        return new Coord3d(x,y,z);
    }
    
    public static Vector3d createVector3d(Coord3d pos, Coord3d dir, float length){
        Coord3d dirN = dir.getNormalizedTo(length/2f);
        Coord3d end = pos.add(dirN);
        dirN = dirN.negative();
        Coord3d start = pos.add(dirN);
        Vector3d result = new Vector3d(start, end);
        return result;
    }
    
    /**
     * Calculate conjunct rotation angle, based on the rotating coordindate system.
     * 
     * @param m 3x3 direction cosine matrix representing the rotationg frame.
     * @return conjunct rotation angle in degree.
     */
    public static double conjunctRotAngle(RotationMatrix2 m){
        double sign = 1d; // +1 == erste Cardan Lösung, -1 == zweite ...
        double result;
        
        //TODO
        // beta = -180,0 müssen separat behandelt werden
        // ausserhalb [-90,90] funktioniert es noch nicht
        
        // ganz unten (beta = -180)
        // dieses if ist eigentlich überflüssig, da das vom zweiten if mit abgedeckt werden könnte
        /*if (-m.m[1][1] == -1){
            // -gammaH+gammaH2
            result = Math.atan(m.m[0][1]/m.m[2][1])+Math.atan(m.m[1][0]/m.m[1][2]);
        
        // untere Hemisphere, d.h. y-Komontente der gedrehten realen y-Achse (vom Ellenbogen zum Schulterzentrum) < 0
        // Vorzeichen/if ist überprüft!
        } else */
        if (m.m[1][1] >= 0){
            //result = Math.atan2(m.m[0][1],m.m[2][1])*m.m[1][1]-Math.atan2(m.m[1][0],m.m[1][2]);
            
            // funktioniert im Bereich [-90,90] weitestgehend, nicht bei gamma=60/30 mit beta [-75,-15]
            // Vorzeichen musste gewechelt werden, damit Außenrotation positiv wird, 
            // Masuda definiert das umgekehrt.
            //result = -Math.atan(m.m[0][1]/m.m[2][1])*m.m[1][1]+Math.atan(m.m[1][0]/m.m[1][2]);
            
            //atan2 und die beiden Vorzeichen in den Argumenten lösen das Problem
            // unklar, warum
            //FIXME
            // ausserdem könnte es sein, dass der zweite atan auch in atan2 gewechselt werden muss
            // falls axiale Rot von mehr als [-90,90] auftreten was möglich sein sollte, aber
            // bisher noch nicht getestet wurde
            //result = m.m[1][1]*Math.atan2(m.m[0][1],-m.m[2][1])+Math.atan(m.m[1][0]/m.m[1][2]););
            
            // stimmt solange ich die Cardan reference vectors einfüge
            // ist bis auf den zweiten atan genau das was ich erawarte
            result = m.m[1][1]*Math.atan2(sign*m.m[0][1],-sign*m.m[2][1])+Math.atan(sign*m.m[1][0]/sign*m.m[1][2]);
            
            // testweise x und y vertauscht, scheint gar nicht zu stimmen
            //result = Math.atan2(m.m[2][1],m.m[0][1])*m.m[1][1]-Math.atan2(m.m[1][2],m.m[1][0]);
             
            // testweise mit Annahme Gamma und Gamma2 wären nicht vertauscht
            //result = -Math.atan2(m.m[1][0],m.m[1][2])*m.m[1][1]+Math.atan2(m.m[0][1],m.m[2][1]);
            
            // testweise mit Annahme Gamma und Gamma2 wäre nicht vertauscht und x und y vertauscht
            //result = -Math.atan2(m.m[1][2],m.m[1][0])*m.m[1][1]+Math.atan2(m.m[2][1],m.m[0][1]);
            
            
        // ganz oben (beta=0)
        // dieses if ist eigentlich überflüssig, da das vom nachfolgenden else mit
        // abgedeckt werden könnte
        } else if (-m.m[1][1] == 1){
             result = Math.acos(m.m[0][0])-Math.PI/2;
             
        // obere Hemisphere
        } else {
            //result = (Math.atan2(m.m[0][1],m.m[2][1])-Math.PI/2d)*m.m[1][1]
            //                       -Math.atan2(m.m[1][0],m.m[1][2]);
            
            // funktioniert im Bereich [-90,90], Vorzeichen muss gewechelt werden,
            // damit Außenrotation positiv wird, Masuda definiert das mit anderem Vorzeichen
            // warum musste für die obere Hemisphere das Vorzeichen erneut gewechselt werden?
            //FIXME
            // testweise auch für den zweiten atan atan2, ergibt 11 Fehler, kein Unterschied?
            result = (Math.atan2(m.m[0][1],-m.m[2][1])+Math.PI/2d)*m.m[1][1]
                                   -Math.atan2(sign*m.m[1][0],sign*m.m[1][2])/*+Math.PI/2*/;
            
            // testweise x und y vertauscht, scheint gar nicht zu stimmen
            //result = (Math.atan2(m.m[2][1],m.m[0][1])-Math.PI/2d)*m.m[1][1]
            //                       -Math.atan2(m.m[1][2],m.m[1][0]);
            
            // testweise mit Annahme Gamma und Gamma2 wäre nicht vertauscht
            //result = (-Math.atan2(m.m[1][0],m.m[1][2])-Math.PI/2d)*m.m[1][1]
            //                       +Math.atan2(m.m[0][1],m.m[2][1]);
            
            // testweise mit Annahme Gamma und Gamma2 wäre nicht vertauscht und x, y vertauscht
            //result = (-Math.atan2(m.m[1][2],m.m[1][0])-Math.PI/2d)*m.m[1][1]
            //                       +Math.atan2(m.m[2][1],m.m[0][1]);
            
            //TODO
            // vermutlich brauche ich noch so etwas:
            // gamma [-180,180], damit ergibt sich result = [-270,90]
            //if (result < -Math.PI/2){
            //    result = Math.PI + result;
            //}
        }
        return result*180d/Math.PI;
    }
    /**
     * Calculate conjunct rotation angle based on arccos-function and a 
     * given reference vector.
     * 
     * @param v orientation vector
     * @param r radius vector
     * @param n reference vector at radius vector r
     * @return conjunct rotation angle in degree
     */
    public static double conjunctRotAngle(Coord3d v, Coord3d r, Coord3d n){
        double value = Math.acos(n.dot(v))*180/Math.PI;
        Coord3d test = cross(n,v);
        if (r.dot(test) < 0){
            value = -value;
        }
        return value;
    }
    /**
     * Calculate conjunct rotation angle based on cardan angles with 
     * rotation order yx'y''.
     * 
     * @param gamma in radians
     * @param beta in radians
     * @param gamma2 in radians
     * @return in degree
     */
    public static double conjunctRotAngle(double gamma, double beta, double gamma2){
        double result;
        // untere Hemisphere
        if (beta <=-Math.PI/2d){
            result = gamma*180/Math.PI*Math.cos(beta)+gamma2;
        // obere Hemisphere
        } else {
            result = (gamma*180/Math.PI-90)*Math.cos(beta)+gamma2;
        }
        // gamma [-180,180], damit ergibt sich result = [-270,90]
        if (result < -180d){
            result = 360d + result;
        }
        return result;
    }
    
    // two step rotation angles
    
    /**
     * Axial rotation based on two-step rotation decomposition.
     * 
     * @param m dircos matrix
     * @return axial rotation in [deg]
     */
    public static double twoStepRotAngle(RotationMatrix2 m){
        double result = Math.atan2((m.m[0][2] - m.m[2][0]),(m.m[0][0]+m.m[2][2]));
        result *=180d/Math.PI;
        return result;
    }
    /** 
     */
    public static void testlH(){
        List<double[]> angleSet = createPositionSetAnglesLowerHemisphere();
        List<Coord3d> masudaNlH = createMasudaNlH();
        List<Coord3d> cardanNlH = createCardanNlH();
        List<Coord3d> positions = createPositionSetLowerHemisphere();
        for (int i=0;i < angleSet.size();i++){
            // (-) für die vordere Hemisphere  gamma = [0,180], (+) für die hintere
            // acos: da kommen immer Werte zwischen [0,pi] raus, d.h. das Vorzeichen geht verloren
            /*double dircosBasedValue = Math.acos(masudaNlH.get(i).dot(cardanNlH.get(i)))*180/Math.PI;
            Coord3d test = masudaNlH.get(i).cross(cardanNlH.get(i));
            if (positions.get(i).dot(test) <0){
                dircosBasedValue = -dircosBasedValue;
            }
            double referenceFieldBasedValue = angleSet.get(i)[0]*180/Math.PI*Math.cos(angleSet.get(i)[1]);
            */
            double value = conjunctRotAngle(cardanNlH.get(i), positions.get(i), masudaNlH.get(i));
            double value2 = conjunctRotAngle(angleSet.get(i)[0], angleSet.get(i)[1], 0d);
            
            System.out.print("gamma="+angleSet.get(i)[0]*180/Math.PI+" beta="+angleSet.get(i)[1]*180/Math.PI);
            System.out.println(" val1="+value+" val2="+value2);
        }
    }
    
    public static void testuH(){
        List<double[]> angleSet = createPositionSetAnglesUpperHemisphere();
        List<Coord3d> masudaNuH = createMasudaNuH();
        List<Coord3d> cardanNuH = createCardanNuH();
        List<Coord3d> positions = createPositionSetUpperHemisphere();
        for (int i=0;i < angleSet.size();i++){
            System.out.print("gamma="+angleSet.get(i)[0]*180/Math.PI+" beta="+angleSet.get(i)[1]*180/Math.PI);
            
            double value = conjunctRotAngle(cardanNuH.get(i), positions.get(i), masudaNuH.get(i));
            double value2 = conjunctRotAngle(angleSet.get(i)[0], angleSet.get(i)[1], 0d);
            
            // gamma [-180,180], damit ergibt sich referenceFieldBasedValue = [-270,90]
            /*double referenceFieldBasedValue = (angleSet.get(i)[0]*180/Math.PI-90d)*Math.cos(angleSet.get(i)[1]);
            if (referenceFieldBasedValue < -180){
                referenceFieldBasedValue = 360 + referenceFieldBasedValue;
            }*/
            System.out.println(" val1="+value+" val2="+value2);
        }
    }
    
    /**
     * Compare the generic method, based on the reference field with the method 
     * based on the direction cosine matrix only, to determine conjunct rotation angles.
     * 
     * @param excludePoles 
     */
    public static void compareConjunctRotationAngleCalcMethods(boolean excludePoles){
        System.out.println("Conjunct rotation comparison:");
        List<double[]> posAngles = createPositionSetAngles(excludePoles);
        List<Coord3d> r = createPositionSet(excludePoles);
        // Fehler damit nur in der oberen Hemisphere
        // Achtung: v = cardan ist nur korrekt solange createRadomFrames auch auf
        // Cardan basiert
        List<Coord3d> v = createCardanN(excludePoles); 
        List<RotationMatrix2> frames = createRandomFrames(excludePoles);
        
        boolean diffOnly = true;
        int errors = 0;
        for (int i=0;i< frames.size();i++){
            // based on dircos matrix
            double newValue = conjunctRotAngle(frames.get(i));
            // acos based
            Coord3d n = createConjunctN(
                    posAngles.get(i)[0], posAngles.get(i)[1]);
            double oldValue = conjunctRotAngle(v.get(i), r.get(i), n);
            
            String newValueString = RotationMatrix2.format(newValue);
            String oldValueString = RotationMatrix2.format(oldValue);
            if (!diffOnly || !newValueString.equals(oldValueString)){
                System.out.println(String.valueOf(errors++)+": field based: "+oldValueString+" matrix based: "+newValueString+
                    " (gamma="+RotationMatrix2.format(posAngles.get(i)[0]*180d/Math.PI)
                            +" beta="+RotationMatrix2.format(posAngles.get(i)[1]*180d/Math.PI)+") "+frames.get(i).toString());
            }
        }
    }
    
    public static void compareTwoStepRotationAngleCalcMethods(boolean excludePoles){
        System.out.println("Twostep rotation comparison:");
        List<double[]> posAngles = createPositionSetAngles(excludePoles);
        List<Coord3d> r = createPositionSet(excludePoles);
        // Achtung: v = cardan ist nur korrekt solange createRandomFrames auch auf
        // cardan basiert, das soll aber später geändert werden ...
        //FIXME
        List<Coord3d> v = createCardanN(excludePoles); 
        List<RotationMatrix2> frames = createRandomFrames(excludePoles);
        
        boolean diffOnly = true;
        int errors = 0;
        for (int i=0;i< frames.size();i++){
            // based on dircos matrix
            double dircosBasedValue = twoStepRotAngle(frames.get(i)); //conjunctRotAngle(frames.get(i)); 
            // acos/referenence field based - scheint korrekt!!!
            Coord3d n = createTwoStepN(
                    posAngles.get(i)[0], posAngles.get(i)[1]);
            double referenceFieldBasedValue = conjunctRotAngle(v.get(i), r.get(i), n);
            
            String newValueString = RotationMatrix2.format(dircosBasedValue);
            String oldValueString = RotationMatrix2.format(referenceFieldBasedValue);
            if (!diffOnly || !newValueString.equals(oldValueString)){
                System.out.println(String.valueOf(errors++)+": field based: "+oldValueString+" matrix based: "+newValueString+
                    " (gamma="+RotationMatrix2.format(posAngles.get(i)[0]*180d/Math.PI)
                            +" beta="+RotationMatrix2.format(posAngles.get(i)[1]*180d/Math.PI)+") "+frames.get(i).toString());
            }
        }
    }
    
    public static Coord3d cross(Coord3d v1, Coord3d v2){
	
	Coord3d v3 = new Coord3d();					  
        v3.x = v1.y * v2.z - v1.z * v2.y; // x1    x2     x3  <-
        v3.y = v1.z * v2.x - v1.x * v2.z; // y1 \/ y2     y3
        v3.z = v1.x * v2.y - v1.y * v2.x; // z1 /\ z2     z3

        return v3;
    }
}
