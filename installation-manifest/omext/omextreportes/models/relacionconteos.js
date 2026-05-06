const moongose = require("../db")

const schema = new moongose.Schema(
    {
        //el id es el collname
        _id: Number,
        grupos : [
            {
                id : Number,
                nombre: String, 
                entes : [{type: String}]
            }
        ]
    }
);

let RelacionConteos = moongose.model("relacionconteos", schema);

module.exports = RelacionConteos;