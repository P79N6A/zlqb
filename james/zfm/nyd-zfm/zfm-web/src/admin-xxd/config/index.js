// see http://vuejs-templates.github.io/webpack for documentation.
var path = require("path");

module.exports = {
    build: {
        env: require("./prod.env"),
        // index: path.resolve(__dirname, "../dist/index.html"),
        // assetsRoot: path.resolve(__dirname, "../dist"),
        index: path.resolve(__dirname, '../../main/resources/static/index.html'),
        assetsRoot: path.resolve(__dirname, '../../main/resources/static'),
        // assetsSubDirectory: "static",
        // assetsPublicPath: "/admin/",
        assetsSubDirectory: '',
        assetsPublicPath: '',
        productionSourceMap: true,
        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: false,
        productionGzipExtensions: ["js", "css"],
        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report
    },
    dev: {
        env: require("./dev.env"),
        port: 51500,
        autoOpenBrowser: true,
        assetsSubDirectory: "static",
        assetsPublicPath: "/",
        proxyTable: {
            '/user': {
                target: 'https://api.zefandata.com',
                changeOrigin: true
            },
            '/zfm': {
                target: 'http://47.103.128.134:8095', // 王
                changeOrigin: true
            },
            // '/zfm': {
            //     target: 'http://192.168.1.27:8095', // 王
            //     changeOrigin: true
            // },
            // '/zfm': {
            //     target: 'http://192.168.1.32:8083', // 柯昂
            //     changeOrigin: true
            // },
            // '/zfm': {
            //     target: 'http://47.103.128.134:8095', // 生产
            //     changeOrigin: true
            // },
            // '/zfm': {
            //     target: 'http://192.168.1.24:8083', // 郭玉婷
            //     changeOrigin: true
            // },
            '/order': {
                target: 'https://api.zefandata.com',
                changeOrigin: true
            },
            '/application': {
                target: 'https://api.zefandata.com',
                changeOrigin: true
            }
        },
        // CSS Sourcemaps off by default because relative paths are "buggy"
        // with this option, according to the CSS-Loader README
        // (https://github.com/webpack/css-loader#sourcemaps)
        // In our experience, they generally work as expected,
        // just be aware of this issue when enabling this option.
        cssSourceMap: false
    }
};