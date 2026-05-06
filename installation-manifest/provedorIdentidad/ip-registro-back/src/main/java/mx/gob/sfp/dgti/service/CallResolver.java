/*
 * Sistemna Propiedad de la Secretaría de la Función Pública DGTI
 * "OMEXT" Sistema que permite la identificación de los Omisos y Extemporaneos en 
 * presentar la declaración patrimonial y de conflicto de intereses.
 * 
 * Proyecto desarrollado por cgarias@funcionpublica.gob.mx
 */
package mx.gob.sfp.dgti.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.mountcloud.graphql.GraphqlClient;
import org.mountcloud.graphql.request.mutation.DefaultGraphqlMutation;
import org.mountcloud.graphql.request.mutation.GraphqlMutation;
import org.mountcloud.graphql.request.query.DefaultGraphqlQuery;
import org.mountcloud.graphql.request.query.GraphqlQuery;
import org.mountcloud.graphql.response.GraphqlResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase con funciones para consumir un servicio en grahpql
 * @author cgarias
 * @since 10/06/2019
 */
public class CallResolver {
	
    private final GraphqlClient client;
    private GraphqlQuery query;
    private GraphqlMutation mutation;
    private final Map<String,String> headers;
    private String firstObjectInResult;
    private Class className;
    private Object resultObject;
    private Map mapaResult;//esta propiedad es porque solo se permite ejecutar un solo llamado a los servicios por Instancia de esta clase.
    
    //primer step
    public CallResolver(String pathResolver) {
        headers = new HashMap<>();
        client = GraphqlClient.buildGraphqlClient(pathResolver);
    }
    //primer step
    public CallResolver(String pathResolver, Class className) {
        this.headers = new HashMap<>();
        this.className = className;
        this.client = GraphqlClient.buildGraphqlClient(pathResolver);
    }
    
    //segundo step
    public Map getMapToSetHeaders() {
        return headers;
    }
    
    //tercer step
    public void setHeadersToclient() {
        //set http headers
        client.setHttpHeaders(headers);
    }
    
    //cuarto step
    public void setQueryName(String queryName) {
        this.firstObjectInResult = queryName;
        //create query
        query = new DefaultGraphqlQuery(queryName);
    }
    
    //cuarto step
    public void setMutationName(String mutationName) {
        this.firstObjectInResult = mutationName;
        //create mutaion
        mutation = new DefaultGraphqlMutation(mutationName);
    }
    
    //quinto step
    public void putParameterToQuery(String key, String value) {
        //add query or mutation param
        query.addParameter(key, value);
    }

    //quinto step
    public void putParameterToMutation(String key, String value) {
        //add query or mutation param
        mutation.addParameter(key, value);
    }
    
    //quinto step
    public void putParameterToQuery(String key, Map value) {
        //add query or mutation param
        query.getRequestParameter().addObjectParameter(key, value);
    }
    
    //quinto step
    public void putParameterToMutation(String key, Map value) {
        //add query or mutation param
        mutation.getRequestParameter().addObjectParameter(key, value);
    }
    
    //cuarto step
    public void setAtributesFromQuery(String[] campos) {        
        query.addResultAttributes(campos);
    }
    
    //cuarto step
    public void setAtributesFromMutation(String[] campos) {
        mutation.addResultAttributes(campos);
        
    }
    
    //quinto step getResult
    public Map getResultQuery() throws IOException {
        return this.executeQuery();
    }
    
    //quinto step getResult
    public Object getResultQueryCasting() throws IOException {
        if (resultObject == null)
            resultObject = new ObjectMapper().convertValue(((Map) this.executeQuery().get("data")).get(firstObjectInResult), className);
        return resultObject;
    }       
    
    //quinto step getResult
    public Map getResultMutation() throws IOException {
        return this.exucuteMutation();
    }
    
    //quinto step getResult
    public Object getResultMutationCasting() throws IOException {
        if (resultObject == null)
            resultObject = new ObjectMapper().convertValue(((Map) this.exucuteMutation().get("data")).get(firstObjectInResult), className);
        return resultObject;
    }   
    
    /**
     * Método que realiza el llamado al servicio Graph para realizar el query
     * NOTA: solamente permite realizar un llamado por instancia  del objeto.
     * @return Map Objeto mapa de respuesta.
     * 
     * @throws IOException en caso de existir una excepcion la notifica.
     */
    private Map executeQuery() throws IOException {
        if (mapaResult == null){
            mapaResult = new HashMap<>();
            mapaResult = client.doQuery(query).getData();
        }
        return mapaResult;
    }
    
    /**
     * Método que realiza el llamado al servicio Graph para realizar la mutación
     * NOTA: solamente permite realizar un llamado por instancia  del objeto.
     * @return Map Objeto mapa de respuesta.
     * 
     * @throws IOException en caso de existir una excepcion la notifica.
     */
    private Map exucuteMutation() throws IOException {
        if (mapaResult == null){
            mapaResult = new HashMap<>();
            GraphqlResponse response = client.doMutation(mutation);
            mapaResult = response.getData();
        }
        return mapaResult;
    }
    
}
