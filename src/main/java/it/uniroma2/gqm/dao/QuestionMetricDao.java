package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.QuestionMetric;

import org.appfuse.dao.GenericDao;

public interface QuestionMetricDao extends GenericDao<QuestionMetric, Long> {
    public QuestionMetric getQuestionMetric(Long questionId,Long metricId); 
}