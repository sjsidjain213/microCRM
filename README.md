# microCRM
This application is a microCRM. In which users can add the email address and name.
Which is then stored in the database.  
This application gives options to create the customizable email lists for any set 
of users whose entries are present in the database.
once lists are created owner can send mail to all the users whose email id's are 
present in these list by single click.
The owner can use this  application to sent  mail to registered users.

In backend at server side, Java Servlets is used. At Front, End AngularJS is used. 

Database Details-->
MySQL Querybrowser version 1.2.12
For Using this application create a table named "coursecode" (do not include inverted commas in name) inside a schema named "test" (do not include inverted commas in name).

table coursecode should have following schema ->

	create table coursecode(
			 ID int unsigned NOT NULL AUTO_INCREMENT,
             Name varchar(255) NOT NULL,
             Email varchar(255) NOT NULL,
             PRIMARY KEY (P_Id)
			 )

Deployment Details-->
create an additional folder with following directory structure WebContent/WEB-INF/lib for adding following dependency jar
Inside lib add following jar files
1.java-json.jar (http://www.java2s.com/Code/JarDownload/java-json/java-json.jar.zip)

2.jersey-bundle-1.8.jar (http://www.java2s.com/Code/JarDownload/jersey-bundle/jersey-bundle-1.8.jar.zip)

3.jsr311-api-1.1.1.jar (http://www.java2s.com/Code/JarDownload/jsr311/jsr311-api-1.1.1.jar.zip)

4.json-simple-1.1.1.jar (http://www.java2s.com/Code/JarDownload/json-simple/json-simple-1.1.1.jar.zip )

5.com.mysql.jdbc_5.15.jar  ( http://www.java2s.com/Code/JarDownload/com.mysql/com.mysql.jdbc_5.1.5.jar.zip)

Server congifuration and other-->
running on port no -> 8080 of tomcat apache
server runtime used -> apache tomat 7.0
name of project -> causecode
name of package for java class -> controller

expected url format->
http://localhost:8080/causecode/option.html
http://localhost:8080/causecode/newlist.html
http://localhost:8080/causecode/maillist.html
