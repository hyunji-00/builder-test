var exec = matrixMobile.require('matrixMobile/exec'); // eslint-disable-line no-undef
var utils = require('matrixMobile/utils');

// https, file protocol is called from navigator.geolocation. (https://developer.mozilla.org/en-US/docs/Web/API/Geolocation)
// Native watchPosition method is called async after permissions prompt.
// So we use additional map and own ids to return watch id synchronously.
var pluginToNativeWatchMap = {};

module.exports = {
    getCurrentPosition: function (callback, args) {
        var afterGetPermission = function () {
            if (document.location.protocol == 'https:' || document.location.protocol == 'file:') {
                var geo = matrixMobile.require('matrixMobile/modulemapper').getOriginalSymbol(window, 'navigator.geolocation'); // eslint-disable-line no-undef

                try {
                    geo.getCurrentPosition(callback, args);
                } catch(e) {
                    geo.getCurrentPosition(callback, (err)=>{console.error(`ERROR(${err.code}): ${err.message}`);}, args);
                }

            } else {
                var locationCallback = function(position){
                    var positionObj = {};
                    positionObj.coords = {};

                    positionObj.coords.accuracy = position.data.coords.accuracy;
                    positionObj.coords.altitudeAccuracy = null;
                    positionObj.coords.heading = null;
                    positionObj.coords.latitude = position.data.coords.latitude;
                    positionObj.coords.longitude = position.data.coords.longitude;
                    positionObj.coords.speed = position.data.coords.speed;
                    positionObj.timestamp = position.data.timestamp;
                    callback(positionObj);
                };
                exec(locationCallback, 'Geolocation', 'getCurrentPosition', []);
            }
        };
        exec(afterGetPermission, 'Geolocation', 'getPermission', []);
    },

    watchPosition: function (callback, args) {
        var pluginWatchId = utils.createUUID();

        var afterGetPermission = function () {
            if (document.location.protocol == 'https:' || document.location.protocol == 'file:') {
                var geo = matrixMobile.require('matrixMobile/modulemapper').getOriginalSymbol(window, 'navigator.geolocation'); // eslint-disable-line no-undef

                try {
                    pluginToNativeWatchMap[pluginWatchId] = geo.watchPosition(callback, args);
                } catch(e) {
                    pluginToNativeWatchMap[pluginWatchId] = geo.watchPosition(callback, (err)=>{console.error(`ERROR(${err.code}): ${err.message}`);}, args);
                }

            } else {
                args.watcherID = pluginWatchId;

                exec(callback, 'Geolocation', 'watchPosition', args)
            }
        };
        exec(afterGetPermission, 'Geolocation', 'getPermission', []);

        return pluginWatchId;
    },

    clearWatch: function (pluginWatchId) {
        var afterGetPermission = function () {
            if (document.location.protocol == 'https:' || document.location.protocol == 'file:') {
                var nativeWatchId = pluginToNativeWatchMap[pluginWatchId];
                var geo = matrixMobile.require('matrixMobile/modulemapper').getOriginalSymbol(window, 'navigator.geolocation'); // eslint-disable-line no-undef
                geo.clearWatch(nativeWatchId);
            } else {
                var args = {};
                args.watcherID = pluginWatchId;
                exec(null, 'Geolocation', 'clearWatch', args)
            }
        };

        exec(afterGetPermission, 'Geolocation', 'getPermission', []);
    }
};