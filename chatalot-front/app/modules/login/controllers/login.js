'use strict';

angular
  .module('login')
  .controller('LoginController', ['$scope', '$rootScope', '$state', '$http', 'loginService', 'authProvider',
    function ($scope, $rootScope, $state, $http, loginService) {
      $scope.failedToLogin = false;
      $scope.loginError = "";

      $scope.login = function () {
        if($scope.loginForm.$invalid){
          return;
        }
        $scope.noOfTries++;
        loginService.doLogin($scope.username, $scope.password);
      }

      $scope.$on('loggedIn', function (event, data) {
        $state.go('chat.main');
      });

      $rootScope.$on('logInFailed', function (event, data) {
        $scope.failedToLogin = true;
        $scope.loginError = data;
      });

      $rootScope.$on('loggedOut', function (event, data) {
        $state.go('login');
      })

      $scope.goRegister = function () {
        $state.go('register');
      }

      $scope.reset = function () {
        $scope.username = '';
        $scope.password = '';
        $scope.loginForm.$setPristine();
      }

      $scope.initialize = function () {
        $scope.noOfTries = 0;
        $scope.failedToLogin = false;
      }

      $scope.initialize();

      $scope.logout = function () {
        loginService.doLogout();
      }

    }]);
