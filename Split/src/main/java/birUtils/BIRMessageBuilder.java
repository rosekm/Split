package birUtils;

import java.util.HashMap;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.DOMException;
import org.w3c.dom.NodeList;

/*
 * Class responsible for building SOAPMessage for BIR System
 */
public class BIRMessageBuilder {

	private static SOAPMessage soapMessage;
	private static final String USER_LOG_IN_KEY = "abcde12345abcde12345";
	private static String sessionIDparameter;
	private static BIRQueryParameters queryParameters;
	
	public enum ActionType {
		LOGIN("http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Zaloguj"), 
		LOGOUT("http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Wyloguj"), 
		SESSION_STATUS("http://CIS/BIR/2014/07/IUslugaBIR/GetValue"), 
		QUERY_SINGLE_COMPANY("http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/DaneSzukajPodmioty");
	
		String action;
		private ActionType(String actionText) {
			action = actionText;
	}
		public void setAction(String action) {
			this.action = action;
		}
	};

	public static SOAPMessage getSOAPMessage(ActionType actionType) throws SOAPException {
		
		buildBaseSOAPMeaage(actionType);
		switch(actionType) {
		case LOGIN:
			buildLogInEnvelope();
			break;
		case LOGOUT:
			buildLogOutEnvelope();
			break;
		case SESSION_STATUS:
			buildGetValueEnvelope();
			break;
		case QUERY_SINGLE_COMPANY:
			buildQuerySingleCompanyEnvelope();
			break;	
		}
		return soapMessage;
	}

	private static void buildBaseSOAPMeaage(ActionType actionType) throws SOAPException {
		MessageFactory factory = MessageFactory.newInstance(SOAPConstants.SOAP_1_2_PROTOCOL);
		soapMessage = factory.createMessage();

		SOAPEnvelope envelope = soapMessage.getSOAPPart().getEnvelope();
		envelope.setPrefix("soap");
		envelope.setAttribute("xmlns:soap", "http://www.w3.org/2003/05/soap-envelope");
		//envelope.setAttribute("xmlns:ns", "http://CIS/BIR/PUBL/2014/07");

		SOAPHeader header = soapMessage.getSOAPHeader();
		header.setPrefix("soap");
		header.setAttribute("xmlns:wsa", "http://www.w3.org/2005/08/addressing");

		QName qname1 = new QName("http://www.w3.org/2005/08/addressing", "To", "wsa");
		SOAPHeaderElement elem1 = header.addHeaderElement(qname1);
		elem1.addTextNode("https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc");

		QName qname2 = new QName("http://www.w3.org/2005/08/addressing", "Action", "wsa");
		SOAPHeaderElement elem2 = header.addHeaderElement(qname2);
		elem2.addTextNode(actionType.action);
	}

	private static void buildLogInEnvelope() throws DOMException, SOAPException {
		soapMessage.getSOAPPart().getEnvelope().setAttribute("xmlns:ns", "http://CIS/BIR/PUBL/2014/07");
		SOAPBody body = soapMessage.getSOAPBody();
		body.setPrefix("soap");
		QName qname1 = new QName("", "Zaloguj", "ns");
		SOAPBodyElement elemb1 = body.addBodyElement(qname1);

		SOAPElement bodySubelement1 = elemb1.addChildElement("pKluczUzytkownika", "ns");
		bodySubelement1.addTextNode(USER_LOG_IN_KEY);	
	} 

	private static void buildLogOutEnvelope() throws DOMException, SOAPException {
		soapMessage.getSOAPPart().getEnvelope().setAttribute("xmlns:ns", "http://CIS/BIR/PUBL/2014/07");
		SOAPBody body = soapMessage.getSOAPBody();
		body.setPrefix("soap");
		QName qname1 = new QName("", "Wyloguj", "ns");
		SOAPBodyElement elemb1 = body.addBodyElement(qname1);

		SOAPElement bodySubelement1 = elemb1.addChildElement("pIdentyfikatorSesji", "ns");
		bodySubelement1.addTextNode(sessionIDparameter);
	}

	private static void buildGetValueEnvelope() throws DOMException, SOAPException {
		soapMessage.getSOAPPart().getEnvelope().setAttribute("xmlns:ns", "http://CIS/BIR/2014/07");
		SOAPBody body = soapMessage.getSOAPBody();
		body.setPrefix("soap");
		QName qname0 = new QName("", "GetValue", "ns");
		SOAPBodyElement bodyElement1 = body.addBodyElement(qname0);

		SOAPElement bodySubelement1 = bodyElement1.addChildElement("pNazwaParametru", "ns");
		bodySubelement1.addTextNode("StatusSesji");
		addSIDToMimeHeader();
	}

	public static void buildQuerySingleCompanyEnvelope() throws SOAPException {
		soapMessage.getSOAPPart().getEnvelope().setAttribute("xmlns:ns", "http://CIS/BIR/PUBL/2014/07");
		soapMessage.getSOAPPart().getEnvelope().setAttribute("xmlns:dat", "http://CIS/BIR/PUBL/2014/07/DataContract");
		SOAPBody body = soapMessage.getSOAPBody();
		body.setPrefix("soap");
		
		QName qname1 = new QName("", "DaneSzukajPodmioty", "ns");
		SOAPBodyElement bodyElement1 = body.addBodyElement(qname1);
		
		SOAPElement bodySubelement = bodyElement1.addChildElement("pParametryWyszukiwania", "ns");
		
		HashMap<String,String> queryParametersMap = queryParameters.getQueryParameters();
		for(String param: queryParametersMap.keySet()) {
			if(queryParametersMap.get(param) != null && !queryParametersMap.get(param).equals("")) {
				SOAPElement queryParameter = bodySubelement.addChildElement(param, "dat");
				queryParameter.addTextNode(queryParametersMap.get(param));
			}
		}
		
		addSIDToMimeHeader();
		
	}
	
	private static void setNSAttribute(String ns) throws DOMException, SOAPException {
		soapMessage.getSOAPPart().getEnvelope().setAttribute("xmlns:ns", ns);
	}
	
	private static void addSIDToMimeHeader() throws SOAPException {
		MimeHeaders headers = soapMessage.getMimeHeaders();
		headers.addHeader("sid", sessionIDparameter);

		soapMessage.saveChanges();
	}

	public static String getSessionID() {
		return sessionIDparameter;
	}

	public static void setSessionID(String sessionID) {
		sessionIDparameter = sessionID;
	}

	public static void setQueryParameters(BIRQueryParameters queryParameters) {
		BIRMessageBuilder.queryParameters = queryParameters;
	}

}
