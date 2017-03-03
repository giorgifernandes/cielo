package br.com.cielo.models;

/**
 * Created by giorgi on 23/04/15.
 */
public class Transaction {

	private String id;

	private Card card;

	private Order order;

	private Integer authorizationType;

	private String returnUrl;

	private Boolean generateToken;

	public Transaction() {

		//default not generator token for card in trasaction
		this.generateToken = false;

		//default direct autorization
		this.authorizationType = 3;
	}

	public String getId() {
		return id;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Integer getAuthorizationType() {
		return authorizationType;
	}

	public void setAuthorizationType(Integer authorizationType) {
		this.authorizationType = authorizationType;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public Boolean isGenerateToken() {
		return generateToken;
	}

	public void setGenerateToken(Boolean generateToken) {
		this.generateToken = generateToken;
	}
}
