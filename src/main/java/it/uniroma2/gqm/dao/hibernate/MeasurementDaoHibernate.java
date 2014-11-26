package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.MeasurementDao;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.Metric;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("measurementDao")
public class MeasurementDaoHibernate extends GenericDaoHibernate<Measurement, Long>  implements MeasurementDao {
		
    public MeasurementDaoHibernate() {
        super(Measurement.class);
    }
    public List<Measurement> findMeasuremntsByMetric(Metric metric){
    	Query q =  getSession().getNamedQuery("findMeasuremntsByMetric").setLong("metric_id", metric.getId());
    	return q.list();
    }
    
	@Override
	public List<Measurement> findByProject(Long id) {
		     	Query q =  getSession().getNamedQuery("findMeasurementByProject").setLong("project_id", id);
    	return q.list();
	}
}
