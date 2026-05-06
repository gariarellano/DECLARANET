/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.utils;

import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.ext.web.client.WebClient;
import mx.gob.sfp.dgti.clienteweb.EnumCliente;
import mx.gob.sfp.dgti.clienteweb.LlamadoDTO;
import mx.gob.sfp.dgti.clienteweb.LlamadorServicios;
import mx.gob.sfp.dgti.declaracion.dto.individual.CatalogoDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumEstatusEscolaridad;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelAcademico;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDocumento;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErrorMensajesDTO;
import mx.gob.sfp.dgti.validador.dto.out.ErroresDTO;
import mx.gob.sfp.dgti.validador.dto.out.ModuloDTO;
import mx.gob.sfp.dgti.validador.dto.out.PropiedadesErrorDTO;
import mx.gob.sfp.dgti.validador.enu.EnumMensaje;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Clase que permite ejecutar las validaciones a realizar 
 * a todas las propiedades que se pretenden validar.
 * 
 * @author cgarias
 * @since 25/09/2019
 */
public class ExectValidations {
    
    /**
     * Propiedad que permite escribir en consola para visualizar valores.
     */
    private static final Logger logger = Logger.getLogger(ExectValidations.class.getName());
    
    private static final String DOS_PUNTOS = ":";

	private static final String EMPTY = "";
	
	private static final String COMA = ", ";

    /**
     * WebCLient
     */
    private WebClient client;

    /**
     * Url del servicio de catalogos
     */
    private String catalogosUrl;

    public ExectValidations() {
    }

    public ExectValidations(WebClient client, String catalogosUrl) {
        this.client = client;
        this.catalogosUrl = catalogosUrl;
    }

    /**
     * Método que recibe un objeto de ErroresDTO inicializado, en el cual se
     * asignaran los errores detectados en las validaciones.
     * 
     * @param erroresDto Objeto que se le asiganaran los errores de validaciones. 
     * @param dtoModuloValidar Objeto lleno de valores y validaciones a realizar a las propiedades.
     */
    public void ejecutaValidaciones(ErroresDTO erroresDto, ModuloValidarDTO dtoModuloValidar){
        erroresDto.setNombreModulo(dtoModuloValidar.getNombreModulo());
        if (!dtoModuloValidar.getListModuloshijos().isEmpty()){
            CompletableFuture<?>[] futuresComp = new CompletableFuture[dtoModuloValidar.getListModuloshijos().size()];
            int x = 0;
            for(ModuloValidarDTO index : dtoModuloValidar.getListModuloshijos()){
                ErroresDTO erroresDtoHijo = new ErroresDTO();    
                futuresComp[x++]= CompletableFuture.runAsync(() -> {
                    ejecutaValidaciones(erroresDtoHijo, index);
                    if (!erroresDtoHijo.getListErorres().isEmpty() || !erroresDtoHijo.getListModHijos().isEmpty() )
                        erroresDto.getListModHijos().add(erroresDtoHijo);
                });
            }
            CompletableFuture.allOf(futuresComp).join();   
        }
        for (PropiedadesValidarDTO indexPropi : dtoModuloValidar.getListPropsTovalidate()){
            PropiedadesErrorDTO propErrorDto = new PropiedadesErrorDTO(indexPropi.getNombreCampo(),indexPropi.getPropiedadValidar());
            if (indexPropi.isObligatorio() && indexPropi.getPropiedadValidar() == null){            
                propErrorDto.getListErrorMensajes().add(new ErrorMensajesDTO(0, EnumMensaje.OBJETO_DATO_OBLIGATORIO.getMensaje()));
            }else{
                if (!indexPropi.isObligatorio() && indexPropi.getPropiedadValidar() == null)
                    continue;
                if (!indexPropi.getListValToExec().isEmpty()){
                    CompletableFuture<?>[] futuresComp = new CompletableFuture[indexPropi.getListValToExec().size()];
                    int x = 0;
                    for(ParametrosValicacionDTO index:  indexPropi.getListValToExec()){
                        futuresComp[x++] = CompletableFuture.runAsync(() -> {
                            realizaValidacion(propErrorDto.getListErrorMensajes(), indexPropi.getPropiedadValidar(), index);
                        });
                    }
                    CompletableFuture.allOf(futuresComp).join();   
                }
            }
            if (!propErrorDto.getListErrorMensajes().isEmpty())
                erroresDto.getListErorres().add(propErrorDto);
        }
    }

