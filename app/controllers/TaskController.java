package controllers;

import clients.ApiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import play.api.mvc.Results;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import org.bson.types.ObjectId;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * This controller contains an action to handle HTTP requests
 * to the task page.
 */
public class TaskController extends Controller {

    private final Config config;
    private final ApiClient apiClient;
    private final HttpExecutionContext httpExecutionContext;

    private static final String currentUser = "5cb1ef55e83e494919135d9f";
    @Inject
    public TaskController(Config config, WSClient ws, HttpExecutionContext ec) {
        this.httpExecutionContext = ec;
        this.apiClient = new ApiClient(ws, config);
        this.config = config;
    }

    public CompletionStage<Result> editTask(String id) {
        ObjectNode payload = Json.newObject();

        return this.apiClient.fetchTasks().thenApply(response -> {
            payload.put("data", response);
            return ok(payload);
        });
    }

    public CompletionStage<Result> applyToTask(String id) {
        ObjectNode payload = Json.newObject();

        return this.apiClient.fetchUsers().thenApply(response -> {
            payload.put("data", response);
            return ok(payload);
        });
    }

    public CompletionStage<Result> createTask() {
        ObjectNode payload = Json.newObject();

        return this.apiClient.fetchUsers().thenApply(response -> {
            payload.put("data", response);
            return ok(payload);
        });
    }

    public CompletionStage<Result> viewTask(String id) {
        ObjectNode payload = Json.newObject();
        if (!ObjectId.isValid(id)) {
            ObjectNode errorResponse = Json.newObject();
            errorResponse.put("message", "invalid id");

            return CompletableFuture.supplyAsync(() -> badRequest(errorResponse));
        };

        return this.apiClient.fetchTask(id).thenApply(response -> {
            ObjectNode operations = Json.newObject();
            operations.put("edit",false);
            operations.put("apply",false);
            operations.put("cancel",false);
            operations.put("review",false);
            operations.put("remove",false);

            JsonNode task = response.get("data");

            String role = "viewer";
            String status = task.get("status").textValue();
            String workerUser = task.get("workerUser").isContainerNode() ?
                    task.get("workerUser").get("_id").textValue(): null;
            String ownerUser = task.get("creatorUser").get("_id").textValue();

            if (currentUser.equals(ownerUser)) {
                role = "owner";
            } else if (currentUser.equals(workerUser)) {
                role = "worker";
            }

            if (
                    role.equals("owner") &&
                        status.equals("open")
            ) {
                operations.put("edit", "instant");
                operations.put("remove", true);
            }
            if (
                    role.equals("owner") &&
                            (status.equals("accepted") || status.equals("in-progress"))) {
                operations.put("edit", "with-approval");
            }
            if (
                    (role.equals("owner") || role.equals("worker")) &&
                            status.equals("accepted")
            ){
                operations.put("cancel", true);
            }
            if (
                    (role.equals("owner") &&
                            status.equals("in-progress")) ||
                    (role.equals("worker") && (
                            status.equals("in-progress") || status.equals("done") || status.equals("not-done")))
            ){
                operations.put("review", true);
            }
            if (
                    role.equals("viewer") &&
                            status.equals("open")
            ) {
                operations.put("apply", true);
            }

            payload.set("task", task);
            payload.set("operations", operations);
            return ok(payload);
        });
    }

    public CompletionStage<Result> reviewTask(String id) {
        ObjectNode payload = Json.newObject();

        return this.apiClient.fetchUsers().thenApply(response -> {
            payload.put("data", response);
            return ok(payload);
        });
    }

    public CompletionStage<Result> cancelTask(String id) {
        ObjectNode payload = Json.newObject();

        return this.apiClient.fetchUsers().thenApply(response -> {
            payload.put("data", response);
            return ok(payload);
        });
    }
}
