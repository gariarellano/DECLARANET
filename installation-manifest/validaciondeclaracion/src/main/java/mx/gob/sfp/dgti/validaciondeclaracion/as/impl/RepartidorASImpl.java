/**
 * @(#)RepartidorASImpl.java 25/09/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.validaciondeclaracion.as.impl;

import io.reactivex.Single;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.validaciondeclaracion.as.RepartidorAS;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.LlamadoDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.dto.ServicioUrlDTO;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumCampos;
import mx.gob.sfp.dgti.validaciondeclaracion.util.EnumModulo;

import java.util.*;

/**
 * Clase para el servicio ReparidorASImpl
 *
 * @author Pavel Alexei Martinez Regalado aka programador02
 * @since 26/09/2019
 */
public class RepartidorASImpl implements RepartidorAS {

    /**
     * Palabra clave para identificar un servicio validador
     */
    private static final String LLAVE_RUTA = "RUTA";

    /**
     * Palabra clave para identificar el servicio validador de ids
     */
    private static final String LLAVE_RUTA_IDS = "RUTA_IDS";

    /**
     * El logger
     */
    static final Logger LOGGER = LoggerFactory.getLogger(RepartidorASImpl.class);

    /**
     * Mapa con los servicios disponibles
     */
    HashMap<String, ServicioUrlDTO> mapaServicios;

    /**
     * Servicio para validar urls contra base
     */
    private ServicioUrlDTO servicioIds = null;

