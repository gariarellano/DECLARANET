/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "ValidaCurp" sistema que permite realizar validaciones de las curps 
 * y datos personales de los servidores públicos que ya han sido validados por 
 * el sistema de RUSP.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;


import java.util.List;
import java.util.Map;

import graphql.ExecutionInput;
import graphql.GraphQLError;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * clase que permite realizar los queryes de manera asynchrona.
 * 
 * @author cgarias
 * @since 01/05/2019
 */
public class AsyncGraphQLExecImpl implements AsyncGraphQLExec{
    
    private static final Logger LOGG = LoggerFactory.getLogger(AsyncGraphQLExecImpl.class);
    
    /**
     * GraphQLProvider
     */
    private GraphQLProvider graphQLProvider;
    
    public AsyncGraphQLExecImpl(Vertx vertx){
        graphQLProvider = new GraphQLProvider();
        try {
            graphQLProvider.init(vertx);
        } catch (Exception e){
            LOGG.error(e);
        }
    }

    /**
     * Método principal que el framework de graphql ejecuta.
     * 
     * @param query Todos los envios por graphql, vienen envueltos en una variable query, independientemente sean query o mutation.
     * @param operationName Describe la operación que se va a ejecutar. mutation o query
     * @param context contexto del router para realizar la respuesta correspondiente.
     * @param variables en caso de que el query contenga variables en este objeto se reciben.
     * @return 
     */
    @Override
    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables){ 
        Future<JsonObject> fut = Future.future();
        try {
            graphQLProvider
                    .graphQL()
                    .executeAsync(ExecutionInput.newExecutionInput(query).operationName(operationName).variables(variables).build())
                    .handle((executionResult, exception) -> {
                    Map<String, Object> data = executionResult.getData();
                    List<GraphQLError> errors = executionResult.getErrors();
                    if (!errors.isEmpty()) {
                        fut.fail(new AsyncExecException("Excepción durante la ejecución.", errors));
                    } else {
                        fut.complete(new JsonObject(data));
                    }
                return this;
            });
        } catch (Exception e) {
            fut.fail(e);
            LOGG.error(e);
        }
        return fut;
    }
}
