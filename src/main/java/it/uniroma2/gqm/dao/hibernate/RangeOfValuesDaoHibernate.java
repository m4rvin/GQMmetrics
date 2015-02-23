package it.uniroma2.gqm.dao.hibernate;

import java.util.List;

import it.uniroma2.gqm.dao.RangeOfValuesDao;
import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.RangeOfValues;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;



@Repository("rangeOfValuesDao")
public class RangeOfValuesDaoHibernate extends GenericDaoHibernate<RangeOfValues, Long>
		implements RangeOfValuesDao {

	public RangeOfValuesDaoHibernate() {
		super(RangeOfValues.class);
	}

	@Override
	public List<RangeOfValues> findByProject(Long id) {
		Query q =  getSession().getNamedQuery("findRangeOfValuesByProject").setLong("project_id", id);
    	return q.list();
	}
	
	@Override
	public RangeOfValues save(RangeOfValues object) {
		getSession().saveOrUpdate(object);
        getSession().flush();		
		return super.save(object);
	}



}
