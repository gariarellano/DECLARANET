/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "Utils-Validator-SFP" Libreria que permite realizar la validacion 
 * de propiedades que se deceen validar, mediante la asignacion de 
 * una posicion, el nombre de la propiedad, el valor de la propiedad y 
 * las validaciones a ejecutar.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.validador.enu;

/**
 * Enum con las expresiones regulares para las validaciones  
 * 
 * @author Miriam Sanchez Sánchez programador07 
 * @since 19/09/2019
 * @modifiedBy cgarias
 * @modification Se agrego el enum del mensaje de la validación, se ajustaron nombres de variables y se agrego enumTipoValidación.
 * @since 25/09/2019
 */
public enum EnumValidacion {
    CURP(10, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CURP, "^([A-Z][AEIOUX][A-Z]{2}\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01])[HM](?:AS|B[CS]|C[CLMSH]|D[FG]|G[TR]|HG|JC|M[CNS]|N[ETL]|OC|PL|Q[TR]|S[PLR]|T[CSL]|VZ|YN|ZS)[B-DF-HJ-NP-TV-Z]{3}[A-Z\\d])(\\d)$"),

    RFC_A_DIEZ(20, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.RFC_A_DIEZ, "^([A-ZÑ&]{3,4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))$"),
    RFC_PM_A_DOCE(22, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.RFC_PM_A_DOCE, "^([A-ZÑ&]{3})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{2})([A\\d])$"),
    RFC_PF_A_TRECE(21, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.RFC_PF_A_TRECE, "^([A-ZÑ&]{4})(\\d{2}(?:0[1-9]|1[0-2])(?:0[1-9]|[12]\\d|3[01]))([A-Z\\d]{3})$"),
    HOMOCLAVE(23, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.HOMOCLAVE, "([A-Z\\d]{2})([A\\d])"),
    
    CADENA_SIN_CARACTERES_ESPECIALES(30, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_SIN_CARACTERES_ESPECIALES, "^([a-zA-ZñÑÁÉÍÓÚáéíóú\\s]){0,2001}$"),
    CADENA_ALFANUMERICA(31, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_ALFANUMERICA, "^([a-zA-Z0-9ÑÁÉÍÓÚÜñáéíóúü~.,' ]){0,2001}$"),
    CADENA_NOMBRES_ACENTOS_MAY_MIN(32, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_NOMBRES_ACENTOS_MAY_MIN, "^([a-zA-ZñáéíóúÁÉÍÓÚÑüÜ.' ]){0,200}$"),
    CADENA_IGUAL_A(33, EnumTipoValidacion.TIPO_TEXTO_IGUAL_A, EnumMensaje.CADENA_IGAL_A, ""),
    CADENA_NO_IGUAL_A(34, EnumTipoValidacion.TIPO_TEXTO_NO_IGUAL_A, EnumMensaje.CADENA_NO_IGAL_A, ""),
    CADENA_NOMBRES_MAY_MIN(35, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_MAY_MIN, "^(?!.* (?: |$))[a-zA-ZñÑ ]+$"),
    CADENA_TEXTO_AL_MENOS_UNO(36, EnumTipoValidacion.TIPO_TEXTO_AL_MENOS_UNO, EnumMensaje.CADENA_AL_MENOS_UNO, ""),
    CADENA_TEXTO_MINUSCULAS_GIONES(37, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_MINUSCULAS_GUINES, "^[a-zA-Z]+(?:-[a-zA-Z]+)*$"),
    CADENA_ACLARACIONES(38, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_ALFANUMERICA, "^([\\wÑÁÉÍÓÚÜñáéíóúü ()¿?¡!%*+=\\-.,:;$\"\\/\n]){0,2001}$"),
    CADENA_CONTIENE_URL(39, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CADENA_CNTIENE_URL, "^https?:\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+[/#?]?.*$"),
    
    CELULAR_NACIONAL(40, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CELULAR, "^[2-9]{1}[0-9]{9}$"),
    CELULAR_EXTRANJERO(41, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CELULAR, "^[\\d+]{10}$"),
    TELEFONO_CASA(41, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.TELEFONO_CASA, "^\\d+$"),
    CP_MEXICO(42, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CP_MEXICO,"^\\d{5}$"),

