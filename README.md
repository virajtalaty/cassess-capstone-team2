# cassess-capstone-team2
CAssess Capstone Software Project for ASU course SER 402

Test Branch code:
  
  new-rest-service:
    
      1/12/2017
      Either package as runnable JAR with Maven, or Run as a Spring Boot App,
      highly recommended ot import this file structure as a Maven project.
      
      Following executing/running, go to http://localhost:8080/intermediateRest
      At this location the json return value will be seen.
      If you would like to send a particular value for the name in the greeting to be set, alter your address as:
      http://localhost:8080/intermediateRest?name=Thomas
      This would cause the name variabel value to be passed and set to "Thomas"
      This name value is passed from the TestController class into the TestResource class
      where it is then set, and the return is the combined Id with the greeting, now having the name set.
      This is currently just a string returned value.
      *Update 1/14/2017
      TestApplication class has not been updated to return a json packaged value and not a string
      
 existing-rest-request:
      
      1/12/2017
      Either package as runnable JAR with Maven, or Run as a Spring Boot App,
      highly recommended ot import this file structure as a Maven project.
      
      Following execution, you will see that the Logger has been written to 
      at a particular line, with the templated data from the slack api for "users".
      This is currently a string returned value but will be changed shortly to json
      *Update 1/14/2017
      Application class now takes the returned json formatted string and converts to a
      JSON object, also system console prints the object data in pretty print format
      Currently there is shown and utilized, returning the entire json object using simply the String class, 
      and also a customized result using the Users and Members classes to extract specific data from the
      result and then encoding into a JSON object.
      Both results pretty print formatted in strings are displayed to the console
     
jdbc-mysql:

      1/19/2017
      Select App.java and run as a java application to execute main argument method and process jdbc-sql scripts,
      or package as a JAR with the App.java as the main entry point for execution,
      highly recommended ot import this file structure as a Maven project.
      
      Prior to running this project, ensure that you have a MySQL server installed and an instance running.
      Then ensure you have a database schema created and named "dbtest", following this, run the script to
      craete the user table and instantiate the columns properly:
      
      CREATE TABLE `customer` (
      `ID` varchar(10) NOT NULL,
      `NAME` varchar(100) NOT NULL,
      `TEAM_ID` varchar(10) NOT NULL,
      PRIMARY KEY (`ID`)
      ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
      
      Following execution of App.java, the "User" object is created, and the program checks to see if the User record with an
      ID of "USLACKBOT" exists in the "dbtest" MySQL database, under the "User" table and under the 
      "ID" column (primary key).
      If it does exist, then the program retrieves the NAME and TEAM_ID for that particular user corresponding
      to the searched for ID of "USLACKBOT" and applies those values to a User object, after which they are
      converted/built to a string and printed to the console.
      If it does not exists, the program creates that record in the User table for the dbtest database, along
      with the NAME of "slackbot" and TEAM_ID of "T2FBBQ2JH".  Following this, the program retrieves the NAME 
      and TEAM_ID for this particular user corresponding to the searched for ID of "USLACKBOT" and applies 
      those values to a User object, after which they are converted/built to a string and printed to the console.
