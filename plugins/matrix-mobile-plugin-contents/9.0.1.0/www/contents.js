var contentsExport = {};

var argscheck = require('matrixMobile/argscheck'),
    exec = require('matrixMobile/exec');

contentsExport.filePicker = {};
contentsExport.imagePicker = {};
contentsExport.fileDownload = {};

contentsExport.imagePicker = function (callback, data){
    if(data.camera){
        navigator.camera.getPicture(callback,data.cameraoption);
    }else{
        exec(callback,"ContentsPlugin","imagePick",data);
    }
}

contentsExport.filePicker = function (callback, data){
    exec(callback,"ContentsPlugin","filePick",data);
}

contentsExport.fileDownload = function (callback, data){
    exec(callback,"ContentsPlugin","fileDownload",data);
}

module.exports = contentsExport;