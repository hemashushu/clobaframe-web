clobaframe-web
=============

The min web application template that using clobaframe.
Includes a demo page.

Compile and install
-------------------
 * Install Apache Maven
 * Compile and install clobaframe.
 * Run this command to package (generate a jar lib file and javadoc and source package ): 

    $ mvn clean javadoc:jar source:jar package

 * Run this command to install into local Maven repository

    $ mvn clean javadoc:jar source:jar install

Run the Demo
------------
 * Run this command in the source folder

    $ mvn clean jetty:run

   Then open http://localhost:8080/ in the web browser.
