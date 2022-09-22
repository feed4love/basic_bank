
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

5) The database is working currently is H2, but initial develop has been started with Atlas, the MongoDb Cluster for nono E/R database. But the cluster of Nono requires to configure each client Ip address to accept connections, so the most portable option is to use H2.
The codes to switch for MongoDB are commented.

6) Security. Here is choosen for the API rest the Basic Authorization. Neverless is well known better methods such Oauth2.0 , JWT, among others. Any case regarding this matter is decided at this stage to implement the Basic Auth and move forwad with next requirements. The parameters to configre the access to the API are bank.basic.auth.username and bank.basic.auth.password .

7) Is asummed that is needed to have an entity Account to keep the information regarding credit and IBAN. For the simple task to keep the credit possitive , and to validate or to deny a transaction according this rule. Negative fee is allowed, the only restriction is to havent. No api endpoits has been implemented to interrogate the Account entity.

8) When a no mandatory fee is received, the service shall manage nulls instead to alter the null state in the db.

9) Similar with the channel field in the account status requests, at this case the asumption is returns void http202 ACCEPTED.

10) It is assumen according the model class than a transaction is a child from parent account, assumed that is need to keep record of the credit avaibable for an account_iban , and validate or deny a transaction before it occurs. Could be nice to contruct an interceptor to validate the transactions by the Account service. Following the rule easy code at this stage here is an estandart implementation. The endpoints are contructed following this assumptions, but they are running as separated services.

11) Without context hard difficult to know the best way to implement the endpoint ADD_TRANSACTION. As far PUT is less restrictive than POST, the first is the one chosen.

# Main components

#### Controller

• AccountController

#### Services

• AccountService , exposed for debug serve the endpoint (not mandatory):

    http://test:1234@localhost:8080/api/account/{account_iban}
    Payload
    {
        "uid": "d2d6180c-55bb-48b4-9f28-13fe6d7625f8",
        "accountiban": "1",
        "credit": 1.0
    }    

• AccountTransactionService serve the endpoints:

    http://test:1234@localhost:8080/api/account/    
    Payload:
    SimpleBank REST API is running

    http://test:1234@localhost:8080/api/account/{account_iban}
    Payload:
    {
        "uid": "1a215417-00e1-4860-b774-e606c0343bc0",
        "accountiban": "1",
        "credit": 1.0
    }    

    http://test:1234@localhost:8080/api/account/transaction/add
    Payload PUT request:
    {   
        "reference":"1",
        "account_iban":"1",
        "date":"2022-09-19T20:55:42.000Z",
        "amount":2,
        "fee":1
    }

    http://test:1234@localhost:8080/api/account/transaction/iban/{account_iban}
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

    http://test:1234@localhost:8080/api/account/transaction/iban/{account_iban}?descending_amount=true

• AccountTransactionStatusService serve the endpoint:

    http://test:1234@localhost:8080/api/account/transaction/status
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

#### Test

• SimpleBankApplicationTestsIT

#### Repositories

• AccountRepository

• AccountTransactionRepository

#### Model

• Account and request/response DTOs

• AccountTransaction and request/response DTOs

#### Handlers and Exception

• SimpleBankHTTPResponseHandler (currently AccountController extends SimpleBankHTTPResponseHandler)

• SimpleBankBadRequestException, SimpleBankHttpAcceptException, SimpleBankHTTPException and SimpleBankNotFoundException

#### Configuration and Initialize

• SpringMVCConfig adds the Interceptor TransactionSecurityInterceptor.

• ApplicationListenerInitialize , as H2 is a dbm in memory here is able to load the dabase for dev.

• MongoDBConfig the main class to configure the connection to MongoDB Cluster.

• SimpleBankRequestMappings where the restful endpoints will mount before the servlet conetxt path.

# Configuration parameters
File application.properties has the next list of configurable parameters:

• server.servlet.context-path is the parent mount point for the services exposed

• server.port is by default to 8080

• bank.basic.message.alive is the welcome message in the API , accesible at the root path of the servlet context

• bank.basic.auth.username is the username expected in the request headers Basic Auth

• bank.basic.auth.password is the password expected in the request headers Basic Auth

• bank.basic.ACCEPT_UNKNOWN_TRANSACTION_STATUS if set to true, then the service will response status UNKNOWN for trasactions without the field date setted. Set it by default to FALSE, then the service will up a HttpAcceptException, then the servlet returns Http 202 ACCEPTED. Such of that, is better the assumption to response HTTP202 ACCEPTED. By default is set to FALSE.

• bank.basic.TRANSACTION_STATUS_TRUNCATE_DATES if set to true the service will truncate dates for comparision and according the assumption about the requeriment TODAY. If set to false, then the comparision between dates will complete. Set by default to TRUE.

• bank.basic.ASSUMPTION_ACCOUNT_IBAN_SHALL_EXISTS if set to true when the service receive a transaction and the account isnt exists, then the service will create the account and set the initial credit to 0.

• bank.basic.ASSUMPTION_CHECK_CREDIT_FOR_TRANSACTIONS set to true then the service will check if the account iban has enough credit to support the operation. Set by default to TRUE.

• bank.basic.DEBUG_DATA_ON_RESPONSES if set to true the service will add several debug information at the json response. Currently included at the StatusResponseDTO to track the constraint applied to calculate the status. By default set to FALSE.

• bank.basic.DEBUG_API_METHODS if set to true the service will accept debuging api calls to the endpoints from externals:

    /api/account/{account_iban} -> search an account by account_iban.
    /api/account/transaction/all -> retrieve all transactions.
    /api/account/transaction/reference/{reference} -> search transaction by reference

Default configuration:

    server.port=8080    
    server.servlet.context-path=/api

    bank.basic.message.alive=SimpleBank REST API is running
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

# Currently working on (main pendings ordered..)

• (done) tests for field fee set to null on transactions (shall avoid null pointer exceptions managing nulls and maths.)

• (done) transactional propagation on services.

• (done) JPA annotation to link entities Account and Transaction.

• (done) refactor Transaction class name and methods --QOL -> Transaction to AccountTransaction<=Account.

• (done) apply Builder pattern to pojos.

• JWT Security layer instead of Basic.

# Author
2022, Enrique AC
