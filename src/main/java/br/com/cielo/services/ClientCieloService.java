package br.com.cielo.services;


import br.com.cielo.constants.CieloCode;
import br.com.cielo.interfaces.ClientInterface;
import br.com.cielo.models.Card;
import br.com.cielo.models.Transaction;
import br.com.cielo.utils.XML;
import org.glassfish.jersey.client.ClientConfig;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;

public class ClientCieloService implements ClientInterface {

	private final String endPointProduction = "https://ecommerce.cielo.com.br/servicos/ecommwsec.do";
	private final String endPointDevelopment = "https://qasecommerce.cielo.com.br/servicos/ecommwsec.do";


	private String accessKey;

	private String affiliateNumber;

	private String url;

	public ClientCieloService(String accessKey, String affiliateNumber, Boolean development) {
		this.accessKey = accessKey;
		this.affiliateNumber = affiliateNumber;
		this.url = development==true?this.endPointDevelopment:this.endPointProduction;
	}

	@Override
	public HashMap<String, String> createTransation(Transaction transaction){
		Document document = this.send(this.generatorTransationXML(transaction));
		HashMap<String, String> map = new HashMap<String,String>();
		NodeList root = document.getChildNodes();
		if(XML.getNode("erro",root) !=null){
			NodeList error = XML.getNode("erro", root).getChildNodes();

			map.put("status","error");
			map.put("errorCode",XML.getNodeValue("codigo", error));
			map.put("message",CieloCode.valueOfCode(XML.getNodeValue("codigo", error)));
		}else {
			NodeList trans = XML.getNode("transacao", root).getChildNodes();
			map.put("status", "success");
			map.put("message", CieloCode.valueOfCode(XML.getNodeValue("status", trans)));
			map.put("tid", XML.getNodeValue("tid", trans));
		}
		return map;
	}

	@Override
	public HashMap<String, String> consultTransaction(String id) {
		HashMap<String, String> map = new HashMap<String,String>();
		Document document = this.send(this.generatorConsultTransationXML(id));
		//Document document = this.convertStringToXml(xml);
		NodeList root = document.getChildNodes();
		if(XML.getNode("erro",root) !=null){
			NodeList error = XML.getNode("erro", root).getChildNodes();

			map.put("status","error");
			map.put("errorCode",XML.getNodeValue("codigo", error));
			map.put("message",CieloCode.valueOfCode(XML.getNodeValue("codigo", error)));
		}else {
			NodeList transaction = XML.getNode("transacao", root).getChildNodes();
			map.put("status", "success");
			map.put("code", XML.getNodeValue("status", transaction));
			map.put("message", CieloCode.valueOfCode(XML.getNodeValue("status", transaction)));
		}

		return map;
	}

	@Override
	public HashMap<String,String> cancelTransaction(String id) {
		HashMap<String, String> map = new HashMap<String,String>();
		Document document = this.send(this.generatorCancelTransationXML(id));
		NodeList root = document.getChildNodes();
		if(XML.getNode("erro",root) !=null){
			NodeList error = XML.getNode("erro", root).getChildNodes();

			map.put("status","error");
			map.put("errorCode",XML.getNodeValue("codigo", error));
			map.put("message",CieloCode.valueOfCode(XML.getNodeValue("codigo", error)));

		}else {
			map.put("status", "success");
			map.put("message", "Transação cancelada com sucesso");

		}

		return map;
	}

	@Override
	public HashMap<String,String> createToken(Card card) {
		HashMap<String, String> map = new HashMap<String,String>();
		Document document = this.send(this.generatorCreateTokenXML(card));
		NodeList root = document.getChildNodes();
		if(XML.getNode("erro",root) !=null){
			NodeList error = XML.getNode("erro", root).getChildNodes();

			map.put("status","error");
			map.put("errorCode",XML.getNodeValue("codigo", error));
			map.put("message",CieloCode.valueOfCode(XML.getNodeValue("codigo", error)));

		}else {
			NodeList token = XML.getNode("dados-token",XML.getNode("token", XML.getNode("retorno-token", root).getChildNodes()).getChildNodes()).getChildNodes();
			map.put("status", "success");
			map.put("token", XML.getNodeValue("codigo-token", token));
			map.put("card", XML.getNodeValue("numero-cartao-truncado", token));
			map.put("message","Token gerado");

		}

		return map;
	}

	private Document send(String xml){
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget webTarget = client.target(UriBuilder.fromUri(this.url).build());
		String response = webTarget.request().accept(MediaType.APPLICATION_XML).post(Entity.entity(new Form().param("mensagem", xml), MediaType.APPLICATION_FORM_URLENCODED_TYPE), String.class);
		//System.out.println(response);
		return this.convertStringToXml(response);
	}

	public Document convertStringToXml(String xml){
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(xml));

			Document document = db.parse(is);

