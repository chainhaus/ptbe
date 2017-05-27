package controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;

import controllers.raven.BaseAPIController;
import controllers.raven.BaseAPIController.GenericResponseJSON;
import models.raven.Ad;
import models.raven.AppRegistry;
import models.raven.AuthenticatedUser;
import models.raven.MOTD;
import models.raven.quiz.QuestionBank;
import models.raven.quiz.TestResult;
import models.raven.quiz.Topic;
import play.Configuration;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import raven.forms.ChangePasswordForm;
import raven.forms.ResetPasswordForm;
import raven.forms.SignInForm;
import raven.forms.quiz.TestResultForm;

public class PTBEController extends BaseAPIController {
	
	private List<AdJSON> ads = null;
	
	@Inject
	public PTBEController(Configuration conf) {
		super(conf);		
	}
	
	private void loadAdCache() {
		List<Ad> ads = Ad.find.where().eq("disabled",false).findList();
		AdJSON aj = null;
		this.ads = new ArrayList<AdJSON>(ads.size());
		for(Ad a : ads ) {
			aj = new AdJSON();
			aj.id = a.getId();
			aj.adClickURL = a.getAdClickURL();
			aj.adImageURL = a.getAdImageURL();
			this.ads.add(aj);
		}
	}
			
	public Result getTopics() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok(cachedErrorInvalidKey);
		List<Topic> ts = Topic.getCurrentAll();
		List<TopicResponseJSON> json = new ArrayList<TopicResponseJSON>(ts.size());
		TopicResponseJSON j = null;
		for (Topic t : ts) {
			j = new TopicResponseJSON();
			j.id = t.getId();
			j.topicName = t.getName();
			json.add(j);
		}
		return ok(Json.toJson(json));
	}
	
	public class TopicResponseJSON {
		public Long id;
		public String topicName;
		public String getTopicName() {
			return topicName;
		}
	}
	
	public Result getMOTD() {
		MOTD m = MOTD.getCurrentMOTD();
		MOTDResponseJSON json = new MOTDResponseJSON();
		if(m==null) {
			json.id = 0;
			return ok(Json.toJson(json));
		}
		json.id = m.getId();
		json.message = m.getMessage();
		return ok(Json.toJson(json));
	}
	
	public Result getTestHistory() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok(cachedErrorInvalidKey);
		String email = getEmailKey();
		if(email==null || email.isEmpty())
			return ok(cachedErrorInvalidKey);
		
		AppRegistry ar = getAppRegistry();
		if(ar==null)
			return ok(cachedAppNotFound);
		AuthenticatedUser u = ar.findUserByEmail(email);
		
		if(u==null) 
			return ok(cachedErrorUserNotFound);
		if(u.isDisabled()) 
			return ok(cachedErrorUserDisabled);	
		if(!u.isEmailVerified())
			return ok(cachedErrorEmailNotVerified);
		
		
		List<TestResult> trs = TestResult.getTestHistoryByUser(u);
		List<TestHistoryJSON> json = new ArrayList<TestHistoryJSON>(trs.size());
		TestHistoryJSON tj = null;
		for(TestResult t : trs) {	
			tj = new TestHistoryJSON();
			tj.id = t.getId();
			tj.date = t.getCreatedAt().toString();
			tj.testName = t.getTestName();
			tj.score = t.getScore();
			json.add(tj);
		}
		return ok(Json.toJson(json));
	}
		
	public Result submitTestResult() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok(cachedErrorInvalidKey);
		
		String email = getEmailKey();
		if(email==null || email.isEmpty())
			return ok(cachedErrorInvalidKey);
		
		AppRegistry ar = getAppRegistry();
		if(ar==null)
			return ok(cachedAppNotFound);
		AuthenticatedUser u = ar.findUserByEmail(email);
		
		if(u==null) 
			return ok(cachedErrorUserNotFound);
		if(u.isDisabled()) 
			return ok(cachedErrorUserDisabled);
		
		
		if(!u.isEmailVerified())
			return ok(cachedErrorEmailNotVerified);
		
		Form<TestResultForm> trf = ff.form(TestResultForm.class).bindFromRequest();
		if(trf.hasErrors()) {
			showFormBindingErrors(trf);		
			return ok(cachedErrorFormBinding);
		}
		
		TestResultForm tr = trf.get();
		TestResult t = new TestResult();
		t.setAnsweredCorrect(tr.getAnsweredCorrect());
		t.setTestName(tr.getTestName());
		t.setTotalQuestions(tr.getTotalQuestions());
		double correct = tr.getAnsweredCorrect();
		double total = tr.getTotalQuestions();
		double score = correct / total;
		t.setScore(score);
		t.setU(u);
		Ebean.save(t);
		return ok(cachedSuccess);
	}
	// TODO refactor methods into base
	
	public Result viewAddQuestion() {
		return ok(views.html.ptbe.ViewAddQuestion.render());
	}
	
	public Result viewRegisterUser() {
		return ok(views.html.ptbe.ViewRegisterUser.render());
	}
	
	public Result viewVersion() {
		return ok(views.html.ptbe.ViewUpdateVersion.render());
	}
	
	public Result viewResetPassword() {
		String linkUUID = request().getQueryString("linkuuid");
		if(linkUUID==null || linkUUID.isEmpty())
			return ok(cachedErrorNoLinkUUID);
		AppRegistry ar = getAppRegistry();
		if(ar==null)
			return ok(cachedAppNotFound);
		AuthenticatedUser u = ar.findUserByLinkUUID(linkUUID);
		if(u==null)
			return ok(cachedErrorUserNotFound);
		if(u.isDisabled())
			return ok(cachedErrorUserDisabled);
		return ok(views.html.ptbe.ViewResetPassword.render(linkUUID));
	}
	
	public Result submitResetPassword() {
		l("Password set credentials submitted ");
		Form<ResetPasswordForm> rpf = ff.form(ResetPasswordForm.class).bindFromRequest();
		if(rpf.hasErrors()) {
			showFormBindingErrors(rpf);
			return ok(cachedErrorFormBinding);
		}
		
		ResetPasswordForm rp = rpf.get();
		String linkUUID = rp.getLinkUUID();
		if(linkUUID==null || linkUUID.isEmpty())
			return ok(cachedErrorNoLinkUUID);
		AppRegistry ar = getAppRegistry();
		if(ar==null)
			return ok(cachedAppNotFound);
		AuthenticatedUser u = ar.findUserByLinkUUID(linkUUID);
		if(u==null)
			return ok(cachedErrorUserNotFound);
		if(u.isDisabled())
			return ok(cachedErrorUserDisabled);	
		u.setPassword(rp.getNewPassword());
		Ebean.update(u);
		return ok(cachedSuccess);
	}



	public Result submitAddQuestion() {
		Form<QuestionBank> qbf = ff.form(QuestionBank.class).bindFromRequest();
		if(qbf.hasErrors()) {
			showFormBindingErrors(qbf);
		} else {
			QuestionBank qb = new QuestionBank();			
			qb.setQuestion(qbf.get().getQuestion());
			qb.setChoice1(qbf.get().getChoice1());
			qb.setChoice2(qbf.get().getChoice2());
			qb.setChoice3(qbf.get().getChoice3());
			qb.setChoice4(qbf.get().getChoice4());
			qb.setChoice5(qbf.get().getChoice5());
			qb.setAnswer(qbf.get().getAnswer());
			qb.setDisabled(qbf.get().isDisabled());
			qb.setFree(qbf.get().isFree());
			Ebean.save(qb);
		}
		
		return redirect(routes.PTBEController.viewAddQuestion());
	}
	
	public Result getQuestionBank() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok(cachedErrorInvalidKey);	
		String email = getEmailKey();
		l("Question bank retrieved by " + email);	
		AppRegistry ar = getAppRegistry();
		if(ar==null)
			return ok(cachedAppNotFound);
		List<QuestionBank> qb = null;
		if(!email.equals("NONE")) 
			qb = QuestionBank.getFreeAndRegisteredQuestions(ar);
		else
			qb = QuestionBank.getFreeOnlyQuestions(ar);
	
		QuestionBankResponseJSON json = new QuestionBankResponseJSON();
		List<QuestionBankItemResponseJSON> qbi = new ArrayList<QuestionBankItemResponseJSON>(qb.size());
		for(QuestionBank q : qb) {
			q.shuffleStem();
			qbi.add(convert(q));
		}
		json.statusCode = 0;
		json.questions = qbi;
		return ok(Json.toJson(json));
	}

	public Result getQuestionBankPremium() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok(cachedErrorInvalidKey);	
		//if(!isPurchasedInApp())
			//return ok(cachedErrorUnauthorized);

		l("Question premium bank retrieved by " + getEmailKey());
		QuestionBankResponseJSON json = new QuestionBankResponseJSON();
		List<QuestionBank> qb = QuestionBank.getAllCurrent();
		List<QuestionBankItemResponseJSON> qbi = new ArrayList<QuestionBankItemResponseJSON>(qb.size());
		for(QuestionBank q : qb) {
			q.shuffleStem();
			qbi.add(convert(q));
		}
		json.questions = qbi;
		return ok(Json.toJson(json));
	}
	
	
	public Result submitAdURL() {
		Map<String, String[]> formValues = getFormValues();
		Ad ad = new Ad();
		ad.setAdImageURL(formValues.get("adImageURL")[0]);
		ad.setAdClickURL(formValues.get("adClickURL")[0]);
		Ebean.save(ad);
		loadAdCache();
		return redirect(routes.PTBEController.viewAddQuestion());
	}
	
	public Result viewUpdateAdURL() {
		return ok(views.html.ptbe.ViewUpdateAdURL.render());
	}
	
	public Result getAd() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok(cachedErrorInvalidKey);	
		if (this.ads==null) loadAdCache();
		l("Ads retrieved by " + getEmailKey());
		return ok(Json.toJson(this.ads));
	}
	
	private QuestionBankItemResponseJSON convert(QuestionBank qb) {
		QuestionBankItemResponseJSON qbi = new QuestionBankItemResponseJSON();
		qbi.id = qb.getId();
		qbi.question = qb.getQuestion();
		qbi.answer = qb.getAnswer();
		qbi.choice1 = qb.getChoice1();
		qbi.choice2 = qb.getChoice2();
		qbi.choice3 = qb.getChoice3();
		qbi.choice4 = qb.getChoice4();
		qbi.choice5 = qb.getChoice5();
		qbi.topicId = qb.getTopic().getId();
		qbi.topicName = qb.getTopic().getName();
		if(qb.getSolutionDescription()==null)
			qbi.solutionDescription = "";
		else
			qbi.solutionDescription = qb.getSolutionDescription();
		return qbi;
	}
		
	public class AdJSON {
		public long id;
		public String adImageURL;
		public String adClickURL;
		public String getAdImageURL() {
			return adImageURL;
		}
		public String getAdClickURL() {
			return adClickURL;
		}
		public long getId() {
			return id;
		}
	}
	
	public class MOTDResponseJSON {
		public long id;
		public String message;
		public long getId() {
			return id;
		}
		public String getMessage() {
			return message;
		}
	}
	
	public class TestHistoryJSON {
		public Long id;
		public String testName;
		public double score;
		public String date;
		public int statusCode;
		public String status;
		public String getTestName() {
			return testName;
		}
		public double getScore() {
			return score;
		}
		public String getDate() {
			return date;
		}
		public int getStatusCode() {
			return statusCode;
		}
		public String getStatus() {
			return status;
		}
		public Long getId() {
			return id;
		}
	}
	
	public class QuestionBankItemResponseJSON {
		public long id;
		public String question;
		public String choice1;
		public String choice2;
		public String choice3;
		public String choice4;
		public String choice5;
		public Long topicId;
		public String topicName;
		public int answer;
		public String solutionDescription;
		
		public String getQuestion() {
			return question;
		}
		public String getChoice1() {
			return choice1;
		}
		public String getChoice2() {
			return choice2;
		}
		public String getChoice3() {
			return choice3;
		}
		public String getChoice4() {
			return choice4;
		}
		public String getChoice5() {
			return choice5;
		}
		public int getAnswer() {
			return answer;
		}
		public long getId() {
			return id;
		}
		public Long getTopicId() {
			return topicId;
		}
		public String getTopicName() {
			return topicName;
		}
		public String getSolutionDescription() {
			return solutionDescription;
		}
	}
	

	public class QuestionBankResponseJSON {
		private List<QuestionBankItemResponseJSON> questions;
		public int statusCode;
		public List<QuestionBankItemResponseJSON> getQuestions() {
			return questions;
		}

	}
}
