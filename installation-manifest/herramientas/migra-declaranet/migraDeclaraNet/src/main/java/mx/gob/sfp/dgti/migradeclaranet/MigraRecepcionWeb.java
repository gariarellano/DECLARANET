/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.gob.sfp.dgti.migradeclaranet;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.bson.Document;

/**
 *
 * @author jzamudio
 */
public class MigraRecepcionWeb {

    //ANTES DE EJECUTAR EL PROYECTO ES IMPORTANTE QUE SE AJUSTEN LOS VALORES QUE SE ENLISTAN A CONTINUACIÓN CON LOS GENERADOS
    //EN LA NUEVA BASE DE DATOS DE DECLARANET MONGODB.
    //************AJUSTAR ESTOS VALORES CON LOS PROPIOS************
    //ID DEL ENTE RECEPTOR ASIGNADO EN LA COLECCIÓN enteReceptorDeclaracion DE LA BASE DECLARANET MONGODB
    String idEnteReceptor = "123456";
    //COLLNAME DEL ENTE RECEPTOR ASIGNADO EN LA COLECCIÓN enteReceptorDeclaracion DE LA BASE DECLARANET MONGODB
    String collName = "100";
    //ID DE LA DEPENDENCIA ASIGNADO EN EL NUEVO CATÁLOGO DE ENTES (MONGODB)
    String idDependencia = "123456";
    //NOMBRE DE LA DEPENDENCIA COMO FUE REGISTRADO EN EL NUEVO CATÁLOGO DE ENTES (MONGODB)
    String nombreDependencia = "CAMBIAR VALOR DEPENDENCIA";
    //ORDEN DE GOBIERNO COMO FUE REGISTRADO EN EL NUEVO CATÁLOGO DE ENTES (MONGODB)
    String ordenGobiernoEnteReceptor = "CAMBIAR VALOR ORDEN";
    //AMBITO DE GOBIERNO COMO FUE REGISTRADO EN EL NUEVO CATÁLOGO DE ENTES (MONGODB)
    String ambitoGobiernoEnteReceptor = "CAMBIAR VALOR AMBITO";
    //NOMBRE DE LA ENTIDAD FEDERATIVA COMO FUE REGISTRADO EN EL CATÁLOGO DE ENTES (MONGO)
    String entidadFederativaValor = "";
    //ID DE LA ENTIDAD FEDERATIVA COMO FUE REGISTRADO EN EL CATÁLOGO DE ENTES (MONGO)
    int entidadFederativaId = 0;
    //*************************************************************

    //REMPLAZAR LOS SIGUIENTES VALORES CON LOS DATOS DE CONECCIÓN A LA BASE DE DATOS ORACLE, Y ANTES DE EJECUTARLO 
    //SE DEBERÁN CREAR EN LA BASE DE DATOS LAS FUNCIONES QUE SE MENCIONAN EN EL ARCHIVO GUIA_ACUSES.TXT
    //************DATOS DE CONEXIÓN A LAS BASES DE DATOS************
    //*****ORACLE
    //IP DEL HOST DE LA BASE DE DATOS
    String or_host_name = "CAMBIAR HOST";
    //PUERTO DE LA BASE
    String or_port_no = "CAMBIAR PUERTO";
    //NOMBRE DE BASE DE DATOS O SID
    String or_dn_name = "CAMBIAR NOMBRE DE BASE";
    //USUARIO
    String or_usuario = "CAMBIAR USUARIO";
    //CONTRASEÑA
    String or_pwd = "CAMBIAR CONTRASEÑA";

    //*****MONGODB
    //PUERTO DE LA BASE
    int mdb_port_no = 27000; //CAMBIAR VALOR
    //IP DEL HOST DE LA BASE DE DATOS
    String mdb_host_name = "CAMBIAR HOST";
    //NOMBRE DE LA BASE DE DATOS
    String mdb_db_name = "CAMBIAR BASE";
    //USUARIO
    String mdb_usuario = "CAMBIAR USUARIO";
    //CONTRASEÑA
    String mdb_pwd = "CAMBIAR CONTRASEÑA";
    //*************************************************************    

