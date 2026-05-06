import { Component, OnInit, Output, EventEmitter, HostListener } from '@angular/core';
import { NgbModal,  NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormGroup  } from '@angular/forms';
import { Globals, timeTouchEvent } from 'src/app/globals';
declare var $: any;
@Component({
    selector: 'app-ingresos-netos',
    templateUrl: './ingresos-netos.component.html',
    styleUrls: ['./ingresos-netos.component.scss']
})
export class IngresosNetosComponent implements OnInit {
    @Output()
    aceptar = new EventEmitter<JSON>();

    Ingresos: any;
    form: FormGroup;
    help = false;

    listaIngresos: Array<any>;
    datosIngresos: any;

    suma5 = 0;
    suma2 = 0;
    sumaC = 0;
    initLoad: any;
    ini: string;
    tFormato: string;
    arreActividadIndustrial: Array<any> = [];
    arreActividadFinanciera: Array<any> = [];
    arreServiciosProfesionales: Array<any> = [];
    arreOtrosIngresos: Array<any> = [];
    arreEnajenacionBienes: Array<any> = [];
    arreActividadIndustrialR: Array<any> = [];
    arreActividadFinancieraR: Array<any> = [];
    arreServiciosProfesionalesR: Array<any> = [];
    arreOtrosIngresosR: Array<any> = [];
    arreEnajenacionBienesR: Array<any> = [];
    objOtrosIngresos: {
    id: any; idPosicionVista: string; registroHistorico: boolean; remuneracion: {
        monto: number; moneda: { id: number; valor: string; };
    }; tipoIngreso: string; verificar: boolean;
    };
    objServiciosProfesionales: {
    id: any; idPosicionVista: string; registroHistorico: boolean; remuneracion: {
        monto: number; moneda: { id: number; valor: string; };
    }; tipoServicio: string; verificar: boolean;
    };
    objActividadIndustrial: any;
    totalII: { totalobjActividadIndustrial: number; totalobjActividadFinanciera: number; totalobjServiciosProfesionales: number;
         totalobjEnajenacionBienes: number; totalobjOtrosIngresos: number; };


    textRemunera: string;
    textNumIncisoOtros: string;
    textTitulo: string;
    textRemuneracionI: string;
    textRemuneracionA: string;
    textRemuneracionB: string;
    textRemuneracionC: string;
    @Output() acla = new EventEmitter();
    @Output() calculaEnajenacion = new EventEmitter();
    aclara: string;

    endTimeTouch
  startTimeTouch

  countTouches = 0
  countFingers

  distanceTouch
  startScreenTouch
  endScreenTouch

  @HostListener('touchmove', ['$event'])
  moveTouch(event) {
    this.countFingers = event.touches.length
    this.distanceTouch = event.changedTouches[0].clientX - this.startScreenTouch
  }

  @HostListener('touchstart', ['$event'])
  start(event) {
    this.startTimeTouch = new Date().getTime()
    this.startScreenTouch = event.changedTouches[0].clientX
  }

  div=document.getElementById('scrollInNe');
  @HostListener('touchend', ['$event'])
  stop(event) {
    this.div = document.getElementById('scrollInNe');
    this.endTimeTouch = new Date().getTime()
    const diff = this.endTimeTouch - this.startTimeTouch
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {
      if (this.countFingers === 2) {

        this.countTouches++
        if (this.countTouches === 2) {
          if (this.startTimeTouch){
            if (diff < timeTouchEvent) {
                 if (this.distanceTouch > 0) {document.getElementById('goForwardBtn').click()}
                 if (this.distanceTouch < 0) {document.getElementById('goBackBtn').click()}
                }
            this.countTouches = 0;
            }
         }
      }
    } else
      if (diff < timeTouchEvent) {
        if (this.distanceTouch > 0){document.getElementById('goForwardBtn').click()}
        if (this.distanceTouch < 0){ document.getElementById('goBackBtn').click()}
      }
  }

