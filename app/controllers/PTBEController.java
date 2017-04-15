package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;

import controllers.raven.BaseAPIController;
import controllers.raven.BaseAPIController.GenericResponseJSON;
import forms.ptbe.TestResultForm;
import models.ptbe.MOTD;
import models.ptbe.QuestionBank;
import models.ptbe.TestResult;
import models.raven.AuthenticatedUser;
import play.Configuration;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;
import raven.forms.ChangePasswordForm;
import raven.forms.ResetPasswordForm;
import raven.forms.SignInForm;

public class PTBEController extends BaseAPIController {
	
	@Inject
	public PTBEController(Configuration conf) {
		super(conf);
		// TODO Auto-generated constructor stub
	}
	
	private String adImageURL;
	private String adClickURL;
	
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
	
	public Result getTestResult() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok("");
		GenericResponseJSON jsonError = new GenericResponseJSON();
		String email = getEmailKey();
		if(email==null || email.isEmpty()) {
			jsonError.errorCode = -1;
			jsonError.status = "Email key blank or null";
			return ok(Json.toJson(jsonError));
		}
		
		AuthenticatedUser u = AuthenticatedUser.findUserByEmail(email);
		
		if(u==null) {
			jsonError.errorCode = -3;
			jsonError.status = "User not registered";
			return ok(Json.toJson(jsonError));
		}
		if(u.isDisabled()) {
			jsonError.errorCode = -4;
			jsonError.status = "User account is disabled";
			return ok(Json.toJson(jsonError));
		}
		
		if(!u.isEmailVerified()) {
			jsonError.errorCode = -5;
			jsonError.status = "User email not verified";
			return ok(Json.toJson(jsonError));
		}
		
