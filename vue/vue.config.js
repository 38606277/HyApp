module.exports = {
  publicPath: './',
   outputDir: '../android/HyAppAndroid/app/src/main/assets/dist',
  devServer: {
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:3000',
        ws: true,
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/'
        }
      }
    }
  },
  
};
