/**
 * @(#)GraphQLProvider.java 25/04/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.Vertx;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

/**
 * Provider de GraphQL
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 25/04/2019
 */
public class GraphQLProvider {

    /**
     * DataFetchers
     */
    GraphQLDataFetchers graphQLDataFetchers;

    /**
     * GrapQL
     */
    private GraphQL graphQL;

    /**
     * GraphQLProvider
     */
    final Logger logger = Logger.getLogger(GraphQLProvider.class);

    public void init(Vertx vertx) throws IOException {

        graphQLDataFetchers = new GraphQLDataFetchers();
        graphQLDataFetchers.init(vertx);

        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    /**
     * Funcion para crear el schema
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private GraphQLSchema buildSchema(String sdl) {

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    /**
     * Funcion para construir el wiring
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    private RuntimeWiring buildWiring() {
        Map<String, DataFetcher> queries = new HashMap<String, DataFetcher>();
            queries.put("obtenerEntes", graphQLDataFetchers.getObtenerEntes());
            queries.put("obtenerHistoricoEnte", graphQLDataFetchers.getObtenerHistoricoEnte());

        Map<String, DataFetcher> mutations = new HashMap<String, DataFetcher>();
            mutations.put("agregarEnteNuevo", graphQLDataFetchers.getAgregarEnteNuevo());
            mutations.put("actualizarEnte", graphQLDataFetchers.getActualizarEnte());
            mutations.put("desactivarEnte", graphQLDataFetchers.getDesactivarEnte());
            mutations.put("definirSituacionEnte", graphQLDataFetchers.getDefinirSituacionEnte());

        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetchers(queries))
                .type(newTypeWiring("Mutation")
                        .dataFetchers(mutations))
                .build();
    }

    /**
     * Getter del objeto GraphQL
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 25/04/2019
     */
    public GraphQL graphQL() {
        return graphQL;
    }

}