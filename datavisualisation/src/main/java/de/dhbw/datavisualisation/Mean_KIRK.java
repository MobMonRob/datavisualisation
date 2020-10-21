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

public class Mean_KIRK extends AbstractAnalysis {
    
    public static void main(String[] args) throws Exception {
        AnalysisLauncher.open(new Mean_KIRK());
    }

    @Override
    public void init() throws FileNotFoundException, IOException, CsvValidationException {
        List<List<String>> records = new ArrayList<List<String>>();
try (CSVReader csvReader = new CSVReader(new FileReader("C:\\Users\\muellersm\\Documents\\GitHub\\datavisualisation\\datavisualisation\\csv\\mean.csv"));) {
    String[] values = null;
    while ((values = csvReader.readNext()) != null) {
        records.add(Arrays.asList(values));
    }
}
//System.out.print(records.get(0).get(0));

chart = AWTChartComponentFactory.chart(Quality.Advanced, getCanvasType());

Color darkred = new Color(169,0,0);
Color green = new Color(34,139,34);

List<Float> rob_meanx = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(45)))
    {rob_meanx.add(Float.parseFloat(records.get(i+1).get(45)));}
    else {rob_meanx.add(0f);}
}

List<Float> rob_meany = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(49)))
    {rob_meany.add(Float.parseFloat(records.get(i+1).get(49)));}
    else {rob_meany.add(0f);}
}

List<Float> rob_meanz = new ArrayList<Float>();
for (int i= 0; i<records.size()-1;i++){
    if (!"".equals(records.get(i+1).get(47)))
    {rob_meanz.add(Float.parseFloat(records.get(i+1).get(47)));}
    else {rob_meanz.add(0f);}
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
    cubebig.drawCube(5,5,5,boxx.get(i),boxy.get(i),boxz.get(i));
    chart.getScene().getGraph().add(cubebig); 
    }
}
}
