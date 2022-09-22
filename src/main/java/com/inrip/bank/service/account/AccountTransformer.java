package com.inrip.bank.service.account;

import java.util.ArrayList;
import java.util.List;

import com.inrip.bank.dto.AccountRequestDTO;
import com.inrip.bank.dto.AccountResponseDTO;
import com.inrip.bank.model.Account;

/**
 * @author Enrique AC
 * 
 */
public class AccountTransformer {

	
	public static List<AccountResponseDTO> listAccountToResponseDTO(List<Account> listAccount) {
		List<AccountResponseDTO> listAccountResponseDTO = new ArrayList<AccountResponseDTO>();		
		listAccount.forEach(item -> listAccountResponseDTO.add(AccountTransformer.accountToResponseDTO(item)));
		return listAccountResponseDTO;
	}


	public static Account accountRequestDtoToAccount(AccountRequestDTO accountRequest) {
		Account account = new Account();
		account.setAccountiban(accountRequest.getAccountiban());
		account.setCredit(accountRequest.getCredit());		
		return account;
	}

	public static AccountResponseDTO accountToResponseDTO(Account account) {
		AccountResponseDTO response = AccountResponseDTO.Builder.newInstance()
										.setUid(account.getUid())
										.setAccountiban(account.getAccountiban())
										.setCredit(account.getCredit())
										.build();
		/*AccountResponseDTO response = new AccountResponseDTO();				
		response.setUid(account.getUid());
		response.setAccountiban(account.getAccountiban());
		response.setCredit(account.getCredit());		*/
		return response;
	}
	

}
