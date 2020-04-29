package de.dhbw.conjunctvisu;

import org.jzy3d.plot3d.primitives.Arrow;
import java.util.List;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.colors.ColorMapper;
import org.jzy3d.colors.colormaps.ColorMapRainbow;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.Range;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import org.jzy3d.plot3d.primitives.Sphere;

public class VectorfieldOnSphere1 extends AbstractAnalysis {
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new VectorfieldOnSphere1());
    }

    @Override
    public void init() {
        // Define a function to plot
        Mapper mapper = new Mapper() {
            @Override
            public double f(double x, double y) {
                //return x * Math.sin(x * y);
                return Math.sqrt(1- x * x - y * y);
            }
        };

        // Define range and precision for the function to plot
        Range range = new Range(-1, 1);
        int steps = 80;

        // Create the object to represent the function over the given range.
        final Shape surface = Builder.buildOrthonormal(new OrthonormalGrid(range, steps, range, steps), mapper);
        surface.setColorMapper(new ColorMapper(new ColorMapRainbow(), surface.getBounds().getZmin(), 
                surface.getBounds().getZmax(), new Color(1, 1, 1, .5f)));
        surface.setFaceDisplayed(true);
        surface.setWireframeDisplayed(false);

        
        
        Coord3d midpoint = new Coord3d(0.0F, 0.0F, 0.0F);
        
        // Coord3d position, float radius, int slicing, Color color)
        Sphere sphere = new Sphere(midpoint, 1f, 12, Color.BLUE);
        
        //TODO
        // Koordinatenachsen und Raster ausschalten bzw. besser formattieren
        // durchscheinende Oberflächen, Beleuchtung etc.
        // Druck in pdf
        
        chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        chart.getScene().getGraph().add(sphere);
        
        float radius = 0.02f;//0.03f
        
        Arrow arrowX = new Arrow();
        arrowX.setData(Utils.createVector3d(new Coord3d(0.5f,0,0), new Coord3d(1f,0f,0f), 1f),radius,10,0, Color.YELLOW);
        chart.getScene().getGraph().add(arrowX);
        Arrow arrowY = new Arrow();
        arrowY.setData(Utils.createVector3d(new Coord3d(0f,0.5,0), new Coord3d(0f,1f,0f), 1f),radius,10,0, Color.GREEN);
        chart.getScene().getGraph().add(arrowY);
        Arrow arrowZ = new Arrow();
        arrowZ.setData(Utils.createVector3d(new Coord3d(0f,0f,0.5f), new Coord3d(0f,0f,1f), 1f),radius,10,0, Color.CYAN);
        chart.getScene().getGraph().add(arrowZ);
          
        List<Coord3d> posList = Utils.createPositionSetLowerHemisphere();
        
        
        // conjunct
        
        // untere Hemisphere
        /*List<Coord3d> nList = Utils.createMasudaNlH(); //Utils.createUpperConjunctN(); //Utils.createMasudaN(); //createCardanN();
        for (int i=0;i<nList.size();i++){
            Arrow arrowN = new Arrow();
            arrowN.setData(Utils.createVector3d(posList.get(i), nList.get(i), 0.2f),radius,10,0, Color.RED);
            chart.getScene().getGraph().add(arrowN);
        }
        
        // obere Hemisphere
        nList = Utils.createMasudaNuH(); //Utils.createUpperConjunctN(); //Utils.createMasudaN(); //createCardanN();
        posList = Utils.createPositionSetUpperHemisphere();
        for (int i=0;i<nList.size();i++){
            Arrow arrowN = new Arrow();
            arrowN.setData(Utils.createVector3d(posList.get(i), nList.get(i), 0.2f),radius,10,0, Color.GREEN);
            chart.getScene().getGraph().add(arrowN);
        }*/
        
        
        // conjunct obere Hemisphere ursprüngliche Methode
        posList = Utils.createPositionSetUpperHemisphere();
        List<Coord3d> nList = Utils.createUpperConjunctNOld();
        for (int i=0;i<nList.size();i++){
            Arrow arrowN = new Arrow();
            arrowN.setData(Utils.createVector3d(posList.get(i), nList.get(i), 0.2f),
                           radius,10,0, Color.RED);
            chart.getScene().getGraph().add(arrowN);
        }
        
        // conjunct untere Hemisphere ursprüngliche Methode
        posList = Utils.createPositionSetLowerHemisphere();
        nList = Utils.createLowerConjunctNOld();
        for (int i=0;i<nList.size();i++){
            Arrow arrowN = new Arrow();
            arrowN.setData(Utils.createVector3d(posList.get(i), nList.get(i), 0.2f),
                           radius,10,0, Color.GREEN);
            chart.getScene().getGraph().add(arrowN);
        }
        
        boolean excludePoles = true;
        nList = Utils.createConjunctN(excludePoles); // Utils.createTwoStepN(excludePoles); //Utils.createUpperConjunctN(); //Utils.createMasudaN(); //createCardanN();
        posList = Utils.createPositionSet(excludePoles);
        for (int i=0;i<nList.size();i++){
            Arrow arrowN = new Arrow();
            arrowN.setData(Utils.createVector3d(posList.get(i), nList.get(i), 0.2f),radius,10,0, Color.YELLOW);
            chart.getScene().getGraph().add(arrowN);
        }
        
        Utils.compareConjunctRotationAngleCalcMethods(false);
        Utils.compareTwoStepRotationAngleCalcMethods(false);
    }
}
