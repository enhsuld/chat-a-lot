angular.module('core')
  .factory('chatService', ['constants', '$http', '$rootScope', 'authProvider', 'profileService', '$q',
    function (constants, $http, $rootScope, authProvider, profileService, $q) {

      var service = {};
      var activeSessions = {};
      var friends = null;

      var listener = $q.defer();
      var chatSocket = {
        client: null,
        stomp: null
      };

      var CHAT_SERVICE_URL = constants.chatUrl;
      var RECONNECT_TIMEOUT = 30000;
      var RECEIVE_TOPIC_PREFIX = "/topic/users/";
      var SEND_TOPIC = "/chatalot/send";

      var initialized = false;

      service.initialize = function () {
        if (!initialized) {
          chatSocket.topic = RECEIVE_TOPIC_PREFIX + authProvider.user;
          chatSocket.client = new SockJS(CHAT_SERVICE_URL+"?auth="+authProvider.authToken);
          chatSocket.stomp = Stomp.over(chatSocket.client);
          chatSocket.stomp.connect({}, startMessageListener);
          chatSocket.stomp.onclose = reconnect;
          activeSessions = {};
          friends = profileService.getFriends();
          initialized = true;
        }
      };

      service.getActiveSessions = function () {
        return activeSessions;
      };

      service.getFriends = function () {
        return friends;
      };

      /**
       * start a new chat session.
       * @param username of user to start chat with
       */
      service.startChatSession = function (username) {
        return startChatSession(username);
      };

      service.logout = function () {
        if (chatSocket.client != null) {
          chatSocket.stomp.unsubscribe();
          chatSocket.client.close();
          chatSocket = {};
          friends = null;
          activeSessions = {};
          initialized = false;
        }
      };

      service.sendMessage = function (message, to) {
        var messageObj = {
          message: message,
          date: new Date(),
          to: to
        };

        chatSocket.stomp.send(SEND_TOPIC, {
          priority: 9
        }, JSON.stringify(messageObj));
        return messageObj;
      };

      var startChatSession = function (username) {
        return activeSessions[username] = {
          user: findFriendProfile(username),
          messages: [],
          message: "",
          hasNewMessages: false
        };
      };

      var reconnect = function () {
        $timeout(function () {
          initialize();
        }, RECONNECT_TIMEOUT);
      };

      var getMessage = function (data) {
        var message = JSON.parse(data);
        console.log(message);

        if (message.type === "control-message") {
          processControl(message)
        } else if (message.type === "chat-message") {
          processChat(message);
        }
      };

      var processControl = function (message) {
        if (message.message === "refresh-friends") {
          profileService.getFriendsAsync().then(function (newFriends) {
            friends = newFriends;
            for (var index in activeSessions) {
              if (activeSessions.hasOwnProperty(index)) {
                activeSessions[index].user = findFriendProfile(activeSessions[index].user.username);
              }
            }
          }, function (error) {
            console.error("Failed to process control message: " + message.message);
            console.error(error);
          });
        }
      };

      /**
       * Find a friend from the friends list which is populated when the controller
       * is initiated.
       * @param username
       * @returns {*}
       */
      var findFriendProfile = function (username) {
        for (var index in friends) {
          if (friends.hasOwnProperty(index)) {
            if (friends[index].username == username) {
              return friends[index];
            }
          }
        }
      };

      var processChat = function (message) {
        var fromUsername = message.from;

        //When a message arrives and there is no corresponding session, start one.
        if (!activeSessions.hasOwnProperty(fromUsername)) {
          if (startChatSession(fromUsername) == null) {
            console.log("Error starting a chat session");
            return;
          }
        }

        activeSessions[fromUsername].messages.push(message);
        activeSessions[fromUsername].hasNewMessages = true;

        $rootScope.$broadcast("chatServiceMessageReceived", message);
      };

      var startMessageListener = function () {
        chatSocket.subscription = chatSocket.stomp.subscribe(chatSocket.topic, function (data) {
          listener.notify(getMessage(data.body));
        });
      };

      return service;
    }
  ]);
