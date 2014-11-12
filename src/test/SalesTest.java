package Hibernate_Eclipse.com.hibernate;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sales.dao.impl.ProductDaoImpl;
import com.sales.dao.impl.SalesEntryDaoImpl;
import com.sales.model.Product;
import com.sales.model.SalesEntry;
import com.sales.report.ReportBuilder;

/**
 * Unit test for simple App.
 */


public class SalesTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
	
	ApplicationContext ac;	
	ProductDaoImpl productDao ;	
	SalesEntryDaoImpl salesDao ;	
	final int numOfProducts  = 10;
	final int numOfSales = numOfProducts * 2 ; 
	
	@Before
	public void setUp()
	{
		ac = new FileSystemXmlApplicationContext("F:\\sample6\\salesproject\\spring.xml");
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
		//productDao.deleteAllProducts();
		//salesDao.deleteAllSalesEntry();
	}
	
	public void addProducts(){
		for(int i = 0 ; i < numOfProducts; i++){
			Product prod = new Product();
			//prod.setProductId(i);
			prod.setProductName("Product"+i);
			productDao.addProduct(prod);
		}
	}
	
	public void addSalesEntry(){
		List<Product> products = (List<Product>) productDao.listAllProducts();
		for(int i = 0, j = 0 ; i < numOfSales ; i = i+2, j++){
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
	
	
	public SalesTest(){
		super( "AppTest" );
	}

    /**
     * @return the suite of tests being tested
     */
    public static Test suite(){
        return new TestSuite( SalesTest.class );
    }


    public void testProductNumbers(){
    	System.out.println("productdao.listAllProducts().size()===" + productDao.listAllProducts().size());
    	assertTrue(productDao.listAllProducts().size() == numOfProducts );
    }
    
    public void testSalesNumber(){  	
    	System.out.println("salesDao===" + salesDao.listAllSalesEntry().size());
    	assertTrue(salesDao.listAllSalesEntry().size() == numOfSales );
    }
}
