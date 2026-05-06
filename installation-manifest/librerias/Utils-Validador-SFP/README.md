ESTE PROYECTO CONTIEN PRUEBAS UNITARIAS para REALIZAR VALIDACION DE LO QUE SE QUIERA HAGREGAR AL PROYECTO, estas s encuentran en el paquete 'Test Package'

PARA HACER USO DE LIBRERIA:

requerimientos minimos:
    1.- Proyecto maven
    
Pasos:

    1.- importar el repositorio en nuestro .pom con las siguientes lineas:
        ´´´
        <repositories>
            <repository>
                <id>SFP-REPO</id>
                <url>http://sfp-wfds8:8083/repository</url>
            </repository>
        </repositories>
        ´´´
        
    2.-
        ´´´
        <dependencies>
            <dependency> 
             <groupId>mx.gob.sfp.dgti</groupId> 
             <artifactId>Utils-Validador-SFP</artifactId> 
             <version>1.2</version> 
             <type>jar</type> 
         </dependency>
        </dependencies>
        ´´´
        
Ejemplo de una clase para utilizar las validadciones de N propiedades:

    .class OBJECT COMPLEJO
            DTOModuloValidar dtoValidadciones = new DTOModuloValidar("-------------PADRE-------------");
            
            DTOPropiedadesValidar dtoPropDg = new DTOPropiedadesValidar(0, "PRIMER PRO", "AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
            dtoPropDg.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
            //dtoPropDg.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.RFC));
            DTOPropiedadesValidar dtoPropDgEQ = new DTOPropiedadesValidar(0, "valor1", 8);//PROPIEDAD A VLAIDAR DEL PADRE
            dtoPropDgEQ.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.NUMERO_IGUAL, 8)); //VALIDAR CON
            dtoPropDgEQ.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.NUMERO_MAYOR_QUE, 1)); //VALIDAR CON
            dtoPropDgEQ.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.NUMERO_MENOR_QUE, 9)); //VALIDAR CON
            dtoPropDgEQ.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.NUMERO_MENORQUE_MAYORQUE, 6, 10)); //VALIDAR CON
            
            dtoValidadciones.getListPropsTovalidate().add(dtoPropDg);        
            dtoValidadciones.getListPropsTovalidate().add(dtoPropDgEQ);
            
            DTOModuloValidar dtoval = new DTOModuloValidar("------- HIJO - ---------");
            DTOPropiedadesValidar dtoPropDgPrime = new DTOPropiedadesValidar(1,"CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
            dtoPropDgPrime.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
            dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
            DTOPropiedadesValidar dtoPropDgSeg = new DTOPropiedadesValidar(1,"RFC","AICC900114D0S");//PROPIEDAD A VLAIDAR DEL PADRE
            dtoPropDgSeg.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
            dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
            
            dtoPropDgPrime = new DTOPropiedadesValidar(2,"CURP","AICC900114HMCRRH04");//PROPIEDAD A VLAIDAR DEL PADRE
            dtoPropDgPrime.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.CURP));//REGEX A APLICAR A PROPIEDAD
            dtoval.getListPropsTovalidate().add(dtoPropDgPrime);
            dtoPropDgSeg = new DTOPropiedadesValidar(2,"RFC","AICC900114D0S");//PROPIEDAD A VLAIDAR DEL PADRE
            dtoPropDgSeg.getListValToExec().add(new DTOParametrosValicacion(EnumValidacion.RFC_PF_A_TRECE));//REGEX A APLICAR A PROPIEDAD
            dtoval.getListPropsTovalidate().add(dtoPropDgSeg);
            
            dtoValidadciones.getListModuloshijos().add(dtoval);//PRIMER HIJO IGUAL
            
            ErroresDTO erroresDto = new ErroresDTO();
            new ExectValidations().ejecutaValidaciones(erroresDto, dtoValidadciones);
            
            erroresDto //Objeto que al termino, contiene todos los errores encontrados de las validaciones, este contendra la misma estructura del como se haya mandado ne el objeto  :DTOPropiedadesValidar
            
            
            
            para interpretar lo smensajes dentro del objeto se podríann visualizar con el siguiente método, esto esta en sus pruebas unitarias.
            private static void visualizaErrores(ErroresDTO erroresDto){
        
                if (!erroresDto.getListModHijos().isEmpty()){
                    erroresDto.getListModHijos().stream().forEach((index) -> {
                        visualizaErrores(index);
                    });
                }
                System.out.println("--------------VISUALIZACIUON DE ERRORES--------------------");
                System.out.println("Para el modulo:"+erroresDto.getNombreModulo()+" sus propiedades :");
                for(DTOPropiedadesError index: erroresDto.getListErorres()){
                    System.out.println("Posicion: "+ index.getPosicionFila()+" Nombre campo:"+ index.getNombreCampo() +"propiedad valor :"+index.getPropiedadValor()); //validacio ejecutada
                    System.out.println("# errores val = "+index.getListErrorMensajes().size());//NUMERO DE ERRORRES DE LAS VALIDACIONES
                    index.getListErrorMensajes().stream().forEach((indexEr) -> {
                        System.out.println("error encontrado id :"+indexEr.getErrorId()+ " mensaje "+indexEr.getMensaje());
                    });
                }
            }

            

   .class OBJECT SENCILLO

            List<DTOPropiedadesValidar> listPropVali = new ArrayList<>();
            List<DTOParametrosValicacion> listVali = new ArrayList<>();
            listVali.add(new DTOParametrosValicacion(EnumValidacion.CADENA_SIN_CARACTERES_ESPECIALES));
            listVali.add(new DTOParametrosValicacion(EnumValidacion.LONGITUD_MAYOR_QUE, 5));
            listVali.add(new DTOParametrosValicacion(EnumValidacion.LONGITUD_MENOR_QUE, 70));
            listPorpiedadesValidar.add(new DTOPropiedadesValidar(0, "Nombre", "Christopher Giovanni Arias Cruz", listVali));
            
            listVali = new ArrayList<>();
            listVali.add(new DTOParametrosValicacion(EnumValidacion.NUMERO_ES_NUMERO));
            listVali.add(new DTOParametrosValicacion(EnumValidacion.NUMERO_ENTERO));
            listPorpiedadesValidar.add(new DTOPropiedadesValidar(0, "Teleono", "5537303410", listVali));
            
            listVali = new ArrayList<>();
            listVali.add(new DTOParametrosValicacion(EnumValidacion.CURP));        
            listVali.add(new DTOParametrosValicacion(EnumValidacion.LONGITUD_IGUAL, 18));
            listPorpiedadesValidar.add(new DTOPropiedadesValidar(0, "CURP", "AICC900114HMCRRH04", listVali));
            
            listVali = new ArrayList<>();
            listVali.add(new DTOParametrosValicacion(EnumValidacion.CADENA_ALFANUMERICA));
            listVali.add(new DTOParametrosValicacion(EnumValidacion.LONGITUD_IGUAL, 13));
            listVali.add(new DTOParametrosValicacion(EnumValidacion.RFC_PF_A_TRECE));
            listPorpiedadesValidar.add(new DTOPropiedadesValidar(0, "RFC", "AICC900114DS0", listVali));
            
            List<DTOPropiedadesError> listPropErro = new ArrayList<>();
            new ExectValidations().ejecutaValidacioes(listPropVali, listPropErro);
            
            for(DTOPropiedadesError index :listPropErro){
                System.out.println("Fila :"+index.getPosicionFila()+" Nombre Campo :"+index.getNombreCampo()+" Valor :"+index.getPropiedadValor());
                for(DTOErrorMensajes indexPr : index.getListErrorMensajes()){
                    System.out.println("IdError:"+indexPr.getErrorId()+" MENSAJE:"+indexPr.getMensaje());    
                }
            }
            
