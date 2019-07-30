import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class KMeanski {
    public ArrayList<Document> pool_of_Documents = new ArrayList<>();
    private ArrayList<Centroid> centroids=new ArrayList<>();
    MainClass n =MainClass.getInstance();
    private final ArrayList<String> total_words = n.bagofwords;

    public KMeanski(ArrayList<Document> f) {
        pool_of_Documents = f;
    }

    public void createCluster(int K) throws IOException, Exception {
       n.print("Creating Clusters");
             start_K_means(pool_of_Documents,K);
    }

    private void start_K_means(ArrayList<Document> pool_of_Documents, int cardinality2) throws Exception {
        n.print("Starting K-Means");
        //Place random cluster centroids
        centroids=calculate_random_initial_centroids(pool_of_Documents,cardinality2);



        for(int iteration=0;iteration<cardinality2*3;iteration++)
        {
            n.print("Iteration Number "+ iteration );
            for (int i = 0; i < centroids.size(); i++)
            {
                //Clearing the list of previously assigned documents , if any.
                centroids.get(i).clearlistOfDoc();
                centroids.get(i).replaceOldPointsWithNew();
            }

            //Assign each object to the closet centroid
            for(int pod=0;pod<pool_of_Documents.size();pod++)
            {
                double distance[]=new double[centroids.size()];
                for(int cluster=0;cluster<centroids.size();cluster++)
                {
                    distance[cluster]=Edistance(centroids.get(cluster),pool_of_Documents.get(pod).getUnitVector());
                }
                int index= closest_centroid(distance);
                centroids.get(index).AssignDocumentToCentroid(pool_of_Documents.get(pod));
                for (double d:distance)   {
                    n.distance.add(d);
                }
            }

            //recompute the centroids
            for(int cluster=0;cluster<centroids.size();cluster++)    //for every centroid
            {   if(!centroids.get(cluster).getdocs().isEmpty())
            {
                ArrayList<Double> temp= new ArrayList<>();
                for(int t=0;t<centroids.get(cluster).getpoints().size();t++) //for every component of centroid
                {
                    double a=0;
                    for(int b=0;b<centroids.get(cluster).getdocs().size();b++)   //for every document in the cetroid
                    {
                        a+=centroids.get(cluster).getdocs().get(b).getUnitVector().get(t);
                    }
                    a/=centroids.get(cluster).getpoints().size();
                    temp.add(t,a);
                }
                //set new centroid
                centroids.get(cluster).setnewPoints(temp);
            }
            }
            if(nochange()) {
                break;
            }
        }//while(!nochange());

        for(int clusters=0;clusters<centroids.size();clusters++)
        {
            if(centroids.get(clusters).getdocs().size()>0)
            {
                n.print("==========================================================");
                n.print("Cluster  "+clusters);
                n.print("Number of Doucuments: "+centroids.get(clusters).getdocs().size());

                for(int doc=0;doc<centroids.get(clusters).getdocs().size();doc++)
                {
                    n.print(centroids.get(clusters).getdocs().get(doc).title);
                }

                n.print("==========================================================");
                n.print("Centroid of the cluster "+ clusters);
                n.print(centroids.get(clusters).getpoints().toString());
                n.print("==========================================================");
                n.print("Top terms of the Cluster ");
                HashMap<String ,Integer> terms = centroids.get(clusters).wordcoud();
               int i = 1;
               for(String s: terms.keySet()){
                   if(i>30)
                       break;
                   n.print(i+ ": "+s + ": " + terms.get(s));
                   i++;
               }
                n.print("==========================================================");
                n.print("Average Distance From the Centroid is " + centroids.get(clusters).averageDistancefromCentroid());
                n.print("==========================================================");
            }
        }
        n.print("The Distance between the Clusters");
        for(int clusters=0;clusters<centroids.size();clusters++)
            for(int b=clusters+1;b<centroids.size();b++){
                n.print("The distance between Cluster"+ clusters +"from cluster "+ b +"is: "
                        +Centroid.distanceBetweenClusters(centroids.get(clusters), centroids.get(b)));
            }

    }

    //Kmeans++
    private ArrayList<Centroid> calculate_random_initial_centroids(ArrayList<Document> pool_of_Documents,int cardinality1) throws IOException {
        //Implementing Kmeans++ for initial seeding
        n.print("Calculate_random_initial_centroids");
        ArrayList<Centroid> initial_centroids= new ArrayList<>();
        double[] distance = new double[pool_of_Documents.size()];
        //step1:Choose one center uniformly at random from among the data points
        initial_centroids.add(0, random_data_point());
        //step4:Repeat Steps 2 and 3 until k centers have been chosen
        for(int k=1;k<cardinality1;k++){
            System.out.println(k);
            //step2:For each data point x, compute D(x), the distance between x and the nearest center that has already been chosen
            for(int datapoints=0;datapoints<pool_of_Documents.size();datapoints++)
            {
                distance[datapoints]=Edistance(initial_centroids.get(k-1),pool_of_Documents.get(datapoints).getUnitVector());
            }
            //step3:Choose one new data point at random as a new center, using a weighted probability distribution where a point x is chosen with probability proportional to D(x)^2.
            //storing probabilities in place of distance
            double sum=0;
            for(int datapoints=0;datapoints<distance.length;datapoints++)
                sum+=(distance[datapoints]*distance[datapoints]);
            for(int datapoints=0;datapoints<distance.length;datapoints++)
                distance[datapoints]=(distance[datapoints]*distance[datapoints])/sum;
            //choosing new datapoint
            Centroid cq= probable_datapoints(distance);
            int count = 0;
            for(int s=0;s<initial_centroids.size();s++)
                if(initial_centroids.get(s).equals(cq))
                    count++;
            if(count==0)
                initial_centroids.add(k, cq);
            else
                k--;

        }
        return initial_centroids;
    }

    private Centroid random_data_point() {
        Random randomGenerator= new Random();
        int index=randomGenerator.nextInt(pool_of_Documents.size());
        return new Centroid(new ArrayList<>(pool_of_Documents.get(index).getUnitVector()));
    }

    private double Edistance(Centroid get, ArrayList<Double> unitVector) {
        double d;
        double distance=0;
        if(get.getpoints().size()==unitVector.size())
        {

            for(int component=0;component<get.getpoints().size();component++)
            {
                d=get.getpoints().get(component)-unitVector.get(component);
                distance+= d*d;
            }

        }
        else
        {
            throw new IllegalArgumentException("Number of Components do not match " +unitVector.size());
        }
        return Math.sqrt(distance);
    }

    private int closest_centroid(double[] distance) {
        int index=0;

        for (int i = 1; i < distance.length; i++)
        {
            if(distance[index]>distance[i])
                index=i;
        }

        return index;
    }

    private boolean nochange() throws Exception {
        int count=0;
        for(int v=0;v<centroids.size();v++)
        {   //int count=0;
            if(centroids.get(v).compareCentroidPoints())
                count+=0;
            else
            {
                count++;
                break;
            }
        }
        return count==0;
    }

    private Centroid probable_datapoints(double[] distance) {
        int prob;
        Centroid c;
        for(prob=0;prob<distance.length;prob++)
            if(Math.random()<distance[prob])
                break;
        if(prob<distance.length)
            c=new Centroid(pool_of_Documents.get(prob).getUnitVector());
        else
            c=new Centroid(pool_of_Documents.get(prob-1).getUnitVector());
        return c;
    }

}
