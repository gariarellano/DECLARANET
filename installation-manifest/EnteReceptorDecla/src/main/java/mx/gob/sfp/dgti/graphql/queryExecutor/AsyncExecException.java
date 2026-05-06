package mx.gob.sfp.dgti.graphql.queryExecutor;

import graphql.GraphQLError;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author programador04
 * @since 11/05/2020
 */
public class AsyncExecException extends Exception {

    private static final long serialVersionUID = 1L;

    private List<GraphQLError> errors = new ArrayList<>();

    public AsyncExecException(String message, List<GraphQLError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<GraphQLError> getErrors() {
        return errors;
    }

    public Stream<GraphQLError> errorStream() {
        return errors.stream();
    }

    @Override
    public String toString() {
        return super.toString() + " " + errors;
    }
}