    java.sql.Connection connOr = null;

    public static void main(String[] args) {
        MigraRecepcionWeb migraM = new MigraRecepcionWeb();

        System.out.println("**** MIGRA ACUSES DE DECLARACIONES ****");
        migraM.migraOracleMongo(migraM.declaraciones()); //Migra los acuses de declaracion

        System.out.println("**** MIGRA ACUSES DE NOTAS ****");
        migraM.migraOracleMongo(migraM.notas()); //Migra los acuses de notas aclaratorias

    }

    public void migraOracleMongo(String query) {
        StringBuilder sb = new StringBuilder();
        PreparedStatement stmt;

        MigraRecepcionWeb migraM = new MigraRecepcionWeb();

        //********ORACLE*********
        try {
            getOracleConn(or_host_name, or_port_no, or_dn_name, or_usuario, or_pwd);
        } catch (SQLException ex) {
            System.out.println("Error al intentar conectarse a ORACLE " + ex.getLocalizedMessage());
        }

        Integer i = 0;
        Integer err = 0;

        //*********MONGO********
        String client_url = "mongodb://" + mdb_usuario + ":" + mdb_pwd + "@" + mdb_host_name + ":" + mdb_port_no + "/" + mdb_db_name;
        MongoClientURI uri = new MongoClientURI(client_url);
        // Connecting to the mongodb server using the given client uri.
        MongoClient mongo_client = new MongoClient(uri);
        // Fetching the database from the mongodb.
        MongoDatabase db = mongo_client.getDatabase(mdb_db_name);
        // Fetching the collection from the mongodb.
        MongoCollection<Document> coll = db.getCollection("recepcionWeb" + collName);

        //**********OBTIENE LOS DOCUMENTOS DE ORACLE******
        try {
            stmt = connOr.prepareStatement(query);
            ResultSet rset;
            rset = stmt.executeQuery();

            System.out.println("****INICIA CARGA****");

            System.out.println("Documento: ");
            while (rset.next()) {
                i++;

                try {
                    addOneDocument(coll, rset.getString(1));
                } catch (MongoWriteException e) {
                    System.out.println("Error en el registro: " + i + " - " + rset.getString(1).substring(1, rset.getString(1).indexOf("versionRecepcionWeb") - 1) + " - " + e.getMessage());
                    err++;
                } catch (org.bson.json.JsonParseException je) {
                    System.out.println("Error en el JSON: " + rset.getString(1).substring(1, rset.getString(1).indexOf("versionRecepcionWeb") - 1) + " - " + je.getMessage());
                    err++;
                }

                //System.out.print(i + ", "); SI SE QUIERE VER EL RASTRO DE REGISTROS INSERTADOS

                if (i % 10 == 0) {//CADA 10 REGISTROS MOSTRARA EL AVANCE
                    System.out.println("");
                    System.out.println("RENGLON: " + i);
                }

                if (err == 50) {
                    System.out.println("");
                    System.out.println("SE DETENDRÁ EL PROCESO POR ALCANZAR EL MÁXIMO DE ERRORES");
                    break;
                }
            }

            System.out.println("TOTAL DE REGISTROS: " + i);

            rset.close();
            stmt.close();
        } catch (SQLException ex) {
            System.out.println(" error: " + ex.getLocalizedMessage());
        }

        try {
            connOr.close();
        } catch (SQLException ex1) {
            System.out.println(" error : " + ex1.getMessage());
        }

    }

    /**
     * @param args the command line arguments
     */
    // Adding a single document into the mongo collection.
    private static void addOneDocument(MongoCollection<Document> col, String json) {

        // Sample document.
        Document emp1 = new Document();
        emp1 = Document.parse(json);

        col.insertOne(emp1);
    }

