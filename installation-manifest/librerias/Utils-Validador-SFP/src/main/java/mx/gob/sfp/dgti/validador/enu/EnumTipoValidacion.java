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
 * Enum que contiene los tipos de validaciuones que se tienen 
 * programados para este utileria de regresar mensajes.
 * 
 * @author cgarias
 * @since 26/09/2019
 */
public enum EnumTipoValidacion {
    TIPO_REGEX(0),
    
    TIPO_LENGTH_IGUAL(10),
    TIPO_LENGTH_NO_IGUAL(11),
    TIPO_LENGTH_MAYOR_QUE(12),
    TIPO_LENGTH_MENOR_QUE(13),
    TIPO_LENGTH_MENORQUE_Y_MAYORQUE(14),
    
    TIPO_NUMERO_IGUAL(20),
    TIPO_NUMERO_MAYOR_QUE(21),
    TIPO_NUMERO_MENOR_QUE(22),
    TIPO_NUMERO_MAYORQUE_MENORQUE(23),
    TIPO_NUMERO_INTEGER(24),
    TIPO_NUMERO_MAYORIGUALQ_MENORIGUALQ(25),
    TIPO_LONG_IGUAL(26),
    TIPO_NUMERO_MAYOR_IGUAL_QUE(27),
    TIPO_NUMERO_MENOR_IGUAL_QUE(28),

    TIPO_TEXTO_IGUAL_A(34),
    TIPO_TEXTO_NO_IGUAL_A(35),
    TIPO_TEXTO_AL_MENOS_UNO(36),
    
    TIPO_FECHA_MAYORQUE(40),
    TIPO_FECHA_MENORQUE(41),
    TIPO_FECHA_MAYORQUE_MENORQUE(42),
    TIPO_FECHA_MAYORIGUAL_MENORIGUAL_QUE(43),
    TIPO_FECHA_MAYORIGUAL_QUE(44),
    TIPO_FECHA_MENORIGUAL_QUE(45),

    TIPO_OBLIGATORIO(100),

    TIPO_SOLO_UN_OBJ_NO_NULO(90),
    TIPO_OBJETO_NULO(91),
    TIPO_OBJETO_NO_NULO(92),
    TIPO_OBJETO_U_PROPIEDAD_NULL(95),
    TIPO_OBJETO_U_PROPIEDAD_AL_MENOS_UNO_NO_NULL(96),
    
    FECHA_ENCARGO_IGUAL_ANIO_DECLA(97),
    
    TIPO_NIVEL_ACADEMICO_DOC(110), 
    TIPO_DECLARACION(120),
    TIPO_EXISTE_ENUM_NAME(130),

    TIPO_CATALOGO(200)
    ;
    private final int idValidacion;
    
    EnumTipoValidacion(int idValidacion){
        this.idValidacion = idValidacion;
    }
    
    public int getIdValidacion(){
        return idValidacion;
    }
}