  @HostListener('mousewheel', ['$event'])
  _wheel(event: MouseEvent) {
    this.div = document.getElementById('scrollInNe');
   
    if (this.div['scrollHeight'] !== this.div['clientHeight']) {event.stopPropagation()}
    if (this.div['offsetHeight'] !== this.div['scrollHeight']){
      if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
        {document.getElementById('goForwardBtn').click()}
      else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
        {document.getElementById('goBackBtn').click()}
    }
  }
    constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals) {

        this.totalII = {
            totalobjActividadIndustrial: 0,
            totalobjActividadFinanciera: 0,
            totalobjServiciosProfesionales: 0,
            totalobjEnajenacionBienes: 0,
            totalobjOtrosIngresos: 0
        };


        this.objActividadIndustrial = {
            id: null,
            idPosicionVista: "",
            registroHistorico: false,
            remuneracion: {
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            nombreRazonSocial: '',
            tipoNegocio: "",
            verificar: true
        };

        this.arreActividadFinanciera = [
            {
                id: null,
                idPosicionVista: "1",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 1,
                    valor: "CAPITAL"
                },
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "2",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 2,
                    valor: "FONDOS DE INVERSIÓN"
                },
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "3",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 3,
                    valor: "ORGANIZACIONES PRIVADAS"
                },
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "4",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 4,
                    valor: "SEGURO DE SEPARACIÓN INDIVIDUALIZADO"
                },
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "5",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 5,
                    valor: "VALORES BURSÁTILES"
                },
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "6",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 6,
                    valor: "BONOS"
                },
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "7",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoInstrumento: {
                    id: 9999,
                    valor: "OTRO (ESPECIFIQUE)"
                },
                tipoInstrumentoOtro: "OTRO (ESPECIFIQUE)",
                verificar: true
            }
        ];

        this.objServiciosProfesionales = {
            id: null,
            idPosicionVista: "",
            registroHistorico: false,
            remuneracion: {
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            tipoServicio: "",
            verificar: true
        };

        this.objOtrosIngresos  = {
            id: null,
            idPosicionVista: "",
            registroHistorico: false,
            remuneracion: {
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            tipoIngreso: "",
            verificar: true
        };

        this.arreEnajenacionBienes = [
            {
                id: null,
                idPosicionVista: "1",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoBien: "MUEBLE",
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "1",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoBien: "INMUEBLE",
                verificar: true
            },
            {
                id: null,
                idPosicionVista: "1",
                registroHistorico: false,
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                },
                tipoBien: "VEHICULO",
                verificar: true
            }];
    }

    @HostListener('paste', ['$event']) blockPaste(e: KeyboardEvent) {
        e.preventDefault();
      }

    ngOnInit() {
        this.initLoad = JSON.parse(sessionStorage.getItem('declaracionLoaded'));
        this.ini = this.initLoad.encabezado.tipoDeclaracion;
        this.tFormato = this.initLoad.encabezado.tipoFormato;

        console.log("formato: ",this.tFormato);

        let tipoRe: string;



        this.Ingresos = {
            tipoRemuneracion: "",
            remuneracionNetaCargoPublico: {
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            otrosIngresosTotal: {
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            actividadIndustrialComercialEmpresarial: [],
            actividadFinanciera: [],
            serviciosProfesionales: [],
            otrosIngresos: [],
            enajenacionBienes: [],
            ingresoNetoDeclarante: {
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                }
            },
            ingresoNetoParejaDependiente: {
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                }
            },
            totalIngresosNetos: {
                remuneracion: {
                    monto: 0,
                    moneda: {
                        id: 96,
                        valor: "PESO MEXICANO"
                    }
                }
            },
            aclaracionesObservaciones: ""
        }



        this.Ingresos.actividadIndustrialComercialEmpresarial = this.arreActividadIndustrial;
        this.Ingresos.actividadFinanciera = this.arreActividadFinanciera;
        this.Ingresos.serviciosProfesionales = this.arreServiciosProfesionales;
        this.Ingresos.otrosIngresos = this.arreOtrosIngresos;
        this.Ingresos.enajenacionBienes = this.arreEnajenacionBienes;


        
        const ing = JSON.parse(sessionStorage.getItem('ingresos'));
        ing.enajenacionBienes = this.ini === 'INICIO' ? this.arreEnajenacionBienes : ing.enajenacionBienes;


        if(ing.totalIngresosNetos == null)  ing.totalIngresosNetos = this.Ingresos.totalIngresosNetos;
        if(ing.ingresoNetoParejaDependiente == null)  ing.ingresoNetoParejaDependiente = this.Ingresos.ingresoNetoParejaDependiente;
        this.Ingresos = ing;


        switch (this.ini) {
            case "INICIO":
              this.Ingresos.tipoRemuneracion = "MENSUAL";
              this.textRemunera = "mensual";
              this.textNumIncisoOtros = "4";
              this.textTitulo = 'Ingresos netos del declarante' + (this.tFormato !== 'SIMPLIFICADO' ? ', pareja y/o dependientes económicos (Situación actual)' : ' (Situación actual)');
              this.textRemuneracionI = "I. Remuneración mensual neta del declarante por su cargo público (Por concepto de sueldos, honorarios, compensaciones, bonos y otras prestaciones) (Cantidades netas despues de impuestos)"
              this.textRemuneracionA = "A. Ingreso mensual neto del declarante (Suma del numeral I y II)";
              this.textRemuneracionB = "B. Ingreso mensual neto de la pareja y/o dependientes económicos (Después de impuestos)";
              this.textRemuneracionC = "C. Total de ingresos mensuales netos percibidos por el declarante, pareja y / o dependientes económicos (Suma de los apartados A y B)";
              break;

            case "MODIFICACION":
              this.Ingresos.tipoRemuneracion = "ANUAL_ANTERIOR";
              this.textRemunera = "anual";
              this.textNumIncisoOtros = "5";
              this.textTitulo = 'Ingresos netos del declarante' + (this.tFormato !== 'SIMPLIFICADO' ? ', pareja y/o dependientes económicos (Entre el 1 de enero y 31 de diciembre del año inmediato anterior)' : ' (Entre el 1 de enero y 31 de diciembre del año inmediato anterior)');
              this.textRemuneracionI = "I. Remuneración anual neta del declarante por su cargo público (Por concepto de sueldos, honorarios, compensaciones, bonos, aguinaldos y otras prestaciones) (Cantidades netas después de impuestos)"
              this.textRemuneracionA = "A. Ingreso anual neto del declarante (Suma del numeral I y II)";
              this.textRemuneracionB = "B. Ingreso anual neto de la pareja y/o dependientes económicos (Después de impuestos)";
              this.textRemuneracionC = "C. Total de ingresos anuales netos percibidos por el declarante, pareja y / o dependientes económicos (Suma de los apartados A y B)";
              break;

            case "CONCLUSION":
                this.Ingresos.tipoRemuneracion = "ANUAL_ACTUAL";
                this.textRemunera = "anual";
                this.textNumIncisoOtros = "5";
                this.textTitulo = 'Ingresos netos del año en curso a la fecha de conclusión del empleo, cargo o comisión del declarante'  + (this.tFormato !== 'SIMPLIFICADO' ? ', pareja y/o dependientes económicos' : '');
                this.textRemuneracionI = "I. Remuneración neta del año en curso a la fecha de conclusión del empleo, cargo o comisión del declarante por su cargo público (Por concepto de sueldos, honorarios, compensaciones, bonos y otras prestaciones) (Cantidades netas después de impuestos)"
                this.textRemuneracionA = "A. Ingresos del declarante del año en curso a la fecha de conclusión del empleo, cargo o comisión (Suma del numeral I y II)";
                this.textRemuneracionB = "B. Ingresos del año en curso a la fecha de conclusión del empleo, cargo o comisión de la pareja y/o dependientes económicos (Después de impuestos)";
                this.textRemuneracionC = "C. Total de ingresos netos del año en curso a la fecha de conclusión del empleo, cargo o comisión percibidos por el declarante, pareja y / o dependientes económicos (Suma de los apartados A y B)";
                break;
          }
        this.arreActividadIndustrial = this.Ingresos.actividadIndustrialComercialEmpresarial;
        this.arreActividadFinanciera  = this.Ingresos.actividadFinanciera.length > 0
        ? this.Ingresos.actividadFinanciera : this.arreActividadFinanciera;
        this.arreServiciosProfesionales = this.Ingresos.serviciosProfesionales;
        this.arreOtrosIngresos = this.Ingresos.otrosIngresos;
        this.arreEnajenacionBienes = this.Ingresos.enajenacionBienes.length > 0 ?
        this.Ingresos.enajenacionBienes : this.arreEnajenacionBienes;
        this.aclara = this.Ingresos.aclaracionesObservaciones;
        this.calculaEnajenacion.emit();
        this.suma('lugar');

      }

    guardaEnajenacion(obj) {

        this.arreEnajenacionBienes.find(x => x.tipoBien === 'MUEBLE').remuneracion.monto = obj.tm;
        this.arreEnajenacionBienes.find(x => x.tipoBien === 'INMUEBLE').remuneracion.monto = obj.ti;
        this.arreEnajenacionBienes.find(x => x.tipoBien === 'VEHICULO').remuneracion.monto = obj.tv;
        this.suma('lugar');
    }

    guardaLocal() {

        if (this.Ingresos.valid && this.Ingresos.ninguno === null)  {
          this.Ingresos.ninguno = false;
        }

        this.Ingresos.actividadIndustrialComercialEmpresarial = this.arreActividadIndustrial;
        this.Ingresos.actividadFinanciera = this.arreActividadFinanciera;
        this.Ingresos.serviciosProfesionales = this.arreServiciosProfesionales;
        this.Ingresos.otrosIngresos = this.arreOtrosIngresos;
        this.Ingresos.enajenacionBienes = this.arreEnajenacionBienes;

        this.limpiaNulos();

        this.Ingresos.otrosIngresosTotal.monto = this.suma5;
        this.Ingresos.ingresoNetoDeclarante.remuneracion.monto = this.suma2;
        this.Ingresos.totalIngresosNetos.remuneracion.monto = this.sumaC;

        if (this.tFormato === 'SIMPLIFICADO')  {
            this.Ingresos.totalIngresosNetos = null;
            this.Ingresos.ingresoNetoParejaDependiente = null;
        }

        this.aceptar.emit(this.Ingresos);
        setTimeout(() => {
          $('#nextSection').click();
        }, 1000);


    }

    limpiaNulos() {
        this.Ingresos.actividadFinanciera.find(x => x.tipoInstrumento.id === 9999).tipoInstrumentoOtro = 'OTRO (ESPECIFIQUE)';
        this.Ingresos.actividadFinanciera.forEach((item) => {
            if (item.remuneracion.monto === null) 
            { item.remuneracion.monto = 0; }
        });
    }

    deleteItem(index, place) {
        switch (place) {
            case 'industrial':
                this.arreActividadIndustrial.splice(index, 1);
                break;
            case 'financiera':
                this.arreActividadFinanciera.splice(index, 1);
                break;
            case 'pro':
                this.arreServiciosProfesionales.splice(index, 1);
                break;
            case 'ingresos':
                this.arreOtrosIngresos.splice(index, 1);
                break;
            case 'enajenacion':
                this.arreEnajenacionBienes.splice(index, 1);
                break;
        }
    }


    aceptarItem(place) {
        switch (place) {
            case 'industrial':
                this.arreActividadIndustrial.push(this.objActividadIndustrial);

                this.objActividadIndustrial = {
                    id: null,
                    idPosicionVista: "",
                    registroHistorico: false,
                    remuneracion: {
                        monto: 0,
                        moneda: {
                          id: 96,
                          valor: "PESO MEXICANO"
                          }
                    },
                    nombreRazonSocial: "",
                    tipoNegocio: "",
                    verificar: true
                }

                break;

            case 'financiera':
                this.arreActividadFinanciera = this.arreActividadFinanciera;

                break;

            case 'pro':
                this.arreServiciosProfesionales.push(this.objServiciosProfesionales);

                this.objServiciosProfesionales = {
                    id: null,
                    idPosicionVista: "",
                    registroHistorico: false,
                    remuneracion: {
                        monto: 0,
                        moneda: {
                          id: 96,
                          valor: "PESO MEXICANO"
                         }
                    },
                    tipoServicio: "",
                    verificar: true
                }

                break;
            case 'ingresos':
                this.arreOtrosIngresos.push(this.objOtrosIngresos);

                this.objOtrosIngresos = {
                    id: null,
                    idPosicionVista: "",
                    registroHistorico: false,
                    remuneracion: {
                        monto: 0,
                        moneda: {
                          id: 96,
                          valor: "PESO MEXICANO"
                          }
                    },
                    tipoIngreso: "",
                    verificar: true
                }
                break;
            case 'enajenacion':
                this.arreEnajenacionBienes = this.arreEnajenacionBienes;


                break;
        }
    }

    abrirModalIngreso(ingreso: string) {
        switch (ingreso) {
            case 'ingActInd':
                this.arreActividadIndustrialR = JSON.parse(JSON.stringify(this.arreActividadIndustrial));
                break;

            case 'ingActFin':
                this.arreActividadFinancieraR = JSON.parse(JSON.stringify(this.arreActividadFinanciera));
                break;

            case 'ingServProf':
                this.arreServiciosProfesionalesR = JSON.parse(JSON.stringify(this.arreServiciosProfesionales));
                break;
            case 'ingOtros':
                this.arreOtrosIngresosR = JSON.parse(JSON.stringify(this.arreOtrosIngresos));
                break;
            case 'ingEnajenacion':
                this.arreEnajenacionBienesR = JSON.parse(JSON.stringify(this.arreEnajenacionBienes));
                break;
        }

        $(`#modal-lugar-${ingreso}`).css({ 'display': 'flex' });
        $('.menu-nav').css({ 'display': 'none' });
        $('#content-btnSave').css({ 'display': 'none' });
        $('body').addClass('disabled-onepage-scroll');

    }

    cerrarSolo(ingreso: string) {
        switch (ingreso) {
            case 'ingActInd':
                this.arreActividadIndustrial = JSON.parse(JSON.stringify(this.arreActividadIndustrialR));
                break;

            case 'ingActFin':
                this.arreActividadFinanciera = JSON.parse(JSON.stringify(this.arreActividadFinancieraR));
                break;

            case 'ingServProf':
                this.arreServiciosProfesionales = JSON.parse(JSON.stringify(this.arreServiciosProfesionalesR));
                break;
            case 'ingOtros':
                this.arreOtrosIngresos = JSON.parse(JSON.stringify(this.arreOtrosIngresosR));
                break;
            case 'ingEnajenacion':
                this.arreEnajenacionBienes = JSON.parse(JSON.stringify(this.arreEnajenacionBienesR));
                break;
        }



        $(`#modal-lugar-${ingreso}`).fadeOut('slow', () => {
            $(this).css('display', 'none');
        });

        $('.menu-nav').css({ 'display': 'flex' });
        $('#content-btnSave').css({ 'display': 'flex' });
        $("body").removeClass("disabled-onepage-scroll");

    }

    cerrarModalIngreso(ingreso: string) {


        $(`#modal-lugar-${ingreso}`).fadeOut('slow', () => {
            $(this).css('display', 'none');
        });

        $('.menu-nav').css({ 'display': 'flex' });
        $('#content-btnSave').css({ 'display': 'flex' });
        $("body").removeClass("disabled-onepage-scroll");
        this.suma(ingreso);
    }

    cambiarVal(){
      this.suma('a');
    }

    cambiarVal2(){
        this.suma('a');
    }

     suma(lugar) {
        let t1 = 0, t2 = 0, t3 = 0, t4 = 0, t5 = 0;
        this.arreActividadIndustrial.forEach((item) => {
            t1 += Number(item.remuneracion.monto);
        });
        this.totalII.totalobjActividadIndustrial = t1;
        this.arreActividadFinanciera.forEach((item) => {
            t2 += Number(item.remuneracion.monto);
        });
        this.totalII.totalobjActividadFinanciera = t2;
        this.arreServiciosProfesionales.forEach((item) => {
            t3 += Number(item.remuneracion.monto);
        });
        this.totalII.totalobjServiciosProfesionales = t3;
        this.arreEnajenacionBienes.forEach((item) => {
            t4 += Number(item.remuneracion.monto);
        });
        this.totalII.totalobjEnajenacionBienes = t4;
        this.arreOtrosIngresos.forEach((item) => {
            t5 += Number(item.remuneracion.monto);
        });
        this.totalII.totalobjOtrosIngresos = t5;
    
        this.suma5 = t1+t2+t3+t4+t5;
        this.suma5 = Number(this.suma5);

        this.suma2= this.suma5+ Number(this.Ingresos.remuneracionNetaCargoPublico.monto);
        this.sumaC= this.suma2+ Number(this.Ingresos.ingresoNetoParejaDependiente.remuneracion.monto);
    }

    abrirObservaciones() {
      $('body').addClass('disabled-onepage-scroll')
      $('#obs-ingresos').css({ display: 'flex' });
      $('.menu-nav').css({ display: 'none' });
      $('#content-btnSave').css({ display: 'none' });
    }
    cerrarObservaciones() {
      $('#obs-ingresos').css('display', 'none');
      $('.menu-nav').css({ display: 'flex' });
      $('#content-btnSave').css({ display: 'flex' });
      this.aclara = this.Ingresos.aclaracionesObservaciones;

    }
    guardarObservaciones() {
      $('#obs-ingresos').css('display', 'none');
      $('.menu-nav').css({ display: 'flex' });
      $('#content-btnSave').css({ display: 'flex' });
      this.Ingresos.aclaracionesObservaciones = this.aclara;
    }
}
