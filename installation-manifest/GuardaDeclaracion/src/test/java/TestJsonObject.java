/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Iterator;
import mx.gob.sfp.dgti.util.IExecutJsonUtils;

/**
 *
 * @author cgarias
 */
public class TestJsonObject {
 
    public static void main (String args[]){
        String decla = "{\n" +
"    \"declaracion\": {\n" +
"        \"actividadAnualAnterior\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"actividadAnual\": {\n" +
"                \"actividadFinanciera\": [\n" +
"                    {\n" +
"                        \"id\": \"             5e19168a7a759b534ce5f049\",\n" +
"                        \"idPosicionVista\": \"1\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"                  PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 32132\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 1,\n" +
"                            \"valor\": \"            CAPITAL\"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f04a\",\n" +
"                        \"idPosicionVista\": \"2\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"        PESO MEXICANO         \"\n" +
"                            },\n" +
"                            \"monto\": 1321321\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 2,\n" +
"                            \"valor\": \"       FONDOS DE INVERSIÓN    \"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f04b\",\n" +
"                        \"idPosicionVista\": \"3\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 51351\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 3,\n" +
"                            \"valor\": \"ORGANIZACIONES PRIVADAS\"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f04c\",\n" +
"                        \"idPosicionVista\": \"4\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 351\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 4,\n" +
"                            \"valor\": \"SEGURO DE SEPARACIÓN INDIVIDUALIZADO\"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f04d\",\n" +
"                        \"idPosicionVista\": \"5\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 321\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 5,\n" +
"                            \"valor\": \"VALORES BURSÁTILES\"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f04e\",\n" +
"                        \"idPosicionVista\": \"6\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 2132132\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 6,\n" +
"                            \"valor\": \"BONOS\"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f04f\",\n" +
"                        \"idPosicionVista\": \"7\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 31321\n" +
"                        },\n" +
"                        \"tipoInstrumento\": {\n" +
"                            \"id\": 9999,\n" +
"                            \"valor\": \"OTRO (ESPECIFIQUE)\"\n" +
"                        },\n" +
"                        \"verificar\": true\n" +
"                    }\n" +
"                ],\n" +
"                \"actividadIndustrialComercialEmpresarial\": [\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f048\",\n" +
"                        \"idPosicionVista\": \"\",\n" +
"                        \"nombreRazonSocial\": \"EDER\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 15121\n" +
"                        },\n" +
"                        \"tipoNegocio\": \"KHGLKJH\",\n" +
"                        \"verificar\": true\n" +
"                    }\n" +
"                ],\n" +
"                \"enajenacionBienes\": [\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f051\",\n" +
"                        \"idPosicionVista\": \"1\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 35132\n" +
"                        },\n" +
"                        \"tipoBien\": \"MUEBLE\",\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f052\",\n" +
"                        \"idPosicionVista\": \"1\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 321321\n" +
"                        },\n" +
"                        \"tipoBien\": \"INMUEBLE\",\n" +
"                        \"verificar\": true\n" +
"                    },\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f053\",\n" +
"                        \"idPosicionVista\": \"1\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 32\n" +
"                        },\n" +
"                        \"tipoBien\": \"VEHICULO\",\n" +
"                        \"verificar\": true\n" +
"                    }\n" +
"                ],\n" +
"                \"fechaConclusion\": \"2019-12-05\",\n" +
"                \"fechaInicio\": \"2019-12-03\",\n" +
"                \"ingresoNetoDeclarante\": {\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 9102988\n" +
"                    }\n" +
"                },\n" +
"                \"ingresoNetoParejaDependiente\": {\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 1231321\n" +
"                    }\n" +
"                },\n" +
"                \"otrosIngresos\": [\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f054\",\n" +
"                        \"idPosicionVista\": \"\",\n" +
"                        \"registroHistorico\": false,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 12132\n" +
"                        },\n" +
"                        \"tipoIngreso\": \"KJHFJHG\",\n" +
"                        \"verificar\": true\n" +
"                    }\n" +
"                ],\n" +
"                \"otrosIngresosTotal\": {\n" +
"                    \"moneda\": {\n" +
"                        \"id\": 96,\n" +
"                        \"valor\": \"PESO MEXICANO\"\n" +
"                    },\n" +
"                    \"monto\": 9087988\n" +
"                },\n" +
"                \"remuneracionNetaCargoPublico\": {\n" +
"                    \"moneda\": {\n" +
"                        \"id\": 96,\n" +
"                        \"valor\": \"PESO MEXICANO\"\n" +
"                    },\n" +
"                    \"monto\": 15000\n" +
"                },\n" +
"                \"serviciosProfesionales\": [\n" +
"                    {\n" +
"                        \"id\": \"5e19168a7a759b534ce5f050\",\n" +
"                        \"idPosicionVista\": \"\",\n" +
"                        \"registroHistorico\": true,\n" +
"                        \"remuneracion\": {\n" +
"                            \"moneda\": {\n" +
"                                \"id\": 96,\n" +
"                                \"valor\": \"PESO MEXICANO\"\n" +
"                            },\n" +
"                            \"monto\": 5135321\n" +
"                        },\n" +
"                        \"tipoServicio\": \"LKUHGKJ\",\n" +
"                        \"verificar\": true\n" +
"                    }\n" +
"                ],\n" +
"                \"tipoRemuneracion\": \"ANUAL_ANTERIOR\",\n" +
"                \"totalIngresosNetos\": {\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 10334309\n" +
"                    }\n" +
"                }\n" +
"            },\n" +
"            \"servidorPublicoAnioAnterior\": true\n" +
"        },\n" +
"        \"adeudosPasivos\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"adeudos\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"apoyos\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"apoyos\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"beneficiosPrivados\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"beneficios\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"bienesInmuebles\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"bienesInmuebles\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"bienesMuebles\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"bienesMuebles\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"clientesPrincipales\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"clientes\": null,\n" +
"            \"realizaActividadLucrativa\": false\n" +
"        },\n" +
"        \"datosCurricularesDeclarante\": {\n" +
"            \"aclaracionesObservaciones\": \"datos curriculares\",\n" +
"            \"escolaridad\": [\n" +
"                {\n" +
"                    \"carreraAreaConocimiento\": \"CARERA\",\n" +
"                    \"documentoObtenido\": {\n" +
"                        \"fechaObtencion\": \"2016-10-30\",\n" +
"                        \"tipo\": \"CONSTANCIA\"\n" +
"                    },\n" +
"                    \"estatus\": \"FINALIZADO\",\n" +
"                    \"id\": \"5e14c2fb7a759b534ce5ed9e\",\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"institucionEducativa\": \"IHSKJHVDLK\",\n" +
"                    \"nivel\": {\n" +
"                        \"id\": 1,\n" +
"                        \"valor\": \"PRIMARIA\"\n" +
"                    },\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"tipoOperacion\": \"AGREGAR\",\n" +
"                    \"ubicacion\": \"MEXICO\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ]\n" +
"        },\n" +
"        \"datosDependientesEconomicos\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"datosDependientesEconomicos\": [\n" +
"                {\n" +
"                    \"actividadLaboral\": {\n" +
"                        \"ambitoSector\": {\n" +
"                            \"id\": 2,\n" +
"                            \"valor\": \"PRIVADO\"\n" +
"                        },\n" +
"                        \"ambitoSectorOtro\": \"\",\n" +
"                        \"fechaIngreso\": \"2020-01-01\",\n" +
"                        \"salarioMensualNeto\": 2123,\n" +
"                        \"sectorPrivadoOtro\": {\n" +
"                            \"area\": \"SDCACD\",\n" +
"                            \"empleoCargo\": \"VAR880714D\",\n" +
"                            \"nombreEmpresaSociedadAsociacion\": \"DSACDD\",\n" +
"                            \"proveedorContratistaGobierno\": true,\n" +
"                            \"rfc\": \"VAR831024EQ9\",\n" +
"                            \"sector\": {\n" +
"                                \"id\": 13,\n" +
"                                \"valor\": \"SERVICIOS CORPORATIVOS\"\n" +
"                            },\n" +
"                            \"sectorOtro\": \"\"\n" +
"                        },\n" +
"                        \"sectorPublico\": null\n" +
"                    },\n" +
"                    \"ciudadanoExtranjero\": {\n" +
"                        \"curp\": null,\n" +
"                        \"esExtranjero\": true\n" +
"                    },\n" +
"                    \"datosPersonales\": {\n" +
"                        \"fechaNacimiento\": \"2020-01-01\",\n" +
"                        \"nombre\": \"EDER JAVIER\",\n" +
"                        \"primerApellido\": \"RAMÍREZ\",\n" +
"                        \"rfc\": \"VARM831024EQ9\",\n" +
"                        \"segundoApellido\": \"RAMÍREZ\"\n" +
"                    },\n" +
"                    \"domicilioDependienteEconomico\": {\n" +
"                        \"domicilio\": {\n" +
"                            \"domicilioExtranjero\": null,\n" +
"                            \"domicilioMexico\": {\n" +
"                                \"calle\": \"SDFFD\",\n" +
"                                \"codigoPostal\": \"08620\",\n" +
"                                \"coloniaLocalidad\": \"CDSCSDDCS\",\n" +
"                                \"entidadFederativa\": {\n" +
"                                    \"id\": 9,\n" +
"                                    \"valor\": \"CIUDAD DE MÉXICO\"\n" +
"                                },\n" +
"                                \"municipioAlcaldia\": {\n" +
"                                    \"fk\": 9,\n" +
"                                    \"id\": 9006,\n" +
"                                    \"valor\": \"IZTACALCO\"\n" +
"                                },\n" +
"                                \"numeroExterior\": \"FDSFD\",\n" +
"                                \"numeroInterior\": \"FDSFDSC\"\n" +
"                            },\n" +
"                            \"ubicacion\": \"MEXICO\"\n" +
"                        },\n" +
"                        \"lugarDondeReside\": \"MEXICO\"\n" +
"                    },\n" +
"                    \"esDependienteEconomico\": false,\n" +
"                    \"habitaDomicilioDeclarante\": false,\n" +
"                    \"id\": null,\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"ninguno\": false,\n" +
"                    \"parentescoRelacion\": {\n" +
"                        \"id\": 13,\n" +
"                        \"valor\": \"TÍO (A)\"\n" +
"                    },\n" +
"                    \"parentescoRelacionOtro\": \"\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"tipoOperacion\": \"AGREGAR\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"actividadLaboral\": {\n" +
"                        \"ambitoSector\": {\n" +
"                            \"id\": 9999,\n" +
"                            \"valor\": \"OTRO (ESPECIFIQUE)\"\n" +
"                        },\n" +
"                        \"ambitoSectorOtro\": \"WEFDDW\",\n" +
"                        \"fechaIngreso\": \"2020-01-01\",\n" +
"                        \"salarioMensualNeto\": 15222,\n" +
"                        \"sectorPrivadoOtro\": {\n" +
"                            \"area\": \"DSADSDA\",\n" +
"                            \"empleoCargo\": \"VAR880714D\",\n" +
"                            \"nombreEmpresaSociedadAsociacion\": \"DSADAS\",\n" +
"                            \"proveedorContratistaGobierno\": true,\n" +
"                            \"rfc\": \"VAR880714000\",\n" +
"                            \"sector\": {\n" +
"                                \"id\": 9999,\n" +
"                                \"valor\": \"OTRO (ESPECIFIQUE)\"\n" +
"                            },\n" +
"                            \"sectorOtro\": \"DSADSADSAS\"\n" +
"                        },\n" +
"                        \"sectorPublico\": null\n" +
"                    },\n" +
"                    \"ciudadanoExtranjero\": {\n" +
"                        \"curp\": \"VARE880714HDFRMD99\",\n" +
"                        \"esExtranjero\": false\n" +
"                    },\n" +
"                    \"datosPersonales\": {\n" +
"                        \"fechaNacimiento\": \"2008-10-30\",\n" +
"                        \"nombre\": \"HJKLHLJKNKJ\",\n" +
"                        \"primerApellido\": \"NJKJLNLJK\",\n" +
"                        \"rfc\": \"VARE880714HDF\",\n" +
"                        \"segundoApellido\": \"NKJLN\"\n" +
"                    },\n" +
"                    \"domicilioDependienteEconomico\": {\n" +
"                        \"domicilio\": {\n" +
"                            \"domicilioExtranjero\": null,\n" +
"                            \"domicilioMexico\": {\n" +
"                                \"calle\": \"LÑKJKJ\",\n" +
"                                \"codigoPostal\": \"08620\",\n" +
"                                \"coloniaLocalidad\": \"NJKNJKNJK\",\n" +
"                                \"entidadFederativa\": {\n" +
"                                    \"id\": 9,\n" +
"                                    \"valor\": \"CIUDAD DE MÉXICO\"\n" +
"                                },\n" +
"                                \"municipioAlcaldia\": {\n" +
"                                    \"fk\": 9,\n" +
"                                    \"id\": 9006,\n" +
"                                    \"valor\": \"IZTACALCO\"\n" +
"                                },\n" +
"                                \"numeroExterior\": \"LÑJKLÑJLKMK\",\n" +
"                                \"numeroInterior\": \"OIJHHJKJK\"\n" +
"                            },\n" +
"                            \"ubicacion\": \"MEXICO\"\n" +
"                        },\n" +
"                        \"lugarDondeReside\": \"MEXICO\"\n" +
"                    },\n" +
"                    \"esDependienteEconomico\": false,\n" +
"                    \"habitaDomicilioDeclarante\": false,\n" +
"                    \"id\": null,\n" +
"                    \"idPosicionVista\": \"2\",\n" +
"                    \"ninguno\": false,\n" +
"                    \"parentescoRelacion\": {\n" +
"                        \"id\": 12,\n" +
"                        \"valor\": \"SUEGRO (A)\"\n" +
"                    },\n" +
"                    \"parentescoRelacionOtro\": null,\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"tipoOperacion\": \"AGREGAR\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ],\n" +
"            \"ninguno\": false\n" +
"        },\n" +
"        \"datosEmpleoCargoComision\": {\n" +
"            \"aclaracionesObservaciones\": \"empleo\",\n" +
"            \"empleoCargoComision\": [\n" +
"                {\n" +
"                    \"areaAdscripcion\": \"AREA\",\n" +
"                    \"contratadoPorHonorarios\": true,\n" +
"                    \"domicilio\": {\n" +
"                        \"domicilioExtranjero\": null,\n" +
"                        \"domicilioMexico\": {\n" +
"                            \"calle\": \"CALLE\",\n" +
"                            \"codigoPostal\": \"08620\",\n" +
"                            \"coloniaLocalidad\": \"COLONIA\",\n" +
"                            \"entidadFederativa\": {\n" +
"                                \"id\": 9,\n" +
"                                \"valor\": \"CIUDAD DE MÉXICO\"\n" +
"                            },\n" +
"                            \"municipioAlcaldia\": {\n" +
"                                \"fk\": 9,\n" +
"                                \"id\": 9006,\n" +
"                                \"valor\": \"IZTACALCO\"\n" +
"                            },\n" +
"                            \"numeroExterior\": \"NUMERO\",\n" +
"                            \"numeroInterior\": \"INTERIOR\"\n" +
"                        },\n" +
"                        \"ubicacion\": \"MEXICO\"\n" +
"                    },\n" +
"                    \"empleoCargoComision\": \"EMPLEO\",\n" +
"                    \"ente\": {\n" +
"                        \"ambitoPublico\": \"EJECUTIVO\",\n" +
"                        \"id\": \"5c892fed7eefe633e42cca18\",\n" +
"                        \"nivelOrdenGobierno\": {\n" +
"                            \"entidadFederativa\": null,\n" +
"                            \"nivelOrdenGobierno\": \"FEDERAL\"\n" +
"                        },\n" +
"                        \"nombre\": \"Procuraduría Federal de Protección al Ambiente\"\n" +
"                    },\n" +
"                    \"fechaEncargo\": \"2020-01-01\",\n" +
"                    \"funcionPrincipal\": \"FUNCION\",\n" +
"                    \"id\": \"5e13de8b7a759b534ce5ed82\",\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"nivelEmpleoCargoComision\": \"NIVEL\",\n" +
"                    \"nivelJerarquico\": {\n" +
"                        \"id\": 5,\n" +
"                        \"valor\": \"DIRECTOR (A) DE ÁREA U HOMOLOGO (A)\"\n" +
"                    },\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracionNeta\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 29,\n" +
"                            \"valor\": \"PESO CHILENO\"\n" +
"                        },\n" +
"                        \"monto\": 15000\n" +
"                    },\n" +
"                    \"telefonoOficina\": {\n" +
"                        \"extension\": \"51151\",\n" +
"                        \"numero\": \"5566998877\"\n" +
"                    },\n" +
"                    \"tipoOperacion\": \"AGREGAR\",\n" +
"                    \"tipoRemuneracion\": \"MENSUAL\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ]\n" +
"        },\n" +
"        \"datosGenerales\": {\n" +
"            \"aclaracionesObservaciones\": \"aclara datos generales\",\n" +
"            \"correoElectronico\": {\n" +
"                \"institucional\": \"a@a.com\",\n" +
"                \"personalAlterno\": null\n" +
"            },\n" +
"            \"datosPersonales\": {\n" +
"                \"curp\": \"VARE880714HDFRMD03\",\n" +
"                \"nombre\": \"EDER JAVIER\",\n" +
"                \"primerApellido\": \"VARGAS\",\n" +
"                \"rfc\": \"VARE880714DH9\",\n" +
"                \"segundoApellido\": \"RAMIREZ\"\n" +
"            },\n" +
"            \"nacionalidad\": {\n" +
"                \"id\": 186,\n" +
"                \"valor\": \"MEXICANA\"\n" +
"            },\n" +
"            \"paisNacimiento\": {\n" +
"                \"id\": 204,\n" +
"                \"valor\": \"MÉXICO\"\n" +
"            },\n" +
"            \"regimenMatrimonial\": null,\n" +
"            \"regimenMatrimonialOtro\": null,\n" +
"            \"situacionPersonalEstadoCivil\": {\n" +
"                \"id\": 1,\n" +
"                \"valor\": \"SOLTERO (A)\"\n" +
"            },\n" +
"            \"telefonoCasa\": {\n" +
"                \"numero\": null\n" +
"            },\n" +
"            \"telefonoCelular\": {\n" +
"                \"numero\": \"\",\n" +
"                \"paisCelularPersonal\": null\n" +
"            },\n" +
"            \"verificar\": true\n" +
"        },\n" +
"        \"datosParejas\": {\n" +
"            \"aclaracionesObservaciones\": \"observaciones pareja\",\n" +
"            \"datosParejas\": [\n" +
"                {\n" +
"                    \"actividadLaboral\": {\n" +
"                        \"ambitoSector\": {\n" +
"                            \"id\": 9999,\n" +
"                            \"valor\": \"OTRO (ESPECIFIQUE)\"\n" +
"                        },\n" +
"                        \"ambitoSectorOtro\": \"SDFE\",\n" +
"                        \"fechaIngreso\": \"2020-01-01\",\n" +
"                        \"salarioMensualNeto\": 15151,\n" +
"                        \"sectorPrivadoOtro\": {\n" +
"                            \"area\": \"AREA\",\n" +
"                            \"empleoCargo\": \"VAR880714D\",\n" +
"                            \"nombreEmpresaSociedadAsociacion\": \"FDSAFDSA\",\n" +
"                            \"proveedorContratistaGobierno\": true,\n" +
"                            \"rfc\": \"VAR880714000\",\n" +
"                            \"sector\": {\n" +
"                                \"id\": 10,\n" +
"                                \"valor\": \"SERVICIOS FINANCIEROS\"\n" +
"                            },\n" +
"                            \"sectorOtro\": \"\"\n" +
"                        },\n" +
"                        \"sectorPublico\": null\n" +
"                    },\n" +
"                    \"ciudadanoExtranjero\": {\n" +
"                        \"curp\": \"\",\n" +
"                        \"esExtranjero\": true\n" +
"                    },\n" +
"                    \"datosPersonales\": {\n" +
"                        \"fechaNacimiento\": \"2019-09-13\",\n" +
"                        \"nombre\": \"EDER JAVIER\",\n" +
"                        \"primerApellido\": \"RAMÍREZ\",\n" +
"                        \"rfc\": \"VARM831024EQ9\",\n" +
"                        \"segundoApellido\": \"RAMÍREZ\"\n" +
"                    },\n" +
"                    \"domicilioDependienteEconomico\": {\n" +
"                        \"domicilio\": {\n" +
"                            \"domicilioExtranjero\": null,\n" +
"                            \"domicilioMexico\": {\n" +
"                                \"calle\": \"CALLE\",\n" +
"                                \"codigoPostal\": \"08620\",\n" +
"                                \"coloniaLocalidad\": \"COLONIA\",\n" +
"                                \"entidadFederativa\": {\n" +
"                                    \"id\": 9,\n" +
"                                    \"valor\": \"CIUDAD DE MÉXICO\"\n" +
"                                },\n" +
"                                \"municipioAlcaldia\": {\n" +
"                                    \"fk\": 9,\n" +
"                                    \"id\": 9006,\n" +
"                                    \"valor\": \"IZTACALCO\"\n" +
"                                },\n" +
"                                \"numeroExterior\": \"NKJNKJNKJ\",\n" +
"                                \"numeroInterior\": \"INTERIOR\"\n" +
"                            },\n" +
"                            \"ubicacion\": \"MEXICO\"\n" +
"                        },\n" +
"                        \"lugarDondeReside\": \"MEXICO\"\n" +
"                    },\n" +
"                    \"esDependienteEconomico\": true,\n" +
"                    \"habitaDomicilioDeclarante\": false,\n" +
"                    \"id\": \"5e17681a7a759b534ce5eed0\",\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"ninguno\": false,\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"relacionConDeclarante\": \"CONYUGE\",\n" +
"                    \"tipoOperacion\": \"AGREGAR\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ],\n" +
"            \"ninguno\": false\n" +
"        },\n" +
"        \"domicilioDeclarante\": {\n" +
"            \"aclaracionesObservaciones\": \"domicilio\",\n" +
"            \"domicilio\": {\n" +
"                \"domicilioExtranjero\": null,\n" +
"                \"domicilioMexico\": {\n" +
"                    \"calle\": \"CALLE\",\n" +
"                    \"codigoPostal\": \"08620\",\n" +
"                    \"coloniaLocalidad\": \"COLONIA\",\n" +
"                    \"entidadFederativa\": {\n" +
"                        \"id\": 9,\n" +
"                        \"valor\": \"CIUDAD DE MÉXICO\"\n" +
"                    },\n" +
"                    \"municipioAlcaldia\": {\n" +
"                        \"fk\": 9,\n" +
"                        \"id\": 9006,\n" +
"                        \"valor\": \"IZTACALCO\"\n" +
"                    },\n" +
"                    \"numeroExterior\": \"NUMERO\",\n" +
"                    \"numeroInterior\": \"INTE\"\n" +
"                },\n" +
"                \"ubicacion\": \"MEXICO\"\n" +
"            },\n" +
"            \"verificar\": true\n" +
"        },\n" +
"        \"experienciasLaborales\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"experienciaLaboral\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"fideicomisos\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"fideicomisos\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"ingresos\": {\n" +
"            \"aclaracionesObservaciones\": \"Aclaraciones de ingresos netos\",\n" +
"            \"actividadFinanciera\": [\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed8d\",\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 511\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 1,\n" +
"                        \"valor\": \"CAPITAL\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed8e\",\n" +
"                    \"idPosicionVista\": \"2\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 51531\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 2,\n" +
"                        \"valor\": \"FONDOS DE INVERSIÓN\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed8f\",\n" +
"                    \"idPosicionVista\": \"3\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 2132\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 3,\n" +
"                        \"valor\": \"ORGANIZACIONES PRIVADAS\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed90\",\n" +
"                    \"idPosicionVista\": \"3\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 31312\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 4,\n" +
"                        \"valor\": \"SEGURO DE SEPARACIÓN INDIVIDUALIZADO\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed91\",\n" +
"                    \"idPosicionVista\": \"3\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 133211\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 5,\n" +
"                        \"valor\": \"VALORES BURSÁTILES\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed92\",\n" +
"                    \"idPosicionVista\": \"3\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 351351\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 6,\n" +
"                        \"valor\": \"BONOS\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e14c2ab7a759b534ce5ed93\",\n" +
"                    \"idPosicionVista\": \"3\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 332123\n" +
"                    },\n" +
"                    \"tipoInstrumento\": {\n" +
"                        \"id\": 9999,\n" +
"                        \"valor\": \"OTRO (ESPECIFIQUE)\"\n" +
"                    },\n" +
"                    \"verificar\": false\n" +
"                }\n" +
"            ],\n" +
"            \"actividadIndustrialComercialEmpresarial\": [\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eea8\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"nombreRazonSocial\": \"DSA\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 10000\n" +
"                    },\n" +
"                    \"tipoNegocio\": \"SAD\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eea9\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"nombreRazonSocial\": \"EDER\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 22115\n" +
"                    },\n" +
"                    \"tipoNegocio\": \"SAD\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eeaa\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"nombreRazonSocial\": \"SADAD\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 100\n" +
"                    },\n" +
"                    \"tipoNegocio\": \"DSADA\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ],\n" +
"            \"enajenacionBienes\": [\n" +
"                {\n" +
"                    \"id\": null,\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 0\n" +
"                    },\n" +
"                    \"tipoBien\": \"MUEBLE\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"id\": null,\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 0\n" +
"                    },\n" +
"                    \"tipoBien\": \"INMUEBLE\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"id\": null,\n" +
"                    \"idPosicionVista\": \"1\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 0\n" +
"                    },\n" +
"                    \"tipoBien\": \"VEHICULO\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ],\n" +
"            \"ingresoNetoDeclarante\": {\n" +
"                \"remuneracion\": {\n" +
"                    \"moneda\": {\n" +
"                        \"id\": 96,\n" +
"                        \"valor\": \"PESO MEXICANO\"\n" +
"                    },\n" +
"                    \"monto\": 1084093\n" +
"                }\n" +
"            },\n" +
"            \"ingresoNetoParejaDependiente\": {\n" +
"                \"remuneracion\": {\n" +
"                    \"moneda\": {\n" +
"                        \"id\": 96,\n" +
"                        \"valor\": \"PESO MEXICANO\"\n" +
"                    },\n" +
"                    \"monto\": 15152\n" +
"                }\n" +
"            },\n" +
"            \"otrosIngresos\": [\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eead\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 2131\n" +
"                    },\n" +
"                    \"tipoIngreso\": \"DSASDSA\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eeae\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"registroHistorico\": false,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 13213\n" +
"                    },\n" +
"                    \"tipoIngreso\": \"CDASDASD\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ],\n" +
"            \"otrosIngresosTotal\": {\n" +
"                \"moneda\": {\n" +
"                    \"id\": 96,\n" +
"                    \"valor\": \"PESO MEXICANO\"\n" +
"                },\n" +
"                \"monto\": 1074093\n" +
"            },\n" +
"            \"remuneracionNetaCargoPublico\": {\n" +
"                \"moneda\": {\n" +
"                    \"id\": 96,\n" +
"                    \"valor\": \"PESO MEXICANO\"\n" +
"                },\n" +
"                \"monto\": 10000\n" +
"            },\n" +
"            \"serviciosProfesionales\": [\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eeab\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"registroHistorico\": true,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 1231\n" +
"                    },\n" +
"                    \"tipoServicio\": \"DSFDSF\",\n" +
"                    \"verificar\": true\n" +
"                },\n" +
"                {\n" +
"                    \"id\": \"5e1752b97a759b534ce5eeac\",\n" +
"                    \"idPosicionVista\": \"\",\n" +
"                    \"registroHistorico\": true,\n" +
"                    \"remuneracion\": {\n" +
"                        \"moneda\": {\n" +
"                            \"id\": 96,\n" +
"                            \"valor\": \"PESO MEXICANO\"\n" +
"                        },\n" +
"                        \"monto\": 123132\n" +
"                    },\n" +
"                    \"tipoServicio\": \"DSADSAD\",\n" +
"                    \"verificar\": true\n" +
"                }\n" +
"            ],\n" +
"            \"tipoRemuneracion\": \"         MENSUAL         \",\n" +
"            \"totalIngresosNetos\": {\n" +
"                \"remuneracion\": {\n" +
"                    \"moneda\": {\n" +
"                        \"id\": 96,\n" +
"                        \"valor\": \"PESO MEXICANO\"\n" +
"                    },\n" +
"                    \"monto\": 1099245\n" +
"                }\n" +
"            }\n" +
"        },\n" +
"        \"inversionesCuentasValores\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"inversion\": null,\n" +
"            \"ninguno\": true\n" +
"        },\n" +
"        \"participaEmpresasSociedadesAsociaciones\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"ninguno\": true,\n" +
"            \"participaciones\": null\n" +
"        },\n" +
"        \"participacionTomaDecisiones\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"ninguno\": true,\n" +
"            \"participaciones\": null\n" +
"        },\n" +
"        \"prestamoComodato\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"ninguno\": true,\n" +
"            \"prestamo\": null\n" +
"        },\n" +
"        \"representaciones\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"ninguno\": true,\n" +
"            \"representaciones\": null\n" +
"        },\n" +
"        \"vehiculos\": {\n" +
"            \"aclaracionesObservaciones\": \"\",\n" +
"            \"ninguno\": true,\n" +
"            \"vehiculos\": null\n" +
"        }\n" +
"    },\n" +
"    \"encabezado\": {\n" +
"        \"anio\": 2020,\n" +
"        \"fechaActualizacion\": \"2020-01-13T14:17:12.237\",\n" +
"        \"fechaEncargo\": \"2020-01-01\",\n" +
"        \"fechaRegistro\": \"2020-01-06T06:06:41.192\",\n" +
"        \"tipoDeclaracion\": \"INICIO\",\n" +
"        \"tipoFormato\": \"COMPLETO\",\n" +
"        \"usuario\": {\n" +
"            \"curp\": \"VARE880714HDFRMD03\",\n" +
"            \"idUsuario\": 523032,\n" +
"            \"id_movimiento\": null\n" +
"        },\n" +
"        \"versionDeclaracion\": 20200101,\n" +
"        \"numeroDeclaracion\": \"5e13cc827a759b534ce5ed78\"\n" +
"    },\n" +
"    \"firmada\": true,\n" +
"    \"institucionReceptora\": {\n" +
"        \"_id\": \"5dee85a712754a31dfa3e8b5\",\n" +
"        \"collName\": 100,\n" +
"        \"ente\": {\n" +
"            \"ambitoPublico\": \"EJECUTIVO\",\n" +
"            \"id\": \"5c892fec7eefe633e42cc985\",\n" +
"            \"nivelOrdenGobierno\": {\n" +
"                \"nivelOrdenGobierno\": \"FEDERAL\"\n" +
"            },\n" +
"            \"nombre\": \"            Secretaría de Función Pública SFP                   \"\n" +
"        },\n" +
"        \"nombre\": \"                 Secreataria de la Funcion Publica Federal                 \"\n" +
"    }\n" +
"}";
        
        
        JsonObject jsonObject = new JsonObject(decla);
        IExecutJsonUtils.excutTrimJson(jsonObject, jsonObject.fieldNames().iterator());
        new TestJsonObject().imprimeValores(jsonObject, jsonObject.fieldNames().iterator());
    }
    
//    public void iterarHijos(JsonObject jsonObject, Iterator<String> keys){
//        while(keys.hasNext()) {
//            String key = keys.next();
//            if (jsonObject.getValue(key) instanceof String) {
//                  jsonObject.put(key, jsonObject.getString(key).trim());
//            }
//            if (jsonObject.getValue(key) instanceof JsonObject) {
//                  iterarHijos(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator());
//            }
//            if (jsonObject.getValue(key) instanceof JsonArray) {
//                  for(Object index: jsonObject.getJsonArray(key)){
//                    iterarHijos((JsonObject)index, ((JsonObject)index).fieldNames().iterator());
//                  }
//            }
//        }
//    }
    
    public void imprimeValores(JsonObject jsonObject, Iterator<String> keys){
        while(keys.hasNext()) {
            String key = keys.next();
            if (jsonObject.getValue(key) instanceof String) {
                  System.out.println(jsonObject.getString(key));
            }
            if (jsonObject.getValue(key) instanceof JsonObject) {
                  imprimeValores(jsonObject.getJsonObject(key), jsonObject.getJsonObject(key).fieldNames().iterator());
            }
            if (jsonObject.getValue(key) instanceof JsonArray) {
                  for(Object index: jsonObject.getJsonArray(key)){
                    imprimeValores((JsonObject)index, ((JsonObject)index).fieldNames().iterator());
                  }
            }
        }
    }
}
