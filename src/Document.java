import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class Document {
    String title;
    Integer maxtf =1;
    HashMap<String, Integer> tf = new HashMap<>();
    ArrayList<String> words = new ArrayList<>();
    ArrayList<Double> tfIdf = new ArrayList<>();
    HashMap<Integer, Double> tfidfhashmap = new HashMap<>();//key = index of the word value tfidf of word
    HashMap<String, Double> tfidfwords = new HashMap<>();//key = word value tfidf of word
    ArrayList<Double> unitVector=new ArrayList<>();

    Vector<Double> vector = new Vector<Double>();

    public Document(String title) {
        this.title = title;
    }


    public void addtf(String word){
        if(tf.containsKey(word)){
            int temp = tf.get(word)+1;
            tf.put(word,temp);
            if(temp> maxtf)
                maxtf = temp;
        }
        else{
            tf.put(word,1);
        }
    }

    ArrayList<Double> getUnitVector() {
        return unitVector;
    }
}
