/**
 * @(#)EnumError.java 03/06/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.util.constantes;

import io.netty.handler.codec.http.HttpResponseStatus;

/**
 * Enum con los posibles erroresEnumError
 *
 * @author pavel.martinez
 * @since 03/06/2020
 */
public enum EnumError {
    ERROR_GENERAL(-1, "Ocurrio un error", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()),
    ERROR_TIME_OUT(1, "Se excedio el tiempo maximo de espera.", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()),
    ERROR_GRAPHQL(2, "Error de GraphQL por el incumplimiento del schema.", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_CONSULTA_MONGODB(3, "Error al realizar una consulta en MongoDB.", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()),
    ERROR_CONSULTA_EXT_GRAPHQL(4, "Error al hacer una consulta externa a servicio de GraphQL.", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()),
    ERROR_PARAMETROS_INCORRECTOS(5, "Error por los parametros mandados al servicio.", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_CONSULTA_HTTP(6, "Error al hacer una consulta http", HttpResponseStatus.INTERNAL_SERVER_ERROR.code()),
    ERROR_PERIODO_NO_EXISTE(7, "Error no existe el periodo", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_VISTA_PERIODO_YA_GENERADAS(8, "Las vistas para este periodo ya han sido generadas", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_FECHA_LIMITE_NO_SUPERADA(9, "El o las fechas límites para generar las vistas de este periodo no han vencido", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_VISTA_NO_EXISTE(10, "Error, no existe la vista", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_VISTA_NO_GENERADA(11, "Error, la vista no ha sido generada", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_NO_ES_POSIBLE_FIRMAR_VISTA(12, "Error, la vista no se puede firmar", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_NO_REGISTROS_PARA_GENERAR_VISTA(13, "Error, no es posible generar vista por que no hay registros", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_COLLNAME_NO_DISPONIBLE(14, "Error, no es posible acceder al collname.", HttpResponseStatus.BAD_REQUEST.code()),
    ERROR_INSTITUCIONES_NO_VALIDAS(15, "Error, instituciones no validas.", HttpResponseStatus.BAD_REQUEST.code())
    ;

    /**
     * identificador del error
     */
    private final Integer id;

    /**
     * Descripcion del error
     */
    private final String descripcion;

    /**
     * Estatus con el que se debe mandar
     */
    private final Integer statusHttp;

    EnumError(Integer id, String descripcion, Integer statusHttp) {
        this.id = id;
        this.descripcion = descripcion;
        this.statusHttp = statusHttp;
    }

    public final Integer getId() {
        return id;
    }

    public final String getDescripcion() {
        return descripcion;
    }

    public final Integer getStatusHttp() {
        return statusHttp;
    }

}
