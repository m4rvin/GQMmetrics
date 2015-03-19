package it.uniroma2.gqm.model;

import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@DiscriminatorValue("combined")
@Table(name = "CombinedMetric")
public class CombinedMetric extends AbstractMetric {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5194614013197863177L;

	protected Set<AbstractMetric> composedBy;

	
	 @ManyToMany(fetch = FetchType.LAZY)
	 @JoinTable(name = "metriccomposedby_metriccomposerfor",
			 joinColumns = {@JoinColumn(name = "metriccomposedby_id", referencedColumnName="metric_id")},
				inverseJoinColumns = {@JoinColumn(name = "metriccomposerfor_id", referencedColumnName="metric_id")}
			  	)
	public Set<AbstractMetric> getComposedBy() {
		return composedBy;
	}

	public void setComposedBy(Set<AbstractMetric> composedBy) {
		this.composedBy = composedBy;
	}
	
	
	
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

}
