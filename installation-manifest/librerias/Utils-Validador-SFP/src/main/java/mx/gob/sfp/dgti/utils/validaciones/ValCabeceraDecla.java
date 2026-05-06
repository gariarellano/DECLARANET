/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.utils.validaciones;

import mx.gob.sfp.dgti.declaracion.dto.base.CabeceraDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.EnteReceptorDTO;
import mx.gob.sfp.dgti.declaracion.dto.general.InstitucionReceptoraDTO;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumAmbitoPoder;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumCatalogosUnoFk;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelGobierno;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumTipoDeclaracion;
import mx.gob.sfp.dgti.utils.FechaUtil;
import mx.gob.sfp.dgti.utils.propiedades.PropBase;
import mx.gob.sfp.dgti.validador.dto.in.ModuloValidarDTO;
import mx.gob.sfp.dgti.validador.dto.in.ParametrosValicacionDTO;
import mx.gob.sfp.dgti.validador.dto.in.PropiedadesValidarDTO;
import mx.gob.sfp.dgti.validador.enu.EnumValidacion;

/**
 * Clase que contiene los métodos para el armado de 
 * objetos ModuloDTO la validacion de la cabecera
 * de la declaracion (institucionReceptora, encabezado, declaracion, firmado)
 * 
 * @author cgarias
 * @since 19/11/2019
 */
