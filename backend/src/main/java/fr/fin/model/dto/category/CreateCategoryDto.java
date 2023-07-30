package fr.fin.model.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateCategoryDto {

	@NotNull
	@Pattern(regexp = "[a-zA-Z][a-zA-Z ]+", message = "Le nom ne peut contenir que des lettres")
	private String name;

	public CreateCategoryDto() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
