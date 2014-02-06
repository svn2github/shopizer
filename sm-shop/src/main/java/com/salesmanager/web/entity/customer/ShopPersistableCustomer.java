package com.salesmanager.web.entity.customer;

public class ShopPersistableCustomer extends PersistableCustomer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recaptcha_challenge_field;
	private String recaptcha_response_field;
	
	private String checkPassword;
    public String getRecaptcha_challenge_field()
    {
        return recaptcha_challenge_field;
    }
    public void setRecaptcha_challenge_field( final String recaptcha_challenge_field )
    {
        this.recaptcha_challenge_field = recaptcha_challenge_field;
    }
    public String getRecaptcha_response_field()
    {
        return recaptcha_response_field;
    }
    public void setRecaptcha_response_field( final String recaptcha_response_field )
    {
        this.recaptcha_response_field = recaptcha_response_field;
    }
	public String getCheckPassword() {
		return checkPassword;
	}
	public void setCheckPassword(String checkPassword) {
		this.checkPassword = checkPassword;
	}


}