			return document;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Função que cria o xml para transmissão para a cielo
	 * @param transaction
	 * @return
	 */
	private String generatorTransationXML(Transaction transaction){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try {
			documentBuilder=documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			//Dados integração, informações cliente da cielo
			Element cieloClient = document.createElement("dados-ec");
			cieloClient.appendChild(this.setElement(document,"numero",this.affiliateNumber));
			cieloClient.appendChild(this.setElement(document, "chave", this.accessKey));


			Element client = document.createElement("dados-portador");
			if(transaction.getCard().getToken() != null){
				client.appendChild(this.setElement(document, "token", transaction.getCard().getToken()));
			}else {
				client.appendChild(this.setElement(document, "numero", transaction.getCard().getNumber()));
				client.appendChild(this.setElement(document, "validade", transaction.getCard().getValidate()));
				client.appendChild(this.setElement(document, "indicador", "1"));
				client.appendChild(this.setElement(document, "codigo-seguranca", transaction.getCard().getSecurityCode()));
				//client.appendChild(this.setElement(document, "token", transaction.isGenerateToken().toString()));
			}

			Element order = document.createElement("dados-pedido");
			order.appendChild(this.setElement(document, "numero", transaction.getOrder().getNumber()));
			order.appendChild(this.setElement(document, "valor", transaction.getOrder().getValue().toString()));
			order.appendChild(this.setElement(document, "moeda", transaction.getOrder().getCurrencyType()));
			order.appendChild(this.setElement(document, "data-hora", simpleDateFormat.format(transaction.getOrder().getDate())));
			order.appendChild(this.setElement(document, "descricao", transaction.getOrder().getDescription()));
			order.appendChild(this.setElement(document, "idioma", transaction.getOrder().getLanguage()));
			order.appendChild(this.setElement(document, "soft-descriptor", transaction.getOrder().getSoftDescription()));
			//order.appendChild(this.setElement(document,"taxa-embarque",(transaction.getOrder().getRateShipping()==null)?"":transaction.getOrder().getRateShipping()));

			Element paymentType = document.createElement("forma-pagamento");
			paymentType.appendChild(this.setElement(document, "bandeira", transaction.getCard().getFlag()));
			paymentType.appendChild(this.setElement(document, "produto", transaction.getOrder().getPaymentType()));
			paymentType.appendChild(this.setElement(document, "parcelas", transaction.getOrder().getInstallment().toString()));

			Element transation = document.createElement("requisicao-transacao");
			transation.setAttribute("id", "a97ab62a-7956-41ea-b03f-c2e9f612c293");
			transation.setAttribute("versao", "1.2.1");
			transation.appendChild(cieloClient);
			transation.appendChild(client);
			transation.appendChild(order);
			transation.appendChild(paymentType);
			transation.appendChild(this.setElement(document,"url-retorno",transaction.getReturnUrl()==null?"null":transaction.getReturnUrl()));
			transation.appendChild(this.setElement(document,"autorizar",transaction.getAuthorizationType().toString()));
			transation.appendChild(this.setElement(document,"capturar","false"));
			transation.appendChild(this.setElement(document,"gerar-token",transaction.isGenerateToken().toString()));

			document.appendChild(transation);

			return this.XMLToString(document);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * função cria xml para consulta da transação
	 * @param id
	 * @return
	 */
	private String generatorConsultTransationXML(String id){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

		try {
			documentBuilder=documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			//Dados integração, informações cliente da cielo
			Element cieloClient = document.createElement("dados-ec");
			cieloClient.appendChild(this.setElement(document,"numero",this.affiliateNumber));
			cieloClient.appendChild(this.setElement(document, "chave", this.accessKey));


			Element transation = document.createElement("requisicao-consulta");
			transation.setAttribute("id", "a97ab62a-7956-41ea-b03f-c2e9f612c293");
			transation.setAttribute("versao", "1.2.1");
			transation.appendChild(this.setElement(document,"tid",id));
			transation.appendChild(cieloClient);

			document.appendChild(transation);

			return this.XMLToString(document);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * função cria xml para cancelamento da transação
	 * @param id
	 * @return
	 */
	private String generatorCancelTransationXML(String id){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder=documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			//Dados integração, informações cliente da cielo
			Element cieloClient = document.createElement("dados-ec");
			cieloClient.appendChild(this.setElement(document,"numero",this.affiliateNumber));
			cieloClient.appendChild(this.setElement(document, "chave", this.accessKey));


			Element transation = document.createElement("requisicao-cancelamento");
			transation.setAttribute("id", "a97ab62a-7956-41ea-b03f-c2e9f612c293");
			transation.setAttribute("versao", "1.2.1");
			transation.appendChild(this.setElement(document,"tid",id));
			transation.appendChild(cieloClient);

			document.appendChild(transation);

			return this.XMLToString(document);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * função cria xml para geração de token dos dados do portador
	 * @param card
	 * @return
	 */
	private String generatorCreateTokenXML(Card card){
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;

		try {
			documentBuilder=documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();

			//Dados integração, informações cliente da cielo
			Element cieloClient = document.createElement("dados-ec");
			cieloClient.appendChild(this.setElement(document,"numero",this.affiliateNumber));
			cieloClient.appendChild(this.setElement(document, "chave", this.accessKey));

			//Dados integração, informações cliente da cielo
			Element client = document.createElement("dados-portador");
			client.appendChild(this.setElement(document,"numero",card.getNumber()));
			client.appendChild(this.setElement(document, "validade", card.getValidate()));
			client.appendChild(this.setElement(document, "nome-portador", card.getName()));


			Element transation = document.createElement("requisicao-token");
			transation.setAttribute("id", "a97ab62a-7956-41ea-b03f-c2e9f612c293");
			transation.setAttribute("versao", "1.2.1");
			transation.appendChild(cieloClient);
			transation.appendChild(client);

			document.appendChild(transation);

			return this.XMLToString(document);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// utility method to create text node
	private Node setElement(Document document, String name, String value) {
		Element node = document.createElement(name);
		node.appendChild(document.createTextNode(value));
		return node;
	}

	private String XMLToString(Document document){
		// output DOM XML to console
		Transformer transformer = null;
		String xml = null;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);
			StringWriter writer = new StringWriter();
			transformer.transform(source, new StreamResult(writer));
			xml = writer.getBuffer().toString().replaceAll("\n|\r", "");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return xml;
	}
}
