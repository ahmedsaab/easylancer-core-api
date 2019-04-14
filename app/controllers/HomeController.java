package controllers;

import clients.ApiClient;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.*;

import java.util.concurrent.CompletionStage;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    private final Config config;
    private final ApiClient apiClient;
    private final HttpExecutionContext httpExecutionContext;

    @Inject
    public HomeController(Config config, WSClient ws, HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
        this.apiClient = new ApiClient(ws, config);
        this.config = config;
    }

    public Result index() {
        return ok("Hello");
    }
}
