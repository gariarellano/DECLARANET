/**
 * @(#)GraphQLProvider.java 11/05/2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.graphql;


import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.vertx.core.Vertx;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

/**
 * Provider de GraphQL
 *
 * @author programador04
 * @since 11/05/2020
 */
public class GraphQLProvider {
    private static final String SCHEMA_GRAPHQL = "/schema.graphqls";
    private static final String QUERY = "Query";
    private static final String MUTATION = "Mutation";
	
    /**
     * DataFetchers
     */
    GraphQLDataFetchers graphQLDataFetchers;

    /**
     * GrapQL
     */
    private GraphQL graphQL;


    public void init(Vertx vertx) throws IOException {
        graphQLDataFetchers = new GraphQLDataFetchers();
        graphQLDataFetchers.init(vertx);
        File archivo = new File(SCHEMA_GRAPHQL);
        InputStream archivo_prueba = getClass().getResourceAsStream(SCHEMA_GRAPHQL);
        this.graphQL = GraphQL.newGraphQL(buildSchema(archivo_prueba)).build();
    }

    /**
     * Funcion para crear el schema
     *
     * @author programador04
     * @since 11/05/2020
     */
    private GraphQLSchema buildSchema(InputStream archivo) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(new BufferedReader(new InputStreamReader(archivo)));
        RuntimeWiring runtimeWiring = buildWiring();
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    /**
     * @author programador04
     * @since 11/05/2020
     */
    private RuntimeWiring buildWiring() {
        Map<String, DataFetcher> queries = new HashMap<>();
            queries.put("guardarEnte", graphQLDataFetchers.getQueryDefault());

        Map<String, DataFetcher> mutations = new HashMap<>();
            mutations.put("agregarEnte", graphQLDataFetchers.getAgregarEnte());

        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring(QUERY)
                        .dataFetchers(queries))
                .type(newTypeWiring(MUTATION)
                        .dataFetchers(mutations))
                .build();
    }

    /**
     * Getter del objeto GraphQL
     *
     * @author programador04
     * @since 11/05/2020
     */
    public GraphQL graphQL() {
        return graphQL;
    }
}