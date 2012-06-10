package com.salesmanager.core.business.customer.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.salesmanager.core.business.catalog.product.model.review.ProductReview;
import com.salesmanager.core.business.generic.model.SalesManagerEntity;
import com.salesmanager.core.business.reference.country.model.Country;
import com.salesmanager.core.business.reference.zone.model.Zone;
import com.salesmanager.core.constants.SchemaConstant;
import com.salesmanager.core.utils.CloneUtils;

@Entity
@Table(name = "CUSTOMER", schema=SchemaConstant.SALESMANAGER_SCHEMA)
public class Customer extends SalesManagerEntity<Long, Customer> {
	private static final long serialVersionUID = -6966934116557219193L;
	
	@Id
	@Column(name = "CUSTOMER_ID", unique=true, nullable=false)
	@TableGenerator(name = "TABLE_GEN", table = "SM_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT",
	pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
	private Long id;
	
	@Column(name="CUSTOMER_GENDER", length=1, nullable=true)
	private String gender;

	@Column(name="CUSTOMER_FIRSTNAME", length=32, nullable=false)
	private String firstname;
	
	@Column(name="CUSTOMER_LASTNAME", length=32, nullable=false)
	private String lastname;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CUSTOMER_DOB")
	private Date dateOfBirth;
	
	@Column(name="CUSTOMER_EMAIL_ADDRESS", length=96, nullable=false)
	private String emailAddress;
	
	@Column(name="CUSTOMER_NICK", length=96)
	private String nick;
	
	@Column(name="CUSTOMER_TELEPHONE", length=32, nullable=false)
	private String telephone;
	
	@Column(name="CUSTOMER_ADDRESS", length=256, nullable=false)
	private String streetAddress;
	
	@Column(name="CUSTOMER_POSTALCODE", length=20, nullable=false)
	private String postalCode;
	
	@Column(name="CUSTOMER_CITY", length=100, nullable=false)
	private String city;
	
	@Column(name="CUSTOMER_COMPANY", length=100)
	private String company;
	
	@Column(name="CUSTOMER_STATE", length=100)
	private String state;
	
	@Column(name="CUSTOMER_FAX", length=32)
	private String fax;
	
	@Column(name="CUSTOMER_PASSWORD", length=40)
	private String password;
	
	@Column(name="CUSTOMER_NEWSLETTER", length=1)
	private Character newsletter;
	
	@Column(name="CUSTOMER_ANONYMOUS")
	private boolean anonymous;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="COUNTRY_ID", nullable=false)
	private Country country;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="ZONE_ID", nullable=true)
	private Zone zone;
	
	@OneToMany(mappedBy = "customer", targetEntity = ProductReview.class)
	private List<ProductReview> reviews = new ArrayList<ProductReview>();
	
	public Customer() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getDateOfBirth() {
		return CloneUtils.clone(dateOfBirth);
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = CloneUtils.clone(dateOfBirth);
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Character getNewsletter() {
		return newsletter;
	}

	public void setNewsletter(Character newsletter) {
		this.newsletter = newsletter;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public List<ProductReview> getReviews() {
		return reviews;
	}

	public void setReviews(List<ProductReview> reviews) {
		this.reviews = reviews;
	}
}
