package mx.gob.sfp.dgti.dto;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link mx.gob.sfp.dgti.dto.BitacoraDTO}.
 * NOTE: This class has been automatically generated from the {@link mx.gob.sfp.dgti.dto.BitacoraDTO} original class using Vert.x codegen.
 */
public class BitacoraDTOConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, BitacoraDTO obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "codigoSha":
          if (member.getValue() instanceof String) {
            obj.setCodigoSha((String)member.getValue());
          }
          break;
        case "curp":
          if (member.getValue() instanceof String) {
            obj.setCurp((String)member.getValue());
          }
          break;
        case "estatus":
          if (member.getValue() instanceof Number) {
            obj.setEstatus(((Number)member.getValue()).intValue());
          }
          break;
        case "folio":
          if (member.getValue() instanceof String) {
            obj.setFolio((String)member.getValue());
          }
          break;
        case "nombreFirm":
          if (member.getValue() instanceof String) {
            obj.setNombreFirm((String)member.getValue());
          }
          break;
        case "numeroCertificado":
          if (member.getValue() instanceof String) {
            obj.setNumeroCertificado((String)member.getValue());
          }
          break;
        case "respuestaValidacion":
          if (member.getValue() instanceof String) {
            obj.setRespuestaValidacion((String)member.getValue());
          }
          break;
        case "rfc":
          if (member.getValue() instanceof String) {
            obj.setRfc((String)member.getValue());
          }
          break;
        case "transaccion":
          if (member.getValue() instanceof String) {
            obj.setTransaccion((String)member.getValue());
          }
          break;
      }
    }
  }

  public static void toJson(BitacoraDTO obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(BitacoraDTO obj, java.util.Map<String, Object> json) {
    if (obj.getCodigoSha() != null) {
      json.put("codigoSha", obj.getCodigoSha());
    }
    if (obj.getCurp() != null) {
      json.put("curp", obj.getCurp());
    }
    if (obj.getEstatus() != null) {
      json.put("estatus", obj.getEstatus());
    }
    if (obj.getFolio() != null) {
      json.put("folio", obj.getFolio());
    }
    if (obj.getNombreFirm() != null) {
      json.put("nombreFirm", obj.getNombreFirm());
    }
    if (obj.getNumeroCertificado() != null) {
      json.put("numeroCertificado", obj.getNumeroCertificado());
    }
    if (obj.getRespuestaValidacion() != null) {
      json.put("respuestaValidacion", obj.getRespuestaValidacion());
    }
    if (obj.getRfc() != null) {
      json.put("rfc", obj.getRfc());
    }
    if (obj.getTransaccion() != null) {
      json.put("transaccion", obj.getTransaccion());
    }
  }
}
