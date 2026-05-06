/**
 * @(#)ValidacionASImpl.java 30/10/2019
 *
 * Copyright (C) 2019 Secretaria de la Funcion Publica (SFP).
 *
 * Todos los derechos reservados.
 */
package mx.gob.sfp.dgti.decnet.as;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import mx.gob.sfp.dgti.decnet.dao.CatalogoDAO;
import mx.gob.sfp.dgti.decnet.dto.CatalogoDTO;

/**
 * Implementacion de ValidacionASImpl
 *
 * @author pavel.martinez
 * @since 29/10/2019
 */
public class ValidacionASImpl implements ValidacionAS {

    /**
     * DAO para consultar el catalogo
     */
    CatalogoDAO databaseService;

    /**
     * Constructor del servicio de validacion
     *
     * @param vertx
     */
    public ValidacionASImpl(Vertx vertx) {
        databaseService  = CatalogoDAO.create(vertx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<Boolean> validarInfoCatalogo (CatalogoDTO catalogo){
        Future<Boolean> future = Future.future();
        databaseService.obtenerCatalogoPorId(catalogo.getId()).setHandler(catalogoBase -> {

            if(catalogoBase.succeeded() && catalogoBase.result() != null && compararCatalogos(catalogo,
                    catalogoBase.result())) {
                future.handle(Future.succeededFuture(Boolean.TRUE));
            } else {
                future.handle(Future.succeededFuture(Boolean.FALSE));
            }
        });
        return future;
    }

    /**
     * Comparador de catalogos, se compara siempre el id y el valor, se compara valorUno y fk solo si no vienen vacíos
     *
     * @param catalogo que se quiere comparar
     * @param catalogoBase el que viene de la base
     *
     * @return boolean: true si son iguales, false si no lo son
     *
     * @author pavel.martinez
     * @since 03/12/2019
     */
    private boolean compararCatalogos(CatalogoDTO catalogo, CatalogoDTO catalogoBase) {
        if(catalogo.getId() == null || catalogo.getValor() == null) {
            return false;
        }
        if (!catalogo.getId().equals(catalogoBase.getId()) || !catalogo.getValor().equals(catalogoBase.getValor())) {
            return false;
        }
        if (catalogo.getValorUno() != null && !catalogo.getValorUno().equals(catalogoBase.getValorUno())) {
            return false;
        }
        if (catalogo.getFk() != null && !catalogo.getFk().equals(catalogoBase.getFk())) {
           return false;
        }
        return true;
    }

}
