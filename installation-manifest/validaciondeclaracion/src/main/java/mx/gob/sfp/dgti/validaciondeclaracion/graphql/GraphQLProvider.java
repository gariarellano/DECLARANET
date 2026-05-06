/**
 * @(#)GraphQLProvider.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

/**
 * GraphqlProvider para relacionar queries y mutations en el esquema con funciones que se encuentran
 * en el graphQLDataFetcher
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class GraphQLProvider {

	/**
	 * Logger
	 */
	static final Logger LOGGER = LoggerFactory.getLogger(GraphQLProvider.class);

	/**
	 * Nombre del recurso de donde se tomara el schema de graphQL.
	 */
	private static final String SCHEMA_GRAPHQL = "recepcion.graphqls";

	/**
	 * Nombre para agregar los queries.
	 */
	private static final String QUERY = "Query";

	/**
	 * Nombre para agregar las mutation.
	 */
	private static final String MUTATION = "Mutation";

	/**
	 * Clase que contiene los metodos para resolver queries y mutations.
	 */
	private GraphQLDataFetchers graphQLDataFetchers;

	/**
	 * Nuestro objeto GraphQL que sera llamado en los request del graphQL
	 */
	private GraphQL graphQL;

	/**
	 * init del provider
	 *
	 * @param vertx
	 * @throws IOException
	 */
	public void init(Vertx vertx) throws IOException {
		graphQLDataFetchers = GraphQLDataFetchers.init(vertx);
		this.graphQL = GraphQL
				.newGraphQL(buildSchema(Resources.toString(Resources.getResource(SCHEMA_GRAPHQL), Charsets.UTF_8)))
				.build();
		LOGGER.info("graphQL: " + graphQL);
	}

	/**
	 * buildSchema
	 *
	 * @param sdl
	 * @return
	 */
	private GraphQLSchema buildSchema(String sdl) {
		return new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse(sdl), buildWiring());
	}

	/**
	 * Metodo donde se realiza la relacion entre los queries y mutations en el schema y las funciones con las
	 * que se resolveran, que se encuentran en el graphQLDataFetchers
	 *
	 * @return
	 */
	private RuntimeWiring buildWiring() {
		Map<String, DataFetcher> queries = new HashMap<>();
		queries.put("querySimple", graphQLDataFetchers.querySimple());

		Map<String, DataFetcher> mutations = new HashMap<>();
		mutations.put("guardarDeclaracion", graphQLDataFetchers.guardarDeclaracion());
		mutations.put("guardarAviso", graphQLDataFetchers.guardarAviso());
		mutations.put("guardarNotas", graphQLDataFetchers.guardarNotasAclaratorias());

		return RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring(QUERY).dataFetchers(queries))
				.type(newTypeWiring(MUTATION).dataFetchers(mutations))
				.build();
	}

	/**
	 * Metodo publico para obtener el objeto GraphQL
	 *
	 * @return el objeto graphQL
	 */
	public GraphQL graphQL() {
		return graphQL;
	}

}
