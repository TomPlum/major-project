const express = require('express');
const router = express.Router();
const analysis = require("../db/models/analysis");
const tweets = require("../db/models/tweets");

router.get('/', function(req, res) {
   analysis.find().exec({}, function(err, data) {
      if (err) {
          console.log(err);
      }
      res.render('index', {
          title: "Major Project",
          alpha: data[0].alpha,
          tweetCount: data[0].tweetCount,
          charCount: data[0].characterCount,
          emojiCount: data[0].emojiCount
      });
   });
});

module.exports = router;