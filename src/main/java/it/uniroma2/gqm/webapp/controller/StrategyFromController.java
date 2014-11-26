package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Strategy;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.GridManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.StrategyManager;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/strategyform*")
@SessionAttributes({"currentProject", "strategy", "currentUser", "goalParent", "strategyParent", "strategyParent", "strategyChildren"})
public class StrategyFromController  extends BaseFormController {
	
	@Autowired
	private StrategyManager strategyManager;
	@Autowired
    private GoalManager goalManager;
	private ProjectManager projectManager = null;
    private UserManager userManager = null;
    
    private GridManager gridManager;
    
    @Autowired
    public void setGridManager(@Qualifier("gridManager") GridManager gridManager) {
    	this.gridManager = gridManager;
    }
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    public StrategyFromController() {
        setCancelView("redirect:strategies");
        setSuccessView("redirect:strategies");
    }
    
    
    @RequestMapping(method = RequestMethod.GET)
    protected Strategy showForm(HttpServletRequest request, HttpSession session, Model model)
    throws Exception {
        String id = request.getParameter("id");
        Strategy ret = null; 
        Project currentProject = projectManager.getCurrentProject(session);;
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        if (!StringUtils.isBlank(id)) {
            ret = strategyManager.get(new Long(id));
        }else {
        	ret = new Strategy();
        	ret.setStrategyOwner(currentUser);
        	ret.setProject(currentProject);
        }      
        
        List<Goal> oGoalsAll = goalManager.getOrganizationalGoal(currentProject);
		List<Strategy> allStrategies = strategyManager.findByProject(currentProject); 
		
		List<Goal> goalParent = new ArrayList<Goal>(); //tutti i padri Goal ammissibili
		List<Strategy> strategyParent = new ArrayList<Strategy>();
		List<Goal> goalChildren = new ArrayList<Goal>(); //tutti i figli Goal ammissibili
		List<Strategy> strategyChildren = new ArrayList<Strategy>();

		getGoalParentAndChildren(oGoalsAll, ret, goalParent, goalChildren);
		getStrategyParentAndChildren(allStrategies, ret, strategyParent, strategyChildren);
		
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("goalParent", goalParent);
        model.addAttribute("strategyParent", strategyParent);
        model.addAttribute("goalChildren", goalChildren);
        model.addAttribute("strategyChildren", strategyChildren);
        return ret;
    }
    
    /**
     * Recupera le liste di Goal ammissibili come parenti o figli
     * @param oGoalsAll La lista di Goal in cui cercare
     * @param current La Strategy di cui recuperare possibili parenti e figli
     * @param goalParent La lista da popolare con i parenti Goal ammissibili
     * @param goalChildren La lista da popolare con i figli Goal ammissibili
     */
    private void getGoalParentAndChildren(List<Goal> oGoalsAll, Strategy current, List<Goal> goalParent, List<Goal> goalChildren) {
    	boolean isNew = (current.getId() == null);
    	
    	if(isNew) { //se nuovo, current non ha figli
    		for(Goal g : oGoalsAll) {
    			if(!g.hasChildren() || g.areChildrenStrategy()) //goal g senza figli o con figli strategy
    				goalParent.add(g);
    		}
    	} else {
    		for(Goal g : oGoalsAll) {
    			if((!g.hasChildren() || g.areChildrenStrategy()) && !gridManager.isGrandChild(g, current)) //goal g senza figli o con figli strategy, e g non è nipote di current
    				goalParent.add(g);
    			else if(current.areChildrenGoal() && current.getSorgChild().contains(g)) //il goal g è figlio della strategy current
    				goalChildren.add(g);
    		}
    	}
    }
    
