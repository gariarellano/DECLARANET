/**
 * @(#)DatosRuspDTO.java 08/03/2021
 *
 * Copyright (C) 2021 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.impresion;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO para los parametros del servicio de impresion
 *
 * @author pavel.martinez
 * @since 08/03/2021
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ParamsIntsImpresionOmisosVistaDTO {

    /**
     * Parametros servicio impresion vistas curp
     */
    private String curp;

    /**
     * Parametros servicio impresion vistas nombre
     */
    private String nombre;

    /**
     * Parametros servicio impresion vistas uniAdm
     */
    private String uniAdm;

    /**
     * Parametros servicio impresion vistas empleo
     */
    private String empleo;

    /**
     * Constructor
     */
    public ParamsIntsImpresionOmisosVistaDTO(){//Constructor
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ParamsIntsImpresionOmisosVistaDTO(JsonObject json) {
        ParamsIntsImpresionOmisosVistaDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ParamsIntsImpresionOmisosVistaDTOConverter.toJson(this, json);
        return json;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUniAdm() {
        return uniAdm;
    }

    public void setUniAdm(String uniAdm) {
        this.uniAdm = uniAdm;
    }

    public String getEmpleo() {
        return empleo;
    }

    public void setEmpleo(String empleo) {
        this.empleo = empleo;
    }

    @Override
    public String toString() {
        return "ParamsIntsImpresionOmisosVistaDTO{" +
                "curp='" + curp + '\'' +
                ", nombre='" + nombre + '\'' +
                ", uniAdm='" + uniAdm + '\'' +
                ", empleo='" + empleo + '\'' +
                '}';
    }
}
