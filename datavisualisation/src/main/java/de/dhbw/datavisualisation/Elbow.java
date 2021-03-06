package de.dhbw.datavisualisation;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
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
import org.jzy3d.plot3d.rendering.lights.Light;

public class Elbow extends AbstractAnalysis {
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new Elbow());
    }

    @Override
    public void init() throws FileNotFoundException, IOException, CsvValidationException {
        List<List<String>> records = new ArrayList<List<String>>();
try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\muellersm\\Documents\\GitHub\\datavisualisation\\datavisualisation\\csv\\elbow_auswertung.csv"));) {
    String[] values = null;
    while ((values = csvReader.readNext()) != null) {
        records.add(Arrays.asList(values));
    }
}
//System.out.print(records.get(0).get(0));

chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
//Light light = chart.addLight(new Coord3d(500f, -700f, 2000f));
//light.setRepresentationRadius(100);

//define colors
Color darkred = new Color(169,0,0);
Color green = new Color(34,139,34);


List<Float> posz = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(0)))
    {posz.add(Float.parseFloat(records.get(i+1).get(0)));}
    else {posz.add(0f);}
}

List<Float> posy = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(1)))
    {posy.add(Float.parseFloat(records.get(i+1).get(1)));}
    else {posy.add(0f);}
}

List<Float> posx = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(2)))
    {posx.add(Float.parseFloat(records.get(i+1).get(2)));}
    else {posx.add(0f);}
}

List<Float> shiftx = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(7)))
    {shiftx.add(Float.parseFloat(records.get(i+1).get(7)));}
    else {shiftx.add(0f);}
}
List<Float> shifty = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(8)))
    {shifty.add(Float.parseFloat(records.get(i+1).get(8)));}
    else {shifty.add(0f);}
}

List<Float> elbowflex = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(8)))
    {elbowflex.add(Float.parseFloat(records.get(i+1).get(8)));}
    else {elbowflex.add(0f);}
    
}
for (int i= 0; i<posx.size(); i++) {
    Arrow arrowZ = new Arrow();
    //System.out.println(records.get(i)); 
    //System.out.println(posx.get(i));
    //System.out.println(posy.get(i));
    //System.out.println(posz.get(i));
    //System.out.println(shiftx.get(i));
    //System.out.println(shifty.get(i));
    if (elbowflex.get(i) < 20) {
    arrowZ.setData(new Vector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i))),
                                        10f,10,0, Color.RED);
    chart.getScene().getGraph().add(arrowZ); 
    }
    
    else if ((elbowflex.get(i) < 55) & (elbowflex.get(i)) > 20) {
    arrowZ.setData(new Vector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i))),
                                        10f,10,0, green);
    }
    
    else if ((elbowflex.get(i) < 75) & (elbowflex.get(i)) > 55) {
    arrowZ.setData(new Vector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i))),
                                        10f,10,0, Color.YELLOW);
    }
    else
    {
    arrowZ.setData(new Vector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i))),
                                        10f,10,0,darkred);        
            }
    chart.getScene().getGraph().add(arrowZ); 
    }   
            List<List<String>> bigbox = new ArrayList<List<String>>();
try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\muellersm\\Documents\\GitHub\\datavisualisation\\datavisualisation\\csv\\correctedListBig.csv"));) {
    String[] valuesbigbox = null;
    while ((valuesbigbox = csvReader.readNext()) != null) {
        bigbox.add(Arrays.asList(valuesbigbox));
    }
}
    
List<Float> boxx = new ArrayList<Float>();
for (int i= 0; i<bigbox.size();i++){
    if (!"".equals(bigbox.get(i).get(0)))
    {boxx.add(Float.parseFloat(bigbox.get(i).get(0)));}
    else {boxx.add(0f);}
}   

List<Float> boxy = new ArrayList<Float>();
for (int i= 0; i<bigbox.size();i++){
    if (!"".equals(bigbox.get(i).get(1)))
    {boxy.add(Float.parseFloat(bigbox.get(i).get(1)));}
    else {boxy.add(0f);}
} 
 
List<Float> boxz = new ArrayList<Float>();
for (int i= 0; i<bigbox.size();i++){
    if (!"".equals(bigbox.get(i).get(2)))
    {boxz.add(Float.parseFloat(bigbox.get(i).get(2)));}
    else {boxz.add(0f);}
}     

for (int i= 0; i<bigbox.size(); i++) {
    Cube cubebig = new Cube();
    cubebig.drawCube(20,20,20,boxx.get(i),boxy.get(i),boxz.get(i));
    chart.getScene().getGraph().add(cubebig); 
    }
}
}