    /**
     * Recupera le liste di Strategy ammissibili come parenti o figli
     * @param allStrategies La lista di Strategy in cui cercare
     * @param current La Strategy di cui recuperare possibili parenti e figli
     * @param strategyParent La lista da popolare con i parenti Strategy ammissibili
     * @param strategyChildren La lista da popolare con i figli Strategy ammissibili
     */
    private void getStrategyParentAndChildren(List<Strategy> allStrategies, Strategy current, List<Strategy> strategyParent, List<Strategy> strategyChildren) {
    	boolean isNew = (current.getId() == null);
    	
    	if(isNew) {
    		for(Strategy s: allStrategies) {
    			if(!s.hasChildren() || s.areChildrenStrategy()) //strategy s senza figli o con figli strategy
    				strategyParent.add(s);
    		}
    	} else {
    		for(Strategy s: allStrategies) {
    			if((!s.hasChildren() || s.areChildrenStrategy()) && !gridManager.isGrandChild(s, current)) //strategy s senza figli o con figli strategy, e s non è nipote di current
    				strategyParent.add(s);
    			else if(current.areChildrenStrategy() && current.getStrategyChild().contains(s)) //la strategy s è figlia della strategy current
    				strategyChildren.add(s);
    		}
    	}
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Strategy strategy, BindingResult errors, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
 
        if (validator != null) { // validator is null during testing
            validator.validate(strategy, errors);
 
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "strategyform";
            }
        }
 
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = (strategy.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();
                
        if (request.getParameter("delete") != null) {
        	
        	Strategy sDB = strategyManager.get(strategy.getId());
        	
        	if(sDB.hasChildren()) {
    			//TODO Attenzione!!! potrebbe non funzionare
        		errors.rejectValue("childType", "childType", "Can't delete Strategy with children"); 
        		return "strategyform";
        	} else {
        		
        		if (sDB.getParentType() == 0) {
					
        			Goal oParent = sDB.getSorgParent(); 
					oParent.getOrgChild().remove(sDB); 	
					goalManager.save(oParent); 	
					sDB.setSorgParent(null); 
				
        		}else {
					Strategy sParent = sDB.getStrategyParent();
					sParent.getSorgChild().remove(sDB);
					strategyManager.save(sParent);
					sDB.setStrategyParent(null);
				}
        		
        		strategyManager.save(sDB); 	
        	}
        	
        	strategyManager.remove(strategy.getId());
            saveMessage(request, getText("strategy.deleted", locale));
            
        } else {

            
          //#####################INIZIO PARENT CHILDREN###########################

        	strategy.getSorgChild().remove(null);
        	strategy.getStrategyChild().remove(null);
        	
    		if(isNew){ //creazione
    			
    			if (strategy.hasParent()) { //ha padre
    				if (strategy.getParentType() == 0) { //ha padre Goal
						
    					Goal oParent = goalManager.get(strategy.getSorgParent().getId());
    					if(oParent.getChildType() == 1 || oParent.getChildType() == -1) {
    						oParent.getOstrategyChild().add(strategy);
        					strategy.setSorgParent(oParent);
        					goalManager.save(oParent);
    					} else {
    						errors.rejectValue("parentType", "parentType", "Can't add Strategy child to parent already having Goal children");
    						return "strategyform";
    					}
    					
					} else { //ha padre Strategy
						
						Strategy sParent = strategyManager.get(strategy.getStrategyParent().getId());
						if(sParent.getChildType() == 1 || sParent.getChildType() == -1) {
    						sParent.getStrategyChild().add(strategy);
        					strategy.setStrategyParent(sParent);
        					strategyManager.save(sParent);
    					} else {
    						errors.rejectValue("parentType", "parentType", "Can't add Goal child to parent already having Strategy children");
    						return "strategyform";
    					}
					}
					
    				//strategyManager.save(strategy);
    				
				} else {
					
					errors.rejectValue("parentType", "parentType", "Strategy needs parent"); 
            		return "strategyform";
				}
    			
    			if(strategy.hasChildren()) { //ha figli
   	        		if (strategy.getChildType() == 0) { //ha figli Goal
    						
   	        			for (Goal g : strategy.getSorgChild()) {
   	        				g.setOstrategyParent(strategy);		//imposto il padre
   	        				strategy.getSorgChild().add(g);	//aggiungo il figlio a 'strategy'
   	        				goalManager.save(g);		//salvo il figlio
						}
    					
    	        	}else {	//ha figli Strategy
  
    	        		for (Strategy s : strategy.getStrategyChild()) {
    	        			s.setStrategyParent(strategy);
    	        			strategy.getStrategyChild().add(s);
    	        			strategyManager.save(s);
						}
    	        	}
   	        		
   	        		//strategyManager.save(strategy); 			//salvo la strategy
        			
            	}//else non devo fare niente
    			
        	} else { //aggiornamento
        		
        		
        		Strategy sDB = strategyManager.get(strategy.getId());
        		
        		boolean pSameType = (sDB.getParentType() == strategy.getParentType()) ? true : false;
        		int parentType = strategy.getParentType();
        		
        		//stesso tipo padre
        		if (pSameType) { 
        			
        			//stesso padre Goal
        			if (parentType == 0) { 
        			
        				boolean pOSame = (sDB.getSorgParent().getId() == strategy.getSorgParent().getId()) ? true : false;
        				
        				//è cambiato il padre Goal
        				if(!pOSame){
        					/**
        					 * tolgo il figlio al vecchio padre
        					 * ho cambiato il padre
        					 * aggiungo il figlio al nuovo padre
        					 */
        					sDB.getSorgParent().getOrgChild().remove(strategy);
        					goalManager.save(sDB.getSorgParent());
        					
        					sDB.setSorgParent(null);
        					strategyManager.save(sDB);
        					
        					strategy.getSorgParent().getOstrategyChild().add(strategy);
        				}
        				
        			//stesso padre Strategy
					} else {
						
						boolean pSSame = (sDB.getStrategyParent().getId() == strategy.getStrategyParent().getId()) ? true : false;
						
						//è cambiato il padre Strategy
						if(!pSSame) {
							sDB.getStrategyParent().getSorgChild().remove(strategy);
							strategyManager.save(sDB.getStrategyParent());
							
							sDB.setStrategyParent(null);
							strategyManager.save(sDB);
							
							strategy.getStrategyParent().getStrategyChild().add(strategy);
						}
					
					}// else non è cambiato niente, ma è rimasto null
        			
        			//strategyManager.save(strategy);
					
				} else { //non stesso tipo padre, che tipo è?
					
						
					//se il nuovo padre è un Goal e il vecchio padre era una Strategy
					if (strategy.getParentType() == 0) {
						
						sDB.getStrategyParent().getSorgChild().remove(strategy);
						strategyManager.save(sDB.getStrategyParent());
						
						sDB.setStrategyParent(null);
						strategyManager.save(sDB);
						
						strategy.getSorgParent().getOstrategyChild().add(strategy);
						
					//se il nuovo padre è una Strategy e il vecchio padre era un Goal
					} else {
						
						sDB.getSorgParent().getOstrategyChild().remove(strategy);
						goalManager.save(sDB.getSorgParent());
						
						sDB.setSorgParent(null);
						strategyManager.save(sDB);
						
						strategy.getStrategyParent().getStrategyChild().add(strategy);
						
					}
					
					//strategyManager.save(strategy);
				}
        		
        		boolean cSameType = (sDB.getChildType() == strategy.getChildType()) ? true : false;
        		
        		//se stesso tipo figlio
        		if (cSameType) {    
        			
        			//se stesso tipo Goal
        			if(strategy.getChildType() == 0){
            			
        				boolean cOSame = (sDB.getSorgChild() == strategy.getSorgChild()) ? true : false;
        				
        				//se non stesso figlio Goal
            			if (!cOSame) {
            				if(!strategy.getSorgChild().containsAll(sDB.getSorgChild())){
            					errors.rejectValue("childType", "childType", "Can't remove children from here!"); 
    		            		return "strategyform";
            				}
						} //else non fai niente
            			
            		//se stesso tipo Strategy
        			} else if(strategy.getChildType() == 1) {
        				
        				boolean cSSame = (sDB.getStrategyChild() == strategy.getStrategyChild()) ? true : false;
        				
        				//se non stesso figlio Stretegy
						if (!cSSame) {
							if(!strategy.getStrategyChild().containsAll(sDB.getStrategyChild())){
            					errors.rejectValue("childType", "childType", "Can't remove children from here!"); 
    		            		return "strategyform";
            				}
						} 
						
        			} //else non è cambiato niente, ma è rimasto null
        			
        			//strategyManager.save(strategy);
        			
				} else { //non stesso tipo figlio, male male!!!
					
					errors.rejectValue("childType", "childType", "Can't change children type!"); 
            		return "strategyform";
				} 
        	}
        	
        	//#####################FINE PARENT CHILDREN#######################
            
        	strategy = strategyManager.save(strategy);
            String key = (isNew) ? "strategy.added" : "strategy.updated";
            saveMessage(request, getText(key, locale));
    		
            if (!isNew) {
            	success = "redirect:strategyform?id=" + strategy.getId();
        	}
        } 
        return getSuccessView();
    }
    
