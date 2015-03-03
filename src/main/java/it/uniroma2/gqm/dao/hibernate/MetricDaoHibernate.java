package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.MetricDao;
import it.uniroma2.gqm.dao.QuestionDao;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.Scale;
import it.uniroma2.gqm.model.Unit;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;


@Repository("metricDao")
public class MetricDaoHibernate extends GenericDaoHibernate<Metric, Long>  implements MetricDao {
		
    public MetricDaoHibernate() {
        super(Metric.class);
    }
 
    public List<Metric> findByProject(Long id) {
    	Query q =  getSession().getNamedQuery("findMetricByProject").setLong("project_id", id);
    	return q.list();
    }

	@Override
	public Metric save(Metric object) {
		if(object.getId() != null){			
			Metric mTemp = get(object.getId());
			for(QuestionMetric qm: object.getQuestions()){
				if(!mTemp.getQuestions().contains(qm)){
					getSession().saveOrUpdate(qm);
				}
			}
			for(QuestionMetric qmTemp: mTemp.getQuestions()){
				if(!object.getQuestions().contains(qmTemp)){
					getSession().delete(qmTemp);
				}
			}			
		}else {
			getSession().saveOrUpdate(object);
		}
		try{
			getSession().merge(object);
		} catch(Exception e){
			getSession().saveOrUpdate(object);
		}
        getSession().flush();		
		return super.save(object);
	}

   @Override
   public List<Metric> findByMeasurementScale(Long id)
   {
   	 Query q = getSession().getNamedQuery("findByMeasurementScale").setLong("measurementScaleId", id);
   	 return q.list();
   }

}
