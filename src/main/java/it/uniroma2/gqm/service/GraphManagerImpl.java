package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.BinaryTable;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;

import java.util.HashSet;
import java.util.Set;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.stereotype.Service;

@Service("graphManager")
public class GraphManagerImpl extends GenericManagerImpl<BinaryTable, Long> implements GraphManager{

	@Override
	public String getGraph(Goal g) {
				
		String tree = "{";
		tree += "\"name\":";
		tree += "\""+g.getDescription()+"\"";
		tree += ",\"identifier\":";
		tree += "\""+g.getId()+"\"";
		tree += ",\"type\":";
		tree += "\""+0+"\"";
		tree += ",\"parent\":";
		tree += "\"null\"";
		
		//Has Questions
		if(g.getQuestions().size() > 0) { 
			
			tree += ", \"children\":[";
			Set<Question> questions = new HashSet<Question>();
			
			
			for (GoalQuestion gq : g.getQuestions()) {
				questions.add(gq.getQuestion());
			}
			int i = 0;
			for (Question q : questions) {
				if(i != 0)
					tree += ",";
				tree += "{\"parent\":\""+g.getDescription()+"\",\"name\":\""+q.getName()+"\"";
				tree += ",\"identifier\":\""+q.getId()+"\",\"type\":\""+1+"\"";
				i++;
				//Has metrics
				if(q.getMetrics().size() > 0) {
					
					tree += ", \"children\":[";
					Set<SimpleMetric> metrics = new HashSet<SimpleMetric>();
					for(QuestionMetric qm : q.getMetrics()) {
						metrics.add(qm.getMetric());
					}
					int j = 0;
					for (SimpleMetric m : metrics) {
						if(j != 0)
							tree +=",";
						tree += "{\"parent\":\""+q.getName()+"\",\"name\":\""+m.getName()+"\"";
						tree += ",\"identifier\":\""+m.getId()+"\",\"type\":\""+2+"\"}";
						j++;
					}
					tree += "]}";
				} else {
					tree += "}";
				} 
			}
			tree += "]}";
		} else { //non ha figli
			tree += "}";
		}

		System.out.println(tree);
		return tree;
	}

}
