About WeatherProject :

1. The project is created in Netbeans (Can compile in Eclipse)

2. The latest version of Spring BOOT envirnoment is used (Ver 2.1.3)

3. The JSON is used for all data handling. It was a natural choice because the data coming from Accuweather is in
    JSON format and Java has very support for handling JSON (As against XML)

4. Due to Spring BOOT, code is very compact

5. In order to run the project : localhost:8080/wthr

6. The Spring BOOT generates all the test cases and executes them at the time of build itself

7. Build the project in any IDE and copy war file to Tomcat's webapp folder

8. Start Tomcat

9. On a browser, type localhost:8080/wthr

10. If one does not want to go through build process, war file is included on git

11. Only US zip codes are handled
