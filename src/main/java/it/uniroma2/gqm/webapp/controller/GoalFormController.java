package it.uniroma2.gqm.webapp.controller;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import it.uniroma2.gqm.model.*;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.GridManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.StrategyManager;
import it.uniroma2.gqm.webapp.util.RequestUtil;

@Controller
@RequestMapping("/goalform*")
@SessionAttributes({"currentProject", "goal", "currentUser", "visibleGESection", "modificableHeader", "availableStatus", "associableMGoals", "associableOGoals",
					"availableUsers", "goalParent", "strategyParent", "goalChildren", "strategyChildren"})
//@SessionAttributes({"availableUsers","goal","currentUser","strategies","availableGoals"})
public class GoalFormController extends BaseFormController {
    	
    @Autowired
    private GoalManager goalManager;
    
    private UserManager userManager = null;

    private ProjectManager projectManager = null;
    
    @Autowired
    private StrategyManager strategyManager;
    
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
    public void setGoalManager(@Qualifier("goalManager") GoalManager goalManager) {
        this.goalManager = goalManager;
    }


    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public GoalFormController() {
        setCancelView("redirect:goals");
        setSuccessView("redirect:goals");
    }
 
    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected Goal showForm(HttpServletRequest request,HttpSession session, Model model)
    throws Exception {
        String id = request.getParameter("id");
        Goal ret = null;
        
        Project currentProject = projectManager.getCurrentProject(session);
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        if (!StringUtils.isBlank(id)) {
        	ret = goalManager.get(new Long(id));
        }else {
        	ret = new Goal();
        	ret.setStatus(GoalStatus.DRAFT);
        	ret.setGoalOwner(currentUser);
        	ret.setProject(projectManager.get(currentProject.getId()));
        }              
        List<String> availableStatus = goalManager.getAvailableStatus(ret,currentUser);
        
		// header data is modificable only if te current user is the GO and the
		// status is PROPOSED or we are making a new Goal...
		boolean modificableHeader = ( (ret.getStatus() == GoalStatus.DRAFT || ret.getStatus() == GoalStatus.FOR_REVIEW) &&  
									 currentUser.equals(ret.getGoalOwner()));
		
		//La sezione dedicata al Goal Enactor è visibile solo se il goal è in uno stato diverso da DRAFT e PROPOSED e se è di tipo MG
		boolean visibleGESection = !(ret.getStatus() == GoalStatus.DRAFT || 
					ret.getStatus() == GoalStatus.PROPOSED) && ret.isMG();
		
		List<Goal> allGoals = goalManager.findByProject(currentProject); //tutti i goal nel progetto
		List<Strategy> allStrategies = strategyManager.findByProject(currentProject); //tutte le strategie nel progetto

		List<Goal> oGoalsAll = new ArrayList<Goal>(); //tutti gli og nel progetto
		List<Goal> mGoalsAll = new ArrayList<Goal>(); //tutti gli mg nel progetto
		
		for(Goal g: allGoals) {
			if(g.isMG())
				mGoalsAll.add(g);
			else if(g.isOG())
				oGoalsAll.add(g);
		}
		
		List<Goal> goalParent = new ArrayList<Goal>(); //tutti i padri Goal ammissibili (ossia che sono già padri del Goal, o che possono essere selezionati come tali)
		List<Strategy> strategyParent = new ArrayList<Strategy>();
		List<Goal> goalChildren = new ArrayList<Goal>(); //tutti i figli Goal ammissibili
		List<Strategy> strategyChildren = new ArrayList<Strategy>();
		
		getGoalParentAndChildren(oGoalsAll, ret, goalParent, goalChildren);
		getStrategyParentAndChildren(allStrategies, ret, strategyParent, strategyChildren);

		List<Goal> associableMGoals = new ArrayList<Goal>(); //tutti gli mg associabili ad og (ossia senza relazione o già in relazione con l'og) 
		List<Goal> associableOGoals = new ArrayList<Goal>(); //tutti gli og associabili ad mg
		
		associableOGoals.addAll(oGoalsAll); //popolo og associabili ad mg, ossia tutti
			
		for(Goal g: mGoalsAll) { //popolo mg associabili a og, ossia quelli senza relazione o in relazione con l'og corrente
			Goal gAssociatedOG = g.getAssociatedOG(); //og associato al goal g (che è un mg)
			if(gAssociatedOG == null || gAssociatedOG == ret)
				associableMGoals.add(g);
		}
				
		model.addAttribute("currentUser", currentUser);
		model.addAttribute("visibleGESection", visibleGESection);
		model.addAttribute("modificableHeader", modificableHeader);
        model.addAttribute("availableStatus", availableStatus);
        model.addAttribute("associableMGoals", associableMGoals);
        model.addAttribute("associableOGoals", associableOGoals);
        model.addAttribute("availableUsers", ret.getProject().getGQMTeam());
        model.addAttribute("goalParent", goalParent);
        model.addAttribute("strategyParent", strategyParent);
        model.addAttribute("goalChildren", goalChildren);
        model.addAttribute("strategyChildren", strategyChildren);
        
        return ret;
    }
    
