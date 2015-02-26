package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.RangeOfValuesManager;

import java.beans.PropertyEditorSupport;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MeasurementScaleFormController extends BaseFormController{

	 private RangeOfValuesManager rangeOfValuesManager;
	
	 @Autowired
	 public void setRangeOfValuesManager(@Qualifier("rangeOfValuesManager") RangeOfValuesManager rangeOfValuesManager) {
		this.rangeOfValuesManager = rangeOfValuesManager;
	 }
	
	@ModelAttribute
	@RequestMapping(value = "/measurementScaleform*", method = RequestMethod.GET)
	protected MeasurementScale showForm(HttpServletRequest request,HttpSession session, Model model) throws Exception
	{
		MeasurementScale ms = null;
		
		ms = new MeasurementScale();
		
		return ms;
		
	}

	@RequestMapping(value = "/measurementScaleformAjax", method = RequestMethod.GET)
	@ResponseBody
	public String getConsistentValues(HttpServletRequest request)
	{
		 String type = request.getParameter("type");
		 if(!StringUtils.isBlank(type))
		 {
			  JSONArray allowedRangeOfValues = this.rangeOfValuesManager.findBySupportedMeasurementScale(MeasurementScaleTypeEnum.valueOf(type));
	  	     System.out.println("query result : " + allowedRangeOfValues.toString());
			  return allowedRangeOfValues.toString();
		 }
		 return null;
	}
	
	@RequestMapping(method = RequestMethod.POST)
	 public String onSubmit(MeasurementScale measurementScale, BindingResult errors, HttpServletRequest request, HttpServletResponse response)
	 {
       if (request.getParameter("cancel") != null)
           return getCancelView();


		  if (validator != null)
		  { // validator is null during testing
				validator.validate(measurementScale, errors);
				if (errors.hasErrors() && request.getParameter("delete") == null)
				{
					 System.out.println(errors);
					 System.out.println(measurementScale);
					 return "measurementScaleform";
				}
		  }
		   
		  System.out.println(measurementScale);
		  //rangeOfValuesManager.saveRangeOfValues(rangeOfValues);
		  return getSuccessView();
	 }
	
	 @InitBinder(value="measurementScale")
	 public void initBinder(WebDataBinder binder)
	 {
		  //binder.setValidator(this.customValidator);
		  binder.registerCustomEditor(RangeOfValues.class, "rangeOfValues", new MeasurementScaleEditorSupport());
	 }
	 
	 private class MeasurementScaleEditorSupport extends PropertyEditorSupport {
		  
		  @Override
		  public void setAsText(String text)
		  {
				if(text != null && !text.equals(""))
				{
					RangeOfValues rov = null;
					try{
						rov = rangeOfValuesManager.findById(new Long(text));
						setValue(rov);
					}
					catch(Exception e){
						System.out.println(e);
						setValue(null);
					}
				}	
				else
					System.out.println("error in MeasurementScaleEditorSupport conversion");
				//FIXME error
		  }
	 }

	
}
