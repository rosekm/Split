package birUtilsTest;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import birUtils.BIRMessageBuilder;
import birUtils.BIRMessageBuilder.ActionType;
import birUtils.BIRQueryParameters;

public class BIRMessageBuilderTest {

	String baseGetValueEnvelope;
	String baseLogInEnvelope;
	String baseQueryEnvelope;
	String baseLogOutEnvelope;
	String completeGetValueEnvelope;
	String completeLogInEnvelope;
	String completeQueryEnvelope;
	String completeLogOutEnvelope;
	ByteArrayOutputStream baos;
	SOAPMessage testMessage;
	String testedEnvelope;
	HashMap<String, String> testParameterMap;

	@Before
	public void setUp() {
		baseGetValueEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/2014/07/IUslugaBIR/GetValue</wsa:Action></soap:Header><env:Body/></soap:Envelope>";
		baseLogInEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Zaloguj</wsa:Action></soap:Header><env:Body/></soap:Envelope>";
		baseQueryEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/DaneSzukajPodmioty</wsa:Action></soap:Header><env:Body/></soap:Envelope>";
		baseLogOutEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Wyloguj</wsa:Action></soap:Header><env:Body/></soap:Envelope>";
		completeGetValueEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://CIS/BIR/2014/07\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/2014/07/IUslugaBIR/GetValue</wsa:Action></soap:Header><soap:Body><ns:GetValue><ns:pNazwaParametru xmlns:ns=\"http://CIS/BIR/2014/07\">StatusSesji</ns:pNazwaParametru></ns:GetValue></soap:Body></soap:Envelope>";
		completeLogInEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://CIS/BIR/PUBL/2014/07\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Zaloguj</wsa:Action></soap:Header><soap:Body><ns:Zaloguj><ns:pKluczUzytkownika xmlns:ns=\"http://CIS/BIR/PUBL/2014/07\">abcde12345abcde12345</ns:pKluczUzytkownika></ns:Zaloguj></soap:Body></soap:Envelope>";
		completeQueryEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:dat=\"http://CIS/BIR/PUBL/2014/07/DataContract\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://CIS/BIR/PUBL/2014/07\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/DaneSzukajPodmioty</wsa:Action></soap:Header><soap:Body><ns:DaneSzukajPodmioty><ns:pParametryWyszukiwania xmlns:ns=\"http://CIS/BIR/PUBL/2014/07\"><dat:Nip>123-12-12-123</dat:Nip></ns:pParametryWyszukiwania></ns:DaneSzukajPodmioty></soap:Body></soap:Envelope>";
		completeLogOutEnvelope = "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:env=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:ns=\"http://CIS/BIR/PUBL/2014/07\"><soap:Header xmlns:wsa=\"http://www.w3.org/2005/08/addressing\"><wsa:To>https://wyszukiwarkaregontest.stat.gov.pl/wsBIR/UslugaBIRzewnPubl.svc</wsa:To><wsa:Action>http://CIS/BIR/PUBL/2014/07/IUslugaBIRzewnPubl/Wyloguj</wsa:Action></soap:Header><soap:Body><ns:Wyloguj><ns:pIdentyfikatorSesji xmlns:ns=\"http://CIS/BIR/PUBL/2014/07\">Test</ns:pIdentyfikatorSesji></ns:Wyloguj></soap:Body></soap:Envelope>";

		baos = new ByteArrayOutputStream();
	}

