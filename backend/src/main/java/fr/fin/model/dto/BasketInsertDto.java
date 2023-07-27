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
	

	/*
	 * class CartLine {
    private id: number;
    private name: string;
    private price: number;
    private discount: number;
    private quantity: number;
    
    
    class Cart {
    private cartLines: CartLine[];
    private total: number;
    private discount: number;
    
    class Basket {
    private cart : Cart;
    private discount : number;
    private total : number = 0;
    
    
	 * */
}
