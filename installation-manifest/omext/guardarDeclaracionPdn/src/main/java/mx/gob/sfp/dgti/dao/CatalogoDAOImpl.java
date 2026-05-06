/**
 * ConsultaCatalogoDAOImpl.java Mar 31, 2020
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.dao;

import static mx.gob.sfp.dgti.util.Constantes.CATALOGO;
import static mx.gob.sfp.dgti.util.Constantes.CATORCE;
import static mx.gob.sfp.dgti.util.Constantes.CAT_DNET_DATABASE_HOST;
import static mx.gob.sfp.dgti.util.Constantes.CAT_DNET_DATABASE_NAME;
import static mx.gob.sfp.dgti.util.Constantes.CAT_DNET_DATABASE_PORT;
import static mx.gob.sfp.dgti.util.Constantes.CAT_DNET_DATABASE_PSW;
import static mx.gob.sfp.dgti.util.Constantes.CAT_DNET_DATABASE_USERNAME;
import static mx.gob.sfp.dgti.util.Constantes.CERO;
import static mx.gob.sfp.dgti.util.Constantes.CINCO;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_DATABASE;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_HOST;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PORT;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_PSW;
import static mx.gob.sfp.dgti.util.Constantes.CONFIG_USERNAME;
import static mx.gob.sfp.dgti.util.Constantes.CUATRO;
import static mx.gob.sfp.dgti.util.Constantes.DIECINUEVE;
import static mx.gob.sfp.dgti.util.Constantes.DIECIOCHO;
import static mx.gob.sfp.dgti.util.Constantes.DIECISEIS;
import static mx.gob.sfp.dgti.util.Constantes.DIECISIETE;
import static mx.gob.sfp.dgti.util.Constantes.DIEZ;
import static mx.gob.sfp.dgti.util.Constantes.DOCE;
import static mx.gob.sfp.dgti.util.Constantes.DOS;
import static mx.gob.sfp.dgti.util.Constantes.NUEVE;
import static mx.gob.sfp.dgti.util.Constantes.OCHO;
import static mx.gob.sfp.dgti.util.Constantes.ONCE;
import static mx.gob.sfp.dgti.util.Constantes.QUINCE;
import static mx.gob.sfp.dgti.util.Constantes.SEIS;
import static mx.gob.sfp.dgti.util.Constantes.SIETE;
import static mx.gob.sfp.dgti.util.Constantes.TRECE;
import static mx.gob.sfp.dgti.util.Constantes.TRES;
import static mx.gob.sfp.dgti.util.Constantes.UNO;
import static mx.gob.sfp.dgti.util.Constantes.VEINTE;
import static mx.gob.sfp.dgti.util.Constantes.VEINTIUNO;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.PostgreSQLClient;
import io.vertx.ext.sql.SQLClient;
import mx.gob.sfp.dgti.enums.CatalogosPdn;
import mx.gob.sfp.dgti.enums.Encabezados;

/**
 * @author Miriam Sanchez programador07
 * @since Mar 31, 2020
 */
public class CatalogoDAOImpl implements CatalogoDAO {

