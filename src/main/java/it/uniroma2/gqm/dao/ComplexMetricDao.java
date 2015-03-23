package it.uniroma2.gqm.dao;


import it.uniroma2.gqm.model.AbstractMetric;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface ComplexMetricDao extends GenericDao<AbstractMetric, Long> {
	public List<AbstractMetric> findByProject(Long id);
}
