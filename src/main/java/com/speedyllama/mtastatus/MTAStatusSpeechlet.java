package com.speedyllama.mtastatus;

import java.util.List;
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
			} if ("two".equalsIgnoreCase(train)) {
				train = "2";
			} if ("three".equalsIgnoreCase(train)) {
				train = "3";
			} if ("four".equalsIgnoreCase(train)) {
				train = "4";
			} if ("five".equalsIgnoreCase(train)) {
				train = "5";
			} if ("six".equalsIgnoreCase(train)) {
				train = "6";
			} if ("seven".equalsIgnoreCase(train)) {
				train = "7";
			}
			
			return responseText(statusMap.get(train));
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