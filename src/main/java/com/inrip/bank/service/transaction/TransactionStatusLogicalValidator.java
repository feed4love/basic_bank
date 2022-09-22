package com.inrip.bank.service.transaction;

import java.util.Date;
import java.util.Optional;

import org.springframework.util.Assert;

import com.inrip.bank.common.Utils;
import com.inrip.bank.controller.exceptions.HttpAcceptException;
import com.inrip.bank.dto.StatusRequestDTO;
import com.inrip.bank.dto.StatusResponseDTO;
import com.inrip.bank.model.Transaction;

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
public class TransactionStatusLogicalValidator {

	/**
	 * @param StatusRequestDTO
	 */
	public static void validateStatusRequest(StatusRequestDTO request) {
		Assert.notNull(request, "Transaction Status Request cannot be null");		
		Assert.notNull(request.getReference(), "Reference is required");

		//channel == null is allowed
		Assert.isTrue( request.getChannel()==null || request.getChannel()!=null && (
													  request.getChannel().equals("CLIENT") ||
													  request.getChannel().equals("ATM") ||
													  request.getChannel().equals("INTERNAL")),
													"Channel is not supported");
		if(request.getChannel()==null) {
			throw new HttpAcceptException("Can't compute channel/status");
		}

	}

	/* A)
     * Given: A transaction that is not stored in our system
     * When: I check the status from any channel
     * Then: The system returns the status 'INVALID'
	*/
	public static StatusResponseDTO doBusinessRule_A(StatusRequestDTO statusRequest, 
	                                                 Optional<Transaction> optTransactionDTO, 
													 boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		if (!optTransactionDTO.isPresent()) {
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "INVALID");
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
	public static StatusResponseDTO doBusinessRule_B(StatusRequestDTO statusRequest, 
										Transaction transactionDTO, 
										Date todayDate, 
										boolean truncate, 
										boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;

        Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.before(todayDate) && 
		    ( statusRequest.getChannel().equals("CLIENT") || statusRequest.getChannel().equals("ATM") ) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?0:transactionDTO.getFee().doubleValue());
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "SETTLED", 
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
	public static StatusResponseDTO doBusinessRule_C(StatusRequestDTO statusRequest, 
									Transaction transactionDTO,
									Date todayDate, 
									boolean truncate,
									boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.before(todayDate) && 
		    (statusRequest.getChannel().equals("INTERNAL")) ) {
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "SETTLED", 
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
	public static StatusResponseDTO doBusinessRule_D(StatusRequestDTO statusRequest, 
	 									Transaction transactionDTO, 
										Date todayDate, 
										boolean truncate,
										boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.equals(todayDate) &&
		    ( statusRequest.getChannel().equals("CLIENT") || statusRequest.getChannel().equals("ATM")) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?Double.valueOf(0):transactionDTO.getFee()).doubleValue();
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "PENDING", 
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
	public static StatusResponseDTO doBusinessRule_E(StatusRequestDTO statusRequest, 
					Transaction transactionDTO, 
					Date todayDate, 
					boolean truncate,
					boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.equals(todayDate) &&
		    ((statusRequest.getChannel().equals("INTERNAL"))) ) {
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "PENDING", 
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
	public static StatusResponseDTO doBusinessRule_F(StatusRequestDTO statusRequest, 
									Transaction transactionDTO, 
									Date todayDate, 
									boolean truncate,
									boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.after(todayDate) &&
		    ((statusRequest.getChannel().equals("CLIENT"))) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?Double.valueOf(0):transactionDTO.getFee()).doubleValue();
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "FUTURE", 
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
	public static StatusResponseDTO doBusinessRule_G(StatusRequestDTO statusRequest, 
										Transaction transactionDTO, 
										Date todayDate, 
										boolean truncate,
										boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.after(todayDate) &&
		    ((statusRequest.getChannel().equals("ATM"))) ) {
			double total = transactionDTO.getAmount().doubleValue() - (transactionDTO.getFee()==null?Double.valueOf(0):transactionDTO.getFee()).doubleValue();
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "PENDING", 
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
	public static StatusResponseDTO doBusinessRule_H(StatusRequestDTO statusRequest, 
										Transaction transactionDTO, 
										Date todayDate, 
										boolean truncate,
										boolean DEBUG_DATA_ON_RESPONSES) {
		StatusResponseDTO statusResponse = null;
		Date transactionDate = Utils.TransformDateIfExists(transactionDTO.getDate(), truncate);
		if( transactionDate!=null && transactionDate.after(todayDate) &&
		    ((statusRequest.getChannel().equals("INTERNAL"))) ) {
			statusResponse = new StatusResponseDTO(statusRequest.getReference(), "FUTURE", 
			                                    transactionDTO.getAmount(), transactionDTO.getFee());
			if(DEBUG_DATA_ON_RESPONSES) statusResponse.setDebug("doBusinessRule_H");
		}
		return statusResponse;
	}

	
}
