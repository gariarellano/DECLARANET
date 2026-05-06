import gql from "graphql-tag";

export class AUTH {
  public static REMOVE_PROFILE_TOKEN = gql`
    query removeProfile($_token: String!, $_idp: String!) {
      removeProfile(token: $_token, idp: $_idp) {
        curp
        user
        profiles {
          type
          pages
        }
        profile
        msj
        error
        token
      }
    }
  `;

  public static SET_PROFILE_TOKEN = gql`
    query setProfile($_token: String!, $_idp: String!, $_profile: String!) {
      setProfile(token: $_token, idp: $_idp, profile: $_profile) {
        token
        error
        msj
      }
    }
  `;

  public static VALIDATE_TOKEN = gql`
    query validateToken($_token: String!, $_idp: String!) {
      validateToken(token: $_token, idp: $_idp) {
        curp
        user
        profiles {
          type
          pages {
            page
            actions
          }
        }
        institutions {
          profile
          inst {
            ur
          }
        }
        profile
        msj
        error
        token
      }
    }
  `;
  // query isRegistered($_token: String!, $_transaction: String!, $_instancia: String!, $_app: String!) {
  // isRegistered(app: $_app, instancia: $_instancia, token: $_token, transaction: $_transaction) {

  // query isRegistered($_token: String!, $_transaction: String!) {
  // isRegistered(app: "OPER", token: $_token, transaction: $_transaction) {

  public static GET_TOKEN = gql`
  query isRegistered($_token: String!, $_transaction: String!, $_instancia: String!, $_app: String!) {
    isRegistered(app: $_app, instancia: $_instancia, token: $_token, transaction: $_transaction) {
        token
        error
        msj
      }
    }
  `;

  public static LOGOUT = gql`
    query closeSession($_curp: String!, $_app: String!) {
      closeSession(curp: $_curp, app: $_app) {
        error
        msj
      }
    }
  `;

  public static GET_USERS = gql`
    query getUsers($_app: String!) {
      getUsers(app: $_app) {
        error
        msj
        users {
          curp
          user
          email
          active
        }
      }
    }
  `;

  public static GET_DETAIL_USER = gql`
    query getUser($_curp: String!) {
      getUser(curp: $_curp, app: "OPER") {
        profiles {
          type
          pages
        }
      }
    }
  `;

  public static GET_AUTH_USUARIO = gql`
    query getUser($_curp: String!, $_app: String!) {
      getUser(curp: $_curp, app: $_app) {
        curp
        user
      }
    }
  `;

  public static UPDATE_USER = gql`
    query updateUser($_curp: String!, $_user: updateUser!) {
      updateUser(app: "OPER", curp: $_curp, user: $_user) {
        error
        msj
      }
    }
  `;

  /*
gql`query obtenerEntes($poder: PoderEnum!, $nivelGobierno: NivelGobiernoEnum!) {
obtenerEntes(filtro:{poder:$poder nivelGobierno:$nivelGobierno})
{id nombre : enteDesc nivelOrdenGobierno : nivelGobierno{
nivelOrdenGobierno : nivelGobierno entidadFederativa{
id : idEntidadFederativa valor: entidadFederativaDesc municipio{ id: idMunicipio valor :  municipioDesc
}}} ambitoPublico : poder }}`

  */
  public static getServidores = gql`
    query obtenerServidores($filtro: FiltroInput!) {
      obtenerServidores(filtro: $filtro) {
        paginacion {
          offset
          tamanio
          total
        }
        servidoresPublicos {
          activo
          fechaRegistro
          cumplimiento
          datosRusp {
            idMovimiento
            seguridadNacional
            idSp
            curp
            nombres
            primerApellido
            segundoApellido
            sexo
            tipoObligacion
            nombreEnte
            ramo
            ur
            idTipoEnte
            tipoEnte
            claveUa
            unidadAdministrativa
            idPuesto
            empleoCargoComision
            nivelEmpleoCargoComision
            idNivelJerarquico
            valorNivelJerarquico
            idPuestoEstrategico
            puestoEstrategico
            fechaIngresoInstitucion
            fechaTomaPosesionPuesto
            fechaObligacionDeclara
            fechaBaja
            anio
            declaracionPatrimonial
            idMotivoDeclaracionPatrimonial
            motivoDeclaracionPatrimonial
            idTipoContratacion
            tipoContratacion
            politicamenteExpuesto
            idMotivoBaja
            motivoBaja
            idAltaAsociadaBaja
            id
            activo
            fechaRegistro
          }
          datosDecla {
            idDNetNoLocalizado
            idEnte
            idNivelJerarquico
            valorNivelJerarquico
            areaAdscripcion
            empleoCargoComision
            nivelEmpleoCargoComision
            idMovimiento
            idRecepcionWeb
            noComprobante
            fechaRecepcion
            anio
            tipoDeclaracion
            curp
            idUsrDecNet
            numeroDeclaracion
            idSp
            institucionReceptora {
              id
              collName
              ente {
                ambitoPublico
                id
                nivelOrdenGobierno
                nombre
              }
            }
          }
        }
      }
    }
  `;

  public static getInstituciones = gql`
    query obtenerInformacionInst($condiciones: CondicionesInstInput!) {
      obtenerInformacionInst(
        filtro: { collName: $collName, condiciones: $condiciones }
      ) {
        total
        resultado {
          nombreEnte
          idEnte
          situacion
          obligado
          pendiente
          cumplio
          extemporaneo
          porcCumplimiento
          ramo
          ur
        }
      }
    }
  `;

  public static getInfoUA = gql`
  query obtenerInformacionUA($filtro : FiltroUAInput!) {
    obtenerInformacionUA(filtro : $filtro) {
     total
      resultado {
        idEnte
        claveUa
        unidadAdministrativa
        obligado
        pendiente
        cumplio
        extemporaneo
        porcCumplimiento
        ur
      }
    }
  }
  `



}
