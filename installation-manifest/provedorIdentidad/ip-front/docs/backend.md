# Java

En el Back-End para proporcionar el acceso, en java se tiene que crear un Servlet o Servicio de la siguiente forma:
 
Es importante que la terminación del Servlet o Servicio debe de terminar con el path {"/system-log-in-ip”} como se muestra en el ejemplo:

```java
@WebServlet(name = "ServletLoginAlterno", urlPatterns = {"/system-log-in-ip"})
public class ServletLoginAlterno extends HttpServlet{ /* ... */ }
```

La implementación del Servlet es para proporcionar el acceso a la Identificación Digital, en donde se reciben dos parámetros: el token y la transacción, los cuales se recomienda que se almacenen en la base de datos ya que puede ser utilizado para aclarar dudas respecto a su respuesta de validación.
 
Ejemplo:

```java
String token = request.getParameter("access_token");
String transaction = request.getParameter("transaction");
```

Para realizar la autenticación del token y la transacción, existe un JAR llamado AppiExtensionSFP en donde recibe estos parámetros (token, transaction) y nos devuelve el IP para su validez, si los datos proporcionados son correctos nos devolverá una CURP para proporcionarle el acceso con el perfil y roles asignado en el sistema final.  Si los datos son incorrectos mandará un mensaje por medio de una excepción.
Lo que realiza este JAR es mandar a traer la siguiente liga:

```html
(https://api-gateway-staging.apps.funcionpublica.gob.mx/identidad/login/public/validate-response)

```

Esta URL valida el token y la transacción para obtener  la CURP, en caso de usar otra tecnología que no sea java, se puede utilizar esta liga para validar los datos por medio de https.

El ejemplo se muestra más adelante en la imagen 1.1 e imagen 1.2 , en donde se utilizó como cliente Postman.

A través del método GET se le tiene que proporcionar los siguientes datos:

+ HEADERS TRANSACTION   ----Se genera cuando el usuario se ha logueado

+ Cliente_id ----cliente_id del sistema

+ Secret Key ----secret_key del sistema

+ Authorization token-----Token

Ejemplo en Java:

```java
String curp = new AppiExtensionSFP().sfpPetitionToIpToken(token, transaction);
```

Ejemplo de como sería con un Servlet:

``` 
@WebServlet(name = "ServletLoginAlterno", urlPatterns = {"/system-log-in-ip"})
public class ServletLoginAlterno extends HttpServlet{
 
​ /** 
* Processes requests for both HTTP <code>GET</code> and <code>POST</code>
* methods.
*
* @param request servlet request
* @param response servlet response
* @throws ServletException if a servlet-specific error occurs
* @throws IOException if an I/O error occurs
*/
 
protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      response.setContentType("text/html;charset=UTF-8");
response.addHeader("Cache-Control", "no-store, no-cache, must-revalidate"); 
      response.addHeader("pragma", "no-cache");
      response.addDateHeader("Expires", -1);
      request.setCharacterEncoding("UTF-8");
 
//Estos son los parámetros que envia el IP para pedir la curp
 
String token = request.getParameter("access_token");
String transaction = request.getParameter("transaction");
 
try {
FacesContext context = getFacesContext(request, response);
 
context.getExternalContext().getSessionMap().put("mbAdministrador", new MBAdministrador());
      MBAdministrador mbAdministrador = 
      ((MBAdministrador) request.getSession().getAttribute("mbAdministrador"));
 
​//Llamado del JAR AppiExtencionSFP
 
String curp = new AppiExtensionSFP().sfpPetitionToIpToken(token, transaction);
 
DTOUserSession dtoUserSession = new DTOUserSession();
String route = serviceUsuario.buscaUsuario(dtoUserSession, curp,                                                  mbAdministrador);
               if (route.contains("ERROR")) {
                    throw new Exception(route.substring(0,route.length()-5));
               }
              
               response.sendRedirect(route.concat(".jsf"));
               context.renderResponse();
        }
catch(Exception e ) {
             try (PrintWriter out = response.getWriter()) {           
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>ERROR DE VALIDACIÓN</title>");           
                out.println("</head>");
                out.println("<body>");
                out.println("<h2>"+token+"</h2>");
                out.println("<h2>"+transaction+"</h2>");
                out.println("<h1>"+e.getMessage()+"</h1>");
                out.println("</body>");
                out.println("</html>");
            }
        }  
    }
}
```
 


Imagen 1.1 : Ejemplo 

![Ejemplo](https://gitlab.funcionpublica.gob.mx/dgti/ejz/ip-front/raw/docs/docs/img/img1.PNG)

Los datos que se obtienen es como muestra el siguiente ejemplo en donde se utilizó como cliente Postman:

Imagen 1.2 

![Ejemplo](https://gitlab.funcionpublica.gob.mx/dgti/ejz/ip-front/raw/docs/docs/img/datos.PNG)

### Configuración del JAR (MAVEN)
 
Para configurar el JAR que valida la transacción y el token, se tiene que agregar la siguiente configuración en su archivo pom.xml
 
 - Agregar el repositorio donde se encuentra el JAR para validar la transacción y el token:

```xml
   <repositories>
        <repository>
            <id>SFP-REPO</id>
            <url>http://sfp-wfds8:8083/repository</url>
        </repository>
    </repositories>
```
 
 
• Existen dos versiones del JAR: staging y el de producción.
 
Para la configuración de Staging la dependecia sería:

Staging:

```xml
<dependency>
            <groupId>mx.gob.sfp.dgti</groupId>
            <artifactId>SFP-AppiExtension</artifactId>
            <version>1.0-staging</version>
            <type>jar</type>
</dependency
```

 Producción:

```xml
<dependency>
            <groupId>mx.gob.sfp.dgti</groupId>
            <artifactId>SFP-AppiExtension</artifactId>
            <version>1.0-production</version>
            <type>jar</type>
</dependency
```

