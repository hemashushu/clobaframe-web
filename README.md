clobaframe-web
==============

A web application component that base on Clobaframe, includes
site theme, site page and the view tools.

Run the Demo
------------

1. Check out the Clobaframe source then compile and complete the unit tests.

    $ git clone https://github.com/ivarptr/clobaframe.git
    $ cd clobaframe/source
    $ mvn clean test

2. Install Clobaframe library into local Apache Maven repository.

    $ mvn install

3. Check out Clobaframe-web source code into any folder that outside the Clobaframe source folder.

    $ git clone https://github.com/ivarptr/clobaframe-web.git

4. Run with the embed Tomcat/Jetty web server:

    $ cd clobaframe-web/source
    $ mvn clean tomcat7:run

Or perfer IPv4:

    $ mvn -Djava.net.preferIPv4Stack=true clean tomcat7:run

Then open http://localhost:8080/ in the web browser.


Install library
-------------------

Install Clobaframe-web into Apache Maven local repository:

    $ mvn clean javadoc:jar source:jar install

