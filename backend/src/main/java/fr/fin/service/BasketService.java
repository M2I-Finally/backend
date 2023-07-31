package fr.fin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.fin.repository.BasketRepository;


@Service
public class BasketService {
	
	@Autowired
	private BasketRepository basketRepository;

}
