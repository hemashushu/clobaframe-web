clobaframe-web
==============

A web application template that using clobaframe.

Run the Demo
------------

1. Complete all clobaframe unit tests.

2. Install clobaframe library into local Apache Maven repository.

3. Check out source code into any folder

4. Run with the embed Jetty web server:

    $ mvn clean jetty:run

Or perfer IPv4:

    $ mvn -Djava.net.preferIPv4Stack=true clean jetty:run

Then open http://localhost:8080/ in the web browser.


Install library
-------------------

Install clobaframe-web into Apache Maven local repository:

    $ mvn clean javadoc:jar source:jar install

