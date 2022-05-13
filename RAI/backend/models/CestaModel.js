var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var CestaSchema = new Schema({
	'Longitude' : Number,
	'Latitude' : Number,
	'Altitude' : Number,
	'user_id' : Number
});

module.exports = mongoose.model('Cesta', CestaSchema);
