package dbModel;

//Table "Branches"
public class Branch {
	
	//Column "Id"
	Integer id;
	
	//Column "Name"
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
}
