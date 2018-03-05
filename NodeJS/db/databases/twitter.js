const mongoose = require('mongoose');
const dbConfig = require('../db');

const twitter = mongoose.createConnection(dbConfig.twitter);

twitter.once('connected', () => {
    console.log("Successfully Connected To Twitter.");
});

twitter.once('disconnected', () => {
    console.log("Disconnected From Twitter.");
});

twitter.once('error', err => {
    console.log("Twitter Error: " + err);
});

twitter.on('SIGINT', () => {
    mongoose.connection.close(() => {
        console.log("App Terminated: (Twitter) Mongoose Connection Closed.");
        process.exit(0);
    });
});

module.exports = twitter;