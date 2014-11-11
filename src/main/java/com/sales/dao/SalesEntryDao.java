package com.sales.dao;

import java.util.List;

import com.sales.model.SalesEntry;



public interface SalesEntryDao {	
	public SalesEntry getSalesEntry(int salesId);
	public void addSalesEntry(SalesEntry salesEntry);
	public void removeSalesEntry(int salesId);
	public void editSalesEntry(SalesEntry salesEntry);
	public List listAllSalesEntry();
	public void deleteAllSalesEntry();
	public List listAllSalesForProduct(int id);
}
