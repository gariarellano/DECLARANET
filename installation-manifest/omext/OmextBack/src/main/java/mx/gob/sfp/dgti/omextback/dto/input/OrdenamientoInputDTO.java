/**
 * @(#)OrdenamientoInputDTO.java 19/05/2020
 *
 * Copyright (C) 2020 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.omextback.dto.input;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumCampoOrden;
import mx.gob.sfp.dgti.omextback.util.constantes.EnumOrden;

/**
 * DTO para indicar el ordenamiento
 *
 * @author pavel.martinez
 * @since 19/05/2020
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class OrdenamientoInputDTO {

    /**
     * Indica el tipo de ordenamiento, ascendente o desdendente
     */
    private EnumOrden orden;

    /**
     * Indica el campo por el que se va a realizar el ordenamiento
     */
    private EnumCampoOrden campo;

    /**
     * Constructor
     */
    public OrdenamientoInputDTO(){//Constructor
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public OrdenamientoInputDTO(JsonObject json) {
        OrdenamientoInputDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        OrdenamientoInputDTOConverter.toJson(this, json);
        return json;
    }

    public EnumOrden getOrden() {
        return orden;
    }

    public void setOrden(EnumOrden orden) {
        this.orden = orden;
    }

    public EnumCampoOrden getCampo() {
        return campo;
    }

    public void setCampo(EnumCampoOrden campo) {
        this.campo = campo;
    }

    /**
     * Metodo toString
     *
     * @return
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrdenamientoInputDTO{");
        sb.append("orden=").append(orden);
        sb.append(", campo=").append(campo);
        sb.append('}');
        return sb.toString();
    }
}
