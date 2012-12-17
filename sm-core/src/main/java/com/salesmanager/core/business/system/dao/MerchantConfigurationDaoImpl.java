package com.salesmanager.core.business.system.dao;

import org.springframework.stereotype.Repository;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.salesmanager.core.business.generic.dao.SalesManagerEntityDaoImpl;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.system.model.MerchantConfiguration;
import com.salesmanager.core.business.system.model.QMerchantConfiguration;

@Repository("merchantConfigurationDao")
public class MerchantConfigurationDaoImpl extends SalesManagerEntityDaoImpl<Long, MerchantConfiguration>
		implements MerchantConfigurationDao {


	public MerchantConfiguration getMerchantConfiguration(String key, MerchantStore store) {
		
		
		
		QMerchantConfiguration qMerchantCnfiguration = QMerchantConfiguration.merchantConfiguration;

		
		JPQLQuery query = new JPAQuery (getEntityManager());
		query.from(qMerchantCnfiguration)
			.innerJoin(qMerchantCnfiguration.merchantStore).fetch()
			.where(qMerchantCnfiguration.merchantStore.id.eq(store.getId())
			.and(qMerchantCnfiguration.key.eq(key)));
		
		return query.uniqueResult(qMerchantCnfiguration);

	}
}
