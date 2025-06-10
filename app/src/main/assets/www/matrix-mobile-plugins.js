matrixMobile.define('matrixMobile/plugin_list', function(require, exports, module) {
module.exports =
// TOP OF MODULE
[
  {
    "id": "matrix-mobile-plugin-camera.Camera",
    "file": "plugins/matrix-mobile-plugin-camera/www/CameraConstants.js",
    "pluginId": "matrix-mobile-plugin-camera",
    "clobbers": [
      "Camera"
    ]
  },
  {
    "id": "matrix-mobile-plugin-camera.CameraPopoverOptions",
    "file": "plugins/matrix-mobile-plugin-camera/www/CameraPopoverOptions.js",
    "pluginId": "matrix-mobile-plugin-camera",
    "clobbers": [
      "CameraPopoverOptions"
    ]
  },
  {
    "id": "matrix-mobile-plugin-camera.camera",
    "file": "plugins/matrix-mobile-plugin-camera/www/Camera.js",
    "pluginId": "matrix-mobile-plugin-camera",
    "clobbers": [
      "navigator.camera"
    ]
  },
  {
    "id": "matrix-mobile-plugin-camera.CameraPopoverHandle",
    "file": "plugins/matrix-mobile-plugin-camera/www/CameraPopoverHandle.js",
    "pluginId": "matrix-mobile-plugin-camera",
    "clobbers": [
      "CameraPopoverHandle"
    ]
  },
  {
    "id": "matrix-mobile-plugin-network-information.network",
    "file": "plugins/matrix-mobile-plugin-network-information/www/network.js",
    "pluginId": "matrix-mobile-plugin-network-information",
    "clobbers": [
      "navigator.connection",
      "navigator.network.connection"
    ]
  },
  {
    "id": "matrix-mobile-plugin-network-information.Connection",
    "file": "plugins/matrix-mobile-plugin-network-information/www/Connection.js",
    "pluginId": "matrix-mobile-plugin-network-information",
    "clobbers": [
      "Connection"
    ]
  },
  {
    "clobbers": [
      "device"
    ],
    "file": "plugins/matrix-mobile-plugin-device/www/device.js",
    "pluginId": "matrix-mobile-plugin-device",
    "id": "matrix-mobile-plugin-device.device"
  },
  {
    "clobbers": [
      "matrixMobile.plugin.app"
    ],
    "file": "plugins/matrix-mobile-plugin-app/www/app.js",
    "pluginId": "matrix-mobile-plugin-app",
    "id": "matrix-mobile-plugin-app.app"
  }
];
// BOTTOM OF MODULE
module.exports.metadata =
// TOP OF METADATA
{
  "matrix-mobile-plugin-camera": "9.1.1.0",
  "matrix-mobile-plugin-network-information": "9.1.1.0",
  "matrix-mobile-plugin-device": "9.1.1.0",
  "matrix-mobile-plugin-app": "9.1.1.0"
};
// BOTTOM OF METADATA
});