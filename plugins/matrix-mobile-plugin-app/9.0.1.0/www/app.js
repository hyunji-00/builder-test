var appExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

appExport.finish = {};
appExport.version = {};
appExport.checkAppInstalled = {};

appExport.finish = function (callback, data){
    exec(callback,"AppPlugin","appFinish",data);
}

appExport.version = function (callback){
    exec(callback,"AppPlugin","appVersion");
}

appExport.checkAppInstalled = function (callback, data){
    exec(callback,"AppPlugin","checkAppInstalled", data);
}

module.exports = appExport;