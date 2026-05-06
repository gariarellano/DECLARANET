/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "ValidaCurp" sistema que permite realizar validaciones de las curps 
 * y datos personales de los servidores públicos que ya han sido validados por 
 * el sistema de RUSP.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import java.util.concurrent.CompletableFuture;

/**
 * Inerface que permite la relacion con los 
 * fetcher java y con la interacción de codigo
 * 
 * @author cgarias
 * @since 01/05/2019
 * @param <T>
 */
public interface AsyncDataFetcher<T> extends DataFetcher<CompletableFuture<T>> {

    /**
     * Método que realiza la envoltura de codigo para poder ejecutar codigo programado.
     * @param environment
     * @return
     */
    @Override
    default public CompletableFuture<T> get(DataFetchingEnvironment environment) {
        AsyncResCF<T> asyncResCF = new AsyncResCF<>();
        getAsync(environment, asyncResCF);
        return asyncResCF;
    }

    /**
     *
     * @param environment
     * @param handler
     */
    public void getAsync(DataFetchingEnvironment environment, Handler<AsyncResult<T>> handler);
}
