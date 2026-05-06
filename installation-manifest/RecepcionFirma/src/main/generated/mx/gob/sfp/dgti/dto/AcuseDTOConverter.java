package mx.gob.sfp.dgti.dto;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link mx.gob.sfp.dgti.dto.AcuseDTO}.
 * NOTE: This class has been automatically generated from the {@link mx.gob.sfp.dgti.dto.AcuseDTO} original class using Vert.x codegen.
 */
public class AcuseDTOConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, AcuseDTO obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "digestionAcuse":
          if (member.getValue() instanceof String) {
            obj.setDigestionAcuse((String)member.getValue());
          }
          break;
        case "firmaAcuse":
          if (member.getValue() instanceof String) {
            obj.setFirmaAcuse((String)member.getValue());
          }
          break;
        case "idFirmante":
          if (member.getValue() instanceof String) {
            obj.setIdFirmante((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(AcuseDTO obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(AcuseDTO obj, java.util.Map<String, Object> json) {
    if (obj.getDigestionAcuse() != null) {
      json.put("digestionAcuse", obj.getDigestionAcuse());
    }
    if (obj.getFirmaAcuse() != null) {
      json.put("firmaAcuse", obj.getFirmaAcuse());
    }
    if (obj.getIdFirmante() != null) {
      json.put("idFirmante", obj.getIdFirmante());
    }
  }
}
