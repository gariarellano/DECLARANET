/**
 * @(#)GraphQLDataFetchers.java 30/05/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import mx.gob.sfp.dgti.decnet.as.ValidacionAS;
import mx.gob.sfp.dgti.decnet.dao.CatalogoDAO;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;

/**
 * GraphQLDataFetchers con metodos que resuelveran los queries y mutations
 *
 * @since 30/05/2019
 */
public class GraphQLDataFetchers {

    /**
     * Logger
     */
	static final Logger LOGGER = LoggerFactory.getLogger(GraphQLDataFetchers.class);

    /**
     * Id
     */
	private static final String ID = "id";

    /**
     * Registro
     */
	private static final String REGISTRO = "registro";

    /**
     * DAO del catalogo.
     */
	private static CatalogoDAO databaseService;

	private static ValidacionAS validacionAS;

    /**
     * Inicializa el DataFetcher
     *
     * @param vertx
     * @return
     */
    static GraphQLDataFetchers init(Vertx vertx) {
        LOGGER.info("=== Inicia GraphQLDataFetchers");
    	databaseService  = CatalogoDAO.create(vertx);
        validacionAS = ValidacionAS.create(vertx);
    	return new GraphQLDataFetchers();
    }
    
    /**
     * Obtener el catálogo completo
     *
     * @return catalogo
     */
   public AsyncDataFetcher<List<CatalogoDTO>> obtenerCatalogo() {
	   return catalogo;
   }
   
   /**
    * Obtener el catálogo con registros activos
    *
    * @return catalogoActivo
    */
   public AsyncDataFetcher<List<CatalogoDTO>> obtenerCatalogoActivo() {
	   return catalogoActivo;
   }

    /**
     * Obtener el catálogo con registros activos filtrado por fk
     *
     * @return catalogoActivo
     */
    public AsyncDataFetcher<List<CatalogoDTO>> obtenerCatalogoActivoFk() {
        return catalogoActivoFk;
    }

   
   /**
    * Obtener catálogo por su identificador único
    *
    * @return catalogoPorId
    */
   public AsyncDataFetcher<CatalogoDTO> obtenerCatalogoPorId() {
	   return catalogoPorId;
   }

    /**
     * Obtener catálogo por su identificador único y el fk
     *
     * @return catalogoPorIdFk
     */
    public AsyncDataFetcher<CatalogoDTO> obtenerCatalogoPorIdFk() {
        return catalogoPorIdFk;
    }

    /**
     * Validar la informacion de un catalogo
     *
     * @return catalogoPorId
     *
     * @since 29/10/2019
     * @author pavel.martinez
     */
    public AsyncDataFetcher<Boolean> validarInfoCatalogo() {
        return validacionCatalogo;
    }


   /**
    * Agregar un registro nuevo al catálogo
    *
    * @return agregarRegistro
    */
   public AsyncDataFetcher<CatalogoDTO> agregarRegistro() {
		return agregarRegistro;
   }

   /**
    * Actualizar un registro del catálogo
    *
    * @return actualizarRegistro
    */
   public AsyncDataFetcher<CatalogoDTO> actualizarRegistro() {
		return actualizarRegistro;
   }

    /**
     * Resuelve el query de la consulta del catalogo completo.
     */
   private final AsyncDataFetcher<List<CatalogoDTO>> catalogo = (env, handler) -> {
	   databaseService.obtenerCatalogo().setHandler(busqueda -> {
           if(busqueda.succeeded()) {
               handler.handle(Future.succeededFuture(busqueda.result()));
           } else {
               handler.handle(Future.failedFuture(busqueda.cause()));
           }
       });
   };

    /**
     * Resuelve el query de la consulta del catalogo de solo activos.
     */
   private final AsyncDataFetcher<List<CatalogoDTO>> catalogoActivo = (env, handler) -> {

	   databaseService.obtenerCatalogoActivos().setHandler(busqueda -> {
           if(busqueda.succeeded()) {
               handler.handle(Future.succeededFuture(busqueda.result()));
           } else {
               handler.handle(Future.failedFuture(busqueda.cause()));
           }
       });
   };

