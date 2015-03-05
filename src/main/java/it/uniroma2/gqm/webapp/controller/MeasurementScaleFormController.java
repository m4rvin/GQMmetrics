package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.DefaultOperationManager;
import it.uniroma2.gqm.service.MeasurementScaleManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.RangeOfValuesManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.beans.PropertyEditorSupport;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes({ "measurementScale", "currentProject" })
public class MeasurementScaleFormController extends BaseFormController
{

	 private RangeOfValuesManager rangeOfValuesManager;
	 private DefaultOperationManager defaultOperationManager;
	 private MeasurementScaleManager measurementScaleManager;

	 private ProjectManager projectManager = null;

	 public MeasurementScaleFormController()
	 {
		  setCancelView("redirect:" + ViewName.measurementScales);
		  setSuccessView("redirect:" + ViewName.measurementScales);
	 }

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

	 @Autowired
	 public void setRangeOfValuesManager(@Qualifier("rangeOfValuesManager") RangeOfValuesManager rangeOfValuesManager)
	 {
		  this.rangeOfValuesManager = rangeOfValuesManager;
	 }

	 @Autowired
	 public void setDefaultOperationManager(@Qualifier("defaultOperationManager") DefaultOperationManager defaultOperationManager)
	 {
		  this.defaultOperationManager = defaultOperationManager;
	 }

	 @ModelAttribute
	 private void initModel(Model model, HttpSession session)
	 {
		  if (model.asMap().get("currentProject") == null)
		  {
				this.projectManager.getCurrentProject(session);
		  }
	 }

	 @RequestMapping(value = "/measurementScaleform*", method = RequestMethod.GET)
	 protected String showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception
	 {

		  String id = request.getParameter("id");
		  MeasurementScale measurementScale = null;

		  Project currentProject = this.projectManager.getCurrentProject(session);

		  if (!StringUtils.isBlank(id))
		  {
				measurementScale = this.measurementScaleManager.get(new Long(id));
				boolean isUsed = this.measurementScaleManager.isUsed(measurementScale.getId());
				model.addAttribute("used", isUsed);

				MeasurementScaleTypeEnum type = measurementScale.getType();
				
				populateModel(model, type);

		  } else
		  {
				measurementScale = new MeasurementScale();
				measurementScale.setProject(currentProject);
		  }

		  model.addAttribute("measurementScale", measurementScale);

		return ViewName.measurementScaleForm;

	 }

	 @RequestMapping(value = "/measurementScaleformAjax", method = RequestMethod.GET)
	 @ResponseBody
	 public String getConsistentValues(HttpServletRequest request)
	 {
		  String type = request.getParameter("type");
		  if (!StringUtils.isBlank(type))
		  {
				Map<String, JSONArray> responseMap = new HashMap<String, JSONArray>();
				responseMap.put("rangeOfValues", this.rangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.valueOf(type)));
				responseMap.put("operation", this.defaultOperationManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.valueOf(type)));
				JSONObject allowedValues = new JSONObject(responseMap);
				System.out.println("query result : " + allowedValues.toString());
				return allowedValues.toString();
		  }
		  return null;
	 }

	 @RequestMapping(value = "/measurementScaleform*", method = RequestMethod.POST)
	 public String onSubmit(@ModelAttribute MeasurementScale measurementScale, BindingResult errors, HttpServletRequest request, HttpServletResponse response, Model model)
	 {
		  if (request.getParameter("cancel") != null)
				return getCancelView();

		  if (validator != null)
		  {
				validator.validate(measurementScale, errors);
				if (errors.hasErrors() && request.getParameter("delete") == null)
				{
					 System.out.println(errors);
					 System.out.println(measurementScale);
					 if(measurementScale.getType() != null)
						  populateModel(model, measurementScale.getType());
					 return ViewName.measurementScaleForm;
				}
		  }
		  
		  if(request.getParameter("delete") != null)
		  {
				Long id = measurementScale.getId();
				
				if(id != 0 && !this.measurementScaleManager.isUsed(id))
					 {
					 	this.measurementScaleManager.remove(id);
					 	return getSuccessView();
					 }
				
				else
					 return "measurementScaleform";
		  }
		  System.out.println(measurementScale);
		  this.measurementScaleManager.save(measurementScale);
		  return getSuccessView();
	 }

	 @InitBinder(value = "measurementScale")
	 public void initBinder(WebDataBinder binder)
	 {

		  binder.registerCustomEditor(RangeOfValues.class, "rangeOfValues", new MeasurementScaleEditorSupport());
		  binder.registerCustomEditor(Set.class, "operations", new CustomCollectionEditor(Set.class)
		  {
				@Override
				protected Object convertElement(Object obj)
				{
					 Long operation_id = new Long((String) obj);
					 return defaultOperationManager.get(operation_id);
				}
		  });
	 }

	 private class MeasurementScaleEditorSupport extends PropertyEditorSupport
	 {

		  @Override
		  public void setAsText(String text)
		  {
				if (text != null && !text.equals(""))
				{
					 RangeOfValues rov = null;
					 try
					 {
						  rov = rangeOfValuesManager.findById(new Long(text));
						  setValue(rov);
					 } catch (Exception e)
					 {
						  System.out.println(e);
						  setValue(null);
					 }
				} else
				{
					 System.out.println(" FIXME error in MeasurementScaleEditorSupport conversion");
					 setValue(null);
				}
				// FIXME error
		  }
	 }

	 private void populateModel(Model model, MeasurementScaleTypeEnum type)
	 {
		  List<RangeOfValues> rovs = this.rangeOfValuesManager.findBySupportedMeasurementScaleOBJ(type);
		  model.addAttribute("supportedRangeOfValues", rovs);

		  List<DefaultOperation> ret = this.defaultOperationManager.findBySupportedMeasurementScaleOBJ(type);
		  model.addAttribute("supportedOperations", ret);
	 }

}
