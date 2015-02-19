package it.uniroma2.gqm.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import it.uniroma2.gqm.model.MeasurementScale;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/measurementScaleform*")
public class MeasurementScaleFormController extends BaseFormController{

	
	
	
	@ModelAttribute
	@RequestMapping(method = RequestMethod.GET)
	protected MeasurementScale showForm(HttpServletRequest request,HttpSession session, Model model) throws Exception
	{
		return null;
		
	}

}
