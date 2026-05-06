/**
 * @(#)GraphQLProvider.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.graphql;

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
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.decnet.util.TipoSchema;

/**
 * GraphqlProvider para relacionar queries y mutations en el esquema con funciones que se encuentran
 * en el graphQLDataFetcher
 *
 * @since 30/05/2019
 */
public class GraphQLProvider {

	/**
	 * Logger
	 */
	static final Logger LOGGER = LoggerFactory.getLogger(GraphQLProvider.class);

	/**
	 * Nombre del recurso de donde se tomara el schema de graphQL.
	 */
	private static final String SCHEMA_GRAPHQL_PUB = "schemaPublico.graphqls";

	/**
	 * Nombre del recurso de donde se tomara el schema de graphQL.
	 */
	private static final String SCHEMA_GRAPHQL_PRIV = "schemaPrivado.graphqls";

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
	public void init(Vertx vertx, int tipoSchema) throws IOException {
		graphQLDataFetchers = GraphQLDataFetchers.init(vertx);
		String schema = (tipoSchema==TipoSchema.PRIVADO)? SCHEMA_GRAPHQL_PRIV:SCHEMA_GRAPHQL_PUB;

		this.graphQL = GraphQL
				.newGraphQL(
						buildSchema(Resources.toString(Resources.getResource(schema), Charsets.UTF_8),
									tipoSchema))
				.build();
	}

	/**
	 * buildSchema
	 *
	 * @param sdl
	 * @return
	 */
	private GraphQLSchema buildSchema(String sdl, int tipoSchema) {
		if (tipoSchema == TipoSchema.PUBLICO) {
			return new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse(sdl), buildWiringPublico());
		}
		if (tipoSchema == TipoSchema.PRIVADO) {
			return new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse(sdl), buildWiringPrivado());
		}
		return null;
	}

	/**
	 * Metodo donde se realiza la relacion entre los queries y mutations en el schema y las funciones con las
	 * que se resolveran, que se encuentran en el graphQLDataFetchers
	 *
	 * @return
	 */
	private RuntimeWiring buildWiringPublico() {
		Map<String, DataFetcher> queries = new HashMap<>();
		queries.put("obtenerCatalogo", graphQLDataFetchers.obtenerCatalogo());
		queries.put("obtenerCatalogoActivo", graphQLDataFetchers.obtenerCatalogoActivo());
		queries.put("obtenerCatalogoActivoFk", graphQLDataFetchers.obtenerCatalogoActivoFk());
		queries.put("obtenerCatalogoPorId", graphQLDataFetchers.obtenerCatalogoPorId());
		queries.put("obtenerCatalogoPorIdFk", graphQLDataFetchers.obtenerCatalogoPorIdFk());
		queries.put("validarInfoCatalogo", graphQLDataFetchers.validarInfoCatalogo());

		return RuntimeWiring.newRuntimeWiring()
				.type(newTypeWiring(QUERY).dataFetchers(queries))
				.build();
	}

	/**
	 * Metodo donde se realiza la relacion entre los queries y mutations en el schema y las funciones con las
	 * que se resolveran, que se encuentran en el graphQLDataFetchers
	 *
	 * @return
	 */
	private RuntimeWiring buildWiringPrivado() {
		Map<String, DataFetcher> queries = new HashMap<>();
		queries.put("obtenerCatalogo", graphQLDataFetchers.obtenerCatalogo());
		queries.put("obtenerCatalogoActivo", graphQLDataFetchers.obtenerCatalogoActivo());
		queries.put("obtenerCatalogoActivoFk", graphQLDataFetchers.obtenerCatalogoActivoFk());
		queries.put("obtenerCatalogoPorId", graphQLDataFetchers.obtenerCatalogoPorId());
		queries.put("obtenerCatalogoPorIdFk", graphQLDataFetchers.obtenerCatalogoPorIdFk());
		queries.put("validarInfoCatalogo", graphQLDataFetchers.validarInfoCatalogo());

		Map<String, DataFetcher> mutations = new HashMap<String, DataFetcher>();
		mutations.put("agregarRegistro", graphQLDataFetchers.agregarRegistro());
		mutations.put("actualizarRegistro", graphQLDataFetchers.actualizarRegistro());

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
