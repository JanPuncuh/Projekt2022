var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
	const { spawn } = require('child_process');
    const pyProg = spawn('python', ['script.py']);

    pyProg.stdout.on('data', function(data) {

        console.log(data.toString());
		console.log("xde");
        res.write(data);
        res.end('end');
    });
	
	
  res.render('index', { title: 'Express' , userId: req.session.userId, username:req.session.username });
});



	


module.exports = router;
