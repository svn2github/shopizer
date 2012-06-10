package com.salesmanager.core.business.reference.language.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "LANGUAGE", schema="SALESMANAGER")
public class Language extends SalesManagerEntity<Integer, Language> implements Auditable{
	private static final long serialVersionUID = -7676627812941330669L;
	
	@Id
	@Column(name="LANGUAGE_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "LANG_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Integer id;
	
	@Embedded
	private AuditSection auditSection = new AuditSection();

	@Column(name="CODE", nullable = false)
	private String code;
	
	@Column(name="SORT_ORDER")
	private Integer sortOrder;
	
/*	@SuppressWarnings("unused")
	@OneToOne(mappedBy = "defaultLanguage", targetEntity = MerchantStore.class)
	@JoinColumn(name = "STORE_ID")
	private MerchantStore store;*/
	
	public Language() {
	}
	
	public Language(String code) {
		this.setCode(code);
	}
	
	@Override
	public Integer getId() {
		return id;
	}
	
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}
	
	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof Language)) {
			return false;
		} else {
			Language language = (Language) obj;
			return (this.id == language.getId());
		}
	}
	
	@SuppressWarnings("unused")
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "languages")
	private Set<MerchantStore> stores = new HashSet<MerchantStore>();
}
