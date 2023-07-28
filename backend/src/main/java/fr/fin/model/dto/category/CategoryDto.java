package fr.fin.model.dto.category;

public class CategoryDto {

	private Integer id;
	
	private String name;
	
	private Boolean status;
	
	public CategoryDto() { }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	
}
