package mx.gob.sfp.dgti.util;

public enum EnumTipoSolicitud {
	HISTORIAL_RFC, //Consulta historial por parametro RFC
        CONSULTA_RFC_DIEZ_POS,//Consulta usuario por RFC a 10 posiciones
	HISTORIAL_CURP, //Consulta historial por parametro CURP
	HISTORIAL_ID_USR, //Consulta historial por parametro IdUsrDecnet
	DECLARACION, //Consulta Declaracion.
	ACUSE, //Consulta Acuse
	CONSULTA_NOMBRE, //Consulta usuarios por nombre. 
	DOMICILIOS_EN_DECLARACION //consulta los domicilios de una declaracion
}
