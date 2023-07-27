package fr.fin.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.fin.model.entity.BasketDetail;
import fr.fin.model.entity.Payment;
import fr.fin.model.entity.Product;


public class BasketInsertDto {
	
	
	private Float discount;
	
	private Date createdAt;
	
	private String seller;
	
	private Float total;
	
	private List<Product> products = new ArrayList<Product>();
	
	private List<Payment> payments = new ArrayList<Payment>();
	

	
}
