package com.speedyllama.mtastatus;

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
		return responseText("on intent");
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