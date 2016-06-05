'use strict';

angular
  .module('core')
  .controller('ProfileController', ['$scope', 'profileService',
    function ($scope, profileService) {
      $scope.profile = {};
      $scope.editingProfile = false;

      profileService.getProfile().then(function (profile) {
        $scope.profile = profile;
      }, function (error) {
        console.error(error);
      });

      var syncProfile = function () {
        $scope.invitableUsers = profileService.getInvitableUsers();
      };

      syncProfile();

      $scope.uploadProfilePicture = function (pictureFile) {
        $scope.profile = profileService.uploadPicture(pictureFile).then(function (profile) {
          $scope.profile = profile;
        }, function (error) {
          console.error(error);
        });
        $scope.$apply();
      };

      $scope.isPictureAvailable = function () {
        return $scope.profile.profilePicture != null;
      };

      $scope.$on("profileOutOfSync", function (event, message) {
        syncProfile();
      });

      $scope.editProfile = function () {
        $scope.editingProfile = true;
      };

      $scope.saveProfile = function () {
        profileService.updateProfile($scope.profile).then(function (profile) {
          $scope.profile = profile;
          $scope.editingProfile = false;
        }, function (error) {
          console.error(error);
        });
      };

      $scope.invite = function (userId) {
        profileService.invite(userId).then(function (profile) {
          $scope.profile = profile;
        }, function (error) {
          console.error(error);
        });
      };

      $scope.accept = function (inviteId) {
        profileService.accept(inviteId).then(function (profile) {
          $scope.profile = profile;
        }, function (error) {
          console.error(error);
        });
      };
    }]);
