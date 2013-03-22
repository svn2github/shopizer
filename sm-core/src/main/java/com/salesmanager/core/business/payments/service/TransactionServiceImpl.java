package com.salesmanager.core.business.payments.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.payments.dao.TransactionDao;
import com.salesmanager.core.business.payments.model.Transaction;

@Service("transactionService")
public class TransactionServiceImpl  extends SalesManagerEntityServiceImpl<Long, Transaction> implements TransactionService {
	
	
	@Autowired
	TransactionDao transactionDao;
	
	public TransactionServiceImpl(TransactionDao transactionDao) {
		super(transactionDao);
		this.transactionDao = transactionDao;
	}

}
