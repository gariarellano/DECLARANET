/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "GuardaDeclaracion" Sistema que permite realizar el guardado de declaraciones
 * patrimoniales y de intereses auna base de datos de mongodb
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.enu.arreglos;

import mx.gob.sfp.dgti.declaracion.enums.campos.EnumModulo;

/**
 * Enum que contiene los valores del enum
 * para los arreglos qu ele corresponden a un módulo 
 * en particular.
 * 
 * @author cgarias
 * @since 29/11/2019
 */
public enum EnumModuloArreglo {    
    I_AR1_DATOS_CURRICULARES("escolaridad", "",EnumModulo.I_DATOS_CURRICULARES),
    I_AR1_DATOS_EMPLEO("empleoCargoComision", "",EnumModulo.I_DATOS_EMPLEO),
    I_AR1_EXPERIENCIA_LABORAL("experienciaLaboral", "", EnumModulo.I_EXPERIENCIA_LABORAL),
    I_AR1_DATOS_PAREJA(EnumModulo.I_DATOS_PAREJA.getModulo(),"", EnumModulo.I_DATOS_PAREJA),    
    I_AR1_DATOS_DEPENDIENTE("dependienteEconomico", "",EnumModulo.I_DATOS_DEPENDIENTE),
    
    I_AR1_INGRESOS_NETOS("actividadIndustrialComercialEmpresarial", "", EnumModulo.I_INGRESOS_NETOS),
    I_AR2_INGRESOS_NETOS("actividadFinanciera", "", EnumModulo.I_INGRESOS_NETOS),
    I_AR3_INGRESOS_NETOS("serviciosProfesionales", "", EnumModulo.I_INGRESOS_NETOS),
    I_AR4_INGRESOS_NETOS("otrosIngresos", "",EnumModulo.I_INGRESOS_NETOS),
    
    //de los siguientes existe un objeto padre llamado 
    //Modulo principal = "actividadAnualAnterior"
    //Objeto hijo = "actividadAnual" el cual contiene a los arreglos hijos siguientes
    I_AR1_ACTIVIDAD_ANUAL_ANT("actividadIndustrialComercialEmpresarial",".", EnumModulo.I_ACTIVIDAD_ANUAL_ANT),//segundo nivel --> actividadAnualAnterior.actividadAnual.actividadIndustrialComercialEmpresarial
    I_AR2_ACTIVIDAD_ANUAL_ANT("actividadFinanciera",".", EnumModulo.I_ACTIVIDAD_ANUAL_ANT),//actividadIndustrialComercialEmpresarial.actividadFinanciera
    I_AR3_ACTIVIDAD_ANUAL_ANT("serviciosProfesionales",".", EnumModulo.I_ACTIVIDAD_ANUAL_ANT),//actividadIndustrialComercialEmpresarial.serviciosProfesionales
    I_AR4_ACTIVIDAD_ANUAL_ANT("enajenacionBienes",".", EnumModulo.I_ACTIVIDAD_ANUAL_ANT),//actividadIndustrialComercialEmpresarial.enajenacionBienes
    I_AR5_ACTIVIDAD_ANUAL_ANT("otrosIngresos",".", EnumModulo.I_ACTIVIDAD_ANUAL_ANT),//actividadIndustrialComercialEmpresarial.otrosIngresos
    
    //bienInmueble es un arreglo padre de arreglos de terceros y transmisores
    I_AR1_BIENES_INMUEBLES("bienInmueble",".", EnumModulo.I_BIENES_INMUEBLES),//tiene hijos terceros y transmisores
    I_AR2_BIENES_INMUEBLES("bienInmueble_terceros","..", EnumModulo.I_BIENES_INMUEBLES),
    I_AR3_BIENES_INMUEBLES("bienInmueble_transmisores","..", EnumModulo.I_BIENES_INMUEBLES),
    
    //vehiculos es un arreglo padre de arreglos de terceros y transmisores
    I_AR1_VEHICULOS("vehiculos",".", EnumModulo.I_VEHICULOS),//tiene hijos terceros y transmisores
    I_AR2_VEHICULOS("vehiculos_terceros","..", EnumModulo.I_VEHICULOS),
    I_AR3_VEHICULOS("vehiculos_transmisores","..", EnumModulo.I_VEHICULOS),
    
    //bienMueble es un arreglo padre de arreglos de terceros y transmisores
    I_AR1_BIENES_MUEBLES("bienMueble", ".", EnumModulo.I_BIENES_MUEBLES),//tiene hijos terceros y transmisores
    I_AR2_BIENES_MUEBLES("bienMueble_terceros","..", EnumModulo.I_BIENES_MUEBLES),
    I_AR3_BIENES_MUEBLES("bienMueble_transmisores","..", EnumModulo.I_BIENES_MUEBLES),
    
    //inversion es un arreglo padre de arreglos de terceros
    I_AR1_INVERSIONES("inversion",".", EnumModulo.I_INVERSIONES),//tiene arreglo hijos      terceros
    I_AR2_INVERSIONES("inversion_terceros","..", EnumModulo.I_INVERSIONES),
    
    //adeudos es un arreglo padre de arreglos de terceros
    I_AR1_ADEUDOS_PASIVOS("adeudos",".", EnumModulo.I_ADEUDOS_PASIVOS),//tiene arreglo hijos    terceros
    I_AR2_ADEUDOS_PASIVOS("adeudos_terceros","..", EnumModulo.I_ADEUDOS_PASIVOS),
    
    I_AR1_PRESTAMO_COMODATO("prestamo","", EnumModulo.I_PRESTAMO_COMODATO),
    
    I_AR1_PARTICIPACION_EMPRESAS("participaciones","", EnumModulo.II_PARTICIPACION_EMPRESAS),
    I_AR1_TOMA_DECISIONES("participaciones","", EnumModulo.II_PARTICIPACION_INSTITUCIONES),
    I_AR1_APOYOS_BENEFICIOS("apoyos","", EnumModulo.II_APOYOS_BENEFICIOS),
    I_AR1_REPRESENTACION("representaciones","", EnumModulo.II_REPRESENTACION),
    I_AR1_CLIENTES_PRINCIPALES("representaciones","", EnumModulo.II_CLIENTES_PRINCIPALES),
    I_AR1_BENEFICIOS_PRIVADOS("beneficios","", EnumModulo.II_BENEFICIOS_PRIVADOS),
    I_AR1_FIDEICOMISOS("fideicomisos","", EnumModulo.II_FIDEICOMISOS)
    ;
    
    String arregoName;
    String refNivel;
    EnumModulo enumModulo;
    
    
    EnumModuloArreglo(String arregoName, String refNivel, EnumModulo enumModulo){
        this.arregoName = arregoName;
        this.refNivel = refNivel;
        this.enumModulo = enumModulo;       
    }
    
    public String getArregoName(){
        return arregoName;
    }
    
    public String getRefNivel(){
        return refNivel;
    }
    
    public EnumModulo  getEnumModulo(){
        return enumModulo;
    }
}
