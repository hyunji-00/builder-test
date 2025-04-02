var miscExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

miscExport.openBrowser = function (callback, data){
    exec(callback,"MiscPlugin","openBrowser",data);
}

miscExport.settingStatus = function(callback, data){
    exec(callback,"MiscPlugin","settingStatus",data);
}

miscExport.startTimer = function(callback, data){
    exec(callback,"MiscPlugin","startTimer",data);
}

miscExport.stopTimer = function(callback, data){
    exec(callback,"MiscPlugin","stopTimer",data);
}

miscExport.stopTimerAll = function(callback){
    exec(callback,"MiscPlugin","stopTimerAll");
}

miscExport.sendCall = function(callback, data){
    exec(callback,"MiscPlugin","sendCall",data);
}

miscExport.sendSMS = function(callback, data){
    exec(callback,"MiscPlugin","sendSMS",data);
}

miscExport.getContacts = function(callback){
    exec(callback,"MiscPlugin","getContacts");
}

module.exports = miscExport;