package clients;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.config.Config;
import play.libs.ws.*;
import java.util.concurrent.CompletionStage;

public class ApiClient implements WSBodyReadables, WSBodyWritables {
    private final WSClient ws;
    private final Config config;

    @Inject
    public ApiClient(WSClient ws, Config config) {
        this.ws = ws;
        this.config = config.getConfig("api.data");
    }

    public CompletionStage<JsonNode> fetchTasks() {
        String url = config.getString("baseUrl") + "/tasks";
        return this.ws.url(url).get().thenApply(r ->
            r.getBody(json())
        );
    }

    public CompletionStage<JsonNode> fetchTask(String id) {
        String url = config.getString("baseUrl") + "/tasks/" + id;
        return this.ws.url(url).get().thenApply(r ->
                r.getBody(json())
        );
    }

    public CompletionStage<JsonNode> fetchUsers() {
        String url = config.getString("baseUrl") + "/users";
        return this.ws.url(url).get().thenApply(r ->
                r.getBody(json())
        );
    }
}