var logExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

logExport.getLog = {};
logExport.getLogUpload = {};
logExport.getLogMail = {};
logExport.clearLog = {};
logExport.logPath = {};

logExport.getLog = function (callback, data){
    exec(callback,"LogPlugin","getLog", data);
}

logExport.getLogUpload = function (callback, data){
    exec(callback,"LogPlugin","getLogUpload", data);
}

logExport.getLogMail = function (callback, data){
    exec(callback,"LogPlugin","getLogMail", data);
}

logExport.clearLog = function (callback){
    exec(callback,"LogPlugin","clearLog");
}

logExport.logPath = function (callback, data){
    exec(callback,"LogPlugin","logPath",data);
}

module.exports = logExport;