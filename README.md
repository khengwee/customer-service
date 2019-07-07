# Customer Service 

This is Customer Service project. This project shows how to build a microservice using Spring Webflux (Reactive) 
with Spring Security Oauth2 between microservices integration. 

## Build Package from Source

Clone the repository:

    $ git clone https://github.com/khengwee/customer-service.git

If this is your first time using GitHub, review http://help.github.com to learn the basics.

Execute the command line below with Maven:

    $ cd customer-service
    $ mvn clean install -Dbuild.version=1.0.0-SNAPSHOT
    
## Test Integration between Microservices

Please clone both Customer Service & Customer Resource repositories

    $ git clone https://github.com/khengwee/customer-service.git
    $ git clone https://github.com/khengwee/customer-resource.git
    
Start both services by running the command below:

    $ mvn spring-boot:run
    
Use the curl command to trigger the Customer Service

    $ curl -H "Authorization: Basic a2l3aXVzZXI6cGFzc3dvcmQ=" -H "Content-Type: application/json" http://localhost:8091/api/customers/1

You will be able to see the sample response from Customer Resource as shown below

    {"id":"1","name":"JohnSmith","segment":"EXBN"}
    
## Todo

Planning to move customer-service $ customer-resource services into one single repo and 
to move the reusable code into a single core library