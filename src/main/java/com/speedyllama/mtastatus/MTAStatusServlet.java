package com.speedyllama.mtastatus;

import javax.servlet.ServletException;

import com.amazon.speech.speechlet.servlet.SpeechletServlet;

public class MTAStatusServlet extends SpeechletServlet {
	private static final long serialVersionUID = 1892350072140640799L;
	
	@Override
	public void init() throws ServletException {
		setSpeechlet(new MTAStatusSpeechlet());
		super.init();
	}
}
