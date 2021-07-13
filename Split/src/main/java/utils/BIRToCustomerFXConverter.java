package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import birUtils.BIRQueryParameters;
import fxmodel.CustomerFX;

/*Converts xml based results from BIR Service into CustomerFX object
*/
public class BIRToCustomerFXConverter {

	private static CustomerFX customerFX;

	public static CustomerFX convertToCustomerFXObject(Document xmlBIRData) {
		customerFX = new CustomerFX();
		NodeList birElements = xmlBIRData.getElementsByTagName("dane").item(0).getChildNodes();
		for (int i = 0; i < birElements.getLength(); i++) {
			Node node = birElements.item(i);
			if (node instanceof Element) {
				setCustomerFXPropertyValue(node);
			}
		}
		customerFX.setAddressProperty(customerFX.getAddressProperty().getValue().trim());
		return customerFX;
	}

	private static void setCustomerFXPropertyValue(Node node) {
		switch (node.getNodeName()) {
		case "Nip":
			System.out.println(node.getTextContent());
			customerFX.setVatNoProperty(node.getTextContent());
			break;
		case "Nazwa":
			customerFX.setNameProperty(node.getTextContent());
			break;
		case "Miejscowosc":
			customerFX.setCityProperty(node.getTextContent());
			break;
		case "KodPocztowy":
			customerFX.setPostalCodeProperty(node.getTextContent());
			break;
		case "Ulica":
			customerFX.setAddressProperty(customerFX.getAddressProperty().getValue() + " " + node.getTextContent());
			break;
		case "NrNieruchomosci":
			customerFX.setAddressProperty(customerFX.getAddressProperty().getValue() + " " + node.getTextContent());
			break;
		case "NrLokalu":
			customerFX.setAddressProperty(customerFX.getAddressProperty().getValue() + " " + node.getTextContent());
			break;
		}

	}
	
	public static BIRQueryParameters convertToBIRQueryParameters(CustomerFX customerFX) {
		BIRQueryParameters queryParameters = new BIRQueryParameters();
		queryParameters.setNip(customerFX.getVatNoProperty().getValue());
		return queryParameters;
	}
}
