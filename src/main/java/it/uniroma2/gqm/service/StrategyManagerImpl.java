package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.StrategyDao;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Strategy;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("strategyManager")
public class StrategyManagerImpl  extends GenericManagerImpl<Strategy, Long> implements StrategyManager {
    @Autowired
	private StrategyDao strategyDao;
	
    @Autowired
    public StrategyManagerImpl(StrategyDao strategyDao) {
        super(strategyDao);
    }
 
    public List<Strategy> findByProject(Project project) {
    	if(project !=null)
    		return strategyDao.findByProject(project.getId());
    	else
    		return null;
    }
    
    /*@Override
	public boolean hasChildren(Strategy s){
		if(s.getSorgChild().size() >0 || s.getStrategyChild().size() > 0)
			return true;
		else
			return false;
	}
	
	@Override
	public boolean hasParent(Strategy s){
		if (s.getSorgParent() != null || s.getStrategyParent() != null) {
			return true;
		}else {
			return false;
		}
	}*/
}
