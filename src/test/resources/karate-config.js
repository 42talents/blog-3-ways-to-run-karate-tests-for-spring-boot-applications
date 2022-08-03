function fn() {
    var env = karate.env; // get java system property 'karate.env'
    karate.log('karate.env system property was:', env);
    if (!env) {
        env = 'dev'; // a custom 'intelligent' default
    }
    var config = '';
    var port = karate.properties['karate.port'] || '8080'
    if (env === 'dev') {
        config = {
            baseUrl: 'http://localhost:' + port
        }
    }
    // don't waste time waiting for a connection or if servers don't respond within 5 seconds
    karate.configure('connectTimeout', 300);
    karate.configure('readTimeout', 300);
    return config;
}