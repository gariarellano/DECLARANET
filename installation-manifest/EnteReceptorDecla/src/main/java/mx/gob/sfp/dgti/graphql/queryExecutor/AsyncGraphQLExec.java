package mx.gob.sfp.dgti.graphql.queryExecutor;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Map;

public interface AsyncGraphQLExec {

    public static AsyncGraphQLExec create(Vertx vertx) {
        return new AsyncGraphQLExecImpl(vertx);
    }

    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables);
}
