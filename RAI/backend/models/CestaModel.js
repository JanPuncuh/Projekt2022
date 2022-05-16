var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var CestaSchema = new Schema({
	'longitude' : Number,
	'latitude' : Number,
	'gyroscopeX' : Number,
	'gyroscopeY' : Number,
	'gyroscopeZ' : Number,

	'accelerationX' : Number,
	'accelerationY' : Number,
	'accelerationZ' : Number,
	'user_id' : String,
	'timeStamp' : {
		type: Date,
		default: new Date()
	},
	'uniqueID' : String
});

module.exports = mongoose.model('Cesta', CestaSchema);
