package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.service.MeasurementScaleManager;
import it.uniroma2.gqm.service.ProjectManager;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/measurementScales*")
public class MeasurementScaleController
{

	 private ProjectManager projectManager = null;
	 private MeasurementScaleManager measurementScaleManager = null;

	 @Autowired
	 public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager)
	 {
		  this.projectManager = projectManager;
	 }

	 @Autowired
	 public void setMeasurementScaleManager(@Qualifier("measurementScaleManager") MeasurementScaleManager measurementScaleManager)
	 {
		  this.measurementScaleManager = measurementScaleManager;
	 }

	 @RequestMapping(method = RequestMethod.GET)
	 public ModelAndView handleRequest(HttpSession session) throws Exception
	 {
		  Project currentProject = projectManager.getCurrentProject(session);
		  List<MeasurementScale> ms = measurementScaleManager.findByProject(currentProject);
		  return new ModelAndView().addObject("measurementScaleList", ms);
	 }

}
