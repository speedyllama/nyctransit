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
import com.amazon.speech.ui.SimpleCard;

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
		} else if ("AMAZON.YesIntent".equals(intent.getName())) {
			return responseYes(intent, session);
		} else if ("AMAZON.NoIntent".equals(intent.getName())) {
			return responseNo(intent, session);
		} else if ("AMAZON.HelpIntent".equals(intent.getName())) {
			return help(intent, session);
		} else if ("AMAZON.CancelIntent".equals(intent.getName()) || 
			"AMAZON.StopIntent".equals(intent.getName())) {
			return responseText("See you!");
		} else {
			return responseText("Sorry, I didn't get that. Please try again.", false);
		}
	}
	
	protected SpeechletResponse responseShortStatus(Intent intent, Session session) {
		String train = null;
		boolean understood = true;
		try {
			train = intent.getSlot("Train").getValue().toLowerCase();
		} catch (NullPointerException ne) {
			understood = false;
		}
		
		if (train == null) {
			understood = false;
		} else if ("one".equalsIgnoreCase(train)) {
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
		} else if ("8".equalsIgnoreCase(train) || "eight".equalsIgnoreCase(train)) { 
			train = "A";
		} else if ("see".equalsIgnoreCase(train)) { 
			train = "C";
		} else if ("i".equalsIgnoreCase(train)) { 
			train = "E";
		} else if ("ev".equalsIgnoreCase(train)) { 
			train = "F";
		} else if ("out".equalsIgnoreCase(train)) { 
			train = "L";
		} else if ("al".equalsIgnoreCase(train)) { 
			train = "L";
		} else if ("them".equalsIgnoreCase(train)) { 
			train = "M";
		} else if ("and".equalsIgnoreCase(train)) { 
			train = "N";
		} else if ("in".equalsIgnoreCase(train)) { 
			train = "N";
		} else if ("cute".equalsIgnoreCase(train)) { 
			train = "Q";
		} else if ("our".equalsIgnoreCase(train)) { 
			train = "R";
		} else if ("ass".equalsIgnoreCase(train)) { 
			train = "S";
		} else if ("the".equalsIgnoreCase(train)) { 
			train = "Z";
		} else if (train.startsWith("sta")) { 
			train = "SIR";
		} else if (train.startsWith("stockton")) { 
			train = "SIR";
		} else if (train.startsWith("t")) { // Alexa misunderstand Charlie as a "t"-begin word.
			// WARNING: do not shortcut other options.
			train = "C";
		} else if (!train.isEmpty()) {
			// Take the first character from the word.
			train = train.substring(0, 1).toUpperCase();
		} else {
			understood = false;
		}

		if (understood == false) {
			return responseText("Sorry, I didn't get that. " +
					"If you are asking about an alphabetical train, try use another word that begins with that alphabet." 
			);
		}
		
		Status statusObj = currentStatus.getStatus(train);
		if (statusObj == null) {
			return responseText("New York City transit does not have " + train + "-train. Please try again. ", false);
		} else {
			String responseText;
			if ("SIR".equals(train)) {
				responseText = "Staten Island Rail is in " + statusObj.getStatus().toString() + ". ";
			} else {
				responseText = train + "-train is in " + statusObj.getStatus().toString() + ". ";
			}
			String title = statusObj.getTitle();
			if (title != null) {
				responseText += title;
			}
			
			String detail = statusObj.getDetail();
			if (detail != null && !detail.isEmpty()) {
				// Extra dot for pause
				responseText += " . Do you want to hear details?";
				session.setAttribute(Constants.ATTR_PREVIOUS_STATE, "TRAIN_QUERY");
				session.setAttribute("train", train);
				return responseText(responseText, false);
			} else {
				return responseText(responseText);
			}
		}
	}
	
	protected SpeechletResponse responseYes(Intent intent, Session session) {
		String previousState = (String)session.getAttribute(Constants.ATTR_PREVIOUS_STATE);
		// Clear session state
		session.setAttribute(Constants.ATTR_PREVIOUS_STATE, null);

		if ("NATO".equals(previousState)) {
			session.setAttribute(Constants.ATTR_PREVIOUS_STATE, "NATO");
			String nato = "A for Alpha. " + 
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
					"Z for Zulu. ";
			SpeechletResponse response = responseText(
					"Here are NATO phonetic alphabets. " + 
					nato +
					"Also, you may say Shuttle for S train. " + 
					"And, please say the full name: Staten Island Rail, not S. I. R., for Staten Island Rail." +
					"I have also sent this list to your Alexa App. " + 
					"Do you want to hear that again?"
			, false);
			
			SimpleCard card = new SimpleCard();
			card.setTitle("NATO Phonetic Alphabets");
			card.setContent(nato);
			response.setCard(card);
			return response;
		} else if ("TRAIN_QUERY".equals(previousState)) {
			String train = (String)session.getAttribute("train");
	
			Status trainStatus = currentStatus.getStatus(train);
			SpeechletResponse response = responseText(
					"I have also sent the details to your Alexa App. " +
					trainStatus.getDetail()
			);
			SimpleCard card = new SimpleCard();
			card.setContent(trainStatus.getDetail());
			card.setTitle(train + " Train Status: " + trainStatus.getStatus().toString());
			response.setCard(card);
			return response;
		}
		return responseText("");
	}
	
	protected SpeechletResponse responseNo(Intent intent, Session session) {
		String previousState = (String)session.getAttribute(Constants.ATTR_PREVIOUS_STATE);
		session.setAttribute(Constants.ATTR_PREVIOUS_STATE, null);

		if ("NATO".equals(previousState)) {
			return responseText("Please ask me subway status now. Like: What is the status of seven?", false);
		} else if ("TRAIN_QUERY".equals(previousState)) {
			return responseText("See you!");
		}
		return responseText("");
	}
	
	private SpeechletResponse help(Intent intent, Session session) {
		session.setAttribute(Constants.ATTR_PREVIOUS_STATE, "NATO");
		return responseText("Hi! You can ask me New York City subway status. " + 
				"Like: What is the status of seven? " +
				"For alphabetical trains, like A, C, E trains, use a word that begins with that alphabet instead. " +
				"For example, for A train, say: What is the status of Alpha? NATO phonetic alphabets are recommended. " +
				"Do you want to hear a list of NATO phonetic alphabets?"
		, false);
	}
	
	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		return responseText("Hi! You can ask me New York City subway status. Like: What is the status of seven? Or, say \"help\" for help.", false);
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		// Do nothing
	}

	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		// Do nothing
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
