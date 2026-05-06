export interface IngresosJson {
    tipoRemuneracion: string, //Mensual, Anual año anterior, Anual año en curso
    remuneracionNetaCargoPublico: { //suma de los montos de seccion I_4
        //@montoMoneda
        monto: number,
        moneda: {
            id: number,
            valor: string
        }
    },
    otrosIngresosTotal: {
        //@montoMoneda
        monto: number,
        moneda: {
            id: number,
            valor: string
        }
    },
    actividadIndustrialComercialEmpresarial: Array<any>,
    actividadFinanciera: Array<any>,
    serviciosProfesionales: Array<any>,
    otrosIngresos: Array<any>,
    enajenacionBienes: Array<any>,
    ingresoNetoDeclarante: {
        remuneracion: {
            monto: number,
            moneda: {
                id: number,
                valor: string
            }
        }
    },
    ingresoNetoParejaDependiente: {
        remuneracion: {
            monto: number,
            moneda: {
                id: number,
                valor: string
            }
        }
    },
    totalIngresosNetos: {
        remuneracion: {
            monto: number,
            moneda: {
                id: number,
                valor: string
            }
        }
    }

}


  export interface DatosIngresos{
    Ingresos: IngresosJson,
    aclaracionesObservaciones: string
  }
