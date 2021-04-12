var express = require('express')

var router = express.Router()

var scorecontroller = require('../controller/scorecontroller')

router.get('/', scorecontroller.getScore)
router.post('/', scorecontroller.addScore)
router.delete('/', scorecontroller.deleteScore)
router.put('/', scorecontroller.updateScore)

module.exports = router