package com.sales.tests;


import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.sales.dao.impl.ProductDaoImpl;
import com.sales.dao.impl.SalesEntryDaoImpl;
import com.sales.model.Product;
import com.sales.model.SalesEntry;
import com.sales.report.AmountAndUnits;
import com.sales.report.ReportBuilder;



public class SalesTests {
	ApplicationContext ac;	
	ProductDaoImpl productDao ;	
	SalesEntryDaoImpl salesDao ;	
	final int numOfProducts  = 10;
	final int numOfSales = numOfProducts * 2 ; 
	
	@Before
	public void setUp(){
		ac = new FileSystemXmlApplicationContext("file:spring.xml");
		assertNotNull(ac);
		productDao = (ProductDaoImpl) ac.getBean(ProductDaoImpl.class);
		assertNotNull(productDao);
		salesDao = (SalesEntryDaoImpl) ac.getBean(SalesEntryDaoImpl.class);
		assertNotNull(salesDao);
		productDao.deleteAllProducts();	
		addProducts();
		salesDao.deleteAllSalesEntry();
		addSalesEntry();
	}
	
	protected void tearDown() throws Exception {
		productDao.deleteAllProducts();
		salesDao.deleteAllSalesEntry();
	}
	
	public void addProducts(){
		for(int i = 0 ; i < numOfProducts; i++){
			Product prod = new Product();
			prod.setProductName("Product"+i);
			productDao.addProduct(prod);
		}
	}
	
	public void addSalesEntry(){
		List<Product> products = (List<Product>) productDao.listAllProducts();
		for(int i = 0, j = 0 ; i < numOfSales ; i = i+2, j++){
			//add 2 entries for the same product
			
			SalesEntry sale = new SalesEntry();
			sale.setDateOfSale(new Date(System.currentTimeMillis()));
			sale.setProductId(products.get(j).getProductId());
			sale.setSalesAmount(i* i / 3.0);
			sale.setUnits(i*2);
			salesDao.addSalesEntry(sale);
			
			
			SalesEntry sale2 = new SalesEntry();
			sale2.setDateOfSale(new Date(System.currentTimeMillis()));
			sale2.setProductId(products.get(j).getProductId());
			sale2.setSalesAmount(i*i);
			sale2.setUnits(i*2);
			salesDao.addSalesEntry(sale2);
		}
	}


	/**
	 * test total products against the inserted products being same
	 */
    @Test
    public void testProductNumbers(){
    	assertTrue(productDao.listAllProducts().size() == numOfProducts );
    }
    
    
    /**
     * test sales entries against the inserted entries being same
     */
    @Test
    public void testSalesNumber(){  	
    	assertTrue(salesDao.listAllSalesEntry().size() == numOfSales );
    }
    
    
    /**
     * test total entries in the report 
     */
    @Test
    public void testEnteriesInReport(){    	
    	ReportBuilder reportBuilder = new ReportBuilder();
    	Map<String, AmountAndUnits> report = reportBuilder.getReport();
    	assertTrue(report.size() == numOfProducts);    	
    }
    
    
    /**
     * test total sales amount for a product against report amount 
     */
    @Test
    public void testAmountForProduct(){    	
    	ReportBuilder reportBuilder = new ReportBuilder();
    	Map<String, AmountAndUnits> report = reportBuilder.getReport();
    	Product product = (Product) productDao.listAllProducts().get(0);
    	List<SalesEntry> sales = salesDao.listAllSalesForProduct(product.getProductId());
    	double amount = 0.0;
    	for(SalesEntry sale : sales){
    		amount += sale.getSalesAmount();
    	}
    	AmountAndUnits amtAndUnits = report.get(product.getProductName());
    	assertEquals(amtAndUnits.getAmount(),amount,0.0001);    	
    }
    
    
    /**
     * test total sales units for a product against report units 
     */
    @Test
    public void testUnitsOfproduct(){    	
    	ReportBuilder reportBuilder = new ReportBuilder();
    	Map<String, AmountAndUnits> report = reportBuilder.getReport();
    	Product product = (Product) productDao.listAllProducts().get(0);
    	List<SalesEntry> sales = salesDao.listAllSalesForProduct(product.getProductId());
    	int units = 0;
    	for(SalesEntry sale : sales){
    		units += sale.getUnits();
    	}
    	AmountAndUnits amtAndUnits = report.get(product.getProductName());
    	assertEquals(amtAndUnits.getUnits(),units);    	
    }
    
    /**
     * test product update
     */
    @Test
    public void testProductUpdate(){
    	String newProductname = "newName";
    	Product product = (Product) productDao.listAllProducts().get(0);
    	product.setProductName(newProductname);
    	productDao.editProduct(product);    	
    	Product newProd = (Product) productDao.getProduct(product.getProductId());
    	assertEquals(newProductname, newProd.getProductName());    	
    } 
    
    /**
     * test product delete
     */
    @Test
    public void testRemoveProduct(){
    	Product product = (Product) productDao.listAllProducts().get(0);
    	productDao.removeProduct(product.getProductId());    	
    	Product product2 = productDao.getProduct(product.getProductId());   
    	assertNull(product2);
    } 
    
    /**
     * test delete all products
     */
    @Test
    public void testDeleteAllProducts(){
    	Product product = (Product) productDao.listAllProducts().get(0);
    	productDao.deleteAllProducts();    	
    	List<Product> products = (List<Product>) productDao.listAllProducts();   
    	assertTrue(products.size() == 0);	
    } 
    
    
 
    /**
     * test adding a new sale entry
     */
    @Test
    public void testAddingSalesEntry(){
    	SalesEntry sale = new SalesEntry();
    	sale.setDateOfSale(new Date(System.currentTimeMillis()));
    	Product product = (Product) productDao.listAllProducts().get(0);
    	sale.setProductId(product.getProductId());
    	sale.setSalesAmount(100.5);
    	sale.setUnits(100);
    	salesDao.addSalesEntry(sale);
    	SalesEntry sale2 = salesDao.getSalesEntry(sale.getSalesId());
    	assertEquals(sale.getSalesAmount(), sale2.getSalesAmount(),0.001);
    	assertEquals(sale.getUnits(), sale2.getUnits());
    	assertEquals(sale.getProductId(), sale2.getProductId());
    } 
    
    
    /**
     * test updating a sale entry
     */
    @Test
    public void testUpdatingSalesEntry(){
    	SalesEntry sale = (SalesEntry) salesDao.listAllSalesEntry().get(0);
    	int oldUnits = sale.getUnits();
    	int newUnits = oldUnits+10;
    	sale.setUnits(newUnits);
    	salesDao.editSalesEntry(sale);
    	SalesEntry sale2 = salesDao.getSalesEntry(sale.getSalesId());
    	assertEquals(sale2.getUnits(), newUnits);    	
    } 
    
    
    /**
     * test removing a sale entry
     */
    @Test
    public void testRemovingSalesEntry(){
    	SalesEntry sale = (SalesEntry) salesDao.listAllSalesEntry().get(0);
    	salesDao.removeSalesEntry(sale.getSalesId());
    	SalesEntry sale2 = salesDao.getSalesEntry(sale.getSalesId());
    	assertNull(sale2);    	
    } 
    
    
    /**
     * test removing a sale entry
     */
    @Test
    public void testRemovingAllSalesEntry(){
    	salesDao.deleteAllSalesEntry();
    	int salesEntrySize = salesDao.listAllSalesEntry().size();
    	assertEquals(salesEntrySize,0);    	
    }  

}
