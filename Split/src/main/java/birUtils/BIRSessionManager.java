package birUtils;

import java.util.Locale;

import fxmodel.CustomerFX;
import utils.BIRToCustomerFXConverter;
import utils.DialogUtils;

/*Class responsible for managing connection and results of queries to BIR1 - Internet Database REGON 1
 */

public class BIRSessionManager {

	BIRSession birSession;
	BIRQuery birQuery;
	BIRQueryParameters queryParameters;

	public BIRSessionManager() {
		birSession = BIRSession.getInstance();
		birQuery = new BIRQuery();
	}

	// on entry 'searchedCustomer' contains only vatNo data
	public CustomerFX getBIRCustomer(CustomerFX searchedCustomer) {

			if (!birSession.sessionIsActive()) {
				birSession.logIn();
			}
			queryParameters = BIRToCustomerFXConverter.convertToBIRQueryParameters(searchedCustomer);
			birQuery.querySingleCompany(queryParameters);
			if (birQuery.responseReturnedError()) {
				DialogUtils.errorDialog(birQuery.getResponseErrorMessage(Locale.getDefault()));
				return searchedCustomer;
			} else {
				return BIRToCustomerFXConverter.convertToCustomerFXObject(birQuery.getResponseDataDoc());
			}


	}

}
