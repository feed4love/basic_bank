package com.inrip.bank;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inrip.bank.common.RequestMappings;
import com.inrip.bank.common.UtilTest;
import com.inrip.bank.controller.TransactionController;
import com.inrip.bank.dto.StatusRequestDTO;
import com.inrip.bank.dto.TransactionRequestDTO;
import com.inrip.bank.model.Account;
import com.inrip.bank.model.Transaction;
import com.inrip.bank.repository.AccountRepository;
import com.inrip.bank.repository.TransactionRepository;
import com.inrip.bank.service.transaction.TransactionService;
import com.inrip.bank.service.transaction.TransactionTransformer;
import com.inrip.bank.service.transactionStatus.TransactionStatusService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import static com.inrip.bank.common.UtilTest.TRANSACTION_WHEN.NO_DATE;
import static com.inrip.bank.common.UtilTest.TRANSACTION_WHEN.TOMORROW;
import static com.inrip.bank.common.UtilTest.TRANSACTION_WHEN.TODAY;
import static com.inrip.bank.common.UtilTest.TRANSACTION_WHEN.YESTERDAY;
import static com.inrip.bank.common.UtilTest.TRANSACTION_CHANNEL.INTERNAL;
import static com.inrip.bank.common.UtilTest.TRANSACTION_CHANNEL.CLIENT;
import static com.inrip.bank.common.UtilTest.TRANSACTION_CHANNEL.ATM;

import static com.inrip.bank.common.UtilTest.TRANSACTION_STATUS.INVALID;
import static com.inrip.bank.common.UtilTest.TRANSACTION_STATUS.SETTLED;
import static com.inrip.bank.common.UtilTest.TRANSACTION_STATUS.PENDING;
import static com.inrip.bank.common.UtilTest.TRANSACTION_STATUS.FUTURE;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
class BankApplicationTestsIT {

    @Autowired
	private WebApplicationContext webApplicationContext;
    
    @Value("${bank.basic.message.alive}")
	private String REST_RUNNING;

	MockMvc mMockMvc;

    @Mock
    private TransactionService mTransactionService;

    @Mock
    private TransactionStatusService mTransactionStatusService;

    @MockBean
    private TransactionRepository mTransactionRepository;

    @MockBean
    private AccountRepository mAccountRepository;


    private ObjectMapper mObjectMapper;

    @InjectMocks
    private TransactionController mTransactionController;

    @BeforeAll
    public void init(){        
        MockitoAnnotations.initMocks(this);
        mMockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @BeforeEach
    public void setup(){
        mObjectMapper = new ObjectMapper();        
    }

    @Test
	public void initialTest() {		
		List<String> mockList = mock(List.class);
		when(mockList.size()).thenReturn(5);
        assertTrue(mockList.size()==5);
	}

    /*
     * Test Case - test_service_is_running
     * Expected: 400 OK
     */
    @Test
    public void test_service_is_running() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.SERVICE_STATUS;        
        MvcResult result = this.mMockMvc.perform(get(url)
                               .contentType(MediaType.TEXT_PLAIN_VALUE) 
                               .with(httpBasic("test", "1234")) )
                               .andExpect(status().isOk())                               
                               .andReturn();
        String responseString = result.getResponse().getContentAsString();
        assertTrue(responseString.equals(REST_RUNNING));
    }


    /*
     * Test Case - test_add_transaction
     * Expected: 400 OK
     * Description:
     *      create a transaction providing all mandatory data
     *      and check if saved retreiving it
     */
    @Test
    public void test_add_transaction() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();
        Transaction transactionResponse = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        Account account = new Account();
        account.setAccountiban(transactionRequestDTO.getAccount_iban());
        account.setCredit(Double.valueOf(10000000.1632));

