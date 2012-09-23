package com.salesmanager.core.business.system.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "INTEGRATION_MODULE", schema= SchemaConstant.SALESMANAGER_SCHEMA)
public class IntegrationModule extends SalesManagerEntity<Long, IntegrationModule> implements Serializable, Auditable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -357523134800965997L;

	@Id
	@Column(name = "SYSTEM_NOTIF_ID")
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "INT_MOD_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	
	@Column(name="MODULE", unique=true, columnDefinition = "enum('PAYMENT','SHIPPING')") 
	@Enumerated(EnumType.STRING) 
	private String module;
	
	@Column(name="CODE", nullable=false)
	private String code;
	
	@Column(name="REGIONS")
	private String regions;
	
	@Column(name="CONFIGURATION")
	private String configuration;
	
	@Transient
	private Set<String> regionsSet = new HashSet<String>();
	
	@Transient
	private ModuleConfig moduleConfig = null;
	

	
	@Embedded
	private AuditSection auditSection = new AuditSection();



	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}



	@Override
	public void setAuditSection(AuditSection audit) {
		this.auditSection = audit;
		
	}



	@Override
	public Long getId() {
		return id;
	}



	@Override
	public void setId(Long id) {
		this.id = id;
	}



	public String getModule() {
		return module;
	}



	public void setModule(String module) {
		this.module = module;
	}



	public String getRegions() {
		return regions;
	}



	public void setRegions(String regions) {
		this.regions = regions;
	}



	public String getConfiguration() {
		return configuration;
	}



	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}



	public void setRegionsSet(Set<String> regionsSet) {
		this.regionsSet = regionsSet;
	}



	public Set<String> getRegionsSet() {
		return regionsSet;
	}




	public void setCode(String code) {
		this.code = code;
	}



	public String getCode() {
		return code;
	}



	public void setModuleConfig(ModuleConfig moduleConfig) {
		this.moduleConfig = moduleConfig;
	}



	public ModuleConfig getModuleConfig() {
		return moduleConfig;
	}


}
