This HR Application is a CRUD application and talks to the User Application. This demonstrates MICROSERVICES comunication.
It is created using swagger top down approch using https://editor.swagger.io/
It uses H2 Database
Uses Junit
Uses JWT decoder and Encoder
Uses Custom Exception Handling

Functionality:

When HR application is started a default admin/admin user is added to H2 DB
User adds user with username and password in User Application. This will call te HR appliction using Rest Template. Logic is written in HR Application.
User logins with any registered DB. This will call te HR appliction using Rest Template to check if User Exists. Logic is written in HR Application.
Gets Token
Use the Token in all the api calls in Hr Application.