        when(mTransactionRepository.save(any())).thenReturn(transactionResponse);        
        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(account));
        when(mAccountRepository.save(any())).thenReturn(account);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.reference").value(transactionRequestDTO.getReference()))
                     .andExpect(jsonPath("$.account_iban").value(transactionRequestDTO.getAccount_iban()))
                     .andExpect(jsonPath("$.amount").value(transactionRequestDTO.getAmount()))
                     .andExpect(jsonPath("$.fee").value(transactionRequestDTO.getFee()));

    }

    /*
     * Test Case - test_add_transaction_reference_is_duplicated
     * Expected: error
     * Description:
     *      create a transaction with a reference already is stored in the system
     */
    @Test
    public void test_add_transaction_reference_is_duplicated() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();        
        Transaction transactionResponse = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);
        
        Account account = new Account();
        account.setAccountiban(transactionRequestDTO.getAccount_iban());
        account.setCredit(Double.valueOf(10000000.1632));

        when(mTransactionRepository.findByReference(transactionRequestDTO.getReference())).thenReturn(Optional.of(transactionResponse));
        when(mTransactionRepository.save(any())).thenReturn(transactionResponse);
        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(account));
        when(mAccountRepository.save(any())).thenReturn(account);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isBadRequest());
    }


    /*
     * Test Case - test_add_transaction_no_account_iban
     * Expected: error
     * Description:
     *      create a transaction with no account_iban
     */
    @Test
    public void test_add_transaction_no_account_iban() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();        
        transactionRequestDTO.setAccount_iban(null);
        Transaction transactionResponse = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        when(mTransactionRepository.save(any())).thenReturn(transactionResponse);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isBadRequest());

    }
    
    /*
     * Test Case - test_add_transaction_no_reference_and_is_generated
     * Expected: error
     * Description:
     *      create a transaction with no reference, the system assign and return the reference
     */
    @Test
    public void test_add_transaction_no_reference_and_is_generated() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();        
        transactionRequestDTO.setReference(null);
        Transaction transactionResponse = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);
        
        Account account = new Account();
        account.setAccountiban(transactionRequestDTO.getAccount_iban());
        account.setCredit(Double.valueOf(10000000.1632));

        when(mTransactionRepository.save(any())).thenReturn(transactionResponse);
        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(account));
        when(mAccountRepository.save(any())).thenReturn(account);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isOk())
                     .andExpect(jsonPath("$.reference").isNotEmpty())
                     ;

    }

    /*
     * Extra: find by reference, test DISABLE for debug purpouses
     */
    /*@Test
    public void test_load_transaction_by_reference() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.SEARCH_BY_REFERENCE;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();
        Transaction transaction = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        List<Transaction> listTransaction = new ArrayList<Transaction>();
        listTransaction.add(transaction);

        when(mTransactionRepository.findByReference(transactionRequestDTO.getReference(), Sort.by(Direction.ASC, "id")))
                                   .thenReturn(listTransaction);

        this.mMockMvc.perform(get(url,transactionRequestDTO.getReference())
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON) )
                     .andExpect(status().isOk())
                     .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                     .andExpect(jsonPath("$.*").value(hasSize(1)))
                     .andExpect(jsonPath("$.size()", is(1)))
                     .andExpect(jsonPath("$.[0].reference").value(transactionRequestDTO.getReference()));
    }*/
   
    /*
     * Test Case A - test_case_status_ruleA
     * Given: A transaction that is not stored in our system
     * When: I check the status from any channel
     * Then: The system returns the status 'INVALID'* 
     * 
     */
    @Test
    public void test_case_status_ruleA() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;
        StatusRequestDTO statusRequestDTO = null;
        
        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);
        this.mMockMvc.perform(get(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                                           
                     .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                     .andExpect(status().isOk())
                     .andExpect(content().contentTypeCompatibleWith("application/json"))
                     .andExpect(jsonPath("reference").value(statusRequestDTO.getReference()))
                     .andExpect(jsonPath("status").value(is(INVALID.get()))
                      ).andReturn();

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);
        this.mMockMvc.perform(get(url)
                    .with(httpBasic("test", "1234"))
                    .contentType(MediaType.APPLICATION_JSON)                                           
                    .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith("application/json"))
                    .andExpect(jsonPath("reference").value(statusRequestDTO.getReference()))
                    .andExpect(jsonPath("status").value(is(INVALID.get()))
                    ).andReturn();

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(statusRequestDTO.getReference()))
                        .andExpect(jsonPath("status").value(is(INVALID.get()))
                        ).andReturn();
                                
    }

    /*
     * Test Case B - test_case_status_ruleB
     *	Given: A transaction that is stored in our system
     *	When: I check the status from CLIENT or ATM channel
     *	And the transaction date is before today
     *	Then: The system returns the status 'SETTLED' And the amount substracting the fee		
     * 
     */
    @Test
    public void test_case_status_ruleB() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(YESTERDAY, 
        true, Double.valueOf(10), Double.valueOf(2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);
        
        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(SETTLED.get())
                        ).andReturn();

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);  
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(SETTLED.get())
                        ).andReturn();
                                        
    }

    /*
     * Test Case B2 - test_case_status_ruleB2_field_fee_is_null
     *	Given: A transaction that is stored in our system
     *	When: fee field is null
     *	Then: The system returns the status 'SETTLED' And the amount substracting the fee		
     * 
     */
    @Test
    public void test_case_status_ruleB2_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(YESTERDAY, 
                                                        true, 
                                                        Double.valueOf(10), 
                                                        null);

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);
        
        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(SETTLED.get()));

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);  
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(SETTLED.get()));
                                        
    }

    /*
     * Test Case C - test_case_status_ruleC
	 *	Given: A transaction that is stored in our system
	 *	When: I check the status from INTERNAL channel
	 *	And the transaction date is before today
	 *	Then: The system returns the status 'SETTLED'
 	 *	And the amount
 	 *	And the fee
     * 
     */
    @Test
    public void test_case_status_ruleC() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(YESTERDAY, 
                    true,Double.valueOf( 10), Double.valueOf(2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(SETTLED.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount()))
                        .andExpect(jsonPath("fee").value(transaction.get().getFee()))
                        .andReturn();
    }

        /*
     * Test Case C2 - test_case_status_ruleC2_field_fee_is_null
	 *	Given: A transaction that is stored in our system
	 *	When: I check the status from INTERNAL channel
	 *	And the transaction date is before today
	 *	Then: The system returns the status 'SETTLED'
 	 *	And the amount
 	 *	And the fee
     * 
     */
    @Test
    public void test_case_status_ruleC2_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(YESTERDAY, 
                    true,Double.valueOf( 10), null);

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(SETTLED.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount()))                                                
                        .andReturn();
    }


    /*
     * Test Case D - test_case_status_ruleD
	 * Given: A transaction that is stored in our system
	 * When: I check the status from CLIENT or ATM channel
	 * 	And the transaction date is equals to today
 	 * Then: The system returns the status 'PENDING'
 	 * 	And the amount substracting the fee
     * 
     */
    @Test
    public void test_case_status_ruleD() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TODAY, 
                                true, Double.valueOf(10),Double.valueOf( 2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);        
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() - transaction.get().getFee().doubleValue()) )
                        .andReturn();

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() - transaction.get().getFee().doubleValue() ))
                        .andReturn();                        
    }

        /*
     * Test Case D2 - test_case_status_ruleD2_field_fee_is_null
	 * Given: A transaction that is stored in our system
	 * When: I check the status from CLIENT or ATM channel
	 * 	And the transaction date is equals to today
 	 * Then: The system returns the status 'PENDING'
 	 * 	And the amount substracting the fee
     * 
     */
    @Test
    public void test_case_status_ruleD2_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TODAY, 
                                true, Double.valueOf(10), null);

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);        
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() 
                                                            - (transaction.get().getFee()==null?Double.valueOf(0):transaction.get().getFee()).doubleValue()) )
                        .andReturn();

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() 
                                                            - (transaction.get().getFee()==null?Double.valueOf(0):transaction.get().getFee()).doubleValue()) )
                        .andReturn();                        
    }

    /*
     * Test Case E - test_case_status_ruleE
     * Given: A transaction that is stored in our system
     * When: I check the status from INTERNAL channel
     *   And the transaction date is equals to today
     * Then: The system returns the status 'PENDING'
     *   And the amount
     *   And the fee
     * 
     */
    @Test
    public void test_case_status_ruleE() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TODAY, 
                                true,Double.valueOf( 10),Double.valueOf( 2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);        
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount() ))
                        .andExpect(jsonPath("fee").value(transaction.get().getFee() ))
                        .andReturn();

    }

        /*
     * Test Case E2 - test_case_status_ruleE2_field_fee_is_null
     * Given: A transaction that is stored in our system
     * When: I check the status from INTERNAL channel
     *   And the transaction date is equals to today
     * Then: The system returns the status 'PENDING'
     *   And the amount
     *   And the fee
     * 
     */
    @Test
    public void test_case_status_ruleE2_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TODAY, 
                                true,Double.valueOf( 10),null);

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);        
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount() ))                        
                        .andReturn();

    }

    /*
     * Test Case F - test_case_status_ruleF
     * Given: A transaction that is stored in our system
     * When: I check the status from CLIENT channel
     *   And the transaction date is greater than today
     * Then: The system returns the status 'FUTURE'
     *   And the amount substracting the fee
     * 
     */
    @Test
    public void test_case_status_ruleF() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TOMORROW, 
                                                true,Double.valueOf( 10),Double.valueOf( 2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(FUTURE.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() - transaction.get().getFee().doubleValue() ))                
                        .andReturn();

    }

        /*
     * Test Case F2 - test_case_status_ruleF2_field_fee_is_null
     * Given: A transaction that is stored in our system
     * When: I check the status from CLIENT channel
     *   And the transaction date is greater than today
     * Then: The system returns the status 'FUTURE'
     *   And the amount substracting the fee
     * 
     */
    @Test
    public void test_case_status_ruleF2_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TOMORROW, 
                                                true,Double.valueOf( 10), null);

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(CLIENT);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(FUTURE.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() 
                                                            - (transaction.get().getFee()==null?Double.valueOf(0):transaction.get().getFee().doubleValue() )))
                        .andReturn();

    }

    /*
    * Test Case G - test_case_status_ruleG
    * Given: A transaction that is stored in our system
    * When: I check the status from ATM channel
    * And the transaction date is greater than today
    * Then: The system returns the status 'PENDING'
    * And the amount substracting the fee
    * 
    */
    @Test
    public void test_case_status_ruleG() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TOMORROW, 
                                    true, Double.valueOf(10), Double.valueOf(2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() - transaction.get().getFee().doubleValue() ))                
                        .andReturn();

    }

    /*
    * Test Case G2 - test_case_status_ruleG_field_fee_is_null
    * Given: A transaction that is stored in our system
    * When: I check the status from ATM channel
    * And the transaction date is greater than today
    * Then: The system returns the status 'PENDING'
    * And the amount substracting the fee
    * 
    */
    @Test
    public void test_case_status_ruleG_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TOMORROW, 
                                    true, Double.valueOf(10), null);

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(ATM);
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(PENDING.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount().doubleValue() 
                                                        - (transaction.get().getFee()==null?Double.valueOf(0):transaction.get().getFee()).doubleValue() ))
                        .andReturn();

    }

    

    /*
    * Test Case H - test_case_status_ruleH
    * Given: A transaction that is stored in our system
    * When: I check the status from INTERNAL channel
    * And the transaction date is greater than today
    * Then: The system returns the status 'FUTURE'
    * And the amount
    * And the fee
    */
    @Test
    public void test_case_status_ruleH() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TOMORROW, 
                                            true, Double.valueOf(10), Double.valueOf(2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);  
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(FUTURE.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount() ))
                        .andExpect(jsonPath("fee").value(transaction.get().getFee() ))
                        .andReturn();

    }    

        /*
    * Test Case H2 - test_case_status_ruleH2_field_fee_is_null
    * Given: A transaction that is stored in our system
    * When: I check the status from INTERNAL channel
    * And the transaction date is greater than today
    * Then: The system returns the status 'FUTURE'
    * And the amount
    * And the fee
    */
    @Test
    public void test_case_status_ruleH2_field_fee_is_null() throws Exception {
        StatusRequestDTO statusRequestDTO = null;
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.TRANSACTION_STATUS;

        Optional<Transaction> transaction = UtilTest.getFakeOptionalTransaction(TOMORROW, 
                                            true, Double.valueOf(10), Double.valueOf(2));

        when(mTransactionRepository.findByReference(transaction.get().getReference())).thenReturn(transaction);

        statusRequestDTO = UtilTest.getFakeStatusRequestDTO(INTERNAL);  
        this.mMockMvc.perform(get(url)
                        .with(httpBasic("test", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)                                           
                        .content(mObjectMapper.writeValueAsString(statusRequestDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith("application/json"))
                        .andExpect(jsonPath("reference").value(transaction.get().getReference()))
                        .andExpect(jsonPath("status").value(FUTURE.get()))
                        .andExpect(jsonPath("amount").value(transaction.get().getAmount() ))
                        .andReturn();

    }    


    /*
     * Test Case - test_case_credit_after_transaction_is_posivite
     */
    @Test
    public void test_case_credit_after_transaction_is_posivite() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();
        transactionRequestDTO.setReference(null);
        Transaction mockTransaction = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        Account mockAccount = new Account();
        mockAccount.setAccountiban(transactionRequestDTO.getAccount_iban());

        /*
         *   MAIN FUNTION DATA PARAMETERS FOR RESULT
         */
        transactionRequestDTO.setAmount   (Double.valueOf(10));
        transactionRequestDTO.setFee   (Double.valueOf(2));        
        mockAccount.setCredit    (Double.valueOf(0));
        mockTransaction.setAmount(Double.valueOf(10));
        mockTransaction.setFee   (Double.valueOf(2));

        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(mockAccount));
        when(mAccountRepository.save(any())).thenReturn(mockAccount);
        when(mTransactionRepository.save(any())).thenReturn(mockTransaction);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isOk());


    }

    /*
     * Test Case - test_case_credit_after_transaction_is_negative
	 * IMPORTANT to note that a transaction that leaves the total account balance bellow
     *   0 is not allowed.
     */
    @Test
    public void test_case_credit_after_transaction_is_negative() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();
        transactionRequestDTO.setReference(null);
        Transaction mockTransaction = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        Account mockAccount = new Account();
        

        /*
         *   MAIN FUNTION DATA PARAMETERS FOR RESULT
         */
        transactionRequestDTO.setAmount   (Double.valueOf(-10));
        transactionRequestDTO.setFee   (Double.valueOf(2));
        mockTransaction.setAmount(Double.valueOf(-10));        
        mockTransaction.setFee   (Double.valueOf(2));
        mockAccount.setAccountiban(transactionRequestDTO.getAccount_iban());
        mockAccount.setCredit    (Double.valueOf(10));

        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(mockAccount));
        when(mAccountRepository.save(any())).thenReturn(mockAccount);
        when(mTransactionRepository.save(any())).thenReturn(mockTransaction);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isBadRequest());


    }

    /*
     * Test Case - test_case_credit_after_transaction_is_zero
	 * IMPORTANT to note that a transaction that leaves the total account balance bellow
     *   0 is not allowed.
     */
    @Test
    public void test_case_credit_after_transaction_is_zero() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();
        transactionRequestDTO.setReference(null);
        Transaction transaction = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        Account account = new Account();
        account.setAccountiban(transactionRequestDTO.getAccount_iban());

        /*
         *   MAIN FUNTION DATA PARAMETERS FOR RESULT
         */
        transactionRequestDTO.setAmount   (Double.valueOf(-8));
        transactionRequestDTO.setFee   (Double.valueOf(2));
        account.setCredit    (Double.valueOf(10));
        transaction.setAmount(Double.valueOf(-8));
        transaction.setFee   (Double.valueOf(2));

        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(account));
        when(mAccountRepository.save(any())).thenReturn(account);
        when(mTransactionRepository.save(any())).thenReturn(transaction);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isOk());

    }


        /*
     * Test Case - test_case_total_result_credit_nagetive_fee_and_amount
	 * IMPORTANT to note that a transaction that leaves the total account balance bellow
     *   0 is not allowed.
     */
    @Test
    public void test_case_total_result_credit_nagetive_fee_and_amount() throws Exception {
        String url = RequestMappings.REQUEST_CONTEXT + RequestMappings.ADD_TRANSACTION;

        TransactionRequestDTO transactionRequestDTO =  UtilTest.getFakeTransactionRequestDTO();
        transactionRequestDTO.setReference(null);
        Transaction transaction = TransactionTransformer.transactionRequestDtoToTransaction(transactionRequestDTO);

        Account account = new Account();
        account.setAccountiban(transactionRequestDTO.getAccount_iban());

        /*
         *   MAIN FUNTION DATA PARAMETERS FOR RESULT
         */
        transactionRequestDTO.setAmount   (Double.valueOf(-10));
        transactionRequestDTO.setFee   (Double.valueOf(-10));
        account.setCredit    (Double.valueOf(0));
        transaction.setAmount(Double.valueOf(-10));
        transaction.setFee   (Double.valueOf(-10));

        when(mAccountRepository.findOneByAccountiban(any())).thenReturn(Optional.of(account));
        when(mAccountRepository.save(any())).thenReturn(account);
        when(mTransactionRepository.save(any())).thenReturn(transaction);

        this.mMockMvc.perform(post(url)
                     .with(httpBasic("test", "1234"))
                     .contentType(MediaType.APPLICATION_JSON)                      
                     .content(mObjectMapper.writeValueAsString(transactionRequestDTO)))                     
                     .andExpect(status().isOk());

    }

}
