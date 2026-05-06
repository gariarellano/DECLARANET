export interface Escolaridad {
    "id": number,
    idPosicionVista: number,
    registroHistorico:boolean,
    verificar:boolean,
    "tipoOperacion": string,
    "nivel": {
        "id": number,
        "valor": string
    },
    "institucionEducativa": string,
    "carreraAreaConocimiento": string,
    "estatus": string,  //CURSANDO,FINALIZADO,TRUNCO
    "documentoObtenido": {
        "tipo": string,//BOLETA, CERTIFICADO, CONSTANCIA, TITULO
        "fechaObtencion": string
    },
    "ubicacion": string
}
