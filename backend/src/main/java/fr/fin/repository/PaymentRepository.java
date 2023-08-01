package fr.fin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Payment;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer>{

}
