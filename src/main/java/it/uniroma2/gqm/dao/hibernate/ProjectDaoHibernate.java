package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.ProjectDao;
import it.uniroma2.gqm.model.Project;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.springframework.stereotype.Repository;

@Repository("projectDao")
public class ProjectDaoHibernate extends GenericDaoHibernate<Project, Long>	implements ProjectDao {

	public ProjectDaoHibernate() {
		super(Project.class);
	}
}
