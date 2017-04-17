package models.ptbe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import com.avaje.ebean.Model.Finder;
import com.avaje.ebean.annotation.CacheStrategy;

import models.raven.BaseModel;

@CacheStrategy
@Entity
public class QuestionBank extends BaseModel {
	public static Finder<Long, QuestionBank> find = new Finder<>(QuestionBank.class);
	private static Random seed = new Random(System.nanoTime());
	private String question;
	private List<String> choices;
	private String choice1;
	private String choice2;
	private String choice3;
	private String choice4;
	private String choice5;
	@ManyToOne
	private Topic topic;
	private boolean free;
	private boolean disabled;
	private int answer;
	
	public class Stem {
		public Stem (String choice){
			this.choice = choice;
		}
		public String choice;
		public boolean answer;
	}
	
	public void shuffleStem() {

		List<Stem> choices = new ArrayList<Stem>(5);
		choices.add(new Stem(choice1));
		choices.add(new Stem(choice2));
		choices.add(new Stem(choice3));
		choices.add(new Stem(choice4));
		choices.add(new Stem(choice5));
		choices.get(answer-1).answer=true;
		Collections.shuffle(choices,seed);
		this.choice1 = choices.get(0).choice;
		if(choices.get(0).answer) this.answer = 1;
		this.choice2 = choices.get(1).choice;
		if(choices.get(1).answer) this.answer = 2;
		this.choice3 = choices.get(2).choice;
		if(choices.get(2).answer) this.answer = 3;
		this.choice4 = choices.get(3).choice;
		if(choices.get(3).answer) this.answer = 4;
		this.choice5 = choices.get(4).choice;
		if(choices.get(4).answer) this.answer = 5;
		
	}
	
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
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public List<String> getChoices() {
		return choices;
	}

	public void setChoices(List<String> choices) {
		this.choices = choices;
	}
	
	public void addChoices(String choices) {
		this.addChoices(choices);
	}

}