    /**
     * Metodo reactivo para ejecutar las validaciones
     *
     * @param moduloValidarDto
     * @param moduloDto
     *
     * @return Observable con los errores, modifica el onjeto de errores que entra como parametor
     *
     * @author pavel.martinez
     * @since 21/12/2019
     */
    public Observable ejecutarValidacionesRx(ModuloValidarDTO moduloValidarDto, ModuloDTO moduloDto){

        return validarModuloRx(moduloValidarDto, moduloDto.getErrores(), new StringBuilder());

    }

    /**
     * Método que realiza la validacion de los módulos y va agregando las validaciones realizadas de cada 
     * propiedad, en caso de tener un error el valor de un campo,
     * 
     * @param moduloValidarDto Objeto que coontiene un 
     * @param moduloDto 
     */
    public void ejecutaValidaciones(ModuloValidarDTO moduloValidarDto, ModuloDTO moduloDto){
        this.ejecutaValidaciones(moduloValidarDto, moduloDto.getErrores(), new StringBuilder());
    }

    /**
     * Método que recibe un objeto de ErroresDTO inicializado, en el cual se
     * asignaran los errores detectados en las validaciones.
     * 
     * @param listPropiedadErroresDto Lista que se le asignaran los errores de validaciones y los errores con etiqueta en campo. 
     * @param moduloValidarDto Objeto lleno de valores y validaciones a realizar a las propiedades.
     */
    private void ejecutaValidaciones(ModuloValidarDTO moduloValidarDto, List<PropiedadesErrorDTO> listPropiedadErroresDto, StringBuilder campos){
        campos.append(moduloValidarDto.getNombreModulo()).append(moduloValidarDto.getPosicion() == null?"":"["+ moduloValidarDto.getPosicion()+"]").append(DOS_PUNTOS);
        if (!moduloValidarDto.getListModuloshijos().isEmpty()){
            for(ModuloValidarDTO index : moduloValidarDto.getListModuloshijos()){
                    ejecutaValidaciones(index, listPropiedadErroresDto, campos);
            }
        }
        for (PropiedadesValidarDTO indexPropi : moduloValidarDto.getListPropsTovalidate()){
            PropiedadesErrorDTO propErrorDto = new PropiedadesErrorDTO(
                   campos.toString()
                                .concat(indexPropi.getNombreCampo()),
                                //.concat(DOS_PUNTOS)
                                //.concat(indexPropi.getPosicionFila().toString())
                                //.concat(DOS_PUNTOS)+indexPropi.getPropiedadValidar(),
                    indexPropi.getPropiedadValidar());
            if (indexPropi.isObligatorio() && indexPropi.getPropiedadValidar() == null){            
                propErrorDto.getListErrorMensajes().add(new ErrorMensajesDTO(0, EnumMensaje.OBJETO_DATO_OBLIGATORIO.getMensaje()));
            }else{
                if (!indexPropi.isObligatorio() && indexPropi.getPropiedadValidar() == null)
                    continue;
                if(!indexPropi.getListValToExec().isEmpty()){
                    CompletableFuture<?>[] futuresComp = new CompletableFuture[indexPropi.getListValToExec().size()];
                    int x = 0;
                    for(ParametrosValicacionDTO index :indexPropi.getListValToExec()){
                        futuresComp[x++]=CompletableFuture.runAsync(() -> {
                            realizaValidacion(propErrorDto.getListErrorMensajes(), indexPropi.getPropiedadValidar(), index);
                        });
                    };
                    CompletableFuture.allOf(futuresComp).join();   
                }
            }
            if (!propErrorDto.getListErrorMensajes().isEmpty())
                listPropiedadErroresDto.add(propErrorDto);
        }
        campos.delete(campos.length()-moduloValidarDto.getNombreModulo().length()-1, campos.length());
    }

    /**
     * Metodo reactivo y recursivo que recorre los módulos
     *
     * @param moduloValidarDto modulo a validar
     * @param listPropiedadErroresDto lista de propiedades
     * @param campos campos que se van concatenando
     *
     * @return Observable, solo importa es que se complete, como si fuera void
     *
     * @author pavel.martinez
     * @since 24/11/2019
     */
    public Observable validarModuloRx(ModuloValidarDTO moduloValidarDto,
                                      List<PropiedadesErrorDTO> listPropiedadErroresDto, StringBuilder campos) {

        StringBuilder camposNuevo = new StringBuilder(campos.toString());

        camposNuevo.append(moduloValidarDto.getNombreModulo()).append(moduloValidarDto.getPosicion() == null?
                "":"["+ moduloValidarDto.getPosicion()+"]").append(DOS_PUNTOS);


        if(moduloValidarDto.getListModuloshijos().isEmpty()) {
            if(moduloValidarDto.getListPropsTovalidate().isEmpty()) {

                return Observable.just(listPropiedadErroresDto);

            } else {
                return validarPropiedadesRx(moduloValidarDto, listPropiedadErroresDto, camposNuevo);
            }
        } else {
            if(moduloValidarDto.getListPropsTovalidate().isEmpty()) {
                return  Observable.fromIterable(moduloValidarDto.getListModuloshijos())
                        .flatMap(modulo ->  validarModuloRx(modulo, listPropiedadErroresDto, camposNuevo));
            } else {
                return Observable.merge(
                        validarPropiedadesRx(moduloValidarDto, listPropiedadErroresDto, camposNuevo),
                        Observable.fromIterable(moduloValidarDto.getListModuloshijos())
                                .flatMap(modulo -> validarModuloRx(modulo, listPropiedadErroresDto, camposNuevo))
                );
            }
        }

    }

