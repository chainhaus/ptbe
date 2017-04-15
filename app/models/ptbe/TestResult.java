package models.ptbe;

import java.util.List;

import javax.persistence.ManyToOne;

import com.avaje.ebean.Model.Finder;

import models.raven.AuthenticatedUser;
import models.raven.BaseModel;

public class TestResult extends BaseModel {
	public static Finder<Long, TestResult> find = new Finder<>(TestResult.class);
	
	@ManyToOne
	private AuthenticatedUser u;
	private String testName;
	private int answeredCorrect;
	private int totalQuestions;
	private double score;

	public static List<TestResult> getTestHistoryByUser (AuthenticatedUser u) {
		List<TestResult> trs = find.where().eq("u", u).order().desc("id").findList();
		return trs;
	}
	
	public AuthenticatedUser getU() {
		return u;
	}
	public void setU(AuthenticatedUser u) {
		this.u = u;
	}
	public String getTestName() {
		return testName;
	}
	public void setTestName(String testName) {
		this.testName = testName;
	}
	public int getAnsweredCorrect() {
		return answeredCorrect;
	}
	public void setAnsweredCorrect(int answeredCorrect) {
		this.answeredCorrect = answeredCorrect;
	}
	public int getTotalQuestions() {
		return totalQuestions;
	}
	public void setTotalQuestions(int totalQuestions) {
		this.totalQuestions = totalQuestions;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
		
}
	
