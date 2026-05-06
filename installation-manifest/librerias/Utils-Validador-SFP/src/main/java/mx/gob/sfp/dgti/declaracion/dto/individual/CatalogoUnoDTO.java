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
public class CatalogoUnoDTO extends CatalogoDTO{

    private String valorUno;

	/**
     * Constructor
     */
    public CatalogoUnoDTO(){ };

    public CatalogoUnoDTO(Integer id, String valor, String valorUno) {
        super(id, valor);
        this.valorUno = valorUno;
    }

    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     * @param json objeto en JsonObject
     */
    public CatalogoUnoDTO(JsonObject json) {
    	CatalogoUnoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        CatalogoUnoDTOConverter.toJson(this, json);
        return json;
    }

    public String getValorUno() {
        return valorUno;
    }

    public void setValorUno(String valorUno) {
        this.valorUno = valorUno;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CatalogoUnoDTO{");
        sb.append("valorUno='").append(valorUno).append('\'');
        sb.append("super='").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}
