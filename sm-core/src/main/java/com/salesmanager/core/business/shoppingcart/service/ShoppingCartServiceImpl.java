package com.salesmanager.core.business.shoppingcart.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salesmanager.core.business.customer.model.Customer;
import com.salesmanager.core.business.generic.exception.ServiceException;
import com.salesmanager.core.business.generic.service.SalesManagerEntityServiceImpl;
import com.salesmanager.core.business.shoppingcart.dao.ShoppingCartDao;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCart;
import com.salesmanager.core.business.shoppingcart.model.ShoppingCartItem;

@Service("shoppingCartService")
public class ShoppingCartServiceImpl extends SalesManagerEntityServiceImpl<Long, ShoppingCart> implements ShoppingCartService {

	@Autowired
	public ShoppingCartServiceImpl(
			ShoppingCartDao shoppingCartDao) {
		super(shoppingCartDao);

	}
	
	
	/**
	 * Retrieve a {@link ShoppingCart} cart for a given customer
	 */
	public ShoppingCart getShoppingCart(Customer customer) throws ServiceException {
		return null;
	}
	
	/**
	 * Save or update a {@link ShoppingCart} for a given customer
	 */
	public void saveOrUpdate(ShoppingCart shoppingCart) throws ServiceException {
		if(shoppingCart.getId()==null || shoppingCart.getId().longValue()==0) {
			super.create(shoppingCart);
		} else {
			super.update(shoppingCart);
		}
	}
	
	
	
	
	public void calculateShoppingCart(List<ShoppingCartItem> items) throws ServiceException {
		
		
/*		the model should be completed so that a shopping cart has this structure
		
		ShoppingCart
			List<ShoppingCartItem> items
			List<ShoppingCartPriceItem> priceItems
			
		only ShoppingCartItem are saved in the database when a customer is logged on
		
		
		This method should return a ShoppingCart entity
		
		
		- price calculation
		
		the cart price calculation is based on a workflow. The workflow takes the ShoppingCart as input and populates the ShoppingCartPriceItem
		based on different calculation steps. The steps are individual item price calculation, sub-total, apply tax (when customer is known), eventually apply
		custom pricing rules (shopping cart coupons) and other specific rule that can be configured on business rules engine such as drools
		
		STEP 1 Unit price item calculation
		
		:get the Product from ShoppingDartItem and all ProductAttribute fromShoppingCartAttributeItem
		
		:invoke for each product productPriceUtils.getFinalOrderPrice
		
		:get the final price for each Product
		
		:set the price in shoppingCartItem.itemPrice
		
		STEP 2 Calculate cart sub total
		
		:calculate sub total based on each shoppingCartItem.itemPrice, create a ShoppingCartPriceItem that will contain this sub total
		
		STEP 3 Calculate taxes (if the customer is logged on)
		
		STEP 4 Calculate Cart total
		
		
		WORKFLOW example (package name may not be accurate)
		
		<beans:bean id="shoppingCartWorkflow" class="com.salesmanager.core.service.shoppingcart.workflow.WorkflowProcessor">
			<beans:property name="processes">
				<beans:list>
					<beans:ref bean="a" />
					<beans:ref bean="b" />
					<beans:ref bean="c" />
				</beans:list>
			</beans:property>
		</beans:bean>
		


		<beans:bean id="a" class="com.salesmanager.core.service.shoppingcart.workflow.CalculateThis"/>
		<beans:bean id="b" class="com.salesmanager.core.service.shoppingcart.workflow.CalculateThat"/>
		<beans:bean id="c" class="com.salesmanager.core.service.shoppingcart.workflow..."/>

		In the shopping cart service autowire the workflow processor which implements execute
		
		
		*/
		
		
		
		
	}
	
	


}
