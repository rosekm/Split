package birUtilsTest;

import static org.junit.Assert.assertNotNull;

import javax.xml.soap.SOAPMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import birUtils.BIRQuery;
import birUtils.BIRQueryParameters;
import birUtils.BIRSession;

public class BIRQueryTest {

	BIRQuery birQuery;
	BIRQueryParameters birQueryParameters;
	SOAPMessage queryMessage;
	
	@Before
	public void setUp() {
		birQuery = new BIRQuery();
		birQueryParameters = new BIRQueryParameters();
		BIRSession.getInstance().logIn();
	}
	
	@Test
	public void shouldReturnNotNullDocumentAfterExecutingQuery() {
		birQueryParameters.setNip("6791510863");
		
		birQuery.querySingleCompany(birQueryParameters);
		
		assertNotNull(birQuery.getResponseDataDoc());
		
	}
	
	@After
	public void tearUp() {
		BIRSession.getInstance().logout();
	}
}
