'use strict';

angular.module(ApplicationConfiguration.applicationModuleName)
  .controller('NavBarController', ['$scope', '$state', 'loginService', 'authProvider',
    function ($scope, $state, loginService, authProvider) {
      $scope.username = "";
      $scope.showNavBar = false;
      $scope.isCollapsed = true;
      $scope.menu = {};

      $scope.menu.right = {};

      $scope.menu.left = {
        "profile.main": "Profile",
        "chat.main": "Chat"
      };

      $scope.$on('loggedIn', function (event, data) {
        $scope.username = authProvider.user;
      });

      $scope.$on('rolesChanged', function (event, data) {
      });

      $scope.$on('loggedOut', function (event, data) {
      });

      $scope.logOut = function () {
        loginService.doLogout();
      };

      $scope.isSubMenu = function (item) {
        return typeof item === 'object';
      };

      $scope.isMainMenuItem = function (item) {
        return typeof item === 'string';
      };

      $scope.dropDownState = {};
      $scope.toggleDropDown = function (index) {
        $scope.dropDownState[index] = !$scope.dropDownState[index];

        for (var other in $scope.dropDownState) {
          if (index != other) {
            $scope.dropDownState[other] = false
          }
        }
      };
    }
  ]);

