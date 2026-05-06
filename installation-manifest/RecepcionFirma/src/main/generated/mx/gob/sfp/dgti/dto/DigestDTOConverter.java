package mx.gob.sfp.dgti.dto;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link mx.gob.sfp.dgti.dto.DigestDTO}.
 * NOTE: This class has been automatically generated from the {@link mx.gob.sfp.dgti.dto.DigestDTO} original class using Vert.x codegen.
 */
public class DigestDTOConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, DigestDTO obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "collName":
          if (member.getValue() instanceof Number) {
            obj.setCollName(((Number)member.getValue()).intValue());
          }
          break;
        case "declaracion":
          if (member.getValue() instanceof JsonObject) {
            obj.setDeclaracion(((JsonObject)member.getValue()).copy());
          }
          break;
        case "digestionDcn":
          if (member.getValue() instanceof String) {
            obj.setDigestionDcn((String)member.getValue());
          }
          break;
        case "idUsuario":
          if (member.getValue() instanceof Number) {
            obj.setIdUsuario(((Number)member.getValue()).intValue());
          }
          break;
        case "numeroDeclaracion":
          if (member.getValue() instanceof String) {
            obj.setNumeroDeclaracion((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(DigestDTO obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(DigestDTO obj, java.util.Map<String, Object> json) {
    if (obj.getCollName() != null) {
      json.put("collName", obj.getCollName());
    }
    if (obj.getDeclaracion() != null) {
      json.put("declaracion", obj.getDeclaracion());
    }
    if (obj.getDigestionDcn() != null) {
      json.put("digestionDcn", obj.getDigestionDcn());
    }
    if (obj.getIdUsuario() != null) {
      json.put("idUsuario", obj.getIdUsuario());
    }
    if (obj.getNumeroDeclaracion() != null) {
      json.put("numeroDeclaracion", obj.getNumeroDeclaracion());
    }
  }
}
