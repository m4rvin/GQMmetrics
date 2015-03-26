package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;
import org.json.JSONArray;

public interface ComplexMetricManager extends GenericManager<AbstractMetric, Long> {
	
	//generic query
	public List<AbstractMetric> findAllByProject(Project project);
	public QuestionMetric getQuestionMetric(AbstractMetric metric,Question question);
	public List<String> getAvailableStatus(QuestionMetric questionMetric, User user);
	public QuestionMetric saveQuestionMetric(QuestionMetric questionMetric);
	public List<Double> getMeasuredMetricValues(Long metricId);
	public List<String> getMetricInfo(Long metricId);
	public boolean getSatisfaction(AbstractMetric m);
	

	//SimpleMetric related query
	public List<SimpleMetric> findSimpleMetricByProject(Project project);
	public SimpleMetric findSimpleMetricById(Long id);
	
	
	//CombinedMetric related query
	public List<CombinedMetric> findCombinedMetricByProject(Project project);
	public CombinedMetric findCombinedMetricById(Long id);
	public JSONArray findByMeasurementScaleType(MeasurementScaleTypeEnum type);
	
}
