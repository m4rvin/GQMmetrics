package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.GoalStatus;
import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.Scale;
import it.uniroma2.gqm.model.Unit;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

@WebService
public interface MetricManager extends GenericManager<Metric, Long> {
	public List<Metric> findByProject(Project project);
	
	public QuestionMetric getQuestionMetric(Metric metric,Question question);
	public List<String> getAvailableStatus(QuestionMetric questionMetric, User user);
	public QuestionMetric saveQuestionMetric(QuestionMetric questionMetric);
	
	public Metric findById(Long id);
	public List<Double> getMeasuredMetricValues(Long metricId);
	public List<String> getMetricInfo(Long metricId);
	public boolean getSatisfaction(Metric m);
}