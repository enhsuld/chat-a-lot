### Chat-A-Lot

Chat-A-Lot is an application demonstrating Spring Security and Websocket capabilities using AngularJS on the front. 

Token-based authentication is used in this application. The token is obtained at login, and sent to the server in an http header with every request. Currently, browsers and the SockJS library don't support the setting of custom headers for websocket requests. To work around this, the token is sent as a url parameter in the ws handshake request.
