var database = require('../model/database')

exports.addScore = async function(req, res) {
    let token = req.body.usertoken
    let score = req.body.score;
    let result = await database.addScore(token, score)
    res.send(result)
}

exports.deleteScore = async function(req, res) {
    let token = req.headers.usertoken
    let result = await database.deleteScore(token)
    res.send(result)
}

exports.getScore = async function(req, res) {
    let token = req.headers.usertoken
    let result = await database.getScore(token)
    res.send(result)
}

exports.updateScore = async function(req, res) {
    let token = req.body.usertoken
    let score = req.body.score
    let result = await database.updateScore(token, score)
    res.send(result)
}