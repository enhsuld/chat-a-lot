/**
 * Created by mustafa on 01/05/2016.
 */

angular.module('login')
  .factory('registerService', ['constants', '$http', function(constants, $http) {
    var registerUrl = constants.registrationUrl;
    return {
      doRegister : function(userDetails){
        return $http.post(registerUrl, userDetails);
      }
    };
  }]);
