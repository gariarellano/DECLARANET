## Implementación del Servicio en Angular

En el index de su aplicación deberá de agregar las siguientes líneas de código:

**1.-** Se agrega dentro de la sección de encabezado ``<head></head>`` la siguiente librería para el correcto funcionamiento  del servicio.

```html
<script type ="text/javascript" 
 src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js">
</script>

```

**2.-** En cualquier parte del componente, crear un ``<div></div>`` con la propiedad [id="DnetTool"] y la propiedad [data-info="#1,#2,<1>"] donde:

**#1:** Es el **ID del aplicativo** que se proporcionará en el registro del sistema al que se pretende implementar el IP. [(Ver registro)](http://dgti.doc.funcionpublica.gob.mx/ejz/ip-front/registro/)

**#2:** Es el **Secret Key** que se le proporcionará a la hora del registro del sistema. [(Ver registro)](http://dgti.doc.funcionpublica.gob.mx/ejz/ip-front/registro/)

**1:** <span class ="projo">Opcional</span>, si se especifica este parámetro con el valor 1, la ventana de "Login" se abrirá en una nueva pestaña del navegador.

Ejemplo 1.- La ventana de "Login" se abrirá en la misma pestaña:

~~~html
  <div id="DnetTool" data-info="2,1234">
  </div>
~~~

Ejemplo 2.- La ventana de "Login" se abrirá en una nueva pestaña del navegador:

~~~html
  <div id="DnetTool" data-info="2,1234,1">
  </div>
~~~

**3.-** <span class ="projo">Opcional</span>, agregar al ``<div id="DnetTool" data-info="2,1234,1"></div>`` la propiedad [data-usr="curp"] y seguido del ``<div></div>`` insertar un ``<input>`` con la propiedad [id="curp"], esto con la finalidad de que si el usuario conoce la curp, al momento de ingresar con Declaranet, enviará dicha curp a la ventana de login de Proveedor de Identidad y no podrá editarse, solo faltará ingresar la contraseña.

Ejemplo:

```html
<div id="DnetTool" data-info="2,1234,1" data-usr="curp">
  </div>
  <input id="curp">
```

**4.-** En el ts del componente agregar las líneas de código que se indican a continuación.

Implementar dentro del componente la función `` AfterViewInit``

Posteriormente en el constructor agregar `` private elementRef: ElementRef `` de la librería ``@angular/core``. 

Agregar en la función ``AfterViewInit`` el siguiente código:
```js
ngAfterViewInit() {
...
...
const s = document.createElement('script');
s.type = 'text/javascript';
s.src = 'https://dgti-ejz-ip-front-staging.200.34.175.120.nip.io/assets/plugin/DnetTool.js';
s.id = 'DnetScript';
this.elementRef.nativeElement.appendChild(s);
...
...
}
```
Es así como debe quedar en conjunto: 

```js
export class Component implements
OnInit, AfterViewInit {






constructor(
...
...
...,
private elementRef: ElementRef
) {

}


ngAfterViewInit() {

const s = document.createElement('script');
s.type = 'text/javascript';
s.src = 'https://dgti-ejz-ip-front-staging.200.34.175.120.nip.io/assets/plugin/DnetTool.js';
s.id = 'DnetScript';
this.elementRef.nativeElement.appendChild(s);

}

```
Si los pasos anteriores son realizados correctamente, se mostrará un botón en su sistema, el cual realizará la petición al Servicio de Identidad Digital.

Diseño del botón que aparece por default:

![boton](https://gitlab.funcionpublica.gob.mx/dgti/ejz/ip-front/raw/docs/docs/img/boton.PNG)

Puedes crear tu botón personalizado, el cual debe tener como id="DnetToolButton"

Ejemplo:

```html
<div id="DnetTool" data-info="2,1234">
      <button   id="DnetToolButton" class="mybutton">
            Mi Botón
            <img src= "path_to_imagen">
      </button>
</div>
```

<span class="nota">Nota</span>: Es importante que el botón insertado sea HTML puro. 
