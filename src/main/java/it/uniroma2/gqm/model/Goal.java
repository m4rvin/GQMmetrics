package it.uniroma2.gqm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;

@Entity
@NamedQueries({
    @NamedQuery(
            name = "findGoalByProject",
            query = "select g from Goal g  where g.project.id= :project_id "
    ),
    @NamedQuery(
            name = "findMeasuredGoal",
            query = "select distinct g from Goal g inner join g.questions gq " +
            		" inner join gq.pk.question q  " +
            		" inner join q.metrics qm " +
            		" inner join qm.pk.metric m " +
            		" where g.project.id= :project_id "
    ),
    @NamedQuery(
            name = "findOrganizationalGoal",
            query = "select distinct g from Goal g" +
            		" where g.type = 0 and g.project.id= :project_id "
    )
})
public class Goal extends BaseObject {

	private static final long serialVersionUID = -5289775436595676632L;
	
	/*****Common fields*****/
	private String description;	
	private Integer type;
	private String scope;
	private String focus;
	private User goalOwner;
	private User goalEnactor;
	private GoalStatus status;
	private Long id;
	private Project project;		
	private Set<User> QSMembers = new HashSet<User>();
	private Set<User> MMDMMembers = new HashSet<User>();
	private Set<GoalQuestion> questions = new HashSet<GoalQuestion>();	
	private Set<User> votes = new HashSet<User>();
	private String refinement;
	/*****Common fields*****/
	
	/*****OG fields*****/
	private String activity;
	private String object;
	private String magnitude;
	private String timeframe;
	private String constraints;
	
	//OG hierarchy fields	
	private Goal orgParent;
	private Strategy ostrategyParent;
	
	private int childType = -1; //-1=no figli, 0=figli OG, 1=figli Strategy
	private int parentType = -1; //-1=no padre, 0=padre OG, 1=padre Strategy
	
	private Set<Goal> orgChild = new HashSet<Goal>();
	private Set<Strategy> ostrategyChild = new HashSet<Strategy>();
	
	private Set<Goal> associatedMGs = new HashSet<Goal>();
	/*****OG fields*****/
	
	/*****MGG fields*****/
	private String subject;
	private String context;
	private String viewpoint;
	private String impactOfVariation;
	
	private Goal associatedOG;
	/*****MGG fields*****/
	
	public Goal() {
		
	}

