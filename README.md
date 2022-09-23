
# Code Challenge 
The goal of this code challenge is to create a microservice using Java and any framework
that you think it is appropriate.

# General notes and assumptions
The app is using the Spring Boot framework. 
The integration test of the rest controller are written with JUnit5 and Mockito.

1) Optional dates and nulls at the transactions could cause indeterminated states in the Status according the requirements. It is asummed at this cases the service can return a new status UNKNOWN, or response empty Http204 NO CONTENT.

2) Date comparations could cause conflict regarding the requirement described as 'TODAY' as today (take into account seconds or miliseconds dont make sense, rigth?). Set to true the parameter com.inrip.bank.param.simple_dates_comparision means the service will trucate dates when compares itself, removing hours minutes and seconds.

3) Entity account keep data to match requeriments about account and credit.

4) Entity transaction keep data to match requeriments about transaction and status logic.

5) The database is working currently is H2, but initial develop has been started with Atlas, the MongoDb Cluster for nono E/R database. But the cluster of Nono requires to configure each client Ip address to accept connections, so the most portable option is to use H2.
The codes to switch for MongoDB are commented.

6) Security. Here is choosen for the API rest the Basic Authorization. Neverless is well known better methods such Oauth2.0 , JWT, among others. Any case regarding this matter is decided at this stage to implement the Basic Auth and move forwad with next requirements. The parameters to configre the access to the API are bank.basic.auth.username and bank.basic.auth.password .

7) Is asummed that is needed to have an entity Account to keep the information regarding credit. For the task to keep the credit possitive , and to validate or to deny a transaction according this rule. Negative fee is allowed, the only restriction is to havent. 

8) When a no mandatory fee is received, the service manage nulls instead to alter the null state in the db.

9) Similar with the channel field in the account status requests, at this case the asumption is returns void http202 ACCEPTED.

10) It is assumen according the model class than a transaction is a child from parent account, assumed that is need to keep record of the credit avaibable for an account_iban , and validate or deny a transaction before it occurs. Could be nice to contruct an interceptor to validate the transactions by the Account service. Following the rule easy code at this stage here is an estandart implementation. The endpoints are contructed following this assumptions, but they are running as separated services.

11) Without context hard difficult to know the best way to implement the endpoint ADD_TRANSACTION. As far PUT is less restrictive than POST, the first is the one chosen.

12) Basic security has been replaced by JWT security (HS512.) The endpoints are /api/user/register and /api/auth/login. The context paths /api/debug , /api/user and /api/auth are not filtering by JWT. The database is H2, so each time the server start is need to register a new user.

13) All the endpoints that are not included or directly referenced in the requirements, or they are needs based on asumptions, are accesible by a unique controller for this tasks, will startup according the value of com.inrip.bank.param.debug.enabled. Also several services are going to include debug traces on json responses if this parameter is set to true.

14) The endpoint to retrieve transactions by account_iban has been protected to return a maximun of items defined by com.inrip.bank.param.security.max_items_size_protection. The client side can especify the page and the size of the result list. By default, the controller set the page to 0 and the size to 5. If the request has set a page where isn't data to response or the items requested are more than the configured protection, then the service will returns empty Http204 NO CONTENT.

15) Entity User is need to save the user credentials for JWT.

# Main components

#### Controller

• AccountController the main controller for the api.
    
    http://localhost:8080/api  
    Payload:
    SimpleBank REST API is running

    http://localhost:8080/api/account/transaction/add
    Payload PUT request:
    {   
        "reference":"1",
        "account_iban":"1",
        "date":"2022-09-19T20:55:42.000Z",
        "amount":2,
        "fee":1
    }

    http://localhost:8080/api/account/transaction/iban/{account_iban}
    http://localhost:8080/api/account/transaction/iban/{account_iban}?descending_amount=true
    http://localhost:8080/api/account/transaction/iban/{account_iban}?descending_amount=false&page=0&size=20
    Payload:
    [
        {
            "reference": "1",
            "account_iban": "1",
            "date": "2022-09-19T20:55:42.000+0000",
            "amount": 2.0,
            "fee": 1.0
        },
        {
            "reference": "2",
            "account_iban": "1",
            "date": "2022-09-19T20:55:42.000+0000",
            "amount": 2.0,
            "fee": 1.0
        }
    ]

    http://localhost:8080/api/account/transaction/status
    Payload request:
    {
        "reference": "1",
        "channel": "CLIENT"
    }    

    Payload response:
    {
        "reference": "1",
        "status": "SETTLED",
        "amount": 1.0,
        "debug": "doBusinessRule_B"
    }    

• UserController to register a new user.

    http://localhost:8080/api/user/register
    Payload POST request
    {
    "userName": "test",
    "password" : "1234",
    "roles" : "ADMIN"
    }    

    Payload response
    {
        "id": 1,
        "userName": "test",
        "roles": "ADMIN"
    }

• AuthenticationController to login.

    http://localhost:8080/api/auth/login
    Payload request
    {
    "username": "test",
    "password" : "1234"
    }

    Payload response
    {
        "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0Iiwic2NvcGVzIjpbeyJyb2xlIjoiUk9MRV9BRE1JTiJ9XSwiaXNzIjoiYWRtaW4iLCJpYXQiOjE2NjM5MjAyNTIsImV4cCI6MTY2MzkzODI1Mn0.y5dF9pN29D1M3t8zr3-VuohZRVz3LU8E4FqNV2mnfBCUX4KsJSKMvmIf7YgR1hWwFww-aqKIBLoTGsaOkrW_cw"
    }    

