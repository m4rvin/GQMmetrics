package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.dao.QuestionMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.GoalStatus;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("complexMetricManager")
public class ComplexMetricManagerImpl extends GenericManagerImpl<AbstractMetric, Long> implements ComplexMetricManager {

    private ComplexMetricDao metricDao;
    private QuestionMetricDao questionMetricDao;

	@Autowired
    public ComplexMetricManagerImpl(ComplexMetricDao metricDao, QuestionMetricDao questionMetricDao) {
        super(metricDao);
        this.metricDao = metricDao;
        this.questionMetricDao = questionMetricDao;
    }
	

	@Override
	public QuestionMetric getQuestionMetric(AbstractMetric metric,
			Question question) {
		return questionMetricDao.getQuestionMetric(question.getId(), metric.getId());
	}

	@Override
	public List<String> getAvailableStatus(QuestionMetric questionMetric,
			User user) {
		List<String> ret = new ArrayList<String>();
		switch (questionMetric.getStatus()) {
			case PROPOSED:
				ret.add(GoalStatus.PROPOSED.toString());				
				if(questionMetric.getQuestion().getQuestionOwner().equals(user)){			// only Question Stakeholder owner can approve a metric...
					ret.add(GoalStatus.FOR_REVIEW.toString());
					ret.add(GoalStatus.APPROVED.toString());
					ret.add(GoalStatus.REJECTED.toString());
				}
				break;
			case FOR_REVIEW:
				ret.add(GoalStatus.FOR_REVIEW.toString());				
				if(questionMetric.getMetric().getMetricOwner().equals(user)){			// only MMDM owner can propose a metric...
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
	public QuestionMetric saveQuestionMetric(QuestionMetric questionMetric) {
		return questionMetricDao.save(questionMetric);
	}

	@Override
	public List<Double> getMeasuredMetricValues(Long metricId) {
		AbstractMetric metric = get(metricId);
		List<Double> ret = new ArrayList<Double>();
		Iterator<Measurement> it = metric.getMeasurements().iterator();
		if(it.hasNext()){
			while (it.hasNext()) {
				Measurement mm = it.next();
				ret.add(mm.getValue());
			}
		}
		return ret;
	}

	@Override
	public List<String> getMetricInfo(Long metricId) {
		AbstractMetric metric = get(metricId);
		List<String> ret = new ArrayList<String>();
		ret.add("Id: " + (metric.getId()) );
		ret.add("Name: " + (metric.getName()));
		if(metric.getUnit() != null)
			ret.add("Unit: " + (metric.getUnit().getName()));
		ret.add("Type: " + (metric.getType()));

		//TODO measurement scale
		ret.add("Collecting Type: " + (metric.getCollectingType()));
		return ret;
	}

	@Override
	public boolean getSatisfaction(AbstractMetric m) {
		return m.isConditionReached();
	}

	@Override
	public List<SimpleMetric> findSimpleMetricByProject(Project project) {
		return metricDao.findSimpleMetricByProject(project);
	}

	@Override
	public SimpleMetric findSimpleMetricById(Long id) {
		return metricDao.findSimpleMetricById(id);
	}

	@Override
	public List<CombinedMetric> findCombinedMetricByProject(Project project) {
		return metricDao.findCombinedMetricByProject(project);
	}

	@Override
	public CombinedMetric findCombinedMetricById(Long id) {
		return metricDao.findCombinedMetricById(id);
	}

}
