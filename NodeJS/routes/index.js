const express = require('express');
const router = express.Router();
const analysis = require("../db/models/analysis");
const tweets = require("../db/models/tweets");

router.get('/', function(req, res) {
   analysis.find().exec({}, function(err, data) {
      if (err) {
          console.log(err);
      }

      console.log(data[0].users[0].username);

      res.render('index', {
          title: "Major Project",
          tweetCount: data[0].tweetCount,
          charCount: data[0].characterCount,
          emojiCount: data[0].emojiCount,
          users: JSON.stringify(data[0].users)
      });
   });
});

router.post('/character-analysis', function(req, res) {
    analysis.find().exec({}, function(err, data) {
        if (err) {
            console.log(err);
        }
        res.status(200).send(data[0].alpha);
    });
});

module.exports = router;