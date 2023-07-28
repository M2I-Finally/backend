package fr.fin.model.dto.category;

import jakarta.validation.constraints.NotNull;

public class CreateCategoryDto {

	@NotNull
	private String name;
	
	public CreateCategoryDto() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		
}
