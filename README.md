# CodeFellowship

This app allows students at Code Fellows to connect with each other and support each other on their coding joureys. It uses Spring Security to authenticate users.
If a user is not logged in they only have the options to login or sign up. If they are logged in, they can add posts to their profile, find other users, view user profiles, follow other users, and view the posts of all the users they are following on a personal feed page.


To run this app:
  *Ensure you have postgreSQL installed on your computer.
  *Clone this repo.
  *Modify the application.properties file to use the correct databaseURL, name, and password. Create a new database on your machine if necessary.
  *Run `./gradlew bootRun`. This will start a local server on port 8080 which you can view in your browser on http://localhost:8080/