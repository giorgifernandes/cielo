package br.com.cielo.interfaces;

import br.com.cielo.models.Card;
import br.com.cielo.models.Transaction;

import java.util.HashMap;

public interface ClientInterface {

	HashMap<String, String> createTransation(Transaction transaction);

	HashMap<String, String> consultTransaction(String id);

	HashMap<String, String> cancelTransaction(String id);

	HashMap<String, String> createToken(Card card);

}
