package com.salesmanager.core.business.tax.model.taxclass;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.salesmanager.core.business.common.model.Description;
import com.salesmanager.core.constants.SchemaConstant;

@Entity
@Table(name = "TAX_CLASS_DESC", schema=SchemaConstant.SALESMANAGER_SCHEMA, uniqueConstraints={
	@UniqueConstraint(columnNames={
			"TAX_CLASS_ID",
			"LANGUAGE_ID"
		})
	}
)
public class TaxClassDescription extends Description {
	private static final long serialVersionUID = 4393485617951636829L;
	
	@ManyToOne(targetEntity = TaxClass.class)
	@JoinColumn(name = "TAX_CLASS_ID")
	private TaxClass taxClass;
	
	public TaxClassDescription() {
	}
	
	public TaxClass getTaxClass() {
		return taxClass;
	}

	public void setTaxClass(TaxClass taxClass) {
		this.taxClass = taxClass;
	}
}
