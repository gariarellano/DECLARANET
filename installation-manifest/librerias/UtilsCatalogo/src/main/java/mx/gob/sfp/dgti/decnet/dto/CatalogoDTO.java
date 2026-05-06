/**
 * @(#)CatalogoDTO.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.dto;

import java.util.Objects;

import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * DTO generico para los catalogos
 *
 * @author Miriam Sanchez Sanchez programador07
 * @since 30/05/2019
 */
@DataObject(generateConverter = true, inheritConverter = true)
public class CatalogoDTO {

	/**
	 * Id del elemento del catalogo.
	 */
	private Integer id;

	/**
	 * Descripcion del elemento del catalogo.
	 */
	private String valor;

	/**
	 * Descripcion del elemento del catalogo.
	 */
	private String valorUno;

	/**
	 * FK
	 */
	private Integer fk;

	/**
	 * Indica si el elemento se encuentra activo(1) o inactivo(0).
	 */
	private Integer activo;

	/**
	 * Fecha de registro del elemento
	 */
	private String fechaRegistro;

	/**
	 * Constructor
	 */
	public CatalogoDTO(){ };

	public CatalogoDTO(Integer id, String valor, String valorUno, Integer fk, Integer activo, String fechaRegistro) {
		this.id = id;
		this.valor = valor;
		this.valorUno = valorUno;
		this.fk = fk;
		this.activo = activo;
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * Constructor desde JsonObject, utiliza convertidores generados
	 *
	 * @param json: objeto en JsonObject
	 */
	public CatalogoDTO(JsonObject json) {
		CatalogoDTOConverter.fromJson(json, this);
	}

	/**
	 * Metodo para obtener el JsonObject, utiliza convertidores generados
	 *
	 * @return JsonObject a partir del objeto
	 */
	public JsonObject toJson() {
		JsonObject json = new JsonObject();
		CatalogoDTOConverter.toJson(this, json);
		return json;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getValorUno() {
		return valorUno;
	}

	public void setValorUno(String valorUno) {
		this.valorUno = valorUno;
	}

	public Integer getFk() {
		return fk;
	}

	public void setFk(Integer fk) {
		this.fk = fk;
	}

	public Integer getActivo() {
		return activo;
	}

	public void setActivo(Integer activo) {
		this.activo = activo;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * Metodo toString
	 *
	 * @return
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("CatalogoDTO{");
		sb.append("id=").append(id);
		sb.append(", valor='").append(valor).append('\'');
		sb.append(", valorUno='").append(valorUno).append('\'');
		sb.append(", fk=").append(fk);
		sb.append(", activo=").append(activo);
		sb.append(", fechaRegistro='").append(fechaRegistro).append('\'');
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof CatalogoDTO)) return false;
		CatalogoDTO that = (CatalogoDTO) o;
		return getId().equals(that.getId()) &&
				getValor().equals(that.getValor()) &&
				Objects.equals(getValorUno(), that.getValorUno()) &&
				Objects.equals(getFk(), that.getFk());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId(), getValor());
	}
}
