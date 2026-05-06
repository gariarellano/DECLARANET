package mx.gob.sfp.dgti.omextback.graphql;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;

import java.util.Map;

/**
 * 
 * @since 13/06/2019
 *
 */
public interface AsyncGraphQLExec {
	
    public static AsyncGraphQLExec create(Vertx vertx) {
        return new AsyncGraphQLExecImpl(vertx);
    }

    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables);
}
