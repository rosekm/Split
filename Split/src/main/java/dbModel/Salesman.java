package dbModel;

//tableName = "Salesmen"
public class Salesman {

	//column "id"
	private Integer id;
	//column "name"
	private String name;
	//column "surmane"
	private String surname;
	//column "branch"
	private Branch branch;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Branch getBranch() {
		return branch;
	}

	public void setBranch(Branch branch) {
		this.branch = branch;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}
