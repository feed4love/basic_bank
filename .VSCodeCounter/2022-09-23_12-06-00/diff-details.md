# Diff Details

Date : 2022-09-23 12:06:00

Directory d:\\BACKUP_UBUNTU_PROFILE\\develop\\simple_bank

Total : 80 files,  1451 codes, 200 comments, 314 blanks, all 1965 lines

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [Bank.postman_collection.json](/Bank.postman_collection.json) | JSON | 311 | 0 | 0 | 311 |
| [README.md](/README.md) | Markdown | 158 | 0 | 78 | 236 |
| [pom.xml](/pom.xml) | XML | 25 | -2 | 5 | 28 |
| [src/main/java/com/inrip/bank/BankApplication.java](/src/main/java/com/inrip/bank/BankApplication.java) | Java | -15 | 0 | -8 | -23 |
| [src/main/java/com/inrip/bank/SimpleBankApplication.java](/src/main/java/com/inrip/bank/SimpleBankApplication.java) | Java | 15 | 0 | 8 | 23 |
| [src/main/java/com/inrip/bank/common/Constants.java](/src/main/java/com/inrip/bank/common/Constants.java) | Java | -26 | -4 | -4 | -34 |
| [src/main/java/com/inrip/bank/common/RequestMappings.java](/src/main/java/com/inrip/bank/common/RequestMappings.java) | Java | -10 | -5 | -3 | -18 |
| [src/main/java/com/inrip/bank/common/SimpleBankConstants.java](/src/main/java/com/inrip/bank/common/SimpleBankConstants.java) | Java | 43 | 3 | 8 | 54 |
| [src/main/java/com/inrip/bank/common/SimpleBankRequestMappings.java](/src/main/java/com/inrip/bank/common/SimpleBankRequestMappings.java) | Java | 12 | 8 | 7 | 27 |
| [src/main/java/com/inrip/bank/common/SimpleBankUtils.java](/src/main/java/com/inrip/bank/common/SimpleBankUtils.java) | Java | 53 | 0 | 9 | 62 |
| [src/main/java/com/inrip/bank/common/Utils.java](/src/main/java/com/inrip/bank/common/Utils.java) | Java | -53 | 0 | -9 | -62 |
| [src/main/java/com/inrip/bank/config/SpringMVCConfig.java](/src/main/java/com/inrip/bank/config/SpringMVCConfig.java) | Java | -15 | -7 | -8 | -30 |
| [src/main/java/com/inrip/bank/config/WebSecurityConfig.java](/src/main/java/com/inrip/bank/config/WebSecurityConfig.java) | Java | 48 | 34 | 10 | 92 |
| [src/main/java/com/inrip/bank/controller/AccountController.java](/src/main/java/com/inrip/bank/controller/AccountController.java) | Java | 61 | 25 | 24 | 110 |
| [src/main/java/com/inrip/bank/controller/AuthenticationController.java](/src/main/java/com/inrip/bank/controller/AuthenticationController.java) | Java | 44 | 0 | 11 | 55 |
| [src/main/java/com/inrip/bank/controller/DebugController.java](/src/main/java/com/inrip/bank/controller/DebugController.java) | Java | 71 | 12 | 16 | 99 |
| [src/main/java/com/inrip/bank/controller/TransactionController.java](/src/main/java/com/inrip/bank/controller/TransactionController.java) | Java | -72 | -31 | -20 | -123 |
| [src/main/java/com/inrip/bank/controller/UserController.java](/src/main/java/com/inrip/bank/controller/UserController.java) | Java | 22 | 0 | 5 | 27 |
| [src/main/java/com/inrip/bank/controller/exceptions/BadRequestException.java](/src/main/java/com/inrip/bank/controller/exceptions/BadRequestException.java) | Java | -13 | -4 | -6 | -23 |
| [src/main/java/com/inrip/bank/controller/exceptions/DuplicateUserNameException.java](/src/main/java/com/inrip/bank/controller/exceptions/DuplicateUserNameException.java) | Java | 14 | 0 | 5 | 19 |
| [src/main/java/com/inrip/bank/controller/exceptions/HTTPException.java](/src/main/java/com/inrip/bank/controller/exceptions/HTTPException.java) | Java | -44 | -4 | -17 | -65 |
| [src/main/java/com/inrip/bank/controller/exceptions/NotFoundException.java](/src/main/java/com/inrip/bank/controller/exceptions/NotFoundException.java) | Java | -13 | -4 | -7 | -24 |
| [src/main/java/com/inrip/bank/controller/exceptions/ResourceNotFoundException.java](/src/main/java/com/inrip/bank/controller/exceptions/ResourceNotFoundException.java) | Java | 24 | 0 | 8 | 32 |
| [src/main/java/com/inrip/bank/controller/exceptions/SimpleBankBadRequestException.java](/src/main/java/com/inrip/bank/controller/exceptions/SimpleBankBadRequestException.java) | Java | 13 | 0 | 6 | 19 |
| [src/main/java/com/inrip/bank/controller/exceptions/SimpleBankHTTPException.java](/src/main/java/com/inrip/bank/controller/exceptions/SimpleBankHTTPException.java) | Java | 44 | 0 | 17 | 61 |
| [src/main/java/com/inrip/bank/controller/exceptions/SimpleBankHttpAcceptException.java](/src/main/java/com/inrip/bank/controller/exceptions/SimpleBankHttpAcceptException.java) | Java | 16 | 3 | 3 | 22 |
| [src/main/java/com/inrip/bank/controller/exceptions/SimpleBankNotFoundException.java](/src/main/java/com/inrip/bank/controller/exceptions/SimpleBankNotFoundException.java) | Java | 13 | 0 | 7 | 20 |
| [src/main/java/com/inrip/bank/controller/handlers/HTTPResponseHandler.java](/src/main/java/com/inrip/bank/controller/handlers/HTTPResponseHandler.java) | Java | -173 | -4 | -75 | -252 |
| [src/main/java/com/inrip/bank/controller/handlers/SimpleBankHTTPResponseHandler.java](/src/main/java/com/inrip/bank/controller/handlers/SimpleBankHTTPResponseHandler.java) | Java | 195 | 3 | 87 | 285 |
| [src/main/java/com/inrip/bank/dto/AccountRequestDTO.java](/src/main/java/com/inrip/bank/dto/AccountRequestDTO.java) | Java | 25 | 0 | 0 | 25 |
| [src/main/java/com/inrip/bank/dto/AccountResponseDTO.java](/src/main/java/com/inrip/bank/dto/AccountResponseDTO.java) | Java | 18 | 0 | -4 | 14 |
| [src/main/java/com/inrip/bank/dto/AccountTransactionRequestDTO.java](/src/main/java/com/inrip/bank/dto/AccountTransactionRequestDTO.java) | Java | 99 | 4 | 18 | 121 |
| [src/main/java/com/inrip/bank/dto/AccountTransactionResponseDTO.java](/src/main/java/com/inrip/bank/dto/AccountTransactionResponseDTO.java) | Java | 83 | 4 | 17 | 104 |
| [src/main/java/com/inrip/bank/dto/AccountTransactionStatusRequestDTO.java](/src/main/java/com/inrip/bank/dto/AccountTransactionStatusRequestDTO.java) | Java | 42 | 10 | 8 | 60 |
| [src/main/java/com/inrip/bank/dto/AccountTransactionStatusResponseDTO.java](/src/main/java/com/inrip/bank/dto/AccountTransactionStatusResponseDTO.java) | Java | 72 | 16 | 16 | 104 |
| [src/main/java/com/inrip/bank/dto/AuthToken.java](/src/main/java/com/inrip/bank/dto/AuthToken.java) | Java | 15 | 0 | 6 | 21 |
| [src/main/java/com/inrip/bank/dto/LoginUser.java](/src/main/java/com/inrip/bank/dto/LoginUser.java) | Java | 17 | 0 | 7 | 24 |
| [src/main/java/com/inrip/bank/dto/StatusRequestDTO.java](/src/main/java/com/inrip/bank/dto/StatusRequestDTO.java) | Java | -17 | -10 | -9 | -36 |
| [src/main/java/com/inrip/bank/dto/StatusResponseDTO.java](/src/main/java/com/inrip/bank/dto/StatusResponseDTO.java) | Java | -56 | -16 | -18 | -90 |
| [src/main/java/com/inrip/bank/dto/TransactionRequestDTO.java](/src/main/java/com/inrip/bank/dto/TransactionRequestDTO.java) | Java | -59 | -4 | -22 | -85 |
| [src/main/java/com/inrip/bank/dto/TransactionResponseDTO.java](/src/main/java/com/inrip/bank/dto/TransactionResponseDTO.java) | Java | -51 | -4 | -21 | -76 |
| [src/main/java/com/inrip/bank/model/Account.java](/src/main/java/com/inrip/bank/model/Account.java) | Java | 9 | 2 | 1 | 12 |
| [src/main/java/com/inrip/bank/model/AccountTransaction.java](/src/main/java/com/inrip/bank/model/AccountTransaction.java) | Java | 80 | 23 | 34 | 137 |
| [src/main/java/com/inrip/bank/model/Transaction.java](/src/main/java/com/inrip/bank/model/Transaction.java) | Java | -70 | -21 | -30 | -121 |
| [src/main/java/com/inrip/bank/model/User.java](/src/main/java/com/inrip/bank/model/User.java) | Java | 41 | 0 | 16 | 57 |
| [src/main/java/com/inrip/bank/repository/AccountTransactionRepository.java](/src/main/java/com/inrip/bank/repository/AccountTransactionRepository.java) | Java | 14 | 9 | 7 | 30 |
| [src/main/java/com/inrip/bank/repository/TransactionRepository.java](/src/main/java/com/inrip/bank/repository/TransactionRepository.java) | Java | -14 | -9 | -7 | -30 |
| [src/main/java/com/inrip/bank/repository/UserRepository.java](/src/main/java/com/inrip/bank/repository/UserRepository.java) | Java | 6 | 0 | 3 | 9 |
| [src/main/java/com/inrip/bank/security/JwtAuthenticationEntryPoint.java](/src/main/java/com/inrip/bank/security/JwtAuthenticationEntryPoint.java) | Java | 17 | 0 | 5 | 22 |
| [src/main/java/com/inrip/bank/security/JwtAuthenticationFilter.java](/src/main/java/com/inrip/bank/security/JwtAuthenticationFilter.java) | Java | 67 | 0 | 15 | 82 |
| [src/main/java/com/inrip/bank/security/JwtTokenUtil.java](/src/main/java/com/inrip/bank/security/JwtTokenUtil.java) | Java | 67 | 0 | 23 | 90 |
| [src/main/java/com/inrip/bank/service/accountTransactionStatus/AccountTransactionStatusService.java](/src/main/java/com/inrip/bank/service/accountTransactionStatus/AccountTransactionStatusService.java) | Java | 7 | 4 | 3 | 14 |
| [src/main/java/com/inrip/bank/service/accountTransactionStatus/AccountTransactionStatusServiceImpl.java](/src/main/java/com/inrip/bank/service/accountTransactionStatus/AccountTransactionStatusServiceImpl.java) | Java | 80 | 18 | 30 | 128 |
| [src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionLogicalValidator.java](/src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionLogicalValidator.java) | Java | 10 | 22 | 8 | 40 |
| [src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionService.java](/src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionService.java) | Java | 13 | 4 | 4 | 21 |
| [src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionServiceImpl.java](/src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionServiceImpl.java) | Java | 140 | 48 | 43 | 231 |
| [src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionStatusLogicalValidator.java](/src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionStatusLogicalValidator.java) | Java | 194 | 93 | 42 | 329 |
| [src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionTransformer.java](/src/main/java/com/inrip/bank/service/accountTransaction/AccountTransactionTransformer.java) | Java | 34 | 11 | 11 | 56 |
| [src/main/java/com/inrip/bank/service/account/AccountLogicalValidator.java](/src/main/java/com/inrip/bank/service/account/AccountLogicalValidator.java) | Java | -1 | 7 | -2 | 4 |
| [src/main/java/com/inrip/bank/service/account/AccountServiceImpl.java](/src/main/java/com/inrip/bank/service/account/AccountServiceImpl.java) | Java | 4 | -1 | -2 | 1 |
| [src/main/java/com/inrip/bank/service/account/AccountTransformer.java](/src/main/java/com/inrip/bank/service/account/AccountTransformer.java) | Java | 1 | 4 | 0 | 5 |
| [src/main/java/com/inrip/bank/service/auth/AuthService.java](/src/main/java/com/inrip/bank/service/auth/AuthService.java) | Java | -4 | -4 | -4 | -12 |
| [src/main/java/com/inrip/bank/service/auth/AuthServiceImpl.java](/src/main/java/com/inrip/bank/service/auth/AuthServiceImpl.java) | Java | -25 | -5 | -9 | -39 |
| [src/main/java/com/inrip/bank/service/auth/SimpleBankAuthService.java](/src/main/java/com/inrip/bank/service/auth/SimpleBankAuthService.java) | Java | 4 | 3 | 4 | 11 |
| [src/main/java/com/inrip/bank/service/auth/SimpleBankAuthServiceImpl.java](/src/main/java/com/inrip/bank/service/auth/SimpleBankAuthServiceImpl.java) | Java | 25 | 4 | 9 | 38 |
| [src/main/java/com/inrip/bank/service/auth/SimpleBankSecurityInterceptor.java](/src/main/java/com/inrip/bank/service/auth/SimpleBankSecurityInterceptor.java) | Java | 45 | 4 | 22 | 71 |
| [src/main/java/com/inrip/bank/service/auth/TransactionSecurityInterceptor.java](/src/main/java/com/inrip/bank/service/auth/TransactionSecurityInterceptor.java) | Java | -47 | -4 | -23 | -74 |
| [src/main/java/com/inrip/bank/service/transactionStatus/TransactionStatusService.java](/src/main/java/com/inrip/bank/service/transactionStatus/TransactionStatusService.java) | Java | -6 | -4 | -3 | -13 |
| [src/main/java/com/inrip/bank/service/transactionStatus/TransactionStatusServiceImpl.java](/src/main/java/com/inrip/bank/service/transactionStatus/TransactionStatusServiceImpl.java) | Java | -70 | -15 | -28 | -113 |
| [src/main/java/com/inrip/bank/service/transaction/TransactionLogicalValidator.java](/src/main/java/com/inrip/bank/service/transaction/TransactionLogicalValidator.java) | Java | -10 | -20 | -6 | -36 |
| [src/main/java/com/inrip/bank/service/transaction/TransactionService.java](/src/main/java/com/inrip/bank/service/transaction/TransactionService.java) | Java | -13 | -4 | -4 | -21 |
| [src/main/java/com/inrip/bank/service/transaction/TransactionServiceImpl.java](/src/main/java/com/inrip/bank/service/transaction/TransactionServiceImpl.java) | Java | -139 | -47 | -41 | -227 |
| [src/main/java/com/inrip/bank/service/transaction/TransactionStatusLogicalValidator.java](/src/main/java/com/inrip/bank/service/transaction/TransactionStatusLogicalValidator.java) | Java | -139 | -71 | -17 | -227 |
| [src/main/java/com/inrip/bank/service/transaction/TransactionTransformer.java](/src/main/java/com/inrip/bank/service/transaction/TransactionTransformer.java) | Java | -33 | -4 | -11 | -48 |
| [src/main/java/com/inrip/bank/service/user/UserService.java](/src/main/java/com/inrip/bank/service/user/UserService.java) | Java | 6 | 0 | 3 | 9 |
| [src/main/java/com/inrip/bank/service/user/UserServiceImpl.java](/src/main/java/com/inrip/bank/service/user/UserServiceImpl.java) | Java | 46 | 0 | 14 | 60 |
| [src/main/resources/application.properties](/src/main/resources/application.properties) | Java Properties | 2 | 2 | 2 | 6 |
| [src/test/java/com/inrip/bank/BankApplicationTestsIT.java](/src/test/java/com/inrip/bank/BankApplicationTestsIT.java) | Java | -465 | -133 | -132 | -730 |
| [src/test/java/com/inrip/bank/SimpleBankApplicationTestsIT.java](/src/test/java/com/inrip/bank/SimpleBankApplicationTestsIT.java) | Java | 620 | 242 | 174 | 1,036 |
| [src/test/java/com/inrip/bank/common/UtilTest.java](/src/test/java/com/inrip/bank/common/UtilTest.java) | Java | 19 | 19 | 5 | 43 |

[Summary](results.md) / [Details](details.md) / [Diff Summary](diff.md) / Diff Details