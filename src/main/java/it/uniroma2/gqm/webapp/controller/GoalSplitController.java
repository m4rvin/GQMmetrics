package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.service.GoalManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/goalsplit*")
public class GoalSplitController extends BaseFormController {
	
    @Autowired
    private GoalManager goalManager;
    
    public GoalSplitController() {
        setCancelView("redirect:goals");
        setSuccessView("redirect:goals");
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Goal goal, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
    	Goal g =goalManager.get(goal.getId());     			
    	goalManager.goalSplitting(g, new Integer(request.getParameter("split")).intValue());
    	return getSuccessView();
    }
}
