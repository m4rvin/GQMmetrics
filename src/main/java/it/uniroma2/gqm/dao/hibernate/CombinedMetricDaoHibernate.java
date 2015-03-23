package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.CombinedMetricDao;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.QuestionMetric;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


@Repository("combinedMetricDao")
public class CombinedMetricDaoHibernate extends GenericDaoHibernate<CombinedMetric, Long>  implements CombinedMetricDao {
		
    public CombinedMetricDaoHibernate() {
        super(CombinedMetric.class);
    }
 
    public List<CombinedMetric> findByProject(Long id) {
    	Query q =  getSession().getNamedQuery("findMetricByProject").setLong("project_id", id);
    	return q.list();
    }

	@Override
	public CombinedMetric save(CombinedMetric object) {
		if(object.getId() != null){			
			CombinedMetric mTemp = get(object.getId());
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
   public List<CombinedMetric> findByMeasurementScale(Long id)
   {
   	 Query q = getSession().getNamedQuery("findByMeasurementScale").setLong("measurementScaleId", id);
   	 return q.list();
   }

}
