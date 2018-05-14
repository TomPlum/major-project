const express = require('express');
const router = express.Router();
const analysis = require("../db/models/analysis");
const tweets = require("../db/models/tweets");
const results = require("../db/models/results");
const countryNames = require('i18n-iso-countries');
const fs = require('fs');
const mime = require('mime');

function encodeRFC5987ValueChars (str) {
    return encodeURIComponent(str).
    replace(/['()]/g, escape).
    replace(/\*/g, '%2A').
    replace(/%(?:7C|60|5E)/g, unescape);
}

function downloadFile(filepath, filename, res) {
    const mimeType = mime.getType(__dirname + filepath);

    fs.readFile(filepath, function(err) {
        if (err) console.log(err);
        res.setHeader("Content-type", mimeType);
        res.setHeader("Content-disposition", 'download; filename=' + encodeRFC5987ValueChars(filename));
        const filestream = fs.createReadStream(filepath);
        filestream.pipe(res);
    });
}

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

/* Download Dissertation */
router.get('/dissertation', function(req, res) {
    const filepath = "public/doc/dissertation.docx";
    const filename = "Tom Plumpton - University Dissertation.docx";
    downloadFile(filepath, filename, res);
});

/* Download Vivia Presentation */
router.get('/viva-presentation', function(req, res) {
    const filepath = "public/ppt/viva.pptx";
    const filename = "Tom Plumpton - Major Project Viva.pptx";
    downloadFile(filepath, filename, res);
});

/* Download Supervisor Log */
router.get('/supervisor-log', function(req, res) {
    const filepath = "public/doc/log.docx";
    const filename = "Tom Plumpton - Supervisor Log.docx";
    downloadFile(filepath, filename, res);
});

router.post('/calculate-user-stats', function(req, res) {
    tweets.find({user: req.body.username}, (err, data) => {
        if (err) { console.log(err);}

        let linkNo = 0;
        let twitterNo = 0;
        let hashtags = 0;
        let mentions = 0;
        let lang = {};
        let shortestTweet = 9999;
        let longestTweet = 0;
        let averageTweet = 0;

        console.log("TEST: " + countryNames.getName("ar", "en"));
        for (let i = 0; i < data.length; i++) {
            //Extract Tweet
            let tweet = data[i].text;

            //Count Hyperlinks
            if (tweet.includes("http://") || tweet.includes("https://") || tweet.includes("www.") || tweet.includes(".com")) {
                linkNo++;
            }

            //Count Language Types
            //console.log(data[i].language);
            let country = data[i].language === "en" ? "English" : countryNames.getName(data[i].language, "en");
            //console.log(country);

            if (!lang.hasOwnProperty(country)) {
                lang[country] = 1;
            } else {
                lang[country]++;
            }

            //Count Hastags
            if (tweet.includes("#")) {
                hashtags++;
            }

            //Count Times Twitter is Used
            if (tweet.includes("twitter") || tweet.includes("Twitter")) {
                twitterNo++;
            }

            //Count Mentions
            if (tweet.includes("@")) {
                mentions++;
            }

            //Find Longest, Shortest & Average Tweet Length
            if (tweet.length > longestTweet) {
                longestTweet = tweet.length;
            }
            if (tweet.length < shortestTweet) {
                shortestTweet = tweet.length;
            }
            averageTweet += tweet.length;
        }

        res.status(200).send({
            userTweetCount: data.length,
            links: linkNo,
            twitterUsed: twitterNo,
            mentions: mentions,
            hashtags: hashtags,
            language: lang,
            shortestTweet: shortestTweet,
            longestTweet: longestTweet,
            averageTweet: parseInt(averageTweet / data.length)
        });
    });
});

router.post('/get-robocode-results', function(req, res) {
    results.find({}, function(err, data) {
       if (err) {console.log(err);}
       res.status(200).send(data);
    });
});

module.exports = router;