	public Goal(Long id) {
		this.id = id;
	}
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id @Column(name = "goal_id",nullable=false,unique=true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "description", length = 255, nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "type", length = 255, nullable = false)
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "subject", length = 255)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "scope", length = 255)
	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Column(name = "focus", length = 255)
	public String getFocus() {
		return focus;
	}

	public void setFocus(String focus) {
		this.focus = focus;
	}

	@Column(name = "context", length = 255)
	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	@Column(name = "viewpoint", length = 255)
	public String getViewpoint() {
		return viewpoint;
	}

	public void setViewpoint(String viewpoint) {
		this.viewpoint = viewpoint;
	}

	@Column(name = "impact_of_variation", length = 255)
	public String getImpactOfVariation() {
		return impactOfVariation;
	}

	public void setImpactOfVariation(String impactOfVariation) {
		this.impactOfVariation = impactOfVariation;
	}

	@Transient
	public String getTypeAsString() {
		if(isMG())
			return GoalType.MG.getString();
		else
			return GoalType.OG.getString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Goal other = (Goal) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Goal [id=" + id + ", description=" + description + "]";
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public GoalStatus getStatus() {
		return status;
	}
	
	public void setStatus(GoalStatus status) {
		this.status = status;
	}
	
	@ManyToOne
	@JoinColumn(name = "go_id", nullable = false)	
	public User getGoalOwner() {
		return goalOwner;
	}
	
	public void setGoalOwner(User goalOwner) {
		this.goalOwner = goalOwner;
	}

    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name = "goal_qs", joinColumns = { @JoinColumn(name = "goal_id") }, inverseJoinColumns = @JoinColumn(name = "user_id"))
	public Set<User> getQSMembers() {
		return QSMembers;
	}
    
	public void setQSMembers(Set<User> qSMembers) {
		QSMembers = qSMembers;
	}

	@ManyToOne
	@JoinColumn(name = "ge_id", nullable = true)	
	public User getGoalEnactor() {
		return goalEnactor;
	}
	
	public void setGoalEnactor(User goalEnactor) {
		this.goalEnactor = goalEnactor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
    @ManyToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinTable(name = "goal_mmdm",joinColumns = { @JoinColumn(name = "goal_id") },inverseJoinColumns = @JoinColumn(name = "user_id"))	
	public Set<User> getMMDMMembers() {
		return MMDMMembers;
	}
    
	public void setMMDMMembers(Set<User> mMDMMembers) {
		this.MMDMMembers = mMDMMembers;
	}
	
	@Column(name = "refinement", length = 255)
	public String getRefinement() {
		return refinement;
	}

	public void setRefinement(String refinement) {
		this.refinement = refinement;
	}
	
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "goal_vote",joinColumns = { @JoinColumn(name = "goal_id") },inverseJoinColumns = @JoinColumn(name = "user_id"))	
	public Set<User> getVotes() {
		return votes;
	}
    
	public void setVotes(Set<User> votes) {
		this.votes = votes;
	}
	
	@Transient
	public int getNumberOfVote(){
		return getVotes().size();
	}
	
	@Transient
	public int getQuorum(){
		return this.project.getProjectManagers().size();
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.goal")
	public Set<GoalQuestion>  getQuestions() {
		return questions;
	}

	public void setQuestions(Set<GoalQuestion> questions) {
		this.questions = questions;
	}

	@Column(name = "activity", length = 255)
	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	@Column(name = "object", length = 255)
	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Column(name = "magnitude", length = 255)
	public String getMagnitude() {
		return magnitude;
	}

	public void setMagnitude(String magnitude) {
		this.magnitude = magnitude;
	}

	@Column(name = "timeframe", length = 255)
	public String getTimeframe() {
		return timeframe;
	}

	public void setTimeframe(String timeframe) {
		this.timeframe = timeframe;
	}
	
	@Column(name = "constraints", length = 255)
	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	@Transient
	public int getChildType() {
		//Prima di verificare il tipo di figli, devo caricarli da db (di base sono caricati in modalità lazy)
		getOrgChild();
		getOstrategyChild();
		
		if(orgChild.size() > 0){
			childType = 0;
		}else if (ostrategyChild.size() > 0) {
			childType = 1;
		}else {
			childType = -1;
		}
		
		return childType;
	}

	@Transient
	public int getParentType() {		
		//utilizzo i getter anzichè le variabili, perchè di base non vengono caricati i parent, essendo definiti come lazy
		if(getOrgParent() != null)
			parentType = 0;
		else if(getOstrategyParent() != null)
			parentType = 1;
		else
			parentType = -1;		
		
		return parentType;
	}
	
	@ManyToOne
	//@JoinColumn(name="oparent_id")
	public Goal getOrgParent() {
		return orgParent;
	}

	public void setOrgParent(Goal orgParent) {
		this.orgParent = orgParent;
	}
	
	@OneToMany(mappedBy="orgParent")
	public Set<Goal> getOrgChild() {
		return orgChild;
	}
	
	public void setOrgChild(Set<Goal> orgChild) {
		this.orgChild = orgChild;
	}
	
	@ManyToOne
	//@JoinColumn(name="sparent_id")
	public Strategy getOstrategyParent() {
		return ostrategyParent;
	}

	public void setOstrategyParent(Strategy strategyParent) {
		this.ostrategyParent = strategyParent;
	}

	@OneToMany(mappedBy="sorgParent")
	public Set<Strategy> getOstrategyChild() {
		return ostrategyChild;
	}
	
	public void setOstrategyChild(Set<Strategy> strategyChild) {
		this.ostrategyChild = strategyChild;
	}
	
	public boolean hasChildren() {
		return getChildType() != -1;
	}
	
	public boolean hasParent() {
		return getParentType() != -1;
	}
	
	public boolean areChildrenGoal() {
		return getChildType() == 0;
	}
	
	public boolean areChildrenStrategy() {
		return getChildType() == 1;
	}
	
	@Transient
	public boolean isParentGoal() {
		return getParentType() == 0;
	}
	
	@Transient
	public boolean isParentStrategy() {
		return getParentType() == 1;
	}
	
	@Transient
	public boolean isMG() {
		if(type == null)
			return false;
		
		return type.intValue() == GoalType.MG.getId();
	}
	
	@Transient
	public boolean isOG() {
		if(type == null)
			return false;
		
		return type.intValue() == GoalType.OG.getId();
	}
	
	@OneToMany(mappedBy="associatedOG")
	public Set<Goal> getAssociatedMGs() {
		return associatedMGs;
	}

	public void setAssociatedMGs(Set<Goal> associatedMGs) {
		this.associatedMGs = associatedMGs;
	}

	//mg è proprietario della relazione, e quindi prima di salvarlo devi essere sicuro che l'og sia esistente su db
	@ManyToOne
	public Goal getAssociatedOG() {
		return associatedOG;
	}

	public void setAssociatedOG(Goal associatedOG) {
		this.associatedOG = associatedOG;
	}
}
