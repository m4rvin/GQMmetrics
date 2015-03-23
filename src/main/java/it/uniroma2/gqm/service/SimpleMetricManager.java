package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;

import java.util.List;
import javax.jws.WebService;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

@WebService
public interface SimpleMetricManager extends GenericManager<SimpleMetric, Long> {
	public List<SimpleMetric> findByProject(Project project);
	
	public QuestionMetric getQuestionMetric(SimpleMetric metric,Question question);
	public List<String> getAvailableStatus(QuestionMetric questionMetric, User user);
	public QuestionMetric saveQuestionMetric(QuestionMetric questionMetric);
	
	public SimpleMetric findById(Long id);
	public List<Double> getMeasuredMetricValues(Long metricId);
	public List<String> getMetricInfo(Long metricId);
	public boolean getSatisfaction(SimpleMetric m);
}