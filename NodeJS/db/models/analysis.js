const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const ObjectId = Schema.ObjectId;
const twitter = require('../databases/twitter');

const Integer = {
    type: Number,
    validate: {
        validator: Number.isInteger,
        message: "{VALUE} is not an integer."
    }
};

const analysis_schema = new Schema({
    _id: ObjectId,
    tweetCount: Integer,
    characterCount: Integer,
    min: Integer,
    avg: Integer,
    max: Integer,
    //emoji: Array,
    emojiCount: Integer,
    alpha: Array,
    totalPercentage: String,
    users: Array
});

module.exports = twitter.model('analysis', analysis_schema, 'analysis');