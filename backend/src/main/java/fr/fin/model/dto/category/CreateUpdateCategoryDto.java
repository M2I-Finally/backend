package fr.fin.model.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateUpdateCategoryDto {

	@NotNull
	@Pattern(regexp = "[A-Za-zÀ-ÖØ-öø-ÿ0-9  ']+", message = "Le nom ne peut contenir que des lettres et des espaces")
	private String name;

	public CreateUpdateCategoryDto() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
