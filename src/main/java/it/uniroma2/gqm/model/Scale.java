package it.uniroma2.gqm.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.appfuse.model.BaseObject;

@Entity
@NamedQueries({
    @NamedQuery(
            name = "findScales",
            query = "select s from Scale s "
    )
})
public class Scale   extends BaseObject  implements Serializable {
	private Long id;
	private String name;
	private String description;
	private String type;	
	private String examples;
	private String operations;
	
	@Id
	@Column(name="scale_id")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="name", length=255)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Scale other = (Scale) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Scale [id=" + id + ", name=" + name + "]";
	}
	
	@Column(name="operations", length=255)
	public String getOperations() {
		return operations;
	}
	
	public void setOperations(String operations) {
		this.operations = operations;
	}
	
	@Column(name="description", length=1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name="type", length=1000)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name="examples", length=4000)
	public String getExamples() {
		return examples;
	}
	public void setExamples(String examples) {
		this.examples = examples;
	}
	
	
}