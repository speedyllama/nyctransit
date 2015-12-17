package com.speedyllama.mtastatus;

import org.junit.Before;
import org.junit.Test;

public class TestStatusParser {
	private StatusParser parser;
	private XMLFetcher fetcher;
	
	@Before
	public void init() {
		this.parser = new StatusParser();
		this.fetcher = new TestFileXMLFetcher();
	}

	@Test
	public void test() throws MTAStatusException {
		//parser.parse(fetcher.fetchXML("a.txt"));
		//fail("Not yet implemented");
	}
}
