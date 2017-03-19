import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import models.ptbe.QuestionBank;
import play.Application;
import play.Configuration;
import play.Environment;
import play.inject.ApplicationLifecycle;
import raven.BaseLifecycle;
import raven.services.ImageService;

@Singleton
public class Lifecycle extends BaseLifecycle {
	
	
	@Inject
	public Lifecycle(Environment env, Configuration conf, Application app, ApplicationLifecycle al, ImageService is) {
		super(env, conf, app, al, is);

		if (QuestionBank.find.findRowCount() == 0) {
			loadAndSave("questionbank.yml");
		}
			
		al.addStopHook(() -> {
			return CompletableFuture.completedFuture(null);
		});
	}

}
