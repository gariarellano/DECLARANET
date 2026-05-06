// Importamos el css
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

// Existe div y no existe botón, se crea el botón
if (document.getElementById('DnetTool') != null) {
    let dataInfoName = null;
    if ($('div[id="DnetTool"]').attr('data-info')) {
        dataInfoName = 'data-info';
    } else if ($('div[id="DnetTool"]').attr('datainfo')) {
        dataInfoName = 'datainfo';
    }
    const thisdata = $('div[id="DnetTool"]').attr(dataInfoName).split(',');
    const bgColor = $('div[id="DnetTool"]').attr('data-bg') || $('div[id="DnetTool"]').attr('databg') || '';
    const bgImage = $('div[id="DnetTool"]').attr('data-bgImage') || $('div[id="DnetTool"]').attr('databgimage') || '';
    const c = thisdata[0];
    const s = thisdata[1];
    const txtC = $('div[id="DnetTool"]').attr('data-txtLogin') || $('div[id="DnetTool"]').attr('datatxtlogin') || '';
    dnetLogin(c, s, 2, '', bgColor, bgImage, txtC);
}


function dnetLogin(c, s, t, u, bg, bgI, txtC) {
    let apiAuth;
    const pathScr = $("#DnetScript").attr("src");
    const pathArray = pathScr.split('/');
    const path2 = pathArray[2];
    const path1 = pathArray[0] === 'https:' ? 'https://' : 'http://';
    if (path2.startsWith('${JS_URL_PROD_INT}')) {
        apiAuth = `${path1}${JS_API_PROD}`;
    } else if (path2.startsWith('${JS_URL_PROD_PUB}')) {
        apiAuth = `${path1}${JS_API_PROD}`;
    } else if (path2.startsWith('${JS_URL_PROD_PUB_PRES}')) {
        apiAuth = `${path1}${JS_API_PROD_PRES}`;
    } else if (path2.startsWith('${JS_URL_STAGING_INT}')) {
        apiAuth = `${path1}${JS_API_STAGING}`;
    } else if (path2.startsWith('${JS_URL_STAGING_PUB}')) {
        apiAuth = `${path1}${JS_API_STAGING}`;
    } else if (path2.startsWith('${JS_URL_REVIEW}')) {
        apiAuth = `${path1}${JS_API_REVIEW}`;
    } else {
        apiAuth = `NODATA`;
    }

    const url = apiAuth + "?cliente_id=" + c + "&secret_key=" + s + "&grant_type=authorization_code";
    const request = new XMLHttpRequest();
    let urlNva;
    request.open('GET', url, true);
    // request.setRequestHeader("dominio", `${window.location.protocol}//${window.location.host}`);
    request.setRequestHeader("dominio", path1 + path2);
    request.onload = function() {
        if (request.status === 200) {
            let data = JSON.parse(this.response);
            if (data.url) {
                var Authorization = request.getResponseHeader("Authorization");
                // console.log(Authorization);
                urlNva = data.url + "?k=" + Authorization + "&t=" + t + '&c=' + c + '&s=' + s + (u === '' ? '' : '&u=' + u) + (bg === '' ? '' : '&bg=' + bg) + (bgI === '' ? '' : '&bgI=' + bgI) + (txtC === '' ? '' : '&txtC=' + txtC);

            } else {
                urlNva = data + "?k=" + encodeURIComponent(JSON.stringify(''));
            }

            if (t === 2) {
                const padreUrl = window.location.href;
                urlNva += '&pU=' + encodeURI(padreUrl);
                var frame = document.getElementById('frameDnetTool');
                frame.src = urlNva;
            }

        }
    };
    request.onerror = function() {
        renderModal('Estamos experimentando problemas de comunicación, te pedimos volverlo a intentar mas tarde.<small> (' + request.status + ')</small>');
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