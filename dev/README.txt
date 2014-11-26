==== Installation and Configuration ====
This guide is avaiable at https://code.google.com/p/gqm-blind/


Run the following command to start working with GQM-Blind on a jetty server.

mvn jetty:run -P prod -DskipTests=false -Djetty.port=9999 -Ddb.name=GQM_BLIND_PROD
Open http://localhost:9999/ in your browser. Login with your username/password and start to eork.

If you prefer to use a different application such as Tomcat, you need to generate a war file. The following is an example configuration that uses the following environment variables:

JAVA_HOME=C:\Program Files\Java\jdk1.7.0_21
M2_HOME=C:\Dev\Software\apache-maven-3.0.5
TOMCAT_HOME=C:\Dev\Software\apache-tomcat-7.0.41
Generates a war package with the following command:

mvn install -P prod -DskipTests=true -Ddb.name=GQM_BLIND_PROD
 * skipTests -> skip testing and does not populate the database with 
sample data (skip the process of creating and dropping tables);
 * db.name -> database name to use (must be available on the host 
of the application need)
If you want skip test and does not populate a database with samples data, you have to use an esternal database. For example to import a mysql database backup run this command from command line:

mysqlimport.exe -u root -p GQM_BLIND_test GQM_BLIND_PROD.sql
The database name must match whit db.name parameter in the previous step.

if you want to experiment with web services you can find a wsdl for the metrics at: http://localhost:9999/services/MetricService?wsdl

A simple web application is available under the tags directory of the sources. You can access a page that works as a client for the web service metric. From this page you can get information on a metric ask to receive all the measurements collected for that metric. The URL to which access is the following: http://localhost:8080/GQM-WebServiceClient/sampleMetricManagerProxy/TestClient.jsp