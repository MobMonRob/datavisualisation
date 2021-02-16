package de.dhbw.datavisualisation;


import au.com.bytecode.opencsv.CSVReader;
import de.dhbw.conjunctvisu.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.jzy3d.maths.Vector3d;
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Cube;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import org.jzy3d.plot3d.primitives.Sphere;

/**
 * Mittelwert |x,y,z| Abweichungen als Kugelradius
 * 
 * @author rettig
 */
public class Mean_KIRK_payload_c2 extends AbstractAnalysis {
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new Mean_KIRK_payload_c2());
    }

    @Override
    public void init() throws FileNotFoundException, IOException  {
        List<List<String>> records = new ArrayList<List<String>>();
        
        //try {
            CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\muellersm\\Desktop\\Neuer Ordner\\c2.csv"), ';' ,'"'); 
            //CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\rettig\\Documents\\NetBeansProjects\\datavisualisation\\datavisualisation\\csv\\mean.csv"), '\t' ,'"'); 
            
            String[] values = null;

            while ((values = csvReader.readNext()) != null) {
                records.add(Arrays.asList(values));
            }
            //System.out.print(records.get(0).get(0));

            chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
            //chart.open("f2", 900, 900);

            Color darkred = new Color(169,0,0);
            Color green = new Color(34,139,34);

            System.out.println(records);
            List<Float> rob_meanx = new ArrayList<>();
            for (int i= 1; i<records.size();i++){
                if (!records.get(i).get(84).strip().isEmpty()) {
                    try {
                        rob_meanx.add(Float.parseFloat(records.get(i).get(84)));
                    } catch (NumberFormatException e){
                        System.out.println("\""+records.get(i).get(84)+"\"");
                    }
                }

            }

            List<Float> rob_meany = new ArrayList<Float>();
            for (int i= 1; i<records.size();i++){
                if (!records.get(i).get(91).strip().isEmpty())
                {rob_meany.add(Float.parseFloat(records.get(i).get(91)));}

            }

            List<Float> rob_meanz = new ArrayList<Float>();
            for (int i= 1; i<records.size();i++){
                if (!records.get(i).get(85).strip().isEmpty())
                {rob_meanz.add(Float.parseFloat(records.get(i).get(85)));}

            }

            List<Float> vicon_meanx = new ArrayList<Float>();
            for (int i= 1; i<records.size();i++){
                if (!records.get(i).get(130).strip().isEmpty())
                {vicon_meanx.add(Float.parseFloat(records.get(i).get(130)));}

            }

            List<Float> vicon_meany = new ArrayList<Float>();
            for (int i= 1; i<records.size();i++){
                if (!records.get(i).get(125).strip().isEmpty())
                {vicon_meany.add(Float.parseFloat(records.get(i).get(125)));}
                else {vicon_meany.add(0f);}
            }

            List<Float> vicon_meanz = new ArrayList<Float>();
            for (int i= 1; i<records.size();i++){
                if (!records.get(i).get(124).strip().isEmpty())
                {vicon_meanz.add(Float.parseFloat(records.get(i).get(124)));}

            }

               double radius;
            for (int i= 0; i<vicon_meanx.size()-5; i++) {
                radius = sqrt(((rob_meanx.get(i)-vicon_meanx.get(i))*(rob_meanx.get(i)-vicon_meanx.get(i)))
                            +((rob_meany.get(i)-vicon_meany.get(i))*(rob_meany.get(i)-vicon_meany.get(i)))
                            +((rob_meanz.get(i)-vicon_meanz.get(i))*(rob_meanz.get(i)-vicon_meanz.get(i))));
                if (radius < 1000){
                Sphere sphere = new Sphere(new Coord3d(rob_meanx.get(i),rob_meany.get(i),rob_meanz.get(i)),(float)radius/25,15,darkred);
                
                sphere.setWireframeColor(darkred);
                
                chart.getScene().getGraph().add(sphere); 
                
                }
            }
            
            //for (int i= 0; i<rob_meanz.size(); i++) {
              //  Sphere sphere = new Sphere(new Coord3d(vicon_meanx.get(i),vicon_meany.get(i),vicon_meanz.get(i)),2f,15,green);
               // sphere.setWireframeColor(green);
                //chart.getScene().getGraph().add(sphere); 
            //}
        /*} catch (Exception e){
        
        }*/
    }
}

