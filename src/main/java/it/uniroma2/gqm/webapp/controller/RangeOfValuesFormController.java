package it.uniroma2.gqm.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import it.uniroma2.gqm.model.DefaultRangeOfValuesEnum;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.MetricTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.ProjectManager;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/rangeOfValuesform*")
@SessionAttributes({ "currentProject", "rangeOfValues", "defaultRangeSets" })
public class RangeOfValuesFormController extends BaseFormController
{

	 private UserManager userManager = null;
	 private ProjectManager projectManager = null;

	 public RangeOfValuesFormController()
	 {
		  setCancelView("redirect:rangeOfValues");
		  setSuccessView("redirect:rangeOfValues");
	 }

	 @Autowired
	 public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager)
	 {
		  this.projectManager = projectManager;
	 }

	 @Autowired
	 public void setUserManager(UserManager userManager)
	 {
		  this.userManager = userManager;
	 }

	 @ModelAttribute
	 @RequestMapping(method = RequestMethod.GET)
	 protected RangeOfValues showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception
	 {

		  // String id = request.getParameter("id");
		  RangeOfValues rov = null;

		  Project currentProject = projectManager.getCurrentProject(session);
		  User currentUser = userManager.getUserByUsername(request.getRemoteUser());

		  rov = new RangeOfValues();
		  rov.setProject(currentProject);
		  rov.setDefaultRange(true);
		  rov.setNumeric(true);

		  model.addAttribute("currentProject", currentProject);
		  model.addAttribute("currentUser", currentUser);

		  ArrayList<String> availableMeasurementScaleTypes = new ArrayList<String>();
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.INTERVAL.toString());
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.NOMINAL.toString());
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.ORDINAL.toString());
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.RATIO.toString());
		  model.addAttribute("availableMeasurementScaleTypes", availableMeasurementScaleTypes);

		  List<String> defaultRangeSets = new ArrayList<String>();
		  defaultRangeSets.add(DefaultRangeOfValuesEnum.NATURAL_NUMBERS.toString());
		  defaultRangeSets.add(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  defaultRangeSets.add(DefaultRangeOfValuesEnum.REAL_NUMBERS.toString());
		  
		  model.addAttribute("defaultRangeSets", defaultRangeSets);
		  model.addAttribute("numberTypes", defaultRangeSets);

		  return rov;
	 }


	 @RequestMapping(method = RequestMethod.POST)
	 public String onSubmit(RangeOfValues rangeOfValues, BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	 {
        if (request.getParameter("cancel") != null)
            return getCancelView();


		  if (validator != null)
		  { // validator is null during testing
				validator.validate(rangeOfValues, errors);
				if (errors.hasErrors() && request.getParameter("delete") == null)
				{
					 System.out.println("errors during validation");
					 System.out.println(rangeOfValues);
					 return "rangeOfValuesform";
				}
		  }
		  
		  String[] values = rangeOfValues.getRangeValues().split(",");
		  for(String value : values)
		  {
				if(value.length() > 0)
				{
					 rangeOfValues.setRangeValues(value);
					 break;
				}
		  }
		  
		  System.out.println(errors);
		  System.out.println(rangeOfValues);
		  return getSuccessView();
	 }
}
