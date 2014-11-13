package com.sales.report;


public class AmountAndUnits {
	double amount ;
	int units;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public int getUnits() {
		return units;
	}

	public void setUnits(int units) {
		this.units = units;
	}
	
	public AmountAndUnits(){
		this.amount = 0.0;
		this.units = 0;
	}

}
