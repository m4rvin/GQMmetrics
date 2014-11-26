package it.uniroma2.gqm.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/graph*")
@SessionAttributes({"currentProject","goal","currentUser"})
public class GraphController {
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpSession session, HttpServletRequest request) throws Exception {
		
		String identifier = request.getParameter("id");
		return new ModelAndView().addObject("identifier", identifier);
	}

}
