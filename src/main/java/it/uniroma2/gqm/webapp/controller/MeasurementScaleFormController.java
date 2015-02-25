package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.RangeOfValuesManager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	
	public JSONObject getJSONObject(List<String> input)
	{
		 return null;
	}
}
