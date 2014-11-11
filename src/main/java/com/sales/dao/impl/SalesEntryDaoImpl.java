package com.sales.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.sales.dao.SalesEntryDao;
import com.sales.model.Product;
import com.sales.model.SalesEntry;


/**
 * Represents SalesEntryDao implementation
 * @author Admin
 *
 */
public class SalesEntryDaoImpl implements SalesEntryDao {
	

	private SessionFactory sessionFactory ;
	
	
	/**
	 * initialize session factory
	 * @param sessionFactory
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
	
	public SalesEntryDaoImpl(){		
	}


	/**
	 * based on sales id get the sales entry
	 */
	public SalesEntry getSalesEntry(int salesId) {
		return (SalesEntry) sessionFactory.openSession().get(SalesEntry.class, salesId);
	}


	/**
	 * add a new sales entry in database
	 */	
	public void addSalesEntry(SalesEntry salesEntry) {
		sessionFactory.openSession().save(salesEntry);

	}

	/**
	 * remove sales entry from the database
	 */
	public void removeSalesEntry(int salesId) {
		sessionFactory.openSession().delete(getSalesEntry(salesId));

	}

	/**
	 * 	Updates sales entry in the database
	 */
	public void editSalesEntry(SalesEntry salesEntry) {
		sessionFactory.openSession().update(salesEntry);	
	}


	/**
	 * get list of all sales entries from the database
	 */
	public List<?> listAllSalesEntry() {
		return sessionFactory.openSession().createQuery("from SalesEntry").list();
	}

	/**
	 * delete all sales entries from the database
	 */
	public void deleteAllSalesEntry() {
		List<SalesEntry> sales = (List<SalesEntry>) listAllSalesEntry();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for(SalesEntry sale : sales){
			session.delete(sale);			
								
		}
		tx.commit();	
	}

	/**
	 * based on product id get the list of sales entries from the database
	 */
	public List listAllSalesForProduct(int id) {
		return sessionFactory.openSession().createQuery("from SalesEntry where productId = " +id).list();
	}	
}