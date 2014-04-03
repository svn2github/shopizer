package com.salesmanager.core.business.payments.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
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
	public void create(Transaction transaction) throws ServiceException {
		
		//parse JSON string
		String transactionDetails = transaction.toJSONString();
		if(!StringUtils.isBlank(transactionDetails)) {
			transaction.setDetails(transactionDetails);
		}
		
		super.create(transaction);
		
		
	}
	
	@Override
	public List<Transaction> listTransactions(Order order) throws ServiceException {
		
		List<Transaction> transactions = transactionDao.listByOrder(order);
		ObjectMapper mapper = new ObjectMapper();
		for(Transaction transaction : transactions) {
			if(transaction.getTransactionType().name().equals(TransactionType.AUTHORIZE.name())) {
				if(!StringUtils.isBlank(transaction.getDetails())) {
					try {
						@SuppressWarnings("unchecked")
						Map<String,String> objects = mapper.readValue(transaction.getDetails(), Map.class);
						transaction.setTransactionDetails(objects);
					} catch (Exception e) {
						throw new ServiceException(e);
					}
				}
			}
		}
		
		return transactions;
	}

	@Override
	public Transaction getCapturableTransaction(Order order)
			throws ServiceException {
		List<Transaction> transactions = transactionDao.listByOrder(order);
		ObjectMapper mapper = new ObjectMapper();
		for(Transaction transaction : transactions) {
			if(transaction.getTransactionType().name().equals(TransactionType.AUTHORIZE.name())) {
				if(!StringUtils.isBlank(transaction.getDetails())) {
					try {
						@SuppressWarnings("unchecked")
						Map<String,String> objects = mapper.readValue(transaction.getDetails(), Map.class);
						transaction.setTransactionDetails(objects);
					} catch (Exception e) {
						throw new ServiceException(e);
					}
				}
				
				return transaction;
			}
		}
		
		return null;
	}
	
	@Override
	public Transaction getRefundableTransaction(Order order)
		throws ServiceException {
		List<Transaction> transactions = transactionDao.listByOrder(order);
		Map<String,Transaction> finalTransactions = new HashMap<String,Transaction>();
		Transaction finalTransaction = null;
		for(Transaction transaction : transactions) {
			if(transaction.getTransactionType().name().equals(TransactionType.AUTHORIZECAPTURE.name())) {
				finalTransactions.put(TransactionType.AUTHORIZECAPTURE.name(),transaction);
			}
			if(transaction.getTransactionType().name().equals(TransactionType.CAPTURE.name())) {
				finalTransactions.put(TransactionType.CAPTURE.name(),transaction);
			}
			if(transaction.getTransactionType().name().equals(TransactionType.REFUND.name())) {
				finalTransactions.put(TransactionType.REFUND.name(),transaction);
			}
		}
		
		if(finalTransactions.containsKey(TransactionType.AUTHORIZECAPTURE.name())) {
			finalTransaction = finalTransactions.get(TransactionType.AUTHORIZECAPTURE.name());
		}
		
		if(finalTransactions.containsKey(TransactionType.CAPTURE.name())) {
			finalTransaction = finalTransactions.get(TransactionType.CAPTURE.name());
		}

		
		if(finalTransaction!=null && !StringUtils.isBlank(finalTransaction.getDetails())) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				Map<String,String> objects = mapper.readValue(finalTransaction.getDetails(), Map.class);
				finalTransaction.setTransactionDetails(objects);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
		
		return finalTransaction;
	}

}
