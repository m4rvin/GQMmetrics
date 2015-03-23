package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.GenericMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.QuestionMetric;

import java.util.List;
import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


@Repository("genericMetricDao")
public class GenericMetricDaoHibernate extends GenericDaoHibernate<AbstractMetric, Long>  implements GenericMetricDao {
		
    public GenericMetricDaoHibernate() {
        super(AbstractMetric.class);
    }
 
    public List<AbstractMetric> findByProject(Long id) {
    	Query q =  getSession().getNamedQuery("findMetricByProject").setLong("project_id", id);
    	return q.list();
    }

	@Override
	public AbstractMetric save(AbstractMetric object) {
		if(object.getId() != null){			
			AbstractMetric mTemp = get(object.getId());
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
   public List<AbstractMetric> findByMeasurementScale(Long id)
   {
   	 Query q = getSession().getNamedQuery("findByMeasurementScale").setLong("measurementScaleId", id);
   	 return q.list();
   }

}
