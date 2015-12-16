package com.speedyllama.mtastatus;

import java.io.InputStream;
import java.util.Map;

public class CurrentStatus {

    private XMLFetcher xmlFetcher;
    private StatusParser statusParser;
    private String url;
    
    private Map<String, String> statusMap;

	public CurrentStatus(XMLFetcher xmlFetcher, StatusParser statusParser, String url) {
		this.xmlFetcher = xmlFetcher;
		this.statusParser = statusParser;
		this.url = url;
	}
	
	public String getStatus(String line) {
		if (statusMap == null) {
			return null;
		}
		return statusMap.get(line);
	}
	
	public void start() throws MTAStatusException {
		while (true) {
			InputStream is = xmlFetcher.fetchXML(url);
			statusMap = statusParser.parse(is);
			try {
				Thread.sleep(1000 * 60);
			} catch (InterruptedException e) {
				throw new MTAStatusException(e);
			}
		}
	}
}
