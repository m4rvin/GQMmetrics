package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.BinaryElement;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.service.BinaryTableManager;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.GridManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.SatisfyingConditionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/binarytable*")
@SessionAttributes({"currentProject","currentUser"})
public class BinaryTableController {	
	
	@Autowired
    private GoalManager goalManager;
	private ComplexMetricManager metricManager;
	private UserManager userManager;
	private GridManager gridManager;
	private ProjectManager projectManager = null;
	@Autowired
	private SatisfyingConditionManager satisfyingConditionManager;
	
	@Autowired
	private BinaryTableManager binaryManager;
	
	@Autowired
    public void setMetricManager(@Qualifier("complexMetricManager") ComplexMetricManager metricManager) {
        this.metricManager = metricManager;
    }

	@Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
	
	@Autowired
	public void setGridManager(@Qualifier("gridManager") GridManager gridManager) {
		this.gridManager = gridManager;
	}
	
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
    
    //==== GQM+S+MS changes
    /**
     * Evaluate and set the status for an OrganizationalGoal. 
     * Retrieve each MeasurementGoal associated to OG and evaluate it using only metrics that have SatisfyingCondition assigned and have been measured.
     * @param goalToEvaluate The OrganizationalGoal to evaluate
     * @param mgs The MeasurementGoal associated to the OrganizationalGoal to evaluate
     */
    public void setGoalStatus(BinaryElement goalToEvaluate, Set<Goal> mgs){
    	
    	boolean satisfyAll = true;
    	
    	//Evaluate each measurement goal associated to the passed OG (goalToEvaluate)
        for (Goal mg : mgs) {
        	List<AbstractMetric> metrics = goalManager.getMeasuredMetricByGoal(mg);
        	
        	if (metrics.size() > 0) {
        		boolean satisfy = true;
        		boolean evaluable = false;
        		//retrieve the metrics associated to the MG through a question 
        		for(AbstractMetric m: metrics){
               	  List<SatisfyingCondition> satisfyingConditions = this.satisfyingConditionManager.findByProjectGoalMetric(mg.getProject(), mg, m);
               	  //evaluate only the metrics with an assigned Satisfying Condition
               	  for(SatisfyingCondition sc: satisfyingConditions)
               	  {
               			System.out.println(sc);
               		try{
           				satisfy &= sc.getSatisfaction(m.getActualValue());
   	                	satisfyAll &= satisfy;
   	                	evaluable = true;
               		}
               		catch (IllegalArgumentException exc){
               			//a metric with an associated SatisfyingCondition returned an error. The Goal cannot be correctly evaluated. 
               			goalToEvaluate.setValue(-1); //error value
               			String newMessage = "The metric with name: " + m.getName() + " has returned the following error: \n" + exc.getMessage() + "\n The associated MeasurementGoal is not evaluable.";
               			throw new IllegalArgumentException(newMessage);
               		}
               	  }
                }
        		if(!evaluable)//no metric with a satisfying condition associated found. The Goal is not evaluable.
               		satisfyAll = false;

                if(satisfyAll)
                	goalToEvaluate.setValue(1);
                else
                	goalToEvaluate.setValue(0);
			} else {
				goalToEvaluate.setValue(0);
			}
		}
    }
    
    //==== GQM+S+MS changes

	
	@ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected Goal showTable(HttpServletRequest request,HttpSession session, Model model) throws Exception {
		
        String id = request.getParameter("id");
        Goal ret = null;
        Set<Goal> mgs = new HashSet<Goal>();
        
        Project currentProject = projectManager.getCurrentProject(session);
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());        
        
        if(StringUtils.isBlank(id))
        	return null;
        
        //Recupera goal selezionato
        ret = goalManager.get(new Long(id));
        boolean hasPermission = checkPermission(currentUser, ret);
        
        if (hasPermission) {
        	//Recupera MG associati
        	mgs = ret.getAssociatedMGs();
        	
            BinaryElement mainGoal = new BinaryElement(ret, 0);
            String fatherErrorMessage = null;
            try{
            	setGoalStatus(mainGoal, mgs);
            }
            catch(IllegalArgumentException exc){
            	fatherErrorMessage = exc.getMessage();
            }
            
            Set<BinaryElement> childGoal = new HashSet<BinaryElement>();

            //Recupera la lista degli OG figli di questo OG
            Set<Goal> set = binaryManager.findOGChildren(goalManager.get(Long.parseLong(id)));
            String childErrorMessage = null;

            for (Goal g : set) {
            	mgs.clear();
            	//Recupera associazioni con MG
            	mgs = g.getAssociatedMGs();
            	
                BinaryElement gGoal = new BinaryElement(g, 0);

                try{
                    setGoalStatus(gGoal, mgs);
                }
                catch(IllegalArgumentException exc){
                	childErrorMessage = exc.getMessage();
                    break;
                }
            	childGoal.add(gGoal);
			}
            
            //Stampa di debug
            System.out.println("Figli OG di: "+ret.getDescription());
    		for (Goal s : set) {
    			System.out.println(s.getDescription());
    		}
    		
    		Set<String> suggestions = getSuggestion(mainGoal, childGoal, fatherErrorMessage, childErrorMessage);
           
    		model.addAttribute("suggestions", suggestions);
    		model.addAttribute("mainGoal", mainGoal);
    		model.addAttribute("childGoal", childGoal);
    		model.addAttribute("currentUser",currentUser);
    		model.addAttribute("currentProject",currentProject);
    		
        }
        //else return null?
        
