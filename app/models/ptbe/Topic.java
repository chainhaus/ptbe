package models.ptbe;

import java.util.List;

import javax.persistence.Entity;

import com.avaje.ebean.Model.Finder;

import models.raven.BaseModel;
@Entity
public class Topic extends BaseModel {
	public static Finder<Long, Topic> find = new Finder<>(Topic.class);
	private String name;
	private boolean disabled;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public static List<Topic> getCurrentAll() {
		return find.where().eq("disabled", false).findList();
	}

}
