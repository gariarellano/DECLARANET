/**
 * @(#)AsyncGraphQLExecImpl.java 25/04/2019
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
import mx.gob.sfp.dgti.utils.LoggerUtils;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 25/04/2019
 */
public class AsyncGraphQLExecImpl implements AsyncGraphQLExec{

    /**
     * GraphQLProvider
     */
    GraphQLProvider graphQLProvider;

    /**
     * Logger
     */
    final Logger logger = Logger.getLogger(AsyncGraphQLExecImpl.class);


    public AsyncGraphQLExecImpl(Vertx vertx) {

        graphQLProvider = new GraphQLProvider();

        try {
            graphQLProvider.init(vertx);
        } catch (Exception e){
            LoggerUtils.logError(e);
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