    /**
     * Constructor
     *
     * Obtiene las variables de entorno y agrega todas las que contienen "RUTA" en
     * su nombre, las separa para obtener el servicio con la ruta y guardarlas en
     * listaServicioRuta. La estructura de las variables de entorno deben de tener
     * el formato:
     *
     * RUTA_<<NOMBRE_DE LA VARIABLE>>=<<NOMBRE_MODULO_SERVICIO>>|<<RUTA_ABSOLUTA_SERVICIO>>
     *
     * Ejemplo:
     *
     * RUTA_DATOS_GENERALES=datosGenerales|http://172.29.68.100:5001/
     *
     * @author Pavel Alexei Martinez Regalado aka programador02
     * @since 26/09/2019
     */
    public RepartidorASImpl() {

        Map<String, String> variablesEntorno = System.getenv();

        mapaServicios = new HashMap<>();

        variablesEntorno.forEach((llave, valor) -> {

            if(llave.contains(LLAVE_RUTA)) {
                String[] valores = valor.split("[|]");
                if(valores.length == 2) {

                    if (llave.contains(LLAVE_RUTA_IDS)) {
                        servicioIds =  new ServicioUrlDTO(EnumModulo.obtenerEnum(valores[0]), valores[1]);
                        LOGGER.info("=== Existe el validador de ids");
                    } else {
                        mapaServicios.put(valores[0], new ServicioUrlDTO(EnumModulo.obtenerEnum(valores[0]), valores[1]));
                        LOGGER.info(
                                "\n=== Existe el servicio: " + valores[0] + " en la url: " + valores[1]);
                    }
                }
            }
        });
        LOGGER.info("=== Los servicios existentes son: \n\n" + mapaServicios.size());
        mapaServicios.entrySet().forEach(servicioUrl ->
                LOGGER.info("\n=== Existe el servicio: "
                        .concat(servicioUrl.getKey())
                        .concat(" en la url: ")
                        .concat(servicioUrl.getValue().getUrl())));
        LOGGER.info(mapaServicios);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<LlamadoDTO>> repartirDeclaracion(JsonObject declaracionCompleta, Set<String> mandados) {

        //Obtener el objeto de la declaracion
        JsonObject declaracion = declaracionCompleta.getJsonObject(EnumCampos.DECLARACION.getNombre());

        //Obtener el objeto del encabezado y crear nuestro propio objeto con los campos que necesitamos
        JsonObject encabezado = new JsonObject();
        JsonObject declaracionConEncabezados = new JsonObject(declaracion.toString());

        //Se toman los valores del encabezado
        String tipoDeclaracion = declaracionCompleta.getJsonObject(
                EnumCampos.ENCABEZADO.getNombre()).getString(EnumCampos.TIPO_DECLARACION.getNombre());
        Integer anio = declaracionCompleta.getJsonObject(
                EnumCampos.ENCABEZADO.getNombre()).getInteger(EnumCampos.ANIO.getNombre());
        String tipoFormato = declaracionCompleta.getJsonObject(
                EnumCampos.ENCABEZADO.getNombre()).getString(EnumCampos.TIPO_FORMATO.getNombre());

        JsonObject nivelJerarquicoActual = new JsonObject(
                declaracionCompleta
                        .getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                        .getJsonObject(EnumCampos.NIVEL_JERARQUICO.getNombre())
                        .toString()
        );
        //Para el caso de datos de ingreso, es necesario enviar tambien los bienes si se tiene el formato completo,
        //sino solo se envia solo los ingresos.
        JsonObject datosIngresos = new JsonObject().put(
                        EnumModulo.I_INGRESOS_NETOS.getModulo(),
                        declaracionConEncabezados.getValue(EnumModulo.I_INGRESOS_NETOS.getModulo()));
        declaracionConEncabezados.put(
                EnumModulo.I_INGRESOS_NETOS.getModulo(), datosIngresos);

        //Agregar datos de encabezado a lo que se manda a validadores
        encabezado.put(EnumCampos.ANIO.getNombre(), anio);
        if (tipoDeclaracion.equals(EnumTipoDeclaracion.MODIFICACION.name())) {
            encabezado.put(EnumCampos.FECHA_ENCARGO.getNombre(), anio + "-01-01");
        } else {
            encabezado.put(EnumCampos.FECHA_ENCARGO.getNombre(),
                    declaracionCompleta.getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                            .getValue(EnumCampos.FECHA_ENCARGO.getNombre()));
        }
        encabezado.put(EnumCampos.NUMERO_DECLARACION.getNombre(),
                declaracionCompleta.getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                        .getValue(EnumCampos.NUMERO_DECLARACION.getNombre()));
        encabezado.put(EnumCampos.TIPO_DECLARACION.getNombre(), tipoDeclaracion);
        encabezado.put(EnumCampos.TIPO_FORMATO.getNombre(), tipoFormato);
        encabezado.put(EnumCampos.NIVEL_JERARQUICO.getNombre(), nivelJerarquicoActual);

        //Se crea una lista con el objeto de
        List<LlamadoDTO> declaracionRepartida = new ArrayList<>();

        //Se agrega el servicio de validacion de ids
        if(servicioIds != null) {

            declaracionRepartida.add(
                    new LlamadoDTO(
                            servicioIds.getServicio(),
                            servicioIds.getUrl(),
                            declaracionCompleta
                    ));
        }

        //Se agregan servicios de validacion de modulos
        mandados.forEach(servicioUrl -> {
            if(mapaServicios.get(servicioUrl) == null) {
                LOGGER.info("=== Se llama a un servicio no disponible o error al cargar servicios por " +
                        "variables de entorno");
            }
            if (declaracion.getJsonObject(servicioUrl)!= null) {

                JsonObject infoMandar = declaracionConEncabezados.getJsonObject(servicioUrl);

                //En caso de ingresos netos se tiene que enviar bienes muebles, bienes inmuebles y vehiculos
                if(servicioUrl.equals(EnumModulo.I_INGRESOS_NETOS.getModulo())) {
                    infoMandar
                        .getJsonObject(EnumModulo.I_INGRESOS_NETOS.getModulo())
                        .put(EnumCampos.ENCABEZADO.getNombre(), encabezado);

                //Todos los demas casos
                } else {
                    infoMandar.put(EnumCampos.ENCABEZADO.getNombre(), encabezado);
                }
                declaracionRepartida.add(
                        new LlamadoDTO(
                                mapaServicios.get(servicioUrl).getServicio(),
                                mapaServicios.get(servicioUrl).getUrl(),
                                infoMandar
                        ));
            }
        });

        return Single.just(declaracionRepartida);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<List<LlamadoDTO>> repartirAvisoCambio(JsonObject avisoCambioCompleta, Set<String> mandados) {

        //Obtener el objeto de la declaracion
        JsonObject avisoCambio = avisoCambioCompleta.getJsonObject(EnumCampos.DECLARACION.getNombre());

        //Obtener el objeto del encabezado y crear nuestro propio objeto con los campos que necesitamos
        JsonObject encabezado = new JsonObject();
        JsonObject avisoConEncabezados = new JsonObject(avisoCambio.toString());

        JsonObject nivelJerarquicoActual = new JsonObject(
                avisoCambioCompleta
                        .getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                        .getJsonObject(EnumCampos.NIVEL_JERARQUICO.getNombre())
                        .toString());

        encabezado.put(EnumCampos.NIVEL_JERARQUICO.getNombre(), nivelJerarquicoActual);

        //Agregar datos de encabezado a lo que se manda a validadores
        encabezado.put(EnumCampos.FECHA_ENCARGO.getNombre(),
                avisoCambioCompleta.getJsonObject(EnumCampos.ENCABEZADO.getNombre())
                        .getValue(EnumCampos.FECHA_ENCARGO.getNombre()));

        //Se crea una lista con el objeto de
        List<LlamadoDTO> avisoRepartido = new ArrayList<>();

        //Se agrega el servicio de validacion de ids
        if(servicioIds != null) {

            avisoRepartido.add(
                    new LlamadoDTO(
                            servicioIds.getServicio(),
                            servicioIds.getUrl(),
                            avisoCambioCompleta
                    ));
        }

        //Se agregan servicios de validacion de modulos
        mandados.forEach(servicioUrl -> {
            if(mapaServicios.get(servicioUrl) == null) {
                LOGGER.info("=== Se llama a un servicio no disponible o error al cargar servicios por " +
                        "variables de entorno");
            }
            if (avisoConEncabezados.getJsonObject(servicioUrl)!= null) {

                JsonObject infoMandar = avisoConEncabezados.
                        getJsonObject(servicioUrl).put(EnumCampos.ENCABEZADO.getNombre(), encabezado);

                avisoRepartido.add(
                        new LlamadoDTO(
                                mapaServicios.get(servicioUrl).getServicio(),
                                mapaServicios.get(servicioUrl).getUrl(),
                                infoMandar
                        ));
            }
        });

        return Single.just(avisoRepartido);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Single<LlamadoDTO> repartirNotas(JsonObject notasCompleto) {

        //Obtener el objeto de la declaracion
        JsonObject notas = notasCompleto.getJsonObject(EnumCampos.DECLARACION.getNombre());

        //Obtener el objeto del encabezado y crear nuestro propio objeto con los campos que necesitamos

        LlamadoDTO llamado;

        if (mapaServicios.get(EnumModulo.NOTAS_ACLARATORIAS.getModulo()) != null) {
            llamado = new LlamadoDTO(
                    mapaServicios.get(EnumModulo.NOTAS_ACLARATORIAS.getModulo()).getServicio(),
                    mapaServicios.get(EnumModulo.NOTAS_ACLARATORIAS.getModulo()).getUrl(),
                    notas
            );
        } else {
            LOGGER.info("=== Se llama a un servicio no disponible o error al cargar servicios por " +
                    "variables de entorno");
            return Single.error(new Exception("=== Servicio no disponible-"));
        }

        return Single.just(llamado);
    }

}
