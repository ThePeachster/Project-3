package search.analyzers;

import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import misc.exceptions.NotYetImplementedException;
import search.models.Webpage;

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

    public TfIdfAnalyzer(ISet<Webpage> webpages) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        //this.idfScores = this.computeIdfScores(webpages);
        //this.documentTfIdfVectors = this.computeAllDocumentTfIdfVectors(webpages);
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
    		//create dictionary
    		IDictionary<String, Double> idf = new ArrayDictionary<String, Double>();
    		//for each word in each webpage
    		for (Webpage page : pages) {
    			IList<String> words = page.getWords();
    			for (String word : words) {
    				//if it doesnt exist in the dictionary
    				if (!idf.containsKey(word)) {
    					
    				}
    				//count number of docs it is in
    				//compute idf (ln(pages.size / # term appears))
    					//if term doesnt exist, say 0
    				//store it in the dictionary
    			}
    		}
    		//set field equal to dictionary
		//return dictionary
    	
        throw new NotYetImplementedException();
    }

    /**
     * Returns a dictionary mapping every unique word found in the given list
     * to their term frequency (TF) score.
     *
     * We are treating the list of words as if it were a document.
     */
    private IDictionary<String, Double> computeTfScores(IList<String> words) {
    		//create a dictionary
    		IDictionary<String, Double> tfScores = new ArrayDictionary<>();
    		//for each word in the list
    		for (String word : words) {
    			//if word doesn't exist in dictionary
    			if (!tfScores.containsKey(word)) {
    				int count = 0;
    				//find the number of times we see it in the doc
    				for (int i = 0; i < words.size(); i++) {
    					if(words.get(i).equals(word)) {
    						count++;
    					}
    				}
    				//divide that number by words.size
    				Double termScore = (double)count / (double)words.size();
    				//put word and termscore in dictionary
    				tfScores.put(word, termScore);
    			}
    		}
    		
    		//store in a field? Does this need to be saved??
    		//return dictionary
    		return tfScores;
    }

    /**
     * See spec for more details on what this method should do.
     */
    private IDictionary<URI, IDictionary<String, Double>> computeAllDocumentTfIdfVectors(ISet<Webpage> pages) {
        // Hint: this method should use the idfScores field and
        // call the computeTfScores(...) method.
    	
    		//new dictionary<URI, IDictionary<String, Double>>
    		//for each webpage
    			//computeTfScores(pages.words) * idfScores.getKey(word)
    			//store in dictionary(URI of webpage, term, relevance)
    		//store in a field
    		//return dictionary
    		
    	
        throw new NotYetImplementedException();
    }

    /**
     * Returns the cosine similarity between the TF-IDF vector for the given query and the
     * URI's document.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public Double computeRelevance(IList<String> query, URI pageUri) {
        // TODO: Replace this with actual, working code.

        // TODO: The pseudocode we gave you is not very efficient. When implementing,
        // this smethod, you should:
        //
        // 1. Figure out what information can be precomputed in your constructor.
        //    Add a third field containing that information.
        //
        // 2. See if you can combine or merge one or more loops.
        return 0.0;
    }
}