• DebugController will startup and expose the endpoints according the value of com.inrip.bank.param.debug.enabled.

    http://localhost:8080/api/debug/account/transaction/all
    Payload:
    [
        {
            "reference": "1",
            "account_iban": "1",
            "date": "2022-09-19T20:55:42.000+0000",
            "amount": 2.0,
            "fee": 1.0
        },
        {
            "reference": "2",
            "account_iban": "1",
            "date": "2022-09-19T20:55:42.000+0000",
            "amount": 2.0,
            "fee": 1.0
        }
    ]

    http://localhost:8080/api/debug/account/{account_iban}
    Payload:
    {
        "uid": "1a215417-00e1-4860-b774-e606c0343bc0",
        "accountiban": "1",
        "credit": 1.0
    }    

    http://localhost:8080/api/debug/account/transaction/reference/{reference}
    http://localhost:8080/api/debug/account/transaction/reference/{reference}?descending_amount=false
    Payload
    {
        "uid": "d2d6180c-55bb-48b4-9f28-13fe6d7625f8",
        "accountiban": "1",
        "credit": 1.0
    }    


#### Services

• AccountService

• AccountTransactionService

• AccountTransactionStatusService

• UserService

#### Test

• SimpleBankApplicationTestsIT

#### Repositories

• AccountRepository

• AccountTransactionRepository

• UserRepository

#### Model

• Account and request/response DTOs

• AccountTransaction and request/response DTOs

• User and AuthToken and LoginUser DTOs

#### Handlers and Exception

• SimpleBankHTTPResponseHandler (currently AccountController extends SimpleBankHTTPResponseHandler)

• SimpleBankBadRequestException, SimpleBankHttpAcceptException, SimpleBankHTTPException and SimpleBankNotFoundException

• JwtAuthenticationFilter

#### Configuration and Initialize

• SpringMVCConfig adds the Interceptor TransactionSecurityInterceptor.

• WebSecurityConfig

• ApplicationListenerInitialize , as H2 is a dbm in memory here is able to load the dabase for dev.

• MongoDBConfig the main class to configure the connection to MongoDB Cluster.

• SimpleBankRequestMappings where the restful endpoints will mount before the servlet conetxt path.

# Configuration parameters
File application.properties has the next list of configurable parameters:

• server.servlet.context-path is the parent mount point for the services exposed

• server.port is by default to 8080

• com.inrip.bank.param.alive_message is the welcome message in the API , accesible at the root path of the servlet context

• com.inrip.bank.param.uncomputable_status_transactions_returns_unknown if set to true, then the service will response status UNKNOWN for trasactions without the field date setted. Set it by default to FALSE, then the service will up a HttpAcceptException, then the servlet returns Http 202 ACCEPTED. Such of that, is better the assumption to response HTTP202 ACCEPTED. By default is set to FALSE.

• com.inrip.bank.param.simple_dates_comparision if set to true the service will truncate dates for comparision and according the assumption about the requeriment TODAY. If set to false, then the comparision between dates will complete. Set by default to TRUE.

• com.inrip.bank.param.create_account_iban_if_not_exists if set to true when the service receive a transaction and the account isnt exists, then the service will create the account and set the initial credit to 0.

• com.inrip.bank.param.validate_credit_before_transacion set to true then the service will check if the account iban has enough credit to support the operation. Set by default to TRUE.

• com.inrip.bank.param.debug.enabled if set to true the service will add several debug information at the json response and startup the debug controller that expose the endpoints from /api/debug/*.

• com.inrip.bank.param.security.signing_key is the string to sign the securoty tokens.

• com.inrip.bank.param.security.token_prefix included for the token.

• com.inrip.bank.param.security.header_string is the string to locate the autorization block in the header.

• com.inrip.bank.param.security.max_items_protection is the maximun size of items returned by the endpoints. 

Default recommended configuration:

    server.port=8080
    server.servlet.context-path=/api

    com.inrip.bank.param.alive_message=SimpleBank REST API is running
    com.inrip.bank.param.uncomputable_status_transactions_returns_unknown=false
    com.inrip.bank.param.simple_dates_comparision=true
    com.inrip.bank.param.create_account_iban_if_not_exists=true
    com.inrip.bank.param.validate_credit_before_transacion=true
    com.inrip.bank.param.debug.enabled=false

    com.inrip.bank.param.security.signing_key=<write_your_key>
    com.inrip.bank.param.security.token_prefix=Bearer 
    com.inrip.bank.param.security.header_string=Authorization
    com.inrip.bank.param.security.max_items_size_protection=20


# Test cases
The next list parameters could interfere with the proper run of the test. 
Set them to the default values:

    com.inrip.bank.param.create_account_iban_if_not_exists=true
    com.inrip.bank.param.validate_credit_before_transacion=true

# API URL
The file Bank.postman_collection.json include the main commands and paths to the api.

# Currently working on (main pendings ordered..)

• (done) tests for field fee set to null on transactions (shall avoid null pointer exceptions managing nulls and maths.)

• (done) transactional propagation on services.

• (done) JPA annotation to link entities Account and Transaction.

• (done) refactor Transaction class name and methods --QOL -> Transaction to AccountTransaction<=Account.

• (done) apply Builder pattern to pojos.

• (done) JWT Security layer instead of Basic.

• (done) pagination on methods find or findAll

• (done) debugging instances into a debug apirest controller (debug, configured by a parameter on startup)

• test cases for pagination

# Author
2022, Enrique AC
