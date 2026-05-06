/**
 * @(#)GraphQLProvider.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.graphql;

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
import mx.gob.sfp.dgti.omextback.util.constantes.EnumGraphQL;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

/**
 * GraphqlProvider para relacionar queries y mutations en el esquema con funciones que se encuentran
 * en el graphQLDataFetcher
 *
 * @author pavel.martinez
 * @since 19/05/2020
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
		queries.put(EnumGraphQL.BUSCAR_SERVIDORES.getValor(), graphQLDataFetchers.buscarServidores());
		queries.put(EnumGraphQL.OBTENER_SERVIDORES.getValor(), graphQLDataFetchers.obtenerServidores());
		queries.put(EnumGraphQL.OBTENER_INFO_GRAF.getValor(), graphQLDataFetchers.obtenerInformacionGraficas());
		queries.put(EnumGraphQL.OBTENER_INFO_INST.getValor(), graphQLDataFetchers.obtenerInformacionCumplimiento());
		queries.put(EnumGraphQL.OBTENER_INFO_UA.getValor(), graphQLDataFetchers.obtenerInformacionCumplimientoUA());
		queries.put(EnumGraphQL.GENERAR_REPORTE_SERV.getValor(), graphQLDataFetchers.generarReporteServ());
		queries.put(EnumGraphQL.GENERAR_IMPRESION_VISTA_POR_FOLIO.getValor(), graphQLDataFetchers.generarImpresionVistaPorFolio());
		queries.put(EnumGraphQL.OBTENER_INFO_GRUPO.getValor(), graphQLDataFetchers.obtenerInformacionGrupoCumplimiento());

		queries.put(EnumGraphQL.OBTENER_PERIODO.getValor(), graphQLDataFetchers.obtenerPeriodos());
		queries.put(EnumGraphQL.OBTENER_INST_PERIODO.getValor(), graphQLDataFetchers.obtenerInstPeriodo());
		queries.put(EnumGraphQL.OBTENER_VISTA.getValor(), graphQLDataFetchers.obtenerVistas());
		queries.put(EnumGraphQL.OBTENER_SERVIDORES_VISTA.getValor(), graphQLDataFetchers.obtenerServidoresVistas());
		queries.put(EnumGraphQL.OBTENER_VISTA_PENDIENTES_USUARIO.getValor(), graphQLDataFetchers.generarReporteServ());

		Map<String, DataFetcher> mutations = new HashMap<>();

		mutations.put(EnumGraphQL.EXTENDER_PERIODO.getValor(), graphQLDataFetchers.extenderPeriodo());
		mutations.put(EnumGraphQL.EXTENDER_PERIODO_INST.getValor(), graphQLDataFetchers.extenderPeriodoInstitucion());
		mutations.put(EnumGraphQL.GENERAR_VISTAS_OMISOS.getValor(),  graphQLDataFetchers.generarVistasOmisos());
		mutations.put(EnumGraphQL.GUARDAR_PERIODO.getValor(), graphQLDataFetchers.guardarPeriodo());
		mutations.put(EnumGraphQL.TERMINAR_PROCESO_VISTA.getValor(), graphQLDataFetchers.terminarProcesoVista());

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
