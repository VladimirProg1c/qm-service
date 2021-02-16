# qm-service

This service is a Spring Rest application, the part of the QuoteMe application.

In order to build artifact run the following command:<br>
<code>gradle build</code>

Command to build service image:<br>
<code>docker build -t qm-service-image:latest .</code><br>

Command to run service container:<br>
<code>docker run -p 8080:8080 qm-service-image:latest</code><br>

Command to run service container in background:<br>
<code>docker run -d -p 8080:8080 qm-service-image:latest</code>


Service uses mysql database. In order to install database with
Docker, execute following commands:
- pull mysql server image:<br>
<code>docker pull mysql/mysql-server:latest</code>
- run container:<br>
<code>docker run -p 3306:3306 --name=local-mysql -e MYSQL_ROOT_PASSWORD=root -d mysql/mysql-server:latest</code>
- run mysql client in the started container:<br>
<code>docker exec -it local-mysql mysql -uroot -p</code>
- create user that has access outside the container<br>
<code>CREATE USER 'root'@'%' IDENTIFIED BY 'root';</code><br>
<code>GRANT ALL PRIVILEGES ON \*.\* TO 'root'@'%' WITH GRANT OPTION;</code>
