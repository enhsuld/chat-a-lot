/**
 * Created by mustafa on 01/05/2016.
 */
'use strict';

/**
 * @ngdoc object
 * @name core.Controllers.RegisterController
 * @description Register controller
 * @requires ng.$scope
 */
angular
  .module('login')
  .controller('RegisterController', ['$scope', '$location', 'registerService',
    function ($scope, $location, registerService) {
      $scope.userDetails = {};

      $scope.registerUser = function () {
        if(!$scope.registerForm.$valid){
          return;
        }

        registerService.doRegister($scope.userDetails).then(function(){
          $location.path('/registrationSuccess');
        }, function (error) {
          $scope.failedToRegister = true;
          $scope.globalErrors = error.data.globalErrors;
        });
      }

      $scope.goLogin = function () {
        $location.path('/login');
      }

      $scope.reset = function () {
        $scope.userDetails = {};
        $scope.confirmPassword = '';
        $scope.globalErrors = null;
        $scope.registerForm.$setPristine();
      }

      $scope.initialize = function () {
        $scope.failedToRegister = false;
        $scope.userDetails.vendor = false;
      }

      $scope.initialize();
    }]);
