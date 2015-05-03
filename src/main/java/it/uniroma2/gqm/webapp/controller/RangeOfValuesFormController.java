package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.DefaultRangeOfValuesEnum;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.RangeOfValuesManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/rangeOfValuesform*")
@SessionAttributes({ "rangeOfValues", "currentProject" })
public class RangeOfValuesFormController extends BaseFormController
{

	 private ProjectManager projectManager = null;
	 private RangeOfValuesManager rangeOfValuesManager = null;
	 private RangeOfValueValidator customValidator;

	 public RangeOfValuesFormController()
	 {
		  setCancelView("redirect:" + ViewName.rangeOfValues);
		  setSuccessView("redirect:" + ViewName.rangeOfValues);
	 }

	 @Autowired
	 public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager)
	 {
		  this.projectManager = projectManager;
	 }

	 @Autowired
	 public void setRangeOfValuesManager(@Qualifier("rangeOfValuesManager") RangeOfValuesManager rangeOfValuesManager)
	 {
		  this.rangeOfValuesManager = rangeOfValuesManager;
	 }

	 @Autowired
	 public void setCustomValidator(@Qualifier("rangeOfValueValidator") RangeOfValueValidator validator)
	 {
		  this.customValidator = validator;
	 }

	 @ModelAttribute
	 private void addModelAttributes(Model model, HttpSession session)
	 {
		  if (model.asMap().get("currentProject") == null)
		  {
				this.projectManager.getCurrentProject(session);
		  }

		  ArrayList<String> availableMeasurementScaleTypes = new ArrayList<String>();
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.NOMINAL.toString());
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.ORDINAL.toString());
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.INTERVAL.toString());
		  availableMeasurementScaleTypes.add(MeasurementScaleTypeEnum.RATIO.toString());
		  model.addAttribute("availableMeasurementScaleTypes", availableMeasurementScaleTypes);

		  List<String> defaultRangeSets = new ArrayList<String>();
		  defaultRangeSets.add(DefaultRangeOfValuesEnum.NATURAL_NUMBERS.toString());
		  defaultRangeSets.add(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  defaultRangeSets.add(DefaultRangeOfValuesEnum.REAL_NUMBERS.toString());

		  model.addAttribute("defaultRangeSets", defaultRangeSets);
		  model.addAttribute("numberTypes", defaultRangeSets);
	 }

	 @RequestMapping(method = RequestMethod.GET)
	 protected String showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception
	 {
		  String id = request.getParameter("id");
		  RangeOfValues rov = null;

		  Project currentProject = this.projectManager.getCurrentProject(session);

		  if (!StringUtils.isBlank(id)) // rov già creato, visualizzo le info per
												  // l'update
		  {
				rov = this.rangeOfValuesManager.findById(new Long(id));
				boolean isUsed = this.rangeOfValuesManager.isUsed(rov.getId());
				model.addAttribute("used", isUsed);
		  } else
		  {
				rov = new RangeOfValues();
				rov.setProject(currentProject);
				rov.setDefaultRange(true);
				rov.setNumeric(true);
				rov.setNumberType("none");
				rov.setRangeValues("none");
		  }

		  model.addAttribute("rangeOfValues", rov);

		  return ViewName.rangeOfValuesForm;
	 }

	 @RequestMapping(method = RequestMethod.POST)
	 public String onSubmit(@Valid RangeOfValues rangeOfValues, BindingResult errors, HttpServletRequest request, SessionStatus status, HttpSession session, Model model)
	 {
		  if (request.getParameter("cancel") != null)
		  {
				// status.setComplete();
				return getCancelView();
		  }

		  if (validator != null)
		  { // validator is null during testing
				validator.validate(rangeOfValues, errors);
				if (errors.hasErrors() && request.getParameter("delete") == null)
				{
					 System.out.println(errors);
					 System.out.println(rangeOfValues);
					 return ViewName.rangeOfValuesForm;
				}
		  }

		  if (request.getParameter("delete") != null)
		  {
				Long id = rangeOfValues.getId();

				if (id != 0 && !this.rangeOfValuesManager.isUsed(id))
				{
					 this.rangeOfValuesManager.remove(id);
					 return getSuccessView();
				}

				else
					 return ViewName.rangeOfValuesForm;
		  }

		  System.out.println(rangeOfValues);
		  try
		  {
				rangeOfValues.sortAndMerge();
				rangeOfValues = rangeOfValuesManager.saveRangeOfValues(rangeOfValues);

		  } catch (DataIntegrityViolationException e)
		  {
				System.err.println(e.getMessage());
				if (e.getMessage().contains("name"))
				{
					 model.addAttribute("duplicate_value", "A range of values with the same name already exists. Please change the name and retry.");

				} else
				{
					 model.addAttribute("duplicate_value", "The range of values already exists in the database. Change some parameter and retry.");
				}
				return ViewName.rangeOfValuesForm;
		  }
		  // status.setComplete();
		  return getSuccessView();
	 }

	 @InitBinder(value = "rangeOfValues")
	 public void initBinder(WebDataBinder binder)
	 {
		  binder.setValidator(this.customValidator);
		  binder.registerCustomEditor(String.class, "numberType", new RangeValuesEditorSupport());
	 }

	 private class RangeValuesEditorSupport extends PropertyEditorSupport
	 {

		  @Override
		  public void setAsText(String text)
		  {
				if (text != null && !text.equals(""))
				{
					 String[] values = text.split(",");
					 String res = "";
					 for (String val : values)
					 {
						  if (val.length() > 0)
						  {
								if (res.length() > 0)
									 res += ",";
								res = res + val;
						  }
					 }
					 setValue(res);
				} else
				{
					 System.out.println("Error in RangeOfValuesEditorSupport conversion");
				}
		  }
	 }
}
