package com.sales.dao;

import java.util.List;

import com.sales.model.Product;

public interface ProductDao {
	public Product getProduct(int id);
	public void addProduct(Product product);
	public void removeProduct(int id);
	public void editProduct(Product product);
	public List listAllProducts();
	public void deleteAllProducts();
}
