package entity.payment;

import java.sql.Timestamp;

public class CreditCard {
	private final String cardCode;
	private final String owner;
	private final int cvvCode;
	private final String dateExpired;

	public CreditCard(String cardCode, String owner, int cvvCode, String dateExpired) {
		super();
		this.cardCode = cardCode;
		this.owner = owner;
		this.cvvCode = cvvCode;
		this.dateExpired = dateExpired;
	}
}
