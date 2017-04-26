import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.raven.AuthenticatedUser;
import models.raven.quiz.QuestionBank;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Logger;
import play.inject.ApplicationLifecycle;
import raven.BaseLifecycle;
import raven.services.ImageService;

@Singleton
public class Lifecycle extends BaseLifecycle {
	
	
	@Inject
	public Lifecycle(Environment env, Configuration conf, Application app, ApplicationLifecycle al, ImageService is) {
		super(env, conf, app, al, is);

		if (QuestionBank.find.findRowCount() < 1) {
			loadAndSave("questionbank.yml");
		} else {
			Logger.info("Not loading YML");
		}
		

		for(AuthenticatedUser u : AuthenticatedUser.getAllCurrentAuthenticatedUsers()) {
			Logger.info("email " + u.getEmail() + " " + u.getSessionUUID());
		}
		al.addStopHook(() -> {
			return CompletableFuture.completedFuture(null);
		});
	}

}