    /*******************Binder*******************/
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(Strategy.class, "strategyParent", new strategyParentEditorSupport());
    	binder.registerCustomEditor(Goal.class, "sorgParent", new sorgParentEditorSupport());
    	
    	binder.registerCustomEditor(Set.class, "strategyChild", new strategyChildCollectionEditor(Set.class));
    	binder.registerCustomEditor(Set.class, "sorgChild", new sorgChildCollectionEditor(Set.class));
    }
    
    private class strategyParentEditorSupport extends PropertyEditorSupport {
    	public void setAsText(String text) throws IllegalArgumentException {
    		if(text != null && StringUtils.isNotBlank((String)text)) {
    			Long id = new Long(text);
    			
    			if(id != -1) {
    				Strategy strategy = strategyManager.get(id);
    				setValue(strategy);
    			} else {
    				setValue(null);
    			}
    		}
    	}
    }
    
    private class sorgParentEditorSupport extends PropertyEditorSupport {
    	public void setAsText(String text) throws IllegalArgumentException {
    		if(text != null && StringUtils.isNotBlank((String)text)) {
    			Long id = new Long(text);
    			
    			if(id != -1) {
    				Goal goal = goalManager.get(id);
    				setValue(goal);
    			} else {
    				setValue(null);
    			}
    		}
    	}
    }
    
    private class strategyChildCollectionEditor extends CustomCollectionEditor {
    	private strategyChildCollectionEditor(Class collectionType) {
    		super(collectionType);
    	}
    	
    	public void setValue(Object value) { //se la collection è nulla (perchè non ci sono elementi selezionati), la reimposto al valore di default
    		if(value == null)
    			super.setValue(new HashSet<Strategy>());
    		else
    			super.setValue(value);    		
    	}
    	
    	protected Object convertElement(Object element) {
    		if (element != null && StringUtils.isNotBlank((String)element)) {
    			Long id = new Long((String)element);
    			
    			if(id != -1) {
    				Strategy strategy = strategyManager.get(id);    				
    				return strategy;
    			}
    		}
    		return null;
    	}
    }
    
    private class sorgChildCollectionEditor extends CustomCollectionEditor {
    	private sorgChildCollectionEditor(Class collectionType) {
    		super(collectionType);
    	}
    	
    	public void setValue(Object value) { //se la collection è nulla (perchè non ci sono elementi selezionati), la reimposto al valore di default
    		if(value == null)
    			super.setValue(new HashSet<Goal>());
    		else
    			super.setValue(value);    		
    	}
    	
    	protected Object convertElement(Object element) {
    		if (element != null && StringUtils.isNotBlank((String)element)) {
    			Long id = new Long((String)element);
    			
    			if(id != -1) {
    				Goal goal = goalManager.get(id);
    				return goal;
    			}
    		}
    		return null;
    	}
    }
}
