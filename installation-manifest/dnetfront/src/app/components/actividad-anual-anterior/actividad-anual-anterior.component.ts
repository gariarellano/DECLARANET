import { Component, OnInit, Output, EventEmitter, ViewChild, HostListener } from '@angular/core';
import { NgbModal, NgbModalConfig, NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Globals, timeTouchEvent } from 'src/app/globals';
declare var $: any;
import * as moment from 'moment';
import { ToastrService } from 'ngx-toastr';

@Component({
    selector: 'app-actividad-anual-anterior',
    templateUrl: './actividad-anual-anterior.component.html',
    styleUrls: ['./actividad-anual-anterior.component.scss']
})
export class ActividadAnualAnteriorComponent implements OnInit {
    @Output()
    aceptar = new EventEmitter<JSON>();


    actividadAnual: any;
    form: FormGroup;
    help = false;
    trabajo = false;

    listaIngresos: Array<any>;
    datosIngresos: any;
    tip_remu = 'Anual Anterior'

    initLoad: any;
    tFormato: string;

    suma5 = 0;
    suma2 = 0;
    sumaC = 0;

    fechaMaxAnioAnt: string;
    fechaMinAnioAnt: string;


    // Arreglos
    arreActividadIndustrial = [];
    arreActividadFinanciera = [];
    arreServiciosProfesionales = [];
    arreOtrosIngresos = [];
    arreEnajenacionBienes = [];
    arreActividadIndustrialR = [];
    arreActividadFinancieraR = [];
    arreServiciosProfesionalesR = [];
    arreOtrosIngresosR = [];
    arreEnajenacionBienesR = [];

    objOtrosIngresos: {
        id: any; idPosicionVista: string; registroHistorico: boolean; remuneracion: {
            //@montoMoneda
            monto: number; moneda: { id: number; valor: string; };
        }; tipoIngreso: string; verificar: boolean;
    };
    objServiciosProfesionales: {
        id: any; idPosicionVista: string; registroHistorico: boolean; remuneracion: {
            //@montoMoneda
            monto: number; moneda: { id: number; valor: string; };
        }; tipoServicio: string; verificar: boolean;
    };
    objActividadIndustrial: any;
    totalII: { totalobjActividadIndustrial: number; totalobjActividadFinanciera: number; totalobjServiciosProfesionales: number; totalobjEnajenacionBienes: number; totalobjOtrosIngresos: number; };

    // Objetos
    @Output() acla = new EventEmitter();
    aclara: string;

    // arreactividadFinanciera:Array<any>=[];

    endTimeTouch
    startTimeTouch

    /* Contar touchEnd events */
    countTouches = 0
    countFingers

    /* Datos de la distacia del touch Event */
    distanceTouch
    startScreenTouch
    endScreenTouch

    /* Escuchar cuando el touch se mueva */
    @HostListener('touchmove', ['$event'])
    moveTouch(event) {
        // Obtener el No. dedos del evento touch
        this.countFingers = event.touches.length
        // Calcular distancia recorrida desde el punto inicial
        this.distanceTouch = event.changedTouches[0].clientX - this.startScreenTouch
    }

    /* Escuchar cuando inicie el evento touch */
    @HostListener('touchstart', ['$event'])
    start(event) {
        // Settear el momento en que inicia el evento touch
        this.startTimeTouch = new Date().getTime()
        // Settear posición en que inicia el evento touch
        this.startScreenTouch = event.changedTouches[0].clientX
    }

    div = document.getElementById('scrollAcAn');
    @HostListener('touchend', ['$event'])
    stop(event) {
        this.div = document.getElementById('scrollAcAn');
        // Setter el momento en que termina el touch event
        this.endTimeTouch = new Date().getTime()
        // Calcular diferencia de tiempo de inicio al fin del evento touch
        let diff = this.endTimeTouch - this.startTimeTouch
        // Valida si el elemento tiene scrollBars
        if (this.div['scrollHeight'] != this.div['clientHeight']) {
            // Valida que el movimiento sea con dos dedos
            if (this.countFingers === 2) {

                this.countTouches++
                // Cuando sea 2 es porque quitó los dos dedos de la pantalla
                if (this.countTouches === 2) {
                    // Valida que exista el momento de inicio del evento
                    if (this.startTimeTouch)
                        // Valida la velocidad del movimiento
                        if (diff < timeTouchEvent) {
                            // Si la distancia es > 0 avanza a la siguiente sección
                            if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
                            // Si la distancia es < 0 regresa a la sección anterior
                            if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
                        }
                    this.countTouches = 0 // Settear a 0 el némero de de touchEnd events
                }
            }
        } else
            // Valida la velocidad del movimiento
            if (diff < timeTouchEvent) {
                // Si la distancia es > 0 avanza a la siguiente sección
                if (this.distanceTouch > 0) document.getElementById('goForwardBtn').click()
                // Si la distancia es < 0 regresa a la sección anterior
                if (this.distanceTouch < 0) document.getElementById('goBackBtn').click()
            }
    }

