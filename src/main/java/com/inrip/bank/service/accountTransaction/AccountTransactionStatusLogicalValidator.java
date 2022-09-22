package com.inrip.bank.service.accountTransaction;

import java.util.Date;
import java.util.Optional;

import org.springframework.util.Assert;

import com.inrip.bank.common.SimpleBankUtils;
import com.inrip.bank.controller.exceptions.SimpleBankHttpAcceptException;
import com.inrip.bank.dto.AccountTransactionStatusRequestDTO;
import com.inrip.bank.dto.AccountTransactionStatusResponseDTO;
import com.inrip.bank.model.AccountTransaction;

/**
 * @author Enrique AC
 *
	Payload:
	{
		"reference":"12345A",
		"channel":"CLIENT"
	}
	reference (mandatory): The transaction reference number
	channel (optional): The type of the channel that is asking for the status. It can be any of these values: CLIENT, ATM, INTERNAL
 * 
 */
public class AccountTransactionStatusLogicalValidator {

	/**
	 * @param AccountTransactionStatusRequestDTO
	 */
	public static void validateStatusRequest(AccountTransactionStatusRequestDTO request) {
		Assert.notNull(request, "Transaction Status Request cannot be null");		
		Assert.notNull(request.getReference(), "Reference is required");

		//channel == null is allowed
		Assert.isTrue( request.getChannel()==null || request.getChannel()!=null && (
													  request.getChannel().equals("CLIENT") ||
													  request.getChannel().equals("ATM") ||
													  request.getChannel().equals("INTERNAL")),
													"Channel is not supported");
		if(request.getChannel()==null) {
			throw new SimpleBankHttpAcceptException("Can't compute channel/status");
		}

	}

	/* A)
     * Given: A transaction that is not stored in our system
     * When: I check the status from any channel
     * Then: The system returns the status 'INVALID'
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_A(AccountTransactionStatusRequestDTO statusRequest, 
	                                                 Optional<AccountTransaction> optTransactionDTO, 
													 boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		if (!optTransactionDTO.isPresent()) {
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "INVALID");
			if(DEBUG_DATA_ON_RESPONSES)
				statusResponse.setDebug("doBusinessRule_A");			
		}
		return statusResponse;
	}

	/* B)
		 *	Given: A transaction that is stored in our system
		 *	When: I check the status from CLIENT or ATM channel
		 *	And the transaction date is before today
		 *	Then: The system returns the status 'SETTLED' And the amount substracting the fee		
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_B(AccountTransactionStatusRequestDTO statusRequest, 
										AccountTransaction transactionDTO, 
										Date todayDate, 
										boolean truncate, 
										boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;

        Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.before(todayDate) && 
		    ( statusRequest.getChannel().equals("CLIENT") || statusRequest.getChannel().equals("ATM") ) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?0:transactionDTO.getFee().doubleValue());
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "SETTLED", 
			                                       Double.valueOf(total) );
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_B");
		}
		return statusResponse;
	}

	/* C)
	*	Given: A transaction that is stored in our system
	*	When: I check the status from INTERNAL channel
	*	And the transaction date is before today
	*	Then: The system returns the status 'SETTLED'
	*	And the amount
	*	And the fee
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_C(AccountTransactionStatusRequestDTO statusRequest, 
									AccountTransaction transactionDTO,
									Date todayDate, 
									boolean truncate,
									boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.before(todayDate) && 
		    (statusRequest.getChannel().equals("INTERNAL")) ) {
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "SETTLED", 
			                                    transactionDTO.getAmount(), transactionDTO.getFee());
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_C");
		}
		return statusResponse;
	}

	/* D)
	* Given: A transaction that is stored in our system
	* When: I check the status from CLIENT or ATM channel
	* 	And the transaction date is equals to today
	* Then: The system returns the status 'PENDING'
	* 	And the amount substracting the fee
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_D(AccountTransactionStatusRequestDTO statusRequest, 
	 									AccountTransaction transactionDTO, 
										Date todayDate, 
										boolean truncate,
										boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.equals(todayDate) &&
		    ( statusRequest.getChannel().equals("CLIENT") || statusRequest.getChannel().equals("ATM")) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?Double.valueOf(0):transactionDTO.getFee()).doubleValue();
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "PENDING", 
			                                       Double.valueOf(total));
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_D");
		}
		return statusResponse;
	}

    /* E)
    * Given: A transaction that is stored in our system
    * When: I check the status from INTERNAL channel
    *   And the transaction date is equals to today
    * Then: The system returns the status 'PENDING'
    *   And the amount
    *   And the fee
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_E(AccountTransactionStatusRequestDTO statusRequest, 
					AccountTransaction transactionDTO, 
					Date todayDate, 
					boolean truncate,
					boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.equals(todayDate) &&
		    ((statusRequest.getChannel().equals("INTERNAL"))) ) {
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "PENDING", 
			                                    transactionDTO.getAmount(), transactionDTO.getFee());
			if(DEBUG_DATA_ON_RESPONSES)  statusResponse.setDebug("doBusinessRule_E");
		}
		return statusResponse;
	}

    /* F)
    * Given: A transaction that is stored in our system
    * When: I check the status from CLIENT channel
    *   And the transaction date is greater than today
    * Then: The system returns the status 'FUTURE'
    *   And the amount substracting the fee
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_F(AccountTransactionStatusRequestDTO statusRequest, 
									AccountTransaction transactionDTO, 
									Date todayDate, 
									boolean truncate,
									boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.after(todayDate) &&
		    ((statusRequest.getChannel().equals("CLIENT"))) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?Double.valueOf(0):transactionDTO.getFee()).doubleValue();
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "FUTURE", 
			                                       Double.valueOf(total));
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_F");
		}
		return statusResponse;
	}

    /* G)
    * Given: A transaction that is stored in our system
    * When: I check the status from ATM channel
    * And the transaction date is greater than today
    * Then: The system returns the status 'PENDING'
    * And the amount substracting the fee
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_G(AccountTransactionStatusRequestDTO statusRequest, 
										AccountTransaction transactionDTO, 
										Date todayDate, 
										boolean truncate,
										boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.after(todayDate) &&
		    ((statusRequest.getChannel().equals("ATM"))) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?Double.valueOf(0):transactionDTO.getFee()).doubleValue();
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "PENDING", 
			                                       Double.valueOf(total));
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_G");
		}
		return statusResponse;
	}

    /* H)
    * Given: A transaction that is stored in our system
    * When: I check the status from INTERNAL channel
    * And the transaction date is greater than today
    * Then: The system returns the status 'FUTURE'
    * And the amount
    * And the fee
	*/
	public static AccountTransactionStatusResponseDTO doBusinessRule_H(AccountTransactionStatusRequestDTO statusRequest, 
										AccountTransaction transactionDTO, 
										Date todayDate, 
										boolean truncate,
										boolean DEBUG_DATA_ON_RESPONSES) {
		AccountTransactionStatusResponseDTO statusResponse = null;
		Date transactionDate = SimpleBankUtils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.after(todayDate) &&
		    ((statusRequest.getChannel().equals("INTERNAL"))) ) {
			statusResponse = new AccountTransactionStatusResponseDTO(statusRequest.getReference(), "FUTURE", 
			                                    transactionDTO.getAmount(), transactionDTO.getFee());
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_H");
		}
		return statusResponse;
	}

	
}
