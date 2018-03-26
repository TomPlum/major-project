const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const ObjectId = Schema.ObjectId;
const twitter = require('../databases/twitter');

const results_schema = new Schema({
    _id: ObjectId,
    date: Date,
    results: Array
});

module.exports = twitter.model('results', results_schema, 'results');