import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.avaje.ebean.Ebean;

import models.raven.AppRegistry;
import models.raven.AuthenticatedUser;
import models.raven.quiz.QuestionBank;
import models.raven.quiz.Topic;
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
		if(QuestionBank.find.findRowCount() < 1) {
			Logger.info("Loading question bank");
			try {
				Reader in = new FileReader("conf/questions2.csv");
				Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
				QuestionBank q = null;
				AppRegistry ar = AppRegistry.getAppByAPIKey("8df7340a493240ff889060c9f26081bc");
				for (CSVRecord r : records) {
						if(r.get("free").contains("1")) {
							q = new QuestionBank();
							System.out.println("Free - " + r.getRecordNumber());
							q.setQuestion("Free - " + r.get("question"));
							q.setChoice1(r.get("choice1"));
							q.setChoice2(r.get("choice2"));
							q.setChoice3(r.get("choice3"));
							q.setChoice4(r.get("choice4"));
							q.setChoice5(r.get("choice5"));
							q.setAnswer(Integer.valueOf(r.get("answer")));
							q.setDifficulty(Integer.valueOf(r.get("difficulty")));
							q.setTopic(Topic.findTopicByName(r.get("topic")));
							String free = r.get("free");
							if(free.contains("1"))
								q.setFree(true);
							String registered = r.get("registered");
							if(registered.contains("1"))
								q.setRegistered(true);
							q.addApp(ar);
							Ebean.save(q);
						}
				}
				
				in = new FileReader("conf/questions2.csv");
				records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
				ar = AppRegistry.getAppByAPIKey("78f64513888c4ea6a8b6f04162e60bba");
				
				for (CSVRecord r : records) {
					q = new QuestionBank();
					System.out.println("T1 - " + r.getRecordNumber());
					q.setQuestion("T1 - " + r.get("question"));
					q.setChoice1(r.get("choice1"));
					q.setChoice2(r.get("choice2"));
					q.setChoice3(r.get("choice3"));
					q.setChoice4(r.get("choice4"));
					q.setChoice5(r.get("choice5"));
					q.setAnswer(Integer.valueOf(r.get("answer")));
					q.setDifficulty(Integer.valueOf(r.get("difficulty")));
					q.setTopic(Topic.findTopicByName(r.get("topic")));
					String free = r.get("free");
					if(free.contains("1"))
						q.setFree(true);
					String registered = r.get("registered");
					if(registered.contains("1"))
						q.setRegistered(true);
					q.addApp(ar);
					Ebean.save(q);
				}
				
				in = new FileReader("conf/questions2.csv");
				records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
				ar = AppRegistry.getAppByAPIKey("b5fa48e9ce2a43c0a1e16601dc3ad649");
				for (CSVRecord r : records) {
					q = new QuestionBank();
					System.out.println("T2 - " + r.getRecordNumber());
					q.setQuestion("T2 - " + r.get("question"));
					q.setChoice1(r.get("choice1"));
					q.setChoice2(r.get("choice2"));
					q.setChoice3(r.get("choice3"));
					q.setChoice4(r.get("choice4"));
					q.setChoice5(r.get("choice5"));
					q.setAnswer(Integer.valueOf(r.get("answer")));
					q.setDifficulty(Integer.valueOf(r.get("difficulty")));
					q.setTopic(Topic.findTopicByName(r.get("topic")));
					String free = r.get("free");
					if(free.contains("1"))
						q.setFree(true);
					String registered = r.get("registered");
					if(registered.contains("1"))
						q.setRegistered(true);
					q.addApp(ar);
					Ebean.save(q);
				}
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Logger.info("Load complete");
		}
		
		
		/*
		// load questions
		Map<Integer,String[]> questions = FileParser.parseCSV("conf/questions.csv");
		Set<Integer> keys = questions.keySet();
		String[] question = null;
		QuestionBank qb = null;
		for(Integer i : keys) {
			question = questions.get(i);
			qb = new QuestionBank();
			System.out.println("0 " + question[0]);
			qb.setQuestion(question[0]);
			System.out.println("1 " + question[1]);
			qb.setChoice1(question[1]);
			System.out.println("2 " + question[2]);
			qb.setChoice2(question[2]);
			System.out.println("3 " + question[3]);
			qb.setChoice3(question[3]);
			System.out.println("4 " + question[4]);
			qb.setChoice4(question[4]);
			System.out.println("5 " + question[5]);
			qb.setChoice5(question[5]);
			System.out.println("6 " + question[6]);
			qb.setAnswer(Integer.valueOf(question[6]));
			System.out.println("7 " + question[7]);
			qb.setDifficulty(Integer.valueOf(question[7]));
			System.out.println("8 " + question[8]);
			qb.setTopic(Topic.findTopicByName(question[8]));
			Ebean.save(qb);
			System.out.println(question[1]);
		}
	*/
		for(AuthenticatedUser u : AuthenticatedUser.getAllCurrentAuthenticatedUsers()) {
			Logger.info("email " + u.getEmail() + " " + u.getSessionUUID());
		}
		al.addStopHook(() -> {
			return CompletableFuture.completedFuture(null);
		});
	}

}
