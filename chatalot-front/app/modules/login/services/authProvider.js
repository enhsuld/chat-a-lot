angular.module('login')
  .factory('authProvider', ['$rootScope', function($rootScope) {
    var userName;
    var userRoles;
    var authToken;

    return {
      set User(aUser){
        userName = aUser;
      },
      get User() {
        return userName;
      },
      set AuthToken(token){
        authToken = token;
      },
      get AuthToken() {
        return authToken;
      },
      set Roles(roles){
        userRoles = roles;
      },
      get Roles(){
        return userRoles
      },
      hasRole : function(role){
        return(this.isLoggedIn() && this.roles.indexOf(role) > -1);
      },
      isLoggedIn : function(){
        return(this.user != null);
      }
    };
  }]);
