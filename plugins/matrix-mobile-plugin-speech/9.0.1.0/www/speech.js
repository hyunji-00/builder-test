var speechExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

speechExport.speakTTS = {};
speechExport.stopTTS = {};
speechExport.startSpeechToText = {};
speechExport.stopSpeechToText = {};

speechExport.speakTTS = function (callback, data){
    exec(callback,"SpeechPlugin","speakTTS",data);
}

speechExport.stopTTS = function (callback){
    exec(callback, "SpeechPlugin","stopTTS")
}

speechExport.startSpeechToText = function (callback, data){
    exec(callback,"SpeechPlugin","startSpeechToText", data);
}

speechExport.stopSpeechToText = function (callback){
    exec(callback,"SpeechPlugin","stopSpeechToText");
}

module.exports = speechExport;