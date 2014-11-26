package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.Scale;
import it.uniroma2.gqm.model.Unit;

import java.util.List;
import org.appfuse.dao.GenericDao;

public interface MetricDao extends GenericDao<Metric, Long> {
	public List<Metric> findByProject(Long id);       
}
