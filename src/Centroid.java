

import java.util.*;

import org.omg.CosNaming.NamingContextPackage.CannotProceed;

/**
 *
 * @author umang_borad
 */

public class Centroid {
    private ArrayList<Double> points= new ArrayList<>();
    private ArrayList<Document> docs= new ArrayList<>();
    private ArrayList<Double> newpoints= new ArrayList<>();
   
    public Centroid(ArrayList<Double> set_points)
    {
        points.addAll(0,set_points);
        
        newpoints.addAll(0,set_points);
    }

    ArrayList<Double> getpoints() {
        return points;
    }
    
    void AssignDocumentToCentroid(Document d)
    {
        docs.add(d);
    }
    
    void clearlistOfDoc()
    {
        docs =new ArrayList<>();
    }
    
    void setPoints(ArrayList<Double> set_points)
    {
        points=new ArrayList<>();
        points.addAll(0,set_points);
    }
    
    ArrayList<Document> getdocs()
    {
        return docs;
    }

    void setnewPoints(ArrayList<Double> temp) {
        
        newpoints=new ArrayList<>();
        newpoints.addAll(0,temp);
    
    }
    
    boolean compareCentroidPoints() throws Exception
    {   
        int counter=0;
    if(points.size()==newpoints.size())
    {
       for(int z=0;z<points.size();z++)
        {
           if(points.get(z).compareTo(newpoints.get(z))==0)
            counter+=0; 
           else 
               counter++;
        }
    }
    else
    {
        throw new CannotProceed("Number of components do not match", null,null);
    }
       return counter==0; 
    }
    
    void replaceOldPointsWithNew()
    {
        points=newpoints;
        newpoints=new ArrayList<>();
    }

    HashMap<String , Integer> wordcoud(){
        HashMap<String , Integer> thewords = new HashMap<>();
        for (Document d : this.docs)
        {
            for (String s: d.tf.keySet()){
                if(thewords.containsKey(s)){
                    thewords.put(s,thewords.get(s)+d.tf.get(s));
                }
                else{
                    thewords.put(s,d.tf.get(s));
                }
            }
        }
        thewords = sortByValue(thewords);
        return thewords;
    }

    public static HashMap<String , Integer> sortByValue(HashMap<String , Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String , Integer>> list =
                new LinkedList<Map.Entry<String , Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String , Integer> >() {
            public int compare(Map.Entry<String , Integer> o1,
                               Map.Entry<String , Integer>o2)
            {
                return (o1.getValue()).compareTo(o2.getValue()) *-1;//*-1 makes the best come 1st
            }
        });

        // put data from sorted list to hashmap
        HashMap<String , Integer> temp = new LinkedHashMap<String , Integer>();
        for (Map.Entry<String , Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public double averageDistancefromCentroid(){
        double sum =0;
        for (Document d:this.docs){
            sum+= EdistanceFromCentroid(d.unitVector);
        }
        return sum/docs.size();
    }

    public static double distanceBetweenClusters(Centroid a , Centroid b){
        return a.EdistanceFromCentroid(b.points);
    }

    private double EdistanceFromCentroid( ArrayList<Double> unitVector) {
        double d;
        double distance=0;
        if(this.getpoints().size()==unitVector.size())
        {

            for(int component=0;component<this.getpoints().size();component++)
            {
                d=this.getpoints().get(component)-unitVector.get(component);
                distance+= d*d;
            }

        }
        else
        {
            throw new IllegalArgumentException("Number of Components do not match");
        }
        return Math.sqrt(distance);
    }
    
}
