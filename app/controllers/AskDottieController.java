package controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import controllers.raven.BaseController;
import play.mvc.Result;

public class AskDottieController extends BaseController {
	private List<String> responses = new ArrayList<String>(10);

	public AskDottieController() {
		responses.add("There is an open house at 399 Park Avenue, Room 1213. It is a 3 bedroom 2 bathroom and the ask is 5 point 2 million.");
		responses.add("There are 3 open houses in a 1 mile radius");
		responses.add("Douglis Ellyman is hosting a cocktail party at 555 Fifth Avenue, stop by today at 7pm");
	}
	
	
	public Result processRequestGet() {
		System.out.println("Get");
		return ok(returnData());
	}
	
	public Result processRequestPost() {
		System.out.println("Post");
		return ok(returnData());
	}
	
	public String returnData() {
		return response.replaceAll("SPEAK_MSG", responses.get(ThreadLocalRandom.current().nextInt(0, 3)));
	}
	
	private static String response = "{		  \"version\": \"1.0\",		  \"response\": {		    \"outputSpeech\": {		      \"type\": \"SSML\",		      \"ssml\": \"<speak> SPEAK_MSG </speak>\"		    },		    \"card\": {		      \"content\": \"SPEAK_MSG\",		      \"title\": \"Space Facts\",		      \"type\": \"Simple\"		    },		    \"shouldEndSession\": true		  },		  \"sessionAttributes\": {}		}";
}
