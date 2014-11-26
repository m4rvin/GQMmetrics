package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.*;
import it.uniroma2.gqm.service.ProjectManager;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Locale;

 
@Controller
@RequestMapping("/projectform*")
@SessionAttributes({"availableUsers","currentUser"})
public class ProjectFormController extends BaseFormController {
	private ProjectManager projectManager = null;

    @Autowired
    private UserManager userManager;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
 
    public ProjectFormController() {
        setCancelView("redirect:projects");
        setSuccessView("redirect:projects");
    }
 
    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected Project showForm(HttpServletRequest request, Model model)
    throws Exception {
    	Project ret;
        String id = request.getParameter("id");
        
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        
        if (!StringUtils.isBlank(id)) {
            ret= projectManager.get(new Long(id));
        }else {
        	ret = new Project();
        	ret.setProjectOwner(currentUser);
        }
        model.addAttribute("availableUsers",userManager.getAll());
        model.addAttribute("currentUser",currentUser);
        return ret;
    }
    
    @ModelAttribute
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Project project, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
 
        if (validator != null) { // validator is null during testing
            validator.validate(project, errors);
 
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "projectform";
            }
        }
 
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = (project.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();
                
        if (request.getParameter("delete") != null) {
            projectManager.remove(project.getId());
            saveMessage(request, getText("project.deleted", locale));
        } else {
        	
        	project.setProjectOwner(userManager.get(project.getProjectOwner().getId()));
        	System.out.println("Project: " + project);
            projectManager.save(project);
            String key = (isNew) ? "project.added" : "project.updated";
            saveMessage(request, getText(key, locale));
            
            if (!isNew) {
            	success = "redirect:projectform?id=" + project.getId();
        	}
        }
 
        return getSuccessView();
    }

    @InitBinder
    protected void initBinder1(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(List.class, "projectManagers", new CustomCollectionEditor(List.class) {
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    User u = userManager.get(id);
                    return u;
                }
                return null;
            }
        });
    }
    @InitBinder
    protected void initBinder2(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(List.class, "projectTeam", new CustomCollectionEditor(List.class) {
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    User u = userManager.get(id);
                    return u;
                }
                return null;
            }
        });
    }
    @InitBinder
    protected void initBinder3(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(List.class, "GQMTeam", new CustomCollectionEditor(List.class) {
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    User u = userManager.get(id);
                    return u;
                }
                return null;
            }
        });
    }    
}