package it.uniroma2.gqm.model;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.appfuse.model.BaseObject;


@Entity
@Table(name = "question_metric")
@AssociationOverrides({ @AssociationOverride(name = "pk.metric", joinColumns = @JoinColumn(name = "metric_id")),
						@AssociationOverride(name = "pk.question", joinColumns = @JoinColumn(name = "question_id")) })
@NamedQueries({
    @NamedQuery(
            name = "findQuestionMetric",
            query = "select qm from QuestionMetric qm  where qm.pk.question.id = :question_id and  qm.pk.metric.id = :metric_id"
    )
})
public class QuestionMetric extends BaseObject {
	private static final long serialVersionUID = -5926420204193701080L;
	private QuestionMetricPK pk;
	private QuestionMetricStatus status;
	private String refinement;

	
	@EmbeddedId
	public QuestionMetricPK getPk() {
		return pk;
	}

	public void setPk(QuestionMetricPK pk) {
		this.pk = pk;
	}
	
	@Transient
	public Question getQuestion(){
		return this.pk.getQuestion();
	}

	@Transient
	public AbstractMetric getMetric(){
		return this.pk.getMetric();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pk == null) ? 0 : pk.hashCode());
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
		QuestionMetric other = (QuestionMetric) obj;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "QuestionMetric [question=" + pk.getQuestion() + ", metric=" + this.pk.getMetric() + "]";
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public QuestionMetricStatus getStatus() {
		return status;
	}

	public void setStatus(QuestionMetricStatus status) {
		this.status = status;
	}
	
	@Column(name = "refinement", length = 255)
	public String getRefinement() {
		return refinement;
	}

	public void setRefinement(String refinement) {
		this.refinement = refinement;
	}
	
}
