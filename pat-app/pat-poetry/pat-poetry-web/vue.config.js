const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    pages: {
        index: {
            entry: 'src/main.js',
            title: '百诗',
        }
    },
    devServer: {

        proxy: {
            '/api': {
                target: 'http://localhost:18080',
                // ws : true,
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            },
            '/oauth': {
                target: 'http://localhost:18081',
                // ws : true,
                changeOrigin: true,
                pathRewrite: {
                    '^/oauth': ''
                }
            }

        }
    }
})
