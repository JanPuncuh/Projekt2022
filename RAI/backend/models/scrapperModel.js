var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var scrapperSchema = new Schema({
    'info': String,
    'date': Date
});

module.exports = mongoose.model('Scrapper', scrapperSchema);
