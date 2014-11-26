package it.uniroma2.gqm.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;

@Entity
public class Project extends BaseObject  implements Serializable{
	private static final long serialVersionUID = -944346145252151052L;
	
	private Long id;
	private String name;
	private String description;
	private List<User> projectManagers;
	private List<User> projectTeam;
	private List<User> GQMTeam;
	private List<Goal> goals;
	private User projectOwner;
	
	public Project (){
		;
	}
	public Project (Long id){
		this.id = id;
	}
	
	@Id
	@Column(name="project_id")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="name", length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="description", length=255)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Project [id=" + id + ", name=" + name + ", description="
				+ description + "]";
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
		Project other = (Project) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_pm",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
	public List<User> getProjectManagers() {
		return projectManagers;
	}
	public void setProjectManagers(List<User> projectManagers) {
		this.projectManagers = projectManagers;
	}

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_team",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
	public List<User> getProjectTeam() {
		return projectTeam;
	}
	public void setProjectTeam(List<User> projectTeam) {
		this.projectTeam = projectTeam;
	}
	
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "project_gqm",
            joinColumns = { @JoinColumn(name = "project_id") },
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )	
	public List<User> getGQMTeam() {
		return GQMTeam;
	}
	public void setGQMTeam(List<User> gQMTeam) {
		GQMTeam = gQMTeam;
	}
	
	@OneToMany(mappedBy="project", cascade=CascadeType.ALL)
	public List<Goal> getGoals() {
		return goals;
	}
	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "po_id", nullable = true)	
	public User getProjectOwner() {
		return projectOwner;
	}
	public void setProjectOwner(User projectOwner) {
		this.projectOwner = projectOwner;
	}
}
