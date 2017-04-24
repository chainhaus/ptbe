package models.ptbe;

import javax.persistence.Entity;

import com.avaje.ebean.Model.Finder;

import models.raven.BaseModel;

@Entity
public class Ad extends BaseModel {
	public static Finder<Long, Ad> find = new Finder<>(Ad.class);
	private String adImageURL;
	private String adClickURL;
	private boolean disabled;
	public String getAdImageURL() {
		return adImageURL;
	}
	public void setAdImageURL(String adImageURL) {
		this.adImageURL = adImageURL;
	}
	public String getAdClickURL() {
		return adClickURL;
	}
	public void setAdClickURL(String adClickURL) {
		this.adClickURL = adClickURL;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	
}
