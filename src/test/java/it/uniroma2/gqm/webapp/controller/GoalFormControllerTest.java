package it.uniroma2.gqm.webapp.controller;

import static org.junit.Assert.*;
import it.uniroma2.gqm.model.Goal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

public class GoalFormControllerTest extends BaseControllerTestCase {
	@Autowired
	private GoalFormController form;
	private Goal goal;
	private MockHttpServletRequest request;
	private MockHttpSession session;
	private Model model;
	
	@Test
	public void testSave() throws Exception {
		request = newGet("/goalform");
		request.addParameter("id", "1");
		
		session = new MockHttpSession();
		
		model = new ExtendedModelMap();
				
		goal = form.showForm(request, session, model);
		assertNotNull(goal);
	}
}
