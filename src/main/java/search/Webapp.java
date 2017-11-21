package search;

import datastructures.interfaces.IList;
import search.models.Result;
import search.SearchEngine;
import search.misc.WordTokenizer;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all code to manage and serve our website.
 */
public class Webapp {
    /**
     * The path to all static, non-changing files we will serve.
     * (See 'src/main/resources')
     */
    private static final String STATIC_FILES = "webapp/static";

    /**
     * The path to all template HTML files. (See 'hw2/main/resources')
     */
    private static final String TEMPLATE_FILES = "webapp/templates";

    private final String siteName;
    private final SearchEngine engine;
    private final Service http;

    /**
     * Creates a new instance of this class.
     *
     * @param engine    The SearchEngine we will be using to answer user queries
     * @param siteName  The name of our website
     * @param port      The port to serve our website on.
     */
    public Webapp(SearchEngine engine, String siteName, int port) {
        this.engine = engine;
        this.siteName = siteName;

        this.http = Service.ignite()
                .staticFileLocation(STATIC_FILES)
                .port(port);
        this.http.get("/", this::handleMain);
        this.http.get("/search", this::handleSearch);
        this.http.exception(Exception.class, (ex, req, res) -> {
            ex.printStackTrace();
            res.status(500);
            res.body("<h1>500 Internal Server Error</h1><p>See console for stack trace.</p>");
        });
    }

    /**
     * Starts running this website.
     */
    public void launch() {
        this.http.init();
    }

    /**
     * Handles all incoming requests for our home page.
     */
    private String handleMain(Request req, Response res) {
        Map<String, Object> model = new HashMap<>();
        model.put("siteTitle", this.siteName);

        return this.render("main.mustache", model);
    }

    /**
     * Handles all incoming user queries.
     */
    private String handleSearch(Request req, Response res) {
        // Get search query
        String query = req.queryParams("query");
        int numResults = Integer.parseInt(req.queryParamOrDefault("num_results", "20"));

        // Perform core search
        IList<String> queryTerms = WordTokenizer.extract(query);
        IList<Result> results = this.engine.getTopKResults(queryTerms, numResults);

        // Render results
        Map<String, Object> model = new HashMap<>();
        model.put("siteTitle", this.siteName);
        model.put("results", results);
        model.put("initialQuery", query);

        return this.render("search.mustache", model);
    }

    private String render(String templateName, Map<String, Object> params) {
        return new MustacheTemplateEngine(TEMPLATE_FILES).render(
                new ModelAndView(params, templateName));
    }
}
