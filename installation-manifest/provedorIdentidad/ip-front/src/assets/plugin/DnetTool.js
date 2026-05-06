// Existe div y no existe botón, se crea el botón
if (document.getElementById('DnetToolButton') == null && document.getElementById('DnetTool') != null) {
    var button = document.createElement("button");
    button.innerHTML = "Ingresar con Declaranet";
    button.className = "DnetToolStyle";
    button.id = "DnetToolButton";
    var body = document.getElementById("DnetTool");
    body.appendChild(button);
}

// Obtener recurso css
const pathScr = $("#DnetScript").attr("src");
if (pathScr) {
    let pathcss = pathScr.split('/').slice(0, -1).join('/');
    pathcss += (pathcss != '' ? '/' : '') + 'DnetTool.css'; //Si está local no requiere '/'

    var element = document.createElement("link");
    element.setAttribute("rel", "stylesheet");
    element.setAttribute("type", "text/css");
    element.setAttribute("href", pathcss);
    document.getElementsByTagName("head")[0].appendChild(element);
}

// Botón creado con valores en div
if (document.getElementById('DnetToolButton') != null && document.getElementById('DnetTool') != null) {
    document.getElementById("DnetToolButton").addEventListener("click", function() {
        document.getElementById('DnetToolButton').disabled = true;
        if ($('div[id="DnetTool"]').attr('data-info')) {
            dataInfoName = 'data-info';
        } else if ($('div[id="DnetTool"]').attr('datainfo')) {
            dataInfoName = 'datainfo';
        }
        const thisdata = $('div[id="DnetTool"]').attr(dataInfoName).split(',');
        const thisUsrData = $('div[id="DnetTool"]').attr('data-usr') || $('div[id="DnetTool"]').attr('datausr');
        let thisUsr = '';
        if (thisUsrData) {
            thisUsr = $('#' + thisUsrData).val();
            if (!thisUsr) {
                thisUsr = '';
            }
        }
        console.log(thisUsr);
        const c = thisdata[0];
        const s = thisdata[1];
        if (thisdata.length === 2) {
            dnetLogin(c, s, 0, thisUsr);

        } else if (thisdata.length === 3) {
            const t = thisdata[2];
            dnetLogin(c, s, t, thisUsr);
        }
        return false;

    });
}


function dnetLogin(c, s, t, u) {
    let apiAuth;
    const pathScr = $("#DnetScript").attr("src");
    const pathArray = pathScr.split('/');
    const path2 = pathArray[2];
    const path1 = pathArray[0] === 'https:' ? 'https://' : 'http://';
    if (path2.startsWith('dgti-ejz-ip-front.200.34.175')) {
        apiAuth = `${path1}servicios.apps.funcionpublica.gob.mx/identidad/login/public/authorize`;
    } else if (path2.startsWith('identidad.apps.funcionpublica.gob.mx')) {
        apiAuth = `${path1}servicios.apps.funcionpublica.gob.mx/identidad/login/public/authorize`;
    } else if (path2.startsWith('dgti-ejz-ip-front-staging.200.34.175')) {
        apiAuth = `${path1}api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/public/authorize`;
    } else if (path2.startsWith('identidad-staging.k8s.sfp.gob.mx')) {
        apiAuth = `${path1}api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/public/authorize`;
    } else if (path2.startsWith('70-review-testing-7qeaae.200.34.175.120.nip.io')) {
        apiAuth = `${path1}52-review-testing-wf02bm.k8s.sfp.gob.mx/api/public/authorize`;
    } else {
        apiAuth = `https://52-review-testing-wf02bm.k8s.sfp.gob.mx/api/public/authorize`;
        // apiAuth = `${path1}52-review-testing-wf02bm.200.34.175.120.nip.io/api/public/authorize`;
        // apiAuth = "http://172.29.100.49:8088/api/public/authorize";
        // apiAuth = "/api/auth/authorize";
    }

    const url = apiAuth + "?cliente_id=" + c + "&secret_key=" + s + "&grant_type=authorization_code";
    const request = new XMLHttpRequest();
    let urlNva;
    request.open('GET', url, true);
    // request.setRequestHeader("dominio", `${window.location.protocol}//${window.location.host}`);
    request.setRequestHeader("dominio", path1 + path2);
    request.onload = function() {
        var wnd_settings = {
            width: Math.floor(window.outerWidth * 0.4),
            height: Math.floor(window.outerHeight * 0.4)
        };
        if (wnd_settings.width < 800) {
            wnd_settings.width = 800;
        }
        if (wnd_settings.height < 600) {
            wnd_settings.height = 600;
        }
        wnd_settings.left = Math.floor(window.screenX + (window.outerWidth - wnd_settings.width) / 2);
        wnd_settings.top = Math.floor(window.screenY + (window.outerHeight - wnd_settings.height) / 8);
        let wnd_options = "width=" + wnd_settings.width + ",height=" + wnd_settings.height;
        wnd_options += ",toolbar=0,scrollbars=1,status=1,resizable=1,location=1,menuBar=0";
        wnd_options += ",left=" + wnd_settings.left + ",top=" + wnd_settings.top;
        if (request.status === 200) {
            let data = JSON.parse(this.response);
            if (data.url) {
                var Authorization = request.getResponseHeader("Authorization");
                // console.log(Authorization);
                urlNva = data.url + "?k=" + Authorization + "&t=" + t + '&c=' + c + '&s=' + s + (u === '' ? '' : '&u=' + u);

            } else {
                urlNva = data + "?k=" + encodeURIComponent(JSON.stringify(''));
            }

            if (t === 0) {
                // Agregamos pathname para devolver con ente
                const path = window.location.pathname ? `&p=${window.location.pathname}` : '';
                window.location.assign(urlNva + path);
            } else {
                var wnd = window.open('', "Loggeate", wnd_options);
                wnd.location.href = urlNva;
            }
        }
    };
    request.onerror = function() {
        renderModal('Estamos experimentando problemas de comunicación, te pedimos volverlo a intentar mas tarde.<small> (' + request.status + ')</small>');
        document.getElementById('DnetToolButton').disabled = false;
    };



    request.send();
}

function renderModal(element) {

    const modalDnet = document.getElementById("dnetToolModal");

    if (!modalDnet) {
        // create the background modal div
        const modal = document.createElement('div');
        modal.classList.add('modalDnetToolStyle');
        modal.id = "dnetToolModal";
        // create the inner modal div with appended argument
        const content = document.createElement('div');
        content.classList.add('modalDnetToolStyleModal-content');

        const closeBtn = document.createElement('span');
        closeBtn.classList.add('modalDnetToolStyleClose');
        closeBtn.innerHTML = '&times;';
        content.appendChild(closeBtn);


        const head = document.createElement('p');
        head.classList.add('modalDnetToolHeader');
        head.innerHTML = 'Servicio de Identidad Digital';
        head.id = 'header';
        content.appendChild(head);


        const texto = document.createElement('p');
        texto.id = 'textoElement';
        content.appendChild(texto);

        // render the modal with child on DOM
        modal.appendChild(content);
        document.body.appendChild(modal);


        closeBtn.onclick = function() {
            const modalCerrar = document.getElementById("dnetToolModal");
            modalCerrar.style.display = "none";
        };

    }
    const modalAbrir = document.getElementById("dnetToolModal");
    const spanTexto = document.getElementById("textoElement");
    spanTexto.innerHTML = element;
    modalAbrir.style.display = "block";

}