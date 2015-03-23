package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;

import java.util.List;

import javax.jws.WebService;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

@WebService
public interface CombinedMetricManager extends GenericManager<CombinedMetric, Long> {
	public List<CombinedMetric> findByProject(Project project);
	
	public QuestionMetric getQuestionMetric(CombinedMetric metric, Question question);
	public List<String> getAvailableStatus(QuestionMetric questionMetric, User user);
	public QuestionMetric saveQuestionMetric(QuestionMetric questionMetric);
	
	public CombinedMetric findById(Long id);
	public List<Double> getMeasuredMetricValues(Long metricId);
	public List<String> getMetricInfo(Long metricId);
	public boolean getSatisfaction(CombinedMetric m);
}