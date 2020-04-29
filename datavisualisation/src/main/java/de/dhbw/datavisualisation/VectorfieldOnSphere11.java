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
import org.jzy3d.plot3d.builder.Builder;
import org.jzy3d.plot3d.builder.Mapper;
import org.jzy3d.plot3d.builder.concrete.OrthonormalGrid;
import org.jzy3d.plot3d.primitives.Shape;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import org.jzy3d.plot3d.primitives.Sphere;

public class VectorfieldOnSphere11 extends AbstractAnalysis {
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new VectorfieldOnSphere11());
    }

    @Override
    public void init() throws FileNotFoundException, IOException, CsvValidationException {
        List<List<String>> records = new ArrayList<List<String>>();
try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\muellersm\\Desktop\\Auswertung.csv"));) {
    String[] values = null;
    while ((values = csvReader.readNext()) != null) {
        records.add(Arrays.asList(values));
    }
}
//System.out.print(records.get(0).get(0));

chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());
        
List<Float> posx = new ArrayList<Float>();
for (int i= 0; i<records.size();i++){
    if (!"".equals(records.get(i).get(0)))
    {posx.add(Float.parseFloat(records.get(i).get(0)));}
    else {posx.add(0f);}
}

List<Float> posy = new ArrayList<Float>();
for (int i= 0; i<records.size();i++){
    if (!"".equals(records.get(i).get(1)))
    {posy.add(Float.parseFloat(records.get(i).get(1)));}
    else {posy.add(0f);}
}

List<Float> posz = new ArrayList<Float>();
for (int i= 0; i<records.size();i++){
    if (!"".equals(records.get(i).get(2)))
    {posz.add(Float.parseFloat(records.get(i).get(2)));}
    else {posz.add(0f);}
}

List<Float> shiftx = new ArrayList<Float>();
for (int i= 0; i<records.size();i++){
    if (!"".equals(records.get(i).get(7)))
    {shiftx.add(Float.parseFloat(records.get(i).get(7)));}
    else {shiftx.add(0f);}
}
List<Float> shifty = new ArrayList<Float>();
for (int i= 0; i<records.size();i++){
    if (!"".equals(records.get(i).get(8)))
    {shifty.add(Float.parseFloat(records.get(i).get(8)));}
    else {shifty.add(0f);}
}
for (int i= 0; i<posx.size(); i++) {
    Arrow arrowZ = new Arrow();
    //System.out.println(records.get(i)); 
    //System.out.println(posx.get(i));
    //System.out.println(posy.get(i));
    //System.out.println(posz.get(i));
    //System.out.println(shiftx.get(i));
    //System.out.println(shifty.get(i));
    if (sqrt(shiftx.get(i)+shiftx.get(i)+shifty.get(i)*shifty.get(i)) < 25) {
    arrowZ.setData(Utils.createVector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i)),
                                        50f),5f,10,0, Color.CYAN);
    chart.getScene().getGraph().add(arrowZ); 
    }
    
    else if ((sqrt(shiftx.get(i)+shiftx.get(i)+shifty.get(i)*shifty.get(i)) < 50) & (sqrt(shiftx.get(i)+shiftx.get(i)+shifty.get(i)*shifty.get(i))) > 25) {
    arrowZ.setData(Utils.createVector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i)),
                                        50f),5f,10,0, Color.GREEN);
    }
    
    else if ((sqrt(shiftx.get(i)+shiftx.get(i)+shifty.get(i)*shifty.get(i)) < 75) & (sqrt(shiftx.get(i)+shiftx.get(i)+shifty.get(i)*shifty.get(i))) > 50) {
    arrowZ.setData(Utils.createVector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i)),
                                        50f),5f,10,0, Color.YELLOW);
    }
    else
    {
    arrowZ.setData(Utils.createVector3d(new Coord3d(posx.get(i),
                                                    posy.get(i),
                                                    posz.get(i)), 
                                        new Coord3d(posx.get(i)+shiftx.get(i),
                                                    posy.get(i)+shifty.get(i),
                                                    posz.get(i)),
                                        50f),5f,10,0, Color.RED);        
            }
    chart.getScene().getGraph().add(arrowZ); 
    }   
    }
}

