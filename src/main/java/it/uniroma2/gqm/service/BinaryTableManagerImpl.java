package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.BinaryTable;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Strategy;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.stereotype.Service;

@Service("binaryManager")
public class BinaryTableManagerImpl extends GenericManagerImpl<BinaryTable, Long> implements BinaryTableManager {

	@Override
	public Set<Goal> findOGChildren(Object obj) {
		
		Set<Goal> childList = new HashSet<Goal>();
		
		if(obj instanceof Goal) {
			
			Goal g = (Goal) obj;
			if(g.hasChildren()) { //ha figli
				if(g.areChildrenGoal()) { //i figli sono goal
					childList = g.getOrgChild();
				} else { //i figli sono strategy
					Set<Strategy> children = g.getOstrategyChild();
					for (Strategy strategy : children) {
						childList.addAll(findOGChildren(strategy));
					}
				}
			}
		} else if(obj instanceof Strategy) {
			
			Strategy stra = (Strategy)obj;
			if(stra.hasChildren()) { //ha figli
				if(stra.areChildrenGoal()) { //i figli sono goal
					childList = stra.getSorgChild();
				} else { //i figli sono strategy
					Set<Strategy> children = stra.getStrategyChild();
					for (Strategy strategy : children) {
						childList.addAll(findOGChildren(strategy));
					}
				}
			} 
		}
		return childList;
	}

}
