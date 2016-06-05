'use strict';

angular.module(ApplicationConfiguration.applicationModuleName)
  .controller('AppController', ['$scope', 'chatService',
    function ($scope, chatService) {
    $scope.showNavBar = false;

    $scope.$on('loggedIn', function (event, data) {
      $scope.showNavBar = true;
      chatService.initialize();
    });

    $scope.$on('loggedOut', function (event, data) {
      $scope.showNavBar = false;
      chatService.logout();
    });
  }]);

