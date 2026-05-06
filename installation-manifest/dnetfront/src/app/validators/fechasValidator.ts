import { Directive, Input } from '@angular/core';
import { NG_VALIDATORS, AbstractControl, ValidatorFn, Validator, FormControl } from '@angular/forms';
import * as moment from 'moment';

@Directive({
    selector: '[fechaValid][ngModel]',
    providers: [
        { provide: NG_VALIDATORS, useExisting: validadorFecha, multi: true }
    ]
})

export class validadorFecha implements Validator {
    fecha: any = {};
    validator: ValidatorFn;
    @Input('fechaValid') datosFecha: any;

    constructor() {
        this.validator = this.validateFechaFactory();
    }

    validate(ca: FormControl) {
        this.fecha = JSON.parse(this.datosFecha);
        return this.validator(ca);
    }
    validateFecha(fechaSet, fechaMax: string, fechaMin: string) {

        let valid = true;
        const fechaSelect = moment(fechaSet.value).format("YYYY-MM-DD");
        const today = moment().format("YYYY-MM-DD");
        let validOption;

        validOption = fechaMax != null && fechaMin != null ? 'todos' : fechaMax == null &&
            fechaMin != null ? 'min' : fechaMax != null && fechaMin == null ? 'max' : null;

        switch (validOption) {
            case 'todos':

                let fechaVacia = this.fecha.fechaMax != "" && this.fecha.fechaMin != ""
                    ? 'all' : this.fecha.fechaMax == "" && this.fecha.fechaMin != ""
                        ? 'max' : this.fecha.fechaMin == "" && this.fecha.fechaMax != "" ? 'min' : null;

                if (fechaVacia == 'all') {
                    if (this.fecha.fechaMax != "" && this.fecha.fechaMin != "") {
                        valid = fechaSelect > this.fecha.fechaMax || fechaSelect < this.fecha.fechaMin ? false : valid;
                    }
                }

                if (fechaVacia == 'max') {
                    if (this.fecha.fechaMax == "" && this.fecha.fechaMin != "") {
                        valid = fechaSelect > today || fechaSelect < this.fecha.fechaMin ? false : valid;
                    } else {
                        valid = fechaSelect > this.fecha.fechaMax || fechaSelect < this.fecha.fechaMin ? false : valid;
                    }
                }

                if (fechaVacia == 'min') {
                    if (this.fecha.fechaMin == "" && this.fecha.fechaMax != "") {
                        valid = fechaSelect > fechaMax || fechaSelect < today ? false : valid;
                    } else {
                        valid = fechaSelect > this.fecha.fechaMax || fechaSelect < this.fecha.fechaMin ? false : valid;
                    }
                }

                break;
            case 'min':

                if (this.fecha.fechaMin == "") {
                    valid = fechaSelect < today ? false : valid;
                } else {
                    valid = fechaSelect < this.fecha.fechaMin ? false : valid;
                }
                break;
            case 'max':

                if (this.fecha.fechaMax == "") {
                    valid = fechaSelect > today ? false : valid;
                } else {
                    valid = fechaSelect > this.fecha.fechaMax ? false : valid;
                }
                break;

        }

        if (valid) {
            return true;
        } else {
            return false;
        }
    }

    validateFechaFactory(): ValidatorFn {
        return (ca: AbstractControl) => {
            let valid = this.validateFecha(ca, this.fecha.fechaMax, this.fecha.fechaMin);

            if (valid) {
                return null;
            } else {
                return {
                    fechaValid: {
                        valid: false
                    }
                }
            }

        };
    }
}
