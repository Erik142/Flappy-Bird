const express = require('express')
const fs = require('fs')
const path = require('path')
var scorerouter = require('./router/scorerouter')
var userrouter = require('./router/userrouter')

const relativeConfigPath = './config/config.json'

const configPath = path.join(__dirname, relativeConfigPath)

var config = JSON.parse(fs.readFileSync(configPath, 'utf8'));

const app = express();

app.use(express.json())
app.use(express.urlencoded({ extended: true }))

app.use('/score', scorerouter)
app.use('/user', userrouter)

app.listen(config.port, config.listenAddress)