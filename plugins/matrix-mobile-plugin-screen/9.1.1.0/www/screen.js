var screenExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

screenExport.startWebView = function (callback, data){
    exec(callback,"ScreenPlugin","startWebView",data);
}

screenExport.closeSubWebView = function (callback){
    var tag = $h.WebViewTAG
    var options = {"webViewTag": tag};
    exec(callback,"ScreenPlugin","closeSubWebView", options);
}

screenExport.screenCapture = function (callback, data){
    exec(callback,"ScreenPlugin","screenCapture", data);
}

screenExport.screenOrientation = function (callback, data){
    exec(callback,"ScreenPlugin","screenOrientation", data);
}

screenExport.setupBrightness = function (callback, data){
    exec(callback,"ScreenPlugin","setupBrightness",data);
}

module.exports = screenExport;