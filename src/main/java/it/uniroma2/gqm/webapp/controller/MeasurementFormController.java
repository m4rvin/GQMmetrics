package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.service.MeasurementManager;
import it.uniroma2.gqm.service.SimpleMetricManager;
import it.uniroma2.gqm.service.ProjectManager;

import java.beans.PropertyEditorSupport;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
@RequestMapping("/measurementform*")
@SessionAttributes({"currentProject","measurement","currentUser","availableMetrics"})
public class MeasurementFormController extends BaseFormController {
	@Autowired
	private SimpleMetricManager metricManager;
    
    @Autowired
    private MeasurementManager measurementManager;
    
	private UserManager userManager = null;
	private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    

    public MeasurementFormController() {
        setCancelView("redirect:measurements");
        setSuccessView("redirect:measurements");
    }

    @ModelAttribute
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
        model.addAttribute("availableMetrics",metricManager.findByProject(currentProject));
        return ret;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Measurement measurement, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
        
                
        if (validator != null) { // validator is null during testing
            validator.validate(measurement, errors);
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "measurementform";
            }
        }
 
        if(measurement.getMetric() == null) {
        	errors.rejectValue("metric", "metric", "Metric field cannot be blank!"); 
        	return "measurementform";
        }
        
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = (measurement.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();
 
        if (request.getParameter("delete") != null) {
        	measurementManager.remove(measurement.getId());
            saveMessage(request, getText("measurement.deleted", locale));
        } else {
        	if(measurement.getMeasurementOwner()==null)
        		measurement.setMeasurementOwner(userManager.getUserByUsername(request.getRemoteUser()));
            
        	measurementManager.save(measurement);
            String key = (isNew) ? "measurement.added" : "measurement.updated";
            saveMessage(request, getText(key, locale));
        }
        return success;
    }

    @InitBinder
    protected void initBinder1(HttpServletRequest request, ServletRequestDataBinder binder) {
    	binder.registerCustomEditor(SimpleMetric.class, "metric", new MetricEditorSupport());
    }

    private class MetricEditorSupport extends PropertyEditorSupport {
		@Override
		public void setAsText(String text) throws IllegalArgumentException {
			if(text != null && !StringUtils.isBlank(text)) {		
				Long id = new Long(text);
				SimpleMetric m = metricManager.get(id);
				setValue(m);
			} else
				setValue(null);
		}
    }
}
