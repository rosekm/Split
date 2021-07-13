package birUtilsTest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import birUtils.BIRSession;
import birUtils.BIRSessionManager;
import fxmodel.CustomerFX;

public class BIRSessionManagerTest {

	BIRSessionManager birSessionManager;
	CustomerFX customerFx;

	@Before
	public void setUp() {
		birSessionManager = new BIRSessionManager();
		customerFx = new CustomerFX();
	}

	@Test
	public void shouldReturnCorrectNameForQueryByCorrectVatNo() {
		customerFx.setVatNoProperty("6791510863");

		customerFx = birSessionManager.getBIRCustomer(customerFx);

		assertEquals("LOGAN SPÓŁKA Z OGRANICZONĄ ODPOWIEDZIALNOŚCIĄ", customerFx.getNameProperty().getValue());
	}

	@Test(expected = ExceptionInInitializerError.class)
	public void shouldNotChangeObjectWhenQueriedByIncorrectVatNo() {
		customerFx.setVatNoProperty("000000");

		customerFx = birSessionManager.getBIRCustomer(customerFx);

		assertEquals("000000", customerFx.getNameProperty().getValue());
		assertEquals(null, customerFx.getNameProperty().getValue());
	}
	
	@After
	public void tearUp() {
		BIRSession.getInstance().logout();
	}

}