    private String declaraciones() {
        StringBuilder sb = new StringBuilder();

        sb.delete(0, sb.length());
        sb.append("	select '{' || ");
        sb.append("       FN_ETIQUETAJSONSTR('_id',nvl(trim(rw.id_recepcion_web),'NULL'),1) ");
        sb.append("       ||'\"versionRecepcionWeb\": \"20191231\",' ");
        sb.append("       ||'\"institucionReceptora\":{' ");
        sb.append("       ||  '\"id\": \"").append(idEnteReceptor).append("\",' ");
        sb.append("       ||  '\"nombre\": \"").append(nombreDependencia).append("\",' ");
        sb.append("       ||  '\"collName\":").append(collName).append(",' ");
        sb.append("       ||  '\"ente\": {' ");
        sb.append("       ||      '\"id\": \"").append(idDependencia).append("\",' ");
        sb.append("       ||      '\"nombre\":\"").append(nombreDependencia).append("\",' ");
        sb.append("       ||      '\"nivelOrdenGobierno\": {' ");
        sb.append("       ||          '\"nivelOrdenGobierno\": \"").append(ordenGobiernoEnteReceptor).append("\"' ");
        if (entidadFederativaId > 0) {//SI EN LAS VARIABLES DE INICIO SE DEFINIÓ UNA ENTIDAD FEDERATIVA
            sb.append("       ||         ', \"entidadFederativa\": {' ");
            sb.append("       ||                '\"valor\": \"").append(entidadFederativaValor).append("\",' ");
            sb.append("       ||                '\"id\": ").append(entidadFederativaId).append("' ");
            sb.append("       ||           '}' ");
        }
        sb.append("       ||      '}' ");
        sb.append("       ||  '},' ");
        sb.append("       ||  '\"ambitoPublico\": \"").append(ambitoGobiernoEnteReceptor).append("\"' ");
        sb.append("       ||'},' ");
        sb.append("       ||'\"declaracion\":{' ");
        sb.append("       ||    FN_ETIQUETAJSONSTR('numeroDeclaracion',nvl(trim(rw.no_declaracion),'NULL'),1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('tipoDeclaracion',decode(rw.tipo_declaracion,0,'INICIO',1,'CONCLUSION',2,'MODIFICACION',6,'AVISO',9,'NOTA','NO DEFINIDA'),1) ");
        sb.append("           ||FN_ETIQUETAJSONNUM('anio',rw.anio,1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('fechaEncargo',nvl(to_char(rw.F_INICIO,'YYYY-MM-DD'),nvl(to_char(rw.F_CONCLUSION,'YYYY-MM-DD'),'NULL')),1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('fechaRecepcion',NVL(TRIM(to_CHAR(rw.F_ENVIO,'YYYY-MM-DD\"T\"HH24:MI:SS')),'NULL'),1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('extemporanea',decode(rw.EXTEMPORANEA,0,'TIEMPO',1,'EXTEMPORANEA','NO DEFINIDA'),1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('situacion',decode(rw.SITUACION,'C','FIRMADA','P','PENDIENTE','X','DESACTIVADA','E','ERROR', 'NO DEFINIDA'),1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('noComprobante',nvl(trim(rw.NO_COMPROBANTE),'NULL'),1) ");
        sb.append("           ||FN_ETIQUETAJSONSTR('fechaId',nvl(trim(rw.FECHA_ID),'NULL'),0) ");
        sb.append("       ||'},' ");
        sb.append("       ||'\"declarante\":{' ");
        sb.append("       ||FN_ETIQUETAJSONSTR('nombre',nvl(trim(rw.NOMBRE),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('rfc',nvl(trim(rw.RFC),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONNUM('idUsrDecnet',rw.ID_USR_DECNET,1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('idDependencia',NVL(TRIM(de.ID_DEPENDENCIA),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('dependencia',nvl(trim(regexp_REPLACE(upper(utl_raw.cast_to_varchar2((nlssort(rw.DEPENDENCIA, 'nls_sort=binary_ai')))),'[^ABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ,.()-0123456789 ]','')),'NULL'),0) ");
        sb.append("       ||'},' ");
        sb.append("       ||'\"firmaDeclaracion\":{' ");
        sb.append("       ||FN_ETIQUETAJSONSTR('digestionDcn',NVL(TRIM(rw.DIGESTION_DCN),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('noCertificado',NVL(TRIM(rw.NOCERTIFICADO),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONNUM('idTransaccionFirma',rw.ID_TRANSACCION_FIRMA,1) ");
        sb.append("       ||FN_ETIQUETAJSONNUM('folioFirma',rw.FOLIO_FIRMA,1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('nomArchivo',NVL(TRIM(rw.NOMARCHIVO),'NULL'),0) ");
        sb.append("       ||'},' ");
        sb.append("       ||'\"firmaAcuse\":{' ");
        sb.append("       ||FN_ETIQUETAJSONSTR('digestionAcuse',NVL(TRIM(rw.DIGESTION_ACUSE),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('firmaAcuse',nvl(trim(rw.FIRMA_ACUSE),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('idFirmante',nvl(trim(rw.ID_FIRMANTE),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('nombreFirmante',nvl(trim(cf.NOMBRES||' '||cf.PRIMER_APELLIDO||' '||cf.SEGUNDO_APELLIDO),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('tituloFirmante',nvl(trim(cf.TITULO_UNIVERSITARIO),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('puestoFirmante',nvl(trim(cf.DESCRIPCION_PUESTO),'NULL'),0) ");
        sb.append("       ||'},' ");
        sb.append("       ||'\"publicoHistorico\":{' ");
        sb.append("       ||FN_ETIQUETAJSONSTR('hacerPublico',NVL(TRIM(rw.HACER_PUBLICO),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('hacerPublicoCi',NVL(TRIM(rw.HACER_PUBLICO_CI),'NULL'),1) ");
        sb.append("       ||FN_ETIQUETAJSONSTR('datosPublicos',nvl(trim(rw.DATOSPUBLICOS),'NULL'),0) ");
        sb.append("       ||'}' ");
        sb.append("       ||'}' as JSON ");
        sb.append("    from RECEPCION_WEB rw ");
        sb.append("		join DECLARACIONES_EXISTENTES de ON de.NO_DECLARACION = rw.NO_DECLARACION ");
        sb.append("		left join CAT_DEPENDENCIA cd ON cd.ID_DEPENDENCIA = de.ID_DEPENDENCIA ");
        sb.append("		LEFT JOIN CAT_FIRMANTE cf on cf.ID_FIRMANTE = rw.ID_FIRMANTE ");
        sb.append("    where rw.situacion='C' ");
        sb.append("		and rw.id_recepcion_web is not null ");
        sb.append("		and rw.TIPO_DECLARACION IN (0,1,2,6) ");
        //sb.append("		and rw.FECHA_ID > 20200120 ");//SI SE QUIERE LIMITAR A COPIAR LOS ACUSES DE UN DETERMINADO PERIODO
        sb.append("    order by rw.ID_RECEPCION_WEB ");

        return sb.toString();
    }