    CORREO(50, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.CORREO, "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?"),

    LONGITUD_IGUAL(60, EnumTipoValidacion.TIPO_LENGTH_IGUAL, EnumMensaje.LONGITUD_IGUAL, ""),
    LONGITUD_MENOR_QUE(61, EnumTipoValidacion.TIPO_LENGTH_MENOR_QUE, EnumMensaje.LONGITUD_MENOR_QUE, ""),
    LONGITUD_NO_IGUAL(62, EnumTipoValidacion.TIPO_LENGTH_NO_IGUAL, EnumMensaje.LONGITUD_NO_IGUAL, ""),
    LONGITUD_MAYOR_QUE(63, EnumTipoValidacion.TIPO_LENGTH_MAYOR_QUE, EnumMensaje.LONGITUD_MAYOR_QUE, ""),
    LONGITUD_MAYORQUE_MENORQUE(64, EnumTipoValidacion.TIPO_LENGTH_MENORQUE_Y_MAYORQUE, EnumMensaje.LONGITUD_MENORQUE_MAYORQUE, ""),

    NUMERO_ENTERO(70, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.NUMERO_ENTERO,"^[0-9]+$"),
    NUMERO_IGUAL(71, EnumTipoValidacion.TIPO_NUMERO_IGUAL, EnumMensaje.NUMERO_IGUAL,""),
    NUMERO_MENOR_QUE(72, EnumTipoValidacion.TIPO_NUMERO_MENOR_QUE, EnumMensaje.NUMERO_MENOR_QUE,""),
    NUMERO_MAYOR_QUE(73, EnumTipoValidacion.TIPO_NUMERO_MAYOR_QUE, EnumMensaje.NUMERO_MAYOR_QUE,""),
    NUMERO_MAYORQUE_MENORQUE(74, EnumTipoValidacion.TIPO_NUMERO_MAYORQUE_MENORQUE, EnumMensaje.NUMERO_MAYORQUE_MENORQUE,""),
    NUMERO_ES_NUMERO(75, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.NUMERO_ES_NUMERO,"^\\-{0,1}([0-9]*|\\d*\\.\\d{1}?\\d*)$"),
    TIPO_NUMERO_INTEGER(76, EnumTipoValidacion.TIPO_NUMERO_INTEGER, EnumMensaje.TIPO_NUMERO_INTEGER,""),
    NUMERO_MAYORIGUALQ_MENORIGUALQ(77, EnumTipoValidacion.TIPO_NUMERO_MAYORIGUALQ_MENORIGUALQ, EnumMensaje.NUMERO_MAYORIGUALQ_MENORIGUALQ,""),
    NUMERO_LONG_IGUAL(71, EnumTipoValidacion.TIPO_LONG_IGUAL, EnumMensaje.NUMERO_IGUAL,""),
    NUMERO_MENOR_IGUAL_QUE(78, EnumTipoValidacion.TIPO_NUMERO_MENOR_QUE, EnumMensaje.NUMERO_MENOR_IGUAL_QUE,""),
    NUMERO_MAYOR_IGUAL_QUE(79, EnumTipoValidacion.TIPO_NUMERO_MAYOR_QUE, EnumMensaje.NUMERO_MAYOR_IGUAL_QUE,""),

    PROTOCOLO_IP(80, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.PROTOCOLO_IP, "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$"),

