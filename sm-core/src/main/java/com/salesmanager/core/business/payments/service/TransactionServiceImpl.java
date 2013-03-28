package com.salesmanager.core.business.payments.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.order.model.Order;
import com.salesmanager.core.business.payments.dao.TransactionDao;
import com.salesmanager.core.business.payments.model.Transaction;
import com.salesmanager.core.business.payments.model.TransactionType;

@Service("transactionService")
public class TransactionServiceImpl  extends SalesManagerEntityServiceImpl<Long, Transaction> implements TransactionService {
	

	TransactionDao transactionDao;
	
	@Autowired
	public TransactionServiceImpl(TransactionDao transactionDao) {
		super(transactionDao);
		this.transactionDao = transactionDao;
	}

	@Override
	public Transaction getCapturableTransaction(Order order)
			throws ServiceException {
		// TODO Auto-generated method stub
		List<Transaction> transactions = transactionDao.listByOrder(order);
		
		for(Transaction transaction : transactions) {
			if(transaction.getTransactionType().name().equals(TransactionType.AUTHORIZE.name())) {
				return transaction;
			}
		}
		
		return null;
	}

}
