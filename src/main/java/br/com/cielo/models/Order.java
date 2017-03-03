package br.com.cielo.models;


import java.util.Date;

public class Order {

	private String number;

	private Integer value;

	private String currencyType;

	private Date date;

	private String description;

	private String language;

	private String softDescription;

	private String rateShipping;

	private Integer installment;

	private String paymentType;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {

		this.currencyType = currencyType;

	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getSoftDescription() {
		return softDescription;
	}

	public void setSoftDescription(String softDescription) {

		if(softDescription.length() > 13){
			//TODO validar tamanho da descrição curta
		}else {
			this.softDescription = softDescription;
		}
	}

	public String getRateShipping() {
		return rateShipping;
	}

	public void setRateShipping(String rateShipping) {
		this.rateShipping = rateShipping;
	}

	public Integer getInstallment() {
		return installment;
	}

	public void setInstallment(Integer installment) {
		this.installment = installment;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
}