    FECHA_FORMATO(90, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.FECHA_FORMATO, "^\\d{4}-\\d{2}-\\d{2}$"),
    FECHA_MAYORQUE(91, EnumTipoValidacion.TIPO_FECHA_MAYORQUE, EnumMensaje.FECHA_MAYORQUE, ""),
    FECHA_MENORQUE(92, EnumTipoValidacion.TIPO_FECHA_MENORQUE, EnumMensaje.FECHA_MENORQUE, ""),
    FECHA_MAYORQUE_MENORQUE(93, EnumTipoValidacion.TIPO_FECHA_MAYORQUE_MENORQUE, EnumMensaje.FECHA_MAYORQUE_MENORQUE, ""),
    FECHA_MAYORIGUAL_MENORIGUAL_QUE(94, EnumTipoValidacion.TIPO_FECHA_MAYORIGUAL_MENORIGUAL_QUE, EnumMensaje.FECHA_MAYORIGUAL_MENORIGUAL_QUE, ""),
    FECHA_MAYORIGUAL_QUE(95, EnumTipoValidacion.TIPO_FECHA_MAYORIGUAL_QUE, EnumMensaje.FECHA_MAYORIGUAL_QUE, ""),
    FECHA_MENORIGUAL_QUE(96, EnumTipoValidacion.TIPO_FECHA_MENORIGUAL_QUE, EnumMensaje.FECHA_MENORIGUAL_QUE, ""),
    FECHA_REGISTRO_ACTUALIZACION_FORMATO_SEGUNDOS(97, EnumTipoValidacion.TIPO_REGEX, EnumMensaje.FORMATO_FECHA_SEGUNDOS,"^(\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.[a-zA-Z-0-9]*)$"),
    FECHA_ENCARGO_IGUAL_ANIO_DECLA(99, EnumTipoValidacion.FECHA_ENCARGO_IGUAL_ANIO_DECLA, EnumMensaje.FECHA_ENCARGO_IGUAL_ANIO_DECLA,""),
    
    UN_SOLO_OBJETO_NO_NULO(100, EnumTipoValidacion.TIPO_SOLO_UN_OBJ_NO_NULO, EnumMensaje.UN_SOLO_OBJETO_NO_NULO, ""),
    OBJETO_DEBE_SER_NULO(101, EnumTipoValidacion.TIPO_OBJETO_NULO, EnumMensaje.OBJETO_DEBE_SER_NULO, ""),
    OBJETO_DEBE_SER_NO_NULO(102, EnumTipoValidacion.TIPO_OBJETO_NO_NULO, EnumMensaje.OBJETO_DEBE_SER_NO_NULO, ""),
    
    OBJETO_U_PROPIEDAD_DEBE_SER_NULL(105, EnumTipoValidacion.TIPO_OBJETO_U_PROPIEDAD_NULL, EnumMensaje.OBJETO_U_PROPIEDAD_DEBE_SER_NULO, ""),
    AL_MENOS_UN_OBJETO_NO_DEBE_SER_NULL(106, EnumTipoValidacion.TIPO_OBJETO_U_PROPIEDAD_AL_MENOS_UNO_NO_NULL, EnumMensaje.OBJETO_U_PROPIEDAD_AL_MENOS_UNO_NO_DEBE_SER_NULO, ""),
    NIVEL_ACADEMICO_DOC(110, EnumTipoValidacion.TIPO_NIVEL_ACADEMICO_DOC, EnumMensaje.NIVEL_ACADEMICO_DOC, ""),TIPO_DECLARACION(120, EnumTipoValidacion.TIPO_DECLARACION, EnumMensaje.TIPO_DECLARACION, ""),
    ENUM_EXISTE_NAME(130, EnumTipoValidacion.TIPO_EXISTE_ENUM_NAME, EnumMensaje.EXISTE_ENUM_NAME, ""),

    CATALOGO(200, EnumTipoValidacion.TIPO_CATALOGO, EnumMensaje.CATALOGO,"")
    ;
    
    
    
	
    private final int id;
    private final EnumMensaje enumValidacion;
    private final EnumTipoValidacion enumTipoValidacion;
    private final String validador;

    /**
     * Constructor con parametros para inicializar en enum.
     * 
     * @param id 
     * @param enumTipoValidacion
     * @param enumValidacion
     * @param validador
	 */
    EnumValidacion(int id, EnumTipoValidacion enumTipoValidacion, EnumMensaje enumValidacion, String validador) {
        this.id = id;
        this.enumTipoValidacion = enumTipoValidacion;
        this.enumValidacion  = enumValidacion;
        this.validador = validador;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    public EnumTipoValidacion getEnumTipoValidacion(){
        return enumTipoValidacion;
    }
    /**
     * @return the enumValidacion
     */
    public EnumMensaje getEnumValidacion(){
        return enumValidacion;
    }
    /**
     * @return the validador
     */
    public String getValidador() {
        return validador;
    }
}
