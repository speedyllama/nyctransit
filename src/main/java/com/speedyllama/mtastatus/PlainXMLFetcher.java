package com.speedyllama.mtastatus;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PlainXMLFetcher implements XMLFetcher {

	@Override
	public InputStream fetchXML(String url) throws MTAStatusException {
		try {
			URL urlObj = new URL(url);
			URLConnection connection = urlObj.openConnection();
			return connection.getInputStream();
		} catch (MalformedURLException me) {
			throw new MTAStatusException(me);
		} catch (IOException ie) {
			throw new MTAStatusException(ie);
		}
	}

}
