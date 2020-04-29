package org.jzy3d.maths;

/**
 *
 * @author rettig
 */
public class Utils2 {
    public static Coord3d cross(Coord3d v1, Coord3d v2){
	
	Coord3d v3 = new Coord3d();					  
        v3.x = v1.y * v2.z - v1.z * v2.y; // x1    x2     x3  <-
        v3.y = v1.z * v2.x - v1.x * v2.z; // y1 \/ y2     y3
        v3.z = v1.x * v2.y - v1.y * v2.x; // z1 /\ z2     z3

        return v3;
    }

}
