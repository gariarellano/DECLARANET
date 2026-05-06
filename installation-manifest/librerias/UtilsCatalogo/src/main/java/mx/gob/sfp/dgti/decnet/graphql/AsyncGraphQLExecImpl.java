package mx.gob.sfp.dgti.decnet.graphql;

import graphql.ExecutionInput;
import graphql.GraphQLError;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.List;
import java.util.Map;

public class AsyncGraphQLExecImpl implements AsyncGraphQLExec {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncGraphQLExecImpl.class);
    
    private GraphQLProvider graphQLProvider;
    
    
    public AsyncGraphQLExecImpl(Vertx vertx, int tipoSchema){
        graphQLProvider = new GraphQLProvider();
        try {
            graphQLProvider.init(vertx, tipoSchema);
        } catch (Exception e){
            LOGGER.error(e);
        }
    }

    @Override
    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables) {        
    	Future<JsonObject> future = Future.future();
    	try {
            graphQLProvider.graphQL()
                    .executeAsync(ExecutionInput.newExecutionInput(query)
                    			.operationName(operationName).variables(variables).operationName(operationName).build())
                    .handle((executionResult, exception) -> {
                    Map<String, Object> data = executionResult.getData();
                    List<GraphQLError> errors = executionResult.getErrors();
                    if (!errors.isEmpty()) {
                        future.fail(new AsyncExecException("Excepción durante la ejecución.", errors));
                    } else {
                        future.complete(new JsonObject(data));
                    }
                return this;
            });
        
        } catch(Exception e) {
        	future.fail(e);
        }
    	return future;
    }
}
