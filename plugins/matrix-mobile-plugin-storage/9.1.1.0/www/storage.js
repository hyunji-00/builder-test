var storageExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');


storageExport.reset = function (callback, data){
    exec(callback,"StoragePlugin","appReset",data);
}

storageExport.setPreference = function (callback, data){
    exec(callback,"StoragePlugin","setPreference", data);
}

storageExport.getPreference = function (callback, data){
    exec(callback,"StoragePlugin","getPreference", data);
}

storageExport.removePreference = function (callback, data){
    exec(callback,"StoragePlugin","removePreference", data);
}

storageExport.shareData = function (callback, data){
    exec(callback,"StoragePlugin","shareData", data);
}

module.exports = storageExport;