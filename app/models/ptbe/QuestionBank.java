package models.ptbe;

import javax.persistence.Entity;

import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.annotation.CacheStrategy;

import models.raven.BaseModel;

@CacheStrategy
@Entity
public class QuestionBank extends BaseModel {
	public static Finder<Long, QuestionBank> find = new Finder<>(QuestionBank.class);
	private String question;
	private String choice1;
	private String choice2;
	private String choice3;
	private String choice4;
	private String choice5;
	private boolean free;
	private boolean disabled;
	private int answer;
	
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getChoice1() {
		return choice1;
	}
	public void setChoice1(String choice1) {
		this.choice1 = choice1;
	}
	public String getChoice2() {
		return choice2;
	}
	public void setChoice2(String choice2) {
		this.choice2 = choice2;
	}
	public String getChoice3() {
		return choice3;
	}
	public void setChoice3(String choice3) {
		this.choice3 = choice3;
	}
	public String getChoice4() {
		return choice4;
	}
	public void setChoice4(String choice4) {
		this.choice4 = choice4;
	}

	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	public boolean isFree() {
		return free;
	}
	public void setFree(boolean free) {
		this.free = free;
	}
	public String getChoice5() {
		return choice5;
	}
	public void setChoice5(String choice5) {
		this.choice5 = choice5;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

}
