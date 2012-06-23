package com.salesmanager.core.business.merchant.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.catalog.product.model.Product;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.merchant.dao.MerchantStoreDao;
import com.salesmanager.core.business.merchant.model.MerchantStore;
import com.salesmanager.core.business.merchant.model.MerchantStore_;

@Service("merchantService")
public class MerchantStoreServiceImpl extends SalesManagerEntityServiceImpl<Integer, MerchantStore> 
		implements MerchantStoreService {
	
	private MerchantStoreDao merchantStoreDao;
	
	@Autowired
	public MerchantStoreServiceImpl(MerchantStoreDao merchantStoreDao) {
		super(merchantStoreDao);
		this.merchantStoreDao = merchantStoreDao;
	}
	
	public MerchantStore getMerchantStore(Integer merchantStoreId) throws ServiceException {
		return merchantStoreDao.getMerchantStore(merchantStoreId);
	}
	
	@Override
	public MerchantStore getMerchantStore(String merchantStoreCode) throws ServiceException {
		return super.getByField(MerchantStore_.code, merchantStoreCode);
	}

	@Override
	public void addProduct(MerchantStore merchantStore, Product product) throws ServiceException {
		//if(!merchantStore.getProducts().contains(product)) {
		//	merchantStore.getProducts().add(product);
		//	update(merchantStore);
		//}
	}

	@Override
	public void addProducts(MerchantStore merchantStore, List<Product> products) throws ServiceException {
		//if(!merchantStore.getProducts().contains(products)) {
			//merchantStore.getProducts().addAll(products);
			//update(merchantStore);
		//}
	}

	@Override
	public void removeProduct(MerchantStore merchantStore, Product product) throws ServiceException {
		//if(merchantStore.getProducts().contains(product)) {
			//merchantStore.getProducts().remove(product);
			//update(merchantStore);
		//}
	}

	@Override
	public void removeProducts(MerchantStore merchantStore, List<Product> products) throws ServiceException {
		//if(merchantStore.getProducts().contains(products)) {
			//merchantStore.getProducts().removeAll(products);
			//update(merchantStore);
		//}
	}
	
	@Override
	public Collection<Product> getProducts(MerchantStore merchantStore) throws ServiceException {
		

		return merchantStoreDao.getProducts(merchantStore);
		
		
	}

	@Override
	public MerchantStore getByCode(String code) {
		return getByField(MerchantStore_.code, code);
	}

}