    protected static final Map<Integer, String> MAP_PAIS = new HashMap<>();
    protected static final Map<Integer, String> MAP_NIVEL_ACADEMICO = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_PARTICIPACION = new HashMap<>();
    protected static final Map<Integer, String> MAP_MONEDA = new HashMap<>();
    protected static final Map<Integer, String> MAP_ENTIDAD_FEDERATIVA = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_BIEN_INMUEBLE = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_BIEN_MUEBLE = new HashMap<>();
    protected static final Map<Integer, String> MAP_TITULAR = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_INVERSION = new HashMap<>();
    protected static final Map<Integer, String> MAP_SECTOR_PRIVADO = new HashMap<>();
    protected static final Map<Integer, String> MAP_SUBTIPO_INVERSION = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_INSTRUMENTO = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_RELACION_BIENES = new HashMap<>();
    protected static final Map<Integer, String> MAP_FORMA_ADQUISICION_BIEN = new HashMap<>();
    protected static final Map<Integer, String> MAP_MOTIVO_BAJA_BIEN = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_VEHICULO = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_ADEUDO = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_INSTITUCION = new HashMap<>();
    protected static final Map<Integer, String> MAP_BENEFICIARIO_PROGRAMA = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_APOYO = new HashMap<>();
    protected static final Map<Integer, String> MAP_TIPO_BENEFICIO = new HashMap<>();
    protected static final Map<Integer, String> MAP_MUNICIPIO_ALCALDIA = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoDAOImpl.class);

    private static final String CAT_DNET_HOST = System.getenv(CAT_DNET_DATABASE_HOST) != null ? System.getenv(CAT_DNET_DATABASE_HOST) : "localhost";
    private static final Integer CAT_DNET_PORT = System.getenv(CAT_DNET_DATABASE_PORT) != null ? Integer.parseInt(System.getenv(CAT_DNET_DATABASE_PORT)) : 5432;
    private static final String CAT_DNET_DATABASE = System.getenv(CAT_DNET_DATABASE_NAME) != null ? System.getenv(CAT_DNET_DATABASE_NAME) : "catalogo";
    private static final String CAT_DNET_USERNAME = System.getenv(CAT_DNET_DATABASE_USERNAME) != null ? System.getenv(CAT_DNET_DATABASE_USERNAME) : "catalogousr";
    private static final String CAT_DNET_PWD = System.getenv(CAT_DNET_DATABASE_PSW) != null ? System.getenv(CAT_DNET_DATABASE_PSW) : "password";
    private static final String SELECT = "SELECT ";
    private static final String SELECT_POR_CAT = "(SELECT ";
    private static final String CAMPOS = " json_agg(json_build_object('id', id, 'clavePdn', clave_pdn)) from ";
    private static final String CERRAR_QUERY = ")";
    private static final String UNION_ALL = " UNION ALL ";
    private static SQLClient postgreSQLt;

    /**
     * Constructor
     */
    public CatalogoDAOImpl() {

    }

    /**
     * Constructor
     *
     * @param vertx
     */
    public CatalogoDAOImpl(Vertx vertx) {
        postgreSQLt = PostgreSQLClient.createShared(vertx, getConfig());
    }

    @Override
    public Future<Void> obtenerCatalogos() {
        Future<Void> future = Future.future();

        postgreSQLt.queryWithParams(obtenerSqlCatalogo(), new JsonArray(), resultado -> {
            postgreSQLt.close();
            if (resultado.succeeded()) {
                llenarMaps(new JsonArray(resultado.result().getRows().get(CERO).getString(CATALOGO)), MAP_BENEFICIARIO_PROGRAMA);
                llenarMaps(new JsonArray(resultado.result().getRows().get(UNO).getString(CATALOGO)), MAP_ENTIDAD_FEDERATIVA);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DOS).getString(CATALOGO)), MAP_MONEDA);
                llenarMaps(new JsonArray(resultado.result().getRows().get(TRES).getString(CATALOGO)), MAP_MOTIVO_BAJA_BIEN);
                llenarMaps(new JsonArray(resultado.result().getRows().get(CUATRO).getString(CATALOGO)), MAP_MUNICIPIO_ALCALDIA);
                llenarMaps(new JsonArray(resultado.result().getRows().get(CINCO).getString(CATALOGO)), MAP_NIVEL_ACADEMICO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(SEIS).getString(CATALOGO)), MAP_FORMA_ADQUISICION_BIEN);
                llenarMaps(new JsonArray(resultado.result().getRows().get(SIETE).getString(CATALOGO)), MAP_PAIS);
                llenarMaps(new JsonArray(resultado.result().getRows().get(OCHO).getString(CATALOGO)), MAP_SECTOR_PRIVADO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(NUEVE).getString(CATALOGO)), MAP_SUBTIPO_INVERSION);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DIEZ).getString(CATALOGO)), MAP_TIPO_ADEUDO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(ONCE).getString(CATALOGO)), MAP_TIPO_APOYO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DOCE).getString(CATALOGO)), MAP_TIPO_BENEFICIO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(TRECE).getString(CATALOGO)), MAP_TIPO_BIEN_INMUEBLE);
                llenarMaps(new JsonArray(resultado.result().getRows().get(CATORCE).getString(CATALOGO)), MAP_TIPO_BIEN_MUEBLE);
                llenarMaps(new JsonArray(resultado.result().getRows().get(QUINCE).getString(CATALOGO)), MAP_TIPO_INVERSION);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DIECISEIS).getString(CATALOGO)), MAP_TIPO_INSTITUCION);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DIECISIETE).getString(CATALOGO)), MAP_TIPO_INSTRUMENTO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DIECIOCHO).getString(CATALOGO)), MAP_TIPO_PARTICIPACION);
                llenarMaps(new JsonArray(resultado.result().getRows().get(DIECINUEVE).getString(CATALOGO)), MAP_TIPO_RELACION_BIENES);
                llenarMaps(new JsonArray(resultado.result().getRows().get(VEINTE).getString(CATALOGO)), MAP_TIPO_VEHICULO);
                llenarMaps(new JsonArray(resultado.result().getRows().get(VEINTIUNO).getString(CATALOGO)), MAP_TITULAR);
                LOGGER.info("Catalogos cargados");
            } else {
                LOGGER.error(String.format("fail %s", resultado));
                future.fail(resultado.cause());
            }
        });
        return future;
    }

    private void llenarMaps(JsonArray array, Map<Integer, String> catalogo) {
        array.stream().forEach(r -> {
            JsonObject obj = (JsonObject) r;
            catalogo.put(obj.getInteger(Encabezados.ID.getCampo()), obj.getString(Encabezados.CLAVE_PDN.getCampo()));
        });
    }

    /**
     * @return
     */
    private static String obtenerSqlCatalogo() {
        StringBuilder sql = new StringBuilder(SELECT);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_BENEFICIARIO_PROGRAMA).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_ENTIDAD_FEDERATIVA).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_MONEDA).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_MOTIVO_BAJA_BIEN).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_MUNICIPIO_ALCALDIA).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_NIVEL_ACADEMICO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_FORMA_ADQUISICION_BIEN).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_PAIS).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_SECTOR_PRIVADO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_SUBTIPO_INVERSION).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_ADEUDO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_APOYO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_BENEFICIO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_BIEN_INMUEBLE).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_BIEN_MUEBLE).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_INVERSION).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_INSTITUCION).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_INSTRUMENTO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_PARTICIPACION).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_RELACION_BIENES).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TIPO_VEHICULO).append(CERRAR_QUERY).append(UNION_ALL);
        sql.append(SELECT_POR_CAT).append(CAMPOS).append(CatalogosPdn.CAT_TITULAR).append(CERRAR_QUERY);
        return sql.toString();
    }

    public static String obtenerClavePdnCat(CatalogosPdn catalogo, Integer id) {
        String clave = null;

        switch (catalogo) {

            case CAT_PAIS:
                clave = MAP_PAIS.get(id);
                break;

            case CAT_NIVEL_ACADEMICO:
                clave = MAP_NIVEL_ACADEMICO.get(id);
                break;

            case CAT_TIPO_PARTICIPACION:
                clave = MAP_TIPO_PARTICIPACION.get(id);
                break;

            case CAT_MONEDA:
                clave = MAP_MONEDA.get(id);
                break;

            case CAT_ENTIDAD_FEDERATIVA:
                clave = MAP_ENTIDAD_FEDERATIVA.get(id);
                break;

            case CAT_TIPO_BIEN_INMUEBLE:
                clave = MAP_TIPO_BIEN_INMUEBLE.get(id);
                break;

            case CAT_TIPO_BIEN_MUEBLE:
                clave = MAP_TIPO_BIEN_MUEBLE.get(id);
                break;

            case CAT_TIPO_INVERSION:
                clave = MAP_TIPO_INVERSION.get(id);
                break;

            case CAT_SECTOR_PRIVADO:
                clave = MAP_SECTOR_PRIVADO.get(id);
                break;

            case CAT_TIPO_INSTRUMENTO:
                clave = MAP_TIPO_INSTRUMENTO.get(id);
                break;

            case CAT_TIPO_RELACION_BIENES:
                clave = MAP_TIPO_RELACION_BIENES.get(id);
                break;

            case CAT_FORMA_ADQUISICION_BIEN:
                clave = MAP_FORMA_ADQUISICION_BIEN.get(id);
                break;

            case CAT_MOTIVO_BAJA_BIEN:
                clave = MAP_MOTIVO_BAJA_BIEN.get(id);
                break;

            case CAT_TIPO_VEHICULO:
                clave = MAP_TIPO_VEHICULO.get(id);
                break;

            case CAT_TIPO_ADEUDO:
                clave = MAP_TIPO_ADEUDO.get(id);
                break;

            case CAT_TIPO_INSTITUCION:
                clave = MAP_TIPO_INSTITUCION.get(id);
                break;

            case CAT_BENEFICIARIO_PROGRAMA:
                clave = MAP_BENEFICIARIO_PROGRAMA.get(id);
                break;

            case CAT_TIPO_APOYO:
                clave = MAP_TIPO_APOYO.get(id);
                break;

            case CAT_TIPO_BENEFICIO:
                clave = MAP_TIPO_BENEFICIO.get(id);
                break;

            case CAT_MUNICIPIO_ALCALDIA:
                clave = MAP_MUNICIPIO_ALCALDIA.get(id);
                break;

            case CAT_TITULAR:
                clave = MAP_TITULAR.get(id);
                break;

            case CAT_SUBTIPO_INVERSION:
                clave = MAP_SUBTIPO_INVERSION.get(id);
                break;
        }
        return clave;
    }

    /**
     * Se obtiene la configuracion de la base de datos
     *
     * @return JsonObject
     */
    private static JsonObject getConfig() {
        return new JsonObject()
                .put(CONFIG_HOST, CAT_DNET_HOST)
                .put(CONFIG_PORT, CAT_DNET_PORT)
                .put(CONFIG_DATABASE, CAT_DNET_DATABASE)
                .put(CONFIG_USERNAME, CAT_DNET_USERNAME)
                .put(CONFIG_PSW, CAT_DNET_PWD);
    }
}