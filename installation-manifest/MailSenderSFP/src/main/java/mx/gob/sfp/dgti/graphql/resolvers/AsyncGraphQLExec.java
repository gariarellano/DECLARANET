/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "ValidaCurp" sistema que permite realizar validaciones de las curps 
 * y datos personales de los servidores públicos que ya han sido validados por 
 * el sistema de RUSP.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.util.Map;

/**
 *
 * @author cgarias
 * @since 01/05/2019
 */
public interface AsyncGraphQLExec {
    public static AsyncGraphQLExec create(Vertx vertx) {
        return new AsyncGraphQLExecImpl(vertx);
    }

    public Future<JsonObject> executeQuery(String query, String operationName, Object context, Map<String, Object> variables);
}
