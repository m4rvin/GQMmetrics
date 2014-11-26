package it.uniroma2.gqm.dao;


import org.appfuse.dao.GenericDao;
import it.uniroma2.gqm.model.*;
 
import java.util.List;
 
public interface QuestionDao extends GenericDao<Question, Long> {
    public List<Question> findByProject(Long id);       
}