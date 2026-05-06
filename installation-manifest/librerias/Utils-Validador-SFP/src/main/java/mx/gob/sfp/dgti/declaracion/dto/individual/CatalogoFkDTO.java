/**
 * @(#)CatalogoUnoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para catalogos que incluyen un campo extra
 * @author pavel.martinez
 * @since 07/11/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CatalogoFkDTO extends CatalogoDTO{

    private Integer fk;

	/**
     * Constructor
     */
    public CatalogoFkDTO(){ };

    public CatalogoFkDTO(Integer id, String valor, Integer fk) {
        super(id, valor);
        this.fk = fk;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public CatalogoFkDTO(JsonObject json) {
    	CatalogoFkDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CatalogoFkDTOConverter.toJson(this, json);
        return json;
    }

    public Integer getFk() {
        return fk;
    }

    public void setFk(Integer fk) {
        this.fk = fk;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CatalogoFkDTO{");
        sb.append("fk=").append(fk);
        sb.append('}');
        return sb.toString();
    }
}
