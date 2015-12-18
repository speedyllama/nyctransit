package com.speedyllama.mtastatus;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;

public class MTAStatusSpeechlet implements Speechlet {
	
	private CurrentStatus currentStatus;

	public MTAStatusSpeechlet() {
		this.currentStatus = new CurrentStatus(new PlainXMLFetcher(), new StatusParser(), Constants.STATUS_URL);
		currentStatus.start();
	}

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		Intent intent = request.getIntent();
		
		if (Constants.INTENT_QUERY_TRAIN_STATUS.equals(intent.getName())) {
			return responseShortStatus(intent, session);
		} else if (Constants.INTENT_QUERY_STATUS_DETAIL.equals(intent.getName())) {
			return responseDetailStatus(intent, session);
		} else {
			return responseText("Sorry, I didn't get it. Let's try again.");
		}
	}
	
	protected SpeechletResponse responseShortStatus(Intent intent, Session session) {
		String train = intent.getSlot("Train").getValue();
		if ("one".equalsIgnoreCase(train)) {
			train = "1";
		// Alexa is not smart one number "two".
		} else if ("two".equalsIgnoreCase(train) ||
				"to".equalsIgnoreCase(train) ||
				"too".equalsIgnoreCase(train)) {
			train = "2";
		} else if ("three".equalsIgnoreCase(train)) {
			train = "3";
		// Alexa is not smart one number "four".
		} else if ("four".equalsIgnoreCase(train) || 
				"for".equalsIgnoreCase(train)) {
			train = "4";
		} else if ("five".equalsIgnoreCase(train)) {
			train = "5";
		} else if ("six".equalsIgnoreCase(train)) {
			train = "6";
		} else if ("seven".equalsIgnoreCase(train)) {
			train = "7";
		} else if (train != null && !train.isEmpty()) {
			// Take the first character from the word.
			train = train.substring(0, 1).toUpperCase();
		} else {
			return responseText("Sorry, there is a problem understanding which train you want to ask about." +
					"If you are asking about an alphabetical train, try use another word that begins with that alphabet. " + 
					"Like: What is the status of Echo train?");
		}

		Status statusObj = currentStatus.getStatus(train);
		if (statusObj == null) {
			return responseText("New York City transit does not have " + train + "-train.");
		} else {
			String responseText = train + "-train is in " + statusObj.getStatus().toString() + ". ";
			String title = statusObj.getTitle();
			if (title != null) {
				responseText += title;
			}
			
			String detail = statusObj.getDetail();
			if (detail != null && !detail.isEmpty()) {
				responseText += "Do you want to hear details?";
				session.setAttribute("train", train);
				return responseText(responseText, false);
			} else {
				return responseText(responseText);
			}
		}
	}
	
	protected SpeechletResponse responseDetailStatus(Intent intent, Session session) {
		if (true == (boolean)session.getAttribute("nato")) {
			if (isPositive(intent.getSlot("Answer").getValue())) {
				return responseText(
						"A for Alpha. " + 
						"B for Bravo. " + 
						"C for Charlie. " + 
						"D for Delta. " + 
						"E for Echo. " + 
						"F for Foxtrot. " + 
						"G for Golf. " + 
						"J for Juliette. " + 
						"L for Lima. " + 
						"M for Mike. " + 
						"N for November. " + 
						"Q for Quebec. " + 
						"R for Romeo. " + 
						"S for Sierra. " + 
						"Z for Zulu. " + 
						"Also, you may say Shuttle for S train. " + 
						"Do you want to hear that again?"
				, false);
			} else {
				return responseText("Please ask me subway status now. Like: What is the status of seven train?", false);
			}
		}

		String train = (String)session.getAttribute("train");

		String answer = intent.getSlot("Answer").getValue().toLowerCase();
		if (isPositive(answer)) {
			return responseText(currentStatus.getStatus(train).getDetail());
		} else {
			return responseText("See you!");
		}
	}
	
	private boolean isPositive(String answer) {
		if (answer.startsWith("y") || 
				answer.equals("ok") ||
				answer.equals("go ahead")) {
			return true;
		}
		return false;
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		session.setAttribute("nato", true);
		return responseText("Hi! You can ask me N.Y.C subway status. " + 
				"Like: What is the status of seven train? " +
				"For alphabetical trains, like A, C, E trains, use a word that begins with that alphabet instead. " +
				"Like: What is the status of Echo train? NATO phonetic alphabets are recommended. " +
				"Do you want to hear a list of NATO phonetic alphabets?"
		, false);
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
	}

	protected SpeechletResponse responseText(String text, boolean shouldEndSession) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(text);
		SpeechletResponse response = new SpeechletResponse();
		response.setOutputSpeech(speech);
		response.setShouldEndSession(shouldEndSession);
		return response;
	}
	
	protected SpeechletResponse responseText(String text) {
		return responseText(text, true);
	}
}
