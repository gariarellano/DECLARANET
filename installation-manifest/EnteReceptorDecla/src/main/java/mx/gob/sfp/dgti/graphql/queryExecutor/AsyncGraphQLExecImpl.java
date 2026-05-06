/**
 * @(#)AsyncGraphQLExecImpl.java 11/05/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.graphql.queryExecutor;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQLError;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.graphql.GraphQLProvider;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author programador04
 * @since 11/05/2020
 */
public class AsyncGraphQLExecImpl implements AsyncGraphQLExec{

    /**
     * GraphQLProvider
     */
    private GraphQLProvider graphQLProvider;

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(AsyncGraphQLExecImpl.class.getName());


    public AsyncGraphQLExecImpl(Vertx vertx) {
        graphQLProvider = new GraphQLProvider();
        try {
            graphQLProvider.init(vertx);
        } catch (Exception e){
            logger.log(Level.SEVERE,"error en obtener el graph provider",e);
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables) {
        Future<JsonObject> fut = Future.future();
        try {
            ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                    .query(query)
                    .variables(variables)
                    .operationName(operationName)
                    .context(context)
                    .build();

            CompletableFuture<ExecutionResult> asyncResult = graphQLProvider.graphQL().executeAsync(executionInput);

            asyncResult.handle((executionResult, exception) -> {
                try {
                    Map<String, Object> data = executionResult.getData();
                    List<GraphQLError> errors = executionResult.getErrors();
                    if (!errors.isEmpty()) {
                        fut.fail(new AsyncExecException("Ocurrió algo inesperado", errors));
                    } else {
                        fut.complete(new JsonObject(data));
                    }
                } catch (Exception e) {
                    fut.fail(e);
                }
                return 0;
            });
        } catch (Exception e) {
            fut.fail(e);
        }
        return fut;
    }
}
