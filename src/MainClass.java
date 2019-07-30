import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.stemmers.LovinsStemmer;
import weka.filters.unsupervised.attribute.StringToWordVector;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class MainClass {
    private static MainClass ourInstance;

    static {
        try {
            ourInstance = new MainClass();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MainClass getInstance() {
        return ourInstance;
    }
    FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Roi\\Downloads\\Documents\\שנה ד' סיום\\סמסטר ב\\אחזור מידע\\ihzurweka\\src\\output.txt"));
    PrintWriter printWriter = new PrintWriter(fileWriter);
         ArrayList<Double> distance = new ArrayList<>();
         ArrayList<String> bagofwords = new ArrayList();
    HashMap<String, Document> allFiles = new HashMap<>();
    HashMap<String, Integer> df = new HashMap<>();
    public static String[] stopwords = {"set","a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "\nthis", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero",
            "0","1","2","3","4","5","6","7","8","9","&",":","b","c","d","e","f","g","h","j","k","l","m","n","p","r","s","t","w","y",
            "\na","10","al","form","decision","paper","number","in","we","good","multiple","algorithm","higher","high","social","university","cases","analysis","group","include","knowledge","systems","focus","design","machine","acm","order","impact","due",
            "in\nthe", "found","level","computer","making","general","international","terms","similar","based","important","made","make","study","addition","this","potential","results","models","performance","science","model","current","journal","learning","problem","part","methods","system","result","provide","research","future","approach","values","individual","algorithms","0",
            "shows","shown","measure","show","process","information","\nand","effect","\nfor","bias","including","\nthe","time","point","specific","case","type","data","considered","section","context","means","conference","present","large","work","fact","related", "\nin","\nwe","applied" , "of\nthe"};
    public static Set<String> stopWordSet = new HashSet<String>(Arrays.asList(stopwords));
    private MainClass() throws IOException {}

    public static void main(String[] args) throws Exception {
        MainClass n =MainClass.getInstance();
        n.scannAll();
        n.print("Creating Vectors For Files");
        for (Document d : n.allFiles.values()) {
            n.tfidfforVector(d);
            d.unitVector =new ArrayList<>( n.vec(d));
        }
//        for (Document d : n.allFiles.values()) {
////         System.out.println(n.vec(d).toString());
//            j.v.add(n.vec(d) );
//
//        }
      n.print( "We have "+n.allFiles.size()+ " Different Documents");
        n.print("Dictionary size: "+n.df.size());
        n.makebagofwords();
//        System.out.println(n.bagofwords);
        KMeanski k=new KMeanski(new ArrayList<>(n.allFiles.values()));
        k.createCluster(3);
//        n.createwekainstance();
//        n.createwekainstance1();
//        n.fifapes();
//        n.fifapes();
//        String outputFilename= "C:\\Users\\Roi\\Downloads\\Documents\\שנה ד' סיום\\סמסטר ב\\אחזור מידע\\ihzurweka\\src\\bigbig.arff";
//        Instances dataset = n.createwekainstance();
//
//        try {
//            ConverterUtils.DataSink.write(outputFilename, dataset);
//        }
//        catch (Exception e) {
//            System.err.println("Failed to save data to: " + outputFilename);
//            e.printStackTrace();
//        }
//        SimpleKMeans k = new SimpleKMeans();
//        k.setNumClusters(3);
//        k.buildClusterer(n.createwekainstance());
//      System.out.println(k.toString());
//        k.clusterInstance(n.createwekainstance());



//        System.out.println( n.createwekainstance().toString());


    }

    public void makebagofwords(){
        this.bagofwords.addAll(df.keySet());
    }

    public void scannAll() throws FileNotFoundException {
        JFileChooser j = new JFileChooser();
        j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        j.showOpenDialog(j);
        //File dir = new File("C:\\Users\\tirth_parikh\\Desktop\\Test");
        File dir = new File(j.getSelectedFile().getAbsolutePath());
        List<File> files = (List<File>) FileUtils.listFiles(dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
        try {
            print("Scanning the document");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (File fileName : files) {
            Scanner sc = new Scanner(fileName).useDelimiter("[^A-Za-z]+");
//        File temp = new File(String.valueOf(directory), fileNam1e);
            LovinsStemmer l = new LovinsStemmer();
            Document d = new Document(fileName.getName());
            while (sc.hasNext()) {
                String w = sc.next();
                w= w.toLowerCase();
                if (!stopWordSet.contains(w)) {
//                    w = l.stemString(w);

//                    System.out.println(d.title + " : "+w);
                    d.words.add(w);//check
                    if (!d.tf.containsKey(w)) {
                        this.addDf(w);//Document Frequency!
                    }
                    d.addtf(w);//Term Frequency
                }

                allFiles.put(d.title, d);
                //fileLocations.add(String.valueOf(temp));
            }
        }
    }

    public void print(String s)
            throws IOException {
        System.out.println(s);
        printWriter.println(s +"\n");

    }




    public int addDf(String word) {
        if (df.containsKey(word)) {
            df.put(word, df.get(word) + 1);
        } else {
            df.put(word, 1);
        }
        return 1;
    }

    public void tfidfforVector(Document d) {
        int i = 0;
        double sidzer = this.allFiles.values().size();
        for (String s : df.keySet()) {
            if (d.tf.containsKey(s)) {
                double tfnormal = Double.valueOf(d.tf.get(s)) / Double.valueOf(d.maxtf);
                double idfnormal = Math.log10(sidzer / df.get(s));
                d.tfidfhashmap.put(i,tfnormal * idfnormal);
                d.tfidfwords.put(s, tfnormal * idfnormal);
            }
            i++;
        }
//        System.out.println(d.tfidfhashmap.toString()+ " ");
    }

    public Vector<Double> vec(Document d) {
        Vector<Double> v = new Vector<Double>();
        for (int i=0; i< this.df.size(); i++) {
            if(d.tfidfhashmap.containsKey(i)) {
                v.add(i, d.tfidfhashmap.get(i)*1000);
            }
            else
            v.add(i,0.0);
        }
        return vecNormailized(v);
    }

    public Vector<Double> vecNormailized(Vector<Double> d) {
        double squaredSum=0;
        Vector<Double> v = new Vector<Double>();
        for(int i = 0; i<d.size();i++)
        {
            squaredSum+=(d.get(i)*d.get(i));
        }
        squaredSum=Math.sqrt(squaredSum);
        for(int j=0;j<d.size();j++)
        {
            v.add(j,d.get(j)/squaredSum);
        }

        return v;
    }


    public FastVector fvec (){
        FastVector fv = new FastVector(this.df.size());
        for (String s: df.keySet()) {
                fv.addElement(new Attribute(s));
        }
        return fv;
    }


    public void scanweka()  {

        Instances pes = new Instances("Manny", fvec(), df.size());
        for (Document d : this.allFiles.values()) {
            Instance fifaDemo = new Instance(this.df.size());
//            fifa.setDataset(pes);
            Integer jbSmoove = 1;
            for (String s : d.words) {
                fifaDemo.setValue(jbSmoove, s);
                jbSmoove++;
            }
            pes.add(fifaDemo);
        }
        StringToWordVector filter = new StringToWordVector();
        try {
            filter.setInputFormat(pes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setIDFTransform(true);
        filter.setUseStoplist(true);
        LovinsStemmer stemmer = new LovinsStemmer();
        filter.setStemmer(stemmer);
        filter.setLowerCaseTokens(true);
        String outputFilename = "C:\\Users\\Roi\\Downloads\\Documents\\שנה ד' סיום\\סמסטר ב\\אחזור מידע\\ihzurweka\\src\\bigbig.arff";

        try {
            ConverterUtils.DataSink.write(outputFilename, pes);
        } catch (Exception e) {
            System.err.println("Failed to save data to: " + outputFilename);
            e.printStackTrace();
        }
    }
   public Instances createwekainstance(){
       Instance fifa = new Instance(this.df.size());
       Instances pes = new Instances("Manny" , fvec(),df.size()) ;
       fifa.setDataset(pes);
       int kgb =1;
       for (Document d: this.allFiles.values())
       {
           Instance fifaDemo = new Instance(this.df.size());
//           fifaDemo.setDataset(pes);
          Vector<Double> v = vec(d);
          for(int i =0; i<v.size();i++) {
              fifaDemo.setValue(i, v.get(i));
              }
           pes.add(fifaDemo);
          System.out.println(kgb);
          kgb++;
       }
//fifa.setDataset(pes);
       return pes;
   }

    public Instances createwekainstance1(){
        Instance fifa = new Instance(this.df.size());
        Instances pes = new Instances("Manny" , fvec(),df.size()) ;
        fifa.setDataset(pes);
        int kgb =1;
        for (Document d: this.allFiles.values())
        {
            Instance fifaDemo = new Instance(this.df.size());
           fifaDemo.setDataset(pes);
//            Vector<Double> v = vec(d);
            for(int i =0; i<d.words.size();i++) {
                fifaDemo.setValue(i, d.words.get(i));
            }
            pes.add(fifaDemo);
            System.out.println(kgb);
            kgb++;
        }
//fifa.setDataset(pes);
        return pes;
    }

    public void fifapes(){
        Instance fifa = new Instance(this.df.size());
        Instances pes = new Instances("Manny" , fvec(),df.size()) ;
        fifa.setDataset(pes);
        int kgb =1;
        for (Document d: this.allFiles.values())
        {
            Instance fifaDemo = new Instance(this.df.size());
//           fifaDemo.setDataset(pes);
            Vector<Double> v = vec(d);
            for(int i =0; i<df.size();i++) {
                if(d.tf.containsKey(bagofwords.get(i)))
                fifaDemo.setValue(i, v.get(i));
                else
                    fifaDemo.setValue(i, 0);
            }

            pes.add(fifaDemo);
            System.out.println(kgb);
            kgb++;
        }


        StringToWordVector filter = new StringToWordVector();
        try {
            filter.setInputFormat(pes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        filter.setIDFTransform(true);
        filter.setUseStoplist(true);
        LovinsStemmer stemmer = new LovinsStemmer();
        filter.setStemmer(stemmer);
        filter.setLowerCaseTokens(true);
        String outputFilename = "C:\\Users\\Roi\\Downloads\\Documents\\שנה ד' סיום\\סמסטר ב\\אחזור מידע\\ihzurweka\\src\\bigbig.arff";

        try {
            ConverterUtils.DataSink.write(outputFilename, pes);
        } catch (Exception e) {
            System.err.println("Failed to save data to: " + outputFilename);
            e.printStackTrace();
        }
    }


}
