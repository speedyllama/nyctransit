package com.speedyllama.mtastatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class TestFileXMLFetcher implements XMLFetcher {

	@Override
	public InputStream fetchXML(String filepath) throws MTAStatusException {
		try {
			File file = new File(filepath);
			InputStream is = new FileInputStream(file);
			return is;
		} catch (FileNotFoundException e) {
			throw new MTAStatusException(e);
		}
	}

}