    /**
     * Metodo reactivo y recursivo que recorre las propiedades
     *
     * @param moduloValidarDto modulo a validar
     * @param listPropiedadErroresDto lista de propiedades
     * @param campos campos que se van concatenando
     *
     * @return Observable, solo importa es que se complete, como si fuera void
     *
     * @author pavel.martinez
     * @since 24/11/2019
     */
    public Observable validarPropiedadesRx(ModuloValidarDTO moduloValidarDto,
                                           List<PropiedadesErrorDTO> listPropiedadErroresDto, StringBuilder campos) {

            StringBuilder camposNuevo = new StringBuilder(campos.toString());

            if (moduloValidarDto.getListPropsTovalidate() == null || moduloValidarDto.getListPropsTovalidate().isEmpty()) {
                return Observable.empty();
            }

            return Observable.fromIterable(moduloValidarDto.getListPropsTovalidate())
                    .flatMap(propiedad -> {

                        PropiedadesErrorDTO propErrorDto = new PropiedadesErrorDTO(
                                camposNuevo.toString()
                                        .concat(propiedad.getNombreCampo()),
                                propiedad.getPropiedadValidar());
                        if (propiedad.isObligatorio() && propiedad.getPropiedadValidar() == null) {

                            propErrorDto.getListErrorMensajes().add(
                                    new ErrorMensajesDTO(0, EnumMensaje.OBJETO_DATO_OBLIGATORIO.getMensaje()));
                            listPropiedadErroresDto.add(propErrorDto);
                            return Observable.empty();
                        } else {
                            if (!propiedad.isObligatorio() && propiedad.getPropiedadValidar() == null) {
                                return Observable.empty();
                            }
                            if (!propiedad.getListValToExec().isEmpty()) {

                                return Observable.fromIterable(propiedad.getListValToExec())
                                        .flatMap(validacion -> realizaValidacionRx(
                                                propErrorDto.getListErrorMensajes(),
                                                propiedad.getPropiedadValidar(),
                                                validacion)
                                                ).doOnComplete(() -> {
                                                    if (!propErrorDto.getListErrorMensajes().isEmpty()) {
                                                        listPropiedadErroresDto.add(propErrorDto);
                                                    }
                                                });
                            } else {
                                return Observable.empty();
                            }

                        }
                    });

    }

