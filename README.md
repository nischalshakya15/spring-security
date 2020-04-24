## Spring security with JWT authentication 
This project demonstrates how to integrate jwt authentication in spring boot application. 
Along with this it contains spring profiles (dev and prod), H2 database and swagger for API documentation.
Additionally, you can choose your own database while using in production.

## Prerequisite
* Java 8 or higher
* Maven 
* Docker (optional)
* IDE 

## Build and Run
* git clone https://github.com/nischalshakya15/spring-security.git

* Go to the project directory.
    
    ``cd spring-security``
    
* Build the project with specific profile. i.e: dev or prod. By default, profile will be dev if not specified.

    ``mvn clean install -P{PROFILE_NAME}``
    
* Go to the target directory.
    
    ``cd /target``
    
* Run the project 
    
    ``java -jar spring-security.war``

## Run with docker 
* Build the project using mvn command. 

    ``mvn clean install -P{PROFILE_NAME}``
    
* Build and run the container. 

    ``docker-compose up``

## Swagger documentation
* Go to the browser

   ``http://localhost:8081/swagger-ui.html``

   ![API Endpoints](./images/endpoints.png)


## Using accessToken to access API endpoints. 

* Authenticate the user with **userName admin**  and **password admin**.

* If authentication is successful it will give you an accessToken. 

  ![Authentication successful](./images/authentication.png)

* Using that accessToken we can access the protected end points of **user-resource**.
   
  ![Access UserResource](./images/response.png) 



**Note: You can also use curl command as shown in Curl section of swagger-ui**
    