		List<TestResult> trs = TestResult.getTestHistoryByUser(u);
		List<TestHistoryJSON> json = new ArrayList<TestHistoryJSON>(trs.size());
		TestHistoryJSON tj = null;
		for(TestResult t : trs) {
			tj = new TestHistoryJSON();
			tj.date = t.getCreatedAt().toString();
			tj.testName = t.getTestName();
			tj.score = String.valueOf(t.getScore());
			json.add(tj);
		}
		return ok(Json.toJson(json));
	}
	
	public Result submitTestResult() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok("");
		
		GenericResponseJSON json = new GenericResponseJSON();
		String email = getEmailKey();
		if(email==null || email.isEmpty()) {
			json.errorCode = -1;
			json.status = "Email key blank or null";
			return ok(Json.toJson(json));
		}
		AuthenticatedUser u = AuthenticatedUser.findUserByEmail(email);
		
		if(u==null) {
			json.errorCode = -3;
			json.status = "User not registered";
			return ok(Json.toJson(json));
		}
		if(u.isDisabled()) {
			json.errorCode = -4;
			json.status = "User account is disabled";
			return ok(Json.toJson(json));
		}
		
		if(!u.isEmailVerified()) {
			json.errorCode = -5;
			json.status = "User email not verified";
			return ok(Json.toJson(json));
		}
		
		Form<TestResultForm> trf = ff.form(TestResultForm.class).bindFromRequest();
		if(trf.hasErrors()) {
			showFormBindingErrors(trf);		
			json.errorCode = -1;
			json.status = "Error";
			return ok(Json.toJson(json));
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
		json.errorCode = 0;
		json.status = "Test result recorded successfully";
		return ok(Json.toJson(json));
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
			return ok("no link UUID");
		AuthenticatedUser u = AuthenticatedUser.findUserByLinkUUID(linkUUID);
		if(u==null)
			return ok("User not found");
		if(u.isDisabled())
			return ok("User is disabled");
		return ok(views.html.ptbe.ViewResetPassword.render(linkUUID));
	}
	
	public Result submitResetPassword() {
		l("Password set credentials submitted ");
		Form<ResetPasswordForm> rpf = ff.form(ResetPasswordForm.class).bindFromRequest();
		if(rpf.hasErrors()) {
			showFormBindingErrors(rpf);
			return ok("error");
		}
		
		ResetPasswordForm rp = rpf.get();
		String linkUUID = rp.getLinkUUID();
		if(linkUUID==null || linkUUID.isEmpty())
			return ok("no link UUID");
		AuthenticatedUser u = AuthenticatedUser.findUserByLinkUUID(linkUUID);
		if(u==null)
			return ok("User not found");
		if(u.isDisabled())
			return ok("User is disabled");	
		u.setPassword(rp.getNewPassword());
		Ebean.update(u);
		return ok("Successfully updated password");
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
			return ok("");	
		l("Question bank retrieved by " + getEmailKey());
		QuestionBankResponseJSON json = new QuestionBankResponseJSON();
		List<QuestionBank> qb = QuestionBank.find.where().eq("free", true).and().eq("disabled", false).findList();
		List<QuestionBankItemResponseJSON> qbi = new ArrayList<QuestionBankItemResponseJSON>(qb.size());
		for(QuestionBank q : qb) {
			qbi.add(convert(q));
		}
		json.freeQuestions = qbi;
		json.version = getVersionResponseJSON();
		json.ad = new AdJSON();
		json.ad.adImageURL = adImageURL;
		json.ad.adClickURL = adClickURL;
		return ok(Json.toJson(json));
	}

	public Result getQuestionBankPremium() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok("");	
		l("Question premium bank retrieved by " + getEmailKey());
		QuestionBankResponseJSON json = new QuestionBankResponseJSON();
		List<QuestionBank> qb = QuestionBank.find.where().eq("disabled", false).findList();
		List<QuestionBankItemResponseJSON> qbi = new ArrayList<QuestionBankItemResponseJSON>(qb.size());
		for(QuestionBank q : qb) {
			qbi.add(convert(q));
		}
		json.allQuestions= qbi;
		json.version = getVersionResponseJSON();
		json.ad = new AdJSON();
		json.ad.adImageURL = adImageURL;
		json.ad.adClickURL = adClickURL;
		return ok(Json.toJson(json));
	}
	
	public Result submitAdURL() {
		Map<String, String[]> formValues = getFormValues();
		adImageURL = formValues.get("adImageURL")[0];
		adClickURL = formValues.get("adClickURL")[0];
		return redirect(routes.PTBEController.viewAddQuestion());
	}
	
	public Result viewUpdateAdURL() {
		return ok(views.html.ptbe.ViewUpdateAdURL.render());
	}
	
	public Result getAd() {
		if(!isValidAPIKey() || !isValidSessionKey())
			return ok("");	
		l("Ad retrieved by " + getEmailKey());
		AdJSON json = new AdJSON();
		json.adImageURL = adImageURL;
		json.adClickURL = adClickURL;
		return ok(Json.toJson(json));
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
		return qbi;
	}
	

	
	public class AdJSON {
		public String adImageURL;
		public String adClickURL;
		public String getAdImageURL() {
			return adImageURL;
		}
		public String getAdClickURL() {
			return adClickURL;
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
		public String testName;
		public String score;
		public String date;
	}
	
	public class QuestionBankItemResponseJSON {
		public long id;
		public String question;
		public String choice1;
		public String choice2;
		public String choice3;
		public String choice4;
		public String choice5;
		public int answer;
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
	}
	

	
	public class QuestionBankResponseJSON {
		private List<QuestionBankItemResponseJSON> freeQuestions;
		private List<QuestionBankItemResponseJSON> allQuestions;
		private VersionResponseJSON version;
		private AdJSON ad;
		
		public List<QuestionBankItemResponseJSON> getFreeQuestions() {
			return freeQuestions;
		}
		public List<QuestionBankItemResponseJSON> getAllQuestions() {
			return allQuestions;
		}
		public VersionResponseJSON getVersion() {
			return version;
		}
		public AdJSON getAd() {
			return ad;
		}
	}
	
	

}
