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
 * Enum con el nombre de las validaciones y el mensaje de error 
 * @author Miriam Sanchez Sánchez programador07 
 * @since 19/09/2019
 */
public enum EnumMensaje {
    OBJETO_DATO_OBLIGATORIO("Validación de un objeto o dato contenga informacion y no sea nulo","El dato es obligatorio."),
    CURP("Validación de la CURP","Se requiere una CURP válida."),    
    RFC_HOMOCLAVE("Validación de HOMOCLAVE ", "Se requiere una Homoclave válida."),
    RFC_A_DIEZ("Validación del RFC sin homoclave.", "Se requiere un RFC válido."),
    RFC_PM_A_DOCE("Validación del RFC persona moral con homoclave.", "Se requiere un RFC válido de 12 posiciones, por ejemplo: ABC010101XX1."),
    RFC_PF_A_TRECE( "Validación del RFC persona física con homoclave.", "Se requiere un RFC válido de 13 posiciones, por ejemplo: ABCD010101XXX."),
    CORREO("Validación del correo electrónico.","Se requiere un correo electrónico válido."),
    CELULAR("Validación del teléfono.","Se requiere un número de celular válido."),
    TELEFONO_CASA("Validación del teléfono.","Se requiere un número de teléfono válido."),
    CP_MEXICO("Validación del código postal en México.","Se requiere un Código Postal válido, por ejemplo: 01020 ó 31203."),
    HOMOCLAVE("Validación de la homoclave del RFC.","Se requiere una Homoclave válida."),
    CADENA_SIN_CARACTERES_ESPECIALES("Validación que solo permite letras en mayusculas sin caracteres especiales y sin acentos.","Solo se aceptan letras mayúsculas, sin caracteres especiales y sin acentos."),
    CADENA_ALFANUMERICA("Validación de cadena alfanumerica incorrecta", "La cadena proporcionada no tiene formato alfanumérico."),
    CADENA_NOMBRES_ACENTOS_MAY_MIN("Validación de cadena incorrecta", "La cadena no debe contener caracteres especiales."),
    CADENA_MAY_MIN("Validación de cadena incorrecta", "La cadena no debe contener acentos ni caracteres especiales."),
    CADENA_IGAL_A("Validacion de contenido de cadena", "La cadena proporcionada no es igual a %s."),
    CADENA_NO_IGAL_A("Validacion de contenido de cadena no igualdad", "La cadena proporcionada no debe de ser igual a %s."),
    CADENA_AL_MENOS_UNO("Validación de al menos un dato obligatorio", "Debe llenar al menos un campo"),
    CADENA_MINUSCULAS_GUINES("Validación de solo letras minusculas y guines", "Debe proporcionar una cadena de caracteres y guines sin espacios"),
    CADENA_CNTIENE_URL("Debe contener una url", "Debe proporcionar una url válida"),
    LONGITUD_IGUAL("Validación de longitud tamanio igual.","La longitud de caracteres debe ser de %s."),
    LONGITUD_NO_IGUAL("Validación de longitud tamanio no igual.","La longitud de caracteres no debe ser de %s."),
    LONGITUD_MAYOR_QUE("Validación de longitud tamanio mayor que el valor porporionado.","La longitud de caracteres debe ser mayor que %s."),
    LONGITUD_MENOR_QUE("Validación de longitud tamanio menor que el valor porporionado.","La longitud de caracteres debe ser menor que %s."),    
    LONGITUD_MENORQUE_MAYORQUE("Validación de longitud tamanio menor y mayor que el valor porporionado.","La longitud de caracteres debe ser menor que %s y mayor que %s."),
    NUMERO_ENTERO("Validación del dato proporcionado sea número entero.","El número no debe tener decimales."),    
    NUMERO_IGUAL("Validación de número igual.","El número debe ser igual a %s."),
    NUMERO_MENOR_QUE("Validación de número menor.","El número debe ser menor a %s."),
    NUMERO_MAYOR_QUE("Validación de número mayor","El número debe ser mayor a %s."),
    NUMERO_MENOR_IGUAL_QUE("Validación de número menor.","El número debe ser menor igual o menor a %s."),
    NUMERO_MAYOR_IGUAL_QUE("Validación de número mayor","El número debe ser igual o mayor a %s."),
    NUMERO_MAYORQUE_MENORQUE("Validación de un número entre un rango","El número debe ser un valor entre %s y %s."),
    NUMERO_MAYORIGUALQ_MENORIGUALQ("Validación de un número entre un rango","El número no debe encontrarse entre %s y %s."),
    NUMERO_ES_NUMERO("Validacion de cadena con formato numero", "El valor proporcionado no es numérico."),
    TIPO_NUMERO_INTEGER("Validación numero"," El valor proporcionado no es un numérico"),
    PROTOCOLO_IP("Validación de IP incorrecta.","La ip que se proporcionó es incorrecta."),
    FECHA_FORMATO("Validación de fechas.","La fecha no se encuentra en el formato yyyy-mm-dd: "),
    FECHA_MAYORQUE("Validación de fechas.","La fecha %s no es mayor que %s."),
    FECHA_MENORQUE("Validación de fechas.","La fecha %s no es menor que %s."),
    FECHA_MAYORIGUAL_QUE("Validación de fechas.","La fecha %s no es mayor o igual que %s."),
    FECHA_MENORIGUAL_QUE("Validación de fechas.","La fecha %s no es menor o igual que %s."),
    FECHA_MAYORQUE_MENORQUE("Validación de fechas.","La fecha %s no se encuentra en el rango entre %s y %s."),
    FECHA_MAYORIGUAL_MENORIGUAL_QUE("Validación de fechas.","La fecha %s no se encuentra en el rango %s y %s."),
    FORMATO_FECHA_SEGUNDOS("Validación de fechas.", "La fecha no se encuentra en el formato yyyy-MM-dd'T'hh:mm:ss"),
    FECHA_ENCARGO_IGUAL_ANIO_DECLA("alidación de años","El año de la declaración a presentar no coincide con la fecha de encargo."),
    UN_SOLO_OBJETO_NO_NULO("Validación de un solo objeto no nulo.", "Solo uno de los objetos puede ser no nulo."),
    OBJETO_DEBE_SER_NULO("Validación de que un objeto debe ser nulo.", "Este módulo debe ser nulo debido a los otros datos recibidos."),
    OBJETO_DEBE_SER_NO_NULO("Validación de que un objeto debe ser no nulo.", "Este módulo debe ser no nulo debido a los otros datos recibidos."),
    OBJETO_U_PROPIEDAD_DEBE_SER_NULO("Validación de que un objeto debe ser nulo.", "El valor de la propiedad u objeto, debe ser obligatoriamente nulo."),
    OBJETO_U_PROPIEDAD_AL_MENOS_UNO_NO_DEBE_SER_NULO("Validación de que al menos un objeto no debe ser null.", "Al menos uno de los objetos debe de ser no nulo."),
    NIVEL_ACADEMICO_DOC("Validación del documento obtenido por nivel académico", "El documento no es elegible para el nivel académico."), 
    TIPO_DECLARACION("Validación de tipo declaración", "El tipo de declaración %s no aplica en el módulo %s."),
    EXISTE_ENUM_NAME("Validación de la existencia del Enumproporcionado", "El valor del Enum proporcionado %s no corresponde a los valores permitidos."),

    CATALOGO_OBLIGATORIO("Validacion del catalogo obligatorio", "El catalogo es obligatorio."),
    CATALOGO("Validacion del catalogo no existe en base", "El valor del catalogo no concuerda con el de la base."),
    CATALOGO_IMPOSIBLE_VALIDAR("Validacion cuando no se pudo contactar el servicio de validacion", "No fue posible validar el catálogo.")
    ;

    private final String descripcion;
    private final String mensaje;

    /**
     * Constructor con parametros
     * @param descripcion
     * @param mensaje
     */
    EnumMensaje(String descripcion, String mensaje){
        this.descripcion = descripcion;
        this.mensaje = mensaje;
    }
    
    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }
}
