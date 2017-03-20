package controllers;

import controllers.raven.BaseAPIController;
import controllers.raven.BaseAuthentication;
import controllers.raven.LoginForm;
import controllers.raven.BaseAuthentication.LoginResponseJSON;
import models.raven.AuthenticatedUser;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import raven.security.SessionKeys;

// TODO refactor into raven
public class Authentication extends BaseAuthentication {
	

    public Result authenticate() {	
    	LoginResponseJSON json = new LoginResponseJSON();

    	Form<LoginForm> loginForm = ff.form(LoginForm.class).bindFromRequest();
    	
    	if(super.authenticate(loginForm)) {
    		AuthenticatedUser u = AuthenticatedUser.findUserByEmail(loginForm.get().getEmail());
    		if (u.isEmailVerified()) {
    			json.message="Success";
    			json.sessionUUID = u.getSessionUUID();
    		} else
    			json.message="Email unverified";
    	}
    	else
    		json.message = "Fail";
    	
    	return ok(Json.toJson(json));
    }
    
}