    /**
     * Recupera le liste di Goal ammissibili come parenti o figli
     * @param oGoalsAll La lista di Goal in cui cercare
     * @param current Il Goal di cui recuperare possibili parenti e figli
     * @param goalParent La lista da popolare con i parenti Goal ammissibili
     * @param goalChildren La lista da popolare con i figli Goal ammissibili
     */
    private void getGoalParentAndChildren(List<Goal> oGoalsAll, Goal current, List<Goal> goalParent, List<Goal> goalChildren) {
    	boolean isNew = (current.getId() == null);
    	
    	if(isNew) { //se nuovo, current non ha figli
			for(Goal g : oGoalsAll) {
				if(!g.hasChildren() || g.areChildrenGoal()) //goal g senza figli o con figli goal
					goalParent.add(g);
			}
    	} else {
			for(Goal g : oGoalsAll) {
				if((!g.hasChildren() || g.areChildrenGoal()) && !gridManager.isGrandChild(g, current)) //goal g senza figli o con figli goal, e g non è nipote di current
					goalParent.add(g);
				else if(current.areChildrenGoal() && current.getOrgChild().contains(g)) //il goal g è figlio del goal current
					goalChildren.add(g);
			}
    	}
    }
    
    /**
     * Recupera le liste di Strategy ammissibili come parenti o figli
     * @param allStrategies La lista di Strategy in cui cercare
     * @param current Il Goal di cui recuperare possibili parenti e figli
     * @param strategyParent La lista da popolare con i parenti Strategy ammissibili
     * @param strategyChildren La lista da popolare con i figli Strategy ammissibili
     */
    private void getStrategyParentAndChildren(List<Strategy> allStrategies, Goal current, List<Strategy> strategyParent, List<Strategy> strategyChildren) {
    	boolean isNew = (current.getId() == null);
    	
    	if(isNew) { //se nuovo, current non ha figli
			for(Strategy s: allStrategies) {
				if(!s.hasChildren() || s.areChildrenGoal()) //strategy s senza figli o con figli goal
					strategyParent.add(s);
			}
    	} else {
			for(Strategy s: allStrategies) {
				if((!s.hasChildren() || s.areChildrenGoal()) && !gridManager.isGrandChild(s, current)) //strategy s senza figli o con figli goal, e s non è nipote di current
					strategyParent.add(s);
				else if(current.areChildrenStrategy() && current.getOstrategyChild().contains(s)) //la strategy s è figlia del goal current
					strategyChildren.add(s);
			}
    	}
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@Valid Goal goal, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
    	
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
 
        if (validator != null) { // validator is null during testing
            validator.validate(goal, errors);
            
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
            	System.out.println("errors: " + errors);
                return "goalform";
            }
        }
 
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = (goal.getId() == null);
        	Locale locale = request.getLocale();
 
