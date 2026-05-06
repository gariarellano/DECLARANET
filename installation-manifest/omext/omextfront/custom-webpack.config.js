const webpack = require('webpack');
// const envir = require('./rutas.json');
var ruta = require('fs').readFileSync('./rutas.json', 'utf8');


module.exports = {
    plugins: [
        new webpack.DefinePlugin({
            $ENV: {
                API_RESOURCES: JSON.parse(JSON.stringify(ruta))

            }
        })
    ]
};