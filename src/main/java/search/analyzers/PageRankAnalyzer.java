package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ArrayDictionary;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
    	IDictionary<URI, ISet<URI>> graph = new ArrayDictionary<>();
    	// make a set of the URIs of all webpages for later comparison
    	ISet<URI> allPages = new ChainedHashSet<>();
    	for (Webpage compare : webpages) {
    		allPages.add(compare.getUri());
    	}
    	// in each webpage
    	for (Webpage page : webpages) {
    		// get the links and store it in a list
    		URI currentPage = page.getUri();
    		IList<URI> links = page.getLinks();
    		ISet<URI> setLinks = new ChainedHashSet<>();
    		// convert list to set for returning
    		for (URI link : links) {
    			// if it doesnt link to itself and doesnt link to something not in the set
    			if (!link.equals(currentPage) && allPages.contains(link)) {
    				setLinks.add(link);
    			}
    		}
    		graph.put(currentPage, setLinks);
    	}	
        return graph;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
    			double decay,
    			int limit,
    			double epsilon) {
	    // Step 1: The initialize step should go here
	    IDictionary<URI, Double> webpageRanks = new ChainedHashDictionary<URI, Double>();
	    for (KVPair<URI, ISet<URI>> pair : graph) {
	        webpageRanks.put(pair.getKey(), 1.0 / (double) graph.size());
	    }
	    
	    
	    	for (int i = 0; i < limit; i++) { 
	    		IDictionary<URI, Double> temp = new ChainedHashDictionary<URI, Double>();
	    		// Step 2: The update step should go here
		    // give web page new rank of 0
	    		for (KVPair<URI, ISet<URI>> pair : graph) {
	        		temp.put(pair.getKey(), 0.0);
	    	    }
	    		
		    // share old page rank with every webpage it links to
	    		for (KVPair<URI, ISet<URI>> pair : graph) {
	    			ISet<URI> links = pair.getValue();
	    			if (!links.isEmpty()) {
	    				for (URI uri : links) {
						double rank = temp.get(uri) + (webpageRanks.get(pair.getKey()) * decay / (double) links.size());
						temp.put(uri, rank);
					}
	    			} else {
	    				for (KVPair<URI, Double> tempPair : temp) {
	    					temp.put(tempPair.getKey(), tempPair.getValue() + 
	    							decay * (webpageRanks.get(pair.getKey()) / graph.size()));
	    				} 
					
	    			}    			
	    		}
	    		
	    		boolean check = false;
	    		int count = 0;
	    		for(KVPair<URI, Double> pair : temp) {	    			
	    			URI uri = pair.getKey();
	    			double newPageRank = pair.getValue() + (1 - decay) / graph.size();
	    			// old page rank - new page rank < epsilon
	    			if (Math.abs(webpageRanks.get(uri) - newPageRank) < epsilon) {
	    				count++;
	    			}
	    			if (count == temp.size()) {
	    				check = true;
	    			}
	    			webpageRanks.put(uri, newPageRank);
	    		}
	    		// check: if converged, stop
	    		if (check) {
	    			return webpageRanks;
	    		}
	    	}
	    	return webpageRanks;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        // TODO: Add working code here
        return pageRanks.get(pageUri);
    }
}
