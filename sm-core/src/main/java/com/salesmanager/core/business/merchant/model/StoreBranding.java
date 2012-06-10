package com.salesmanager.core.business.merchant.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.generic.model.SalesManagerEntity;


@Entity
@Table(name="STORE_BRANDING", schema="SALESMANAGER")
// TODO : create DAO / Service
public class StoreBranding extends SalesManagerEntity<Long, StoreBranding> {
	private static final long serialVersionUID = 2328333658753596988L;
	
	@Id
	@Column(name = "STORE_BRANDING_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "STORE_BRAND_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", nullable=false, updatable=false)
	private MerchantStore merchantSore;

	@Column(name="STORE_LOGO", length=100)
	private String storeLogo;
	
	@Column(name="STORE_TEMPLATE", length=25)
	private String storeTemplate;
	
	@Column(name="INVOICE_TEMPLATE", length=25)
	private String invoiceTemplate;
	
	@Column(name="DOMAIN_NAME", length=80)
	private String domainName;
	
	@Column(name="CONTINUESHOPPINGURL", length=150)
	private String continueshoppingurl;
	
	@Column(name = "STORE_EMAIL", length=60, nullable=false)
	private String storeEmailAddress;
	
	public String getStoreEmailAddress() {
		return storeEmailAddress;
	}

	public void setStoreEmailAddress(String storeEmailAddress) {
		this.storeEmailAddress = storeEmailAddress;
	}

	public StoreBranding(){
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getStoreTemplate() {
		return storeTemplate;
	}

	public void setStoreTemplate(String storeTemplate) {
		this.storeTemplate = storeTemplate;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getContinueshoppingurl() {
		return continueshoppingurl;
	}

	public void setContinueshoppingurl(String continueshoppingurl) {
		this.continueshoppingurl = continueshoppingurl;
	}

	
	public String getInvoiceTemplate() {
		return invoiceTemplate;
	}

	public void setInvoiceTemplate(String invoiceTemplate) {
		this.invoiceTemplate = invoiceTemplate;
	}

	public MerchantStore getMerchantSore() {
		return merchantSore;
	}

	public void setMerchantSore(MerchantStore merchantSore) {
		this.merchantSore = merchantSore;
	}

}
