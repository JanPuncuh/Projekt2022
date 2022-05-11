var express = require('express');
var router = express.Router();
var CestaController = require('../controllers/CestaController.js');

/*
 * GET
 */
router.get('/', CestaController.list);

/*
 * GET
 */
router.get('/:id', CestaController.show);

/*
 * POST
 */
router.post('/', CestaController.create);

/*
 * PUT
 */
router.put('/:id', CestaController.update);

/*
 * DELETE
 */
router.delete('/:id', CestaController.remove);

module.exports = router;
