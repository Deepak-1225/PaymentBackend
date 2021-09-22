package com.payment.web.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.payment.web.beans.Customer;
import com.payment.web.repository.CustomerRepository;
@Service
public class CustomerService {
	public CustomerService() {
		// TODO Auto-generated constructor stub
	}
	
	@Autowired
	private CustomerRepository custRepository;
	
	public List<Customer> getCustomers()
	{
		List<Customer> customers = new ArrayList<Customer>();
		this.custRepository.findAll().forEach(cust->customers.add(cust));
		return customers;
	}
	
	public  Customer findCustomerById(String id)
	{
		try {
			Optional<Customer> cust = this.custRepository.findById(id);
			return cust.orElseThrow(()->{
	
				return new EntityNotFoundException("Customer with "+id + " does not exist");
			});
		}catch(IllegalArgumentException e )
		{
			return null;
		}
	}
	
	public boolean sendMoney(Customer senderAcc,double amount) {
		
		String senderAccNo = senderAcc.getCustomerid();
	    senderAcc = this.findCustomerById(senderAccNo);
		if (senderAcc.getClearbalance() > amount || senderAcc.getOverdraftflag() == 1) {
			senderAcc.setClearbalance(senderAcc.getClearbalance()-amount);
			custRepository.save(senderAcc);
			System.out.println("Balance : " + senderAcc.getClearbalance());
			return true;
		}
		return false;
	}
	
}
