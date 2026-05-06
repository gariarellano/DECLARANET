/* 
 * Sistema Propiedad de la Secretaría de la Función Pública DGTI
 * "ValidaCurp" sistema que permite realizar validaciones de las curps 
 * y datos personales de los servidores públicos que ya han sido validados por 
 * el sistema de RUSP.
 * 
 * desarrolado por: cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.graphql.resolvers;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import io.vertx.core.Vertx;

/**
 * Clase que envuelve el schema descrito en el resources.
 * 
 * @author cgarias
 * @since 01/05/2019
 */
public class GraphQLProvider {
    private static final String SCHEMA_GRAPHQL = "schema.graphqls";
    private GraphQLDataFetchers graphQLDataFetchers;
    private GraphQL graphQL;
    
    /**
     * Método principal para inicializar la clase como objeto.
     * 
     * @param vertx herrmaienta vertx.
     * @throws IOException en caso de ocurrir una exepcion.
     */
    public void init(Vertx vertx) throws IOException {
        graphQLDataFetchers = GraphQLDataFetchers.init(vertx);
        this.graphQL = GraphQL.newGraphQL(buildSchema(Resources.toString(Resources.getResource(SCHEMA_GRAPHQL), Charsets.UTF_8))).build();
    }
    
    /**
     * Método que compila el Schema definido para graphql.
     * 
     * @param sdl Schema definido como recurso fisico del proyecto.
     * @return GraphQLSchema O)bjeto compilado par asu uso.
     */
    private GraphQLSchema buildSchema(String sdl) {
        return new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse(sdl), buildWiring());
    }
    
    private RuntimeWiring buildWiring() {
        Map<String, DataFetcher> query = new HashMap<>(); 
        query.put("mailSends", graphQLDataFetchers.mailSends);
        
        Map<String, DataFetcher> mutation = new HashMap<>();//Mapa que contiene los mutation's en caso de tener.
        mutation.put("sendMail", graphQLDataFetchers.sendMail);
        mutation.put("sendMailObj", graphQLDataFetchers.sendMailObj);
        mutation.put("sendMailObjResp", graphQLDataFetchers.sendMailObjResp);
        
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query").dataFetchers(query))// se agregan a las respuestas los tipos utilizados.
                .type(newTypeWiring("Mutation").dataFetchers(mutation)) //en caso de tener mutations.
                .build();
    }

    public GraphQL graphQL() {
        return graphQL;
    }
}
