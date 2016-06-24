package com.jlb.tools.metamodel;

import java.util.List;

import com.jlb.tools.metamodel.criterion.ICriterion;

public interface IDataProcessorServices {

	public void createDatabase();

	public void storeEntities(List<Entity> entities);

	public List<Entity> requestEntities(ICriterion criterion);

	public List<Link> requestLinks(Entity entity);

	public void deleteObjects(List<Entity> entities);

	public void deleteLinks(List<Link> links);

	public void endDatabaseService();
}
