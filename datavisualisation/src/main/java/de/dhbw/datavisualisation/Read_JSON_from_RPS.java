package de.dhbw.datavisualisation;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import com.google.gson.*;
import de.dhbw.datavisualisation.Read_JSON_from_RPS.RPS.PointsPerTemplates.Points;
import java.io.Reader;
import lombok.Getter;
import lombok.Setter;


public class Read_JSON_from_RPS /*extends AbstractAnalysis*/ {
    

    @Getter
    @Setter
    public class RPS {
        List <PointsPerTemplates> pointsPerTemplates;
        
        @Override
        public String toString(){
            StringBuilder sb = new StringBuilder();
            
            for (Points points: pointsPerTemplates.get(0).getPoints()){
                sb.append(points.toString());
                sb.append("\n");
            }
            return sb.toString();
        }
        
        @Getter
        @Setter
        public class PointsPerTemplates {
            List<Points> points;
            
            
            @Getter
            @Setter
            public class Points {
                double controllerNodeID;        
                Flange flange;
                
                @Getter
                @Setter
                public class Flange {
                    String name;
                    String reference;
                    float rx;
                    float ry;
                    float rz;
                    float x;
                    float y;
                    float z;
                    

                String graspAction;
                State state;    
                
                @Getter
                @Setter
                public class State {
                    List<Joints> joints;
                        
                    @Getter
                    @Setter
                    public class Joints {
                        String name;
                        String unit;
                        float value;
                        }
                    }
   
                TCP tcp;
                
                @Getter
                @Setter
                public class TCP {
                    String name;
                    String reference;
                    float rx;
                    float ry;
                    float rz;
                    float x;
                    float y;
                    float z;
                    }
                
                float time;
                
                @Override
                public String toString(){
                    StringBuilder sb = new StringBuilder();
            
                    sb.append(name);
                    sb.append(" (");
                    sb.append(rx);
                    sb.append("°, ");
                    sb.append(ry);
                    sb.append("°, ");
                    sb.append(rz);
                    sb.append("°, ");
                    sb.append(x);
                    sb.append(", ");
                    sb.append(y);
                    sb.append(", ");
                    sb.append(z);
                    sb.append(")");
                    sb.append("\n");
                    sb.append("time: ");
                    sb.append(time);
                            
                    return sb.toString();
                    }
                }
        
            @Override
                    public String toString(){
                        StringBuilder sb = new StringBuilder();
                        
                        sb.append(flange);
                        sb.append("\n");
                            
                        return sb.toString();
                    }
            }
        
    }
    }
    
    
    
    public static void main(String[] args) throws Exception {
        //AnalysisLauncher.open(new Read_JSON_from_RPS());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        
          try (Reader reader = new FileReader("C:\\Users\\muellersm\\Documents\\GitHub\\datavisualisation\\datavisualisation\\etc\\json\\test.json")) {

            // Convert JSON File to Java Object
            RPS rps = gson.fromJson(reader, RPS.class);
            
            float rx = rps.getPointsPerTemplates().get(0).getPoints().get(0).getFlange().getRx();
            
            // print  
            System.out.println(rps);
            System.out.println(reader);
            System.out.println(rx);

        } catch (IOException e) {
            e.printStackTrace();
        }
    
        System.out.println("test");
        //System.out.println(rps);
}

    

    
   // @Override
    public void init() throws FileNotFoundException, IOException  {
    
// create Gson instance

    

    }
  
    }

