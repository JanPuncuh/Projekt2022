var express = require('express');
var router = express.Router();
var CestaController = require('../controllers/CestaController.js');

/*
 * GET
 */
router.get('/session/last', CestaController.sessionLast);
router.get('/', CestaController.list);
router.get('/scrapper', CestaController.scrape);
router.get('/scrapper/list', CestaController.listScrapper);


router.get('/session/:id', CestaController.session);


router.get('/sessions/:id', CestaController.sessions);

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