public class ValCabeceraDecla {
    
 
	/**
     * Metodo para definir el encabezado a validar cuando se inicia la declaración
     *
     * @param cabecera objeto toda la cabecera de la declaración
     * @return Objeto ModuloValidarDTO
     *
     * @author programador04
     * @since 20/11/2019
     */
	public  static ModuloValidarDTO crearCabeceraInicial(CabeceraDTO cabecera) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO("Cabecera");
		realizaValidacioInstitucionRecep(moduloValidar, cabecera.getInstitucionReceptora());
		creaModuloValidacionEncabezado(moduloValidar, cabecera, true);
		return moduloValidar;
	}
	
	 /**
    * Metodo para definir el encabezado a validar cuando se continúa la declaracion
    *
     * @param cabecera objeto toda la cabecera de la declaración
    * @return Objeto ModuloValidarDTO
    *
    * @author programador04
    * @since 20/11/2019
    */
	public  static ModuloValidarDTO crearCabeceraDeclaracionExistente(CabeceraDTO cabecera) {
		ModuloValidarDTO moduloValidar = new ModuloValidarDTO("Cabecera");
		realizaValidacioInstitucionRecep(moduloValidar, cabecera.getInstitucionReceptora());
		creaModuloValidacionEncabezado(moduloValidar, cabecera, false);
		return moduloValidar;
    }
	
    /**
     * Método que realiza la validacion del la existencia del módulo de Institucion receptora.
     * 
     * @param modulo        Objetoq ue contdra las validacione a ejecutar.
     * @param institucionReceptoraDto Objeto que contiene el mapeo completo de lainstitucion receptora de declaraciones. 
     */
    public static void realizaValidacioInstitucionRecep(ModuloValidarDTO modulo, InstitucionReceptoraDTO institucionReceptoraDto){
        modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(institucionReceptoraDto, "InstitucionReceptora"));
        if (institucionReceptoraDto != null){
            ModuloValidarDTO institRecep = new ModuloValidarDTO("institucionReceptora");
            institRecep.getListPropsTovalidate().add(new PropiedadesValidarDTO("nombre", institucionReceptoraDto.getNombre()));
            ((PropiedadesValidarDTO) institRecep.getListPropsTovalidate().get(0)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_ACENTOS_MAY_MIN));        
            institRecep.getListPropsTovalidate().add(new PropiedadesValidarDTO("_id", institucionReceptoraDto.get_id()));            
            institRecep.getListPropsTovalidate().add(new PropiedadesValidarDTO("collName", institucionReceptoraDto.getCollName()));
            institRecep.getListPropsTovalidate().get(2).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
            institRecep.getListPropsTovalidate().get(2).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYORQUE_MENORQUE, 99,999999));
            creaModuloValidacionEnteInstitucion(institRecep, institucionReceptoraDto.getEnte());
            modulo.getListModuloshijos().add(institRecep);
        }
    }
    
    /**
     * Mètodo que realiza la validaciòn de institucionReceptora.
     * 
     * NOTA : En este mètodo no se contemplo la bandera de isPrimera vez, ya que 
     *        este ente en estricto sentido siempre debe de contener los identificadores
     *        por lo cual siempre se debe de validar queno vengan null.
     *        
     *        Es un mètodovoid porque realizaelllenado de padre "modulo" por paso por referencia.
     * 
     * @param modulo        Objeto que hace referencia al mòdulo padre de todas las validaciones.
     * @param enteReceptor  Objeto que contiene el mapeo completo de ente receptor.     
     */
    public static void creaModuloValidacionEnteInstitucion(ModuloValidarDTO modulo, EnteReceptorDTO enteReceptor){        
        modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(enteReceptor, "ente"));
        if(enteReceptor!= null){
            ModuloValidarDTO enteInstitucion = new ModuloValidarDTO("ente");
            enteInstitucion.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(enteReceptor.getNivelOrdenGobierno(), "nivelOrdenGobierno"));
            if(enteReceptor.getNivelOrdenGobierno() != null){
                ModuloValidarDTO nivelOrdenGobierno = new ModuloValidarDTO("nivelOrdenGobierno");
                nivelOrdenGobierno.getListPropsTovalidate().add(new PropiedadesValidarDTO("EnumNivelGobierno", enteReceptor.getNivelOrdenGobierno().getNivelOrdenGobierno()));
                ((PropiedadesValidarDTO) nivelOrdenGobierno.getListPropsTovalidate().get(0)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumNivelGobierno.class));

                if(!enteReceptor.getNivelOrdenGobierno().getNivelOrdenGobierno().name().equals(EnumNivelGobierno.FEDERAL.name())){
                    nivelOrdenGobierno.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(enteReceptor.getNivelOrdenGobierno().getEntidadFederativa(),"entidadFederativa"));

                    if(enteReceptor.getNivelOrdenGobierno().getEntidadFederativa() != null){
                        ModuloValidarDTO entidadFederativa = new ModuloValidarDTO("entidadFederativa");

                        entidadFederativa.getListPropsTovalidate().add(new PropiedadesValidarDTO("id", enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getId()));
                        ((PropiedadesValidarDTO) entidadFederativa.getListPropsTovalidate().get(0)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
                        entidadFederativa.getListPropsTovalidate().add(new PropiedadesValidarDTO("valor", enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getValor()));
                        ((PropiedadesValidarDTO) entidadFederativa.getListPropsTovalidate().get(1)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_ACENTOS_MAY_MIN));
                        if(enteReceptor.getNivelOrdenGobierno().getNivelOrdenGobierno().name().equals(EnumNivelGobierno.MUNICIPAL.name())){
                            entidadFederativa.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getMunicipioAlcaldia(),"municipioAlcaldia"));
                            if(enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getMunicipioAlcaldia() != null){
                                ModuloValidarDTO municipioAlcaldia = new ModuloValidarDTO("municipioAlcaldia");

                                municipioAlcaldia.getListPropsTovalidate().add(new PropiedadesValidarDTO("id", enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getMunicipioAlcaldia().getId()));
                                ((PropiedadesValidarDTO) municipioAlcaldia.getListPropsTovalidate().get(0)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
                                municipioAlcaldia.getListPropsTovalidate().add(new PropiedadesValidarDTO("valor", enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getMunicipioAlcaldia().getValor()));
                                ((PropiedadesValidarDTO) municipioAlcaldia.getListPropsTovalidate().get(1)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_NOMBRES_ACENTOS_MAY_MIN));

                                entidadFederativa.getListModuloshijos().add(municipioAlcaldia);
                            }
                        }else {
                            entidadFederativa.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(enteReceptor.getNivelOrdenGobierno().getEntidadFederativa().getMunicipioAlcaldia(),"municipioAlcaldia"));
                        }
                        nivelOrdenGobierno.getListModuloshijos().add(entidadFederativa);
                    }
                }else {
                    nivelOrdenGobierno.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNulo(enteReceptor.getNivelOrdenGobierno().getEntidadFederativa(),"entidadFederativa"));
                }
                enteInstitucion.getListModuloshijos().add(nivelOrdenGobierno);
            }
            enteInstitucion.getListPropsTovalidate().add(new PropiedadesValidarDTO("ambitoPublico", enteReceptor.getAmbitoPublico()));
            ((PropiedadesValidarDTO) enteInstitucion.getListPropsTovalidate().get(1)).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumAmbitoPoder.class));
            enteInstitucion.getListPropsTovalidate().add(new PropiedadesValidarDTO("id", enteReceptor.getId()));
            enteInstitucion.getListPropsTovalidate().add(new PropiedadesValidarDTO("nombre", enteReceptor.getNombre()));
            enteInstitucion.getListPropsTovalidate().get(3).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_MENOR_QUE,151));
            modulo.getListModuloshijos().add(enteInstitucion);
        }
    }
	
    private static void creaModuloValidacionEncabezado(ModuloValidarDTO modulo, CabeceraDTO cabeceraDto, boolean isInicial) {
    	modulo.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(cabeceraDto.getEncabezado(), "Encabezado"));
   
    	if(cabeceraDto.getEncabezado() != null) {
    		ModuloValidarDTO encabezado = new ModuloValidarDTO("encabezado");
    		if(isInicial) {
    			//1
    			encabezado.getListPropsTovalidate().add(PropBase.crearPropDebeSerNulo(cabeceraDto.getEncabezado().getNumeroDeclaracion(),"numeroDeclaracion"));
    			//2
    			encabezado.getListPropsTovalidate().add(PropBase.crearPropDebeSerNulo(cabeceraDto.getEncabezado().getFechaRegistro(),"fechaRegistro"));
    			//3
    			encabezado.getListPropsTovalidate().add(PropBase.crearPropDebeSerNulo(cabeceraDto.getEncabezado().getFechaActualizacion(), "fechaActualizacion"));
    		}else {
    			//1
    			encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("numeroDeclaracion", cabeceraDto.getEncabezado().getNumeroDeclaracion()));
    			if(cabeceraDto.getEncabezado().getNumeroDeclaracion() != null) 
    				encabezado.getListPropsTovalidate().get(0).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.LONGITUD_IGUAL,24));
    			//2
    			encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("fechaRegistro",cabeceraDto.getEncabezado().getFechaRegistro()));
    			if(cabeceraDto.getEncabezado().getFechaRegistro() != null)
    				encabezado.getListPropsTovalidate().get(1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_REGISTRO_ACTUALIZACION_FORMATO_SEGUNDOS));
    			//3
    			encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("fechaActualizacion",cabeceraDto.getEncabezado().getFechaActualizacion()));
    			if(cabeceraDto.getEncabezado().getFechaActualizacion() != null)
    				encabezado.getListPropsTovalidate().get(2).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_REGISTRO_ACTUALIZACION_FORMATO_SEGUNDOS));
    		}
    		//4
    		encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("tipoDeclaracion",cabeceraDto.getEncabezado().getTipoDeclaracion()));
    		if(cabeceraDto.getEncabezado().getTipoDeclaracion() != null){
                    encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size()-1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.ENUM_EXISTE_NAME, EnumTipoDeclaracion.class));
                }
    		//5
    		encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("año", cabeceraDto.getEncabezado().getAnio()));
    		if(cabeceraDto.getEncabezado().getAnio() != null) {
    			encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MENOR_QUE, FechaUtil.getAnioActual()+1));
    		}
    		
    		if(cabeceraDto.getEncabezado().getTipoDeclaracion() != null && !cabeceraDto.getEncabezado().getTipoDeclaracion().getDescripcion().equals(EnumTipoDeclaracion.MODIFICACION.getDescripcion())) {
    			//6
    			encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("fechaEncargo",cabeceraDto.getEncabezado().getFechaEncargo()));
    			if(cabeceraDto.getEncabezado().getFechaEncargo() != null) {
        			encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_FORMATO));
    			}
    			if(cabeceraDto.getEncabezado().getAnio() != null) {
        			encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size() -2).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
        			if(cabeceraDto.getEncabezado().getAnio() != null && cabeceraDto.getEncabezado().getFechaEncargo() != null) {
            			encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size() -2).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_ENCARGO_IGUAL_ANIO_DECLA,cabeceraDto.getEncabezado().getFechaEncargo()));
            			encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.FECHA_MENORIGUAL_QUE, FechaUtil.getFechaActualString()));
            		}
        		}
        		
        		
    		}else if(cabeceraDto.getEncabezado().getTipoDeclaracion() != null && cabeceraDto.getEncabezado().getTipoDeclaracion().getDescripcion().equals(EnumTipoDeclaracion.MODIFICACION.getDescripcion())){
    			//6
    			encabezado.getListPropsTovalidate().add(PropBase.crearPropDebeSerNulo(cabeceraDto.getEncabezado().getFechaEncargo(), "fechaEncargo"));
    		}
    		//7
    		encabezado.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(cabeceraDto.getEncabezado().getUsuario(), "Usuario"));
    		if(cabeceraDto.getEncabezado().getUsuario() != null) {
    			ModuloValidarDTO usuario = new ModuloValidarDTO("Usuario");
    			usuario.getListPropsTovalidate().add(new PropiedadesValidarDTO("idUsuario",cabeceraDto.getEncabezado().getUsuario().getIdUsuario()));
    			if(cabeceraDto.getEncabezado().getUsuario().getIdUsuario() != null) 
    				usuario.getListPropsTovalidate().get(0).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_ENTERO));
    			usuario.getListPropsTovalidate().add(new PropiedadesValidarDTO("curp",cabeceraDto.getEncabezado().getUsuario().getCurp()));
    			if( cabeceraDto.getEncabezado().getUsuario().getCurp() != null)
    				usuario.getListPropsTovalidate().get(1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CURP));
    			usuario.getListPropsTovalidate().add(new PropiedadesValidarDTO("curp",cabeceraDto.getEncabezado().getUsuario().getCurp()));
    			usuario.getListPropsTovalidate().add(PropBase.crearPropDebeSerNulo(cabeceraDto.getEncabezado().getUsuario().getId_movimiento(), "id_movimiento"));
    			encabezado.getListModuloshijos().add(usuario);
    		}
    		//8
    		encabezado.getListPropsTovalidate().add(new PropiedadesValidarDTO("versionDeclaracion",cabeceraDto.getEncabezado().getVersionDeclaracion()));
    		if(cabeceraDto.getEncabezado().getVersionDeclaracion() != null && isInicial) {
    			encabezado.getListPropsTovalidate().get(encabezado.getListPropsTovalidate().size() -1).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.NUMERO_MAYOR_QUE, (Integer.parseInt(System.getenv("VERSION_DECLARACION") != null ? System.getenv("VERSION_DECLARACION") :"20200414"))-1));
    		}
    			//9
    		//if(cabeceraDto.getEncabezado().getTipoDeclaracion() != null & !EnumTipoDeclaracion.AVISO.name().equals(cabeceraDto.getEncabezado().getTipoDeclaracion().name())) {
    			encabezado.getListPropsTovalidate().add(PropBase.crearModuloDebeSerNoNulo(cabeceraDto.getEncabezado().getNivelJerarquico(), "nivelGerarquico"));
        	    	if(cabeceraDto.getEncabezado().getNivelJerarquico() != null) {
        	    		ModuloValidarDTO nivelActual = new ModuloValidarDTO("nivelJerarquico");
        	    			
        	    		nivelActual.getListPropsTovalidate().add(PropBase.crearPropCatalogo(
        	    	               "nivelJerarquico", EnumCatalogosUnoFk.CAT_NIVEL_JERARQUICO.name(),
        	    	               cabeceraDto.getEncabezado().getNivelJerarquico(), true));
        	    			
        	    		//modificar el numero de propiedad
        	    		nivelActual.getListPropsTovalidate().add(new PropiedadesValidarDTO("valorUno", cabeceraDto.getEncabezado().getNivelJerarquico().getValorUno()));
        	    		if(cabeceraDto.getEncabezado().getNivelJerarquico().getValorUno() != null && !(cabeceraDto.getEncabezado().getNivelJerarquico().getValorUno().equals("S") || cabeceraDto.getEncabezado().getNivelJerarquico().getValorUno().equals("C"))) {
        	    			nivelActual.getListPropsTovalidate().get(0).getListValToExec().add(new ParametrosValicacionDTO(EnumValidacion.CADENA_IGUAL_A, "S o C"));
        	    		}
        	    			encabezado.getListModuloshijos().add(nivelActual);
        	    		}
    		//}	
    		
    		modulo.getListModuloshijos().add(encabezado);
    	}
    }
}
