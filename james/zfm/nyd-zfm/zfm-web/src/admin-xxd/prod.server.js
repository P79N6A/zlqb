var express = require('express')
var config = require('./config/index')
var axios = require('axios')

var port = process.env.PORT || config.build.port
var proxyMiddleware = require('http-proxy-middleware')
var app = express()

var apiRoutes = express.Router()

apiRoutes.get('/test.json', function (req, res) {
  // TODO
})

// app.use('/api', apiRoutes)
const apiPayProxy = proxyMiddleware('/zfm', {target: 'http://47.103.128.134:8095', changeOrigin: true})

app.use('/zfm', apiPayProxy)

app.use(express.static('./dist'))

module.exports = app.listen(50000, function (err) {
  if (err) {
    console.log(err)
    return
  }
  console.log('Listening at http://localhost:' + 50000 + '\n')
})