    /**
     * Resuelve el query de la consulta del catalogo de solo activos.
     */
    private final AsyncDataFetcher<List<CatalogoDTO>> catalogoActivoFk = (env, handler) -> {

        Integer fk = (Integer) env.getArgument("fk");
        databaseService.obtenerCatalogoActivosFk(fk).setHandler(busqueda -> {
            if(busqueda.succeeded()) {
                handler.handle(Future.succeededFuture(busqueda.result()));
            } else {
                handler.handle(Future.failedFuture(busqueda.cause()));
            }
        });
    };

    /**
     * Resuelve el query de validacion de catalogos.
     */
    private final AsyncDataFetcher<Boolean> validacionCatalogo = (env, handler) -> {

        Map<String, Object> argumentos = env.getArguments();
        JsonObject json = JsonObject.mapFrom(argumentos.get(REGISTRO));
        CatalogoDTO catalogo = new CatalogoDTO(json);

         validacionAS.validarInfoCatalogo(catalogo).setHandler(busqueda -> {
            if(busqueda.succeeded()) {
                handler.handle(Future.succeededFuture(busqueda.result()));
            } else {
                handler.handle(Future.failedFuture(busqueda.cause()));
            }
        });
    };

   /**
     * Resuelve el query de busqueda en catalogo por id.
     */
   private final AsyncDataFetcher<CatalogoDTO> catalogoPorId = (env, handler) -> {
	   String[] campos = null;
       List<String> camposLista;
       if(env.getSelectionSet().getArguments().size() > 0){
           camposLista = new ArrayList<>();
           env.getSelectionSet().getArguments().entrySet().stream().forEach((entrySet) -> 
               camposLista.add(entrySet.getKey())
           );
           campos = camposLista.toArray(new String[camposLista.size()]);
       }
       Map<String, Object> argumentos = env.getArguments();
       int id = Integer.parseInt(argumentos.get(ID).toString());
       databaseService.obtenerCatalogoPorId(id).setHandler(busqueda -> {
           if(busqueda.succeeded()) {
               handler.handle(Future.succeededFuture(busqueda.result()));
           } else {
               handler.handle(Future.failedFuture(busqueda.cause()));
           }
       });
   };

    /**
     * Resuelve el query de busqueda en catalogo por id.
     */
    private final AsyncDataFetcher<CatalogoDTO> catalogoPorIdFk = (env, handler) -> {
        String[] campos = null;
        List<String> camposLista;
        if(env.getSelectionSet().getArguments().size() > 0){
            camposLista = new ArrayList<>();
            env.getSelectionSet().getArguments().entrySet().stream().forEach((entrySet) ->
                    camposLista.add(entrySet.getKey())
            );
            campos = camposLista.toArray(new String[camposLista.size()]);
        }
        Map<String, Object> argumentos = env.getArguments();
        int id = Integer.parseInt(argumentos.get(ID).toString());
        int fk = Integer.parseInt(argumentos.get("fk").toString());
        databaseService.obtenerCatalogoPorIdFk(id, fk).setHandler(busqueda -> {
            if(busqueda.succeeded()) {
                handler.handle(Future.succeededFuture(busqueda.result()));
            } else {
                handler.handle(Future.failedFuture(busqueda.cause()));
            }
        });
    };


    /**
     * Resuelve la mutation para agregar elementos al catalogo.
     */
   private final AsyncDataFetcher<CatalogoDTO> agregarRegistro = (env, handler) -> {
	   Map<String, Object> argumentos = env.getArguments();
       JsonObject json = JsonObject.mapFrom(argumentos.get(REGISTRO));
       CatalogoDTO catalogo = new CatalogoDTO(json);

       databaseService.agregarRegistro(catalogo).setHandler(resultado -> {
           if(resultado.succeeded()) {
               handler.handle(Future.succeededFuture(resultado.result()));
           } else {
               handler.handle(Future.failedFuture(resultado.cause()));
           }
       });
   };

    /**
     * Resuelve la mutation para actualizar elementos del catalogo.
     */
   private final AsyncDataFetcher<CatalogoDTO> actualizarRegistro = (env, handler) -> {
	   Map<String, Object> argumentos = env.getArguments();
       JsonObject json = JsonObject.mapFrom(argumentos.get(REGISTRO));
       CatalogoDTO catalogo = new CatalogoDTO(json);

       databaseService.actualizarRegistro(catalogo).setHandler(resultado -> {
           if(resultado.succeeded()) {
               handler.handle(Future.succeededFuture(resultado.result()));
           } else {
               handler.handle(Future.failedFuture(resultado.cause()));
           }
       });
   };


}