        if (request.getParameter("delete") != null) {
        	Goal gDB = goalManager.get(goal.getId());
        	
        	if(gDB.isOG()){
        		
        		if(gDB.hasChildren()) {
        			//TODO Attenzione!!! potrebbe non funzionare
            		errors.rejectValue("childType", "childType", "Can't delete OG with children"); 
            		return "goalform";
            	} else {
            		
            		//TODO si potrebbe mettere dentro una funzione
            		//Prendo il padre, accedo al campo figli tipo goal, cerco il figlio e lo elimino
    	        	if(gDB.getParentType() != -1) {
    	        		if (gDB.getParentType() == 0) {
    						
    	        			Goal oParent = gDB.getOrgParent(); 	//recupero il padre
    						oParent.getOrgChild().remove(gDB); 	//aggiorno il padre
    						goalManager.save(oParent); 			//salvo il padre
    						gDB.setOrgParent(null); 			//aggiorno il goal
    					
    	        		}else {
    						Strategy sParent = gDB.getOstrategyParent();
    						sParent.getSorgChild().remove(gDB);
    						strategyManager.save(sParent);
    						gDB.setOstrategyParent(null);
    					}
    	        		goalManager.save(gDB); 					//salvo il goal
    	        	}
            	}
        	}
   	
        	removeMGOGRelationship(gDB);
        	
        	goalManager.remove(gDB);
        	
            saveMessage(request, getText("goal.deleted", locale));
            
        } else { //Caso di aggiornamento o creazione di un Goal
        	
        	//###########################################################
        	
        	
        	if(goal.isOG()){
        		
        		goal.getOrgChild().remove(null);
            	goal.getOstrategyChild().remove(null);
            	//goal.setOrgParent(null);
            	//goal.setOstrategyParent(null);
            	
        		if(isNew){ //creazione
        			
        			if (goal.hasParent()) { //ha padre
        				if (goal.getParentType() == 0) { //ha padre Goal
							
        					Goal oParent = goalManager.get(goal.getOrgParent().getId());
        					if(oParent.getChildType() == 0 || oParent.getChildType() == -1) {
        						oParent.getOrgChild().add(goal);
            					goal.setOrgParent(oParent);
            					goalManager.save(oParent);
        					} else {
        						errors.rejectValue("parentType", "parentType", "Can't add Goal child to parent already having Strategy children");
        						return "goalform";
        					}
        					
						} else { //ha padre Strategy
							
							Strategy sParent = strategyManager.get(goal.getOstrategyParent().getId());
							if(sParent.getChildType() == 0 || sParent.getChildType() == -1) {
								sParent.getSorgChild().add(goal);
								goal.setOstrategyParent(sParent);
								strategyManager.save(sParent);
	    					} else {
	    						errors.rejectValue("parentType", "parentType", "Can't add Strategy child to parent already having Goal children");
	    						return "goalform";
	    					}
						}
        				
					} else {
						
						//else controllare che sia il primo ROOT, altrimenti errore
						if(goalManager.rootExists(goal.getProject())){
							errors.rejectValue("parentType", "parentType", "OG Root exists!!!"); 
		            		return "goalform";
						}
					}
        			
        			if(goal.hasChildren()) { //ha figli
       	        		if (goal.getChildType() == 0) { //ha figli Goal
        						
       	        			for (Goal g : goal.getOrgChild()) {
       	        				g.setOrgParent(goal);		//imposto il padre
       	        				goal.getOrgChild().add(g);	//aggiungo il figlio a 'goal'
       	        				goalManager.save(g);		//salvo il figlio
    						}
        					
        	        	}else {	//ha figli Strategy
      
        	        		for (Strategy s : goal.getOstrategyChild()) {
        	        			s.setSorgParent(goal);
        	        			goal.getOstrategyChild().add(s);
        	        			strategyManager.save(s);
    						}
        	        	}
            			
                	}//else non devo fare niente
        			
        			//goal = goalManager.save(goal);
        			
            	} else { //aggiornamento
            		
            		
            		Goal gDB = goalManager.get(goal.getId());
            		
            		boolean pSameType = (gDB.getParentType() == goal.getParentType()) ? true : false;
            		int parentType = goal.getParentType();
            		
            		//stesso tipo padre
            		if (pSameType) { 

            			//stesso padre Goal
            			if (parentType == 0) { 
            				
            				boolean pOSame = (gDB.getOrgParent().getId() == goal.getOrgParent().getId()) ? true : false;
                    		//è cambiato il padre Goal
            				if(!pOSame){
            					/**
            					 * tolgo il figlio al vecchio padre
            					 * ho cambiato il padre
            					 * aggiungo il figlio al nuovo padre
            					 */
            					gDB.getOrgParent().getOrgChild().remove(goal);
            					goalManager.save(gDB.getOrgParent());
            					
            					gDB.setOrgParent(null);
            					goalManager.save(gDB);
            					
            					goal.getOrgParent().getOrgChild().add(goal);
            				}
            				
            			//stesso padre Strategy
						} else if(parentType == 1){
							
							boolean pSSame = (gDB.getOstrategyParent().getId() == goal.getOstrategyParent().getId()) ? true : false;
							//è cambiato il padre Strategy
							if(!pSSame) {
								gDB.getOstrategyParent().getSorgChild().remove(goal);
								strategyManager.save(gDB.getOstrategyParent());
								
								gDB.setOstrategyParent(null);
								goalManager.save(gDB);
								
								goal.getOstrategyParent().getSorgChild().add(goal);
							}
						
						}// else non è cambiato niente, ma è rimasto null
            			
            			//goal = goalManager.save(goal);
						
					} else { //non stesso tipo padre, che tipo è?
						
						//se il nuovo padre è null, quindi non ho più il padre
						if (parentType == -1) { //il nuovo parent è null
							
							//se il vecchio padre era un Goal
							if (gDB.getParentType() == 0 ) { 
								
								/*
								 * mi tolgo dalla lista dei figli del mio vecchio padre
								 */
								gDB.getOrgParent().getOrgChild().remove(goal);
								goalManager.save(gDB.getOrgParent());
								
								gDB.setOrgParent(null);
            					goalManager.save(gDB);
					
            				//Se il vecchio padre era una Strategy
							} else if(gDB.getParentType() == 1){
								
								gDB.getOstrategyParent().getSorgChild().remove(goal);
								strategyManager.save(gDB.getOstrategyParent());
								
								gDB.setOstrategyParent(null);
								goalManager.save(gDB);
							}

						} else { //il nuovo parent è Goal/Strategy ed il vecchio è Strategy/Goal
							
							//se il nuovo padre è un Goal e il vecchio padre era una Strategy
							if (goal.getParentType() == 0) {
								
								gDB.getOstrategyParent().getSorgChild().remove(goal);
								strategyManager.save(gDB.getOstrategyParent());
								
								gDB.setOstrategyParent(null);
								goalManager.save(gDB);
								
								goal.getOrgParent().getOrgChild().add(goal);
								
							//se il nuovo padre è una Strategy e il vecchio padre era un Goal
							} else {
								
								gDB.getOrgParent().getOrgChild().remove(goal);
								goalManager.save(gDB.getOrgParent());
								
								gDB.setOrgParent(null);
								goalManager.save(gDB);
								
								goal.getOstrategyParent().getSorgChild().add(goal);
								
							}
						}
						
						//goal = goalManager.save(goal);
					}
            		
            		boolean cSameType = (gDB.getChildType() == goal.getChildType()) ? true : false;
            		
            		//se stesso tipo figlio
            		if (cSameType) { 
            			
            			//se stesso tipo Goal
            			if(goal.getChildType() == 0){
	            			
            				boolean cOSame = (gDB.getOrgChild() == goal.getOrgChild()) ? true : false;
            				//se non stesso figlio Goal
	            			if (!cOSame) {
	            				if(!goal.getOrgChild().containsAll(gDB.getOrgChild())){
	            					errors.rejectValue("childType", "childType", "Can't remove children from here!"); 
	    		            		return "goalform";
	            				}
							} 
	            			
	            		//se stesso tipo Strategy
            			} else if(goal.getChildType() == 1) {
            				
            				boolean cSSame = (gDB.getOstrategyChild() == goal.getOstrategyChild()) ? true : false;
            				//se non stesso figlio Stretegy
							if (!cSSame) {
								if(!goal.getOstrategyChild().containsAll(gDB.getOstrategyChild())){
	            					errors.rejectValue("childType", "childType", "Can't remove children from here!"); 
	    		            		return "goalform";
	            				}
							} 
							
            			} //else non è cambiato niente, ma è rimasto null
            			
            			//goal = goalManager.save(goal);
            			
					} else { //non stesso tipo figlio, male male!!!
						
						errors.rejectValue("childType", "childType", "Can't change children type!"); 
	            		return "goalform";
						/*
						//se il nuovo figlio è null, quindi non ho più figli
						if (goal.getChildType() == -1) { //il nuovo figlio è null
							
							//se il vecchio figlio era un Goal
							if (gDB.getChildType() == 0 ) { 
								
								//elimino tutti i figli Goal
								gDB.getOrgChild().clear();
								goalManager.save(gDB);
								
							//se il vecchio figlio era una Strategy
							} else if(gDB.getChildType() == 1) { 
								
								//elimino tutti i figli Strategy
								gDB.getOstrategyChild().clear();
								goalManager.save(gDB);
							}
							
						} else { //il nuovo figlio è Goal/Strategy ed il vecchio era Strategy/Goal
							
							//se il nuovo figlio è un Goal e il vecchio figlio era una Strategy
							if (goal.getChildType() == 0) {
								
								gDB.getOrgChild().clear();
								goalManager.save(gDB);
							
							//se il nuovo figlio è una Strategy e il vecchio figlio era un Goal
							} else {
							
								gDB.getOstrategyChild().clear();
								goalManager.save(gDB);
							}
						}
						
						goal = goalManager.save(goal);
						*/
					} 
            	}
        		
        	}
        	
        	//###########################################################
        	
        	goal.setGoalOwner(userManager.get(goal.getGoalOwner().getId()));
        	
        	goal.setGoalEnactor(userManager.get(goal.getGoalEnactor().getId()));
        	
        	if("true".equalsIgnoreCase(request.getParameter("vote"))) {
        		goal.getVotes().add(userManager.getUserByUsername(request.getRemoteUser()));
        	}
            
        	goal = handleMGOGRelationship(goal);
        	
        	goal = goalManager.save(goal);
        	
            String key = (isNew) ? "goal.added" : "goal.updated";
            saveMessage(request, getText(key, locale));
        	
            if(goal.getId() == null){
		        try {
		        	User ge =  userManager.getUserByUsername(goal.getGoalEnactor().getFullName());
		        	message.setSubject(getText("goal.email.subject", locale));
		            sendUserMessage(ge, getText("goal.email.message", locale), RequestUtil.getAppURL(request));		            
		        } catch (MailException me) {
		            saveError(request, me.getMostSpecificCause().getMessage());
		        }
            }else {
            	Project cp = projectManager.get(goal.getProject().getId());
            	List<User> users = new ArrayList<User>();
            	for(User u:cp.getGQMTeam())
            		users.add(u);
            	for(User u:cp.getProjectTeam())
            		users.add(u);            	
            	for(User u:users){
    		        try {
    		        	User user =  userManager.getUserByUsername(u.getFullName());
    		        	message.setSubject(getText("goal.changed.email.subject", locale));
    		            sendUserMessage(user, getText("goal.changed.email.message", locale), RequestUtil.getAppURL(request));		            
    		        } catch (MailException me) {
    		            saveError(request, me.getMostSpecificCause().getMessage());
    		        }            		
            	}
            }
            
            /*if (!isNew) {
                success = "redirect:goalform?id=" + goal.getId();
            }*/
        }
        
