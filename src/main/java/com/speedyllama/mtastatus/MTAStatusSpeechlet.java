package com.speedyllama.mtastatus;

import java.util.Map;

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

	@Override
	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		StatusParser parser = new StatusParser(new PlainXMLFetcher(), "http://web.mta.info/status/serviceStatus.txt");
		try {
			Map<String, String> statusMap = parser.parse();
			
			String train = request.getIntent().getSlot("Train").getValue();
			if ("one".equalsIgnoreCase(train)) {
				train = "1";
			} else if ("two".equalsIgnoreCase(train)) {
				train = "2";
			} else if ("three".equalsIgnoreCase(train)) {
				train = "3";
			} else if ("four".equalsIgnoreCase(train)) {
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
				return responseText("Sorry, there is a problem understanding which train you want to ask about.");
			}

			String status = statusMap.get(train);
			if (status == null || status.isEmpty()) {
				return responseText("New York City transit does not have " + train + "-train.");
			} else {
				return responseText(statusMap.get(train));
			}
		} catch (MTAStatusException e) {
			throw new SpeechletException(e);
		}
	}

	@Override
	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		return responseText("on launch");
	}

	@Override
	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub
	}

	protected SpeechletResponse responseText(String text) {
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(text);
		SpeechletResponse response = new SpeechletResponse();
		response.setOutputSpeech(speech);
		return response;
	}
}
