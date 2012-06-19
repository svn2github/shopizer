package com.salesmanager.core.business.catalog.category.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.salesmanager.core.business.common.model.audit.AuditListener;
import com.salesmanager.core.business.common.model.audit.AuditSection;
import com.salesmanager.core.business.common.model.audit.Auditable;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.merchant.model.MerchantStore;

@Entity
@EntityListeners(value = AuditListener.class)
@Table(name = "CATEGORY", schema="SALESMANAGER")
public class Category extends SalesManagerEntity<Long, Category> implements Auditable {
	private static final long serialVersionUID = -846291242449186747L;
	
	@Id
	@Column(name = "CATEGORY_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CATEGORY_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;

	@Embedded
	private AuditSection auditSection = new AuditSection();

	@OneToMany(mappedBy="category", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CategoryDescription> descriptions = new ArrayList<CategoryDescription>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="MERCHANT_ID", nullable=false)
	private MerchantStore merchantSore;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private Category parent;
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<Category> categories = new ArrayList<Category>();
	
	@Column(name = "CATEGORY_IMAGE", length=100)
	private String categoryImage;

	@Column(name = "SORT_ORDER")
	private Integer sortOrder = 0;

	@Column(name = "CATEGORY_STATUS")
	private Boolean categoryStatus;

	@Column(name = "VISIBLE")
	private Boolean visible;

	@Column(name = "DEPTH")
	private Integer depth;

	@Column(name = "LINEAGE")
	private String lineage;

	public Category() {
	}
	
	public Category(MerchantStore store) {
		this.merchantSore = store;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}
	
	@Override
	public AuditSection getAuditSection() {
		return auditSection;
	}
	
	@Override
	public void setAuditSection(AuditSection auditSection) {
		this.auditSection = auditSection;
	}

	public List<CategoryDescription> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<CategoryDescription> descriptions) {
		this.descriptions = descriptions;
	}

	public String getCategoryImage() {
		return categoryImage;
	}

	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Boolean isCategoryStatus() {
		return categoryStatus;
	}

	public void setCategoryStatus(Boolean categoryStatus) {
		this.categoryStatus = categoryStatus;
	}

	public Boolean isVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public Integer getDepth() {
		return depth;
	}

	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public String getLineage() {
		return lineage;
	}

	public void setLineage(String lineage) {
		this.lineage = lineage;
	}

	public Category getParent() {
		return parent;
	}

	public void setParent(Category parent) {
		this.parent = parent;
	}
	



	public MerchantStore getMerchantSore() {
		return merchantSore;
	}

	public void setMerchantSore(MerchantStore merchantSore) {
		this.merchantSore = merchantSore;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
