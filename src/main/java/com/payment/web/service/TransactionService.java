package com.payment.web.service;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.emitter.ScalarAnalysis;

import com.payment.web.beans.Currency;
import com.payment.web.beans.Customer;
import com.payment.web.beans.Transaction;
import com.payment.web.repository.TransactionRepository;
@Service
public class TransactionService {
	
	@Autowired
	private TransactionRepository transactionRepository;
	@Autowired
	private CustomerService custService;
	
	SdnChecker sc=new SdnChecker();
	
	public List<Transaction> getAllTransactions() {
		List<Transaction> t = new ArrayList<Transaction>();
		t = (List<Transaction>) this.transactionRepository.findAll();
		return t;
	}
	
	
	
	public boolean insertTransaction(Transaction transaction) {
		if (this.transactionRepository.findById(transaction.getTransactionid()).isPresent())
			return false;
		else {
			SdnChecker sc=new SdnChecker();
			try {
				if(sc.checker(transaction.getReceiveraccountholdername())){
					Customer c = transaction.getCustomerid();
					double amount = transaction.getInramount() + (transaction.getInramount() * 0.25);
					if (custService.sendMoney(c, amount)) {
						transaction.setTransferfees(transaction.getInramount()*0.25);
						transaction.setTransferdate(LocalDate.now());
						this.transactionRepository.save(transaction);
						return true;
					} else {
						return false;
					}
				}
				else {
					System.out.println("RECEIVER "+transaction.getReceiveraccountholdername()+
					" IS PRESENT IN SPECIALLY DESIGNATED NATIONALS AND BLOCKEDPERSONS (SDN list)");
					return false;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
	}
	/*
	 * public boolean insertTransaction(String senderid,String receiverid,String
	 * currencyCode,double amount) { Customer sender =
	 * custService.findCustomerById(senderid); double a = amount + (amount * 0.25);
	 * Transaction transaction = new Transaction(); Currency currency =
	 * CurrencyService.getCurrencyByCode(currencyCode); if
	 * (custService.sendMoney(sender, amount)) { transaction.setCustomerid(sender);
	 * transaction.setCurrencycode(currency);
	 * 
	 * transaction.setTransferfees(transaction.getInramount()*0.25);
	 * transaction.setTransferdate(LocalDate.now());
	 * this.transactionRepository.save(transaction); return true; } else { return
	 * false; } }
	 */
		
	}