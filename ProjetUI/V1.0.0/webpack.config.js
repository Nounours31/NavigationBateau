const webpack = require('webpack');
const path = require('path');



module.exports = (env, argv) => {
    const production = argv.mode === 'production';
    const productionPlugins = [];

    var retour =  {
        mode: argv.mode || 'development',
        entry: './src/index.ts',
        devtool: production ? 'hidden-source-map' : 'source-map',
        watch: true,
        watchOptions: {
            ignored: ['**/files/**/*.js', '**/node_modules'],
        },
        module: {
            rules: [
                {
                    test: /\.(js|ts)x?$/,
                    include: path.resolve(__dirname, 'src'),
                    use: 'ts-loader',
                    exclude: /node_modules/,
                },
                {
                    test: /\.s[ac]ss$/i,
                    use: [
                        // Creates `style` nodes from JS strings
                        "style-loader",
                        // Translates CSS into CommonJS
                        "css-loader",
                        // Compiles Sass to CSS
                        "sass-loader",
                    ],
                },
            ],
        },
        resolve: {
            extensions: ['.tsx', '.ts', '.js'],
        },
        output: {
            filename: 'index.js',
            path: path.resolve(__dirname, 'dist'),
        },
        devServer: {
            static: {
              directory: __dirname,
            },
            client: {
                progress: true,
            },
            compress: true,
            port: 9000,
          },
    };


    return retour;
};