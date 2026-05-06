import { SelectorContext } from '@angular/compiler';

export interface ExpLab {
    verificar:boolean,
    id: number,
    idPosicionVista: number,
    registroHistorico: boolean,
    tipoOperacion: string,
    actividadLaboral: {
        ambitoSector: {
            id: number,
            valor: string
        },
        ambitoSectorOtro?:string,
        SectorPublico: {
            nivelOrdenGobierno:string,
            ambitoPublico: string,
            nombreEntePublico: string,
            areaAdscripcion: string,
            empleoCargoComision: string,
            funcionPrincipal: string
        },
        sectorPrivadoOtro: {
            nombreEmpresaSociedadAsociacion: string,
            rfc: string,
            area: string,
            empleoCargo: string,
            sectorOtro?:string,
            sector: {
                id: number,
                valor: string
            }
        },
        fechaIngreso: string,
        fechaEgreso: string,
        ubicacion: string

    }
}
