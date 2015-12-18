package com.speedyllama.mtastatus;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class TestStatusParser {
	private StatusParser parser;
	private XMLFetcher fetcher;
	
	@Before
	public void init() {
		this.parser = new StatusParser();
		this.fetcher = new PlainXMLFetcher();
	}

	@Test
	public void test() throws MTAStatusException {
		Map<String, Status> statusMap = parser.parse(fetcher.fetchXML(getClass().getResource("/xmls/20151217T2257.xml").toString()));
		
	}
}
