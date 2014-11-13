package com.sales.report;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import com.sales.dao.impl.ProductDaoImpl;
import com.sales.dao.impl.SalesEntryDaoImpl;
import com.sales.model.Product;
import com.sales.model.SalesEntry;

/**
 * Represents report building class
 * @author Admin
 *
 */

public class ReportBuilder {
	
	String newLine = System.getProperty("line.separator");
	ApplicationContext ctx ;
	SalesEntryDaoImpl salesDao;	
	ProductDaoImpl productDao ;	

	
	public ApplicationContext getCtx() {
		return ctx;
	}


	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}


	public SalesEntryDaoImpl getSalesDao() {
		return salesDao;
	}


	public void setSalesDao(SalesEntryDaoImpl salesDao) {
		this.salesDao = salesDao;
	}


	public ProductDaoImpl getProductDao() {
		return productDao;
	}


	public void setProductDao(ProductDaoImpl productDao) {
		this.productDao = productDao;
	}


	/**
	 * initialize application context and DAO
	 */
	public ReportBuilder(){
		ctx  = new ClassPathXmlApplicationContext("file:spring.xml");
		productDao = (ProductDaoImpl) ctx.getBean(ProductDaoImpl.class);	
		salesDao = (com.sales.dao.impl.SalesEntryDaoImpl) ctx.getBean(com.sales.dao.impl.SalesEntryDaoImpl.class);	

	}
	
	
	/**
	 * Build and Generate report
	 * @return
	 */
	public Map<String, AmountAndUnits> getReport(){			
		Map<Integer, String> products = getAllProductDetails();		
		List<SalesEntry> salesList = getAllSales();	
		return buildReport(products,salesList);				
	}
	
	/**
	 * get all products id and name
	 * @return
	 */
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
	
	/**
	 * get all sales entries from the database
	 * @return
	 */
	public List<SalesEntry> getAllSales(){
		return (List<SalesEntry>) salesDao.listAllSalesEntry();			
	}
	
	
	/**
	 * build the report with product name, units sold and total amount
	 * @param products
	 * @param sales
	 * @return
	 */
	public Map<String, AmountAndUnits> buildReport(Map<Integer, String> products,List<SalesEntry> sales) {
		Map<String, AmountAndUnits> salesReport = new HashMap<String, AmountAndUnits>();
		for(SalesEntry salesItem : sales){
			if(salesReport.containsKey(products.get(salesItem.getProductId()))){
				AmountAndUnits amountUnits = salesReport.get(products.get(salesItem.getProductId()));
				amountUnits.setAmount(amountUnits.getAmount() + salesItem.getSalesAmount());
				amountUnits.setUnits(amountUnits.getUnits()+ salesItem.getUnits());
				salesReport.put(products.get(salesItem.getProductId()), amountUnits);
				
			}else{
				 AmountAndUnits amtAndUnits = new AmountAndUnits();
				 amtAndUnits.setAmount(salesItem.getSalesAmount());
				 amtAndUnits.setUnits(salesItem.getUnits());
				salesReport.put(products.get(salesItem.getProductId()), amtAndUnits );
			}		
		}
		return salesReport;	
	}
	
	
	/**
	 * show report to console
	 * @param salesSummary
	 */
	public void printReport(Map<String, AmountAndUnits> salesSummary) {
		if (salesSummary != null) {
			System.out.println("Sales Summary :");
			System.out.println("ProductName" + "\t\t" + "TotalAmount" + "\t\t" + "UnitsSold");
			java.util.Iterator<Entry<String, AmountAndUnits>> it = salesSummary.entrySet().iterator();

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
}
