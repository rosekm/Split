package utils;

import dbModel.Salesman;
import fxmodel.BranchFX;
import fxmodel.SalesmanFX;

public class SalesmanFXConverter {

	private static SalesmanFX salesmanFX;
	private static Salesman salesman;

	public static SalesmanFX convertToSalesmanFX(Salesman salesman) {
		salesmanFX = new SalesmanFX();
		salesmanFX.setIdProperty(salesman.getId());
		salesmanFX.setNameProperty(salesman.getName());
		salesmanFX.setSurnameProperty(salesman.getSurname());
		salesmanFX.setBranchObjectProperty(BranchFXConverter.convertToBranchFX(salesman.getBranch()));
		return salesmanFX;
	}

	public static Salesman convertToSalesman(SalesmanFX salesmanFX) {
		salesman = new Salesman();
		salesman.setId(salesmanFX.getIdProperty().getValue());
		salesman.setName(salesmanFX.getNameProperty().getValue());
		salesman.setSurname(salesmanFX.getSurnameProperty().getValue());
		salesman.setBranch(BranchFXConverter.convertToBranch(salesmanFX.getBranchObjectProperty().getValue()));
		return salesman;
	}
}
