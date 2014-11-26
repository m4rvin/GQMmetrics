package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.BinaryTable;
import it.uniroma2.gqm.model.Goal;

import java.util.Set;

import org.appfuse.service.GenericManager;

public interface BinaryTableManager extends GenericManager<BinaryTable, Long>{

	public Set<Goal> findOGChildren(Object obj);
}
