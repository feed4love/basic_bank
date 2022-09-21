
# Code Challenge 
The goal of this code challenge is to create a microservice using Java and any framework
that you think it is appropriate.

# General notes and assumptions
The app is using the Spring Boot framework. 
The integration test of the rest controller are written with JUnit5 and Mockito.

1) Optional dates to null in the Transaction will cause indeterminated state in the Status Transaction when it is requested. It is asummed at this cases the service can return a new status UNKNOWN, or response empty HTTP 202 ACCEPTED.
Can be configured with the parameter ACCEPT_UNKNOWN_TRANSACTION_STATUS, if set to true then the service will response UNKNOWN when the date is not avaibable. By default set to FALSE, then the service will return Htttp202.

2) Date comparations could cause conflict regarding the requirement described as 'TODAY' as today (take into account seconds or miliseconds dont make sense, rigth?). Anycase, If dates are or not truncated for comparision shall be configured with the parameter TRANSACTION_STATUS_TRUNCATE_DATES.

3) Entity account keep data to match requeriments about account and credit

4) Entity transaction keep data to match requeriments about transaction and status logic

5) The database is working curently is H2, but initial develop has been started with Atlas, the MongoDb Cluster that is a great option for nono E/R database. But the cluster of Mongo require to configure each client Ip address to accept connections, so the most portable option is to use H2.
The codes to switch for MongoDB are commented.

6) Security. Here is choose to use a Basic Authorization for the API. Is well known better methods such Oauth2.0 , JWT, among others. Any case regarding this matter is decided at this stage to implement the Basic Auth and move forwad with requirements.
The parameters to configre the access to the API are:
    bank.basic.auth.username
    bank.basic.auth.password

7) It is asumme that is need to have an entity name account to keep data about the credit avaibable associated with an IBAN Account code.


# Configuration parameters
The file application.properties has the next list of configurable parameters:

• server.port is by default to 8080

• bank.basic.message.alive is the welcome message in the API , accesible at the root path of the servlet context

• bank.basic.auth.username is the username expected in the request headers Basic Auth

• bank.basic.auth.password is the password expected in the request headers Basic Auth

• bank.basic.ACCEPT_UNKNOWN_TRANSACTION_STATUS if set to true, then the service will response status UNKNOWN for trasactions without the field date setted. Set it by default to FALSE, then the service will up a HttpAcceptException, then the servlet returns Http 202 ACCEPTED. Such of that, is better the assumption to response HTTP202 ACCEPTED. By default is set to FALSE.

• bank.basic.TRANSACTION_STATUS_TRUNCATE_DATES if set to true the service will truncate dates for comparision and according the assumption about the requeriment TODAY. If set to false, then the comparision between dates will complete. Set by default to FALSE.

• bank.basic.ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS if set to true when the service receive a transaction and the account isnt exists, then the service will create the account and set the initial credit to 0.

• bank.basic.ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS set to true then the service will check if the account iban has enough credit to support the operation.

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

# Author
Enrique AC
