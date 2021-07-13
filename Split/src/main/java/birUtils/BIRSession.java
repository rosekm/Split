package birUtils;

import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.w3c.dom.NodeList;

import birUtils.BIRMessageBuilder.ActionType;
import utils.DialogUtils;


/*
 * Class responsible for managing connection status with BIR System
 */
public class BIRSession {

	
	private final String getSessionStatusAction = "http://CIS/BIR/2014/07/IUslugaBIR/GetValue";
	private static BIRSession birSession;
	private SOAPMessage responseMessage;


	public BIRSession() {
	}

	public void logIn() {
			try {
				SOAPMessage logInMessage = BIRMessageBuilder.getSOAPMessage(ActionType.LOGIN);

				responseMessage = getResponseMessage(logInMessage);

				NodeList responseNodeList = responseMessage.getSOAPPart().getEnvelope()
						.getElementsByTagName("ZalogujResult");

				if (responseNodeList.getLength() != 0) {
					BIRMessageBuilder.setSessionID(responseNodeList.item(0).getTextContent());
				}
			} catch (SOAPException e) {
				DialogUtils.errorDialog(e.getMessage());
				e.printStackTrace();
			}
	}

	public void logout() {
			try {
				SOAPMessage logOutMessage = BIRMessageBuilder.getSOAPMessage(ActionType.LOGOUT);

				responseMessage = getResponseMessage(logOutMessage);
				BIRMessageBuilder.setSessionID(null);
			} catch (SOAPException e) {
				DialogUtils.errorDialog(e.getMessage());
				e.printStackTrace();
			}
	}

	public boolean sessionIsActive(){
			try {
				SOAPMessage statusQueryMessage = BIRMessageBuilder.getSOAPMessage(ActionType.SESSION_STATUS);

				responseMessage = getResponseMessage(statusQueryMessage);
				NodeList responseNodeList = responseMessage.getSOAPPart().getEnvelope()
						.getElementsByTagName("GetValueResult");
				int sessionStatusCode = Integer.valueOf(responseNodeList.item(0).getTextContent());
				if (sessionStatusCode == 1) {
					return true;
				} else {
					BIRMessageBuilder.setSessionID(null);
					return false;
				}

			} catch (SOAPException e) {
				e.printStackTrace();
				return false;
			}
	}

	public SOAPMessage getResponseMessage(SOAPMessage requestMessage)
			throws UnsupportedOperationException, SOAPException {
		SOAPConnection soapConnection = SOAPConnectionFactory.newInstance().createConnection();
		return soapConnection.call(requestMessage,
				"https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc");
	}
	
	public static BIRSession getInstance() {
		if(birSession == null) {
			birSession = new BIRSession();
		}
		return birSession;
	}

}
