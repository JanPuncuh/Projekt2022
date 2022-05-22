var CestaModel = require('../models/CestaModel.js');

const axios = require("axios")
const cheerio = require("cheerio")

/**
 * CestaController.js
 *
 * @description :: Server-side logic for managing Cestas.
 */
module.exports = {

    /**
     * CestaController.list()
     */
    list: function (req, res) {
        CestaModel.find(function (err, Cestas) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting Cesta.',
                    error: err
                });
            }
            var temp = []
            Cestas = Cestas.filter((item) => {
                if (!temp.includes(item.latitude)) {
                    temp.push(item.latitude)
                    return true;
                }
            })

            return res.json(Cestas);
        });
    },

    scrape: function (req, res) {
        res.render('index', {title: 'Express'});

        const url = "https://www.24ur.com/novice/ceste"

        axios(url)
            .then(response => {
                const html = response.data
                //console.log(html)
                const $ = cheerio.load(html)

                //sam spodn table
                //$('div.table-wrapper.table-rounded.table-scroll-y').find("tr").each((idx, ref) => {

                //oba table
                $('table').find("tr").each((idx, ref) => {
                    const elem = $(ref);

                    const splitElem = elem.text().split(',')
                    const road = splitElem[0]
                    //brezveze splitat, ker so nekonstantni podatki (kagdaj ni oznake ceste al pa vejca fali...)
                    //al pa bi mogu z regexom preverjat

                    //todo model in shranuj v bazo (kot 1 podatek?)

                    console.log(elem.text())
                });
            }).catch(err => console.log(err))
    },

    /**
     * CestaController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        CestaModel.findOne({_id: id}, function (err, Cesta) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting Cesta.',
                    error: err
                });
            }

            if (!Cesta) {
                return res.status(404).json({
                    message: 'No such Cesta'
                });
            }

            return res.json(Cesta);
        });
    },

    /**
     * CestaController.create()
     */
    create: function (req, res) {
        console.log(req.body.latitude + "  , ", req.body.longitude + "  id: ", req.body.uid);
        var Cesta = new CestaModel({
            longitude: req.body.longitude,
            latitude: req.body.latitude,

            accelerationX: req.body.accelX,
            accelerationY: req.body.accelY,
            accelerationZ: req.body.accelZ,

            gyroscopeX: req.body.gyroX,
            gyroscopeY: req.body.gyroY,
            gyroscopeZ: req.body.gyroZ,
            timeStamp: new Date(),
            user_id: req.body.user_id,

            uniqueID: req.body.uid
        });

        Cesta.save(function (err, Cesta) {
            if (err) {
                console.log(err);
                return res.status(500).json({
                    message: 'Error when creating Cesta',
                    error: err
                });
            }

            return res.status(201).json(Cesta);
        });
    },

    /**
     * CestaController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        CestaModel.findOne({_id: id}, function (err, Cesta) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting Cesta',
                    error: err
                });
            }

            if (!Cesta) {
                return res.status(404).json({
                    message: 'No such Cesta'
                });
            }

            Cesta.Longitude = req.body.Longitude ? req.body.Longitude : Cesta.Longitude;
            Cesta.Latitude = req.body.Latitude ? req.body.Latitude : Cesta.Latitude;
            Cesta.Altitude = req.body.Altitude ? req.body.Altitude : Cesta.Altitude;
            Cesta.user_id = req.body.user_id ? req.body.user_id : Cesta.user_id;

            Cesta.save(function (err, Cesta) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating Cesta.',
                        error: err
                    });
                }

                return res.json(Cesta);
            });
        });
    },

    /**
     * CestaController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        CestaModel.findByIdAndRemove(id, function (err, Cesta) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the Cesta.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
