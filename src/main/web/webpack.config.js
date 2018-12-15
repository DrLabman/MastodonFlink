const path = require("path");
module.exports = {
    entry: ["./src/js/app.js"],
    output: {
        path: path.resolve(__dirname, "dist"),
        filename: "js/[name].js"
    },
    devtool: "source-map",
    module: {
        rules: [
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader",
                    options: {
                        presets: ['react']
                    }
                }
            }
        ]
    },
    watchOptions: {
        ignored: /node_modules/
    }
};