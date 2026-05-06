package mx.gob.sfp.dgti.dto;

import io.vertx.core.json.JsonObject;
import io.vertx.core.json.JsonArray;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Converter for {@link mx.gob.sfp.dgti.dto.FirmaDTO}.
 * NOTE: This class has been automatically generated from the {@link mx.gob.sfp.dgti.dto.FirmaDTO} original class using Vert.x codegen.
 */
public class FirmaDTOConverter {

  public static void fromJson(Iterable<java.util.Map.Entry<String, Object>> json, FirmaDTO obj) {
    for (java.util.Map.Entry<String, Object> member : json) {
      switch (member.getKey()) {
        case "acuse":
          if (member.getValue() instanceof JsonObject) {
            obj.setAcuse(new mx.gob.sfp.dgti.dto.AcuseDTO((JsonObject)member.getValue()));
          }
          break;
        case "code":
          if (member.getValue() instanceof Number) {
            obj.setCode(((Number)member.getValue()).intValue());
          }
          break;
        case "collName":
          if (member.getValue() instanceof Number) {
            obj.setCollName(((Number)member.getValue()).intValue());
          }
          break;
        case "declaracion":
          if (member.getValue() instanceof String) {
            obj.setDeclaracion((String)member.getValue());
          }
          break;
        case "digest":
          if (member.getValue() instanceof String) {
            obj.setDigest((String)member.getValue());
          }
          break;
        case "digestion":
          if (member.getValue() instanceof JsonObject) {
            obj.setDigestion(new mx.gob.sfp.dgti.dto.DigestDTO((JsonObject)member.getValue()));
          }
          break;
        case "fechaId":
          if (member.getValue() instanceof String) {
            obj.setFechaId((String)member.getValue());
          }
          break;
        case "fechaRecepcion":
          if (member.getValue() instanceof String) {
            obj.setFechaRecepcion((String)member.getValue());
          }
          break;
        case "firmado":
          if (member.getValue() instanceof JsonObject) {
            obj.setFirmado(new mx.gob.sfp.dgti.dto.BitacoraDTO((JsonObject)member.getValue()));
          }
          break;
        case "idUsuario":
          if (member.getValue() instanceof Number) {
            obj.setIdUsuario(((Number)member.getValue()).intValue());
          }
          break;
        case "noComprobante":
          if (member.getValue() instanceof String) {
            obj.setNoComprobante((String)member.getValue());
          }
          break;
        case "numeroDeclaracion":
          if (member.getValue() instanceof String) {
            obj.setNumeroDeclaracion((String)member.getValue());
          }
          break;
        case "rfc":
          if (member.getValue() instanceof String) {
            obj.setRfc((String)member.getValue());
          }
          break;
        case "tipoFirma":
          if (member.getValue() instanceof String) {
            obj.setTipoFirma(mx.gob.sfp.dgti.util.EnumTipoFirma.valueOf((String)member.getValue()));
          }
          break;
        case "token":
          if (member.getValue() instanceof String) {
            obj.setToken((String)member.getValue());
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

  public static void toJson(FirmaDTO obj, JsonObject json) {
    toJson(obj, json.getMap());
  }

  public static void toJson(FirmaDTO obj, java.util.Map<String, Object> json) {
    if (obj.getAcuse() != null) {
      json.put("acuse", obj.getAcuse().toJson());
    }
    if (obj.getCode() != null) {
      json.put("code", obj.getCode());
    }
    if (obj.getCollName() != null) {
      json.put("collName", obj.getCollName());
    }
    if (obj.getDeclaracion() != null) {
      json.put("declaracion", obj.getDeclaracion());
    }
    if (obj.getDigest() != null) {
      json.put("digest", obj.getDigest());
    }
    if (obj.getDigestion() != null) {
      json.put("digestion", obj.getDigestion().toJson());
    }
    if (obj.getFechaId() != null) {
      json.put("fechaId", obj.getFechaId());
    }
    if (obj.getFechaRecepcion() != null) {
      json.put("fechaRecepcion", obj.getFechaRecepcion());
    }
    if (obj.getFirmado() != null) {
      json.put("firmado", obj.getFirmado().toJson());
    }
    if (obj.getIdUsuario() != null) {
      json.put("idUsuario", obj.getIdUsuario());
    }
    if (obj.getNoComprobante() != null) {
      json.put("noComprobante", obj.getNoComprobante());
    }
    if (obj.getNumeroDeclaracion() != null) {
      json.put("numeroDeclaracion", obj.getNumeroDeclaracion());
    }
    if (obj.getRfc() != null) {
      json.put("rfc", obj.getRfc());
    }
    if (obj.getTipoFirma() != null) {
      json.put("tipoFirma", obj.getTipoFirma().name());
    }
    if (obj.getToken() != null) {
      json.put("token", obj.getToken());
    }
    if (obj.getTransaccion() != null) {
      json.put("transaccion", obj.getTransaccion());
    }
  }
}
