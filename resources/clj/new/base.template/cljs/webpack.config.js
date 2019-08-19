module.exports = {
    entry: './src/js/index.js',
    output: {
        filename: 'index.bundle.js'
    },
    module: {
        rules: [
            {test: /\.css$/, use: ['style-loader', 'css-loader']},
            {test: /\.ts$/, use: 'ts-loader'}
        ]
    }
};