    private String notas() {
        StringBuilder sb = new StringBuilder();

        sb.delete(0, sb.length());
        sb.append("	select '{' || ");
        sb.append("       FN_ETIQUETAJSONSTR('_id',nvl(trim(rw.id_recepcion_web),'NULL'),1) ");
        sb.append("       ||'\"versionRecepcionWeb\": \"20191231\",' ");
        sb.append("       ||'\"institucionReceptora\":{' ");
        sb.append("       ||  '\"id\": \"").append(idEnteReceptor).append("\",' ");
        sb.append("       ||  '\"nombre\": \"").append(nombreDependencia).append("\",' ");
        sb.append("       ||  '\"collName\":").append(collName).append(",' ");
        sb.append("       ||  '\"ente\": {' ");
        sb.append("       ||      '\"id\": \"").append(idDependencia).append("\",' ");
        sb.append("       ||      '\"nombre\":\"").append(nombreDependencia).append("\",' ");
        sb.append("       ||      '\"nivelOrdenGobierno\": {' ");
        sb.append("       ||          '\"nivelOrdenGobierno\": \"").append(ordenGobiernoEnteReceptor).append("\"' ");
        if (entidadFederativaId > 0) {//SI EN LAS VARIABLES DE INICIO SE DEFINIÓ UNA ENTIDAD FEDERATIVA
            sb.append("       ||         ', \"entidadFederativa\": {' ");
            sb.append("       ||                '\"valor\": \"").append(entidadFederativaValor).append("\",' ");
            sb.append("       ||                '\"id\": ").append(entidadFederativaId).append("' ");
            sb.append("       ||           '}' ");
        }
        sb.append("       ||      '}' ");
        sb.append("       ||  '},' ");
        sb.append("       ||  '\"ambitoPublico\": \"").append(ambitoGobiernoEnteReceptor).append("\"' ");
        sb.append("       ||'},' ");
        sb.append("	    ||'\"declaracion\":{' ");
        sb.append("	    ||    FN_ETIQUETAJSONSTR('numeroDeclaracion',nvl(trim(rw.no_declaracion),'NULL'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('tipoDeclaracion',decode(rw.tipo_declaracion,0,'INICIO',1,'CONCLUSION',2,'MODIFICACION',6,'AVISO',9,'NOTA','NO DEFINIDA'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('fechaRecepcion',NVL(TRIM(to_CHAR(rw.F_ENVIO,'YYYY-MM-DD\"T\"HH24:MI:SS')),'NULL'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('extemporanea',decode(rw.EXTEMPORANEA,0,'TIEMPO',1,'EXTEMPORANEA','NO DEFINIDA'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('situacion',decode(rw.SITUACION,'C','FIRMADA','P','PENDIENTE','X','DESACTIVADA','E','ERROR', 'NO DEFINIDA'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('noComprobante',nvl(trim(rw.NO_COMPROBANTE),'NULL'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('fechaId',nvl(trim(rw.FECHA_ID),'NULL'),1) ");
        sb.append("	        ||  '\"declaracionOrigen\":{'     ");
        sb.append("	        ||      '\"acuse\":{'     ");
        sb.append("	        ||          FN_ETIQUETAJSONSTR('fechaRecepcion',NVL(TRIM(to_CHAR(dor.F_ENVIO,'YYYY-MM-DD\"T\"HH24:MI:SS')),'NULL'),0) ");
        sb.append("	        ||      '},' ");
        sb.append("	        ||      '\"encabezado\":{'     ");
        sb.append("	        ||          FN_ETIQUETAJSONSTR('fechaRegistro',NVL(TRIM(to_CHAR(dor.F_ENVIO,'YYYY-MM-DD\"T\"HH24:MI:SS')),'NULL'),1) ");
        sb.append("	        ||          FN_ETIQUETAJSONSTR('numeroDeclaracion',nvl(trim(dor.no_declaracion),'NULL'),1) ");
        sb.append("	        ||          FN_ETIQUETAJSONSTR('tipoDeclaracion',decode(dor.tipo_declaracion,0,'INICIO',1,'CONCLUSION',2,'MODIFICACION',6,'AVISO',9,'NOTA','NO DEFINIDA'),1) ");
        sb.append("	        ||          '\"usuario\":{' ");
        sb.append("	        ||              FN_ETIQUETAJSONNUM('idUsuario',dor.ID_USR_DECNET,0) ");
        sb.append("	        ||          '},' ");
        sb.append("	        ||          '\"versionDeclaracion\": 20191231' ");
        sb.append("	        ||      '}' ");
        sb.append("	        ||  '}' ");
        sb.append("	    ||'},' ");
        sb.append("	    ||'\"declarante\":{' ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('nombre',nvl(trim(rw.NOMBRE),'NULL'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('rfc',nvl(trim(rw.RFC),'NULL'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONNUM('idUsrDecnet',rw.ID_USR_DECNET,1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('idDependencia',NVL(TRIM(de.ID_DEPENDENCIA),'NULL'),1) ");
        sb.append("	        ||FN_ETIQUETAJSONSTR('dependencia',nvl(trim(regexp_REPLACE(upper(utl_raw.cast_to_varchar2((nlssort(rw.DEPENDENCIA, 'nls_sort=binary_ai')))),'[^ABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ,.()-0123456789 ]','')),'NULL'),0) ");
        sb.append("	    ||'},' ");
        sb.append("	    ||'\"firmaDeclaracion\":{' ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('digestionDcn',NVL(TRIM(rw.DIGESTION_DCN),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('noCertificado',NVL(TRIM(rw.NOCERTIFICADO),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONNUM('idTransaccionFirma',rw.ID_TRANSACCION_FIRMA,1) ");
        sb.append("	    ||FN_ETIQUETAJSONNUM('folioFirma',rw.FOLIO_FIRMA,1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('nomArchivo',NVL(TRIM(rw.NOMARCHIVO),'NULL'),0) ");
        sb.append("	    ||'},' ");
        sb.append("	    ||'\"firmaAcuse\":{' ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('digestionAcuse',NVL(TRIM(rw.DIGESTION_ACUSE),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('firmaAcuse',nvl(trim(rw.FIRMA_ACUSE),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('idFirmante',nvl(trim(rw.ID_FIRMANTE),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('nombreFirmante',nvl(trim(cf.NOMBRES||' '||cf.PRIMER_APELLIDO||' '||cf.SEGUNDO_APELLIDO),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('tituloFirmante',nvl(trim(cf.TITULO_UNIVERSITARIO),'NULL'),1) ");
        sb.append("	    ||FN_ETIQUETAJSONSTR('puestoFirmante',nvl(trim(cf.DESCRIPCION_PUESTO),'NULL'),0) ");
        sb.append("	    ||'}' ");
        sb.append("	    ||'}' as JSON ");
        sb.append("	from RECEPCION_WEB rw ");
        sb.append("	 join DECLARACIONES_EXISTENTES de ON de.NO_DECLARACION = rw.NO_DECLARACION ");
        sb.append("	 JOIN RECTIFICACION rc on rw.NO_DECLARACION = rc.NO_RECTIFICACION ");
        sb.append("	   JOIN ( ");
        sb.append("	            SELECT rco.NO_RECTIFICACION, rwo.NO_DECLARACION, rwo.F_ENVIO, rwo.ANIO, rwo.TIPO_DECLARACION, rwo.RFC, rwo.ID_USR_DECNET ");
        sb.append("	            FROM RECEPCION_WEB rwo ");
        sb.append("	            JOIN RECTIFICACION rco ON rwo.NO_DECLARACION = rco.NO_DECLARACION AND rwo.SITUACION='C' AND rwo.TIPO_DECLARACION IN (0,1,2,6) ");
        sb.append("	        ) dor ON dor.NO_RECTIFICACION = rc.NO_RECTIFICACION ");
        sb.append("	 left join CAT_DEPENDENCIA cd ON cd.ID_DEPENDENCIA = de.ID_DEPENDENCIA ");
        sb.append("	 LEFT JOIN CAT_FIRMANTE cf on cf.ID_FIRMANTE = rw.ID_FIRMANTE ");
        sb.append("	where rw.situacion='C' ");
        sb.append("	and rw.id_recepcion_web is not null ");
        sb.append("	AND rw.TIPO_DECLARACION IN (9) ");
        //sb.append("		and rw.FECHA_ID > 20200115 ");//SI SE QUIERE LIMITAR A COPIAR LOS ACUSES DE UN DETERMINADO PERIODO
        sb.append("	order by rw.ID_RECEPCION_WEB  ");

        return sb.toString();
    }

    public void getOracleConn(String host, String puerto, String dbName, String usuario, String pwd) throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connOr = DriverManager.getConnection("jdbc:oracle:thin:@" + host + ":" + puerto + ":" + dbName, usuario, pwd);
        } catch (ClassNotFoundException c) {
            System.out.println("Class not found!" + c.getMessage());
        } catch (Exception em) {
            System.out.println("Se detecto un error :::" + em.toString());
        }
    }

}
