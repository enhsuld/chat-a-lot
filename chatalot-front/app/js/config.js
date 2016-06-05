'use strict';

// Init the application configuration module for AngularJS application
var ApplicationConfiguration = (function () {
  // Init module configuration options
  var applicationModuleName = 'chatalot';
  var applicationModuleVendorDependencies = [
    'ngResource',
    'ngCookies',
    'ngAnimate',
    'ngSanitize',
    'ngMaterial',
    'ui.router',
    'ui.bootstrap',
    'ui.utils',
    'ngFileUpload',
    'base64',
    'luegg.directives'
  ];

  var baseAddress = "http://localhost:8090";


  var applicationConstants = {
    authenticationUrl: baseAddress + "/rest/auth/token",
    logoutUrl: baseAddress + "/rest/logout",
    registrationUrl: baseAddress + "/rest/register",
    profileUrl: baseAddress + "/rest/profile",
    chatUrl: baseAddress + "/rest/socket"
  };

  // Add a new vertical module
  var registerModule = function (moduleName) {
    // Create angular module
    angular
      .module(moduleName, []);

    // Add the module to the AngularJS configuration file
    angular
      .module(applicationModuleName)
      .requires
      .push(moduleName);
  };

  return {
    applicationModuleName: applicationModuleName,
    applicationModuleVendorDependencies: applicationModuleVendorDependencies,
    applicationConstants: applicationConstants,
    registerModule: registerModule
  };
})();
