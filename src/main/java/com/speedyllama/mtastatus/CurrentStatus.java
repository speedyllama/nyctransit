package com.speedyllama.mtastatus;

import java.io.InputStream;
import java.util.Map;

public class CurrentStatus {

    private XMLFetcher xmlFetcher;
    private StatusParser statusParser;
    private String url;
    
    private Map<String, Status> statusMap;

	public CurrentStatus(XMLFetcher xmlFetcher, StatusParser statusParser, String url) {
		this.xmlFetcher = xmlFetcher;
		this.statusParser = statusParser;
		this.url = url;
	}
	
	public Status getStatus(String line) {
		if (statusMap == null) {
			return null;
		}
		return statusMap.get(line);
	}
	
	public void start() {
		Thread thread = new Thread() {
			public void run() {
				InputStream is;
				try {
					is = xmlFetcher.fetchXML(url);
					statusMap = statusParser.parse(is);
					Thread.sleep(1000 * 60);
				} catch (MTAStatusException mtae) {
					System.err.println("Error fetching status map.");
				} catch (InterruptedException ie) {
					System.err.println("Cannot fall asleep.");
				}
			}
		};
		thread.start();
	}
}
