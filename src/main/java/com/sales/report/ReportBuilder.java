package com.sales.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.bytecode.Descriptor.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.sales.dao.impl.ProductDaoImpl;
import com.sales.dao.impl.SalesEntryDaoImpl;
import com.sales.model.Product;
import com.sales.model.SalesEntry;


public class ReportBuilder {
	
	String newLine = System.getProperty("line.separator");
	
	// ApplicationContext ctx  = new FileSystemXmlApplicationContext("E:\\media\\com.salesproject\\spring.xml");
	ApplicationContext ctx  = new ClassPathXmlApplicationContext("file:spring.xml");
	SalesEntryDaoImpl salesDao = (SalesEntryDaoImpl) ctx.getBean(SalesEntryDaoImpl.class);	
	ProductDaoImpl productDao = (ProductDaoImpl) ctx.getBean(ProductDaoImpl.class);	

	
	public ReportBuilder(){
	}
	
	public Map<String, AmountAndUnits> getReport(){			
		Map<Integer, String> products = getAllProductDetails();		
		List<SalesEntry> salesList = getAllSales();	
		Map<String, AmountAndUnits> salesSummary = buildReport(products,salesList);				
		return salesSummary;
		
	}
	
	public Map<Integer, String> getAllProductDetails(){
		Map<Integer, String> allProducts = new HashMap<Integer, String>();
		List<Product> productList = (List<Product>) productDao.listAllProducts();
		for(Product prod : productList){
			//avoid irrelevant products
			if(prod.getProductName() == null || prod.getProductName().length() == 0){
				continue;
			}			
			allProducts.put(prod.getProductId(), prod.getProductName());
		}
		return allProducts;
	}
	
	public List<SalesEntry> getAllSales(){
		return (List<SalesEntry>) salesDao.listAllSalesEntry();			
	}
	
	public Map<String, AmountAndUnits> buildReport(Map<Integer, String> products,List<SalesEntry> sales) {
		Map<String, AmountAndUnits> salesReport = new HashMap<String, AmountAndUnits>();
		for(SalesEntry salesItem : sales){
			if(salesReport.containsKey(products.get(salesItem.getProductId()))){
				AmountAndUnits amountUnits = salesReport.get(products.get(salesItem.getProductId()));
				amountUnits.setAmount(amountUnits.getAmount() + salesItem.getSalesAmount());
				amountUnits.setUnits(amountUnits.getUnits()+ salesItem.getUnits());
				salesReport.put(products.get(salesItem.getProductId()), amountUnits);
				
			}else{
				salesReport.put(products.get(salesItem.getProductId()), new AmountAndUnits(salesItem.getSalesAmount(),salesItem.getUnits()));
			}		
		}
		return salesReport;	
	}
	
	public void printReport(Map<String, AmountAndUnits> salesSummary) {
		if (salesSummary != null) {
			System.out.println("Sales Summary :");
			System.out.println("ProductName" + "\t\t" + "TotalAmount" + "\t\t"
					+ "UnitsSold");
			java.util.Iterator<Entry<String, AmountAndUnits>> it = salesSummary
					.entrySet().iterator();

			while (it.hasNext()) {
				Entry<String, AmountAndUnits> entry = it.next();
				System.out.println(entry.getKey() + "\t\t"
						+ entry.getValue().getAmount() + "\t\t"
						+ entry.getValue().getUnits());
			}
		} else {
			System.out.println("Sales Summary : " + salesSummary);
		}
	}
	
	public static void main(String[] args){
		ReportBuilder reportBuilder = new ReportBuilder();
		reportBuilder.printReport(reportBuilder.getReport());
	}
	
	public static class AmountAndUnits{
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

		double amount ;
		int units;
		
		public AmountAndUnits(double amount, int units){
			this.amount = amount;
			this.units = units;
		}
		
	}

}
