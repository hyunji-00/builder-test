var viewExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

viewExport.toast = {};
viewExport.snackBar = {};
viewExport.notification = {};

viewExport.toast = function (callback, data){
    exec(callback, "ViewPlugin", "toast", data);
}

viewExport.snackBar = function (callback, data){
    exec(callback, "ViewPlugin", "snackBar", data);
}

viewExport.notification = function (callback, data){
    exec(callback, "ViewPlugin", "notification", data);
}

module.exports = viewExport;