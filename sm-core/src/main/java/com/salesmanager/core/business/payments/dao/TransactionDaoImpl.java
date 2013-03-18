package com.salesmanager.core.business.payments.dao;

import org.springframework.stereotype.Repository;

import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.payments.model.Transaction;

@Repository("transactionDao")
public class TransactionDaoImpl extends SalesManagerEntityDaoImpl<Long, Transaction>
		implements TransactionDao {


}
