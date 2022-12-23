const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack:{
    performance: { 
      hints: false,
    },
    optimization:{
      minimize:true,
      splitChunks:{
        chunks: 'all',
      }
    }
  },
  devServer : {
    proxy : {
      '/fligent' : {
        target : 'http://127.0.0.1:8080/',
        changeOrigin : true,
        logLevel : 'debug'
      },
      '/ROOT/*' : {
        target : 'http://127.0.0.1:8080/',
        changeOringin : true,
        pathRewrite: { '^/ROOT': '/fligent' }
      }, 
      // '^/oauth2.0' : { 
      //   'target' : 'https://nid.naver.com/',
      //   'changeOringin' : true,
      //   'pathRewrite': { '^/oauth2.0': '/' },
      //   'secure':false
      // },
      // "/oauth2.0" : { // 원본
      //   target : 'https://nid.naver.com/',
      //   changeOrigin : true,
      //   logLevel : 'debug'
      // }, // 수정
      // '^/v1' : {
      //   'target' : 'https://openapi.naver.com/',
      //   'pathRewrite' : { '^/v1': '/' },
      //   'changeOringin' : true,
      //   'secure':false,
      //   'logLevel' : 'debug'
      // },
      // "/v1" : { //원본
      //   target : 'https://openapi.naver.com/',
      //   changeOrigin : true,
      //   logLevel : 'debug'
      // },
    },
    port : 9090
  },
  // = context path + 폴더
  // fligent = http://127.0.0.1:8080/
  // vue = 백엔드/resources/static/vue
  // publicPath : '/fligent',
  publicPath : '/fligent/vue',
    // 개발시, 배포시 publicPath 다르게
    // publicPath: process.env.NODE_ENV === 'production'  ? '/fligent/vue/' : '/',
  // 배포 위치 설정
  // build한 후 저장되는 위치(경로) 지정
  outputDir : 'C:/Java/backend/airport6341/src/main/resources/static/vue'
  // outputDir : 'C:/Users/ASUS/Desktop/naver/backend/airport6341/src/main/resources/static/vue'
  // outputDir : 'C:/Code/backend/airport6341/src/main/resources/static/vue'

})
