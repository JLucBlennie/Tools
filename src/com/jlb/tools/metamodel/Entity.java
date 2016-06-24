package com.jlb.tools.metamodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jlb.tools.metamodel.attributes.IAttribute;

public abstract class Entity {

	protected static final ResourceBundle DICO_PROPERTIES = ResourceBundle.getBundle("resources/dico",
			Locale.getDefault());

	protected String mTableName;
	protected List<Class> mAuthorizedChildrenClass = new ArrayList<Class>();

	protected List<IAttribute> mAttributes = new ArrayList<IAttribute>();
	protected List<Entity> mChildren = new ArrayList<Entity>();
	protected List<Link> mLinks = new ArrayList<Link>();
	protected Entity mParent;
	protected int mId;

	public Entity() {
		mTableName = this.getClass().getSimpleName();
	}

	public List<Class> getAuthorizedChildrenClass() {
		return mAuthorizedChildrenClass;
	}

	public String getTableName() {
		return mTableName;
	}

	public void setParent(Entity parent) {
		this.mParent = parent;
	}

	public Entity getParent() {
		return mParent;
	}

	public int getId() {
		return this.mId;
	}

	public List<IAttribute> getAttributes() {
		return mAttributes;
	}

	public IAttribute getAttribute(String attrName) {
		IAttribute resAttr = null;
		for (IAttribute attribute : mAttributes) {
			if (attribute.getName().equals(attrName))
				resAttr = attribute;
		}
		return resAttr;
	}

	public List<Entity> getChildrenOfType(String className) {
		List<Entity> results = new ArrayList<Entity>();
		for (Entity child : mChildren) {
			if (child.getClass().getName().equals(className) && !results.contains(child)) {
				results.add(child);
			}
		}
		return results;
	}

	public Entity getChildOfType(String className, int id) {
		for (Entity child : getChildrenOfType(className)) {
			if (child.getId() == id)
				return child;
		}

		// Si pas de resultat
		return null;
	}

	public List<Entity> getChildren() {
		return mChildren;
	}

	private Link getLink(Entity dest) {
		Link lnk = null;

		for (Link l : mLinks) {
			if (l.getSource().equals(dest)) {
				lnk = l;
				break;
			}
		}

		return lnk;
	}

	public List<Link> getLinks() {
		return mLinks;
	}

	public void removeLink(Entity dest) {
		mLinks.remove(getLink(dest));
	}

	public void addLink(Entity dest) {
		if (getLink(dest) == null) {
			mLinks.add(new Link(this, dest));
		}
	}
}
