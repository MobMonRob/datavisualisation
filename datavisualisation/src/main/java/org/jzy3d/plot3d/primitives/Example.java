package org.jzy3d.plot3d.primitives;

import java.util.ArrayList;
import java.util.List;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * @author Dr. Oliver Rettig, DHBW-Karlsruhe, Germany, 2019
 */
public class Example extends AbstractAnalysis {
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new Example());
    }

    @Override
    public void init() {
        Sphere sphere = new Sphere(new Coord3d(0.0F, 0.0F, 0.0F), 1f, 12, Color.BLUE);
        
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        chart.getScene().getGraph().add(sphere);
        
        List<Coord3d> nList = createCardanN();
        List<Coord3d> posList = createPositionSet();
        for (int i=0;i<nList.size();i++){
            Arrow arrowN = new Arrow();
            arrowN.setData(createVector3d(posList.get(i), nList.get(i), 0.2f),0.02f,10,0, Color.RED);
            chart.getScene().getGraph().add(arrowN);
        }
    }
    
    public static List<Coord3d> createCardanN(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAngles().forEach((angles) -> {
            double x = Math.cos(angles[0]);
            double y = 0d;
            double z = -Math.sin(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
    private static Vector3d createVector3d(Coord3d pos, Coord3d dir, float length){
        Coord3d dirN = dir.getNormalizedTo(length/2f);
        Coord3d end = pos.add(dirN);
        dirN = dirN.negative();
        Coord3d start = pos.add(dirN);
        return new Vector3d(start, end);
    }
    private static final double longitudes = 12; 
    private static final double latitudes = 13;
    
    private static List<double[]> createPositionSetAngles(){
        List<double[]> result = new ArrayList<>();
        // Längenkreise: theta = [0,360]
        for (int i=0;i<longitudes;i++){
            double theta = ((double) i)*30d/180d * Math.PI;
            //System.out.println("i="+i+" theta="+theta);
            // Breitenkreise: beta = [-180;0]
            for (int j=0;j<latitudes;j++){
            //int j = 6; //d.h. beta = -90 Grad
                double beta = - ((double) j) * 15d/180d * Math.PI;
                result.add(new double[]{theta, beta});
            }
        }
        return result;
    }
    private static List<Coord3d> createPositionSet(){
        List<Coord3d> result = new ArrayList<>();
        createPositionSetAngles().forEach((angles) -> {
            double x = Math.sin(angles[0])*Math.sin(angles[1]); 
            double y = -Math.cos(angles[1]);
            double z = Math.sin(angles[1]) * Math.cos(angles[0]);
            result.add(new Coord3d(x,y,z));
        });
        return result;
    }
}
