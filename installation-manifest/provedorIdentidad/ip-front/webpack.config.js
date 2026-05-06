const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const test = require('fs').readFileSync('./rutas.json', 'utf8');
const path = require('path');

module.exports = {
    entry: './src/main.ts',
    module: {
        rules: [{
                test: /\.ts$/,
                use: ['ts-loader', 'angular2-template-loader'],
                exclude: /node_modules/
            },
            {
                test: /\.(html|css)$/,
                loader: 'raw-loader'
            },
        ]
    },
    resolve: {
        extensions: ['.ts', '.js'],
        alias: {
            '@': path.resolve(__dirname, 'src/app/'),
        }
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: './src/index.html',
            filename: 'index.html',
            inject: 'body'
        }),
        new webpack.DefinePlugin({
            // global app config object
            $ENV: {
                API_RESOURCES: JSON.parse(JSON.stringify(test))
            }
        })
    ],
    optimization: {
        splitChunks: {
            chunks: 'all',
        },
        runtimeChunk: true
    },
    devServer: {
        historyApiFallback: true
    }
};