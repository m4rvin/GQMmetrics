package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.Unit;
import it.uniroma2.gqm.service.DefaultOperationManager;
import it.uniroma2.gqm.service.MeasurementScaleManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.RangeOfValuesManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.appfuse.service.GenericManager;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "measurementScale", "currentProject", "units" })
public class MeasurementScaleFormController extends BaseFormController
{

	 private RangeOfValuesManager rangeOfValuesManager;
	 private DefaultOperationManager defaultOperationManager;
	 private MeasurementScaleManager measurementScaleManager;
	 private GenericManager<Unit, Long> unitManager = null;

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
	 public void setUnitManager(@Qualifier("unitManager") GenericManager<Unit, Long> unitManager)
	 {
		  this.unitManager = unitManager;
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
		  List<Unit> units = (ArrayList<Unit>) this.unitManager.getAll();
		  
		  Collections.sort(units, new Comparator<Unit>()
		 {
			  @Override
			  public int compare(Unit o1, Unit o2)
			  {
					return (o1.getId() > o2.getId()) ? 1 : -1;
			  }
	
		 });
		  
		  model.addAttribute("units", units);
		  
		  List<RangeOfValues> rovs = this.rangeOfValuesManager.findByProject((Project)session.getAttribute("currentProject"));
		  model.addAttribute("supportedRangeOfValues", rovs);

	 }

	 @RequestMapping(value = ViewName.measurementScaleForm, method = RequestMethod.GET)
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
				model.addAttribute("units", unitManager.getAll());
		  }

		  model.addAttribute("measurementScale", measurementScale);

		return ViewName.measurementScaleForm;

	 }

	 @RequestMapping(value = ViewName.measurementScaleFormAjax, method = RequestMethod.GET)
	 @ResponseBody
	 public String getConsistentValues(HttpServletRequest request)
	 {
		  String type = request.getParameter("type");
		  if (!StringUtils.isBlank(type))
		  {
				JSONArray allowedOperations =  this.defaultOperationManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.valueOf(type));
				System.out.println("query result : " + allowedOperations.toString());
				return allowedOperations.toString();
		  }
		  return null;
	 }

	 @RequestMapping(value = ViewName.measurementScaleForm, method = RequestMethod.POST)
	 public String onSubmit(@Valid @ModelAttribute MeasurementScale measurementScale, BindingResult errors, HttpServletRequest request, HttpServletResponse response, SessionStatus status, Model model)
	 {
		  if (request.getParameter("cancel") != null){
			  status.setComplete();
			  return getCancelView();
		  }

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
					 	status.setComplete();
					 	return getSuccessView();
					 }
				
				else
					 return ViewName.measurementScaleForm;
		  }
		  /*if (measurementScale.getMeasurementUnit() != null && measurementScale.getMeasurementUnit().getId() != null)
			{
				measurementScale.setMeasurementUnit(unitManager.get(measurementScale.getMeasurementUnit().getId()));
			} else
			{
				 measurementScale.setMeasurementUnit(null);
			}*/
		  
		  Unit unit = null;
		  System.out.println(measurementScale);
		  try{
				unit = measurementScale.getMeasurementUnit();
				  if(measurementScale.getMeasurementUnit().getId() == null)
				  {
						 unit = unitManager.save(unit);
						 measurementScale.setMeasurementUnit(unit);
				  }
			  measurementScale = this.measurementScaleManager.save(measurementScale);
		  }
		  catch(DataIntegrityViolationException e){
			  System.err.println(e.getMessage());
			  unitManager.remove(unit);
			  if(e.getMessage().contains("name"))
			  {
				  model.addAttribute("duplicate_value", "A measurement scale with the same name already exists. Please change the name and retry.");
			  }
			  else
			  {
				  model.addAttribute("duplicate_value", "The measurement scale already exists in the database. Change some parameter and retry.");
			  }
			  return ViewName.measurementScaleForm;
		  }
		  
		  status.setComplete();
		  return getSuccessView();
	 }

	 @InitBinder(value = "measurementScale")
	 public void initBinder(WebDataBinder binder)
	 {

		  binder.registerCustomEditor(RangeOfValues.class, "rangeOfValues", new RangeOfValuesEditorSupport());
		  binder.registerCustomEditor(Unit.class, "measurementUnit", new MeasurementUnitEditorSupport());
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

	 private class RangeOfValuesEditorSupport extends PropertyEditorSupport
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
	 
	 private class MeasurementUnitEditorSupport extends PropertyEditorSupport
	 {
		  @Override
		  public String getAsText()
		  {
				 Unit unit = (Unit) this.getValue();
				 if(unit != null)
				 {
					  if(unit.getId() == null)
						 {
							  return unit.getName();
						 }
						 else
							  return unit.toString();
				 }
				 return null;
		  }
		  
		  @Override
		  public void setAsText(String text)
		  {
				if (text != null && !text.equals(""))
				{
					 Unit unit = null;
					 String[] formResult = text.split(",");
					 
					 try
					 {
						  Long unit_id = new Long(formResult[0]);
						  
						  if(unit_id == -12)
						  {
								 String unit_custom_name = formResult[1];
								if(!unit_custom_name.equals(""))
								{
									 unit = new Unit();
									 unit.setName(unit_custom_name);
									 setValue(unit);
								}
								else
									 setValue(null);
						  }
						  else
						  {
								unit = unitManager.get(new Long(formResult[0]));
								setValue(unit);
						  }
					 }
					 catch(Exception e)
					 {
						  System.out.println(e);
						  setValue(null);
					 }
				}
				else
					 setValue(null);
		  }
	 }

	 private void populateModel(Model model, MeasurementScaleTypeEnum type)
	 {
		  List<DefaultOperation> defops = this.defaultOperationManager.findBySupportedMeasurementScaleOBJ(type);
		  model.addAttribute("supportedOperations", defops);
	 }

}
