package fr.fin.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.fin.model.entity.Category;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Integer> {

	List<Category> findAllByDeletedFalseOrderById();

	Category findCategoryByNameIgnoreCaseAndDeletedFalse(String name);
}
