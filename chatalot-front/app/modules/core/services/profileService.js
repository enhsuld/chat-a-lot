angular.module('core')
  .factory('profileService', ['constants', '$http', '$rootScope', '$timeout', 'Upload', '$q',
    function (constants, $http, $rootScope, $timeout, Upload, $q) {

      var PROFILE_SERVICE_URL = constants.profileUrl;
      var service = {};

      service.getProfile = function () {
        var deferred = $q.defer();

        $http.get(PROFILE_SERVICE_URL).success(function (profile) {
          deferred.resolve(JSOG.decode(profile));
        }).error(function (response) {
          deferred.reject("Failed to retrieve user profile: " + response);
        });

        return deferred.promise;
      };

      service.getInvitableUsers = function () {
        var deferred = $q.defer();

        var invitableUsersUrl = "/invitable";
        $http.get(PROFILE_SERVICE_URL + invitableUsersUrl).success(function (response) {
          deferred.resolve(response);
        }).error(function (response) {
          deferred.reject("Failed to retrieve user profile: " + response);
        });
        return deferred.promise;
      };

      service.invite = function (userId) {
        var deferred = $q.defer();

        var inviteUserUrl = "/invite";
        $http.post(PROFILE_SERVICE_URL + inviteUserUrl + "/" + userId).success(function (profile) {
          deferred.resolve(JSOG.decode(profile));
          $rootScope.$broadcast('profileOutOfSync');
        }).error(function (response) {
          deferred.reject("Failed to invite user: " + response);
        });

        return deferred.promise;
      };

      service.accept = function (inviteId) {
        var deferred = $q.defer();

        var inviteUserUrl = "/accept";
        $http.post(PROFILE_SERVICE_URL + inviteUserUrl + "/" + inviteId).success(function (profile) {
          deferred.resolve(JSOG.decode(profile));
          $rootScope.$broadcast('profileOutOfSync');
        }).error(function (response) {
          deferred.reject("Failed to accept invitation: " + response);
        });

        return deferred.promise;
      };

      service.updateProfile = function (profile) {
        var deferred = $q.defer();
        var decirclizedProfile = JSOG.encode(profile);
        $http.post(PROFILE_SERVICE_URL, decirclizedProfile).success(function (profile) {
          deferred.resolve(JSOG.decode(profile))
        }).error(function () {
          deferred.reject("Failed to update profile.");
        });
        return deferred.promise;
      };

      service.uploadPicture = function (pictureFile) {
        var deferred = $q.defer();

        var uploadPicturePath = '/uploadPicture';
        if (pictureFile) {
          pictureFile.upload = Upload.upload({
            url: PROFILE_SERVICE_URL + uploadPicturePath,
            data: {file: pictureFile}
          });

          pictureFile.upload.then(function (response) {
            $timeout(function () {
              deferred.resolve(JSOG.decode(response.data));
            });
          }, function (response) {
            deferred.reject("Failed to upload profile picture: " + response);
          }, function (evt) {
            pictureFile.progress = Math.min(100, parseInt(100.0 *
              evt.loaded / evt.total));
          });
        }

        return deferred.promise;
      };

      var getFriendsInternal = function () {
        var friendsServicePath = "/friends";
        return $http.get(PROFILE_SERVICE_URL + friendsServicePath);
      };


      service.getFriendsAsync = function () {
        var deferred = $q.defer();
        var friends = [];
        getFriendsInternal().success(function (response) {
          for (var index in response) {
            if (response.hasOwnProperty(index)) {
              friends.push(response[index]);
            }
          }
          deferred.resolve(friends);
        }).error(function () {
          deferred.reject("Failed to retrieve user friends.");
        });

        return deferred.promise;
      };

      service.getFriends = function () {
        var friends = [];
        this.getFriendsAsync().then(function (response) {
            for (var index in response) {
              if (response.hasOwnProperty(index)) {
                friends.push(response[index]);
              }
            }
          }, function (error) {
            $rootScope.$broadcast('chatServiceError', "Failed to retrieve user friends. [" + error + "]");
          }
        );
        return friends;
      };

      return service;
    }]);
