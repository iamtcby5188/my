'use strict';

// Modules
// ReSharper disable UndeclaredGlobalVariableUsing
var webpack = require('webpack'),
    util = require("util"),
    path = require('path');

var autoprefixer = require('autoprefixer');
// var LessPluginCleanCss = require('less-plugin-clean-css');
// ReSharper restore UndeclaredGlobalVariableUsing

const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = function makeWebpackConfig() {

    /**
     * Config
     * Reference: http://webpack.github.io/docs/configuration.html
     * This is the object where all configuration gets set
     */
    var config = {};

    /**
     * Entry
     * Reference: http://webpack.github.io/docs/configuration.html#entry
     * Should be an empty object if it's generating a test build
     * Karma will set this when it's a test build
     */
    config.entry = {
        app: ['./src/app/appConfig.js']
    };

    /**
     * Loaders
     * Reference: http://webpack.github.io/docs/configuration.html#module-loaders
     * List: http://webpack.github.io/docs/list-of-loaders.html
     * This handles most of the magic responsible for converting modules
     */

    // Initialize module
    config.module = {
        preLoaders: [],

        loaders: [{
            // JS LOADER
            // Reference: https://github.com/babel/babel-loader
            // Transpile .js files using babel-loader
            // Compiles ES6 and ES7 into ES5 code
            test: /\.js$/,
            loader: 'babel',
            exclude: /node_modules/
        }, {
            //// CSS LOADER
            //// Reference: https://github.com/webpack/css-loader
            //// Allow loading css through js
            ////
            //// Reference: https://github.com/postcss/postcss-loader
            //// Postprocess your css with PostCSS plugins
            //test: /\.css$/,
            //// Reference: https://github.com/webpack/extract-text-webpack-plugin
            //// Extract css files in production builds
            ////
            //// Reference: https://github.com/webpack/style-loader
            //// Use style-loader in development.
            test: /\.less$/,
            loader: 'style-loader?sourceMap!css-loader!postcss-loader!less-loader'
        }, {
            test: /\.css$/,
            loader: 'style-loader?sourceMap!css-loader!postcss-loader'
        }, {
            // ASSET LOADER
            // Reference: https://github.com/webpack/file-loader
            // Copy png, jpg, jpeg, gif, svg, woff, woff2, ttf, eot files to output
            // Rename the file using the asset hash
            // Pass along the updated reference to your code
            // You can add here any file extension you want to get copied to your output
            test: /\.(png|jpg|jpeg|gif|svg|woff|woff2|ttf|eot)$/,
            loader: 'file'
        }, {
            // HTML LOADER
            // Reference: https://github.com/webpack/raw-loader
            // Allow loading html through js
            test: /\.html$/,
            loader: 'raw'
        }]
    };

    config.resolve = {

        alias: {

            'ngAnimate': 'angular-animate/angular-animate.js',
            'ngAria': 'angular-aria/angular-aria.js',
            'ngMessages': 'angular-messages/angular-messages.js',
            'ngMd5': 'angular-md5/angular-md5.js',
            'ngSanitize': 'angular-sanitize/angular-sanitize.js',

            'amcharts': path.resolve('lib/amcharts/amcharts.js'),
            'amcharts-serial': path.resolve('lib/amcharts/serial.js'),
            'amcharts-responsive': path.resolve('lib/amcharts/plugins/responsive/responsive.js'),
            'amcharts.css': path.resolve('lib/amcharts/style.css'),


            // 'ngMaterial': 'angular-material/angular-material.js',
            // 'ngMaterial.css': 'angular-material/angular-material.css',
            'ngMaterial': path.resolve('lib/angular-material/angular-material.js'),
            'ngMaterial.css': path.resolve('lib/angular-material/angular-material.css'),

            'angular-ui-grid': 'angular-ui-grid/ui-grid',
            'reconnectingwebsocket': path.resolve('lib/reconnectingwebsocket/reconnecting-websocket.js'),

            // 'ngComponentRouter': 'ngcomponentrouter/index.js',
            'ngComponentRouter': 'angular-component-router-noscope/angular1/angular_1_router.js',

            //{ test: './lib/avalon-ui.1.3/dist/avalon-ui.js', loader: 'script'},
            //{ test: './lib/avalon-ui.1.3/dist/avalon-ui.css', loader: 'style-loader' },
            //{ test: './lib/avalon-ui.1.3/dist/avalon-ui-icon.js', loader: 'script' }
            'avalon-ui': path.resolve('lib/avalon-ui.1.3/dist/avalon-ui.js'),
            'avalon-ui.css': path.resolve('lib/avalon-ui.1.3/dist/avalon-ui.css'),
            'avalon-ui-icon.css': path.resolve('lib/avalon-ui.1.3/dist/avalon-ui-icon.css'),
            'spin': path.resolve('lib/spin/spin.min.js'),
            // 'angular-ui-router': 'angular-ui-router/release/angular-ui-router.js',            

            // 'config.js': path.resolve(__dirname, 'config/config.dev.js')

        }
    };

    /**
     * PostCSS
     * Reference: https://github.com/postcss/autoprefixer-core
     * Add vendor prefixes to your css
     */
    config.postcss = [
        autoprefixer({
            browsers: ['last 2 version']
        })
    ];


    /**
     * Dev server configuration
     * Reference: http://webpack.github.io/docs/configuration.html#devserver
     * Reference: http://webpack.github.io/docs/webpack-dev-server.html
     */
    config.devServer = {
        contentBase: './src/public',
        stats: 'minimal'
        //inline: true,
        //hot: true
    };

    /**
     * Plugins
     * Reference: http://webpack.github.io/docs/configuration.html#plugins
     * List: http://webpack.github.io/docs/list-of-plugins.html
     */
    config.plugins = [
        new webpack.ProvidePlugin({
            $: "jquery",
            jQuery: "jquery",
            Spinner:"spin"
        }),

        // Reference: https://github.com/ampedandwired/html-webpack-plugin
        // Render index.html
        new HtmlWebpackPlugin({
            template: './src/public/index.html',
            inject: 'body'
        })
    ]

    return config;
};