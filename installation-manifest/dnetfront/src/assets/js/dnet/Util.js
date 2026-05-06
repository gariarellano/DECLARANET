function CustomUpperCase(obj) {
    var start = obj.selectionStart;
    var end = obj.selectionEnd;
    obj.value = obj.value.toUpperCase();
    obj.value = obj.value.replace(/  +/g, ' ');
    obj.setSelectionRange(start, end);
}

function validatePorcentaje(obj) {
    if (obj.value > 100 || obj.value < 1) {
        obj.value = '';
    }
    this.maxLengthCheck(obj);
}

function prepareMenuCuenta() {
    // Abrir cerrar menú cuenta
    var cuenta = false;

    $('.cuenta').click(() => {
        cuenta = !cuenta;
        if (cuenta) {
            $('.menu-cuenta1').css('height', '35px');
            $('.menu-cuenta2').css('height', '70px');
            $('.menu-cuenta3').css('height', '105px');
            setTimeout(() => {
                $('.menu-cuenta li').css('display', 'flex');
            }, 300);
        } else {
            $('.menu-cuenta').css('height', '0px');
            $('.menu-cuenta li').css('display', 'none');
        }
    });
}

function solonumero(key) {
    //getting key code of pressed key
    var keycode = key.which ? key.which : key.keyCode;
    //comparing pressed keycodes

    if (
        keycode > 31 &&
        (keycode < 48 || keycode > 57) &&
        (keycode < 96 || keycode > 105)
    ) {
        // alert(' You can enter only characters 0 to 9 ');
        return false;
    } else return true;
}

// function soloNumeroInput(obj) {
//     var oldvalue = obj.getAttribute('prevvalue');
//     var start = obj.selectionStart;
//     var end = obj.selectionEnd;

//     var val = /^\d*$/.test(obj.value);
//     if (val && obj.value !== '') {
//         obj.value = parseInt(obj.value, 10);
//     } else {
//         obj.value = oldvalue;
//     }
//     obj.setSelectionRange(start, end);
// }

// min solo puede ser 0 o 1, en caso de requerir un minimo mayor debemos settear el valor minimo
function soloNumeroInput(obj, min, max) {
    var oldvalue = obj.getAttribute('prevvalue');
    var start = obj.selectionStart || 0;
    var end = obj.selectionEnd || 0;
    var flagPoint = false;
    // alert(`1: ${obj.value}`);
    // obj.value = obj.value.replace(/\./g, '').replace(/\,/g, '');
    if (obj.value.slice(-1) === '.') {
        flagPoint = true;
    }
    var val = /^\d*$/.test(obj.value);
    if (val && obj.value !== '' && rangoValido(obj.value, min, max)) {
        if (obj.value !== '0') {
            obj.value = obj.value.replace(/^0+/, '');
        }
    } else if (obj.value === '') {
        // obj.value = min || 0;
        start = obj.value.length - 1;
        end = start + 1;
    } else {
        obj.value = oldvalue;
        start--;
        end--;
    }
    if (flagPoint) {
        // alert('No se permite capturar decimales en este campo');

        abrirModalInfo('modalNoDecimalesIngresos', obj);
    }
    obj.setAttribute('prevvalue', obj.value);
    obj.focus();
    obj.setSelectionRange(start, end);
}


function abrirModalInfo(modal, obj) {
    console.log("moda ", modal);

    let tipoDeclaracion = obj.getAttribute("data-td");

    var backValue = obj.value;
    $(`#${modal}`).modal('show');

    $(`#${modal}`).on('shown.bs.modal', function (e) {
        console.log($('.modal-backdrop').css('z-index'));
        $('.modal-backdrop').css('z-index', 2049);
        obj.value = backValue;
        $(`#${modal}`).keyup(function (e) {
            if (e.keyCode === 13 || e.keyCode === 27) {
                $(`#${modal}`).modal('hide');
                obj.focus();
            }
        });
    });

    $(`#${modal}`).on('hidden.bs.modal', function (e) {
        $('.modal-backdrop').css('z-index', 1040);
    });
}

function rangoValido(val, min, max) {
    if (!min && !max) {
        return true;
    }
    if (val < min || val > max) {
        return false;
    }
    return true;
}

function focusSoloNumero(obj) {
    obj.setAttribute('prevvalue', obj.value);
}



function maxLengthCheck(object) {
    if (object.value.length > object.max.length)
        object.value = object.value.slice(0, object.max.length);
}

