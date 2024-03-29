package it.uniroma2.gqm.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class QuestionMetricPK implements Serializable {
	private static final long serialVersionUID = 8216954247562866404L;
	private Question question;
	private AbstractMetric metric;
	
	public QuestionMetricPK(){
		;
	}
	
	public QuestionMetricPK(Question q, AbstractMetric m){	
		this.question = q;
		this.metric = m;
	}
	
	@ManyToOne
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	
	@ManyToOne
	public AbstractMetric getMetric() {
		return metric;
	}
	public void setMetric(AbstractMetric metric) {
		this.metric = metric;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((metric == null) ? 0 : metric.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
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
		QuestionMetricPK other = (QuestionMetricPK) obj;
		if (metric == null) {
			if (other.metric != null)
				return false;
		} else if (!metric.getId().equals(other.metric.getId()))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.getId().equals(other.question.getId()))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "QuestionMetricPK [question=" + question + ", metric=" + metric + "]";
	}
	
	
}