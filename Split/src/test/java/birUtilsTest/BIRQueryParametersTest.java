package birUtilsTest;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import birUtils.BIRQueryParameters;

public class BIRQueryParametersTest {
	
	BIRQueryParameters birQueryParameters;
	HashMap<String, String> expectedQueryParameters;
	
	@Before
	public void setUp() {
		birQueryParameters = new BIRQueryParameters();
	}
	

	@Test
	public void shouldReturnAMapWithNullValuesForEachKey() {
		birQueryParameters.clearQueryParametersValues();
		
		assertEquals(null, birQueryParameters.getQueryParameters().get("Regon"));
		assertEquals(null, birQueryParameters.getQueryParameters().get("Nip"));
		assertEquals(null, birQueryParameters.getQueryParameters().get("Krs"));
		assertEquals(null, birQueryParameters.getQueryParameters().get("Nipy"));
		assertEquals(null, birQueryParameters.getQueryParameters().get("Regony9zn"));
		assertEquals(null, birQueryParameters.getQueryParameters().get("Krsy"));
		assertEquals(null, birQueryParameters.getQueryParameters().get("Regony14zn"));
	}
	
}
