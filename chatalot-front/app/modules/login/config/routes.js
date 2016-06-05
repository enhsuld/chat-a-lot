'use strict';

angular
  .module('login')
  .config(['$stateProvider', function ($stateProvider) {
    $stateProvider
      .state('login', {
        url: '/login',
        templateUrl: 'modules/login/views/login.html',
        controller: 'LoginController'
      });
    $stateProvider
      .state('registrationSuccess', {
        url: '/registrationSuccess',
        templateUrl: 'modules/login/views/registrationSuccess.html',
        controller: 'RegisterController'
      });
    $stateProvider
      .state('register', {
        url: '/register',
        templateUrl: 'modules/login/views/register.html',
        controller: 'RegisterController'
      });
  }]);