        return getSuccessView();
    }
    
    /**
     * Elimina i riferimenti a curGoal nei goal associati e viceversa
     * @param curGoal Il Goal da modificare
     */
	public void removeMGOGRelationship(Goal curGoal) {
		if(curGoal.isMG()) {
			Goal og = curGoal.getAssociatedOG();
			if(og != null) {
				og.getAssociatedMGs().remove(curGoal);
				goalManager.save(og);
				curGoal.setAssociatedOG(null);
			}
		} else if(curGoal.isOG()) {
			Set<Goal> mgs = curGoal.getAssociatedMGs();
			for(Goal mg : mgs) {
				mg.setAssociatedOG(null);
				goalManager.save(mg);
			}
			curGoal.getAssociatedMGs().clear();
		}
	}    
    
    /**
     * Gestisce le relazioni di tipo MGOG di un Goal
     * @param curGoal Il Goal da aggiornare
     */
    private Goal handleMGOGRelationship(Goal curGoal) {
    	boolean isNew = curGoal.getId() == null;
    	
    	curGoal.getAssociatedMGs().remove(null);
    	
    	if(isNew)
    		curGoal = handleMGOGRelationshipOfNewGoal(curGoal);
    	else
    		curGoal = handleMGOGRelationshipOnExistingGoal(curGoal);
    	
    	return curGoal;
    }

    /**
     * Gestisce le relazioni di tipo MGOG di un Goal non ancora salvato su db
     * @param curGoal Il Goal da aggiornare
     */
    private Goal handleMGOGRelationshipOfNewGoal(Goal curGoal) {
		if(curGoal.isOG()) { //per rendere la relazione persistente su db, devo salvare il proprietario della stessa su db
			curGoal = goalManager.save(curGoal); //se non salvo subito curGoal, vado in eccezione perchè quando salvo i vari mg, essendo proprietari della relazione, non trovano su db curGoal
			Set<Goal> curMGs = curGoal.getAssociatedMGs();
			
			for(Goal mg : curMGs) {				
				mg.setAssociatedOG(curGoal);
				goalManager.save(mg);
			}
		}
		else if(curGoal.isMG()) {
			//stò creando un mg, quindi posso anche non salvare il relativo og su db, che tanto è l'mg che essendo proprietario della relazione, ha la foreign key sull'og
			Goal og = curGoal.getAssociatedOG();
			
			if(og != null) { 
				Set<Goal> ogMGs = og.getAssociatedMGs();
				if(!ogMGs.contains(curGoal))
					ogMGs.add(curGoal);
			}
		}
		
		return curGoal;
    }
    
    /**
     * Gestisce le relazioni di tipo MGOG di un Goal già esistente
     * @param curGoal Il Goal da aggiornare
     */
    private Goal handleMGOGRelationshipOnExistingGoal(Goal curGoal) {
		if(curGoal.isOG()) { //per rendere la relazione persistente su db, devo salvare il proprietario della stessa su db
			Goal oldCur = goalManager.get(curGoal.getId());
			Set<Goal> oldMGs = oldCur.getAssociatedMGs(); //associazioni salvate su db    			
			Set<Goal> curMGs = curGoal.getAssociatedMGs(); //nuove associazioni
			
			for(Goal oldMG : oldMGs) {
				if(!curMGs.contains(oldMG)) { //se la vecchia associazione non è presente anche fra le nuove, la posso eliminare
					oldMG.setAssociatedOG(null); 
					goalManager.save(oldMG);
				}			
			}
			
			for(Goal curMG : curMGs) {
				if(!oldMGs.contains(curMG)) { //se la nuova associazione non è presente fra le vecchie, la devo aggiungere
					curMG.setAssociatedOG(curGoal);
					goalManager.save(curMG);
				}	
			}
		}
		else if(curGoal.isMG()) {
			Goal oldCur = goalManager.get(curGoal.getId());
			Goal oldOG = oldCur.getAssociatedOG();
			Goal curOG = curGoal.getAssociatedOG();
			
			if(oldOG != curOG && oldOG != null) //se ho modificato l'associazione, devo eliminare quella vecchia
				oldOG.getAssociatedMGs().remove(oldOG);
		}
		
		return curGoal;
    }

    /****************************************InitBinders****************************************/
        
    @InitBinder(value="goal")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new GoalValidator());
    }
    
    @InitBinder
    protected void initBinder1(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Set.class, "QSMembers", new CustomCollectionEditor(Set.class) {
        	
        	public void setValue(Object value) { //se la collection è nulla (perchè non ci sono elementi selezionati), la reimposto al valore di default
        		if(value == null)
        			super.setValue(new HashSet<User>());
        		else
        			super.setValue(value);    		
        	}
        	
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    User u = userManager.get(id);
                    return u;
                }
                return null;
            }
        });
    }
    
    @InitBinder
    protected void initBinder2(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Set.class, "MMDMMembers", new CustomCollectionEditor(Set.class) {
        	
        	public void setValue(Object value) { //se la collection è nulla (perchè non ci sono elementi selezionati), la reimposto al valore di default
        		if(value == null)
        			super.setValue(new HashSet<User>());
        		else
        			super.setValue(value);    		
        	}
        	
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    User u = userManager.get(id);
                    return u;
                }
                return null;
            }
        });
    }
    
    @InitBinder
    protected void initBinder3(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(Set.class, "orgChild", new CustomCollectionEditor(Set.class) {
    		
        	public void setValue(Object value) { //se la collection è nulla (perchè non ci sono elementi selezionati), la reimposto al valore di default
        		if(value == null)
        			super.setValue(new HashSet<Goal>());
        		else
        			super.setValue(value);    		
        	}
    		
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    if(id != -1){
                    	Goal g = goalManager.get(id);
                    	return g;
                    }
                }
                return null;
            }
        });
    }
    
    @InitBinder
    protected void initBinder4(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(Set.class, "ostrategyChild", new CustomCollectionEditor(Set.class) {
    		
        	public void setValue(Object value) { //se la collection è nulla (perchè non ci sono elementi selezionati), la reimposto al valore di default
        		if(value == null)
        			super.setValue(new HashSet<Strategy>());
        		else
        			super.setValue(value);    		
        	}
    		
            protected Object convertElement(Object element) {
                if (element != null ) {
                    Long id = new Long((String)element);
                    if(id != -1){
                    	Strategy s = strategyManager.get(id);
                    	return s;
                    }
                }
                return null;
            }
        });
    }
    
    @InitBinder
    protected void initBinder5(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(Goal.class, "orgParent", new OrgParentEditorSupport());
    	binder.registerCustomEditor(Strategy.class, "ostrategyParent", new OstrategyParentEditorSupport());
    }

    private class OrgParentEditorSupport extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text != null) {			
				Long id = new Long(text);

				if(id != -1) {
					Goal g = goalManager.get(id);
					setValue(g);	
				} else {
					setValue(null);
				}
			}	
		}
    }
    
    private class OstrategyParentEditorSupport extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text != null) {			
				Long id = new Long(text);

				if(id != -1) {
					Strategy s = strategyManager.get(id);
					setValue(s);	
				} else {
					setValue(null);
				}
			}	
		}
    }
    
    @InitBinder
    protected void initBinder6(HttpServletRequest request, ServletRequestDataBinder binder) {    	
    	binder.registerCustomEditor(Set.class, "associatedMGs", new AssociatedMGCollectionEditor(Set.class));
    	binder.registerCustomEditor(Goal.class, "associatedOG", new AssociatedOGEditorSupport());
    }

    private class AssociatedOGEditorSupport extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text != null) {			
				Long id = new Long(text);

				if(id != -1) {
					Goal og = goalManager.get(id);
					setValue(og);	
				} else {
					setValue(null);
				}
			}	
		}
    }
    
    private class AssociatedMGCollectionEditor extends CustomCollectionEditor {
    	private AssociatedMGCollectionEditor(Class collectionType) {
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
		    		Goal mg = goalManager.get(id);		    		
		    		return mg;	
	    		}
    		}
    		return null;
    	}
    }
}
