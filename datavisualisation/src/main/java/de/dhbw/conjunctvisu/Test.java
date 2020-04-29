package de.dhbw.conjunctvisu;

import java.util.ArrayList;
import java.util.List;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Chart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.maths.algorithms.interpolation.algorithms.BernsteinInterpolator;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * Kugel:
 * http://doc.jzy3d.org/javadoc/0.9.1/jzy3d-api/org/jzy3d/plot3d/primitives/Sphere.html#Sphere(org.jzy3d.maths.Coord3d,%20float,%20int,%20org.jzy3d.colors.Color)
 * Zylinder und Pyramiden:
 * http://doc.jzy3d.org/javadoc/0.9.1/jzy3d-api/org/jzy3d/plot3d/primitives/Tube.html
 */
public class Test {
  public static void main(String[] args) {

    List<Coord3d> controlCoords = new ArrayList<>();
    controlCoords.add(new Coord3d(0.0F, 0.0F, 0.0F));
    controlCoords.add(new Coord3d(1.0F, 0.0F, 1.0F));
    controlCoords.add(new Coord3d(1.0F, 0.0F, 2.0F));
    controlCoords.add(new Coord3d(1.0F, 1.0F, 2.0F));
    controlCoords.add(new Coord3d(0.0F, 1.0F, 2.0F));
    controlCoords.add(new Coord3d(3.0F, 2.0F, -1.0F));

    BernsteinInterpolator interp = new BernsteinInterpolator();
    List<Coord3d> interpolatedCoords = interp.interpolate(controlCoords, 30);

    List<Point> controlPoints = new ArrayList<>();
    controlCoords.forEach((coord) -> {
        controlPoints.add(new Point(coord, Color.RED, (float) 5.0));
      });

    List<Point> interpPoints = new ArrayList<>();
    interpolatedCoords.forEach((coord) -> {
        interpPoints.add(new Point(coord, Color.BLUE, (float) 3.0));
      });

    LineStrip line = new LineStrip(interpolatedCoords);
    line.setWireframeColor(Color.BLACK);

    Chart chart = new AWTChart(Quality.Intermediate);
    chart.addDrawable(line);//add(line);
    chart.addDrawable(controlPoints);
    chart.addDrawable(interpPoints);
    chart.open("chart test", 600, 600);
   
  }  
}
