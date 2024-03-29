package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.dao.QuestionMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.GoalStatus;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("complexMetricManager")
@WebService(serviceName = "complexmetricservice", endpointInterface = "it.uniroma2.gqm.service.ComplexMetricManager")
public class ComplexMetricManagerImpl extends GenericManagerImpl<AbstractMetric, Long> implements ComplexMetricManager
{

	 private ComplexMetricDao metricDao;
	 private QuestionMetricDao questionMetricDao;

	 @Autowired
	 public ComplexMetricManagerImpl(ComplexMetricDao metricDao, QuestionMetricDao questionMetricDao)
	 {
		  super(metricDao);
		  this.metricDao = metricDao;
		  this.questionMetricDao = questionMetricDao;
	 }

	 @Override
	 public List<AbstractMetric> findAllByProject(Project project)
	 {
		  return metricDao.findAllMetricByProject(project);
	 }

	 @Override
	 public QuestionMetric getQuestionMetric(AbstractMetric metric, Question question)
	 {
		  return questionMetricDao.getQuestionMetric(question.getId(), metric.getId());
	 }

	 @Override
	 public List<String> getAvailableStatus(QuestionMetric questionMetric, User user)
	 {
		  List<String> ret = new ArrayList<String>();
		  switch (questionMetric.getStatus())
		  {
		  case PROPOSED:
				ret.add(GoalStatus.PROPOSED.toString());
				if (questionMetric.getQuestion().getQuestionOwner().equals(user))
				{ // only Question Stakeholder owner can approve a metric...
					 ret.add(GoalStatus.FOR_REVIEW.toString());
					 ret.add(GoalStatus.APPROVED.toString());
					 ret.add(GoalStatus.REJECTED.toString());
				}
				break;
		  case FOR_REVIEW:
				ret.add(GoalStatus.FOR_REVIEW.toString());
				if (questionMetric.getMetric().getMetricOwner().equals(user))
				{ // only MMDM owner can propose a metric...
					 ret.add(GoalStatus.PROPOSED.toString());
				}
				break;
		  case APPROVED:
				ret.add(GoalStatus.APPROVED.toString());
				break;
		  case REJECTED:
				ret.add(GoalStatus.APPROVED.toString());
				break;
		  }
		  return ret;
	 }

	 @Override
	 public QuestionMetric saveQuestionMetric(QuestionMetric questionMetric)
	 {
		  return questionMetricDao.save(questionMetric);
	 }

	 @Override
	 public List<String> getMetricInfo(Long metricId)
	 {
		  AbstractMetric metric = get(metricId);
		  List<String> ret = new ArrayList<String>();
		  ret.add("Id: " + (metric.getId()));
		  ret.add("Name: " + (metric.getName()));
		  ret.add("Type: " + (metric.getType()));

		  // TODO measurement scale
		  ret.add("Collecting Type: " + (metric.getCollectingType()));
		  return ret;
	 }

	 @Override
	 public List<SimpleMetric> findSimpleMetricByProject(Project project)
	 {
		  return metricDao.findSimpleMetricByProject(project);
	 }

	 @Override
	 public SimpleMetric findSimpleMetricById(Long id)
	 {
		  return metricDao.findSimpleMetricById(id);
	 }
	 
	 @Override
	 public List<SimpleMetric> findMeasureableSimpleMetricByProject(
			Project project) {
		 return metricDao.findMeasureableSimpleMetricByProject(project);
	 }

	 @Override
	 public List<CombinedMetric> findCombinedMetricByProject(Project project)
	 {
		  return metricDao.findCombinedMetricByProject(project);
	 }

	 @Override
	 public CombinedMetric findCombinedMetricById(Long id)
	 {
		  return metricDao.findCombinedMetricById(id);
	 }
	
	 @Override
	 public List<AbstractMetric> findMetricByMeasurementScaleType(MeasurementScaleTypeEnum type)
	 {
		  if(type != null)
		  {
				return this.metricDao.findMetricByMeasurementScaleType(type);
		  }
		  return null;
	 }
	 
	 @Override
	 public List<AbstractMetric> findMetricByMeasurementScaleTypeExludingOneById(MeasurementScaleTypeEnum type, Long metricToExcludeId)
	 {
		  if(type != null)
		  {
				return this.metricDao.findMetricByMeasurementScaleTypeExludingOneById(type, metricToExcludeId);
		  }
		  return null;
	 }
/*
	 @Override
	 public JSONArray findByMeasurementScaleTypeJSONized(MeasurementScaleTypeEnum type)
	 {
		  if(type != null)
		  {
				return new JSONArray(this.metricDao.findByMeasurementScaleType(type));
		  }
		  return null;
	 }
*/
	 @Override
	 public AbstractMetric findMetricByName(String name) throws IndexOutOfBoundsException
	 {
		  if(name != null)
				return this.metricDao.findMetricByName(name);
		  return null;
	 }

	 @Override
	 public List<AbstractMetric> findMetricByOwner(User user)
	 {
		  if(user != null)
		  {
				Long owner_id = user.getId();
				return this.metricDao.findMetricByOwner(owner_id);
		  }
		  else
				return null;
	 }
}