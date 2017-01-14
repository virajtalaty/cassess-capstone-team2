# cassess-capstone-team2
CAssess Capstone Software Project for ASU course SER 402

Test Branch code:
  
  new-rest-service:
    
      1/12/2017
      Either package as runnable JAR with Maven, or Run as a Spring Boot App.
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
      Either package as runnable JAR with Maven, or Run as a Spring Boot App.
      Following execution, you will see that the Logger has been written to 
      at a particular line, with the templated data from the slack api for "users".
      This is currently a string returned value but will be changed shortly to json
      *Update 1/14/2017
      Application class now takes the returned json formatted string and converts to a
      JSON object, also system console prints the object data in pretty print format
