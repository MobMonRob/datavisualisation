package de.dhbw.datavisualisation;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import org.jzy3d.plot3d.primitives.Sphere;

/**
 * pfad visualisierung 
 * 
 * @author rettig
 */
public class path_visualisation extends AbstractAnalysis {
    
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new path_visualisation());
    }

    @Override
    public void init() throws FileNotFoundException, IOException  {
        
////////////////////////////////////////////
//DATA AQ
        
         Gson gson = new GsonBuilder().setPrettyPrinting().create();

        
         Reader reader = new FileReader("C:\\Users\\muellersm\\Documents\\GitHub\\datavisualisation\\datavisualisation\\etc\\json\\kirk_default.json"); 

            // Convert JSON File to Java Object
            Read_JSON_from_RPS.RPS rps = gson.fromJson(reader, Read_JSON_from_RPS.RPS.class);
            
            
            
            // print  
            //System.out.println(rps);
            //System.out.println(reader);


        
    
            //System.out.println("test");
           
            chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());

            Color darkred = new Color(169,0,0,100);
            Color green = new Color(34,139,34);

            
            List<Float> rps_x = new ArrayList<Float>();   
            List<Float> rps_y = new ArrayList<Float>(); 
            List<Float> rps_z = new ArrayList<Float>(); 
            
            //for (int j = 0; j<rps.pointsPerTemplates.size();j++)
            { int j =6;
                for (int i = 0;i<rps.pointsPerTemplates.get(j).getPoints().size();i++){
                    rps_x.add(rps.pointsPerTemplates.get(j).getPoints().get(i).getFlange().getX());
                    rps_y.add(rps.pointsPerTemplates.get(j).getPoints().get(i).getFlange().getY());
                    rps_z.add(rps.pointsPerTemplates.get(j).getPoints().get(i).getFlange().getZ());           
                }
            }
            
///////////////////////////////////////////////////////
//DRAWING
               double radius;
            for (int i= 0; i<rps_x.size(); i++) {
                radius =1;
              
                Sphere sphere = new Sphere(new Coord3d(rps_x.get(i),rps_y.get(i),rps_z.get(i)),(float)radius/10,15,darkred);
                
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


