var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var photoSchema = new Schema({
	'image' : String,
	'username' : String,
	'date' : {
		type: Date,
		default: new Date()
	},
	'uuid' : String
});

module.exports = mongoose.model('photo', photoSchema);
