let createConnectionUri = function createConnectionUri(db) {
    const prefix = 'mongodb://TomPlum:zw3GHs6xmB1bPKgw@portfolio-shard-00-00-bw3cz.mongodb.net:27017,portfolio-shard-00-01-bw3cz.mongodb.net:27017,portfolio-shard-00-02-bw3cz.mongodb.net:27017/';
    const suffix = '?ssl=true&replicaSet=portfolio-shard-0&authSource=admin';
    return prefix + db + suffix;
};

module.exports = {
    'stats': createConnectionUri('stats'),
    'biodata': createConnectionUri('biodata')
};