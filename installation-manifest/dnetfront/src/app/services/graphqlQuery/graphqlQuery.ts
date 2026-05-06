import gql from 'graphql-tag';

export const GuardaDeclaracion = gql`
mutation guardarDeclaracion($parametros: Parametros!) {
  guardarDeclaracion(parametros: $parametros){
    estado
    modulos{
      modulo
      errores{
        listErrorMensajes{
          errorId
          mensaje
          mensajeAlterno
        }
        nombreCampo
        propiedadValor
      }

    }
    declaracion
  }
}
`;

export const GuardaNotas = gql`
mutation guardarNotas($parametros: ParametrosNotas!) {
  guardarNotas(parametros: $parametros) {
    estado
    modulos{
      modulo
      errores{
        listErrorMensajes{
          errorId
          mensaje
          mensajeAlterno
        }
        nombreCampo
        propiedadValor
      }

    }
    declaracion
  }
}`;

export const GuardaAviso = gql`
mutation guardarAviso($parametros: ParametrosAviso!) {
  guardarAviso(parametros: $parametros){
    estado
    modulos{
      modulo
      errores{
        listErrorMensajes{
          errorId
          mensaje
          mensajeAlterno
        }
        nombreCampo
        propiedadValor
      }

    }
    declaracion
  }
}
`;


// export const ConsultaEntes = gql`
// query {catalogo : obtenerEntes(filtro:{poder:EJECUTIVO nivelGobierno:FEDERAL})
//                     {id nombre : enteDesc nivelOrdenGobierno : nivelGobierno{
//                      nivelOrdenGobierno : nivelGobierno entidadFederativa{
//                      id : idEntidadFederativa valor: entidadFederativaDesc municipio{ id: idMunicipio valor :  municipioDesc
//                     }}} ambitoPublico : poder }}
// `;


export const ConsultaEntes2 = gql`query obtenerEntes($poder: PoderEnum!, $nivelGobierno: NivelGobiernoEnum!) {
obtenerEntes(filtro:{poder:$poder nivelGobierno:$nivelGobierno})
{id nombre : enteDesc nivelOrdenGobierno : nivelGobierno{
nivelOrdenGobierno : nivelGobierno entidadFederativa{
id : idEntidadFederativa valor: entidadFederativaDesc municipio{ id: idMunicipio valor :  municipioDesc
}}} ambitoPublico : poder }}`;

export const ConsultaEntes3 = gql`query obtenerEntes($poder: String! $nivelGobierno: String! $idEntidadfederativa: String!) {
obtenerEntes(filtro:{poder:$poder nivelGobierno:$nivelGobierno idEntidadfederativa:$idEntidadfederativa})
{id nombre : enteDesc nivelOrdenGobierno : nivelGobierno{
nivelOrdenGobierno : nivelGobierno entidadFederativa{
id : idEntidadFederativa valor: entidadFederativaDesc municipio{ id: idMunicipio valor :  municipioDesc
}}} ambitoPublico : poder }}`;

export const ConsultaEntes4 = gql`query obtenerEntes($poder: String! $nivelGobierno: String! $idEntidadfederativa: String! $idMunicipio: String!) {
obtenerEntes(filtro:{poder:$poder nivelGobierno:$nivelGobierno idEntidadfederativa:$idEntidadfederativa idMunicipio:$idMunicipio})
{id nombre : enteDesc nivelOrdenGobierno : nivelGobierno{
nivelOrdenGobierno : nivelGobierno entidadFederativa{
id : idEntidadFederativa valor: entidadFederativaDesc municipio{ id: idMunicipio valor :  municipioDesc
}}} ambitoPublico : poder }}`;

