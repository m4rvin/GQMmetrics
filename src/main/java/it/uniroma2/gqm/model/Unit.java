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
            name = "findUnits",
            query = "select u from Unit u "
    )
})
public class Unit  extends BaseObject  implements Serializable {
	private Long id;
	private String name;
	private String symbol;
	private String physical;
	private boolean baseUnit;
	private long multiples;
	
	@Id
	@Column(name="unit_id")
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
		Unit other = (Unit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + "]";
	}
	@Column(name="symbol", length=50)
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
	@Column(name="physical", length=50)
	public String getPhysical() {
		return physical;
	}
	public void setPhysical(String physical) {
		this.physical = physical;
	}
	
	@org.hibernate.annotations.Type(type="true_false")
	@Column(name="base_unit", nullable=true)
	public boolean isBaseUnit() {
		return baseUnit;
	}
	public void setBaseUnit(boolean baseUnit) {
		this.baseUnit = baseUnit;
	}
	
	
	@Column(name="multiples",nullable=true)
	public long getMultiples() {
		return multiples;
	}
	public void setMultiples(long multiples) {
		this.multiples = multiples;
	}

}
