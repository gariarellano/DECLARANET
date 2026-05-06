/**
 * @(#)EnumCatalogosGrandes.java 18/12/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.base;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * Enum de CatalgosGrandes
 * @author pavel.martinez
 * @since 18/12/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class ModuloBaseDTO {

    /**
     * Encabezado
     */
    private EncabezadoDTO encabezado;

    /**
     * Aclaraciones del modulo
     */
    private String aclaracionesObservaciones;

    /**
     * Constructor
     */
    public ModuloBaseDTO(){ };

    /**
     * Constructor
     *
     * @param encabezado encabezado con datos que se necesitan
     * @param aclaracionesObservaciones aclaraciones del modulo
     */
    public ModuloBaseDTO(EncabezadoDTO encabezado, String aclaracionesObservaciones) {
        this.encabezado = encabezado;
        this.aclaracionesObservaciones = aclaracionesObservaciones;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public ModuloBaseDTO(JsonObject json) {
        ModuloBaseDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        ModuloBaseDTOConverter.toJson(this, json);
        return json;
    }

    /**
     * @return the aclaracionesObservaciones
     */
    public String getAclaracionesObservaciones() {
        return aclaracionesObservaciones;
    }
    /**
     * @param aclaracionesObservaciones the aclaracionesObservaciones to set
     */
    public void setAclaracionesObservaciones(String aclaracionesObservaciones) {
        this.aclaracionesObservaciones = aclaracionesObservaciones;
    }

    public EncabezadoDTO getEncabezado() {
        return encabezado;
    }

    public void setEncabezado(EncabezadoDTO encabezado) {
        this.encabezado = encabezado;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ModuloBaseDTO{");
        sb.append("encabezado=").append(encabezado);
        sb.append(", aclaracionesObservaciones='").append(aclaracionesObservaciones).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
