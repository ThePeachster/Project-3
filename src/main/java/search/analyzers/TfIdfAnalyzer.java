package search.analyzers;


import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import java.net.URI;



/**
* This class is responsible for computing how "relevant" any given document is
* to a given search query.
*
* See the spec for more details.
*/
public class TfIdfAnalyzer {
    // This field must contain the IDF score for every single word in all
    // the documents.
    private IDictionary<String, Double> idfScores;
    
    // This field must contain the TF-IDF vector for each webpage you were given
    // in the constructor.
    //
    // We will use each webpage's page URI as a unique key.
    private IDictionary<URI, IDictionary<String, Double>> documentTfIdfVectors;
    
    // Feel free to add extra fields and helper methods.
    private IDictionary<URI, Double> normDocumentVector;
    
    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.
        
        this.idfScores = this.computeIdfScores(webpages);
        this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
        this.normDocumentVector = new ChainedHashDictionary<URI, Double>();
        for (KVPair<URI, IDictionary<String, Double>> pair : documentTfIdfVectors) {
            normDocumentVector.put(pair.getKey(), norm(pair.getValue()));
        }
    }
    
    // Note: this method, strictly speaking, doesn't need to exist. However,
    // we've included it so we can add some unit tests to help verify that your
    // constructor correctly initializes your fields.
    public IDictionary<URI, IDictionary<String, Double>> getDocumentTfIdfVectors() {
        return this.documentTfIdfVectors;
    }
    
    // Note: these private methods are suggestions or hints on how to structure your
    // code. However, since they're private, you're not obligated to implement exactly
    // these methods: Feel free to change or modify these methods if you want. The
    // important thing is that your 'computeRelevance' method ultimately returns the
    // correct answer in an efficient manner.
    
    /**
    * This method should return a dictionary mapping every single unique word found
    * in any documents to their IDF score.
    */
    private IDictionary<String, Double> computeIdfScores(ISet<Webpage> pages) {
        //use field to save time
        idfScores = new ChainedHashDictionary<String, Double>();
        //for each webpage
        for (Webpage page : pages) {
            ISet<String> words = new ChainedHashSet<String>();
            //get every unique word
            for (String word : page.getWords()) {
                words.add(word);
            }
            //for every unique word
            for (String word : words) {
                //if it doesnt exist in the dictionary, set it to 1
                if (!idfScores.containsKey(word)) {
                    idfScores.put(word, 1.0);
                } else {
                    //increment the score by 1 since it does exist
                    idfScores.put(word, idfScores.get(word) + 1.0);
                }
            }
        }
        for (KVPair<String, Double> pair : idfScores) {
            double idfScore = Math.log(pages.size() / pair.getValue());
            idfScores.put(pair.getKey(), idfScore);
        }
        
        //return dictionary
        return idfScores;
        //throw new NotYetImplementedException();
    }
    
    /**
    * Returns a dictionary mapping every unique word found in the given list
    * to their term frequency (TF) score.
    *
    * We are treating the list of words as if it were a document.
    */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
        //create a dictionary
        IDictionary<String, Double> tfScores = new ChainedHashDictionary<String, Double>();
        //for each word in the list
        for (String word : words) {
            //if word exists in dictionary
            if (tfScores.containsKey(word)) {
                tfScores.put(word, tfScores.get(word) + 1);
            } else {
                tfScores.put(word, 1.0);
            }
        }
        
        for (KVPair<String, Double> pair : tfScores) {
            //divide that number by words.size
            tfScores.put(pair.getKey(), pair.getValue() / words.size());
        }
        return tfScores;
    }
    
    
    
    /**
    * See spec for more details on what this method should do.
    */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        IDictionary<URI, IDictionary<String, Double>> tfIdfVector =
        			new ChainedHashDictionary<URI, IDictionary<String, Double>>();
        for (Webpage page : pages) {
            IDictionary<String, Double> scores = computeTfScores(page.getWords());
            
            for (KVPair<String, Double> pair : scores) {
                scores.put(pair.getKey(), pair.getValue() * idfScores.get(pair.getKey()));
            }
            tfIdfVector.put(page.getUri(), scores);
        }
        return tfIdfVector;
    }
    
    
    /**
    * Returns the cosine similarity between the TF-IDF vector for the given query and the
    * URI's document.
    *
    * Precondition: the given uri must have been one of the uris within the list of
    *               webpages given to the constructor.
    */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        IDictionary<String, Double> documentVector = documentTfIdfVectors.get(pageUri);
        IDictionary<String, Double> queryVector = new ChainedHashDictionary<>();
        IDictionary<String, Double> queryTfScores = computeTfScores(query);
        // fill query vector
        for (String word : query) {
            if (this.idfScores.containsKey(word)) {
                double tfIdfScore = queryTfScores.get(word) * idfScores.get(word);
                queryVector.put(word, tfIdfScore);
            }
        }
        
        double numerator = 0.0;
        // for each word in query - compute numerator
        for (KVPair<String, Double> pair : queryVector) {
            String key = pair.getKey();
            if (documentVector.containsKey(key)) {
                numerator += (double) queryVector.get(key) * (double) documentVector.get(key);
            }
        }
        double denominator = normDocumentVector.get(pageUri) * norm(queryVector);
        
        if (denominator != 0.0) {
            return numerator / denominator;
        } else {
            return 0.0;
        }
    }
    
    // provided from spec
    private double norm(IDictionary<String, Double> vector) {
        double output = 0.0;
        for (KVPair<String, Double> pair : vector) {
            double score = pair.getValue();
            output += score * score;
        }
        return Math.sqrt(output);
    }
}