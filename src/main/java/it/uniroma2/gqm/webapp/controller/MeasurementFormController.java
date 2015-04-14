package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CollectingTypeEnum;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.MeasurementManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping(ViewName.measurementForm)
@SessionAttributes({"currentProject","measurement","currentUser","availableMetrics"})
public class MeasurementFormController extends BaseFormController {
	
	@Autowired
	private ComplexMetricManager metricManager;
    
    @Autowired
    private MeasurementManager measurementManager;
    
	private UserManager userManager = null;
	private ProjectManager projectManager = null;
    
	private MeasurementValidator customValidator;
	
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    

    public MeasurementValidator getCustomValidator() {
		return customValidator;
	}


    @Autowired
	public void setCustomValidator(@Qualifier("measurementValidator") MeasurementValidator customValidator) {
		this.customValidator = customValidator;
	}


	public MeasurementFormController() {
        setCancelView("redirect:measurements");
        setSuccessView("redirect:measurements");
    }

    @ModelAttribute("measurement")
    @RequestMapping(method = RequestMethod.GET)
    protected Measurement showForm(HttpServletRequest request,HttpSession session, Model model) throws Exception {
        String id = request.getParameter("id");
        Measurement ret = null;

        Project currentProject = projectManager.getCurrentProject(session);
        
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        if (!StringUtils.isBlank(id)) {
            ret = measurementManager.get(new Long(id));
        } else {
        	ret = new Measurement();        	
        	ret.setMeasurementOwner(currentUser);
        }
        model.addAttribute("currentProject",currentProject);
        model.addAttribute("currentUser",currentUser);        
        model.addAttribute("availableMetrics",metricManager.findMeasureableSimpleMetricByProject(currentProject));
        return ret;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(@ModelAttribute("measurement") @Valid Measurement measurement, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
    	
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
        
        //check errors in tag-validated fields
        if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
            return ViewName.measurementForm;
        }
        
        //check errors using custom validator
        if (validator != null) { // validator is null during testing ???
            validator.validate(measurement, errors);
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
            	//TODO fill model to give back already inserted values
                return ViewName.measurementForm;
            }
        }
 
        
        boolean isNew = (measurement.getId() == null);
        Locale locale = request.getLocale();
 
        if (request.getParameter("delete") != null) {
        	measurementManager.remove(measurement.getId());
            saveMessage(request, getText("measurement.deleted", locale));
        } else {
        	if(measurement.getMeasurementOwner()==null)
        		measurement.setMeasurementOwner(userManager.getUserByUsername(request.getRemoteUser()));
         
        	SimpleMetric metric = (SimpleMetric) measurement.getMetric();
        	
        	//a single value metric must have only one associated measurement. Otherwise save every measurements.
        	if(measurement.getMetric().getCollectingType().equals(CollectingTypeEnum.SINGLE_VALUE))
        	{
        		List<Measurement> saved_measurement = measurementManager.findMeasuremntsByMetric(metric);
        		if(saved_measurement.size() > 0){//there is a saved measurement. Overwrite it. 
        			measurementManager.remove(saved_measurement.get(0).getId());
        			//and remove reference in metric
            		metric.getMeasurements().clear();
        		}
        	}
        	measurement = measurementManager.save(measurement);
    		metric.getMeasurements().add(measurement);

    		
    		Double measurementResult = FormulaHandler.evaluateFormula(metric);
    		metric.setActualValue(measurementResult);
    		
    		metric = (SimpleMetric) metricManager.save(metric);

    		Set<CombinedMetric> composedByMetrics = metric.getComposerFor();

    		int i = 0;
    		boolean evaluation_success = true;
    		for (CombinedMetric composedByMetric : composedByMetrics){
    			evaluation_success = FormulaHandler.evaluateFormula(composedByMetric, metricManager);
    			if(!evaluation_success)
    				i++;
    		}
    		String error_message = "";
    		if(i>0)
    			error_message = "\nWarning: Error evaluation of formula in " + i + " combined metrics.";
   		  
            String key = (isNew) ? "measurement.added" : "measurement.updated";
            key = key + error_message;
            saveMessage(request, getText(key, locale));
        }
        String success = getSuccessView();
        return success;
    }

    @InitBinder/*(value = "measurement")*/
	public void initBinder()
	{
    	//overwrite default validator from basecontroller autowiring
    	validator = this.customValidator;
	}
    
    @InitBinder
    protected void initBinder1(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(AbstractMetric.class, "metric", new MetricEditorSupport());
    }

    private class MetricEditorSupport extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text != null && !StringUtils.isBlank(text)) {		
				Long id = new Long(text);
				AbstractMetric m = metricManager.get(id);
				setValue(m);
			} else
				setValue(null);
		}
    }
}
