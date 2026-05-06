package mx.gob.sfp.dgti.omextback.graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;

import java.util.concurrent.CompletableFuture;

public interface AsyncDataFetcher<T> extends DataFetcher<CompletableFuture<T>> {

    @Override
    public default CompletableFuture<T> get(DataFetchingEnvironment environment) {
        AsyncResCF<T> asyncResCF = new AsyncResCF<>();
        getAsync(environment, asyncResCF);
        return asyncResCF;
    }

    public void getAsync(DataFetchingEnvironment environment, Handler<AsyncResult<T>> handler);
}
