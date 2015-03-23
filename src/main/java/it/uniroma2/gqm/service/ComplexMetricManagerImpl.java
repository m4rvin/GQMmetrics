package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Project;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("complexMetricManager")
public class ComplexMetricManagerImpl extends GenericManagerImpl<AbstractMetric, Long> implements ComplexMetricManager {

    private ComplexMetricDao metricDao;

	@Autowired
    public ComplexMetricManagerImpl(ComplexMetricDao metricDao) {
        super(metricDao);
        this.metricDao = metricDao;
    }
	
	@Override
	public List<AbstractMetric> findByProject(Project project) {
		if(project !=null)
    		return metricDao.findByProject(project.getId());
    	else
    		return null;
	}

}
