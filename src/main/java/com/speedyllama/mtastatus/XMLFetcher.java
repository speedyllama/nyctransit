package com.speedyllama.mtastatus;

import java.io.InputStream;

interface XMLFetcher {
	InputStream fetchXML(String url) throws MTAStatusException;
}
