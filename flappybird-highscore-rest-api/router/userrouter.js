var express = require('express')

var router = express.Router()

var usercontroller = require('../controller/usercontroller')

router.get('/', usercontroller.getUser)
router.post('/', usercontroller.addUser)
router.delete('/', usercontroller.deleteUser)
router.put('/', usercontroller.updateUser)

module.exports = router