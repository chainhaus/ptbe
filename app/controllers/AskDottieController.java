package controllers;

import controllers.raven.BaseController;
import play.mvc.Result;

public class AskDottieController extends BaseController {

	
	public Result processRequestGet() {
		System.out.println("Get");
		return ok(returnData());
	}
	
	public Result processRequestPost() {
		System.out.println("Post");
		return ok(returnData());
	}
	
	public String returnData() {
		return response.replaceAll("SPEAK_MESSAGE", "There are 5 open houses in the area right now");
	}
	
	private static String response = "{		  \"version\": \"1.0\",		  \"response\": {		    \"outputSpeech\": {		      \"type\": \"SSML\",		      \"ssml\": \"<speak> SPEAK_MSG </speak>\"		    },		    \"card\": {		      \"content\": \"SPEAK_MSG\",		      \"title\": \"Space Facts\",		      \"type\": \"Simple\"		    },		    \"shouldEndSession\": true		  },		  \"sessionAttributes\": {}		}";
}
