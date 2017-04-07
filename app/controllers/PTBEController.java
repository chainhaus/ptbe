package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.avaje.ebean.Ebean;

import controllers.raven.BaseAPIController;
import models.ptbe.QuestionBank;
import play.Configuration;
import play.data.Form;
import play.libs.Json;
import play.mvc.Result;

public class PTBEController extends BaseAPIController {
	
	@Inject
	public PTBEController(Configuration conf) {
		super(conf);
		// TODO Auto-generated constructor stub
	}
	
	private String adImageURL;
	private String adClickURL;
	

	
	public Result viewAddQuestion() {
		return ok(views.html.ptbe.ViewAddQuestion.render());
	}
	
	public Result viewRegisterUser() {
		return ok(views.html.ptbe.ViewRegisterUser.render());
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
