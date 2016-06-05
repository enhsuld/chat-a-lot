'use strict';

angular
  .module('core')
  .config(['$stateProvider', '$urlRouterProvider', function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise(function ($injector, $location) {
      $location.path('/chat');
    });

    $stateProvider
      .state('profile', {
      abstract: true,
      url: '',
      template: '<ui-view/>',
      controller: 'ProfileController'
    }).state('profile.main', {
      url: '/profile',
      templateUrl: 'modules/core/views/profile/main.html'
    }).state('chat', {
      abstract: true,
      url: '',
      template: '<ui-view/>',
      controller: 'ChatController'
    }).state('chat.main', {
      url: '/chat',
      templateUrl: 'modules/core/views/chat/main.html'
    });
  }])
  .run(['$rootScope', '$state', 'authProvider', function ($rootScope, $state, authProvider) {
    $rootScope.$on('$stateChangeStart', function (event, toState/*, toParams, fromState, fromParams*/) {
      if (toState.url !== "/login"
        && toState.url !== "/register"
        && !authProvider.isLoggedIn()) {
        console.log('DENY access to ' + toState.url + ': Redirecting to Login');
        event.preventDefault();
        $state.go('login');
      }
    });

    $rootScope.$on('$stateNotFound',
      function (event, unfoundState, fromState, fromParams) {
        console.log("$stateNotFound to: " + unfoundState.to);
        console.log("$stateNotFound toParams:" + unfoundState.toParams);
        console.log("$stateNotFound fromState: " + fromState);
      })
  }]);
