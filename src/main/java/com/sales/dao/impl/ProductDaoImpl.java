package com.sales.dao.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.sales.dao.ProductDao;
import com.sales.model.Product;

/**
 * @author Admin
 *
 */


public class ProductDaoImpl implements ProductDao{	

	private SessionFactory sessionFactory;
	
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

	public Product getProduct(int id) {
		return (Product) sessionFactory.openSession().get(Product.class, id);
	}

	public void addProduct(Product product) {
		sessionFactory.openSession().save(product);
	}

	public void removeProduct(int id) {
		sessionFactory.openSession().delete(getProduct(id));

	}

	public void editProduct(Product product) {
		sessionFactory.openSession().update(product);		
	}

	public List<?> listAllProducts() {
		return sessionFactory.openSession().createQuery("from Product").list();
	}

	public void deleteAllProducts() {
		List<Product> products = (List<Product>) listAllProducts();
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		for(Product product : products){
			session.delete(product);			
								
		}
		tx.commit();	
	}	
}