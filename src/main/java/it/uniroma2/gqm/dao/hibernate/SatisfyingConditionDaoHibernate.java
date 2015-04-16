package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.SatisfyingConditionDao;
import it.uniroma2.gqm.model.SatisfyingCondition;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "satisfyingConditionDao")
public class SatisfyingConditionDaoHibernate extends GenericDaoHibernate<SatisfyingCondition, Long> implements SatisfyingConditionDao
{

	 public SatisfyingConditionDaoHibernate()
	 {
		  super(SatisfyingCondition.class);
	 }

	 @Override
	 public List<SatisfyingCondition> findSatisfyingConditionByProject(Long id)
	 {
		  Query q = getSession().getNamedQuery("findSatisfyingConditionByProject").setLong("project_id", id);
		  return q.list();
	 }

	 

}
