/* 
 * Sistema propiedad de la Secretaría de la Función Pública DGTI
 * "RegAdqPub" sistema que permite realizar el resitro 
 * de adquisiciones Publicas
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;

import graphql.GraphQLError;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 * @author cgarias
 * @since 01/05/2019
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
