package mx.gob.sfp.dgti.decnet.graphql;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * 
 * @since 13/06/2019
 *
 */
public interface AsyncGraphQLExec {
	
    public static AsyncGraphQLExec create(Vertx vertx, int tipoSchema) {
        return new AsyncGraphQLExecImpl(vertx, tipoSchema);
    }

    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables);
}
