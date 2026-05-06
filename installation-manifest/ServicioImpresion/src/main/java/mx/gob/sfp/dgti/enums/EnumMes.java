/**
 *
 */
package mx.gob.sfp.dgti.enums;

/**
 * @author programador09@sfp.gob.mx
 *
 */
public enum EnumMes {

    MES01("01", "ENERO"), MES02("02", "FEBRERO"), MES03("03", "MARZO"), MES04("04", "ABRIL"), MES05("05", "MAYO"),
    MES06("06", "JUNIO"), MES07("07", "JULIO"), MES08("08", "AGOSTO"), MES09("09", "SEPTIEMBRE"),
    MES10("10", "OCTUBRE"), MES11("11", "NOVIEMBRE"), MES12("12", "DICIEMBRE");

    String clave;
    String decripcion;

    /**
     * @param clave
     * @param decripcion
     */
    private EnumMes(String clave, String decripcion) {
        this.clave = clave;
        this.decripcion = decripcion;
    }

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @return the decripcion
     */
    public String getDecripcion() {
        return decripcion;
    }
}
