package com.jlb.tools.metamodel;

public class Link {

	private String mName;
	private Entity mSource;
	private Entity mDestination;

	public Link(Entity src, Entity dest) {
		mName = "Link" + src.getClass().getSimpleName() + dest.getClass().getSimpleName();
		mSource = src;
		mDestination = dest;
	}

	public Entity getSource() {
		return mSource;
	}

	public Entity getDestination() {
		return mDestination;
	}

	public String getName() {
		return mName;
	}

	@Override
	public String toString() {
		return "Link " + mName + " - " + mSource + " --> " + mDestination;
	}
}