    /**
     * Método que realizara las validaciones a los objetos.
     *
     * @param listErrorMesaje    Lista de objetos que contienen los errores encontrados.
     * @param propiedad propiedad a validar.
     * @param dtoParametrosValidacion Objeto que contine las validaciones a ejecutar par la propiedad.
     */
    private Observable realizaValidacionRx(List<ErrorMensajesDTO> listErrorMesaje, Object propiedad, ParametrosValicacionDTO dtoParametrosValidacion){
//        logger.log(Level.INFO, "A validar ({0}) con ({1})", new Object[]{propiedad,dtoParametrosValidacion.getEnumValidacion().getEnumTipoValidacion().name()});

        if(dtoParametrosValidacion.getEnumValidacion().getEnumTipoValidacion().getIdValidacion() == 200) {
            CatalogoDTO catalogo = (CatalogoDTO) propiedad;
            return validarCatalogoRx(catalogo, listErrorMesaje, dtoParametrosValidacion);
        }

        switch(dtoParametrosValidacion.getEnumValidacion().getEnumTipoValidacion().getIdValidacion()){
            case 0 :
                if (!Pattern.compile(dtoParametrosValidacion.getEnumValidacion().getValidador()).matcher(propiedad.toString()).matches())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                break;
            case 10 ://TIPO_LENGTH_IGUAL
                if (propiedad.toString().length()!=((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 11 ://TIPO_LENGTH_NO_IGUAL
                if (propiedad.toString().length()==((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 12 ://TIPO_LENGTH_MAYOR_QUE
                if (propiedad.toString().length()<=((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 13 ://TIPO_LENGTH_MENOR_QUE
                if (propiedad.toString().length()>=((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;

            case 14 ://TIPO_LENGTH_MENORQUE_Y_MAYORQUE
                if (propiedad.toString().length()<((Integer)dtoParametrosValidacion.getPriValToEval()) || propiedad.toString().length()>((Integer)dtoParametrosValidacion.getSegValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                break;
            case 20 :
                if (!Objects.equals((Integer)propiedad, (Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 21 :
                if ((Integer.parseInt(propiedad.toString()))<=(Integer)dtoParametrosValidacion.getPriValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 22 :
                if (((Integer.parseInt(propiedad.toString()))) >= (Integer)dtoParametrosValidacion.getPriValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 23 :
                if (((Integer)propiedad) <= (Integer)dtoParametrosValidacion.getPriValToEval() ||
                        ((Integer)propiedad) >= (Integer)dtoParametrosValidacion.getSegValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                break;
            case 24://valida que el objeto sea integer
                if(!(propiedad instanceof Integer)) {
                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                }
                break;
            case 25: // Numero entre 2 rangos mayor o igual a y menor o igual a
                if (((Integer)propiedad) > (Integer)dtoParametrosValidacion.getPriValToEval() || 
                        ((Integer)propiedad) < (Integer)dtoParametrosValidacion.getSegValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                break;
            case 26 :
                if (!Objects.equals((Long)propiedad, (Long)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),propiedad.toString())));
                break;
            case 27 :
                if ((Integer.parseInt(propiedad.toString())) <=(Integer)dtoParametrosValidacion.getPriValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 28 :
                if ((Integer.parseInt(propiedad.toString())) >= Integer.parseInt(dtoParametrosValidacion.getPriValToEval().toString()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;

            case 34 :
                if (!propiedad.toString().equals(dtoParametrosValidacion.getPriValToEval().toString()))//CADENA_IGUAL_A
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 35 :
                if (propiedad.toString().equals(dtoParametrosValidacion.getPriValToEval().toString()))//CADENA_NO_IGUAL_A
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 36 : //TIPO_TEXTO_UNO_U_OTRO
                if ((dtoParametrosValidacion.getPriValToEval() == null || EMPTY.equals(dtoParametrosValidacion.getPriValToEval().toString()))
                        && (dtoParametrosValidacion.getSegValToEval() == null || EMPTY.equals(dtoParametrosValidacion.getSegValToEval().toString())))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                break;
            case 40 ://TIPO_FECHA_MAYORQUE
            	if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
            			&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
	            	if(!FechaUtil.fechaMayorQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
	                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
            	} else {
            		listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
            	}
            	break;
            case 41 ://TIPO_FECHA_MENORQUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMenorQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                } else {
                    logger.info("Formato erroneo");
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
                }
                break;
            case 42 ://TIPO_FECHA_MENORQUE_Y_MAYORQUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getSegValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMayorMenorQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString() + COMA + dtoParametrosValidacion.getSegValToEval().toString()));
                }
                break;
            case 43 ://TIPO_FECHA_MENORIGUAL_MAYORIGUAL_QUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getSegValToEval().toString()).matches()) {
                    if(!FechaUtil.mayorOIgualQueMenorOIgualQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString() + COMA + dtoParametrosValidacion.getSegValToEval().toString()));
                }
                break;
            case 44 ://TIPO_FECHA_MAYORIGUAL_QUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMayorIgualQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
                }
                break;
            case 45 ://TIPO_FECHA_MENORIGUAL_QUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMenorIgualQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
                }
                break;
            case 90: //SOLO UNO DEBE SER NO NULO

                if(dtoParametrosValidacion.getPriValToEval() != null ) {
                    Object[] objeto = (Object[]) dtoParametrosValidacion.getPriValToEval();

                    if (Arrays.stream(objeto).filter(o -> o != null).count() != 1) {

                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                    }
                }
                break;
            case 91://LISTA Y OBJETOS DEBE DE SER NULA O VACIA SI ES LISTA
                if(dtoParametrosValidacion.getPriValToEval() != null) {

                    if(dtoParametrosValidacion.getPriValToEval() instanceof List) {
                        List<Object> lista = (List<Object>) dtoParametrosValidacion.getPriValToEval();
                        if (!lista.isEmpty()) {
                            listErrorMesaje.add(new ErrorMensajesDTO(
                                    dtoParametrosValidacion.getEnumValidacion().getId(),
                                    dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                        }
                    } else {
                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                    }
                }
                break;
            case 92://OBJETO DEBE DE SER NO NULA Y NO VACIA SI ES LISTA

                if(dtoParametrosValidacion.getPriValToEval() == null) {

                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));

                } else {
                    if(dtoParametrosValidacion.getPriValToEval() instanceof List) {
                        List<Object> lista = (List<Object>) dtoParametrosValidacion.getPriValToEval();

                        if (lista.isEmpty()) {
                            listErrorMesaje.add(new ErrorMensajesDTO(
                                    dtoParametrosValidacion.getEnumValidacion().getId(),
                                    dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                        }
                    }
                }
                break;

            case 95://Validacion de Objeto u propiedad, debe ser nula en priValToEval == null
                if (dtoParametrosValidacion.getPriValToEval() != null){
                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                }
                break;
            case 96://AL MENOS 1 OBJETO O PROPIEDAD NO DEBE VENIR null
                if(dtoParametrosValidacion.getPriValToEval() != null ) {
                    Object[] objeto = (Object[]) dtoParametrosValidacion.getPriValToEval();
                    if (Arrays.stream(objeto).filter(o -> o != null).count() == 0) {
                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                    }
                }
                break;
            case 97://VALIDA QUE EL AÑO A PRESENTAR COINCIDA CON LA FECHA ENCARGO
                if(propiedad == null ||dtoParametrosValidacion.getPriValToEval()== null || !FechaUtil.validarFechaEncargo( (String)dtoParametrosValidacion.getPriValToEval(), (Integer)propiedad) ) {
                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                }
                break;
            case 110: // Validacion para el documento del modulo de datos curriculares
                if(validarNivelAcademico(propiedad.toString(), dtoParametrosValidacion.getPriValToEval(), dtoParametrosValidacion.getSegValToEval())) {
                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                }
                break;
            case 120: // Opción para agregar mensaje cuando el tipo de declaracion no aplica en un módulo
                listErrorMesaje.add(new ErrorMensajesDTO(
                        dtoParametrosValidacion.getEnumValidacion().getId(), String.format(
                        dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 130:
                try{
                    Enum.valueOf((Class) dtoParametrosValidacion.getPriValToEval(), propiedad.toString());
                }catch(IllegalArgumentException e){
//                    logger.log(Level.WARNING, "{0}",e);
                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(), String.format(
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString())));
                }
                break;
        }
        return Observable.empty();

    }

    /**
     * Metodo reactivo y recursivo que recorre modulos y propiedades
     *
     * @param catalogo: catalogo que se va
     *
     * @return Observable, solo importa es que se complete, como si fuera void
     *
     * @author pavel.martinez
     * @since 24/11/2019
     */
    public Observable validarCatalogoRx(CatalogoDTO catalogo, List<ErrorMensajesDTO> listErrorMesaje,
                                        ParametrosValicacionDTO dtoParametrosValidacion) {

        String nombreCatalogo = (String) dtoParametrosValidacion.getPriValToEval();

        JsonObject query = new JsonObject()
                .put(EnumCliente.CATALOGO.getValor(), nombreCatalogo)
                .put(EnumCliente.INFO.getValor(), catalogo.toJson());

        LlamadoDTO llamado = new LlamadoDTO();
        llamado.setNombreServicio(nombreCatalogo);
        llamado.setUrlAbs(catalogosUrl);
        llamado.setInfo(query);


        return new LlamadorServicios().llamarServicio(client, llamado)
                .flatMap( respuesta -> {
                    if(respuesta != null && respuesta.getString(EnumCliente.ESTADO.getValor()) != null) {
                        if(respuesta.getString(EnumCliente.ESTADO.getValor()).equals(EnumCliente.RESPUESTA_CORRECTA.getValor())
                                && !respuesta.getBoolean(EnumCliente.RESPUESTA.getValor())) {
                            listErrorMesaje.add(new ErrorMensajesDTO(
                                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                                EnumMensaje.CATALOGO.getMensaje()));
                        }
                    }else {
                        logger.log(Level.SEVERE, "Las validacion de catalogos no se pudo realizar!. {0}", llamado.toString());
                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                EnumMensaje.CATALOGO_IMPOSIBLE_VALIDAR.getMensaje()));
                    }
                    return Observable.empty();
                }).doOnError(
                        e -> {
                            listErrorMesaje.add(new ErrorMensajesDTO(
                                    dtoParametrosValidacion.getEnumValidacion().getId(),
                                    EnumMensaje.CATALOGO_IMPOSIBLE_VALIDAR.getMensaje()));
                        }
                );
    }

    /**
     * Método que recibe parametros a evaluar para obtener todas
     * las evaluaciuones como un solo arreglo de errores.
     * 
     * @param listPropiedadesValidar Lista de propiedades a evaluar.
     * @param listPropiedadesError Lista de propiedades evaluadas y en caso de error
     */
    public void ejecutaValidacioes(List<PropiedadesValidarDTO> listPropiedadesValidar, List<PropiedadesErrorDTO> listPropiedadesError){
        for (PropiedadesValidarDTO indexPropi : listPropiedadesValidar){
            PropiedadesErrorDTO propErrorDto = new PropiedadesErrorDTO(indexPropi.getNombreCampo(),indexPropi.getPropiedadValidar().toString());
            if(!indexPropi.getListValToExec().isEmpty()){
                CompletableFuture<?>[] futuresComp = new CompletableFuture[indexPropi.getListValToExec().size()];
                int x = 0;
                for (ParametrosValicacionDTO index : indexPropi.getListValToExec()){
                    futuresComp[x++] = CompletableFuture.runAsync(() -> {
                        realizaValidacion(propErrorDto.getListErrorMensajes(), indexPropi.getPropiedadValidar(), index);
                    });
                }
                CompletableFuture.allOf(futuresComp).join();   
            }
            if (!propErrorDto.getListErrorMensajes().isEmpty())
                listPropiedadesError.add(propErrorDto);
        }
    }
    
    /**
     * Método que realizara las validaciones a los objetos.
     * 
     * @param listErrorMesaje    Lista de objetos que contienen los errores encontrados.
     * @param propiedad propiedad a validar.
     * @param dtoParametrosValidacion Objeto que contine las validaciones a ejecutar par la propiedad.
     */
    private void realizaValidacion(List<ErrorMensajesDTO> listErrorMesaje, Object propiedad, ParametrosValicacionDTO dtoParametrosValidacion){
//        logger.log(Level.INFO, "A validar ({0}) con ({1})", new Object[]{propiedad,dtoParametrosValidacion.getEnumValidacion().getEnumTipoValidacion().name()});

        switch(dtoParametrosValidacion.getEnumValidacion().getEnumTipoValidacion().getIdValidacion()){
            case 0 :                
                if (!Pattern.compile(dtoParametrosValidacion.getEnumValidacion().getValidador()).matcher(propiedad.toString()).matches())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                break;
            case 10 ://TIPO_LENGTH_IGUAL
                if (propiedad.toString().length()!=((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 11 ://TIPO_LENGTH_NO_IGUAL
                if (propiedad.toString().length()==((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 12 ://TIPO_LENGTH_MAYOR_QUE
                if (propiedad.toString().length()<=((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;                
            case 13 ://TIPO_LENGTH_MENOR_QUE
                if (propiedad.toString().length()>=((Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
                
            case 14 ://TIPO_LENGTH_MENORQUE_Y_MAYORQUE
                if (propiedad.toString().length()<((Integer)dtoParametrosValidacion.getPriValToEval()) || propiedad.toString().length()>((Integer)dtoParametrosValidacion.getSegValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                break;
            case 20 :
                if (!Objects.equals((Integer)propiedad, (Integer)dtoParametrosValidacion.getPriValToEval()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 21 :
                if ((Integer.parseInt(propiedad.toString()))<=(Integer)dtoParametrosValidacion.getPriValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 22 :
                if ((Integer.parseInt(propiedad.toString())) >= Integer.parseInt(dtoParametrosValidacion.getPriValToEval().toString()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 23 :
                if (((Integer)propiedad) <= (Integer)dtoParametrosValidacion.getPriValToEval() || 
                        ((Integer)propiedad) >= (Integer)dtoParametrosValidacion.getSegValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                break;
            case 24://valida que el objeto sea integer
        		if(!(propiedad instanceof Integer)) {
        			 listErrorMesaje.add(new ErrorMensajesDTO(
                             dtoParametrosValidacion.getEnumValidacion().getId(),
                             dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
        		}
              break;
            case 25: // Numero entre 2 rangos mayor o igual a y menor o igual a
                if (((Integer)propiedad) > (Integer)dtoParametrosValidacion.getPriValToEval() || 
                        ((Integer)propiedad) < (Integer)dtoParametrosValidacion.getSegValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                break;
            case 27 :
                if ((Integer.parseInt(propiedad.toString())) <=(Integer)dtoParametrosValidacion.getPriValToEval())
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 28 :
                if ((Integer.parseInt(propiedad.toString())) >= Integer.parseInt(dtoParametrosValidacion.getPriValToEval().toString()))
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;

            case 34 :
                if (!propiedad.toString().equals(dtoParametrosValidacion.getPriValToEval().toString()))//CADENA_IGUAL_A
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 35 :
                if (propiedad.toString().equals(dtoParametrosValidacion.getPriValToEval().toString()))//CADENA_NO_IGUAL_A
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(),dtoParametrosValidacion.getPriValToEval().toString())));
                break;
            case 36 : //TIPO_TEXTO_UNO_U_OTRO
                if ((dtoParametrosValidacion.getPriValToEval() == null || EMPTY.equals(dtoParametrosValidacion.getPriValToEval().toString())) 
                		&& (dtoParametrosValidacion.getSegValToEval() == null || EMPTY.equals(dtoParametrosValidacion.getSegValToEval().toString()))) 
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                break;
            case 40 ://TIPO_FECHA_MAYORQUE
            	if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
            			&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
	            	if(!FechaUtil.fechaMayorQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
	                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
            	} else {
            		listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
            	}
            	break;
            case 41 ://TIPO_FECHA_MENORQUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMenorQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                } else {
                	logger.info("Formato erroneo");
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
                }
                break;
            case 42 ://TIPO_FECHA_MENORQUE_Y_MAYORQUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getSegValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMayorMenorQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString() + COMA + dtoParametrosValidacion.getSegValToEval().toString()));
                }
                break;
            case 43 ://TIPO_FECHA_MENORIGUAL_MAYORIGUAL_QUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getSegValToEval().toString()).matches()) {
                    if(!FechaUtil.mayorOIgualQueMenorOIgualQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString(), dtoParametrosValidacion.getSegValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString() + COMA + dtoParametrosValidacion.getSegValToEval().toString()));
                }
                break;
            case 44 ://TIPO_FECHA_MAYORIGUAL_QUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMayorIgualQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
                }
                break;
            case 45 ://TIPO_FECHA_MENORIGUAL_QUE
                if (Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(propiedad.toString()).matches()
                		&& Pattern.compile(EnumValidacion.FECHA_FORMATO.getValidador()).matcher(dtoParametrosValidacion.getPriValToEval().toString()).matches()) {
                    if(!FechaUtil.fechaMenorIgualQue(propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString()))
                        listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), String.format(dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
                } else {
                    listErrorMesaje.add(new ErrorMensajesDTO(dtoParametrosValidacion.getEnumValidacion().getId(), EnumMensaje.FECHA_FORMATO.getMensaje() + propiedad.toString() + COMA + dtoParametrosValidacion.getPriValToEval().toString()));
                }
                break;
            case 90: //SOLO UNO DEBE SER NO NULO

                if(dtoParametrosValidacion.getPriValToEval() != null ) {
                    Object[] objeto = (Object[]) dtoParametrosValidacion.getPriValToEval();

                    if (Arrays.stream(objeto).filter(o -> o != null).count() != 1) {

                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                    }
                }
                break;
            case 91://LISTA Y OBJETOS DEBE DE SER NULA O VACIA SI ES LISTA
                if(dtoParametrosValidacion.getPriValToEval() != null) {

                    if(dtoParametrosValidacion.getPriValToEval() instanceof List) {
                        List<Object> lista = (List<Object>) dtoParametrosValidacion.getPriValToEval();
                        if (!lista.isEmpty()) {
                            listErrorMesaje.add(new ErrorMensajesDTO(
                                    dtoParametrosValidacion.getEnumValidacion().getId(),
                                    dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                        }
                    } else {
                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                    }
                }
                break;
            case 92://OBJETO DEBE DE SER NO NULA Y NO VACIA SI ES LISTA

                if(dtoParametrosValidacion.getPriValToEval() == null) {

                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));

                } else {
                    if(dtoParametrosValidacion.getPriValToEval() instanceof List) {
                        List<Object> lista = (List<Object>) dtoParametrosValidacion.getPriValToEval();

                        if (lista.isEmpty()) {
                            listErrorMesaje.add(new ErrorMensajesDTO(
                                    dtoParametrosValidacion.getEnumValidacion().getId(),
                                    dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                        }
                    }
                }
                break;
                
            case 95://Validacion de Objeto u propiedad, debe ser nula en priValToEval == null
                if (dtoParametrosValidacion.getPriValToEval() != null){
                    listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                }
                break;
            case 96://AL MENOS 1 OBJETO O PROPIEDAD NO DEBE VENIR null
                if(dtoParametrosValidacion.getPriValToEval() != null ) {
                    Object[] objeto = (Object[]) dtoParametrosValidacion.getPriValToEval();
                    if (Arrays.stream(objeto).filter(o -> o != null).count() == 0) {
                        listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                    }
                }
                break;
            case 97://VALIDA QUE EL AÑO A PRESENTAR COINCIDA CON LA FECHA ENCARGO
                if(propiedad == null ||dtoParametrosValidacion.getPriValToEval()== null || !FechaUtil.validarFechaEncargo( (String)dtoParametrosValidacion.getPriValToEval(), (Integer)propiedad) ) {
                   listErrorMesaje.add(new ErrorMensajesDTO(
                                dtoParametrosValidacion.getEnumValidacion().getId(),
                                dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
                }
                   break;
            case 110: // Validacion para el documento del modulo de datos curriculares
            	if(validarNivelAcademico(propiedad.toString(), dtoParametrosValidacion.getPriValToEval(), dtoParametrosValidacion.getSegValToEval())) {
            		listErrorMesaje.add(new ErrorMensajesDTO(
                            dtoParametrosValidacion.getEnumValidacion().getId(),
                            dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje()));
            	}
            	break;
            case 120: // Opción para agregar mensaje cuando el tipo de declaracion no aplica en un módulo
        		listErrorMesaje.add(new ErrorMensajesDTO(
                        dtoParametrosValidacion.getEnumValidacion().getId(), String.format(
                        		dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString(), dtoParametrosValidacion.getPriValToEval().toString())));
            	break;
            case 130:
                try{
                    Enum.valueOf((Class) dtoParametrosValidacion.getPriValToEval(), propiedad.toString());
                }catch(IllegalArgumentException e){
//                    logger.log(Level.WARNING, "{0}",e);
                    listErrorMesaje.add(new ErrorMensajesDTO(
                        dtoParametrosValidacion.getEnumValidacion().getId(), String.format(
                        		dtoParametrosValidacion.getEnumValidacion().getEnumValidacion().getMensaje(), propiedad.toString())));
                }
                break;
            default: {
                break;
            }
        }
    }



    
    /**
     * Función para validar el documento obtenido de acuerdo al estatus y nivel académico
     * De PRIMARIA a BACHILLERATO solo podrán elegir BOLETA, CERTIFICADO y CONSTANCIA (cualquier estatus)
     * De CARRERA TECNICA a DOCTORADO en estatus TRUNCO o CURSANDO podrán elegir BOLETA, CERTIFICADO, CONSTANCIA 
     * De CARRERA TECNICA a DOCTORADO en estatus FINALIZADO podrán elegir BOLETA, CERTIFICADO, CONSTANCIA y TITULO
     * @param tipo id del tipo de documento
     * @param estatus id del estatus escolaridad
     * @param nivel el CatalogoDTO del nivel escolaridad  
     * @return boolean
     */
    private boolean validarNivelAcademico(Object tipo, Object estatus, Object nivel) {
    	boolean validacion = true;
    	if(nivel != null && tipo != null && estatus != null) {
			
    		CatalogoDTO nivelAcademico = (CatalogoDTO) nivel;
    		Integer tipoDocumento = Integer.parseInt(tipo.toString());
    		Integer estatusEsc = Integer.parseInt(estatus.toString());
//    		logger.info("== NIVEL: " + nivelAcademico.getId() + " DOCUMENTO: " + tipoDocumento + " ESTATUS: " + estatusEsc);
    		
    		if((EnumNivelAcademico.PRIMARIA.getId() == nivelAcademico.getId()
						|| EnumNivelAcademico.SECUNDARIA.getId() == nivelAcademico.getId()
						|| EnumNivelAcademico.BACHILLERATO.getId() == nivelAcademico.getId())
    				&& EnumTipoDocumento.TITULO.getId() != tipoDocumento) {
    					
					validacion = false;

    		} else if((EnumNivelAcademico.CARRERA_TECNICA.getId() == nivelAcademico.getId()	|| EnumNivelAcademico.ESPECIALIDAD.getId() == nivelAcademico.getId()
						|| EnumNivelAcademico.LICENCIATURA.getId() == nivelAcademico.getId() || EnumNivelAcademico.MAESTRIA.getId() == nivelAcademico.getId()
						|| EnumNivelAcademico.DOCTORADO.getId() == nivelAcademico.getId())) { 

    			if((EnumEstatusEscolaridad.CURSANDO.getId() == estatusEsc || EnumEstatusEscolaridad.TRUNCO.getId() == estatusEsc)
    				&& (EnumTipoDocumento.TITULO.getId() != tipoDocumento)) {
						
					validacion = false;
    			
    			} else if(EnumEstatusEscolaridad.FINALIZADO.getId() == estatusEsc) {
    				validacion = false;
    			}
			} 
		}
    	return validacion;
    }
    
}
