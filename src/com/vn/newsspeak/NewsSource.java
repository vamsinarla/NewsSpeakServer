package com.vn.newsspeak;

import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable
public class NewsSource {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key; 
	
	@Persistent
	private String title;
	
	@Persistent
	private String type;

	@Persistent
	private boolean hasCategories;
	
	@Persistent
	private boolean preferred;

	@Persistent
	private ArrayList<String> categories;
	
	@Persistent
	private ArrayList<String> categoryUrls;
	
	@Persistent
	private String defaultUrl;

	public NewsSource(String name, boolean hasCategories, ArrayList<String> categories, ArrayList<String> categoryUrls, String defaultUrl,
		String type, boolean preferred) {
		
		this.title = name;
		this.hasCategories = hasCategories;
		this.categories = categories;
		this.categoryUrls = categoryUrls;
		this.defaultUrl = defaultUrl;
		this.type = type;
		this.preferred = preferred;
	}
	
	public NewsSource() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isPreferred() {
		return preferred;
	}

	public void setPreferred(boolean preferred) {
		this.preferred = preferred;
	}
	
	public boolean isHasCategories() {
		return hasCategories;
	}

	public void setHasCategories(boolean hasCategories) {
		this.hasCategories = hasCategories;
	}

	public ArrayList<String> getCategories() {
		return categories;
	}

	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	public ArrayList<String> getCategoryUrls() {
		return categoryUrls;
	}

	public void setCategoryUrls(ArrayList<String> categoryUrls) {
		this.categoryUrls = categoryUrls;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}
}
