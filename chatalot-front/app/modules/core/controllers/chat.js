'use strict';

angular
  .module('core')
  .controller('ChatController', ['$scope', 'chatService',
    function ($scope, chatService) {

      $scope.friends = chatService.getFriends();
      $scope.activeSessions = chatService.getActiveSessions();
      $scope.activeSession = null;
      $scope.message = "";

      //update active sessions to reflect new friends' status
      $scope.$watch(function () {
        return chatService.getFriends();
      }, function (newFriendsArray) {
        $scope.friends = newFriendsArray;
      });

      $scope.$on("chatServiceMessageReceived", function (event, message) {
        var fromUsername = message.from;

        //If this session is showing mark its hasNewMessages as false since the new
        // messages will immediately be available.
        if ($scope.activeSessions[fromUsername] == $scope.activeSession) {
          $scope.activeSessions[fromUsername].hasNewMessages = false;
        }

        $scope.$apply();
      });

      /**
       * start a new chat session.
       * @param friendUsername
       */
      var startChatSession = function (friendUsername) {
        return chatService.startChatSession(friendUsername);
      };

      /**
       * Show a user's chat session.
       * @param username
       */
      $scope.showSession = function (username) {
        $scope.activeSession =
          $scope.activeSessions.hasOwnProperty(username)
            ? $scope.activeSessions[username] : startChatSession(username);
        $scope.activeSession.hasNewMessages = false;
      };

      /**
       * Send the active session's message.
       */
      $scope.sendMessage = function () {
        if ($scope.activeSession == null) {
          console.error("Trying to send a message but no active session is available.");
        }

        if ($scope.activeSession.message == null || $scope.activeSession.message.length <= 0) {
          return;
        }

        $scope.activeSession.messages.push(
          chatService.sendMessage($scope.activeSession.message, $scope.activeSession.user.username)
        );
        $scope.activeSession.message = "";
      };

      $scope.hasNewMessages = function (username) {
        if ($scope.activeSessions.hasOwnProperty(username)) {
          return $scope.activeSessions[username].hasNewMessages;
        }
      }
    }]);
