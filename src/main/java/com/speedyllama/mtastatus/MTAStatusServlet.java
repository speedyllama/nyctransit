package com.speedyllama.mtastatus;

import javax.servlet.ServletException;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class MTAStatusServlet extends SpeechletServlet {
	private static final long serialVersionUID = 1892350072140640799L;
	private MTAStatusSpeechlet speechlet;
	
	@Override
	public void init() throws ServletException {
		this.speechlet = new MTAStatusSpeechlet();
		super.init();
	}

	@Override
	public Speechlet getSpeechlet() {
		return this.speechlet;
	}

}
