var database = require('../model/database')

exports.addUser = async function(req, res) {
    let username = req.body.username;
    let password = req.body.password;

    var result = await database.addUser(username, password);

    res.send(result);
}

exports.updateUser = async function(req, res) {
    let username = req.body.username;
    let password = req.body.password;
    let usertoken = req.body.usertoken;

    var result = await database.updateUser(usertoken, username, password);

    res.send(result);
}

exports.getUser = async function(req, res) {
    let token = req.headers.usertoken;

    var result = await database.getUser(token);

    res.send(result);
}

exports.deleteUser = async function(req, res) {
    let token = req.headers.usertoken;

    var result = await database.deleteUser(token);

    res.send(result);
}