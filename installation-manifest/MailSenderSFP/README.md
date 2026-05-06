Microservicio que permite envíar correos electronicos a cualquier cuenta de correos.
Este esta expuesto en el path del proyect, recibe objetos json de tipo mutation

La interfase GRAPHIQL se expone en:


    /graphiql
    
    
    Staging
        https://dgti-ejz-mailsendersfp-staging.200.34.175.120.nip.io/graphiql/
        
        
    Prodction
        https://dgti-ejz-mailsendersfp.200.34.175.120.nip.io/graphiql/
    
    
    
Para consumir e implementar esta solucion con sencillos pasos seria de la siguiente manera:
El objeto en body podría ser de las siguientes dos maneras:

    Complejo:
        {"query":"mutation sendMail($form: String!, $to :String!, $asunto:String!, $message: String!, $html: String!){\n  sendMail(from:$form\n    \t\t\t\tto:$to\n    \t\t\t\tasunto:$asunto\n    \t\t\t\tmessage:$message\n    \t\t\t\thtml:$html\n    \t\t\t\t\n  )\n}","variables":{"form":"cgarias@funcionpublica.gob.mx","to":"cgarias@funcionpublica.gob.mx","asunto":"Prueba 1","message":"Prueba 1 del sender cocumento <h1>HOLA MUNDOO </h1>","html":"html"},"operationName":"sendMail"}

    Facil:
        {"query":"mutation sendMailObj($input:Mail!){\n  sendMailObj(input:$input)\n}","variables":{"input":{"from":"cgarias@funcionpublica.gob.mx","to":"cgarias@funcionpublica.gob.mx","asunto":"Prueba 1","message":"Prueba 1 del sender cocumento <h1>HOLA MUNDOO </h1>","html":"html"}},"operationName":"sendMailObj"}
        
        
        
