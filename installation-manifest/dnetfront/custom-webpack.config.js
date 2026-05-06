const webpack = require('webpack');
// const envir = require('./rutas.json');
var ruta = require('fs').readFileSync('./rutas.json', 'utf8');


module.exports = {
    plugins: [
        new webpack.DefinePlugin({
            $ENV: {
                API_RESOURCES: JSON.parse(JSON.stringify(ruta))
                    // ,
                    // HOSTNAME: JSON.parse(JSON.stringify(ruta)).HOSTNAME,
                    // API_BASE: JSON.parse(JSON.stringify(ruta)).API_BASE,
                    // API_ENTES: JSON.parse(JSON.stringify(ruta)).API_ENTES,
                    // API_CONSULTAR_ENTES: JSON.parse(JSON.stringify(ruta)).API_CONSULTAR_ENTES,
                    // API_RENOVAR: JSON.parse(JSON.stringify(ruta)).API_RENOVAR,
                    // ID_CLIENTE: JSON.parse(JSON.stringify(ruta)).ID_CLIENTE,
                    // SECRET_KEY: JSON.parse(JSON.stringify(ruta)).SECRET_KEY,
                    // ID_CLIENTE_SIGN: JSON.parse(JSON.stringify(ruta)).ID_CLIENTE_SIGN,
                    // SECRET_KEY_SIGN: JSON.parse(JSON.stringify(ruta)).SECRET_KEY_SIGN,
                    // API_IDP: JSON.parse(JSON.stringify(ruta)).API_IDP,
                    // API_SIGN: JSON.parse(JSON.stringify(ruta)).API_SIGN,
                    // API_IDP_AUTH: JSON.parse(JSON.stringify(ruta)).HOSTNAME,
                    // LOGGER: JSON.parse(JSON.stringify(ruta)).LOGGER,
                    // STAGING_HOSTNAME: JSON.parse(JSON.stringify(ruta)).STAGING_HOSTNAME || '',
                    // STAGING_API_BASE: JSON.parse(JSON.stringify(ruta)).STAGING_API_BASE || '',
                    // STAGING_API_ENTES: JSON.parse(JSON.stringify(ruta)).STAGING_API_ENTES || '',
                    // STAGING_API_CONSULTAR_ENTES: JSON.parse(JSON.stringify(ruta)).STAGING_API_CONSULTAR_ENTES || '',
                    // STAGING_API_RENOVAR: JSON.parse(JSON.stringify(ruta)).STAGING_API_RENOVAR || '',
                    // STAGING_ID_CLIENTE: JSON.parse(JSON.stringify(ruta)).STAGING_ID_CLIENTE || '',
                    // STAGING_SECRET_KEY: JSON.parse(JSON.stringify(ruta)).STAGING_SECRET_KEY || '',
                    // STAGING_ID_CLIENTE_SIGN: JSON.parse(JSON.stringify(ruta)).STAGING_ID_CLIENTE_SIGN || '',
                    // STAGING_SECRET_KEY_SIGN: JSON.parse(JSON.stringify(ruta)).STAGING_SECRET_KEY_SIGN || '',
                    // STAGING_API_IDP: JSON.parse(JSON.stringify(ruta)).STAGING_API_IDP || '',
                    // STAGING_API_SIGN: JSON.parse(JSON.stringify(ruta)).STAGING_API_SIGN || '',
                    // STAGING_API_IDP_AUTH: JSON.parse(JSON.stringify(ruta)).STAGING_API_IDP_AUTH || '',
                    // STAGING_LOGGER: JSON.parse(JSON.stringify(ruta)).STAGING_LOGGER || '',
                    // REVIEW_HOSTNAME: JSON.parse(JSON.stringify(ruta)).REVIEW_HOSTNAME || 'SinDatos',
                    // REVIEW_API_BASE: JSON.parse(JSON.stringify(ruta)).REVIEW_API_BASE || 'SinDatos',
                    // REVIEW_API_ENTES: JSON.parse(JSON.stringify(ruta)).REVIEW_API_ENTES || 'SinDatos',
                    // REVIEW_API_CONSULTAR_ENTES: JSON.parse(JSON.stringify(ruta)).REVIEW_API_CONSULTAR_ENTES || 'SinDatos',
                    // REVIEW_API_RENOVAR: JSON.parse(JSON.stringify(ruta)).REVIEW_API_RENOVAR || 'SinDatos',
                    // REVIEW_ID_CLIENTE: JSON.parse(JSON.stringify(ruta)).REVIEW_ID_CLIENTE || 'SinDatos',
                    // REVIEW_SECRET_KEY: JSON.parse(JSON.stringify(ruta)).REVIEW_SECRET_KEY || 'SinDatos',
                    // REVIEW_ID_CLIENTE_SIGN: JSON.parse(JSON.stringify(ruta)).REVIEW_ID_CLIENTE_SIGN || 'SinDatos',
                    // REVIEW_SECRET_KEY_SIGN: JSON.parse(JSON.stringify(ruta)).REVIEW_SECRET_KEY_SIGN || 'SinDatos',
                    // REVIEW_API_IDP: JSON.parse(JSON.stringify(ruta)).REVIEW_API_IDP || 'SinDatos',
                    // REVIEW_API_SIGN: JSON.parse(JSON.stringify(ruta)).REVIEW_API_SIGN || 'SinDatos',
                    // REVIEW_API_IDP_AUTH: JSON.parse(JSON.stringify(ruta)).REVIEW_API_IDP_AUTH || 'SinDatos',
                    // REVIEW_LOGGER: JSON.parse(JSON.stringify(ruta)).REVIEW_LOGGER || 'SinDatos'

            }
        })
    ]
};