        return ret;
    }
	
	/**
	 * Verifica i permessi dell'utente che vuole visualizzare la Binary Table.
	 * Un Project Manager può vedere i risultati di tutti i goal dei progetti di cui è Manager
	 * Un Goal owner può vedere i risultati di tutti i goal di cui è proprietario e relativi figli
	 * Un Goal enactor può vedere i risultati di tuti i goal di cui è enactor e relativi figli
	 * @param curUser Utente che vuole visualizzare i risultati
	 * @param curGoal Goal di cui si vogliono visualizzare i risultati
	 * @return
	 */
	private boolean checkPermission(User curUser, Goal curGoal) {
		Project curProject = curGoal.getProject();
		
		if(curProject.getProjectManagers().contains(curUser)) { //sono project manager
			return true;
		} else {
			if(curGoal.getGoalOwner().equals(curUser) || curGoal.getGoalEnactor().equals(curUser)) //sono owner o enactor del goal
				return true;
			
			List<Goal> allGoals = goalManager.findByProject(curProject);
			List<Goal> userGoals = new ArrayList<Goal>(); //tutti i goal di cui sono owner o enactor
			
			for(Goal g : allGoals) {
				if(g.getGoalOwner().equals(curUser) || g.getGoalEnactor().equals(curUser))
					userGoals.add(g);
			}
			
			for(Goal g : userGoals) {
				if(gridManager.isGrandChild(curGoal, g)) //se curGoal è figlio di un goal (o è un goal) di cui sono responsabile (come owner o enactor)
					return true;
			}
		}
		return false;
	}
	
	public Set<String> getSuggestion(BinaryElement b, Set<BinaryElement> setB, String fatherErrorMessage, String ChildErrorMessage){
		
		String s1 = "Enforce strategies";
		String s2 = "Strategies not sufficient or not effective";
		String s2b = "Assumptions wrong";
		String s3 = "Check magnitudes (less was sufficient)";
		String s3b = "Check root causes for achieving "; //TODO aggiungere sempre ID
		String s4 = "Good work!!!";
		String s5 = "Refinements wrong";
		String s6 = "Strategies may help";
		
		boolean valueSetB = true;
		Set<String> suggestions = new HashSet<String>();
		
	    //==== GQM+S+MS changes

		if(fatherErrorMessage != null){
			suggestions.add(fatherErrorMessage);
			return suggestions;
		}
		else if(ChildErrorMessage != null){
			suggestions.add(ChildErrorMessage);
			return suggestions;
		}
	    //==== END GQM+S+MS changes

		
		for (BinaryElement elto : setB){
			if(elto.getValue() == 0)
				valueSetB &= false;
			else
				valueSetB &= true;
		}
			
		
		//If parent not achieved
		if (b.getValue() == 0) {
			//If all children were achieve
			if(valueSetB){
				//If parent's first children was Strategy
				if(b.getGoal().getChildType() == 1) {
					suggestions.add(s2);
					suggestions.add(s2b);
				//If parent's first children was Goal
				} else {
					suggestions.add(s5);
				}
			//If at least was not achieve
			} else {
				//If parent's first children was Strategy
				if(b.getGoal().getChildType() == 1) {
					suggestions.add(s1);
				//If parent's first children was Goal
				} else {
					suggestions.add(s6);
				}
			}
		//If parent achieved
		} else {
			//If all children were achieve
			if(valueSetB){
				//If parent's first children was Strategy
				if(b.getGoal().getChildType() == 1) {
					suggestions.add(s4);
				//If parent's first children was Goal
				} else {
					suggestions.add(s4);
				}
			//If at least was not achieve	
			} else {
				//If parent's first children was Strategy
				if(b.getGoal().getChildType() == 1) {
					suggestions.add(s3);
					suggestions.add(s3b+"OG"+b.getGoal().getId());
				//If parent's first children was Goal
				} else {
					suggestions.add(s5);
				}
			}
		}
		
		
		
		return suggestions;
	}
	
}
