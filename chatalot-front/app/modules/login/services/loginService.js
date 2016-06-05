angular.module('login')
  .factory('loginService', ['$rootScope', 'constants', '$http', 'authProvider',
    function ($rootScope, constants, $http, authProvider) {
      var loginUrl = constants.authenticationUrl;
      var logoutUrl = constants.logoutUrl;

      return {
        doLogin: function (username, password) {
          var credentials = {"username": username, "password": password};
          $http.post(loginUrl, credentials).success(function (data, status, headers, config) {
            var authToken = headers("x-auth-token");
            if (authToken == null) {
              $rootScope.$broadcast('logInFailed', "Failed to retrieve authorization token.");
              return;
            }
            $http.defaults.headers.common["X-Auth-Token"] = authToken;
            authProvider.user = username;
            authProvider.authToken = authToken;
            authProvider.roles = headers("X-Auth-Roles").split(',');
            $rootScope.$broadcast('loggedIn');
          }).error(function (data, status) {
            $rootScope.$broadcast('logInFailed');
          });
        },
        doLogout: function () {
          $http.post(logoutUrl).success(function (data, status, headers, config) {
            authProvider.user = null;
            delete $http.defaults.headers.common["X-Auth-Token"];
            $rootScope.$broadcast('loggedOut', "");
          }).error(function (data, status) {
            $rootScope.$broadcast('logOutFailed');
          });
        }
      };
    }]);
