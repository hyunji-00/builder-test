matrixMobile.define("matrix-mobile-plugin-network-information.network", function(require, exports, module) {
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/

var exec = require('matrixMobile/exec');
var matrixMobile = require('matrixMobile');
var channel = require('matrixMobile/channel');
var utils = require('matrixMobile/utils');

// Link the onLine property with the Cordova-supplied network info.
// This works because we clobber the navigator object with our own
// object in bootstrap.js.
// Browser platform do not need to define this property, because
// it is already supported by modern browsers
if (matrixMobile.platformId !== 'browser' && typeof navigator !== 'undefined') {
    utils.defineGetter(navigator, 'onLine', function () {
        return this.connection.type !== 'none';
    });
}

function NetworkConnection () {
    this.type = 'unknown';
}

/**
 * Get connection info
 *
 * @param {Function} successCallback The function to call when the Connection data is available
 * @param {Function} errorCallback The function to call when there is an error getting the Connection data. (OPTIONAL)
 */
NetworkConnection.prototype.getInfo = function (callback) {
    exec(callback, 'NetworkStatus', 'getConnectionInfo', []);
};

NetworkConnection.prototype.getCurrentWifi = function (callback) {
    exec(callback, 'NetworkStatus', 'getCurrentWifi', []);
};

var me = new NetworkConnection();
var timerId = null;
var timeout = 500;

channel.createSticky('onMatrixMobileConnectionReady');
channel.waitForInitialization('onMatrixMobileConnectionReady');

channel.onJSReady.subscribe(function () {
    me.getInfo(function (info) {
        me.type = info;
        if (info === 'none') {
            // set a timer if still offline at the end of timer send the offline event
            timerId = setTimeout(function () {
                matrixMobile.fireDocumentEvent('offline');
                timerId = null;
            }, timeout);
        } else {
            // If there is a current offline event pending clear it
            if (timerId !== null) {
                clearTimeout(timerId);
                timerId = null;
            }
            matrixMobile.fireDocumentEvent('online');
        }

        // should only fire this once
        if (channel.onMatrixMobileConnectionReady.state !== 2) {
            channel.onMatrixMobileConnectionReady.fire();
        }
    },
    function (e) {
        // If we can't get the network info we should still tell Cordova
        // to fire the deviceready event.
        if (channel.onMatrixMobileConnectionReady.state !== 2) {
            channel.onMatrixMobileConnectionReady.fire();
        }
        console.log('Error initializing Network Connection: ' + e);
    });

    me.getCurrentWifi = function(callback){
        exec(callback, 'NetworkStatus', 'getCurrentWifi');
    };
});

module.exports = me;

});
