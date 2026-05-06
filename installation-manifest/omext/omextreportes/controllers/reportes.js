const admZip = require("adm-zip");
const uniqid = require("uniqid");
const dateFormat = require("dateformat");

const HashMap = require('hashmap');
const path =require("path");
const servidorService =require("../services/servidor_service");
const fs  = require("fs");

const dirname = path.resolve();

const reportesDisponibles = new HashMap();

exports.generarReporte = async (req, res) => {

    const nombre = formatearNombreArchivo(req.body.nombre);
    const reportes = req.body.reportes;

    if(reportesDisponibles.get(JSON.stringify(reportes))) {
      res.send({"id":reportesDisponibles.get(JSON.stringify(reportes))});
      console.log("*** Se recicla archivo: " + reportesDisponibles.get(JSON.stringify(reportes)));
      return;
    }
    const id = nombre + "@" + uniqid();
    if (reportes.length >0) {

      try {
        await servidorService.crearCsvRusp(reportes, id, nombre);
        const zip = new admZip();
        zip.addLocalFolder("archivos/" + id)
        zip.writeZip("archivos/" +  id + ".zip", () => {
          console.log("*** Creado zip: " + id + ".zip");
        })

        reportesDisponibles.set(JSON.stringify(reportes), id);
        fs.rmdir("archivos/" + id, {recursive:true}, (err) => {
          if (err) {
            throw err;
          }
        });
        console.log("*** Respuesta enviada id:" + id);
        res.send({"id":id});
        
      } catch(err) {
        console.log("*** Ocurrio un error: " + err);
        res.status(500).send("Ocurrio un error al crear archivo.");
      }

    }
}

exports.descargarReporte = (req, res) => {
    if (typeof req.query.a === 'undefined') {
      res.status(500).send("Parametros incorrectos.")
    } else {
      const file = dirname + "/archivos/"+req.query.a+".zip";

      fs.access(file, fs.F_OK, (err) => {
        if (err) {
          console.log("*** No existe archivo: " + file);
          res.status(500).send("Parametros incorrectos.");
          return
        }
        console.log("*** Se descarga " + file);
        const nombreReporte = req.query.a.split("@")
        const nombreZip =nombreReporte[0] + ".zip";
        res.status(200);
        res.download(file, nombreZip);
        setTimeout(borrarArchivo, 10*60000, dirname + "/archivos/"+req.query.a + ".zip", req.query.a);
      })
    }
}

borrarArchivo = (archivo, id) => {
  try {
    fs.unlink( archivo, (err) => {
      if (err) {
        throw err;
    }
      if(reportesDisponibles.search(id)) {
        reportesDisponibles.delete(reportesDisponibles.search(id));
        console.log("*** Se vence archivo" + id);
      }
      console.log("*** Se elimina archivo = " + archivo);
    });
  } catch(err) {
    console.log("*** No se pudo borrar archivo " + id + " - " + err);
  }
}

formatearNombreArchivo = (nombre) => {
  nuevoNombre = nombre.replace(/[#%&{}<>*?=/ $!'":@\\]/g, "");
  if (nuevoNombre.length === 0) {
    return "reporte";
  }
  return nuevoNombre;
}