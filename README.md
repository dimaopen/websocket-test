websocket-test: A simple WebSocket application
========================
Author: Dmitry Openkov

What is it?
-----------

This is a test task made during job appliance. **It's not done yet.**
The `websocket-test` demonstrates authentication of users via WebSocket.

Main use case: **user authentication**

1. Client app sends a websocket request LOGIN_CUSTOMER.
2. App validates the user name/password against the user DB.
3.
    1. If successful app returns token message with expiration date.
    2. If not app returns error message.

Other features:

1. user token DB
2. History of user tokens.
3. Discard user token in case of subsequent authentication.

Now only a single user is created during app startup for testing purposes. His credentials are fpi@bk.ru/123123.

System requirements
-------------------

The application this project produces is designed to be run on Red Hat JBoss Enterprise Application Platform 7 or later. 

All you need to build this project is Java 8.0 (Java SDK 1.8) or later and Maven 3.1.1 or later.


Use of EAP7_HOME
---------------

In the following instructions, replace `EAP7_HOME` with the actual path to your JBoss EAP installation. The installation path is described in detail here: [Use of EAP7_HOME and JBOSS_HOME Variables](https://github.com/jboss-developer/jboss-developer-shared-resources/blob/master/guides/USE_OF_EAP7_HOME.md#use-of-eap_home-and-jboss_home-variables).



Start the JBoss EAP Server
-------------------------

1. Open a command prompt and navigate to the root of the JBoss EAP directory.
2. The following shows the command line to start the server:

        For Linux:   EAP7_HOME/bin/standalone.sh
        For Windows: EAP7_HOME\bin\standalone.bat

 
Build and Deploy the Quickstart
-------------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type this command to build and deploy the archive:

        mvn clean install wildfly:deploy

4. This will deploy `target/jboss-websocket-hello.war` to the running instance of the server.




Access the application 
---------------------

The application will be running at the following URL: <http://localhost:8080/jboss-websocket-hello>. 

1. Click on the `Open Connection` button to create the WebSocket connection and display current status of `Open`.
2. Type a name and click `Say Hello` to create and send the 'Say hello to `<NAME>`' message. The message appears in the server log and a response is sent to the client.
3. Click on the `Close Connection` button to close the WebSocket connection and display the current status of `Closed`.
4. If you attempt to send another message after closing the connection, the following message appears on the page: "WebSocket connection is not established. Please click the Open Connection button".


Undeploy the Archive
--------------------

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. When you are finished testing, type this command to undeploy the archive:

        mvn wildfly:undeploy


Run the Arquillian Tests
-------------------------

This application provides Arquillian tests. By default, these tests are configured to be skipped as Arquillian tests require the use of a container.

1. Make sure you have started the JBoss EAP server as described above.
2. Open a command prompt and navigate to the root directory of this quickstart.
3. Type the following command to run the test goal with the following profile activated:

        mvn clean test -Parq-wildfly-remote


  
