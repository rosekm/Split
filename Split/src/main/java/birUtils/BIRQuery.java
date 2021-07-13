package birUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import birUtils.BIRMessageBuilder.ActionType;
import utils.DialogUtils;

/*
 * Class  responsible for getting query results from BIR System
 */
public class BIRQuery {

	private SOAPMessage queryMessage;
	private SOAPMessage responseMessage;
	private Document responseDataDoc;

	public BIRQuery() {}

	public void querySingleCompany(BIRQueryParameters queryParameters) {
		try {
			BIRMessageBuilder.setQueryParameters(queryParameters);
			queryMessage = BIRMessageBuilder.getSOAPMessage(ActionType.QUERY_SINGLE_COMPANY);
			
			responseMessage = connectAndGetResponse();
			responseDataDoc = extractResponseMessageData();

		} catch (SOAPException e) {
			DialogUtils.errorDialog(e.getMessage());
			e.printStackTrace();
		}

	}

	private SOAPMessage connectAndGetResponse() {
		SOAPConnection connection;
		try {
			connection = SOAPConnectionFactory.newInstance().createConnection();
			return connection.call(queryMessage,
					"https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc");
		} catch (UnsupportedOperationException | SOAPException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Document extractResponseMessageData() {
		SOAPEnvelope env;
		if (responseMessage != null) {
			try {
				env = responseMessage.getSOAPPart().getEnvelope();
				NodeList nl = env.getElementsByTagName("DaneSzukajPodmiotyResult");
				Node child = nl.item(0).getFirstChild();
				if (child instanceof CharacterData) {
					CharacterData cd = (CharacterData) child;
					InputStream is = new ByteArrayInputStream(cd.getData().getBytes());
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder db = dbf.newDocumentBuilder();
					responseMessage = null;
					return db.parse(is);
				} else {
					return null;
				}
			} catch (SOAPException | ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
		

	}

	public boolean responseReturnedError() {
		if (responseDataDoc != null) {
			return responseDataDoc.getElementsByTagName("ErrorCode").item(0) != null ? true : false;
		} else {
			return true;
		}
	}

	public String getResponseErrorMessage(Locale locale) {
		if (responseDataDoc != null) {
			if (locale.getCountry().equals("PL")) {
				return responseDataDoc.getElementsByTagName("ErrorMessagePl").item(0).getTextContent();
			} else {
				return responseDataDoc.getElementsByTagName("ErrorMessageEn").item(0).getTextContent();
			}
		} else {
			//TODO: add items to resource bundle to standardize Locale			
			return "Invalid response from the service!";
		}
	}

	public Document getResponseDataDoc() {
		return responseDataDoc;
	}
}
