package de.dhbw.conjunctvisu;

import org.jzy3d.plot3d.primitives.Cone;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.primitives.AbstractComposite;
import org.jzy3d.plot3d.primitives.Cylinder;
import org.jzy3d.plot3d.transform.Rotate;
import org.jzy3d.plot3d.transform.Transform;

public class ArrowOld extends AbstractComposite {	
	public void setData(Vector3d vec, float radius, int slices, int rings, Color color){
            Coord3d position = vec.getCenter();
            float height = vec.norm();  
            System.out.println("height = "+height+ " x="+position.x+" y="+position.y+" z="+position.z);
            float coneHeight = radius*2.5f;
            float cylinderHeight = height-coneHeight;

            cylinder = new Cylinder();
            cylinder.setData(new Coord3d(position.x, position.y, position.z-height/2f),
                                         cylinderHeight, radius, slices, rings, color);
            cone = new Cone();
            cone.setData(new Coord3d(position.x, position.y, position.z+height/2d-coneHeight), 
                                     coneHeight, radius*1.6f, slices, rings, color);
            add(cylinder);
            add(cone);
            
            Coord3d to = vec.vector();
            Coord3d from = new Coord3d(0d,0d,1d);
            Vector3d test;
            double toMag =  (float) Math.sqrt(to.x * to.x + to.y * to.y + to.z * to.z);
            double angle = Math.acos(from.dot(to)/toMag)*180f/Math.PI;
            Coord3d rotAxis = Utils.cross(from,to);
            Transform trans = new Transform(); 
            trans.add(new Rotate(angle, rotAxis));
            //this.setTransformBefore(trans);
            //applyGeometryTransform(trans);
	}
        //@deprecated
        public void setData(Coord3d position, float height, float radius, int slices, int rings, Color color){
		float coneHeight = radius*2.5f;
                float cylinderHeight = height-coneHeight;
                
		cylinder = new Cylinder();
                cylinder.setData(new Coord3d(position.x, position.y, position.z-height/2f),
                                             cylinderHeight, radius, slices, rings, color);
                cone = new Cone();
                cone.setData(new Coord3d(position.x, position.y, position.z+height/2d-coneHeight), 
                                         coneHeight, radius*1.6f, slices, rings, color);
                add(cylinder);
                add(cone);
	}
	protected Cylinder cylinder;
        protected Cone cone;
}
