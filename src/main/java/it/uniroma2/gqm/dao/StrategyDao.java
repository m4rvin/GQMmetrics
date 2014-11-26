package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.Strategy;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface StrategyDao  extends GenericDao<Strategy, Long> {
    public List<Strategy> findByProject(Long id);       
}