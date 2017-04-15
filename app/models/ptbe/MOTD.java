package models.ptbe;

import java.util.List;

import javax.persistence.Entity;

import com.avaje.ebean.Model.Finder;

import models.raven.BaseModel;

@Entity
public class MOTD extends BaseModel {

	public static Finder<Long, MOTD> find = new Finder<>(MOTD.class);
	private String message;
	private boolean disabled = true;
	
	public static MOTD getCurrentMOTD() {
		List<MOTD> ms = find.where().eq("disabled",false).findList();
		if(ms==null)
			return null;
		MOTD m = ms.get(0);
		if(m==null)
			return null;
		return m;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
