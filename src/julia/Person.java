package julia;

public class Person {
	private String name;
	private String surname;
	private String email;
	private String ddd;
	private String phone;
	private String employee;
	private String code;
	private String city;
	
	public Person(String name, String surname, String email, String ddd, String phone, String employee, String code,
			String city) {
		super();
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.ddd = ddd;
		this.phone = phone;
		this.employee = employee;
		this.code = code;
		this.city = city;
	}
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
}
