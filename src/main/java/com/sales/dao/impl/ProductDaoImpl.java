package com.sales.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;



import com.sales.dao.ProductDao;
import com.sales.model.Product;

/**
 * Represents DAO implementation of Product DAO
 * @author Admin
 *
 */

public class ProductDaoImpl implements ProductDao{	

	private SessionFactory sessionFactory;
	
    /**
     * initialize session factory
     * @param sessionFactory
     */
	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	/**
	 * get the product id
	 */
	public Product getProduct(int id) {
		return (Product) sessionFactory.openSession().get(Product.class, id);
	}

	/**
	 * add product to database
	 */
	public void addProduct(Product product) {
		sessionFactory.openSession().save(product);
	}

	/**
	 * remove product from database
	 */
	public void removeProduct(int id) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.delete(getProduct(id));
		tx.commit();
		session.close();

	}

	/**
	 * update product details in database
	 */
	public void editProduct(Product product) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.update(product);		
		tx.commit();
		session.close();
	}

	/**
	 * Get the list of all products from the database
	 */
	public List<?> listAllProducts() {
		return sessionFactory.openSession().createQuery("from Product").list();
	}

	
	/**
	 * delete all products from the database
	 */
	public void deleteAllProducts() {
		List<Product> products = (List<Product>) listAllProducts();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for(Product product : products){
			session.delete(product);			
								
		}
		tx.commit();	
		session.close();
	}	
}