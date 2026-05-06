package mx.gob.sfp.dgti.declaracion.dto.individual;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;
import mx.gob.sfp.dgti.declaracion.enums.catalogos.EnumNivelGobierno;

/**
 * Enum que contiene las propiedades de nivel gobierno
 * 
 * @since 20/11/2019
 * @autor programador04
 * @editedBy cgarias
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class NivelOrdenGobiernoDTO {
    /**
     * Propiedad que contiene el enum del nivel gobierno.
     */
    private EnumNivelGobierno nivelOrdenGobierno;
    
    /**
     * Propiedad que contiene la entidad federativa en cado de nivel govierno sea diferente de FEDERAL.
     */
    private EntidadFederativaDTO entidadFederativa;

    public NivelOrdenGobiernoDTO() {}

    public NivelOrdenGobiernoDTO(EnumNivelGobierno nivelGobierno, EntidadFederativaDTO entidadFederativa) {
            this.nivelOrdenGobierno = nivelGobierno;
            this.entidadFederativa = entidadFederativa;
    }


    /**
     * Constructor desde JsonObject, utiliza convertidores generados
     *
     * @param json: objeto en JsonObject
     */
    public NivelOrdenGobiernoDTO(JsonObject json) {
    	NivelOrdenGobiernoDTOConverter.fromJson(json, this);
    }

    /**
     * Metodo para obtener el JsonObject, utiliza convertidores generados
     *
     * @return JsonObject a partir del objeto
     */
    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        NivelOrdenGobiernoDTOConverter.toJson(this, json);
        return json;
    }
    
    /**
     * @return the entidadFederativa
     */
    public EntidadFederativaDTO getEntidadFederativa() {
            return entidadFederativa;
    }


    /**
     * @param entidadFederativa the entidadFederativa to set
     */
    public void setEntidadFederativa(EntidadFederativaDTO entidadFederativa) {
            this.entidadFederativa = entidadFederativa;
    }

    public EnumNivelGobierno getNivelOrdenGobierno() {
        return nivelOrdenGobierno;
    }

    public void setNivelOrdenGobierno(EnumNivelGobierno nivelOrdenGobierno) {
        this.nivelOrdenGobierno = nivelOrdenGobierno;
    }
        
}
