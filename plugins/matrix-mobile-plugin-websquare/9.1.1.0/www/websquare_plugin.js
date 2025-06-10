var websquareExport = {};

var exec = require('matrixMobile/exec');

websquareExport.excelDownload = function (success, fail, option){

    var callback = function(result){
        if (result.statusCode == 1000) {
            success(result);
        } else {
            fail(result);
        }
    }

    exec(callback, "WebSquarePlugin", "excelDownload", option);
}

module.exports = websquareExport;