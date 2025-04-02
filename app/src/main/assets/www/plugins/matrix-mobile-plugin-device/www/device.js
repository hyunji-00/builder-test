matrixMobile.define("matrix-mobile-plugin-device.device", function(require, exports, module) {
var argscheck = require('matrixMobile/argscheck');
var channel = require('matrixMobile/channel');
var utils = require('matrixMobile/utils');
var exec = require('matrixMobile/exec');
var matrixMobile = require('matrixMobile');

channel.createSticky('onMatrixMobileInfoReady');
// Tell matrixMobile channel to wait on the matrixMobileInfoReady event
channel.waitForInitialization('onMatrixMobileInfoReady');

/**
 * This represents the mobile device, and provides properties for inspecting the model, version, UUID of the
 * phone, etc.
 * @constructor
 */
function Device () {
    this.available = false;
    this.appID = null;
    this.platform = null;
    this.version = null;
    this.uuid = null;
    this.matrixMobile = null;
    this.model = null;
    this.manufacturer = null;
    this.isVirtual = null;
    this.serial = null;

    var me = this;

    channel.onJSReady.subscribe(function () {
        me.getInfo(function (info) {
            // ignoring info.matrixMobile returning from native, we should use value from matrixMobile.version defined in matrix-mobile.js
            // TODO: CB-5105 native implementations should not return info.matrixMobile
            var buildLabel = info.data.matrixMobileVersion;
            me.available = true;
            me.appID = info.data.appID;
            me.platform = info.data.platform;
            me.version = info.data.version;
            me.uuid = info.data.uuid;
            me.matrixMobile = buildLabel;
            me.model = info.data.model;
            me.isVirtual = info.data.isVirtual;
            me.manufacturer = info.data.manufacturer || 'unknown';
            me.serial = info.data.serial || 'unknown';
            me.type = info.data.type || 'unknown';
            channel.onMatrixMobileInfoReady.fire();
        }, function (e) {
            me.available = false;
            utils.alert('[ERROR] Error initializing matrixMobile: ' + e);
        });
    });

    me.getBatteryLevel = function(callback){
        exec(callback, "Device", "getBatteryLevel");
    }

    me.isCharging = function(callback){
        exec(callback, "Device", "isCharging");
    }

    me.registerBattery = function(callback){
        exec(callback, "Device", "registerBattery");
    }

    me.unregisterBattery = function(callback){
        exec(callback, "Device", "unregisterBattery");
    }
}

/**
 * Get device info
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Device.prototype.getInfo = function (Callback) {
    argscheck.checkArgs('fF', 'Device.getInfo', arguments);
    exec(Callback, 'Device', 'getDeviceInfo', []);
};

module.exports = new Device();
});