var CestaModel = require('../models/CestaModel.js');

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

            return res.json(Cestas);
        });
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
        console.log(req.body);
        var Cesta = new CestaModel({
			longitude : req.body.longitude,
			latitude : req.body.latitude,
            
            accelerationX : req.body.accelX,
            accelerationY : req.body.accelY,
            accelerationZ : req.body.accelZ,

            gyroscopeX : req.body.gyroX,
            gyroscopeY : req.body.gyroY,
            gyroscopeZ : req.body.gyroZ,

			user_id : req.body.user_id
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