	@Test
	public void shouldReturnCorrectBaseGetValueEnvelope() {
		try {
			getAndInvokeMethod("buildBaseSOAPMessage", ActionType.class, ActionType.SESSION_STATUS);
			testMessage = getBIRMessageValue();
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();

			assertEquals(baseGetValueEnvelope, testedEnvelope);
		} catch (SOAPException | IOException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldReturnCorrectBaseLogInEnvelope() {
		try {
			getAndInvokeMethod("buildBaseSOAPMessage", ActionType.class, ActionType.LOGIN);
			testMessage = getBIRMessageValue();
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();

			assertEquals(baseLogInEnvelope, testedEnvelope);
		} catch (SOAPException | IOException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldReturnCorrectBaseQueryEnvelope() {
		try {
			getAndInvokeMethod("buildBaseSOAPMessage", ActionType.class, ActionType.QUERY_SINGLE_COMPANY);
			testMessage = getBIRMessageValue();
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();

			assertEquals(baseQueryEnvelope, testedEnvelope);
		} catch (SOAPException | IOException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void shouldReturnCorrectBaseLogoutEnvelope() {
		try {
			getAndInvokeMethod("buildBaseSOAPMessage", ActionType.class, ActionType.LOGOUT);
			testMessage = getBIRMessageValue();
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();

			assertEquals(baseLogOutEnvelope, testedEnvelope);
		} catch (SOAPException | IOException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	private void getAndInvokeMethod(String methodName, Class argumentClass, ActionType actionType)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Method buildBaseSOAPMessageMethod = BIRMessageBuilder.class.getDeclaredMethod(methodName, argumentClass);
		buildBaseSOAPMessageMethod.setAccessible(true);
		buildBaseSOAPMessageMethod.invoke(null, actionType);
	}

	private SOAPMessage getBIRMessageValue() throws NoSuchFieldException, IllegalAccessException {
		SOAPMessage testMessage;
		Field soapMessageField = BIRMessageBuilder.class.getDeclaredField("soapMessage");
		soapMessageField.setAccessible(true);
		testMessage = (SOAPMessage) soapMessageField.get(null);
		return testMessage;
	}

	@Test
	public void shouldReturnCorrectCompleteGetValueEnvelope() {
		try {
			testMessage = BIRMessageBuilder.getSOAPMessage(ActionType.SESSION_STATUS);
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();
			assertEquals(completeGetValueEnvelope, testedEnvelope);
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void shouldReturnCorrectCompleteLogInEnvelope() {
		try {
			testMessage = BIRMessageBuilder.getSOAPMessage(ActionType.LOGIN);
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();
			assertEquals(completeLogInEnvelope, testedEnvelope);
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void shouldReturnCorrectCompleteQueryEnvelope() {
		try {
			testParameterMap = getFilledParamMap();
			BIRQueryParameters testQueryParameters = EasyMock.createMock(BIRQueryParameters.class);
			BIRMessageBuilder.setQueryParameters(testQueryParameters);
			EasyMock.expect(testQueryParameters.getQueryParameters()).andReturn(testParameterMap);
			EasyMock.replay(testQueryParameters);
			
			testMessage = BIRMessageBuilder.getSOAPMessage(ActionType.QUERY_SINGLE_COMPANY);
			EasyMock.verify(testQueryParameters);
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();
			System.out.println(testedEnvelope);
			assertEquals(completeQueryEnvelope, testedEnvelope);
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void shouldReturnCorrectCompleteLogOutEnvelope() {
		try {
			BIRMessageBuilder.setSessionID("Test");
			testMessage = BIRMessageBuilder.getSOAPMessage(ActionType.LOGOUT);
			testMessage.writeTo(baos);
			testedEnvelope = baos.toString();
			assertEquals(completeLogOutEnvelope, testedEnvelope);
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		}

	}
	
	private HashMap<String,String> getFilledParamMap(){
		testParameterMap = new HashMap<String, String>();
		testParameterMap.put("Regon", null);
		testParameterMap.put("Nip", "123-12-12-123");
		testParameterMap.put("Krs", null);
		testParameterMap.put("Nipy", null);
		testParameterMap.put("Regony9zn", null);
		testParameterMap.put("Krsy", null);
		testParameterMap.put("Regony14zn", null);
		return testParameterMap;
	}

	@After
	public void tearDown() {
		testMessage = null;
	}
	
}
