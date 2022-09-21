
# Code Challenge 
The goal of this code challenge is to create a microservice using Java and any framework
that you think it is appropriate.

# General notes and assumptions
The app is using the Spring Boot framework. 
The integration test of the rest controller are written with JUnit5 and Mockito.

1) Optional dates and nulls at the transactions could cause indeterminated states in the Status according the requirements. It is asummed at this cases the service can return a new status UNKNOWN, or response empty HTTP 202 ACCEPTED.

2) Date comparations could cause conflict regarding the requirement described as 'TODAY' as today (take into account seconds or miliseconds dont make sense, rigth?). Set to true the parameter TRANSACTION_STATUS_TRUNCATE_DATES means the service will trucate dates when compares itself, removing hours minutes and seconds.

3) Entity account keep data to match requeriments about account and credit.

4) Entity transaction keep data to match requeriments about transaction and status logic.

5) The database is working currently is H2, but initial develop has been started with Atlas, the MongoDb Cluster for nono E/R database. But the cluster of Mongo require to configure each client Ip address to accept connections, so the most portable option is to use H2.
The codes to switch for MongoDB are commented.

6) Security. Here is choosen for the API rest the Basic Authorization. Neverless is well known better methods such Oauth2.0 , JWT, among others. Any case regarding this matter is decided at this stage to implement the Basic Auth and move forwad with requirements.
The parameters to configre the access to the API are:
    bank.basic.auth.username
    bank.basic.auth.password

7) It is asumme that is needed to have an entity named account to keep data about the credit avaibable and associated with the IBAN Account noted into the transactions.

# Configuration parameters
The file application.properties has the next list of configurable parameters:

• server.port is by default to 8080

• bank.basic.message.alive is the welcome message in the API , accesible at the root path of the servlet context

• bank.basic.auth.username is the username expected in the request headers Basic Auth

• bank.basic.auth.password is the password expected in the request headers Basic Auth

• bank.basic.ACCEPT_UNKNOWN_TRANSACTION_STATUS if set to true, then the service will response status UNKNOWN for trasactions without the field date setted. Set it by default to FALSE, then the service will up a HttpAcceptException, then the servlet returns Http 202 ACCEPTED. Such of that, is better the assumption to response HTTP202 ACCEPTED. By default is set to FALSE.

• bank.basic.TRANSACTION_STATUS_TRUNCATE_DATES if set to true the service will truncate dates for comparision and according the assumption about the requeriment TODAY. If set to false, then the comparision between dates will complete. Set by default to TRUE.

• bank.basic.ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS if set to true when the service receive a transaction and the account isnt exists, then the service will create the account and set the initial credit to 0.

• bank.basic.ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS set to true then the service will check if the account iban has enough credit to support the operation. Set by default to TRUE.

• bank.basic.DEBUG_DATA_ON_RESPONSES if set to true the service will include debug information in the json, by default false.

• bank.basic.DEBUG_API_METHODS if set to true the service will accept debuging api calls.

Default configuration:

    server.port=8080

    bank.basic.message.alive=Transaction REST API is running
    bank.basic.auth.username=test
    bank.basic.auth.password=1234

    bank.basic.ACCEPT_UNKNOWN_TRANSACTION_STATUS=false
    bank.basic.TRANSACTION_STATUS_TRUNCATE_DATES=true
    bank.basic.ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS=true
    bank.basic.ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS=true
    bank.basic.DEBUG_DATA_ON_RESPONSES=false
    bank.basic.DEBUG_API_METHODS=false

    ## para usar mongodb, en caso contrario H2
    ## bank.basic.mongodb.uri=mongodb+srv://test:1234@sandbox.bjcecbp.mongodb.net/simple_bank
    ## spring.data.mongodb.database=simple_bank

# Test cases
The next list parameters could interfere with the proper run of the test. 
Set them to the default values:
    bank.basic.ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS=true
    bank.basic.ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS=true

# API URL
The file Bank.postman_collection.json include the main commands and paths to the api.
A example list of avaibable calls to end points are:

    http://test:1234@localhost:8080/api/transaction/
    http://test:1234@localhost:8080/api/transaction/add
    http://test:1234@localhost:8080/api/transaction/iban/IBAN-001
    http://test:1234@localhost:8080/api/transaction/iban/IBAN-001?descending_amount=true
    http://test:1234@localhost:8080/api/transaction/status

    Disable by debug:
    http://test:1234@localhost:8080/api/transaction/all
    http://test:1234@localhost:8080/api/transaction/reference/REF-TEST?descending_amount=false

# Author
2022 Enrique AC