    @HostListener('mousewheel', ['$event'])
    _wheel(event: MouseEvent) {
        this.div = document.getElementById('scrollAcAn');
        if (this.div) {
            // Obtener elemento que tiene el scroll
            // Si tiene scroll detiene la propagación del componente padre
            if (this.div['scrollHeight'] != this.div['clientHeight']) event.stopPropagation()
            // Valida que este en al final del scroll del elemento para poder avanzar a la siguiente sección
            if (this.div['offsetHeight'] != this.div['scrollHeight'])
                // Valida que este en el top del elemto y la dirección del scroll sea hacia arriba
                if (this.div['scrollTop'] + this.div['offsetHeight'] + 1 > this.div['scrollHeight'] && event['wheelDeltaY'] < 0)
                    document.getElementById('goForwardBtn').click()
                // Valida que este en el bottom del elemto y la dirección del scroll sea hacia abajo
                else if (this.div['scrollTop'] === 0 && event['wheelDeltaY'] > 0)
                    document.getElementById('goBackBtn').click()
        }
    }

    constructor(private modalService: NgbModal, private activeModal: NgbActiveModal, public cata: Globals, private readonly toastService: ToastrService) {

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
        }

        this.arreActividadFinanciera = [
            {
                id: null,
                idPosicionVista: "1",
                registroHistorico: false,
                remuneracion: {
                    //@montoMoneda
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
                    //@montoMoneda
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
                    //@montoMoneda
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
                    //@montoMoneda
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
                    //@montoMoneda
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
                    //@montoMoneda
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
                    //@montoMoneda
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
        ]

        this.objServiciosProfesionales = {
            id: null,
            idPosicionVista: "",
            registroHistorico: false,
            remuneracion: {
                //@montoMoneda
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            tipoServicio: "",
            verificar: true
        }

        this.objOtrosIngresos = {
            id: null,
            idPosicionVista: "",
            registroHistorico: false,
            remuneracion: {
                //@montoMoneda
                monto: 0,
                moneda: {
                    id: 96,
                    valor: "PESO MEXICANO"
                }
            },
            tipoIngreso: "",
            verificar: true
        }

        this.arreEnajenacionBienes = [
            {
                id: null,
                idPosicionVista: "1",
                registroHistorico: false,
                remuneracion: {
                    //@montoMoneda
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
                    //@montoMoneda
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
                    //@montoMoneda
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
        this.tFormato = this.initLoad.encabezado.tipoFormato;
        console.log("formato anual anterior: ", this.tFormato);
        const AnioActual = new Date(this.cata.fechaReferenciaMax).getUTCFullYear();

        this.fechaMaxAnioAnt = new Date(`${AnioActual - 1}/12/31`).toISOString().split('T')[0];
        this.fechaMinAnioAnt = new Date(`${AnioActual - 1}/01/01`).toISOString().split('T')[0];
        // const initLoad = JSON.parse(sessionStorage.getItem('initLoad'));
        // let ini = initLoad.declaracion.encabezado.tipoDeclaracion;

        // console.log("init ingresos", ini);

        let tipoRe: string;

        this.actividadAnual = {
            servidorPublicoAnioAnterior: null,
            actividadAnual: {
                fechaInicio: '',
                fechaConclusion: '',
                tipoRemuneracion: 'ANUAL_ANTERIOR',
                remuneracionNetaCargoPublico: {
                    //@montoMoneda
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
            },
            aclaracionesObservaciones: ""
        }

        this.actividadAnual.actividadAnual.actividadIndustrialComercialEmpresarial = this.arreActividadIndustrial;
        this.actividadAnual.actividadAnual.actividadFinanciera = this.arreActividadFinanciera;
        this.actividadAnual.actividadAnual.serviciosProfesionales = this.arreServiciosProfesionales;
        this.actividadAnual.actividadAnual.otrosIngresos = this.arreOtrosIngresos;
        this.actividadAnual.actividadAnual.enajenacionBienes = this.arreEnajenacionBienes;

        const ing = JSON.parse(sessionStorage.getItem('ingresosAnt'));

        //if(ing.totalIngresosNetos == null)  ing.totalIngresosNetos = this.actividadAnual.actividadAnual.totalIngresosNetos;
        ing.actividadAnual = ing.actividadAnual ? ing.actividadAnual : this.actividadAnual.actividadAnual;
        if (ing.actividadAnual.totalIngresosNetos == null) { ing.actividadAnual.totalIngresosNetos = this.actividadAnual.actividadAnual.totalIngresosNetos; }
        if (ing.actividadAnual.ingresoNetoParejaDependiente == null) { ing.actividadAnual.ingresoNetoParejaDependiente = this.actividadAnual.actividadAnual.ingresoNetoParejaDependiente; }
        // ing.enajenacionBienes = this.ini === 'INICIO' ? this.arreEnajenacionBienes : ing.enajenacionBienes;
        this.actividadAnual = ing;
        this.arreActividadIndustrial = this.actividadAnual.actividadAnual.actividadIndustrialComercialEmpresarial;
        this.arreActividadFinanciera = this.actividadAnual.actividadAnual.actividadFinanciera.length > 0 ? this.actividadAnual.actividadAnual.actividadFinanciera : this.arreActividadFinanciera;
        this.arreServiciosProfesionales = this.actividadAnual.actividadAnual.serviciosProfesionales;
        this.arreOtrosIngresos = this.actividadAnual.actividadAnual.otrosIngresos;
        this.arreEnajenacionBienes = this.actividadAnual.actividadAnual.enajenacionBienes.length > 0 ? this.actividadAnual.actividadAnual.enajenacionBienes : this.arreEnajenacionBienes;
        this.aclara = this.actividadAnual.aclaracionesObservaciones;
        this.suma('lugar');


        //this.form.setValue(JSON.parse(sessionStorage.getItem('Ingresos')));
    }



    guardaLocal() {


        if (this.actividadAnual.servidorPublicoAnioAnterior) {
            const fechaIngreso = moment(this.actividadAnual.actividadAnual.fechaInicio).format("YYYY-MM-DD");
            const fechaEgreso = moment(this.actividadAnual.actividadAnual.fechaConclusion).format("YYYY-MM-DD");
            if (fechaIngreso > fechaEgreso || fechaIngreso == fechaEgreso) {
                this.toastService.error(
                    `Mensaje: la fecha de inicio debe ser menor ala fecha de conclusión`,
                    `Fecha invalida`,
                    {
                        timeOut: 5000,
                        extendedTimeOut: 5000,
                        closeButton: true,
                        tapToDismiss: true,
                        positionClass: 'toast-top-center',
                        progressBar: true
                    }
                );
            } else {

                // if (this.actividadAnual.servidorPublicoAnioAnterior == false) {
                //     this.actividadAnual.actividadAnual = null;
                // } else {

                this.actividadAnual.actividadAnual.otrosIngresosTotal.monto = this.suma5;
                this.actividadAnual.actividadAnual.ingresoNetoDeclarante.remuneracion.monto = this.suma2;
                this.actividadAnual.actividadAnual.totalIngresosNetos.remuneracion.monto = this.sumaC;



                this.actividadAnual.actividadAnual.actividadIndustrialComercialEmpresarial = this.arreActividadIndustrial;
                this.actividadAnual.actividadAnual.actividadFinanciera = this.arreActividadFinanciera;
                this.actividadAnual.actividadAnual.serviciosProfesionales = this.arreServiciosProfesionales;
                this.actividadAnual.actividadAnual.otrosIngresos = this.arreOtrosIngresos;
                this.actividadAnual.actividadAnual.enajenacionBienes = this.arreEnajenacionBienes;
                // }

                if (this.tFormato === 'SIMPLIFICADO' && this.actividadAnual.actividadAnual) {
                    this.actividadAnual.actividadAnual.totalIngresosNetos = null;
                    this.actividadAnual.actividadAnual.ingresoNetoParejaDependiente = null;
                }
                this.aceptar.emit(this.actividadAnual);


                setTimeout(() => {
                    $("#nextSection").click();
                }, 1000);
            }
        } else {
            if (this.tFormato === 'SIMPLIFICADO' && this.actividadAnual.actividadAnual) {
                this.actividadAnual.actividadAnual.totalIngresosNetos = null;
                this.actividadAnual.actividadAnual.ingresoNetoParejaDependiente = null;
            }
            this.actividadAnual.actividadAnual = null;
            this.aceptar.emit(this.actividadAnual);


            setTimeout(() => {
                $("#nextSection").click();
            }, 1000);
        }





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
                        //@montoMoneda
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
                        //@montoMoneda
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
            case 'ingActIndAnt':
                this.arreActividadIndustrialR = JSON.parse(JSON.stringify(this.arreActividadIndustrial));
                break;

            case 'ingActFinAnt':
                this.arreActividadFinancieraR = JSON.parse(JSON.stringify(this.arreActividadFinanciera));
                break;

            case 'ingServProfAnt':
                this.arreServiciosProfesionalesR = JSON.parse(JSON.stringify(this.arreServiciosProfesionales));
                break;
            case 'ingOtrosAnt':
                this.arreOtrosIngresosR = JSON.parse(JSON.stringify(this.arreOtrosIngresos));
                break;
            case 'ingEnajenacionAnt':
                this.arreEnajenacionBienesR = JSON.parse(JSON.stringify(this.arreEnajenacionBienes));
                break;
        }
        $(`#modal-lugar-${ingreso}`).css({ 'display': 'flex' });
        $('.menu-nav').css({ 'display': 'none' });
        $('#content-btnSave').css({ 'display': 'none' });
    }

    cerrarSolo(ingreso: string) {
        switch (ingreso) {
            case 'ingActIndAnt':
                this.arreActividadIndustrial = JSON.parse(JSON.stringify(this.arreActividadIndustrialR));
                break;

            case 'ingActFinAnt':
                this.arreActividadFinanciera = JSON.parse(JSON.stringify(this.arreActividadFinancieraR));
                break;

            case 'ingServProfAnt':
                this.arreServiciosProfesionales = JSON.parse(JSON.stringify(this.arreServiciosProfesionalesR));
                break;
            case 'ingOtrosAnt':
                this.arreOtrosIngresos = JSON.parse(JSON.stringify(this.arreOtrosIngresosR));
                break;
            case 'ingEnajenacionAnt':
                this.arreEnajenacionBienes = JSON.parse(JSON.stringify(this.arreEnajenacionBienesR));
                break;
        }


        $(`#modal-lugar-${ingreso}`).fadeOut('slow', () => {
            $(this).css('display', 'none');
        });

        $('.menu-nav').css({ 'display': 'flex' });
        $('#content-btnSave').css({ 'display': 'flex' });

    }

    cerrarModalIngreso(ingreso: string) {

        $(`#modal-lugar-${ingreso}`).fadeOut('slow', () => {
            $(this).css('display', 'none');
        });

        $('.menu-nav').css({ 'display': 'flex' });
        $('#content-btnSave').css({ 'display': 'flex' });
        this.suma(ingreso);


    }
    // sumaModalIngreso(ingreso: string) {
    //     console.log("llego22",this.objActividadFinanciera);


    //     $(`#modal-lugar-${ingreso}`).fadeOut('slow', () => {
    //         $(this).css('display', 'none');
    //     });

    //     $('.menu-nav').css({ 'display': 'flex' });
    //     $('#content-btnSave').css({ 'display': 'flex' });
    //     this.suma(ingreso);
    // }

    cambiarVal() {
        this.suma('');
    }

    cambiarVal2() {
        this.suma('');

    }

    suma(lugar) {

        let t = 0;
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

        this.suma5 = t1 + t2 + t3 + t4 + t5;
        // this.Ingresos.remuneracionNetaCargoPublico.monto += this.suma5 = this.suma2;
        this.suma2 = this.suma5 + Number(this.actividadAnual.actividadAnual.remuneracionNetaCargoPublico.monto);
        this.sumaC = this.suma2 + Number(this.actividadAnual.actividadAnual.ingresoNetoParejaDependiente.remuneracion.monto);

    }

    trabajoGob() {

        console.log("bandera de trabajo", this.actividadAnual.servidorPublicoAnioAnterior);

    }

    abrirObservaciones() {
        $('body').addClass('disabled-onepage-scroll')
        $('#obs-ingresosAnt').css({ display: 'flex' });
        $('.menu-nav').css({ display: 'none' });
        $('#content-btnSave').css({ display: 'none' });
    }
    cerrarObservaciones() {
        $('#obs-ingresosAnt').css('display', 'none');
        $('.menu-nav').css({ display: 'flex' });
        $('#content-btnSave').css({ display: 'flex' });
        this.aclara = this.actividadAnual.aclaracionesObservaciones;

    }
    guardarObservaciones() {
        $('#obs-ingresosAnt').css('display', 'none');
        $('.menu-nav').css({ display: 'flex' });
        $('#content-btnSave').css({ display: 'flex' });
        this.actividadAnual.aclaracionesObservaciones = this.aclara;
    }
}
