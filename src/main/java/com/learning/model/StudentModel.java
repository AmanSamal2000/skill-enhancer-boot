package com.learning.model;

import java.util.Objects;

public class StudentModel {

	private Long id;
	private String name;
	private Long contactDetails;
	private String qualification;
	private String email;
	
	/* All of below boiler plate code will be replaced by lombok
	 * @Getter @Setter @AllArgsConstructor @NoArgsConstructor 
	 * @Equals @Hashcode
	 *  */
	
	public StudentModel() {}

	public StudentModel(Long id, String name, Long contactDetails, String qualification, String email) {
		super();
		this.id = id;
		this.name = name;
		this.contactDetails = contactDetails;
		this.qualification = qualification;
		this.email = email;
	}

	public Long getId() {return id;}

	public void setId(Long id) {this.id = id;}

	public String getName() {return name;}

	public void setName(String name) {this.name = name;}

	public Long getContactDetails() {return contactDetails;}

	public void setContactDetails(Long contactDetails) {this.contactDetails = contactDetails;}

	public String getQualification() {return qualification;}

	public void setQualification(String qualification) {this.qualification = qualification;}

	public String getEmail() {return email;}

	public void setEmail(String email) {this.email = email;}

	@Override
	public int hashCode() {
		return Objects.hash(contactDetails, email, id, name, qualification);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StudentModel other = (StudentModel) obj;
		return Objects.equals(contactDetails, other.contactDetails) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(qualification, other.qualification);
	}
	
	@Override
	public String toString() {
		return "StudentEntity [id=" + id + ", name=" + name + ", contactDetails=" + contactDetails + ", qualification="
				+ qualification + ", email=" + email + "]";
	}
	
}
