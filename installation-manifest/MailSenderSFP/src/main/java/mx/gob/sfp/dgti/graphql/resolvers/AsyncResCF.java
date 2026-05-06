/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "ValidaCurp" sistema que permite realizar validaciones de las curps 
 * y datos personales de los servidores públicos que ya han sido validados por 
 * el sistema de RUSP.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author cgarias
 * @since 01/05/2019
 * @param <T>
 */
public class AsyncResCF<T> extends CompletableFuture<T> implements Handler<AsyncResult<T>> {

    @Override
    public void handle(AsyncResult<T> ar) {
        if (ar.succeeded()) {
            complete(ar.result());
        } else {
            completeExceptionally(ar.cause());
        }
    }

}
