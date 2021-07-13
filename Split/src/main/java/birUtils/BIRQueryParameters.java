package birUtils;

import java.util.HashMap;

/*
 * Class responsible for creating standardized query parameter set for BIR System
 */
public class BIRQueryParameters {
	
	private HashMap<String, String> queryParameters;
	
	public BIRQueryParameters() {
		queryParameters = new HashMap<String, String>();
		fillMapWithParametersKeys();
		
	}
	
	private void fillMapWithParametersKeys() {
		queryParameters.put("Regon", null);
		queryParameters.put("Nip", null);
		queryParameters.put("Krs", null);
		queryParameters.put("Nipy", null);
		queryParameters.put("Regony9zn", null);
		queryParameters.put("Krsy", null);
		queryParameters.put("Regony14zn", null);
	}
	
	public void setRegon(String regon) {
		queryParameters.put("Regon", regon);
	}
	
	public void setNip(String nip) {
		queryParameters.put("Nip", nip);
	}
	
	public void setKrs(String krs) {
		queryParameters.put("Krs", krs);
	}
	
	public void setNipy(String nipy) {
		queryParameters.put("Nipy", nipy);
	}

	public void setRegon9zn(String regon9zn) {
		queryParameters.put("Regon9zn", regon9zn);
	}
	
	public void setKrsy(String krsy) {
		queryParameters.put("Krsy", krsy);
	}
	
	public void setRegony14zn(String regony14zn) {
		queryParameters.put("Regony14zn", regony14zn);
	}

	public HashMap<String, String> getQueryParameters() {
		return queryParameters;
	}
	
	public void clearQueryParametersValues() {
		queryParameters.clear();
		fillMapWithParametersKeys();
	}
}
