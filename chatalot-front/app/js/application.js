'use strict';

var app = angular
  .module(ApplicationConfiguration.applicationModuleName, ApplicationConfiguration.applicationModuleVendorDependencies);

app.constant('constants', ApplicationConfiguration.applicationConstants);

app.filter('fewWords', function () {
  return function (input, numberOfWords) {
    if (input == null || typeof input != 'string' || input.length <= 0) {
      return '';
    }

    return input.split(/\s+/).slice(0, numberOfWords).join(" ");
  };
});

angular
  .module(ApplicationConfiguration.applicationModuleName)
  .config(['$locationProvider',
    function ($locationProvider) {
      $locationProvider.html5Mode(true).hashPrefix('!');
    }
  ]);

//Then define the init function for starting up the application
angular
  .element(document)
  .ready(function () {
    /*    if (window.location.hash === '#_=_') {
     window.location.hash = '#!';
     }*/
    angular
      .bootstrap(document,
        [ApplicationConfiguration.applicationModuleName]);
  });
