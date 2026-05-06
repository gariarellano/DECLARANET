const mongoose = require("mongoose");
const config = require("./config/config");

console.log("=== Empieza conexion a MongoDB" );

const uri = "mongodb://" + config.mongoUserName + ":" + config.mongoPassword
  + "@" + config.mongoHost + ":" + config.mongoPort + "/" + config.mongoDbName;

mongoose.connect(uri, {useUnifiedTopology: true,useNewUrlParser: true})
const db = mongoose.connection;

db.on('error', console.error.bind(console, 'Error en la conexion.'));
db.once('open', function() {
  console.log("Conexion correcta")
});

module.exports = mongoose;