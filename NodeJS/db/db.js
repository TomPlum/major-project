let createConnectionUri = function createConnectionUri(db) {
    const prefix = 'mongodb://TomPlum:i7ljjmXIi19PK1CU@twitter-shard-00-00-yu9se.mongodb.net:27017,twitter-shard-00-01-yu9se.mongodb.net:27017,twitter-shard-00-02-yu9se.mongodb.net:27017/';
    const suffix = '?ssl=true&replicaSet=twitter-shard-0&authSource=admin';
    return prefix + db + suffix;
};

module.exports = {
    'twitter': createConnectionUri('twitter'),
};