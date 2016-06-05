### Chat-A-Lot Backend

This is a Spring Boot application. It exposes its functionality through a REST api. It allows clients to register, manage user profiles, invite friends and chat between friends. The chat feature uses the Spring Websocket and STOMP messaging capabilities.

Note: Redis is used to store user details, so a redis server listening on the standard port is requied to start this application.

- To build all the project modules run: `mvn clean package` in chatalot-parent.
- To build individual projects run: `mvn clean package` in there respective folders.
  or make sure dependent projects have been build into your local repo.
- To skip unit tests run with `-DskipTests`.
- To deploy the web app build chatalot-web and copy `chatalot-web-0.0.1-SNAPSHOT.war` to your application server or use its deploy command.
- To print out project dependency tree do: `mvn dependency:tree` in chatalot-parent.
- To run maven in offline mode use the `-o` option.
- To run the web app in an embedded tomcat application server run: `mvn clean spring-boot:run` in chatalot-web.
- To clean local cache:
    - Inside *chatalot-parent* `mvn dependency:purge-local-repository`
    - or `rm -rf ~/.m2/repository`. Plase note that this will purge all of the local cache including other project's.
