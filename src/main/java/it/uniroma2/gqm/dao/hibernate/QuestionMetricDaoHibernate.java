package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.QuestionMetricDao;
import it.uniroma2.gqm.model.QuestionMetric;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


@Repository("questionMetroDao")
public class QuestionMetricDaoHibernate extends GenericDaoHibernate<QuestionMetric, Long> implements QuestionMetricDao {
	
	
    public QuestionMetricDaoHibernate() {
        super(QuestionMetric.class);
    }

	@Override
	public QuestionMetric getQuestionMetric(Long questionId,Long metricId){
    	List<QuestionMetric> metrics;
    	Query q =  getSession().getNamedQuery("findQuestionMetric").setLong("question_id", questionId).setLong("metric_id", metricId);
    	metrics = q.list();
    	return (metrics!=null && metrics.size()>0) ? metrics.get(0) : null;
	}
}