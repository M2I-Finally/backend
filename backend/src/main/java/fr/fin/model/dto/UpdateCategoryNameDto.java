package fr.fin.model.dto;

import jakarta.validation.constraints.NotNull;

public class UpdateCategoryNameDto {
	
	@NotNull
	private String name;
	
	public UpdateCategoryNameDto() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
