/**
 * @(#)AsyncDataFetcher.java 11/05/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.graphql.queryExecutor;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author programador04
 * @since 11/05/2020
 */
public interface AsyncDataFetcher<T> extends DataFetcher<CompletableFuture<T>> {


    /**
     *
     * @param environment
     * @return
     */
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
