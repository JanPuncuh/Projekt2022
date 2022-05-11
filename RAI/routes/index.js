var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' , userId: req.session.userId, username:req.session.username });
});

module.exports = router;
