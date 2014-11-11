package com.sales.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * represents salesentry table from the database
 * @author Admin
 *
 */

@Entity
@Table(name="salesentry")
public class SalesEntry {
	@Id
	@Column(name="SalesId")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int salesId;
	
	@Column(name="DateofSale")
	private Date dateOfSale;
	
	@Column(name="ProductID")
	private int productId;
	
	@Column(name="SaleAmount")
	private double salesAmount;
	
	@Column(name="Units")
	private int units;
	
	
	public SalesEntry(){}
	
	
	public int getSalesId() {
		return salesId;
	}
	public void setSalesId(int salesId) {
		this.salesId = salesId;
	}
	public Date getDateOfSale() {
		return dateOfSale;
	}
	public void setDateOfSale(Date dateOfSale) {
		this.dateOfSale = dateOfSale;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public double getSalesAmount() {
		return salesAmount;
	}
	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}
	
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}


}
